package pro.sky.telegrambot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.telegrambot.exception.AlreadyExistException;
import pro.sky.telegrambot.exception.NotFoundException;
import pro.sky.telegrambot.model.Dog;
import pro.sky.telegrambot.model.ErrorDetails;
import pro.sky.telegrambot.model.Owner;
import pro.sky.telegrambot.service.DogService;

import java.time.LocalDate;

@RestController
public class DogController {
    private final DogService dogService;

    public DogController(DogService dogService) {
        this.dogService = dogService;
    }

    @Operation(summary = "Save dog in database",
            responses = {
                    @ApiResponse(
                            responseCode = "202",
                            description = "Dog saved in database",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Dog.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dog already exist!")
            },
            tags = "Dogs"
    )
    @PostMapping("/saveDog")
    public ResponseEntity<?> saveDog(@Parameter(description = "Dog's name", example = "Boss")
                                     @RequestParam(required = false) String name,
                                     @Parameter(description = "Dog's birth date", example = "19.10.2019")
                                     @RequestParam(required = false) LocalDate birthDate,
                                     @Parameter(description = "Breed of dog", example = "German shepherd")
                                     @RequestParam(required = false) String breed) {
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                    dogService.saveDogWithNameBirthDateAndBreed(name, birthDate, breed));
        } catch (AlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorDetails("Dog already exist!"));
        }
    }

    @Operation(summary = "Assign a dog to an owner",
            responses = {
                    @ApiResponse(
                            responseCode = "202",
                            description = "Dog assigned to owner",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Dog.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Owner or dog with input id not found!")
            },
            tags = "Dogs"
    )
    @PutMapping("/assignDogWithOwner/{ownerId}/{dogId}")
    public ResponseEntity<?> assignDogWithOwner(@Parameter(description = "Owner's id", example = "1")
                                                @PathVariable(required = false) Integer ownerId,
                                                @Parameter(description = "Dog's id", example = "2")
                                                @PathVariable(required = false) Integer dogId) {
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(dogService.assignDogWithOwner(ownerId, dogId));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorDetails("Owner or dog with input id not found"));
        }
    }

    @Operation(summary = "Search for an dog by id",
            responses = {
                    @ApiResponse(
                            responseCode = "202",
                            description = "Found dog",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Dog.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dog with this id not found")
            },
            tags = "Dogs"
    )
    @GetMapping("/findDogById/{id}")
    public ResponseEntity<?> findDogById(@Parameter(description = "Dog's id", example = "1")
                                         @PathVariable(required = false) Integer id) {
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(dogService.findDogById(id));

        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorDetails("Dog with id: " + id + "  not found"));
        }
    }
}
