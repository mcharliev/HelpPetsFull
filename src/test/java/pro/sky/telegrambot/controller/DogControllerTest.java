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
import pro.sky.telegrambot.model.Dog;
import pro.sky.telegrambot.model.DogOwner;
import pro.sky.telegrambot.repository.DogRepository;
import pro.sky.telegrambot.service.DogService;
import pro.sky.telegrambot.service.DogOwnerService;

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
    private DogOwnerService ownerService;

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

        DogOwner owner = new DogOwner();
        Integer ownerId = 2;
        String ownerName = "Alex";
        owner.setId(ownerId);
        owner.setName(ownerName);

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
                .andExpect(jsonPath("$.breed").value(breed))
                .andExpect(jsonPath("$.owner.id").value(owner.getId()))
                .andExpect(jsonPath("$.owner.name").value(owner.getName()));
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