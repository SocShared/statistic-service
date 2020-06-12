package ml.socshared.bstatistics.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ml.socshared.bstatistics.domain.db.GroupTable;
import ml.socshared.bstatistics.domain.db.GroupInfo;
import ml.socshared.bstatistics.domain.db.Post;
import ml.socshared.bstatistics.domain.db.PostInfo;
import ml.socshared.bstatistics.domain.object.*;
import ml.socshared.bstatistics.domain.rabbitmq.response.RabbitMqResponseAll;
import ml.socshared.bstatistics.domain.response.GroupInfoResponse;
import ml.socshared.bstatistics.domain.storage.SocialNetwork;
import ml.socshared.bstatistics.exception.HttpIllegalBodyRequest;
import ml.socshared.bstatistics.exception.HttpNotFoundException;
import ml.socshared.bstatistics.repository.GroupInfoRepository;
import ml.socshared.bstatistics.repository.GroupRepository;
import ml.socshared.bstatistics.repository.PostInfoRepository;
import ml.socshared.bstatistics.repository.PostRepository;
import ml.socshared.bstatistics.service.StatService;
import ml.socshared.bstatistics.service.sentry.SentrySender;
import ml.socshared.bstatistics.service.sentry.SentryTag;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.*;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;



@Slf4j
@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {

    private final GroupInfoRepository groupInfoRep;
    private final GroupRepository groupRep;
    private final PostInfoRepository postInfoRep;
    private final PostRepository postRep;
    private final SentrySender sentrySender;


    @Override
    public GroupInfoResponse getGroupInfoByTime(UUID groupId, SocialNetwork soc, LocalDate begin, LocalDate end) {
        Util.checkDate(begin, end);
        List<GroupInfo> giList = groupInfoRep.findBySocialIdBetweenDates(groupId, soc, LocalDateTime.of(begin, LocalTime.MIN), LocalDateTime.of(end, LocalTime.MIDNIGHT));
        if (giList.isEmpty()) {
            throw new HttpNotFoundException("Not found information on group by set period");
        }

        List<TimePoint<Integer>> gonline = new LinkedList<>();
        List<TimePoint<Integer>> gsubscribers = new LinkedList();
        Long time = null;
        for(GroupInfo info : giList) {
             time = info.getTimeAddedRecord().toInstant(ZoneOffset.UTC).toEpochMilli();
            gonline.add(new TimePoint<>(info.getOnline(), time));
            gsubscribers.add(new TimePoint<>(info.getSubscribers(), time));
        }

        TimeSeries<TimePoint<Integer>> series_online = new TimeSeries<>();
        LocalDate time_end = Instant.ofEpochMilli(time).atZone(ZoneOffset.UTC).toLocalDate();
        LocalDate time_begin = giList.get(0).getTimeAddedRecord().toLocalDate();
        series_online.setSize(gonline.size());
        series_online.setData(gonline);
        series_online.setBegin(time_begin);
        series_online.setEnd(time_end);

        TimeSeries<TimePoint<Integer>> series_subscribers = new TimeSeries<>();
        series_subscribers.setSize(gsubscribers.size());
        series_subscribers.setData(gsubscribers);
        series_subscribers.setBegin(time_begin);
        series_subscribers.setEnd(time_end);

        GroupInfoResponse response = new GroupInfoResponse();
        response.setSystemGroupId(groupId);
        response.setSocialNetwork(soc);
        response.setOnline(series_online);
        response.setSubscribers(series_subscribers);

        Map<String, Object> additional = new HashMap<>();
        additional.put("system_group_id", groupId);
        additional.put("social_network", soc);
        additional.put("time_begin", begin);
        additional.put("time_end", end);
        sentrySender.sentryMessage("get time series of group", additional,
                Collections.singletonList(SentryTag.GroupOnline));

        return response;
    }



    @Override
    public PostInfoByTime getPostInfoByTime(UUID groupId, UUID postId, SocialNetwork soc, LocalDate begin, LocalDate end) {
        List<PostInfo> res =  postInfoRep.findPostInfoByPeriod(groupId, postId, soc, LocalDateTime.of(begin, LocalTime.MIN),LocalDateTime.of(end,LocalTime.MIDNIGHT) );
        PostInfoByTime response = new PostInfoByTime();
        if(res.isEmpty()) {
            throw new HttpNotFoundException("Not information of post (groupId: "
                    +groupId + "; postId: " + postId + ") by period: " +
                    begin.toString() + " - " + end.toString());
        }

        List<TimePoint<Integer>> views =  new LinkedList<>();
        List<TimePoint<Integer>> share =  new LinkedList<>();
        List<TimePoint<Integer>> likes =  new LinkedList<>();
        List<TimePoint<Integer>> comments =  new LinkedList<>();

        for (PostInfo info : res) {
            views.add(new TimePoint<>( info.getViews(), info.getDateAddedRecord().toInstant(ZoneOffset.UTC).toEpochMilli()));
            share.add(new TimePoint<>( info.getShare(), info.getDateAddedRecord().toInstant(ZoneOffset.UTC).toEpochMilli()));
            likes.add(new TimePoint<>( info.getLikes(), info.getDateAddedRecord().toInstant(ZoneOffset.UTC).toEpochMilli()));
            comments.add(new TimePoint<>( info.getComments(), info.getDateAddedRecord().toInstant(ZoneOffset.UTC).toEpochMilli()));
        }
        response.setSystemGroupId(groupId);
        response.setSystemPostId(postId);
        response.setBegin(res.get(0).getDateAddedRecord());
        response.setEnd(res.get(res.size()-1).getDateAddedRecord());
        response.setVariabilityNumberViews(new DataList<>(views.size(), views));
        response.setVariabilityNumberShares(new DataList<>(share.size(), share));
        response.setVariabilityNumberLikes(new DataList<>(likes.size(), likes));
        response.setVariabilityNumberComments(new DataList<>(comments.size(), comments));
        return response;
    }

    /**
     * Метод возвращает сумарные параметры поста (общее количество просмотров, лайков, репостов) и коэффициент вовлеченности
     * посчитанный относительно просмотров записи
     * @param groupId идентификатор группы, в которой находится публикация
     * @param postId индентификатор публикации
     * @return сумарные показатели
     */
    @Override
    public PostSummary getPostSummary(UUID groupId, UUID postId, SocialNetwork  soc) {
        Optional<YoungestTimeRecord> record = postInfoRep.getTimeOfYoungestRecord(groupId, postId, soc);
        if(record.isEmpty()) {
            throw new HttpNotFoundException("Not found information by post (GroupId: "
                    + groupId + "; PostId: " + postId + ")");
        }
        List<PostInfo> postsList = postInfoRep.findPostInfoByPeriod(groupId, postId, soc,
                record.get().getTime(), record.get().getTime());
        PostInfo post = postsList.get(0);
        PostSummary res = new PostSummary();
        res.setSystemGroupId(groupId);
        res.setSystemPostId(postId);
        res.setNumberComments(post.getComments());
        res.setNumberLikes(post.getLikes());
        res.setNumberReposts(post.getShare());
        res.setNumberViews(post.getViews());
        res.setEngagementRate(engagementRate(res.getNumberViews(), res.getNumberReposts(),
                                             res.getNumberComments(), res.getNumberLikes()));

        Map<String, Object> additional = new HashMap<>();
        additional.put("post_id", new SentryPostId(groupId, postId));
        sentrySender.sentryMessage("get post summary", additional,
                Collections.singletonList(SentryTag.PostSummary));
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
    @Transactional
    public void updateInformationOfPost(RabbitMqResponseAll data) {
       //checking data
            if( data.getViewsCount() != null && data.getViewsCount() < 0) {
                throw new HttpIllegalBodyRequest("Number of views can't be bellow zero");
            }
            if(data.getCommentsCount() != null && data.getCommentsCount() < 0) {
                throw new HttpIllegalBodyRequest("Number of comments can't be bellow zero");
            }
            if(data.getLikesCount()!= null && data.getLikesCount() < 0 ) {
                throw new HttpIllegalBodyRequest("Number of likes can't be below zero");
            }
            if(data.getRepostsCount() != null && data.getRepostsCount() < 0) {
                throw new HttpIllegalBodyRequest("Number of shares can't be below zero");
            }


            PostInfo newPostInfo = new PostInfo();
            Optional<Post> postOptional = postRep.findBySocial(data.getGroupId(), data.getPostId(),
                                                               data.getSocialNetwork());
            if(postOptional.isEmpty()) {
                log.error("Internal Server Error. Post by social (groupId: {}, postId: {}, soc: {}) not found in db",
                        data.getGroupId(), data.getPostId(), data.getSocialNetwork());
                return;
            }
            Post post = postOptional.get();
            newPostInfo.setPost(post);
            newPostInfo.setViews(data.getViewsCount());
            newPostInfo.setComments(data.getCommentsCount());
            newPostInfo.setShare(data.getRepostsCount());
            newPostInfo.setLikes(data.getLikesCount());
            newPostInfo.setDateAddedRecord(Instant.ofEpochMilli(data.getDateTime()).atZone(ZoneOffset.UTC).toLocalDateTime());
            postInfoRep.save(newPostInfo);


        Map<String, Object> additional = new HashMap<>();
        additional.put("post_ids", new SentryPostId(data.getSystemGroupId(), data.getSystemPostId()));
        sentrySender.sentryMessage("update information of posts", additional,
                Collections.singletonList(SentryTag.PostUpdate));
    }

    @Override
    @Transactional
    public void updateInformationOfGroup(RabbitMqResponseAll data) {
        LocalDateTime request_data =  Instant.ofEpochMilli(data.getDateTime()).atZone(ZoneOffset.UTC).toLocalDateTime();
        Optional<YoungestTimeRecord> record = groupInfoRep.getYoungestTimeOfRecordBySocialId(data.getSystemGroupId(), data.getSocialNetwork());
        if(record.isPresent()) {
            if(record.get().getTime().isAfter(request_data) ||record.get().getTime().isEqual(request_data)) {
                log.error("Invalid information. Time point must be more time point from db; -> {}", data);
                return;
            }
        }

        GroupInfo info = new GroupInfo();
        Optional<GroupTable> groupOptional = groupRep.findBySocial(data.getGroupId(), data.getSocialNetwork());
        if(groupOptional.isEmpty()) {
            log.error("Internal server error, for group not found systemId; -> {}", data);
            return;
        }
        GroupTable group = groupOptional.get();;
        info.setGroup(group);
        info.setOnline(data.getMembersOnline());
        info.setSubscribers(data.getMembersCount());
        info.setTimeAddedRecord(request_data);

        groupInfoRep.save(info);
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



    private static <ValueT, ContainerT>  List<ValueT> applyGroupValuesByDay(
            List<ContainerT> timeSeries, Function<ContainerT, ValueT> valueGetter, BinaryOperator<ValueT> op,
            Supplier<ValueT> defaultGetter, Function<ContainerT,LocalDateTime> timeGetter,
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
