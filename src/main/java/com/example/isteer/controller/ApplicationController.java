package com.example.isteer.controller;

import com.example.isteer.dto.ErrorMessageDto;
import com.example.isteer.dto.StatusMessageDto;
import com.example.isteer.entity.Applications;
import com.example.isteer.enums.CVSSEnum;
import com.example.isteer.service.ApplicationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/applications")
public class ApplicationController {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationController.class);

    @Autowired
    ApplicationService applicationService;

    @PostMapping
    public ResponseEntity<?> createApplication(@RequestParam String uuid, @Valid @RequestBody Applications application) {
        int status = applicationService.createApplication(uuid, application);
        switch (status) {
            case 1:
                return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new StatusMessageDto(CVSSEnum.APPLICATION_ADD.getStatusCode(), CVSSEnum.APPLICATION_ADD.getStatusMessage()));
            case -1:
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorMessageDto(CVSSEnum.APPLICATION_NOT_FOUND.getStatusCode(), CVSSEnum.APPLICATION_NOT_FOUND.getStatusMessage()));
            case -3:
                	return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorMessageDto(CVSSEnum.COMPUTER_UUID_EMPTY.getStatusCode(), CVSSEnum.COMPUTER_UUID_EMPTY.getStatusMessage()));
            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorMessageDto(CVSSEnum.Internal_Server_Error.getStatusCode(), CVSSEnum.Internal_Server_Error.getStatusMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<Applications>> getAllApplications(@RequestParam (required = false) String computerUuid) {
        List<Applications> applications = applicationService.getAllApplications(computerUuid);
        if (applications.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(applications);
    }

    @PutMapping
    public ResponseEntity<?> updateApplication(@RequestParam String uuid, @Valid @RequestBody Applications application) {
        int status = applicationService.updateApplication(uuid, application);
        switch (status) {
            case 1:
                return ResponseEntity.ok(new StatusMessageDto(CVSSEnum.APPLICATION_UPDATE.getStatusCode(), CVSSEnum.APPLICATION_UPDATE.getStatusMessage()));
            case -1:
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorMessageDto(CVSSEnum.APPLICATION_NOT_FOUND.getStatusCode(), CVSSEnum.APPLICATION_NOT_FOUND.getStatusMessage()));
                case -2:
                	return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorMessageDto(CVSSEnum.APPLICATION_UUID_EMPTY.getStatusCode(), CVSSEnum.APPLICATION_UUID_EMPTY.getStatusMessage()));

            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorMessageDto(CVSSEnum.Internal_Server_Error.getStatusCode(), CVSSEnum.Internal_Server_Error.getStatusMessage()));
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteApplication(@RequestParam String uuid) {
        int status = applicationService.softDeleteApplication(uuid);
        switch (status) {
            case 1:
                return ResponseEntity.ok(new StatusMessageDto(CVSSEnum.APPLICATION_DELETE.getStatusCode(), CVSSEnum.APPLICATION_DELETE.getStatusMessage()));
            case -1:
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorMessageDto(CVSSEnum.APPLICATION_NOT_FOUND.getStatusCode(), CVSSEnum.APPLICATION_NOT_FOUND.getStatusMessage()));
            case -2:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorMessageDto(CVSSEnum.APPLICATION_UUID_EMPTY.getStatusCode(), CVSSEnum.APPLICATION_UUID_EMPTY.getStatusMessage()));
            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorMessageDto(CVSSEnum.Internal_Server_Error.getStatusCode(), CVSSEnum.Internal_Server_Error.getStatusMessage()));
        }
    }
}
