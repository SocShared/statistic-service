package ml.socshared.bstatistics.service.impl;

import ml.socshared.bstatistics.config.Constants;
import ml.socshared.bstatistics.repository.GroupInfoRepository;
import ml.socshared.bstatistics.stat.KDE;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class StatServiceImplTest {

    StatServiceImpl service;
    GroupInfoRepository groupInfoRep = Mockito.mock(GroupInfoRepository.class);

    @Test
    public void estimateOnlineOfHour() {

        service = new StatServiceImpl(groupInfoRep, new KDE(Constants.STAT_KDE_H), Constants.STAT_KDE_ESTIMATE_SIZE);
    }
}