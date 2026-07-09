package com.selfhealing.backend.model;
import lombok.Data;

public class DataRequest {
    private String studentName;
    private Long rollNumber;
    private String grade;

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    
    public Long getRollNumber() { return rollNumber; }
    public void setRollNumber(Long rollNumber) { this.rollNumber = rollNumber; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
}
