package com.isteer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.isteer.dto.ErrorMessageDto;
import com.isteer.dto.StatusMessageDto;
import com.isteer.entity.Application;
import com.isteer.enums.CVSSEnum;
import com.isteer.service.ApplicationService;
import com.isteer.util.StatusMessageUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class ApplicationController {

	@Autowired
	ApplicationService applicationService;

	@Autowired
	StatusMessageUtil statusMessageUtil;

	@PostMapping("/application")
	public ResponseEntity<?> createApplication(@RequestParam String computerUuid, @Valid @RequestBody Application application) {
		int status = applicationService.createApplication(computerUuid, application);
		switch (status) {
		case 1:
			return ResponseEntity.status(HttpStatus.CREATED).body(new StatusMessageDto(
					CVSSEnum.APPLICATION_ADD.getStatusCode(), StatusMessageUtil.getMessage(CVSSEnum.APPLICATION_ADD)));
		case -1:
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorMessageDto(CVSSEnum.COMPUTER_NOT_FOUND.getStatusCode(),
							StatusMessageUtil.getMessage(CVSSEnum.COMPUTER_NOT_FOUND)));
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

	@GetMapping("/applications")
	public ResponseEntity<List<Application>> getAllApplications(@RequestParam(required = false) String computerUuid) {
		List<Application> application = applicationService.getAllApplications(computerUuid);
		if (application.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(application);
	}

	@PutMapping("/application")
	public ResponseEntity<?> updateApplication(@RequestParam String applicationUuid, @Valid @RequestBody Application application) {
		int status = applicationService.updateApplication(applicationUuid, application);
		switch (status) {
		case 1:
			return ResponseEntity.ok(new StatusMessageDto(CVSSEnum.APPLICATION_UPDATE.getStatusCode(),
					StatusMessageUtil.getMessage(CVSSEnum.APPLICATION_UPDATE)));
		case -1:
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorMessageDto(CVSSEnum.APPLICATION_NOT_FOUND.getStatusCode(),
							StatusMessageUtil.getMessage(CVSSEnum.APPLICATION_NOT_FOUND)));
		case -2:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorMessageDto(CVSSEnum.APPLICATION_UUID_EMPTY.getStatusCode(),
							StatusMessageUtil.getMessage(CVSSEnum.APPLICATION_UUID_EMPTY)));

		default:
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorMessageDto(CVSSEnum.Internal_Server_Error.getStatusCode(),
							StatusMessageUtil.getMessage(CVSSEnum.Internal_Server_Error)));
		}
	}

	@DeleteMapping("/application")
	public ResponseEntity<?> deleteApplication(@RequestParam String applicationUuid) {
		int status = applicationService.softDeleteApplication(applicationUuid);
		switch (status) {
		case 1:
			return ResponseEntity.ok(new StatusMessageDto(CVSSEnum.APPLICATION_DELETE.getStatusCode(),
					StatusMessageUtil.getMessage(CVSSEnum.APPLICATION_DELETE)));
		case -1:
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorMessageDto(CVSSEnum.APPLICATION_NOT_FOUND.getStatusCode(),
							StatusMessageUtil.getMessage(CVSSEnum.APPLICATION_NOT_FOUND)));
		case -2:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorMessageDto(CVSSEnum.APPLICATION_UUID_EMPTY.getStatusCode(),
							StatusMessageUtil.getMessage(CVSSEnum.APPLICATION_UUID_EMPTY)));
		default:
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorMessageDto(CVSSEnum.Internal_Server_Error.getStatusCode(),
							StatusMessageUtil.getMessage(CVSSEnum.Internal_Server_Error)));
		}
	}
}
