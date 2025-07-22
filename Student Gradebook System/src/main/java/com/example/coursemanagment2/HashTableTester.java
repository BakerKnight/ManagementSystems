package com.example.coursemanagment2;

import java.util.Random;

public class HashTableTester {
    private CourseManager courseManager;
    private QuadraticHashTable<Integer, Student> studentHashTable;
    private int collisionCount;

    public HashTableTester() {
        courseManager = new CourseManager();
        studentHashTable = new QuadraticHashTable<>(10); // Initial capacity of 10, adjust as needed
        collisionCount = 0;
    }

    public void generateDummyCourses(int numberOfCourses) {
        for (int i = 1; i <= numberOfCourses; i++) {
            String courseName = "Course " + i;
            int courseCode = 1000 + i; // Simple course codes
            courseManager.createCourse(courseCode, courseName);
        }
    }

    public void generateDummyStudents(int numberOfStudents) {
        Random random = new Random();
        for (int i = 1; i <= numberOfStudents; i++) {
            String studentName = "Student " + i;
            int studentId = random.nextInt(10000); // Random ID to potentially increase collision
            Student student = new Student(studentName, studentId);
            addStudentToHashTable(studentId, student);
        }
    }

    private void addStudentToHashTable(int studentId, Student student) {
        // Use put method to add students and observe behavior through debug or modified QuadraticHashTable
        if (studentHashTable.get(studentId) != null) {
            collisionCount++;
            System.out.println("Collision detected before adding Student ID: " + studentId);
        }
        studentHashTable.put(studentId, student);
    }

    public void printCollisionCount() {
        System.out.println("Total collisions detected: " + collisionCount);
    }

    public static void main(String[] args) {
        HashTableTester tester = new HashTableTester();
        tester.generateDummyCourses(30); // Adjust numbers as needed for testing
        tester.generateDummyStudents(50); // Adjust numbers as needed for testing
        tester.printCollisionCount();
    }
}
