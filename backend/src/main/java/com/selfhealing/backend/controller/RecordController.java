package com.selfhealing.backend.controller;

import com.selfhealing.backend.model.DataRecord;
import com.selfhealing.backend.model.DataRequest;
import com.selfhealing.backend.repository.DataRecordRepository;
import com.selfhealing.backend.service.HashLogicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/records")
@CrossOrigin(origins = "*")
public class RecordController {

    @Autowired
    private DataRecordRepository repository;

    @Autowired
    private HashLogicService hashService;

    @PostMapping("/insert")
    public ResponseEntity<?> insertData(@RequestBody DataRequest request) {
        if (request.getStudentName() == null || request.getRollNumber() == null || request.getGrade() == null) {
            throw new IllegalArgumentException("Student details cannot be null");
        }
        String hash = hashService.calculateHash(request.getStudentName(), request.getRollNumber(), request.getGrade());
        
        DataRecord record = new DataRecord();
        record.setStudentName(request.getStudentName());
        record.setRollNumber(request.getRollNumber());
        record.setGrade(request.getGrade());
        
        record.setOriginalStudentName(request.getStudentName());
        record.setOriginalRollNumber(request.getRollNumber());
        record.setOriginalGrade(request.getGrade());

        record.setHash(hash);
        record.setStatus("VERIFIED");
        
        return ResponseEntity.ok(repository.save(record));
    }

    @GetMapping("/data")
    public List<DataRecord> getData(@RequestParam(defaultValue = "false") boolean secureMode) {
        List<DataRecord> records = repository.findAll();
        boolean changed = false;
        
        for (DataRecord record : records) {
            String currentHash = hashService.calculateHash(record.getStudentName(), record.getRollNumber(), record.getGrade());
            
            // Tamper Detection
            if (!currentHash.equals(record.getHash())) {
                if (secureMode) {
                    // Secure Mode ON: auto-recover — restore original data, reject tampered state
                    record.setStudentName(record.getOriginalStudentName());
                    record.setRollNumber(record.getOriginalRollNumber());
                    record.setGrade(record.getOriginalGrade());
                    record.setStatus("RECOVERED");
                } else if (!"TAMPERED".equals(record.getStatus())) {
                    record.setStatus("TAMPERED");
                }
                repository.save(record);
                changed = true;
            }
        }
        return changed ? repository.findAll() : records;
    }
    
    @PostMapping("/recover/{id}")
    public DataRecord recoverData(@PathVariable Long id) {
        DataRecord record = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Record not found"));
        
        // Restore logic
        record.setStudentName(record.getOriginalStudentName());
        record.setRollNumber(record.getOriginalRollNumber());
        record.setGrade(record.getOriginalGrade());
        
        // The hash should match the original. (Actually the original hash is still in the DB).
        record.setStatus("RECOVERED");
        return repository.save(record);
    }
    
    @DeleteMapping("/{id}")
    public void deleteRecord(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
