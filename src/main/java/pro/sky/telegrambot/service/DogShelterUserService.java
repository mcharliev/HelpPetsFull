package pro.sky.telegrambot.service;

import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.DogShelterUser;
import pro.sky.telegrambot.repository.DogShelterUsersRepository;

@Service
public class DogShelterUserService {
    private final DogShelterUsersRepository dogShelterUsersRepository;

    public DogShelterUserService(DogShelterUsersRepository dogShelterUsersRepository) {
        this.dogShelterUsersRepository = dogShelterUsersRepository;
    }

    public DogShelterUser addUser(String phoneNumber, String name) {
        DogShelterUser dogShelterUser = new DogShelterUser();
        dogShelterUser.setPhoneNumber(phoneNumber);
        dogShelterUser.setName(name);
        return dogShelterUsersRepository.save(dogShelterUser);
    }
}
