package com.example.coursemanagment2;


import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.HashMap;

import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;


public class CourseManagement extends Application {

    private Stage primaryStage;
    private Scene optionsScene;
    private CourseManager courseManager;
    private static final QuadraticHashTable<Integer, Course> courseHashTable = new QuadraticHashTable<>(10);

    public static void main(String[] args) {
        launch(args);
    }



    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Course Management System");

        courseManager = new CourseManager();
        HashMap<String, String> userDatabase = new HashMap<>();

        optionsScene = createOptionsScene();
        Scene loginScene = new Login(primaryStage, optionsScene, userDatabase).createLoginScene();
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    private Scene createOptionsScene() {
        VBox optionsBox = new VBox();
        optionsBox.setAlignment(Pos.CENTER);
        optionsBox.setSpacing(20);

        Label welcomeLabel = new Label("Welcome to the Course Management System");
        welcomeLabel.getStyleClass().add("welcome-label");

        Button newCourseButton = new Button("Create New Course");
        newCourseButton.getStyleClass().add("welcome-button");

        Button viewCourseButton = new Button("View Existing Courses");
        viewCourseButton.getStyleClass().add("options-button");

        updateButtonStates(viewCourseButton);

        newCourseButton.setOnAction(e -> {
            showNewCoursePage();
            updateButtonStates(viewCourseButton);
        });

        viewCourseButton.setOnAction(e -> showViewCoursePage());

        optionsBox.getChildren().addAll(welcomeLabel, newCourseButton, viewCourseButton);

        return new Scene(optionsBox, 800, 600);
    }

    private void showViewCoursePage() {
        TableView<Course> courseTable = new TableView<>();

        // Define columns for the table
        TableColumn<Course, Integer> codeColumn = new TableColumn<>("Course Code");
        TableColumn<Course, String> nameColumn = new TableColumn<>("Course Name");

        // Set up the columns' cell value factories to use the getters in Course class
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("courseCode")); // Use the getter name for courseCode
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName")); // Use the getter name for courseName

        // Add the columns to the table
        courseTable.getColumns().add(codeColumn);
        courseTable.getColumns().add(nameColumn);

        // Populate the table with courses from the hash table
        courseTable.setItems(FXCollections.observableArrayList(courseHashTable.getAllValues()));

        // Set row click functionality to open the selected course in the course view
        courseTable.setRowFactory(tv -> {
            TableRow<Course> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Course selectedCourse = row.getItem();
                    openCourseView(selectedCourse);  // Open the course view for selected course
                }
            });
            return row;
        });

        // Back button to return to the options menu
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> primaryStage.setScene(optionsScene));

        // Create a layout and scene for viewing the courses
        VBox layout = new VBox(courseTable, backButton);
        layout.setSpacing(10);
        layout.setAlignment(Pos.CENTER);

        Scene viewCourseScene = new Scene(layout, 800, 600);
        primaryStage.setScene(viewCourseScene);
    }

    private void showEditCoursePage() {
        TableView<Course> courseTable = new TableView<>();
        TableColumn<Course, Integer> codeColumn = new TableColumn<>("Course Code");
        TableColumn<Course, String> nameColumn = new TableColumn<>("Course Name");
        TableColumn<Course, Double> averageColumn = new TableColumn<>("Class Average");

        codeColumn.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        averageColumn.setCellValueFactory(course -> {
            double average = course.getValue().calculateCourseAverage();
            return new SimpleObjectProperty<>(average);
        });

        courseTable.getColumns().addAll(codeColumn, nameColumn, averageColumn);
        courseTable.setItems(FXCollections.observableArrayList(courseHashTable.getAllValues()));

        courseTable.setRowFactory(tv -> {
            TableRow<Course> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Course course = row.getItem();
                    TextInputDialog codeDialog = new TextInputDialog(String.valueOf(course.getCourseCode()));
                    TextInputDialog nameDialog = new TextInputDialog(course.getCourseName());

                    codeDialog.setTitle("Edit Course");
                    codeDialog.setHeaderText("Edit Course Code");
                    codeDialog.setContentText("Enter new Course Code:");

                    nameDialog.setTitle("Edit Course");
                    nameDialog.setHeaderText("Edit Course Name");
                    nameDialog.setContentText("Enter new Course Name:");

                    codeDialog.showAndWait().ifPresent(newCode -> {
                        nameDialog.showAndWait().ifPresent(newName -> {
                            courseHashTable.remove(course.getCourseCode());
                            course.setCourseCode(Integer.parseInt(newCode));
                            course.setCourseName(newName);
                            courseHashTable.put(course.getCourseCode(), course);
                            courseTable.setItems(FXCollections.observableArrayList(courseHashTable.getAllValues()));
                        });
                    });
                }
            });
            return row;
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> primaryStage.setScene(optionsScene));

        VBox editBox = new VBox(courseTable, backButton);
        editBox.setPadding(new Insets(10));
        editBox.setSpacing(10);
        editBox.setAlignment(Pos.CENTER);

        Scene editScene = new Scene(editBox, 800, 600);
        primaryStage.setScene(editScene);
    }

    private void showNewCoursePage() {
        TextInputDialog codeDialog = new TextInputDialog();
        TextInputDialog nameDialog = new TextInputDialog();

        codeDialog.setTitle("Create New Course");
        codeDialog.setHeaderText("Create New Course");
        codeDialog.setContentText("Enter Course Code:");

        nameDialog.setTitle("Create New Course");
        nameDialog.setHeaderText("Create New Course");
        nameDialog.setContentText("Enter Course Name:");

        codeDialog.showAndWait().ifPresent(courseCode -> {
            nameDialog.showAndWait().ifPresent(courseName -> {
                try {
                    int courseCodeInt = Integer.parseInt(courseCode);
                    if (courseHashTable.get(courseCodeInt) != null) {
                        // If the course already exists, show an error
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Course Code Already Exists");
                        alert.setContentText("A course with this code already exists. Please try a different code.");
                        alert.showAndWait();
                    } else {
                        // Create a new course and add it to the hash table
                        Course newCourse = new Course(courseName, courseCodeInt);
                        courseHashTable.put(courseCodeInt, newCourse);

                        // Show success message
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Success");
                        successAlert.setHeaderText("Course Created");
                        successAlert.setContentText("Course '" + courseName + "' with code " + courseCode + " has been created.");
                        successAlert.showAndWait();
                    }
                } catch (NumberFormatException e) {
                    // Handle invalid course code input
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Invalid Course Code");
                    alert.setContentText("Please enter a valid numeric course code.");
                    alert.showAndWait();
                }
            });
        });
    }
    
    
    private void openCourseView(Course course) {
        if (course != null) {
            courseView.showStudentsInCourse(course);  // Call the static method directly
        } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("No Course Selected");
            errorAlert.setContentText("Please select a course to view.");
            errorAlert.showAndWait();
        }
    }

    //helper method for buttons
    private void updateButtonStates(Button viewCourseButton) {
        boolean coursesExist = !courseHashTable.getAllValues().isEmpty();
        viewCourseButton.setDisable(!coursesExist);
    }

    }




