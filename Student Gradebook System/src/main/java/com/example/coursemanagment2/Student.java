package com.example.coursemanagment2;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class Student {
    private String name;
    private int id;
    private Map<Integer, Double> testScores;
    private Map<Integer, List<Course>> courseView;

    public Student(String name, int id) {
        this.name = name;
        this.id = id;
        this.testScores = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStudentId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public void addScore(int testNum, double score) {
        testScores.put(testNum, score);
    }

    // Calculate average test score
    public double calculateAverageTestScore() {
        if (testScores.isEmpty()) {
            return 0.0;
        }
        double sum = 0.0;
        for (double score : testScores.values()) {
            sum += score;
        }
        double average = sum / testScores.size();
        return Math.round(average * 100.0) / 100.0; // Round to 2 decimal places
    }

    public Map<Integer, Double> getTestScores() {
        return testScores;
    }

    public String getTestScoresAsString() {
        return testScores.toString();
    }



    // Remove a test score
    public void removeTestGrade(int testNum) {
        if (testScores.containsKey(testNum)) {
            testScores.remove(testNum);
        } else {
            System.out.println("Test number not found.");
        }
    }

    @Override
    public String toString() {
        return "Name: " + name + ", ID: " + id + ", Grades: " + getTestScoresAsString();
    }





}
