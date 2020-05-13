package ml.socshared.bstatistics.service.impl;

import lombok.extern.slf4j.Slf4j;
import ml.socshared.bstatistics.config.Constants;
import ml.socshared.bstatistics.config.json.LocalDateTimeSerializer;
import ml.socshared.bstatistics.domain.db.GroupOnline;
import ml.socshared.bstatistics.domain.db.PostInfo;
import ml.socshared.bstatistics.domain.object.InformationOfPost;
import ml.socshared.bstatistics.domain.object.TimeSeries;
import ml.socshared.bstatistics.exception.HttpIllegalBodyRequest;
import ml.socshared.bstatistics.exception.HttpNotFoundException;
import ml.socshared.bstatistics.repository.GroupOnlineRepository;
import ml.socshared.bstatistics.repository.PostInfoRepository;
import ml.socshared.bstatistics.service.StatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Slf4j
@Service
public class StatServiceImpl implements StatService {
    private GroupOnlineRepository groupRepository;
    private PostInfoRepository postRepository;

    @Autowired
    public StatServiceImpl(GroupOnlineRepository gr, PostInfoRepository pr) {
        this.groupRepository = gr;
        this.postRepository = pr;
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
        List<GroupOnline> giList = groupRepository.findBetweenDates(groupId, begin, end);
        if (giList.isEmpty()) {
            throw new HttpNotFoundException("Not found information on group by set period");
        }

        List<Integer> data = applyGroupValuesByDay(giList, GroupOnline::getTimeAddedRecord, GroupOnline::getOnline,
                                                    Integer::sum, begin, end);

        TimeSeries<Integer> res = new TimeSeries<>();
        res.setData(data);
        res.setStep((begin.until(end).getDays() != 0 )? Duration.ofDays(1) : Duration.ofDays(1).dividedBy(Constants.NEWS_NUM_CHECK_PER_DAY));
        res.setBegin(begin);
        res.setEnd(end);
        res.setSize(data.size());
        return res;
    }

    /**
     * Запись текущих данных об посте в базу данных. Передается текущее состояние, но в базу
     * попадет разница текущего состояния с предыдущими отметками.
     * @param data - текущее состояние поста
     */
    @Override
    public void updateInformationOfPost(List<InformationOfPost> data) {
        for(InformationOfPost i : data) {
            List<PostInfo> info = postRepository.findPostInfoByGroupIdAndPostId(i.getGroupId(), i.getPostId());
            if (info.isEmpty()) {
                PostInfo newInfo = information2PostInfo(i);
                postRepository.save(newInfo);
           } else {
                Integer views =0;
                Integer commnets = 0;
                Integer  share = 0;
                Integer likes = 0;
                Integer subscribers = 0;
                for(PostInfo pi : info) {
                    views += pi.getViews();
                    commnets += pi.getComments();
                    share += pi.getShare();
                    likes += pi.getLikes();
                }
                PostInfo newPostInfo = new PostInfo();
                newPostInfo.setViews(i.getViews()-views);
                newPostInfo.setComments(i.getComments()-commnets);
                newPostInfo.setShare(i.getShares()-share);
                newPostInfo.setLikes(i.getLikes()-likes);
                newPostInfo.setDateAddedRecord(i.getTime());
                newPostInfo.setGroupId(i.getGroupId());
                newPostInfo.setPostId(i.getPostId());
                postRepository.save(newPostInfo);
            }
        }

    }

    public static  List<Integer> applyGroupValuesByDay(List<GroupOnline> timeSeries, Function<GroupOnline,ZonedDateTime> timeGetter,
                                                Function<GroupOnline, Integer> valueGetter, BinaryOperator<Integer> op, LocalDate begin, LocalDate end) {
        int numDays = begin.until(end).getDays();
        List<Integer> data = new LinkedList<Integer>();
        if(numDays == 0) {
            Collections.sort(timeSeries, Comparator.comparing(timeGetter));
            for(GroupOnline el : timeSeries) {
                data.add(el.getOnline());
            }
        } else {
            HashMap<LocalDate, Integer> sumByDay = new HashMap<>();
            for(GroupOnline el : timeSeries) {
                LocalDate day = LocalDate.from(timeGetter.apply(el));
                sumByDay.put(day, op.apply(sumByDay.getOrDefault(day, 0), valueGetter.apply(el)));
            }
            ArrayList<LocalDate> dates = new ArrayList<>(sumByDay.keySet());
            Collections.sort(dates);
            for(LocalDate d : dates) {
                data.add(sumByDay.get(d));
            }
        }
        return data;
    }

    private PostInfo information2PostInfo(InformationOfPost info) {
        PostInfo newInfo = new PostInfo();
        newInfo.setGroupId(info.getGroupId());
        newInfo.setPostId(info.getPostId());
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


}
