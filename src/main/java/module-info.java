module com.example.nuclearthrone {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.nuclearthrone to javafx.fxml;
    exports com.example.nuclearthrone;
    exports com.example.nuclearthrone.control;
    opens com.example.nuclearthrone.control to javafx.fxml;
}