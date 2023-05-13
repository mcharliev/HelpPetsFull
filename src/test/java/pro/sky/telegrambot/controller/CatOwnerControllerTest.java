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
import pro.sky.telegrambot.model.CatOwner;
import pro.sky.telegrambot.model.DogOwner;
import pro.sky.telegrambot.repository.CatOwnerRepository;
import pro.sky.telegrambot.repository.DogOwnerRepository;
import pro.sky.telegrambot.service.CatOwnerService;
import pro.sky.telegrambot.service.DogOwnerService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CatOwnerController.class)
class CatOwnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CatOwnerRepository catOwnerRepository;

    @SpyBean
    private CatOwnerService catOwnerService;

    @Test
    void saveCatOwner() throws Exception {
        Integer id = 1;
        String name = "Alex";
        Long chatId = 1525L;

        JSONObject ownerObject = new JSONObject();
        ownerObject.put("name", name);
        ownerObject.put("chatId", chatId);

        CatOwner owner = new CatOwner();
        owner.setId(id);
        owner.setName(name);
        owner.setChatId(chatId);


        when(catOwnerRepository.save(any(CatOwner.class))).thenReturn(owner);
        when(catOwnerRepository.findOwnerById(id)).thenReturn(owner);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/cat-owners/{name}/{chatId}", name, chatId)
                        .content(ownerObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.chatId").value(chatId));
    }

    @Test
    void extendProbationaryPeriod() throws Exception {
        Integer id = 1;
        String name = "Alex";
        Integer days = 5;

        JSONObject ownerObject = new JSONObject();
        ownerObject.put("name", name);
        ownerObject.put("days", days);

        CatOwner owner = new CatOwner();
        owner.setId(id);
        owner.setName(name);
        owner.setDateOfEndProbation(LocalDateTime.now());
        owner.setPeriodExtend(days);


        when(catOwnerRepository.save(any(CatOwner.class))).thenReturn(owner);
        when(catOwnerRepository.findOwnerById(id)).thenReturn(owner);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/cat-owners/{id}/{days}", id, days)
                        .content(ownerObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.periodExtend").value(days));
    }

    @Test
    void changeProbationaryStatus() {
    }

    @Test
    void findCatOwnerById() throws Exception {
        Integer id = 1;
        String name = "Alex";

        CatOwner owner = new CatOwner();
        owner.setId(id);
        owner.setName(name);

        when(catOwnerRepository.findOwnerById(id)).thenReturn(owner);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/cat-owners/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name));
    }
}