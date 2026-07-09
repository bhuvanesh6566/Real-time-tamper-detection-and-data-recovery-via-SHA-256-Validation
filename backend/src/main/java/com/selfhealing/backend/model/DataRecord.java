package com.selfhealing.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "records")
public class DataRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studentName;
    private Long rollNumber;
    private String grade;

    @Column(length = 64)
    private String hash;

    @Column(name = "original_student_name")
    private String originalStudentName;
    
    @Column(name = "original_roll_number")
    private Long originalRollNumber;

    @Column(name = "original_grade")
    private String originalGrade;

    private String status; // VERIFIED, TAMPERED, RECOVERED

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public Long getRollNumber() { return rollNumber; }
    public void setRollNumber(Long rollNumber) { this.rollNumber = rollNumber; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public String getHash() { return hash; }
    public void setHash(String hash) { this.hash = hash; }

    public String getOriginalStudentName() { return originalStudentName; }
    public void setOriginalStudentName(String originalStudentName) { this.originalStudentName = originalStudentName; }

    public Long getOriginalRollNumber() { return originalRollNumber; }
    public void setOriginalRollNumber(Long originalRollNumber) { this.originalRollNumber = originalRollNumber; }

    public String getOriginalGrade() { return originalGrade; }
    public void setOriginalGrade(String originalGrade) { this.originalGrade = originalGrade; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
