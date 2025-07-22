module com.example.coursemanagment2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.coursemanagment2 to javafx.fxml;
    exports com.example.coursemanagment2;
}