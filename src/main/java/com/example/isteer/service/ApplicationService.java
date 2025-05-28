package com.example.isteer.service;

import com.example.isteer.entity.Applications;
import com.example.isteer.entity.Dependency;
import com.example.isteer.entity.Vulnerability;
import com.example.isteer.repository.ApplicationRepository;
import com.example.isteer.repository.ComputerRepository;
import com.example.isteer.repository.DependencyRepository;
import com.example.isteer.repository.VulnerabilityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ApplicationService {
	private static final Logger logger = LoggerFactory.getLogger(ApplicationService.class);

	private final ApplicationRepository applicationRepository;
	private final ComputerRepository computerRepository;
	private final DependencyRepository dependencyRepository;
	private final VulnerabilityRepository vulnerabilityRepository;

	public ApplicationService(ApplicationRepository applicationRepository, ComputerRepository computerRepository,
			DependencyRepository dependencyRepository, VulnerabilityRepository vulnerabilityRepository) {
		this.applicationRepository = applicationRepository;
		this.computerRepository = computerRepository;
		this.dependencyRepository = dependencyRepository;
		this.vulnerabilityRepository = vulnerabilityRepository;
	}

	public int createApplication(String computerId, Applications application) {
		if (computerId == null || computerId.trim().isEmpty()) {
			return -3; // Computer UUID cannot be empty
		}
		if(computerRepository.findByUuid(computerId) == null) {
			return -1; // Computer not found
		}
		computerRepository.findByUuid(computerId); // Check computer exists
		application.setComputerUuid(computerId);
		application.setStatus((byte) 1);
		application.setCreatedAt(LocalDateTime.now());
		logger.info("Creating application: {}", application.getName());
		return applicationRepository.save(application);
	}

	public List<Applications> getAllApplications(String computerId) {
		logger.info("Fetching applications for computerId: {}", computerId);		
		if (computerId != null && !computerId.trim().isEmpty()) {
			computerRepository.findByUuid(computerId);
			// Check computer exists
		}
		List<Applications> applications = applicationRepository.findAll(computerId);
		List<Dependency> dependencies = dependencyRepository.findAll(null);
		List<Vulnerability> vulnerabilities = vulnerabilityRepository.findAll(null);

		Map<String, List<Dependency>> depMap = dependencies.stream()
				.collect(Collectors.groupingBy(Dependency::getApplicationUuid));
		Map<String, List<Vulnerability>> vulnMap = vulnerabilities.stream()
				.collect(Collectors.groupingBy(Vulnerability::getDependencyUuid));

		applications.forEach(app -> {
			List<Dependency> appDeps = depMap.getOrDefault(app.getUuid(), Collections.emptyList());
			appDeps.forEach(
					dep -> dep.setVulnerabilities(vulnMap.getOrDefault(dep.getUuid(), Collections.emptyList())));
			app.setDependencies(appDeps);
		});

		return applications;
	}

	public int updateApplication(String uuid, Applications application) {
		if (uuid == null || uuid.trim().isEmpty()) {
			return -2; // UUID cannot be empty
		}
		if (applicationRepository.findByUuid(uuid) == null) {
			return -1; // Application not found
		}
		application.setStatus((byte) 1);
		logger.info("Updating application with UUID: {}", uuid);
		applicationRepository.findByUuid(uuid); // Check existence
		applicationRepository.update(uuid, application);
		return 1; // Assuming update is successful
	}

	public int softDeleteApplication(String uuid) {
		if (uuid == null || uuid.trim().isEmpty()) {
			return -2; // UUID cannot be empty
		}
		if (applicationRepository.findByUuid(uuid) == null) {
			return -1; // Application not found
		}
		logger.info("Soft deleting application with UUID: {}", uuid);
		applicationRepository.findByUuid(uuid); // Check existence
		return applicationRepository.softDelete(uuid);
	}

}