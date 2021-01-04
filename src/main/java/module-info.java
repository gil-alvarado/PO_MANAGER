module com.test {
    
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql;
    requires ucanaccess;
    requires commons.io;
    requires java.desktop;
    
    
//    requires jfxrt;

//    requires rt;

    opens com.test to javafx.fxml;
//    opens Controller to java.base;
    //ADDED TO ALLOW ACCESS TO Controller Package
    opens Controller to javafx.fxml;
    opens Model to javafx.fxml;
            
    exports com.test;
    exports Controller;
    exports Model;
}
