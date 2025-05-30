package com.isteer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isteer.entity.Application;
import com.isteer.entity.Computer;
import com.isteer.entity.Dependency;
import com.isteer.entity.Vulnerability;
import com.isteer.repository.ApplicationRepository;
import com.isteer.repository.ComputerRepository;
import com.isteer.repository.DependencyRepository;
import com.isteer.repository.VulnerabilityRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ComputerService {
	private static final Logger logger = LoggerFactory.getLogger(ComputerService.class);
	private static final Pattern IP_PATTERN = Pattern
			.compile("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");

	@Autowired
	 ComputerRepository computerRepository;
	@Autowired
	 ApplicationRepository applicationRepository;
	@Autowired
	DependencyRepository dependencyRepository;
	@Autowired
	 VulnerabilityRepository vulnerabilityRepository;



	public int createMachine(Computer computer) {
		if (computer.getIpAddress() == null || !IP_PATTERN.matcher(computer.getIpAddress()).matches()) {
			logger.error("Invalid IP address: {}", computer.getIpAddress());
			return -4; // Invalid IP address
		}
	    computer.setStatus(true); // Set status to active
		computer.setActive(true);
		computer.setCreatedAt(LocalDateTime.now());
		logger.info("Creating machine with IP: {}", computer.getIpAddress());
		return computerRepository.save(computer);
	}

	public List<Computer> getAllComputers() {
		logger.info("Fetching all machines");
		List<Computer> computer = computerRepository.findAll();
		// Fetch hierarchical data
		List<Application> application = applicationRepository.findAll(null);
		List<Dependency> dependencies = dependencyRepository.findAll(null);
		List<Vulnerability> vulnerabilities = vulnerabilityRepository.findAll(null);

		// Map applications to computers
		Map<String, List<Application>> appMap = application.stream()
				.collect(Collectors.groupingBy(Application::getComputerUuid));
		// Map dependencies to applications
		Map<String, List<Dependency>> depMap = dependencies.stream()
				.collect(Collectors.groupingBy(Dependency::getApplicationUuid));
		// Map vulnerabilities to dependencies
		Map<String, List<Vulnerability>> vulnMap = vulnerabilities.stream()
				.collect(Collectors.groupingBy(Vulnerability::getDependencyUuid));

		// Build hierarchy
		computer.forEach(wrkComputers -> {
			List<Application> compApps = appMap.getOrDefault(wrkComputers.getUuid(), Collections.emptyList());
			compApps.forEach(app -> {
				List<Dependency> appDeps = depMap.getOrDefault(app.getUuid(), Collections.emptyList());
				appDeps.forEach(
						dep -> dep.setVulnerabilities(vulnMap.getOrDefault(dep.getUuid(), Collections.emptyList())));
				app.setDependencies(appDeps);
			});
			wrkComputers.setApplications(compApps);
		});

		return computer;
	}
	
	public Computer findComputerByUuid(String uuid) {
		logger.info("Fetching machine by UUID: {}", uuid);
		if (uuid == null || uuid.trim().isEmpty()) {
			return null; // UUID cannot be empty
		}
		logger.info("Fetching machine with UUID: {}", uuid);
		return computerRepository.computerByUuid(uuid);
	}

	public int updateMachine(String uuid, Computer computer) {
		if (uuid == null || uuid.trim().isEmpty()) {
			return -3; // UUID cannot be empty
		}
		if (computer.getIpAddress() == null || !IP_PATTERN.matcher(computer.getIpAddress()).matches()) {
			logger.error("Invalid IP address: {}", computer.getIpAddress());
			return -2; // Invalid IP address
		}
		if (computerRepository.findByUuid(uuid) == null) {
			return -1; // Computer not found
		}
		computer.setStatus(true); // Set status to active
		computer.setUpdatedAt(LocalDateTime.now());
		logger.info("Updating machine with UUID: {}", uuid);
		computerRepository.findByUuid(uuid); // Check existence
		computerRepository.update(uuid, computer);
		return 1; // Assuming update is successful
	}

	public int softDeleteMachine(String uuid) {
		if (uuid == null || uuid.trim().isEmpty()) {
			return -2; // UUID cannot be empty
		}
		if (computerRepository.findByUuid(uuid) == null) {
			return -1; // Computer not found
		}
		logger.info("Soft deleting machine with UUID: {}", uuid);
		computerRepository.findByUuid(uuid); // Check existence
		return computerRepository.softDelete(uuid);
	}

	public int deactivateMachine(String uuid) {
		if (uuid == null || uuid.trim().isEmpty()) {
			return -2; // UUID cannot be empty
		}
		if (computerRepository.findByUuid(uuid) == null) {
			return -1; // Computer not found
		}
		logger.info("Deactivating machine with UUID: {}", uuid);
		computerRepository.findByUuid(uuid); // Check existence
		return computerRepository.deactivate(uuid);
	}
	
	
	public int activateMachine(String uuid) {
	    if (uuid == null || uuid.trim().isEmpty()) {
	        return -2;
	    }

	    int updatedRows = computerRepository.activateComputer(uuid);

	    if (updatedRows > 0) {
	        return 1;
	    }

	    // Check if computer exists
	    if (computerRepository.findByUuid(uuid) == null) {
	        return -1;
	    }

	    return 0; // Already active
	}


	public List<Computer> getActiveComputers() {
		logger.info("Fetching all active machines");
		return computerRepository.findAll().stream()
				.filter(Computer::isActive)
				.collect(Collectors.toList());
	}

	public List<Computer> getInactiveComputers() {
		logger.info("Fetching all inactive machines");
		return computerRepository.findAll().stream()
				.filter(computer -> !computer.isActive())
				.collect(Collectors.toList());
	}

}