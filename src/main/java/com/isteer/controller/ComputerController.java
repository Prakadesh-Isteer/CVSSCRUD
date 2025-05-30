package com.isteer.controller;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.isteer.dto.ErrorMessageDto;
import com.isteer.dto.StatusMessageDto;
import com.isteer.entity.Computer;
import com.isteer.enums.CVSSEnum;
import com.isteer.service.ComputerService;
import com.isteer.util.StatusMessageUtil;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ComputerController {

	@Autowired
	ComputerService computerService;

	@Autowired
	StatusMessageUtil statusMessageUtil;

	@PostMapping("/computer")
	public ResponseEntity<?> createMachine(@Valid @RequestBody Computer computer) {
		int status = computerService.createMachine(computer);

		switch (status) {
		case 1:
			return ResponseEntity.status(HttpStatus.CREATED).body(new StatusMessageDto(
					CVSSEnum.COMPUTER_ADD.getStatusCode(), StatusMessageUtil.getMessage(CVSSEnum.COMPUTER_ADD)));
		case -4:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorMessageDto(CVSSEnum.IP_ADDRESS_INVALID.getStatusCode(),
							StatusMessageUtil.getMessage(CVSSEnum.IP_ADDRESS_INVALID)));
		default:
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorMessageDto(CVSSEnum.Internal_Server_Error.getStatusCode(),
							StatusMessageUtil.getMessage(CVSSEnum.Internal_Server_Error)));
		}
	}

	@GetMapping("/allComputers")
	public ResponseEntity<?> getAllComputers() {
		List<Computer> machines = computerService.getAllComputers();

		if (machines.isEmpty()) {
			return ResponseEntity.noContent().build();
		}

		return ResponseEntity.ok(machines);
	}

	@GetMapping("/uuid/computer")
	public ResponseEntity<?> getComputerByUuid(@RequestParam String computerUuid) {
		Computer computer = computerService.findComputerByUuid(computerUuid);

		if (computer == null || computerUuid.trim().isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorMessageDto(CVSSEnum.COMPUTER_NOT_FOUND.getStatusCode(),
							StatusMessageUtil.getMessage(CVSSEnum.COMPUTER_NOT_FOUND)));
		} else if (computer.getUuid() == null || computer.getUuid().trim().isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorMessageDto(CVSSEnum.COMPUTER_UUID_EMPTY.getStatusCode(),
							StatusMessageUtil.getMessage(CVSSEnum.COMPUTER_UUID_EMPTY)));
		}

		return ResponseEntity.ok(computer);
	}

	@PutMapping("computer")
	public ResponseEntity<?> updateMachine(@RequestParam String computerUuid, @Valid @RequestBody Computer computer) {
		int status = computerService.updateMachine(computerUuid, computer);

		switch (status) {
		case 1:
			return ResponseEntity.status(HttpStatus.OK).body(new StatusMessageDto(CVSSEnum.COMPUTER_ADD.getStatusCode(),
					StatusMessageUtil.getMessage(CVSSEnum.COMPUTER_UPDATE)));
		case 0:
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(new ErrorMessageDto(CVSSEnum.COMPUTER_WITH_SAME_IP_EXISTS.getStatusCode(),
							StatusMessageUtil.getMessage(CVSSEnum.COMPUTER_WITH_SAME_IP_EXISTS)));
		case -1:
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorMessageDto(CVSSEnum.COMPUTER_NOT_FOUND.getStatusCode(),
							StatusMessageUtil.getMessage(CVSSEnum.COMPUTER_NOT_FOUND)));
		case -2:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorMessageDto(CVSSEnum.IP_ADDRESS_INVALID.getStatusCode(),
							StatusMessageUtil.getMessage(CVSSEnum.IP_ADDRESS_INVALID)));
		case -3:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorMessageDto(CVSSEnum.COMPUTER_UUID_EMPTY.getStatusCode(),
							StatusMessageUtil.getMessage(CVSSEnum.COMPUTER_UUID_EMPTY)));
		default:
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorMessageDto(CVSSEnum.Internal_Server_Error.getStatusCode(),
							StatusMessageUtil.getMessage(CVSSEnum.Internal_Server_Error)));
		}
	}

	@DeleteMapping("/computer")
	public ResponseEntity<?> deleteMachine(@RequestParam String computerUuid) {
		int status = computerService.softDeleteMachine(computerUuid);

		switch (status) {
		case 1:
			return ResponseEntity.ok(new StatusMessageDto(CVSSEnum.COMPUTER_DELETE.getStatusCode(),
					StatusMessageUtil.getMessage(CVSSEnum.COMPUTER_DELETE)));
		case -1:
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorMessageDto(CVSSEnum.COMPUTER_NOT_FOUND.getStatusCode(),
							StatusMessageUtil.getMessage(CVSSEnum.COMPUTER_NOT_FOUND)));
		case -2:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorMessageDto(CVSSEnum.COMPUTER_UUID_EMPTY.getStatusCode(),
							StatusMessageUtil.getMessage(CVSSEnum.COMPUTER_UUID_EMPTY)));
		default:
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorMessageDto(CVSSEnum.Internal_Server_Error.getStatusCode(),
							StatusMessageUtil.getMessage(CVSSEnum.Internal_Server_Error)));
		}
	}

	@PatchMapping("computer/deactivate")
	public ResponseEntity<?> deactivateMachine(@RequestParam String computerUuid) {
		int status = computerService.deactivateMachine(computerUuid);

		switch (status) {
		case 1:
			return ResponseEntity.ok(new StatusMessageDto(CVSSEnum.COMPUTER_DEACTIVATED.getStatusCode(),
					StatusMessageUtil.getMessage(CVSSEnum.COMPUTER_DEACTIVATED)));
		case -1:
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorMessageDto(CVSSEnum.COMPUTER_NOT_FOUND.getStatusCode(),
							StatusMessageUtil.getMessage(CVSSEnum.COMPUTER_NOT_FOUND)));
		case -2:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorMessageDto(CVSSEnum.COMPUTER_UUID_EMPTY.getStatusCode(),
							StatusMessageUtil.getMessage(CVSSEnum.COMPUTER_UUID_EMPTY)));
		case 0:
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(new ErrorMessageDto(CVSSEnum.COMPUTER_ALREADY_DELETED.getStatusCode(),
							StatusMessageUtil.getMessage(CVSSEnum.COMPUTER_ALREADY_DELETED)));
		default:
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorMessageDto(CVSSEnum.Internal_Server_Error.getStatusCode(),
							StatusMessageUtil.getMessage(CVSSEnum.Internal_Server_Error)));
		}
	}

	@PatchMapping("computer/activate")
	public ResponseEntity<?> activateMachine(@RequestParam String computerUuid) {
		int status = computerService.activateMachine(computerUuid);

		switch (status) {
		case 1:
			return ResponseEntity.ok(new StatusMessageDto(CVSSEnum.COMPUTER_ACTIVATED.getStatusCode(),
					StatusMessageUtil.getMessage(CVSSEnum.COMPUTER_ACTIVATED)));
		case -1:
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorMessageDto(CVSSEnum.COMPUTER_NOT_FOUND.getStatusCode(),
							StatusMessageUtil.getMessage(CVSSEnum.COMPUTER_NOT_FOUND)));
		case -2:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorMessageDto(CVSSEnum.COMPUTER_UUID_EMPTY.getStatusCode(),
							StatusMessageUtil.getMessage(CVSSEnum.COMPUTER_UUID_EMPTY)));
		case 0:
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(new ErrorMessageDto(CVSSEnum.COMPUTER_ALREADY_DELETED.getStatusCode(),
							StatusMessageUtil.getMessage(CVSSEnum.COMPUTER_ALREADY_DELETED)));
		default:
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorMessageDto(CVSSEnum.Internal_Server_Error.getStatusCode(),
							StatusMessageUtil.getMessage(CVSSEnum.Internal_Server_Error)));
		}
	}

	@GetMapping("/activeComputers")
	public ResponseEntity<?> getActiveComputers() {
		List<Computer> activeComputers = computerService.getActiveComputers();

		if (activeComputers.isEmpty()) {
			return ResponseEntity.noContent().build();
		}

		return ResponseEntity.ok(activeComputers);
	}

	@GetMapping("/inactiveComputers")
	public ResponseEntity<?> getInactiveComputers() {

		List<Computer> inactiveComputers = computerService.getInactiveComputers();
		if (inactiveComputers.isEmpty()) {
			return ResponseEntity.noContent().build();
		}

		return ResponseEntity.ok(inactiveComputers);
	}
}
