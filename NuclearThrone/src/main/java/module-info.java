module com.example.nuclearthrone {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.nuclearthrone to javafx.fxml;
    exports com.example.nuclearthrone;
    exports control;
    opens control to javafx.fxml;
}