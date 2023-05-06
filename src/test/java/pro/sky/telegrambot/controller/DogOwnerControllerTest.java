package pro.sky.telegrambot.controller;

import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pro.sky.telegrambot.model.DogOwner;
import pro.sky.telegrambot.repository.DogOwnerRepository;
import pro.sky.telegrambot.service.DogOwnerService;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = DogOwnerController.class)
class DogOwnerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DogOwnerRepository dogOwnerRepository;

    @SpyBean
    private DogOwnerService dogOwnerService;


    @Test
    void test_saveDogOwner() throws Exception {
        Integer id = 1;
        String name = "Alex";
        Long chatId = 1525L;

        JSONObject ownerObject = new JSONObject();
        ownerObject.put("name", name);
        ownerObject.put("chatId", chatId);

        DogOwner owner = new DogOwner();
        owner.setId(id);
        owner.setName(name);
        owner.setChatId(chatId);


        when(dogOwnerRepository.save(any(DogOwner.class))).thenReturn(owner);
        when(dogOwnerRepository.findOwnerById(id)).thenReturn(owner);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/saveDogOwner/{name}/{chatId}", name, chatId)
                        .content(ownerObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.chatId").value(chatId));
    }

    @Test
    void test_findDogOwnerById() throws Exception {
        Integer id = 1;
        String name = "Alex";

        DogOwner owner = new DogOwner();
        owner.setId(id);
        owner.setName(name);

        when(dogOwnerRepository.findOwnerById(id)).thenReturn(owner);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/findDogOwnerById/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name));
    }

    @Test
    void test_extendProbationaryPeriod() throws Exception {
        Integer id = 1;
        String name = "Alex";
        Integer days = 5;

        JSONObject ownerObject = new JSONObject();
        ownerObject.put("name", name);
        ownerObject.put("days", days);

        DogOwner owner = new DogOwner();
        owner.setId(id);
        owner.setName(name);
        owner.setDateOfEndProbation(LocalDateTime.now());
        owner.setPeriodExtend(days);


        when(dogOwnerRepository.save(any(DogOwner.class))).thenReturn(owner);
        when(dogOwnerRepository.findOwnerById(id)).thenReturn(owner);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/extendDogOwnerTrialPeriod/{id}/{days}", id, days)
                        .content(ownerObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.periodExtend").value(days));
    }
}