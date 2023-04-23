package pro.sky.telegrambot.controller.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pro.sky.telegrambot.exception.AlreadyExistException;
import pro.sky.telegrambot.model.ErrorDetails;

@RestControllerAdvice
public class ExceptionControllerAdvice {
    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<ErrorDetails> exceptionAlreadyExistException() {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setMessage("Entity already exist!");
        return ResponseEntity
                .badRequest()
                .body(errorDetails);
    }
}
