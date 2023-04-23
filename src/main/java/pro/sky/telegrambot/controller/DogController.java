package pro.sky.telegrambot.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.telegrambot.exception.AlreadyExistException;
import pro.sky.telegrambot.model.Dog;
import pro.sky.telegrambot.model.ErrorDetails;
import pro.sky.telegrambot.service.DogService;

import java.time.LocalDate;

@RestController
public class DogController {
    private final DogService dogService;

    public DogController(DogService dogService) {
        this.dogService = dogService;
    }

    @PostMapping("/dog")
    public ResponseEntity<Dog> saveDog(@RequestParam(required = false) String name,
                                       @RequestParam(required = false) LocalDate birthDate,
                                       @RequestParam(required = false) String breed) {
         return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                 dogService.saveDogWithNameBirthDateAndBreed(name, birthDate, breed));
    }
}
