package ml.socshared.bstatistics.domain.storage.request;

import lombok.Getter;
import lombok.Setter;
import ml.socshared.bstatistics.domain.storage.PostType;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
public class PublicationRequest {

    private Date publicationDateTime;
    @NotEmpty
    private String userId;
    @NotEmpty
    private String[] groupIds;
    @NotNull
    private PostType type;
    @NotNull
    private String text;

}
