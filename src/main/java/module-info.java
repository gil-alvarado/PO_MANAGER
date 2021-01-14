module com.test {
    
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql;
    requires ucanaccess;
    requires commons.io;
    requires java.desktop;
    requires poi.ooxml;
    requires poi.ooxml.schemas;
    requires poi.scratchpad;
//    requires org.apache.commons.codec;
//    requires org.apache.commons.collections4;
//    requires org.apache.commons.compress;
//    
//    requires org.apache.commons.lang3;
    requires poi;
    requires java.prefs;


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
