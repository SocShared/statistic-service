package ml.socshared.bstatistics.service.impl;

import lombok.extern.slf4j.Slf4j;
import ml.socshared.bstatistics.config.Constants;
import ml.socshared.bstatistics.domain.db.*;
import ml.socshared.bstatistics.domain.object.*;
import ml.socshared.bstatistics.exception.HttpIllegalBodyRequest;
import ml.socshared.bstatistics.exception.HttpNotFoundException;
import ml.socshared.bstatistics.repository.GroupInfoRepository;
import ml.socshared.bstatistics.repository.GroupRepository;
import ml.socshared.bstatistics.repository.PostInfoRepository;
import ml.socshared.bstatistics.repository.PostRepository;
import ml.socshared.bstatistics.service.StatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.tablesaw.aggregate.AggregateFunctions;
import tech.tablesaw.api.*;

import java.time.*;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;



@Slf4j
@Service
public class StatServiceImpl implements StatService {

    private GroupInfoRepository groupInfoRep;
    private GroupRepository groupRep;
    private PostInfoRepository postInfoRep;
    private PostRepository postRep;

    @Autowired
    public StatServiceImpl(GroupInfoRepository gir, GroupRepository gr,
                           PostInfoRepository pir, PostRepository pr) {
        this.groupInfoRep = gir;
        this.groupRep = gr;
        this.postInfoRep = pir;
        this.postRep = pr;
    }


    /**
     * Возвращает временной ряд онлайна по заданной группе и заданному периоду. Если заданный период равнаяется суткам,
     * то шаг временного ряда равняется шагу данных хранимих в БД.
     * Если заданный период больше суток, то шаг равняется суткам.
     * Для получения временного рядя за одни сутки требуется, чтобы begin == end
     * @param groupId идетификатор группы, для которой вытаскивать временной ряд
     * @param begin начало периода
     * @param end конец периода
     * @return времянной ряд
     */
    @Override
    public TimeSeries<Integer> getOnlineByTime(String groupId, LocalDate begin, LocalDate end) {
        Util.checkDate(begin, end);
        List<GroupInfo> giList = groupInfoRep.findBetweenDates(groupId, begin, end);
        if (giList.isEmpty()) {
            throw new HttpNotFoundException("Not found information on group by set period");
        }
       List<Integer> data = applyGroupValuesByDay(giList, GroupInfo::getOnline, Integer::sum, ()->0,
                                                  GroupInfo::getTimeAddedRecord, begin, end);


        TimeSeries<Integer> res = new TimeSeries<>();
        res.setData(data);
        res.setStep((begin.until(end).getDays() != 0 )? Duration.ofDays(1) : Duration.ofDays(1).dividedBy(Constants.NEWS_NUM_CHECK_PER_DAY));
        res.setBegin(begin);
        res.setEnd(end);
        res.setSize(data.size());
        return res;
    }

    /**
     * Возвращает временные ряды по характеристикам публикаци с одинаковым шагом.
     * @param groupId - идентификатор группы, которой принадлежит публикация
     * @param postId - идентификатор публикации
     * @param begin - начало периода из которого извлекать информацию о публикации
     * @param end - конец периода из которого извлекать информацию о публикации
     * @return Объект содержащий времянные ряды с одинаковым шагом
     */
    @Override
    public PostInfoByTime getPostInfoByTime(String groupId, String postId, LocalDate begin, LocalDate end) {
        Util.checkDate(begin, end);
        List<PostInfo> pl = postInfoRep.findPostInfoByPeriod(groupId, postId, Util.toTimeUtc(begin, LocalTime.MIN), Util.toTimeUtc(end, LocalTime.MAX));
        if(pl.isEmpty()) {
            throw new HttpNotFoundException("Not information of post (groupId: "
                    +groupId + "; postId: " + postId + ") by period: " +
                    begin.toString() + " - " + end.toString());
        }
        List<Integer> views = applyGroupValuesByDay(pl, PostInfo::getViews, Integer::sum, ()->0, PostInfo::getDateAddedRecord, begin, end);
        List<Integer> share = applyGroupValuesByDay(pl, PostInfo::getShare, Integer::sum, ()->0, PostInfo::getDateAddedRecord, begin, end);
        List<Integer> likes = applyGroupValuesByDay(pl, PostInfo::getLikes, Integer::sum, ()->0, PostInfo::getDateAddedRecord, begin, end);
        List<Integer> comments = applyGroupValuesByDay(pl, PostInfo::getComments, Integer::sum, ()->0, PostInfo::getDateAddedRecord, begin, end);

        PostInfoByTime res = new PostInfoByTime();
        Integer size = views.size();
        res.setGroupId(groupId);
        res.setPostId(postId);
        res.setBegin(LocalDateTime.of(begin, LocalTime.MIN));
        res.setEnd(LocalDateTime.of(end, LocalTime.MAX));
        res.setVariabilityNumberViews(new DataList<>(size, views));
        res.setVariabilityNumberShares(new DataList<>(size, share));
        res.setVariabilityNumberLikes(new DataList<>(size, likes));
        res.setVariabilityNumberComments(new DataList<>(size, comments));
        return res;
    }

