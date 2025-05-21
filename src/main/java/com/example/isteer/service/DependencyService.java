package com.example.isteer.service;

import com.example.isteer.entity.Dependency;
import com.example.isteer.repository.ApplicationRepository;
import com.example.isteer.repository.DependencyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DependencyService {
	private static final Logger logger = LoggerFactory.getLogger(DependencyService.class);

	private final DependencyRepository dependencyRepository;
	private final ApplicationRepository applicationRepository;

	public DependencyService(DependencyRepository dependencyRepository, ApplicationRepository applicationRepository) {
		this.dependencyRepository = dependencyRepository;
		this.applicationRepository = applicationRepository;
	}

	public int createDependency(String applicationId, Dependency dependency) {

		 dependency.setApplicationId(applicationId);
		if (applicationRepository.findById(applicationId) == null) {
			return -3; // Application ID not found
		}
		if (dependency.getApplicationId() == null || dependency.getApplicationId().trim().isEmpty()) {
			logger.error("Application ID is required");
			return -2; // Application ID is required
		}

		logger.error("Application ID not found: {}", applicationId);

		if (dependency.getName() == null || dependency.getName().trim().isEmpty()) {
			logger.error("Dependency name is required");
			return -1; // Dependency name is required
		}
		dependency.setApplicationId(applicationId);
		dependency.setCreatedAt(LocalDateTime.now());
		logger.info("Creating dependency: {}", dependency.getName());
		return dependencyRepository.save(dependency);
	}

	public List<Dependency> getAllDependencies() {
		logger.info("Fetching all dependencies");
		return dependencyRepository.findAll();
	}

	public int updateDependency(String id, Dependency dependency) {
		validateDependency(dependency);
		if (dependencyRepository.findById(id) == null) {
			logger.error("Dependency not found with ID: {}", id);
			return -1; // Dependency not found
			// Check existence
		}
		dependency.setId(id);
		dependency.setUpdatedAt(LocalDateTime.now());
		logger.info("Updating dependency with ID: {}", id);
		return dependencyRepository.update(id, dependency);

	}

	public int deleteDependency(String id) {
		logger.info("Deleting dependency with ID: {}", id);
		if (dependencyRepository.findById(id) == null) {

			logger.error("Dependency not found with ID: {}", id);
			return -1; // Dependency not found
		}
		dependencyRepository.delete(id);
		return 1; // Success
	}

	private int validateDependency(Dependency dependency) {
		if (dependency.getName() == null || dependency.getName().trim().isEmpty()) {
			return -1;
		}
		if (dependency.getApplicationId() == null) {
			return -2;
		}
		return 0;
	}
}