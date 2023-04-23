package pro.sky.telegrambot.service;

import org.springframework.stereotype.Service;
import pro.sky.telegrambot.exception.AlreadyExistException;
import pro.sky.telegrambot.model.Dog;
import pro.sky.telegrambot.repository.DogRepository;

import java.time.LocalDate;

@Service
public class DogService {
    private final DogRepository dogRepository;

    public DogService(DogRepository dogRepository) {
        this.dogRepository = dogRepository;
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
}

