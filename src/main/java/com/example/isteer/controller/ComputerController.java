package com.example.isteer.controller;

import com.example.isteer.dto.ErrorMessageDto;
import com.example.isteer.dto.StatusMessageDto;
import com.example.isteer.entity.Computers;
import com.example.isteer.service.ComputerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/computers")
public class ComputerController {

	@Autowired
	ComputerService computerService;

	@PostMapping
	public ResponseEntity<?> createMachine(@RequestBody Computers computers) {
		int status = computerService.createMachine(computers);
		if (status > 0) {
			StatusMessageDto message = new StatusMessageDto(7985, "Computer Details Added successfully.");
			return ResponseEntity.status(HttpStatus.CREATED).body(message);
		} else if (status == 0) {
			ErrorMessageDto message = new ErrorMessageDto(7985, "Computer with Same IP Address already exists.");
			return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
		} else {
			ErrorMessageDto message = new ErrorMessageDto(7985, "Something went wrong");
			return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
		}
	}

	@GetMapping
	public ResponseEntity<List<Computers>> getAllMachines() {
		List<Computers> machines = computerService.getAllMachines();
		if (machines.isEmpty()) {

			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(machines);
	}

	@PutMapping
	public ResponseEntity<?> updateMachine(@RequestParam String computerId, @RequestBody Computers computers) {
		int status = computerService.updateMachine(computerId, computers);
		if (status > 0) {
			StatusMessageDto message = new StatusMessageDto(7985, "Computer Details updated successfully.");
			return ResponseEntity.status(HttpStatus.OK).body(message);

		} else if (status == -1) {
			ErrorMessageDto message = new ErrorMessageDto(7985, "Computer with ID " + computerId + " not found.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
		} else if (status == -2) {

			ErrorMessageDto message = new ErrorMessageDto(7985, "Invalid IP address");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
		}

		else if (status == -3) {

			ErrorMessageDto message = new ErrorMessageDto(7985, "Hostname cannot be empty");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
		} else if (status == 0) {

			ErrorMessageDto message = new ErrorMessageDto(7985, "Computer with Same IP Address already exists.");
			return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
		} else {
			ErrorMessageDto message = new ErrorMessageDto(7985, "Something went wrong");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
		}

	}

	@DeleteMapping
	public ResponseEntity<?> deleteMachine(@RequestParam String computerId) {
		int status = computerService.deleteMachine(computerId);
		if (status > 0) {
			StatusMessageDto message = new StatusMessageDto(7985,
					"Machine with ID " + computerId + " deleted successfully.");
			return ResponseEntity.status(HttpStatus.OK).body(message);
		} else {
			ErrorMessageDto message = new ErrorMessageDto(7985, "Machine with ID " + computerId + " not found.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
		}
	}

	@PatchMapping("/deactivate")
	public ResponseEntity<?> deactivateComputer(@RequestParam String id) {
		int status = computerService.deactivateComputer(id);
		if (status > 0) {
			StatusMessageDto message = new StatusMessageDto(7985, "Computer  deactivated successfully.");
			return ResponseEntity.status(HttpStatus.OK).body(message);

		} else if (status == -1) {
			ErrorMessageDto message = new ErrorMessageDto(7985, "Computer with ID " + id + " not found.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
		} else if (status == 0) {

			ErrorMessageDto message = new ErrorMessageDto(7985, "Computer with ID " + id + " already inactive.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
		} else if (status == -2) {

			ErrorMessageDto message = new ErrorMessageDto(7985, "ID cannot be empty");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
		} else {
			ErrorMessageDto message = new ErrorMessageDto(7985, "Something went wrong");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
		}

	}
}
// package com.example.isteer.controller;