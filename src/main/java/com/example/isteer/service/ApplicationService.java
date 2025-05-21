package com.example.isteer.service;

import com.example.isteer.entity.Applications;
import com.example.isteer.repository.ApplicationRepository;
import com.example.isteer.repository.ComputerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApplicationService {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationService.class);

    @Autowired
  ApplicationRepository applicationRepository;
    @Autowired
    ComputerRepository computerRepository;

    public int createApplication(String computerId, Applications application) {
    	try {
        validateApplication(application);
     if(computerRepository.findById(computerId) == null) {
    	 return -1; // Machine not found
    	 
     } // Check machine exists
        application.setComputerId(computerId);
        application.setCreatedAt(LocalDateTime.now());
        logger.info("Creating application: {}", application.getName());
        applicationRepository.save(application);
        return 1; // Assuming save returns the number of records saved
        		} catch (Exception e) {
        			
        			return 0;
        		}
    }

    public List<Applications> getAllApplications() {
        logger.info("Fetching all applications");
        return applicationRepository.findAll();
    }

    public int updateApplication(String id, Applications application) {
    	try {
        validateApplication(application);
     if(   applicationRepository.findById(id) == null) {
    	 return -1; // Application not found
	 } // Check existence
		if (application.getName() != null && application.getName().trim().isEmpty()) {
			return -2; // Application name cannot be empty
		}
		if (application.getVersion() != null && application.getVersion().trim().isEmpty()) {
			return -3; // Version cannot be empty
		}
		application.setId(id);
		application.setUpdatedAt(LocalDateTime.now());
        logger.info("Updating application with ID: {}", id);
        applicationRepository.update(id, application);
        return 1; // Assuming update returns the number of records updated
    	} catch (Exception e) {
    					return 0; // Duplicate key error
        }
    }
    public int deleteApplication(String id) {	
    	 if (id.isBlank()) {
            return -2; // ID cannot be empty
         }
        logger.info("Deleting application with ID: {}", id);
     // Check existence
     
        if (applicationRepository.findById(id) == null) {
			return -1; // Application not found
		}
		applicationRepository.delete(id);
       return 1;
        // Assuming delete returns the number of records deleted
    }

    private void validateApplication(Applications application) {
        if (application.getName() == null || application.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Application name cannot be empty");
        }
      
    }
}