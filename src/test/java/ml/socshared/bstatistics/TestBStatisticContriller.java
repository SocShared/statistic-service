package ml.socshared.bstatistics;

import ml.socshared.bstatistics.controller.BStatisticController;
import ml.socshared.bstatistics.repository.GroupInfoRepository;
import ml.socshared.bstatistics.repository.PostInfoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TestBStatisticContriller {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    BStatisticController controller;
    @Autowired
    PostInfoRepository postRep;
    @Autowired
    GroupInfoRepository groupRep;



    @BeforeEach
    void loadData() throws Exception {

    }




    @Test
    void testLoadContext() throws Exception {
        Assertions.assertNotNull(controller);
    }
}
