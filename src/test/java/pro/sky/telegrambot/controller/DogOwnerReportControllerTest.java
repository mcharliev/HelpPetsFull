package pro.sky.telegrambot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pro.sky.telegrambot.model.DogOwner;
import pro.sky.telegrambot.model.DogOwnerReport;
import pro.sky.telegrambot.repository.DogOwnerReportRepository;
import pro.sky.telegrambot.service.DogOwnerReportService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DogOwnerReportController.class)
class DogOwnerReportControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private DogOwnerReportRepository dogOwnerReportRepository;

    @SpyBean
    private DogOwnerReportService dogOwnerReportService;

    @Test
    void test_findReportsByOwnerId() throws Exception {
        Integer id1 = 1;
        String stringReport1 = "All is well with the dog";
        DogOwnerReport report1 = new DogOwnerReport();
        report1.setId(id1);
        report1.setStringReport(stringReport1);

        Integer id2 = 1;
        String stringReport2 = "All is well with the dog";
        DogOwnerReport report2 = new DogOwnerReport();
        report2.setId(id2);
        report2.setStringReport(stringReport2);

        DogOwner owner = new DogOwner();
        Integer ownerId = 2;
        String ownerName = "Alex";
        owner.setId(ownerId);
        owner.setName(ownerName);
        report1.setDogOwner(owner);
        report2.setDogOwner(owner);

        when(dogOwnerReportRepository.findByDogOwnerId(ownerId)).thenReturn(List.of(report1,report2));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/dogReports/{ownerId}", ownerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(
                        List.of(report1, report2))));
    }
}