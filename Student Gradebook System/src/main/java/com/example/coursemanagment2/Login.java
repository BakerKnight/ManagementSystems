package com.example.coursemanagment2;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalTime;
import java.util.HashMap;

//This class serves as a welcome/login page. Users can create their login to begin managing an inventory.
// Returning users can keep managing an inventory they have already created.
// The login page is the first page that the user sees when they open the application.
// The login page has a welcome message, and two buttons: one for new users and one for returning users.

public class Login {
    private Stage primaryStage;
    private Scene inventoryScene;
    private HashMap<String, String> userDatabase = new HashMap<>();

    public Login(Stage primaryStage, Scene inventoryScene, HashMap<String, String> userDatabase) {
        this.primaryStage = primaryStage;
        this.inventoryScene = inventoryScene;
        this.userDatabase = userDatabase;
    }

    public Scene createLoginScene() {
        VBox loginBox = new VBox();
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setSpacing(20);

        Label welcomeLabel = new Label(getGreeting());
        welcomeLabel.getStyleClass().add("welcome-label");

        Button newUserButton = new Button("Register");
        newUserButton.getStyleClass().add("welcome-button");
        newUserButton.setOnAction(e -> showRegistrationForm(loginBox));

        Button returningUserButton = new Button("Login");
        if (userDatabase.isEmpty()) {
            returningUserButton.setDisable(true);
        }
        returningUserButton.getStyleClass().add("welcome-button");
        returningUserButton.setOnAction(e -> showLoginForm(loginBox));

        loginBox.getChildren().addAll(welcomeLabel, newUserButton, returningUserButton);
        Scene scene = new Scene(loginBox, 800, 600);
//        scene.getStylesheets().add("styles.css");
        return scene;
    }

    private void showLoginForm(VBox loginBox) {
        loginBox.getChildren().clear();

//        ImageView logo = new ImageView(new Image("logo.png"));
//        logo.setFitHeight(100);
//        logo.setFitWidth(100);
//
//        HBox logoBox = new HBox(logo);
//        logoBox.setPadding(new Insets(0, 0, 0, 10));
//        logoBox.setAlignment(Pos.CENTER);

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        usernameField.setPrefWidth(200);
        usernameField.setPromptText("Please Enter your Username");

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPrefWidth(200);
        passwordField.setPromptText("Please Enter your Password");

        gridPane.add(usernameLabel, 0, 0);
        gridPane.add(usernameField, 1, 0);
        gridPane.add(passwordLabel, 0, 1);
        gridPane.add(passwordField, 1, 1);

        Button loginButton = new Button("Login");
        loginButton.getStyleClass().add("login-button");

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (userDatabase.containsKey(username) && userDatabase.get(username).equals(password)) {
                primaryStage.setScene(inventoryScene);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Credentials");
                alert.setContentText("The username or password you entered is incorrect. Please try again.");
                alert.showAndWait();
            }
        });

//        loginBox.getChildren().addAll(logoBox, gridPane, loginButton);
        loginBox.getChildren().addAll( gridPane, loginButton);
    }

    private void showRegistrationForm(VBox loginBox) {
        loginBox.getChildren().clear();

//        ImageView logo = new ImageView(new Image("logo.png"));
//        logo.setFitHeight(100);
//        logo.setFitWidth(100);
//
//        HBox logoBox = new HBox(logo);
//        logoBox.setPadding(new Insets(0, 0, 0, 10));
//        logoBox.setAlignment(Pos.CENTER);

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        usernameField.setPrefWidth(200);
        usernameField.setPromptText("Choose a Username");

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPrefWidth(200);
        passwordField.setPromptText("Choose a Password");

        Label confirmPasswordLabel = new Label("Confirm Password:");
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPrefWidth(200);
        confirmPasswordField.setPromptText("Confirm your Password");

        gridPane.add(usernameLabel, 0, 0);
        gridPane.add(usernameField, 1, 0);
        gridPane.add(passwordLabel, 0, 1);
        gridPane.add(passwordField, 1, 1);
        gridPane.add(confirmPasswordLabel, 0, 2);
        gridPane.add(confirmPasswordField, 1, 2);

        Button registerButton = new Button("Register");
        registerButton.getStyleClass().add("login-button");

        registerButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();
            if (password.equals(confirmPassword)) {
                if (!userDatabase.containsKey(username)) {
                    userDatabase.put(username, password);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Registration Successful");
                    alert.setHeaderText(null);
                    alert.setContentText("You have successfully registered. Please log in.");
                    alert.showAndWait();
                    primaryStage.setScene(createLoginScene());
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Username Taken");
                    alert.setContentText("The username you entered is already taken. Please try again.");
                    alert.showAndWait();
                }

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Password Mismatch");
                alert.setContentText("The passwords do not match. Please try again.");
                alert.showAndWait();
            }
        });

//        loginBox.getChildren().addAll(logoBox, gridPane, registerButton);
        loginBox.getChildren().addAll(gridPane, registerButton);
    }
    //helper method to determine what greeting to display based on the time of day
    private String getGreeting() {
        int hour = LocalTime.now().getHour();
        if (hour >= 5 && hour < 12) {
            return "Good Morning";
        } else if (hour >= 12 && hour < 18) {
            return "Good Afternoon";
        } else {
            return "Good Evening";
        }
    }
}