    /**
     * Метод возвращает сумарные параметры поста (общее количество просмотров, лайков, репостов) и коэффициент вовлеченности
     * посчитанный относительно просмотров записи
     * @param groupId идентификатор группы, в которой находится публикация
     * @param postId индентификатор публикации
     * @return сумарные показатели
     */
    @Override
    public PostSummary getPostSummary(String groupId, String postId) {
        Optional<Post> post = postRep.findById(new PostId(groupId, postId));
        if(post.isEmpty()) {
            throw new HttpNotFoundException("Not found information by post (GroupId: "
                    + groupId + "; PostId: " + postId + ")");
        }
        PostSummary res = new PostSummary();
        res.setGroupId(groupId);
        res.setPostId(postId);
        res.setNumberComments(post.get().getComments());
        res.setNumberLikes(post.get().getLikes());
        res.setNumberReposts(post.get().getShares());
        res.setNumberViews(post.get().getViews());
        res.setEngagementRate(engagementRate(res.getNumberViews(), res.getNumberReposts(),
                                             res.getNumberComments(), res.getNumberLikes()));
        return res;
    }

    /**
     * Запись текущих данных об посте в базу данных. Передается текущее состояние, в базу
     * попадет как переданные данные так и разница текущего состояния с предыдущи. Принимаются только новые данные.
     * Если передать объект с полем time, значение которого меньше самой свежой записи для конкретного поста
     * то будет выкинуто исключение.
     * @param data - текущее состояние поста
     * @throws HttpIllegalBodyRequest если список содержит записи с одинаковым полем time для одного и тогоже поста
     * @throws HttpIllegalBodyRequest если в базе данных содержится более свежая запись чем переданная. Определяется через поле time
     */
    @Override
    public void updateInformationOfPost(List<InformationOfPost> data) {
       //checking data
        for(InformationOfPost el : data) {
            if(el.getViews() < 0) {
                throw new HttpIllegalBodyRequest("Number of views can't be bellow zero");
            }
            if(el.getComments() < 0) {
                throw new HttpIllegalBodyRequest("Number of comments can't be bellow zero");
            }
            if(el.getLikes() < 0 ) {
                throw new HttpIllegalBodyRequest("Number of likes can't be below zero");
            }
            if(el.getShares() < 0) {
                throw new HttpIllegalBodyRequest("Number of shares can't be below zero");
            }
        }
        DateTimeColumn dcol = DateTimeColumn.create("time");
        StringColumn gIdCol = StringColumn.create("groupId");
        StringColumn pIdCol = StringColumn.create("postId");
        for(InformationOfPost el : data) {
            dcol.append(LocalDateTime.from(el.getTime()));
            gIdCol.append(el.getGroupId());
            pIdCol.append(el.getPostId());
        }

        Table t = Table.create(dcol, gIdCol, pIdCol);
        StringColumn group_id_unique = gIdCol.unique();
        //process pair (group id, post id)
        for(String gid : group_id_unique) {
            Table oneGroupRecords = t.where(gIdCol.isEqualTo(gid));
            StringColumn unique_post_id_for_group = oneGroupRecords.stringColumn("postId").unique();
            for(String pid : unique_post_id_for_group) {
                DateTimeColumn timestamps_for_different_records_of_one_post = oneGroupRecords.where(pIdCol.isEqualTo(pid)).dateTimeColumn("time");
                int all_size = timestamps_for_different_records_of_one_post.size();
                DateTimeColumn timestamps_unique = timestamps_for_different_records_of_one_post.unique();
                int unique_size = timestamps_unique.size();
                if(all_size != unique_size) {
                    //get repeat records;
                    throw new HttpIllegalBodyRequest("Json Array contain some records for post (GroupId: "+
                            gid + "; PostId: "+ pid +") with equals time field");
                }
            }
        }

        //check timestamp
        for(InformationOfPost el : data) {
            Optional<OldestTimeRecord> time =  postInfoRep.getTimeOfYoungestRecord(
                    el.getGroupId(),el.getPostId());
            LocalDateTime timestamp = t.where(gIdCol.isEqualTo(el.getGroupId()).and(pIdCol.isEqualTo(el.getPostId())))
                    .dateTimeColumn("time").min();
            if(time.isPresent() && time.get().getTime().toLocalDateTime().isAfter(timestamp)) {
                throw new HttpIllegalBodyRequest("Json Array contain time series with time stamp oldest then record in data base. "+
                        "time_stamp in request: " + timestamp.toString() + "(GroupId: " + el.getGroupId() + "; PostId: " + el.getPostId() +
                        ") time_stamp in data base: " + time.get().getTime().toString());
            }
        }

        data.sort(Comparator.comparing(InformationOfPost::getTime));
        //save before states of post
        Map<PostId, Post> state = new HashMap<>();
        for(String groupId : group_id_unique) {
            StringColumn post_id_unique = t.where(gIdCol.isEqualTo(groupId))
                                            .stringColumn("postId").unique();
            for(String postId : post_id_unique) {
                PostId id = new PostId(groupId, postId);
                Optional<Post> p = postRep.findById(id);
                if(p.isPresent()) {
                    state.put(id, p.get());
                }
            }
        }

        //Save data
        for(InformationOfPost i : data) {
            PostId id = new PostId(i.getGroupId(), i.getPostId());
            if (!state.containsKey(id)) {
                Post post = information2Post(i);
                postRep.save(post);
                PostInfo newInfo = information2PostInfo(i);
                newInfo.setPost(post);
                postInfoRep.save(newInfo);
                state.put(post.getId(), post);
           } else {
                Post post_state = state.get(id);
                //check changes of data
                if(post_state.getViews() > i.getViews()) {
                    throw new HttpIllegalBodyRequest("Number of views can't decrease (Was " + post_state.getViews() + " became " + i.getViews());
                }

                PostInfo newPostInfo = new PostInfo();
                newPostInfo.setViews(i.getViews()- post_state.getViews());
                newPostInfo.setComments(i.getComments()- post_state.getComments());
                newPostInfo.setShare(i.getShares() - post_state.getShares());
                newPostInfo.setLikes(i.getLikes() - post_state.getLikes());
                newPostInfo.setDateAddedRecord(i.getTime());
                Post newPostState = information2Post(i);
                postRep.save(newPostState);
                newPostInfo.setPost(newPostState);
                postInfoRep.save(newPostInfo);
                state.put(newPostState.getId(), newPostState);
            }
        }

    }


