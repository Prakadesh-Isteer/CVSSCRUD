package com.isteer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.isteer.dto.ErrorMessageDto;
import com.isteer.dto.StatusMessageDto;
import com.isteer.entity.Dependency;
import com.isteer.enums.CVSSEnum;
import com.isteer.service.DependencyService;
import com.isteer.util.StatusMessageUtil;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DependencyController {

	@Autowired
	DependencyService dependencyService;
	
	   @Autowired
	    StatusMessageUtil statusMessageUtil;

	@PostMapping("/dependency")
	public ResponseEntity<?> createDependency(@RequestParam String applicationUuid, @RequestBody Dependency dependency) {
		int status = dependencyService.createDependency(applicationUuid, dependency);
		if (status > 0) {
			StatusMessageDto message = new StatusMessageDto(CVSSEnum.DEPENDENCY_ADD.getStatusCode(),
					StatusMessageUtil.getMessage(CVSSEnum.DEPENDENCY_ADD));
			return ResponseEntity.status(HttpStatus.OK).body(message);

		} else if (status == -1) {
			ErrorMessageDto message = new ErrorMessageDto(CVSSEnum.APPLICATION_NOT_FOUND.getStatusCode(),
					StatusMessageUtil.getMessage(CVSSEnum.APPLICATION_NOT_FOUND));
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
		} else if (status == -2) {
			ErrorMessageDto message = new ErrorMessageDto(CVSSEnum.APPLICATION_UUID_EMPTY.getStatusCode(),
					StatusMessageUtil.getMessage(CVSSEnum.APPLICATION_UUID_EMPTY));
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
		}
		else {
			ErrorMessageDto message = new ErrorMessageDto(CVSSEnum.Internal_Server_Error.getStatusCode(),
					StatusMessageUtil.getMessage(CVSSEnum.Internal_Server_Error));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
		}

	}

	@GetMapping("/dependencies")
	public ResponseEntity<List<Dependency>> getAllDependencies(@RequestParam(required = false) String applicationUuid) {
		List<Dependency> dependencies = dependencyService.getAllDependencies(applicationUuid);

		if (dependencies.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(dependencies);
	}

	@PutMapping("/dependency")
	public ResponseEntity<?> updateDependency(@RequestParam String uuid, @RequestBody Dependency dependency) {
		int status = dependencyService.updateDependency(uuid, dependency);
		if (status > 0) {
			StatusMessageDto message = new StatusMessageDto(CVSSEnum.DEPENDENCY_UPDATE.getStatusCode(),
					StatusMessageUtil.getMessage(CVSSEnum.DEPENDENCY_UPDATE));
			return ResponseEntity.ok(message);
		} else if (status == -1) {
			ErrorMessageDto message = new ErrorMessageDto(CVSSEnum.DEPENDENCY_NOT_FOUND.getStatusCode(),
					StatusMessageUtil.getMessage(CVSSEnum.DEPENDENCY_NOT_FOUND));
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
		}
		
		else if (status == -2) {
			ErrorMessageDto message = new ErrorMessageDto(CVSSEnum.DEPENDENCY_UUID_EMPTY.getStatusCode(),
					StatusMessageUtil.getMessage(CVSSEnum.DEPENDENCY_UUID_EMPTY));
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
		} 
		else {
			ErrorMessageDto message = new ErrorMessageDto(CVSSEnum.Internal_Server_Error.getStatusCode(),
					StatusMessageUtil.getMessage(CVSSEnum.Internal_Server_Error));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
		}
	}

	@DeleteMapping("/dependency")
	public ResponseEntity<?> deleteDependency(@RequestParam String uuid) {
		int status = dependencyService.softDeleteDependency(uuid);	
		if (status > 0) {
			StatusMessageDto message = new StatusMessageDto(CVSSEnum.DEPENDENCY_DELETE.getStatusCode(),
					 StatusMessageUtil.getMessage(CVSSEnum.DEPENDENCY_DELETE));
			return ResponseEntity.status(HttpStatus.OK).body(message);
		}
		else if (status == -2) {
			ErrorMessageDto message = new ErrorMessageDto(CVSSEnum.DEPENDENCY_UUID_EMPTY.getStatusCode(),
					StatusMessageUtil.getMessage(CVSSEnum.DEPENDENCY_UUID_EMPTY));
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
		}
		else if (status == -1) {
			ErrorMessageDto message = new ErrorMessageDto(CVSSEnum.DEPENDENCY_NOT_FOUND.getStatusCode(),
					StatusMessageUtil.getMessage(CVSSEnum.DEPENDENCY_NOT_FOUND));
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
		}
	 else {
			ErrorMessageDto message = new ErrorMessageDto(CVSSEnum.Internal_Server_Error.getStatusCode(),
					StatusMessageUtil.getMessage(CVSSEnum.Internal_Server_Error));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
		}
	}
}
