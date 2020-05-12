package ml.socshared.bstatistics.service.impl;

import lombok.extern.slf4j.Slf4j;
import ml.socshared.bstatistics.config.Constants;
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

import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

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

        int numDays = begin.until(end).getDays();
        TimeSeries<Integer> res = new TimeSeries<>();
        Duration stepSeconds;
        List<Integer> data = new LinkedList<Integer>();
        if(numDays == 0) {
            Collections.sort(giList, Comparator.comparing(GroupOnline::getTimeAddedRecord));
            for(GroupOnline el : giList) {
                data.add(el.getOnline());
            }
            stepSeconds = Duration.ofDays(1).dividedBy(Constants.NEWS_NUM_CHECK_PER_DAY);
        } else {
            HashMap<LocalDate, Integer> sumOnlineByDay = new HashMap<>();
            for(GroupOnline el : giList) {
                LocalDate day = LocalDate.from(el.getTimeAddedRecord());
                sumOnlineByDay.put(day, sumOnlineByDay.getOrDefault(day, 0) + el.getOnline());
            }
            ArrayList<LocalDate> dates = new ArrayList<>(sumOnlineByDay.keySet());
            Collections.sort(dates);

            for(LocalDate d : dates) {
                data.add(sumOnlineByDay.get(d));
            }
            stepSeconds = Duration.ofDays(1);
        }
        res.setData(data);
        res.setStep(stepSeconds);
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
