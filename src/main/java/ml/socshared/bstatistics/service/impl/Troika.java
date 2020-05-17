package ml.socshared.bstatistics.service.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ml.socshared.bstatistics.domain.object.InformationType;

@EqualsAndHashCode
@AllArgsConstructor
public class Troika {
    public String groupId;
    public String postId;
    public InformationType type;
}
