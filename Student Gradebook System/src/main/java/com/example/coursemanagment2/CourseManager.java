package com.example.coursemanagment2;
import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class CourseManager {
    
        private QuadraticHashTable<Integer, Course> courseHashTable;

        public CourseManager() {
            this.courseHashTable = new QuadraticHashTable<>(10);
        }

        // Method to add a course
        public void createCourse(int courseCode, String courseName) {
            if (courseHashTable.get(courseCode) == null) {
                Course newCourse = new Course(courseName, courseCode);
                courseHashTable.put(courseCode, newCourse);
            } else {
                // Handle error - course code already exists
                System.out.println("Course with this code already exists!");
            }
        }

        // Method to get a course by course code
        public Course getCourse(int courseCode) {
            return courseHashTable.get(courseCode);
        }

        // Method to get all courses (for listing)
        public List<Course> getAllCourses() {
            return courseHashTable.getAllValues();
        }


}


