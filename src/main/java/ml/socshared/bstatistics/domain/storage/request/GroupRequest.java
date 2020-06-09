package ml.socshared.bstatistics.domain.storage.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ml.socshared.bstatistics.domain.storage.SocialNetwork;;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupRequest {

    private UUID userId;
    private String vkId;
    private String fbId;
    @NotNull
    private String name;
    @NotNull
    private SocialNetwork socialNetwork;

}
