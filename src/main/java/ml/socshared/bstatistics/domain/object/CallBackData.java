package ml.socshared.bstatistics.domain.object;

import lombok.Data;

import java.util.List;

@Data
public class CallBackData {
    DataList<InformationOfGroup> groupInfo;
    DataList<InformationOfPost> postInfo;
}
