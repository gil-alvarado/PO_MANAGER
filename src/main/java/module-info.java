module com.test {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql;

    opens com.test to javafx.fxml;
    
    //ADDED TO ALLOW ACCESS TO Controller Package
    opens Controller to javafx.fxml;
    
    exports com.test;
}
