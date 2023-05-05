package pro.sky.telegrambot.service;

import org.springframework.stereotype.Service;
import pro.sky.telegrambot.exception.AlreadyExistException;
import pro.sky.telegrambot.exception.NotFoundException;
import pro.sky.telegrambot.model.Dog;
import pro.sky.telegrambot.repository.DogRepository;

import java.time.LocalDate;

@Service
public class DogService {
    private final DogRepository dogRepository;
    private final DogOwnerService ownerService;

    public DogService(DogRepository dogRepository, DogOwnerService ownerService) {
        this.dogRepository = dogRepository;
        this.ownerService = ownerService;
    }

    public Dog saveDogWithNameBirthDateAndBreed(String name,
                                                LocalDate birthDate,
                                                String breed) {
        Dog dog = new Dog();
        dog.setName(name);
        dog.setBirthDate(birthDate);
        dog.setBreed(breed);
        dogRepository.findAll()
                .forEach(dogs -> {
                    if (dogs.equals(dog)) {
                        throw new AlreadyExistException();
                    }
                });
        return dogRepository.save(dog);
    }

    public Dog assignDogWithOwner(Integer ownerId, Integer dogId) {
        Dog dog = dogRepository.findDogById(dogId);
        if (dog == null) {
            throw new NotFoundException();
        }
        dog.setOwner(ownerService.findOwnerById(ownerId));
        dogRepository.save(dog);
        return dog;
    }

    public Dog findDogById(Integer id) {
        Dog dog = dogRepository.findDogById(id);
        if (dog == null) {
            throw new NotFoundException();
        }
        return dog;
    }
}

