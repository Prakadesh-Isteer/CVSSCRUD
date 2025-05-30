package com.isteer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isteer.entity.Application;
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
import java.util.stream.Collectors;

@Service
public class ApplicationService {
	private static final Logger logger = LoggerFactory.getLogger(ApplicationService.class);

	@Autowired
	ApplicationRepository applicationRepository;
	@Autowired
	ComputerRepository computerRepository;
	@Autowired
	DependencyRepository dependencyRepository;
	@Autowired
	VulnerabilityRepository vulnerabilityRepository;

	public int createApplication(String computerId, Application application) {
		if (computerId == null || computerId.trim().isEmpty()) {
			return -3; // Computer UUID cannot be empty
		}
		if (computerRepository.findByUuid(computerId) == null) {
			return -1; // Computer not found
		}
		computerRepository.findByUuid(computerId); // Check computer exists
		application.setComputerUuid(computerId);
		application.setStatus(true);
		application.setCreatedAt(LocalDateTime.now());
		logger.info("Creating application: {}", application.getName());
		return applicationRepository.save(application);
	}

	public List<Application> getAllApplications(String computerId) {
		logger.info("Fetching applications for computerId: {}", computerId);
		if (computerId != null && !computerId.trim().isEmpty()) {
			computerRepository.findByUuid(computerId);
			// Check computer exists
		}
		List<Application> application = applicationRepository.findAll(computerId);
		List<Dependency> dependencies = dependencyRepository.findAll(null);
		List<Vulnerability> vulnerabilities = vulnerabilityRepository.findAll(null);

		Map<String, List<Dependency>> depMap = dependencies.stream()
				.collect(Collectors.groupingBy(Dependency::getApplicationUuid));
		Map<String, List<Vulnerability>> vulnMap = vulnerabilities.stream()
				.collect(Collectors.groupingBy(Vulnerability::getDependencyUuid));

		application.forEach(app -> {
			List<Dependency> appDeps = depMap.getOrDefault(app.getUuid(), Collections.emptyList());
			appDeps.forEach(
					dep -> dep.setVulnerabilities(vulnMap.getOrDefault(dep.getUuid(), Collections.emptyList())));
			app.setDependencies(appDeps);
		});

		return application;
	}

	public int updateApplication(String uuid, Application application) {
		if (uuid == null || uuid.trim().isEmpty()) {
			return -2; // UUID cannot be empty
		}
		if (applicationRepository.findByUuid(uuid) == null) {
			return -1; // Application not found
		}
		application.setStatus(true);
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