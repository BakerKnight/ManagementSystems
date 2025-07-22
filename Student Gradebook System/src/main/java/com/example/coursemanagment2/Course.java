package com.example.coursemanagment2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Course extends CourseManager{
    private String courseName;
    private int courseCode;
    private HashMap<String, Integer> studentGrades = new HashMap<>();

    
    public Course(String courseName, int courseCode) {
        this.courseName = courseName;
        this.courseCode = courseCode;
    }

    public ArrayList<Integer> getEnrolledStudents() {
        return new ArrayList<>(studentGrades.values());
    }

    public String getCourseName() {
        return courseName;
    }

    public int getCourseCode() {
        return courseCode;
    }
    
    

    public double calculateCourseAverage() {
        double sum = 0.0;
        for (int grade : studentGrades.values()) {
            sum += grade * grade;
        }
        return sum / studentGrades.size();
    }
    public void setCourseCode(int courseCode) {
        this.courseCode = courseCode;
    }
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void addStudent(String studentName, Integer id) {
        studentGrades.put(studentName, id );
    }
    public void removeStudent(String studentName) {
        if (studentGrades.containsKey(studentName)) {
            studentGrades.remove(studentName);
        } else {
            System.out.println("Invalid student name. Cannot remove student.");
        }
    }
    public void addStudentGrade(String studentName, int grade) {
        studentGrades.put(studentName, grade);
    }
    public void removeStudentGrade(String studentName) {
        if (studentGrades.containsKey(studentName)) {
            studentGrades.remove(studentName);
        } else {
            System.out.println("Invalid student name. Cannot remove grade.");
        }
    }

    @Override
    public String toString() {
        return courseName + " (" + courseCode + ")";
    }

    public Integer[] getEnrolledStudentIds() {
        return studentGrades.values().toArray(new Integer[0]);

    }
}
