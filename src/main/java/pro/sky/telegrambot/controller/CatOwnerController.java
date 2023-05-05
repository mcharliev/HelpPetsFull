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
import pro.sky.telegrambot.enam.ProbationaryStatus;
import pro.sky.telegrambot.exception.AlreadyExistException;
import pro.sky.telegrambot.exception.NotFoundException;
import pro.sky.telegrambot.exception.WrongInputDataException;
import pro.sky.telegrambot.model.DogOwner;
import pro.sky.telegrambot.model.ErrorDetails;
import pro.sky.telegrambot.service.CatOwnerService;
import pro.sky.telegrambot.service.DogOwnerService;

@RestController
public class CatOwnerController {
    private final CatOwnerService catOwnerService;

    public CatOwnerController(CatOwnerService catOwnerService) {
        this.catOwnerService = catOwnerService;
    }

    @Operation(summary = "Save cat owner in database",
            responses = {
                    @ApiResponse(
                            responseCode = "202",
                            description = "Owner saved in database",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = DogOwner.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Owner already exist!")
            },
            tags = "Cat owners"
    )
    @PostMapping("/saveCatOwner/{name}/{chatId}")
    public ResponseEntity<?> saveOwner(@Parameter(description = "Owner's name", example = "Alex")
                                       @PathVariable(required = false) String name,
                                       @Parameter(description = "Owner's chatId", example = "321583984")
                                       @PathVariable(required = false) Long chatId) {
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(catOwnerService.saveOwnerByNameAndChatId(name, chatId));
        } catch (AlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorDetails("Cat owner already exist!"));
        }
    }

    @Operation(summary = "Extend owner's probation period",
            responses = {
                    @ApiResponse(
                            responseCode = "202",
                            description = "Probation period extended",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = DogOwner.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "The probationary period cannot be extended"),
            },
            tags = "Cat owners"
    )
    @PutMapping("/extendCatOwnerTrialPeriod/{id}/{days}")
    public ResponseEntity<?> extendProbationaryPeriod(@Parameter(description = "Owner's id", example = "1")
                                                      @PathVariable(required = false) Integer id,
                                                      @Parameter(description = "Days", example = "5")
                                                      @PathVariable(required = false) Integer days) {
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(catOwnerService.extendProbationaryPeriod(id, days));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorDetails("Dog owner with id: " + id + "  not found"));
        } catch (WrongInputDataException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorDetails("The probationary period cannot be extended on " + days + " days"));
        }
    }

    @Operation(summary = "Change dog owner's probationary status",
            responses = {
                    @ApiResponse(
                            responseCode = "202",
                            description = "Probation status changed",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = DogOwner.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Cat owner with this id not found")
            },
            tags = "Cat owners"
    )
    @PutMapping("/changeCatOwnerStatus")
    public ResponseEntity<?> changeProbationaryStatus(@Parameter(description = "Owner's id", example = "1")
                                                      @RequestParam(required = false) Integer id,
                                                      @Parameter(description = "Probationary status", example = "PASSED")
                                                      @RequestParam(required = false) ProbationaryStatus status) {
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(catOwnerService.changeProbationaryStatus(id, status));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorDetails("Owner with id: " + id + "  not found"));
        }
    }

    @Operation(summary = "Search for an cat owner by id",
            responses = {
                    @ApiResponse(
                            responseCode = "202",
                            description = "Found owner",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = DogOwner.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Cat owner with this id not found")
            },
            tags = "Cat owners"
    )
    @GetMapping("/findCatOwnerById/{id}")
    public ResponseEntity<?> findOwnerById(@Parameter(description = "Owner's id", example = "1")
                                           @PathVariable(required = false) Integer id) {
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(catOwnerService.findOwnerById(id));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorDetails("Dog owner with id: " + id + "  not found"));
        }
    }
}