    /**
     * Запись текущих данных об группе в базу данных. Передается текущее состояние, но в базу
     * попадет разница текущего состояния с предыдущими отметками. Принимаются только новые данные.
     * Если передать объект с полем time значение которого меньше самой свежой записи для конкретной группы
     * то будет выкинуто исключение.
     * @param data - текущее состояние группы
     * @throws HttpIllegalBodyRequest если список содержит записи с одинаковым полем time для однной и тойже группы
     * @throws HttpIllegalBodyRequest если в базе данных содержится более свежая запись чем переданная. Определяется через поле time
     */
    @Override
    public void updateInformationOfGroup(List<InformationOfGroup> data) {
        //checking data
        DateTimeColumn dCol = DateTimeColumn.create("time");
        StringColumn idCol = StringColumn.create("id");
        IntColumn subCol = IntColumn.create("sub");
        for(InformationOfGroup el : data) {
            dCol.append(el.getTime());
            idCol.append(el.getGroupId());
            subCol.append(el.getSubscribersNumber());
        }
        Table t = Table.create(dCol, idCol, subCol);
        StringColumn unique_ids = idCol.unique();
        for(String id : unique_ids) {
            DateTimeColumn dt = t.where(idCol.isEqualTo(id)).dateTimeColumn("time");
            DateTimeColumn dt_unique = dt.unique();
            if(dt.size() != dt_unique.size()) {
                throw new HttpIllegalBodyRequest("Json Array contain some records for Group (GroupId: "+
                        id + ") with equals time field");
            }
        }
        //check timestamp
        for(String id : unique_ids) { ;
            Optional<OldestTimeRecord> time = groupInfoRep.getOldestTimeOfRecord(id);
            if (time.isPresent()) {
                LocalDateTime data_time = t.where(idCol.isEqualTo(id)).dateTimeColumn("time").min();
                if(time.get().getTime().toLocalDateTime().isAfter(data_time)) {
                    throw new HttpIllegalBodyRequest("Json Array contain time series with time stamp oldest then record in data base. "+
                            "time_stamp in request: " + data_time.toString() + "(GroupId: " + id +
                            ") time_stamp in data base: " + time.get().getTime().toString());
                }
            }
        }
        data.sort(Comparator.comparing(InformationOfGroup::getTime));
        Map<String, Group> state = new HashMap<>();
        for(String id : unique_ids) {
            Optional<Group> g = groupRep.findById(id);
            if(g.isPresent()) {
                state.put(id, g.get());
            }
        }

        //TODO change object for complex key
        for(InformationOfGroup el : data) {
            GroupInfo go = new GroupInfo();
            Group group;
            if(state.containsKey(el.getGroupId())) {
                group = state.get(el.getGroupId());
                go.setSubscribers((el.getSubscribersNumber() - group.getSubscribers()));
            } else {
                go.setSubscribers(el.getSubscribersNumber());
                group = new Group();
                group.setGroupId(el.getGroupId());
            }
            group.setSubscribers(el.getSubscribersNumber());
            groupRep.save(group);

            go.setGroup(group);
            go.setOnline(el.getSubscribersOnline());
            go.setTimeAddedRecord(ZonedDateTime.of(el.getTime(), ZoneOffset.UTC));
            groupInfoRep.save(go);
        }
    }


