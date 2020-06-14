package ml.socshared.bstatistics.service;

import ml.socshared.bstatistics.domain.db.PostInfo;
import ml.socshared.bstatistics.domain.object.InformationOfPost;

public interface DataService {
    void addGroup(String groupId, Integer subscribers);
    void updateSubscribers(String groupId, Integer subscribers);
    void deleteGroup(String groupId);
    void addPostInfo(InformationOfPost info);
    PostInfo getPostState(String groupId, String postId);

}
