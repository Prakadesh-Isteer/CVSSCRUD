package com.example.isteer.service;

import com.example.isteer.entity.Computers;
import com.example.isteer.entity.Applications;
import com.example.isteer.entity.Dependency;
import com.example.isteer.entity.Vulnerability;
import com.example.isteer.repository.ComputerRepository;
import com.example.isteer.repository.ApplicationRepository;
import com.example.isteer.repository.DependencyRepository;
import com.example.isteer.repository.VulnerabilityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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

	private final ComputerRepository computerRepository;
	private final ApplicationRepository applicationRepository;
	private final DependencyRepository dependencyRepository;
	private final VulnerabilityRepository vulnerabilityRepository;

	public ComputerService(ComputerRepository computerRepository, ApplicationRepository applicationRepository,
			DependencyRepository dependencyRepository, VulnerabilityRepository vulnerabilityRepository) {
		this.computerRepository = computerRepository;
		this.applicationRepository = applicationRepository;
		this.dependencyRepository = dependencyRepository;
		this.vulnerabilityRepository = vulnerabilityRepository;
	}

	public int createMachine(Computers computer) {
		if (computer.getIpAddress() == null || !IP_PATTERN.matcher(computer.getIpAddress()).matches()) {
			logger.error("Invalid IP address: {}", computer.getIpAddress());
			return -4; // Invalid IP address
		}
		computer.setStatus((byte) 1);
		computer.setActive(true);
		computer.setCreatedAt(LocalDateTime.now());
		computer.setUpdatedAt(LocalDateTime.now());
		logger.info("Creating machine with IP: {}", computer.getIpAddress());
		return computerRepository.save(computer);
	}

	public List<Computers> getAllMachines() {
		logger.info("Fetching all machines");
		List<Computers> computers = computerRepository.findAll();
		// Fetch hierarchical data
		List<Applications> applications = applicationRepository.findAll(null);
		List<Dependency> dependencies = dependencyRepository.findAll(null);
		List<Vulnerability> vulnerabilities = vulnerabilityRepository.findAll(null);

		// Map applications to computers
		Map<String, List<Applications>> appMap = applications.stream()
				.collect(Collectors.groupingBy(Applications::getComputerUuid));
		// Map dependencies to applications
		Map<String, List<Dependency>> depMap = dependencies.stream()
				.collect(Collectors.groupingBy(Dependency::getApplicationUuid));
		// Map vulnerabilities to dependencies
		Map<String, List<Vulnerability>> vulnMap = vulnerabilities.stream()
				.collect(Collectors.groupingBy(Vulnerability::getDependencyUuid));

		// Build hierarchy
		computers.forEach(computer -> {
			List<Applications> compApps = appMap.getOrDefault(computer.getUuid(), Collections.emptyList());
			compApps.forEach(app -> {
				List<Dependency> appDeps = depMap.getOrDefault(app.getUuid(), Collections.emptyList());
				appDeps.forEach(
						dep -> dep.setVulnerabilities(vulnMap.getOrDefault(dep.getUuid(), Collections.emptyList())));
				app.setDependencies(appDeps);
			});
			computer.setApplications(compApps);
		});

		return computers;
	}

	public int updateMachine(String uuid, Computers computer) {
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
		computer.setStatus((byte) 1);
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

}