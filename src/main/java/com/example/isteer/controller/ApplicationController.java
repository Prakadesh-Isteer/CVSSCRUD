package com.example.isteer.controller;

import com.example.isteer.dto.ErrorMessageDto;
import com.example.isteer.dto.StatusMessageDto;
import com.example.isteer.entity.Applications;
import com.example.isteer.service.ApplicationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

	@Autowired
	ApplicationService applicationService;

	@PostMapping
	public ResponseEntity<?> createApplication(@RequestParam String computerId, @RequestBody Applications application) {
		int status = applicationService.createApplication(computerId, application);
		if (status > 0) {
			StatusMessageDto message = new StatusMessageDto(7985, "Application Details Added successfully.");
			return ResponseEntity.status(HttpStatus.CREATED).body(message);
		} else if (status == -1) {
			ErrorMessageDto message = new ErrorMessageDto(7985, "Computer not found");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
		} else {
			ErrorMessageDto message = new ErrorMessageDto(7985, "Something went wrong");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
		}

	}

	@GetMapping
	public ResponseEntity<List<Applications>> getAllApplications() {

		List<Applications> applications = applicationService.getAllApplications();
		if (applications.isEmpty()) {

			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(applications);

	}

	@PutMapping
	public ResponseEntity<?> updateApplication(@RequestParam String id, @RequestBody Applications application) {
		int status = applicationService.updateApplication(id, application);
		if (status > 0) {
			StatusMessageDto message = new StatusMessageDto(7985, "Application Details updated successfully.");
			return ResponseEntity.ok(message);
		} else if (status == -1) {
			ErrorMessageDto message = new ErrorMessageDto(7985, "Application not found");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
		} else {
			ErrorMessageDto message = new ErrorMessageDto(7985, "Something went wrong");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
		}
	}

	@DeleteMapping
	public ResponseEntity<?> deleteApplication(@RequestParam String id) {

		int status = applicationService.deleteApplication(id);
		if (status > 0) {
			StatusMessageDto message = new StatusMessageDto(7985, "Application deleted successfully");
			return ResponseEntity.ok(message);
		}

		else if (status == -2) {
			ErrorMessageDto message = new ErrorMessageDto(7985, "ID cannot be empty");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
		} else if (status == -1) {
			ErrorMessageDto message = new ErrorMessageDto(7985, "Application not found");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
		} else {
			ErrorMessageDto message = new ErrorMessageDto(7985, "Something went wrong");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
		}

	}
}
