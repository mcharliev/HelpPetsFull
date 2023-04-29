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
import pro.sky.telegrambot.model.Owner;
import pro.sky.telegrambot.repository.OwnerRepository;
import pro.sky.telegrambot.service.OwnerService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = OwnerController.class)
class OwnerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OwnerRepository ownerRepository;

    @SpyBean
    private OwnerService ownerService;


    @Test
    void test_saveOwner() throws Exception {
        Integer id = 1;
        String name = "Alex";
        Long chatId = 1525L;

        JSONObject ownerObject = new JSONObject();
        ownerObject.put("name", name);
        ownerObject.put("chatId", chatId);

        Owner owner = new Owner();
        owner.setId(id);
        owner.setName(name);
        owner.setChatId(chatId);


        when(ownerRepository.save(any(Owner.class))).thenReturn(owner);
        when(ownerRepository.findOwnerById(id)).thenReturn(owner);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/saveOwner/{name}/{chatId}", name, chatId)
                        .content(ownerObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.chatId").value(chatId));
    }

    @Test
    void test_findOwnerById() throws Exception {
        Integer id = 1;
        String name = "Alex";

        Owner owner = new Owner();
        owner.setId(id);
        owner.setName(name);

        when(ownerRepository.findOwnerById(id)).thenReturn(owner);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/findById/{id}", id)
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

        Owner owner = new Owner();
        owner.setId(id);
        owner.setName(name);
        owner.setPeriodExtend(days);


        when(ownerRepository.save(any(Owner.class))).thenReturn(owner);
        when(ownerRepository.findOwnerById(id)).thenReturn(owner);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/extendTrialPeriod/{id}/{days}", id, days)
                        .content(ownerObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.periodExtend").value(days));
    }
}