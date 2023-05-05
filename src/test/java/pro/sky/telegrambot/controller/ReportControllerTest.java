package pro.sky.telegrambot.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;
import pro.sky.telegrambot.repository.ReportRepository;
import pro.sky.telegrambot.service.DogOwnerReportService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = DogOwnerReportController.class)
class ReportControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportRepository reportRepository;

    @SpyBean
    private DogOwnerReportService reportService;

    @Test
    void test_findReportsByOwnerId() throws Exception {

    }
}