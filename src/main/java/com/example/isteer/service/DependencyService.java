package com.example.isteer.service;

import com.example.isteer.entity.Dependency;
import com.example.isteer.entity.Vulnerability;
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
import java.util.stream.Collectors;

@Service
public class DependencyService {
    private static final Logger logger = LoggerFactory.getLogger(DependencyService.class);

    private final DependencyRepository dependencyRepository;
    private final ApplicationRepository applicationRepository;
    private final VulnerabilityRepository vulnerabilityRepository;

    public DependencyService(DependencyRepository dependencyRepository, ApplicationRepository applicationRepository,
                             VulnerabilityRepository vulnerabilityRepository) {
        this.dependencyRepository = dependencyRepository;
        this.applicationRepository = applicationRepository;
        this.vulnerabilityRepository = vulnerabilityRepository;
    }

    public int createDependency(String applicationId, Dependency dependency) {
        
    	if (applicationId == null || applicationId.trim().isEmpty()) {
			return -2; // Application ID cannot be empty
		}
    	if(applicationRepository.findByUuid(applicationId) == null) {
    		
    					return -1; // Application does not exist
    	}
        applicationRepository.findByUuid(applicationId); // Check application exists
        dependency.setApplicationUuid(applicationId);
        dependency.setStatus((byte) 1);
        dependency.setCreatedAt(LocalDateTime.now());
        logger.info("Creating dependency: {}", dependency.getName());
        return dependencyRepository.save(dependency);
    }

    public List<Dependency> getAllDependencies(String applicationUuid) {
        logger.info("Fetching dependencies for applicationId: {}", applicationUuid);
        if (applicationUuid != null) {
            applicationRepository.findByUuid(applicationUuid); // Check application exists
        }
        List<Dependency> dependencies = dependencyRepository.findAll(applicationUuid);
        List<Vulnerability> vulnerabilities = vulnerabilityRepository.findAll(null);

        Map<String, List<Vulnerability>> vulnMap = vulnerabilities.stream()
                .collect(Collectors.groupingBy(Vulnerability::getDependencyUuid));

        dependencies.forEach(dep -> dep.setVulnerabilities(vulnMap.getOrDefault(dep.getUuid(), Collections.emptyList())));

        return dependencies;
    }

    public int updateDependency(String uuid, Dependency dependency) {
        if (uuid == null || uuid.trim().isEmpty()) {
			return -2; // ID cannot be empty
		}
        if(dependencyRepository.findByUuid(uuid) == null) {
        	return -1; // Dependency not found
        }
        dependency.setStatus((byte) 1);
        dependencyRepository.findByUuid(uuid); // Check existence
        logger.info("Updating dependency with UUID: {}", uuid);
       return dependencyRepository.update(uuid, dependency);
       
    }

    public int softDeleteDependency(String uuid) {
    	if (uuid == null || uuid.trim().isEmpty()) {
			return -2; // ID cannot be empty
		}
         if (dependencyRepository.findByUuid(uuid) == null) {
			return -1; // Dependency not found
		}
        logger.info("Soft deleting dependency with UUID: {}", uuid);
        dependencyRepository.findByUuid(uuid); // Check existence
        return dependencyRepository.softDelete(uuid);
    }

    
}