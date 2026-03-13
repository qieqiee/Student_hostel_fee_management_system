module com.example.hostel_management_system {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    opens com.example.hostel_management_system to javafx.fxml;
    exports com.example.hostel_management_system;
}
