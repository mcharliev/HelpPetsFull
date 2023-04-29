package pro.sky.telegrambot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.telegrambot.enam.ProbationaryStatus;
import pro.sky.telegrambot.exception.AlreadyExistException;
import pro.sky.telegrambot.exception.NotFoundException;
import pro.sky.telegrambot.exception.WrongInputDataException;
import pro.sky.telegrambot.model.ErrorDetails;
import pro.sky.telegrambot.model.Owner;
import pro.sky.telegrambot.service.OwnerService;

@RestController
public class OwnerController {
    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @Operation(summary = "Save owner in database",
            responses = {
                    @ApiResponse(
                            responseCode = "202",
                            description = "Owner saved in database",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Owner.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Owner already exist!")
            },
            tags = "Owners"
    )
    @PostMapping("/saveOwner/{name}/{chatId}")
    public ResponseEntity<?> saveOwner(@Parameter(description = "Owner's name", example = "Alex")
                                       @PathVariable(required = false) String name,
                                       @Parameter(description = "Owner's chatId", example = "321583984")
                                       @PathVariable(required = false) Long chatId) {
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(ownerService.saveOwnerByNameAndChatId(name, chatId));
        } catch (AlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorDetails("Owner already exist!"));
        }
    }

    @Operation(summary = "Extend owner's probation period",
            responses = {
                    @ApiResponse(
                            responseCode = "202",
                            description = "Probation period extended",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Owner.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "The probationary period cannot be extended"),
            },
            tags = "Owners"
    )
    @PutMapping("/extendTrialPeriod/{id}/{days}")
    public ResponseEntity<?> extendProbationaryPeriod(@Parameter(description = "Owner's id", example = "1")
                                                      @PathVariable(required = false) Integer id,
                                                      @Parameter(description = "Days", example = "5")
                                                      @PathVariable(required = false) Integer days) {
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(ownerService.extendProbationaryPeriod(id, days));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorDetails("Owner with id: " + id + "  not found"));
        } catch (WrongInputDataException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorDetails("The probationary period cannot be extended on " + days + " days"));
        }
    }

    @Operation(summary = "Change owner's probationary status",
            responses = {
                    @ApiResponse(
                            responseCode = "202",
                            description = "Probation status changed",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Owner.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Owner with this id not found")
            },
            tags = "Owners"
    )
    @PutMapping("/changeStatus")
    public ResponseEntity<?> changeProbationaryStatus(@Parameter(description = "Owner's id", example = "1")
                                                      @RequestParam(required = false) Integer id,
                                                      @Parameter(description = "Probationary status", example = "PASSED")
                                                      @RequestParam(required = false) ProbationaryStatus status) {
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(ownerService.changeProbationaryStatus(id, status));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorDetails("Owner with id: " + id + "  not found"));
        }
    }

    @Operation(summary = "Search for an owner by id",
            responses = {
                    @ApiResponse(
                            responseCode = "202",
                            description = "Found owner",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Owner.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Owner with this id not found")
            },
            tags = "Owners"
    )
    @GetMapping("/findById/{id}")
    public ResponseEntity<?> findOwnerById(@Parameter(description = "Owner's id", example = "1")
                                           @PathVariable(required = false) Integer id) {
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(ownerService.findOwnerById(id));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorDetails("Owner with id: " + id + "  not found"));
        }
    }
}
