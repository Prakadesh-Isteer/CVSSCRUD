package com.example.isteer.service;

import com.example.isteer.entity.Computers;
import com.example.isteer.repository.ComputerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class ComputerService {
	
    private static final Logger logger = LoggerFactory.getLogger(ComputerService.class);
    private static final Pattern IP_PATTERN = 
            Pattern.compile("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");

    @Autowired
    private ComputerRepository computerRepository;

    public int createMachine(Computers machine) {
    	try {
        validateMachine(machine);
        machine.setCreatedAt(LocalDateTime.now());
        logger.info("Creating machine with IP: {}", machine.getIpAddress());
         computerRepository.save(machine);
         return 1; // Assuming save returns the number of records saved
    	} catch (DuplicateKeyException e) {
    		
    	return  0;
    	}
    }

    public List<Computers> getAllMachines() {
        logger.info("Fetching all machines");
        return computerRepository.findAll();
    }

    public int updateMachine(String id, Computers machine) {
    	try {
           logger.info("Updating machine with ID: {}", id);
      if(computerRepository.findById(id) == null) {
			return -1; // Machine not found
		}
		if (machine.getIpAddress() != null && !IP_PATTERN.matcher(machine.getIpAddress()).matches()) {
		return -2; // Invalid IP address
		}
		if (machine.getHostname() != null && machine.getHostname().trim().isEmpty()) {
			return -3; // Host name cannot be empty
		}
		machine.setId(id);
		machine.setUpdatedAt(LocalDateTime.now());
        computerRepository.update(id, machine);
        return 1; // Assuming update returns the number of records updated
    	} catch (DuplicateKeyException e) {
			return 0; // Duplicate key error
		}
    }

    public int deleteMachine(String id) {
        logger.info("Deleting machine with ID: {}", id);
        computerRepository.findById(id); // Check existence
       return computerRepository.delete(id);
    }
    
    public int deactivateComputer(String id) {
    	Computers machine = new Computers();
        logger.info("Deactivating computer with ID: {}", id);
        if (id.isBlank()) {
			return -2; // ID cannot be empty
		}
        if (computerRepository.findById(id) == null) {
        	return -1; // Machine not found
        }
        if (computerRepository.findById(id).isActive() == false) {
			return 0; // Machine already inactive
		}
        machine.setId(id);
        computerRepository.findById(id); // Check existence
       return computerRepository.deactivate(id);
    }

    private void validateMachine(Computers machine) {
        if (machine.getIpAddress() == null || !IP_PATTERN.matcher(machine.getIpAddress()).matches()) {
            throw new IllegalArgumentException("Invalid IP address");
        }
        if (machine.getHostname() != null && machine.getHostname().trim().isEmpty()) {
            throw new IllegalArgumentException("Hostname cannot be empty");
        }
    }
}