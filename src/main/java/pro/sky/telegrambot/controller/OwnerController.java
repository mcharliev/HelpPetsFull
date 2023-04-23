package pro.sky.telegrambot.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pro.sky.telegrambot.exception.AlreadyExistException;
import pro.sky.telegrambot.model.ErrorDetails;
import pro.sky.telegrambot.model.Owner;
import pro.sky.telegrambot.service.OwnerService;

@RestController
public class OwnerController {
    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }
    @PostMapping("/owner")
    public ResponseEntity<Owner> saveOwner(@RequestParam(required = false) String name,
                                           @RequestParam(required = false) Long chatId) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(ownerService.saveOwnerByNameAndChatId(name, chatId));
    }
}