    /**
     * Вычисление коэффициента воволеченности аудитории через просмотры публикации
     * @param views - число просмотров
     * @param share - число репостов
     * @param comments - число комментариев
     * @param likes - число лайков
     * @return
     */
    private static Double engagementRate(Integer views, Integer share, Integer comments, Integer likes) {
        return (share + comments + likes)/Double.valueOf(views);
    }

    private PostInfo information2PostInfo(InformationOfPost info) {
        PostInfo newInfo = new PostInfo();
        newInfo.setDateAddedRecord(info.getTime());
        newInfo.setComments(info.getComments());
        newInfo.setViews(info.getViews());
        newInfo.setShare(info.getShares());
        newInfo.setLikes(info.getLikes());
        if(!newInfo.checkNotNull()) {
            throw new HttpIllegalBodyRequest("fields cannot be null");
        }
        return newInfo;
    }

    private Post information2Post(InformationOfPost info) {
        Post post = new Post();
        post.setId(new PostId(info.getGroupId(), info.getPostId()));
        post.setComments(info.getComments());
        post.setLikes(info.getLikes());
        post.setShares(info.getShares());
        post.setViews(info.getViews());
        return post;
    }

    private static <ValueT, ContainerT>  List<ValueT> applyGroupValuesByDay(
            List<ContainerT> timeSeries, Function<ContainerT, ValueT> valueGetter, BinaryOperator<ValueT> op,
            Supplier<ValueT> defaultGetter, Function<ContainerT,ZonedDateTime> timeGetter,
            LocalDate begin, LocalDate end) {

        int numDays = begin.until(end).getDays();
        List<ValueT> data = new LinkedList<>();
        if(numDays == 0) {
            Collections.sort(timeSeries, Comparator.comparing(timeGetter));
            for(ContainerT el : timeSeries) {
                data.add(valueGetter.apply(el));
            }
        } else {
            HashMap<LocalDate, ValueT> sumByDay = new HashMap<>();
            for(ContainerT el : timeSeries) {
                LocalDate day = LocalDate.from(timeGetter.apply(el));
                sumByDay.put(day, op.apply(sumByDay.getOrDefault(day, defaultGetter.get()), valueGetter.apply(el)));
            }
            ArrayList<LocalDate> dates = new ArrayList<>(sumByDay.keySet());
            Collections.sort(dates);
            for(LocalDate d : dates) {
                data.add(sumByDay.get(d));
            }
        }
        return data;
    }

}
