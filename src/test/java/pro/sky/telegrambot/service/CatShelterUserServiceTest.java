package pro.sky.telegrambot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambot.model.CatShelterUser;
import pro.sky.telegrambot.model.DogShelterUser;
import pro.sky.telegrambot.repository.CatShelterUsersRepository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CatShelterUserServiceTest {
    @Mock
    private CatShelterUsersRepository catShelterUsersRepository;

    @InjectMocks
    private DogShelterUserService dogShelterUserService;

    @Test
    void addUserTest() {
        CatShelterUser catShelterUser = new CatShelterUser();
        catShelterUser.setPhoneNumber("39113334455");
        catShelterUser.setName("Alex");
        catShelterUser.setId(1);
        when(catShelterUsersRepository.save(ArgumentMatchers.any(CatShelterUser.class))).thenReturn(catShelterUser);
        CatShelterUser savedCatShelterUser = catShelterUsersRepository.save(catShelterUser);
        assertThat(savedCatShelterUser).isNotNull();
        assertThat(savedCatShelterUser.getId()).isGreaterThan(0);
        assertThat(savedCatShelterUser.getName()).isEqualTo("Alex");
        assertThat(savedCatShelterUser.getPhoneNumber()).isEqualTo("39113334455");
    }
}