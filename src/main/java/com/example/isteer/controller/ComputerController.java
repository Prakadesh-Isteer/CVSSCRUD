package com.example.isteer.controller;

import com.example.isteer.dto.ErrorMessageDto;
import com.example.isteer.dto.StatusMessageDto;
import com.example.isteer.entity.Computers;
import com.example.isteer.enums.CVSSEnum;
import com.example.isteer.service.ComputerService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/computers")
public class ComputerController {

    private final ComputerService computerService;

    public ComputerController(ComputerService computerService) {
        this.computerService = computerService;
    }

    @PostMapping
    public ResponseEntity<?> createMachine(@Valid @RequestBody Computers computers) {
        int status = computerService.createMachine(computers);

        switch (status) {
            case 1:
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new StatusMessageDto(CVSSEnum.COMPUTER_ADD.getStatusCode(), CVSSEnum.COMPUTER_ADD.getStatusMessage()));
            case -4:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorMessageDto(CVSSEnum.IP_ADDRESS_INVALID.getStatusCode(), CVSSEnum.IP_ADDRESS_INVALID.getStatusMessage()));
            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorMessageDto(CVSSEnum.Internal_Server_Error.getStatusCode(), CVSSEnum.Internal_Server_Error.getStatusMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllMachines() {
        List<Computers> machines = computerService.getAllMachines();

        if (machines.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(machines);
    }

    @PutMapping
    public ResponseEntity<?> updateMachine(@RequestParam String uuid, @Valid @RequestBody Computers computers) {
        int status = computerService.updateMachine(uuid, computers);

        switch (status) {
            case 1:
                return ResponseEntity.ok(new StatusMessageDto(CVSSEnum.COMPUTER_UPDATE.getStatusCode(), CVSSEnum.COMPUTER_UPDATE.getStatusMessage()));
            case 0:
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ErrorMessageDto(CVSSEnum.COMPUTER_WITH_SAME_IP_EXISTS.getStatusCode(), CVSSEnum.COMPUTER_WITH_SAME_IP_EXISTS.getStatusMessage()));
            case -1:
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorMessageDto(CVSSEnum.COMPUTER_NOT_FOUND.getStatusCode(), CVSSEnum.COMPUTER_NOT_FOUND.getStatusMessage()));
            case -2:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorMessageDto(CVSSEnum.IP_ADDRESS_INVALID.getStatusCode(), CVSSEnum.IP_ADDRESS_INVALID.getStatusMessage()));
            case -3:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorMessageDto(CVSSEnum.COMPUTER_UUID_EMPTY.getStatusCode(), CVSSEnum.COMPUTER_UUID_EMPTY.getStatusMessage()));
            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorMessageDto(CVSSEnum.Internal_Server_Error.getStatusCode(), CVSSEnum.Internal_Server_Error.getStatusMessage()));
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteMachine(@RequestParam String uuid) {
        int status = computerService.softDeleteMachine(uuid);

        switch (status) {
            case 1:
                return ResponseEntity.ok(new StatusMessageDto(CVSSEnum.COMPUTER_DELETE.getStatusCode(), CVSSEnum.COMPUTER_DELETE.getStatusMessage()));
            case -1:
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorMessageDto(CVSSEnum.COMPUTER_NOT_FOUND.getStatusCode(), CVSSEnum.COMPUTER_NOT_FOUND.getStatusMessage()));
                case -2:
                	return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new ErrorMessageDto(CVSSEnum.COMPUTER_UUID_EMPTY.getStatusCode(), CVSSEnum.COMPUTER_UUID_EMPTY.getStatusMessage()));
            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorMessageDto(CVSSEnum.Internal_Server_Error.getStatusCode(), CVSSEnum.Internal_Server_Error.getStatusMessage()));
        }
    }

    @PatchMapping("/deactivate")
    public ResponseEntity<?> deactivateMachine(@RequestParam String uuid) {
        int status = computerService.deactivateMachine(uuid);

        switch (status) {
            case 1:
                return ResponseEntity.ok(new StatusMessageDto(CVSSEnum.COMPUTER_DEACTIVATED.getStatusCode(), CVSSEnum.COMPUTER_DEACTIVATED.getStatusMessage()));
            case -1:
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorMessageDto(CVSSEnum.COMPUTER_NOT_FOUND.getStatusCode(), CVSSEnum.COMPUTER_NOT_FOUND.getStatusMessage()));
            case -2:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorMessageDto(CVSSEnum.COMPUTER_UUID_EMPTY.getStatusCode(), CVSSEnum.COMPUTER_UUID_EMPTY.getStatusMessage()));
            case 0:
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ErrorMessageDto(CVSSEnum.COMPUTER_NOT_FOUND.getStatusCode(), "Computer is already deactivated Or Deleted."));
            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorMessageDto(CVSSEnum.Internal_Server_Error.getStatusCode(), CVSSEnum.Internal_Server_Error.getStatusMessage()));
        }
    }
}
