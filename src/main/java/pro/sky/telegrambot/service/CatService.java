package pro.sky.telegrambot.service;

import org.springframework.stereotype.Service;
import pro.sky.telegrambot.exception.AlreadyExistException;
import pro.sky.telegrambot.exception.NotFoundException;
import pro.sky.telegrambot.model.Cat;
import pro.sky.telegrambot.repository.CatRepository;

import java.time.LocalDate;

@Service
public class CatService {
    private final CatRepository catRepository;
    private final OwnerService ownerService;

    public CatService(CatRepository catRepository,
                      OwnerService ownerService) {
        this.catRepository = catRepository;
        this.ownerService = ownerService;
    }

    public Cat saveCatWithNameBirthDateAndBreed(String name,
                       LocalDate birthDate,
                       String breed) {
        Cat cat = new Cat();
        cat.setName(name);
        cat.setBirthDate(birthDate);
        cat.setBreed(breed);
        catRepository.findAll()
                .forEach(cats -> {
                    if (cats.equals(cat)) {
                        throw new AlreadyExistException();
                    }
                });
        return catRepository.save(cat);
    }

    public Cat assignCatWithOwner(Integer ownerId, Integer catId) {
        Cat cat = catRepository.findCatById(catId);
        if (cat == null) {
            throw new NotFoundException();
        }
        cat.setOwner(ownerService.findOwnerById(ownerId));
        catRepository.save(cat);
        return cat;
    }

    public Cat findCatById(Integer id) {
        Cat cat = catRepository.findCatById(id);
        if (cat == null) {
            throw new NotFoundException();
        }
        return cat;
    }
}
