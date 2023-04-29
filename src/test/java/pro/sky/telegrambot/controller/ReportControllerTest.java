package pro.sky.telegrambot.controller;

import liquibase.pro.packaged.R;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pro.sky.telegrambot.model.Dog;
import pro.sky.telegrambot.model.Report;
import pro.sky.telegrambot.repository.DogRepository;
import pro.sky.telegrambot.repository.ReportRepository;
import pro.sky.telegrambot.service.DogService;
import pro.sky.telegrambot.service.ReportService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ReportController.class)
class ReportControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportRepository reportRepository;

    @SpyBean
    private ReportService reportService;

    @Test
    void test_findReportsByOwnerId() throws Exception {

    }
}