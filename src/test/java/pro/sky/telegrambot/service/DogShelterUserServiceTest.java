package pro.sky.telegrambot.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambot.model.DogShelterUser;
import pro.sky.telegrambot.repository.DogShelterUsersRepository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DogShelterUserServiceTest {
    @Mock
    private DogShelterUsersRepository dogShelterUsersRepository;

    @InjectMocks
    private DogShelterUserService dogShelterUserService;

    @Test
    public void addUserTest() {
        DogShelterUser dogShelterUser = new DogShelterUser();
        dogShelterUser.setPhoneNumber("39113334455");
        dogShelterUser.setName("Alex");
        dogShelterUser.setId(1);
        when(dogShelterUsersRepository.save(ArgumentMatchers.any(DogShelterUser.class))).thenReturn(dogShelterUser);
        DogShelterUser savedDogShelterUser = dogShelterUsersRepository.save(dogShelterUser);
        assertThat(savedDogShelterUser).isNotNull();
        assertThat(savedDogShelterUser.getId()).isGreaterThan(0);
        assertThat(savedDogShelterUser.getName()).isEqualTo("Alex");
        assertThat(savedDogShelterUser.getPhoneNumber()).isEqualTo("39113334455");
    }
}