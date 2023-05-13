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
import pro.sky.telegrambot.model.Cat;
import pro.sky.telegrambot.model.CatOwner;
import pro.sky.telegrambot.model.Dog;
import pro.sky.telegrambot.model.DogOwner;
import pro.sky.telegrambot.repository.CatRepository;
import pro.sky.telegrambot.service.CatOwnerService;
import pro.sky.telegrambot.service.CatService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CatController.class)
class CatControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CatRepository catRepository;

    @SpyBean
    private CatService catService;

    @MockBean
    private CatOwnerService catOwnerService;

    @Test
    void saveCat() throws Exception {
        Integer id = 1;
        String name = "Barsik";
        String breed = "British";

        JSONObject dogObject = new JSONObject();
        dogObject.put("name", name);
        dogObject.put("breed", breed);

        Cat cat = new Cat();
        cat.setId(id);
        cat.setName(name);
        cat.setBreed(breed);
        when(catRepository.save(any(Cat.class))).thenReturn(cat);
        when(catRepository.findCatById(id)).thenReturn(cat);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/cats")
                        .content(dogObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.breed").value(breed));
    }

    @Test
    void assignDogWithOwner() throws Exception {
        Integer catId = 1;
        String name = "Barsik";
        String breed = "British";

        CatOwner owner = new CatOwner();
        Integer ownerId = 2;
        String ownerName = "Alex";
        owner.setId(ownerId);
        owner.setName(ownerName);

        Cat cat = new Cat();
        cat.setId(catId);
        cat.setName(name);
        cat.setBreed(breed);

        when(catRepository.findCatById(catId)).thenReturn(cat);
        when(catOwnerService.findOwnerById(ownerId)).thenReturn(owner);
        cat.setOwner(owner);
        when(catRepository.save(any(Cat.class))).thenReturn(cat);
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/cats/{ownerId}/{catId}", ownerId, catId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value(catId))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.breed").value(breed))
                .andExpect(jsonPath("$.owner.id").value(owner.getId()))
                .andExpect(jsonPath("$.owner.name").value(owner.getName()));
    }

    @Test
    void findCatById() throws Exception {
        Integer id = 1;
        String name = "Barsik";
        String breed = "British";

        Cat cat = new Cat();
        cat.setId(id);
        cat.setName(name);
        cat.setBreed(breed);

        when(catRepository.findCatById(id)).thenReturn(cat);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/cats/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.breed").value(breed));
    }
}