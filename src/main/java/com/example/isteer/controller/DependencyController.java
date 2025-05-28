package com.example.isteer.controller;

import com.example.isteer.dto.ErrorMessageDto;
import com.example.isteer.dto.StatusMessageDto;
import com.example.isteer.entity.Dependency;
import com.example.isteer.enums.CVSSEnum;
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
	public ResponseEntity<?> createDependency(@RequestParam String applicationUuid, @RequestBody Dependency dependency) {
		int status = dependencyService.createDependency(applicationUuid, dependency);
		if (status > 0) {
			StatusMessageDto message = new StatusMessageDto(CVSSEnum.DEPENDENCY_ADD.getStatusCode(),
					CVSSEnum.DEPENDENCY_ADD.getStatusMessage());
			return ResponseEntity.status(HttpStatus.OK).body(message);

		} else if (status == -1) {
			ErrorMessageDto message = new ErrorMessageDto(CVSSEnum.APPLICATION_NOT_FOUND.getStatusCode(),
					CVSSEnum.APPLICATION_NOT_FOUND.getStatusMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
		} else if (status == -2) {
			ErrorMessageDto message = new ErrorMessageDto(CVSSEnum.APPLICATION_UUID_EMPTY.getStatusCode(),
					CVSSEnum.APPLICATION_UUID_EMPTY.getStatusMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
		}
		else {
			ErrorMessageDto message = new ErrorMessageDto(CVSSEnum.Internal_Server_Error.getStatusCode(),
					CVSSEnum.Internal_Server_Error.getStatusMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
		}

	}

	@GetMapping
	public ResponseEntity<List<Dependency>> getAllDependencies(@RequestParam(required = false) String applicationUuid) {
		List<Dependency> dependencies = dependencyService.getAllDependencies(applicationUuid);

		if (dependencies.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(dependencies);
	}

	@PutMapping
	public ResponseEntity<?> updateDependency(@RequestParam String id, @RequestBody Dependency dependency) {
		int status = dependencyService.updateDependency(id, dependency);
		if (status > 0) {
			StatusMessageDto message = new StatusMessageDto(CVSSEnum.DEPENDENCY_UPDATE.getStatusCode(),
					CVSSEnum.DEPENDENCY_UPDATE.getStatusMessage());
			return ResponseEntity.ok(message);
		} else if (status == -1) {
			ErrorMessageDto message = new ErrorMessageDto(CVSSEnum.DEPENDENCY_NOT_FOUND.getStatusCode(),
					CVSSEnum.DEPENDENCY_NOT_FOUND.getStatusMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
		}
		
		else if (status == -2) {
			ErrorMessageDto message = new ErrorMessageDto(CVSSEnum.DEPENDENCY_UUID_EMPTY.getStatusCode(),
					CVSSEnum.DEPENDENCY_UUID_EMPTY.getStatusMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
		} 
		else {
			ErrorMessageDto message = new ErrorMessageDto(CVSSEnum.Internal_Server_Error.getStatusCode(),
					CVSSEnum.Internal_Server_Error.getStatusMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
		}
	}

	@DeleteMapping
	public ResponseEntity<?> deleteDependency(@RequestParam String uuid) {
		int status = dependencyService.softDeleteDependency(uuid);	
		if (status > 0) {
			StatusMessageDto message = new StatusMessageDto(CVSSEnum.DEPENDENCY_DELETE.getStatusCode(),
					CVSSEnum.DEPENDENCY_DELETE.getStatusMessage());
			return ResponseEntity.status(HttpStatus.OK).body(message);
		}
		else if (status == -2) {
			ErrorMessageDto message = new ErrorMessageDto(CVSSEnum.DEPENDENCY_UUID_EMPTY.getStatusCode(),
					CVSSEnum.DEPENDENCY_UUID_EMPTY.getStatusMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
		}
		else if (status == -1) {
			ErrorMessageDto message = new ErrorMessageDto(CVSSEnum.DEPENDENCY_NOT_FOUND.getStatusCode(),
					CVSSEnum.DEPENDENCY_NOT_FOUND.getStatusMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
		}
	 else {
			ErrorMessageDto message = new ErrorMessageDto(CVSSEnum.Internal_Server_Error.getStatusCode(),
					CVSSEnum.Internal_Server_Error.getStatusMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
		}
	}
}
