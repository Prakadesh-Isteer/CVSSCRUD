package com.example.isteer.controller;

import com.example.isteer.dto.ErrorMessageDto;
import com.example.isteer.dto.StatusMessageDto;
import com.example.isteer.entity.Dependency;
import com.example.isteer.service.DependencyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dependencies")
public class DependencyController {

	@Autowired
	DependencyService dependencyService;

	@PostMapping
	public ResponseEntity<?> createDependency(@RequestParam String applicationId, @RequestBody Dependency dependency) {
		int status = dependencyService.createDependency(applicationId, dependency);
		if (status > 0) {
			StatusMessageDto message = new StatusMessageDto(7985, "Dependency Added successfully.");
			return ResponseEntity.status(HttpStatus.OK).body(message);

		} else if (status == -1) {
			ErrorMessageDto message = new ErrorMessageDto(7985, "Dependency Name is required.");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
		} else if (status == -2) {
			ErrorMessageDto message = new ErrorMessageDto(7985, "Proper Application ID is required.");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
		}
		else {
			ErrorMessageDto message = new ErrorMessageDto(7985, "Something went wrong");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
		}

	}

	@GetMapping
	public ResponseEntity<List<Dependency>> getAllDependencies() {
		List<Dependency> dependencies = dependencyService.getAllDependencies();

		if (dependencies.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(dependencies);
	}

	@PutMapping
	public ResponseEntity<?> updateDependency(@RequestParam String id, @RequestBody Dependency dependency) {
		int status = dependencyService.updateDependency(id, dependency);
		if (status > 0) {
			StatusMessageDto message = new StatusMessageDto(7985, "Dependency Details updated successfully.");
			return ResponseEntity.ok(message);
		} else if (status == -1) {
			ErrorMessageDto message = new ErrorMessageDto(7985, "Dependency not found");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
		} else {
			ErrorMessageDto message = new ErrorMessageDto(7985, "Something went wrong");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
		}
	}

	@DeleteMapping
	public ResponseEntity<?> deleteDependency(@RequestParam String id) {
		int status = dependencyService.deleteDependency(id);	
		if (status > 0) {
			StatusMessageDto message = new StatusMessageDto(7985,
					"Dpendency  deleted successfully.");
			return ResponseEntity.status(HttpStatus.OK).body(message);
		} else if (status == -1) {
			ErrorMessageDto message = new ErrorMessageDto(7985, "No dependency ID found");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
		}
	 else {
			ErrorMessageDto message = new ErrorMessageDto(7985, "Something went wrong");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
		}
	}
}
