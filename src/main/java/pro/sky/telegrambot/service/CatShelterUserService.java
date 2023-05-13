package pro.sky.telegrambot.service;

import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.CatShelterUser;
import pro.sky.telegrambot.repository.CatShelterUsersRepository;

@Service
public class CatShelterUserService {
    private final CatShelterUsersRepository catShelterUsersRepository;

    public CatShelterUserService(CatShelterUsersRepository catShelterUsersRepository) {
        this.catShelterUsersRepository = catShelterUsersRepository;
    }

    public CatShelterUser addUser(String phoneNumber, String name) {
        CatShelterUser catShelterUser = new CatShelterUser();
        catShelterUser.setPhoneNumber(phoneNumber);
        catShelterUser.setName(name);
        return catShelterUsersRepository.save(catShelterUser);
    }
}
