package pro.sky.telegrambot.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pro.sky.telegrambot.exception.NotFoundException;
import pro.sky.telegrambot.model.ErrorDetails;

import pro.sky.telegrambot.model.Report;
import pro.sky.telegrambot.service.ReportService;


@RestController
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @Operation(summary = "Search reports by owner id",
            responses = {
                    @ApiResponse(
                            responseCode = "202",
                            description = "Found reports",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Report.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Reports with this owner id not found!")
            },
            tags = "Reports"
    )
    @GetMapping(value = "/reports/{ownerId}")
    public ResponseEntity<?> findReportsByOwnerId(@Parameter(description = "Owner's id", example = "1")
            @PathVariable(required = false) Integer ownerId) {
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(reportService.findReportsByOwnerId(ownerId));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorDetails("Reports with this owner id not found!"));
        }
    }
}
