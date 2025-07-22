package com.example.coursemanagment2;

import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javafx.geometry.Insets;
import java.util.Optional;

public class courseView extends Application {
    private static QuadraticHashTable<Integer, Student> studentHashTable = new QuadraticHashTable<>(10);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        initializeSampleData();
    }

    public static void showStudentsInCourse(Course course) {
        Stage studentStage = new Stage();
        studentStage.setTitle(course.getCourseName() + " " + course.getCourseCode());

        TableView<Student> studentTable = new TableView<>();
        ObservableList<Student> studentData = FXCollections.observableArrayList();

        // Assuming enrolled students are stored by ID in the course
        for (Integer studentId : course.getEnrolledStudentIds()) { // Assuming method returns some collection
            Student student = studentHashTable.get(studentId);
            if (student != null) {
                studentData.add(student);
            }
        }
        studentTable.setItems(studentData);

        // Setup table columns
        TableColumn<Student, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Student, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));

        TableColumn<Student, Double> averageGradeColumn = new TableColumn<>("Average Grade");
        averageGradeColumn.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(cellData.getValue().calculateAverageTestScore()).asObject());

        TableColumn<Student, String> allGradesColumn = new TableColumn<>("All Grades");
        allGradesColumn.setCellValueFactory(cellData -> {
            Student student = cellData.getValue();
            StringBuilder sb = new StringBuilder();
            student.getTestScores().forEach((testNumber, grade) -> {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(grade);
            });
            return new SimpleStringProperty(sb.toString());
        });

        allGradesColumn.setPrefWidth(300);
        studentTable.getColumns().addAll(nameColumn, idColumn, averageGradeColumn, allGradesColumn);

        // Set up buttons for managing students (Add, Delete, Add Grades, etc.)
        VBox buttonsLayout = new VBox(10);
        buttonsLayout.getChildren().addAll(
                addStudentButton(course, studentTable),
                deleteStudentButton(course, studentTable),
                addTestGradeButton(course, studentTable),
                deleteTestGradeButton(course, studentTable)
        );

        HBox textLayout = new HBox(10);
        TextField consoleTextField = new TextField();
        consoleTextField.setEditable(false);
        consoleTextField.setPrefWidth(250);
        consoleTextField.setStyle("-fx-font-size: 14px;");
        textLayout.getChildren().add(consoleTextField);

        VBox root = new VBox(10);
        root.getChildren().addAll(studentTable, buttonsLayout, textLayout);

        Scene scene = new Scene(root, 800, 600);
        studentStage.setScene(scene);
        studentStage.show();
    }


    private static Button addStudentButton(Course course, TableView<Student> studentTable) {
        Button addButton = new Button("Add Student");

        addButton.setOnAction(event -> {
            TextField studentName = new TextField();
            TextField studentId = new TextField();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Add Student");
            dialog.setHeaderText(null);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new javafx.geometry.Insets(20, 20, 20, 20));

            grid.add(new Label("Name:"), 0, 0);
            grid.add(studentName, 1, 0);
            grid.add(new Label("ID: (4-Digits)"), 0, 1);
            grid.add(studentId, 1, 1);

            dialog.getDialogPane().setContent(grid);

            ButtonType addButtonType = ButtonType.OK;
            dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == addButtonType) {
                try {
                    String studentNameText = studentName.getText();
                    int studentIdInt = Integer.parseInt(studentId.getText());
                    Student newStudent = new Student(studentNameText, studentIdInt);

                    // Add to hash table
                    studentHashTable.put(studentIdInt, newStudent);

                    // Update TableView
                    ObservableList<Student> studentData = studentTable.getItems();
                    studentData.add(newStudent);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid student ID.");
                }
            }
        });

        return addButton;
    }


    private static Button deleteStudentButton(Course course, TableView<Student> studentTable) {
        Button deleteButton = new Button("Delete Student");

        deleteButton.setOnAction(event -> {
            Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
            if (selectedStudent != null) {
                // Remove from hash table
                studentHashTable.remove(selectedStudent.getStudentId());

                // Remove from TableView
                ObservableList<Student> studentData = studentTable.getItems();
                studentData.remove(selectedStudent);
            } else {
                System.out.println("No student selected to delete.");
            }
        });

        return deleteButton;
    }

    private static Button addTestGradeButton(Course course, TableView<Student> studentTable) {
        Button addButton = new Button("Add Test Grade");

        addButton.setOnAction(event -> {
            Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();

            if (selectedStudent != null) {
                TextField testNumberField = new TextField();
                TextField gradeField = new TextField();

                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setTitle("Add Test Grade");
                dialog.setHeaderText("Enter the test number and grade");

                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20));

                grid.add(new Label("Test Number:"), 0, 0);
                grid.add(testNumberField, 1, 0);
                grid.add(new Label("Grade:"), 0, 1);
                grid.add(gradeField, 1, 1);

                dialog.getDialogPane().setContent(grid);

                ButtonType addButtonType = ButtonType.OK;
                dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);


                Optional<ButtonType> result = dialog.showAndWait();

                if (result.isPresent() && result.get() == addButtonType) {

                    String testNumberText = testNumberField.getText();
                    String gradeText = gradeField.getText();

                    try {
                        int testNumber = Integer.parseInt(testNumberText); // Test number
                        double grade = Double.parseDouble(gradeText); // Grade


                        selectedStudent.addScore(testNumber, grade);


                        studentTable.refresh(); // Refresh the TableView to update the view
                    } catch (NumberFormatException e) {

                        System.out.println("Invalid input. Please enter a valid test number and grade.");
                    }
                }
            } else {
                System.out.println("No student selected to add the grade.");
            }
        });

        return addButton;
    }

    private static Button deleteTestGradeButton(Course course, TableView<Student> studentTable) {
        Button deleteButton = new Button("Delete Test Grade");

        deleteButton.setOnAction(event -> {
            Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
            if (selectedStudent != null) {
                TextField testNumberField = new TextField();

                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setTitle("Delete Test Grade");
                dialog.setHeaderText("Enter the test number of the grade to delete");

                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20));

                grid.add(new Label("Test Number:"), 0, 0);
                grid.add(testNumberField, 1, 0);

                dialog.getDialogPane().setContent(grid);

                ButtonType deleteButtonType = ButtonType.OK;
                dialog.getDialogPane().getButtonTypes().addAll(deleteButtonType, ButtonType.CANCEL);

                Optional<ButtonType> result = dialog.showAndWait();

                if (result.isPresent() && result.get() == deleteButtonType) {
                    try {
                        int testNumber = Integer.parseInt(testNumberField.getText());
                        if (selectedStudent.getTestScores().containsKey(testNumber)) {
                            selectedStudent.removeTestGrade(testNumber);
                            studentTable.refresh();
                        } else {
                            System.out.println("Test number not found.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a valid test number.");
                    }
                }
            } else {
                System.out.println("No student selected to delete the grade.");
            }
        });

        return deleteButton;

    }

    private static Button courseAverageButton(Course course, TableView<Student> studentTable, TextField consoleTextField) {
        Button courseAverageButton = new Button("Course Average");

        courseAverageButton.setOnAction(event -> {
            double totalScore = 0;
            int studentCount = course.getEnrolledStudents().size();

            // Iterate over student IDs instead of names
            for (Integer studentId : course.getEnrolledStudents()) {
                Student student = studentHashTable.get(studentId);  // Retrieve student by ID

                if (student != null) {
                    totalScore += student.calculateAverageTestScore();
                }
            }

            // Calculate the course average
            double courseAverage = studentCount > 0 ? totalScore / studentCount : 0;
            String courseAverageText = String.format("Course average: %.2f", courseAverage);
            System.out.println(courseAverageText);

            // Display the course average in the console text field
            consoleTextField.setText(courseAverageText);
        });

        return courseAverageButton;
    }
    private static void initializeSampleData() {
        // Create two courses
        Course course1 = new Course("Introduction to Programming", 101);
        Course course2 = new Course("Data Structures", 102);

        // Create and add students to the courses
        Student student1 = new Student("Alice", 1001);
        Student student2 = new Student("Bob", 1002);
        Student student3 = new Student("Charlie", 1003);
        Student student4 = new Student("Diana", 1004);

        // Add students to hash table
        studentHashTable.put(student1.getStudentId(), student1);
        studentHashTable.put(student2.getStudentId(), student2);
        studentHashTable.put(student3.getStudentId(), student3);
        studentHashTable.put(student4.getStudentId(), student4);

        // Enroll students in courses
        course1.addStudent(student1.getName(), student1.getStudentId());
        course1.addStudent(student2.getName(), student2.getStudentId());
        course2.addStudent(student3.getName(), student3.getStudentId());
        course2.addStudent(student4.getName(), student4.getStudentId());

        // Add grades for students in course 1
        addGradeSafely(studentHashTable, 1001, 1, 85.0);
        addGradeSafely(studentHashTable, 1001, 2, 90.0);
        addGradeSafely(studentHashTable, 1002, 1, 88.0);
        addGradeSafely(studentHashTable, 1002, 2, 80.0);

        // Add grades for students in course 2
        addGradeSafely(studentHashTable, 1003, 1, 95.0);
        addGradeSafely(studentHashTable, 1003, 2, 85.0);
        addGradeSafely(studentHashTable, 1004, 1, 78.0);
        addGradeSafely(studentHashTable, 1004, 2, 83.0);

        // Display courses
        showStudentsInCourse(course1);
        showStudentsInCourse(course2);
    }

    private static void addGradeSafely(QuadraticHashTable<Integer, Student> hashTable, int studentId, int testNum, double grade) {
        Student student = hashTable.get(studentId);
        if (student != null) {
            student.addScore(testNum, grade);
        } else {
            System.out.println("Error: Student with ID " + studentId + " not found.");
        }
    }
}








