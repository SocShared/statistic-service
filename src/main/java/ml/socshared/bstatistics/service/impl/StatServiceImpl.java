package ml.socshared.bstatistics.service.impl;

import ml.socshared.bstatistics.domain.object.TimeSeries;
import ml.socshared.bstatistics.repository.GroupInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;

@Service
public class StatServiceImpl {
    private GroupInfoRepository rep;

    @Autowired
    public StatServiceImpl(GroupInfoRepository rep) {
        this.rep = rep;
    }




}
