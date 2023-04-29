package pro.sky.telegrambot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pro.sky.telegrambot.model.Dog;
import pro.sky.telegrambot.model.Owner;
import pro.sky.telegrambot.repository.DogRepository;
import pro.sky.telegrambot.service.DogService;
import pro.sky.telegrambot.service.OwnerService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = DogController.class)
class DogControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DogRepository dogRepository;

    @SpyBean
    private DogService dogService;

    @MockBean
    private OwnerService ownerService;

    @Test
    void test_saveDog() throws Exception {
        Integer id = 1;
        String name = "Boss";
        String breed = "German shepherd";

        JSONObject dogObject = new JSONObject();
        dogObject.put("name", name);
        dogObject.put("breed", breed);

        Dog dog = new Dog();
        dog.setId(id);
        dog.setName(name);
        dog.setBreed(breed);
        when(dogRepository.save(any(Dog.class))).thenReturn(dog);
        when(dogRepository.findDogById(id)).thenReturn(dog);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/saveDog")
                        .content(dogObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.breed").value(breed));

    }

    @Test
    void test_assignDogWithOwner() throws Exception {
        Integer dogId = 1;
        String name = "Boss";
        String breed = "German shepherd";

        Owner owner = new Owner();
        Integer ownerId = 2;
        owner.setId(ownerId);
        owner.setName("Alex");

        Dog dog = new Dog();
        dog.setId(dogId);
        dog.setName(name);
        dog.setBreed(breed);

        when(dogRepository.findDogById(dogId)).thenReturn(dog);
        when(ownerService.findOwnerById(ownerId)).thenReturn(owner);
        dog.setOwner(owner);
        when(dogRepository.save(any(Dog.class))).thenReturn(dog);
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/assignDogWithOwner/{ownerId}/{dogId}", ownerId, dogId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value(dogId))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.breed").value(breed));

        //как тут проверить лежит ли объект в объекте, то есть лежит ли овнер в доге
//                .andExpect(jsonPath("$.owner").value(owner));

    }

    @Test
    void test_findDogById() throws Exception {
        Integer id = 1;
        String name = "Boss";
        String breed = "German shepherd";

        Dog dog = new Dog();
        dog.setId(id);
        dog.setName(name);
        dog.setBreed(breed);

        when(dogRepository.findDogById(id)).thenReturn(dog);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/findDogById/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.breed").value(breed));

    }
}