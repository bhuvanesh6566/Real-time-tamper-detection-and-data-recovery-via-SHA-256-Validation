package com.selfhealing.backend.service;

import com.selfhealing.backend.model.DataRecord;
import com.selfhealing.backend.repository.DataRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
public class TamperEngine {

    @Autowired
    private DataRecordRepository repository;

    private final Random random = new Random();

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void autoModify() {
        List<DataRecord> records = repository.findAll();
        
        List<DataRecord> healthyRecords = records.stream()
            .filter(r -> r.getStatus().equals("VERIFIED"))
            .toList();

        if (!healthyRecords.isEmpty()) {
            DataRecord target = healthyRecords.get(random.nextInt(healthyRecords.size()));
            
            // Simulating a DB tamper attack - modify value directly, bypassing hash sync
            int modificationChoice = random.nextInt(3); // 0: modify grade, 1: modify rollNumber, 2: modify both
            
            if (modificationChoice == 0 || modificationChoice == 2) {
                String[] maliciousGrades = {"F- (Hacked)", "Z (Invalid)", "Drop-out"};
                String maliciousGrade = maliciousGrades[random.nextInt(maliciousGrades.length)];
                target.setGrade(maliciousGrade);
                System.out.println("🔥 TAMPER ENGINE: Corrupted Record ID " + target.getId() + " grade to '" + maliciousGrade + "' via background attack.");
            }
            
            if (modificationChoice == 1 || modificationChoice == 2) {
                Long maliciousRollNumber = 999900L + random.nextInt(100); // Generate a fake roll number
                target.setRollNumber(maliciousRollNumber);
                System.out.println("🔥 TAMPER ENGINE: Corrupted Record ID " + target.getId() + " roll number to '" + maliciousRollNumber + "' via background attack.");
            }
            
            // Don't update hash to simulate direct tampering
            repository.save(target);
        }
    }
}
