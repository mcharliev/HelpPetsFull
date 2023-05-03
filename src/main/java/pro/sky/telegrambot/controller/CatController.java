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
import pro.sky.telegrambot.model.Cat;
import pro.sky.telegrambot.model.Dog;
import pro.sky.telegrambot.model.ErrorDetails;
import pro.sky.telegrambot.service.CatService;

import java.time.LocalDate;

@RestController
public class CatController {
    private final CatService catService;

    public CatController(CatService catService) {
        this.catService = catService;
    }
    @Operation(summary = "Save cat in database",
            responses = {
                    @ApiResponse(
                            responseCode = "202",
                            description = "Cat saved in database",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Cat.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Cat already exist!")
            },
            tags = "Cats"
    )
    @PostMapping("/saveCat")
    public ResponseEntity<?> saveCat(@Parameter(description = "Cat's name", example = "Barsik")
                                     @RequestParam(required = false) String name,
                                     @Parameter(description = "Cat's birth date", example = "19.10.2019")
                                     @RequestParam(required = false) LocalDate birthDate,
                                     @Parameter(description = "Breed of cat", example = "British")
                                     @RequestParam(required = false) String breed) {
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(
             catService.saveCatWithNameBirthDateAndBreed(name,birthDate,breed));
        } catch (AlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorDetails("Cat already exist!"));
        }
    }
    @Operation(summary = "Assign a cat to an owner",
            responses = {
                    @ApiResponse(
                            responseCode = "202",
                            description = "Cat assigned to owner",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Cat.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Owner or cat with input id not found!")
            },
            tags = "Cats"
    )
    @PutMapping("/assignCatWithOwner/{ownerId}/{catId}")
    public ResponseEntity<?> assignDogWithOwner(@Parameter(description = "Owner's id", example = "1")
                                                @PathVariable(required = false) Integer ownerId,
                                                @Parameter(description = "Cat's id", example = "2")
                                                @PathVariable(required = false) Integer catId) {
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(catService.assignCatWithOwner(ownerId, catId));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorDetails("Owner or cat with input id not found"));
        }
    }
    @Operation(summary = "Search for an cat by id",
            responses = {
                    @ApiResponse(
                            responseCode = "202",
                            description = "Found cat",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Dog.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Cat with this id not found")
            },
            tags = "Cats"
    )
    @GetMapping("/findCatById/{id}")
    public ResponseEntity<?> findCatById(@Parameter(description = "Cat's id", example = "1")
                                         @PathVariable(required = false) Integer id) {
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(catService.findCatById(id));

        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorDetails("Cat with id: " + id + "  not found"));
        }
    }
}
