package com.test;

import Model.ConnectionUtil;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.hibernate.Transaction;
//import org.hibernate.cfg.Configuration;
//import org.hibernate.Session; 

//import net.ucanaccess.complex.Attachment;


/**
 * JavaFX App
 * 
 * 
 * 
 * IMPORTANT: 
 *     mainLayout_WITHANCHOR: anchorpane after/in_stackpane
 *     overviewForm_NOANCHOR: NO anchorpane at root
 * 
 *            mainLayout_WITHANCHOR -> overviewFrom_NOANCHOR
 * 
 *            ----------------------------------------------
 * 
 *     mainLayout_NOANCHOR: NO anchorpane in stackpane
 *     overviewForm_WITHANCHOR: anchorpane AT ROOT
 * 
 *            mainLayout_NOANCHOR -> overviewForm_WITHANCHOR
 *            
 *          
 */
public class App extends Application {

    private static Scene scene;
    private double x,y;
    @Override
    public void start(Stage stage) throws IOException {

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%        
        
//        scene = new Scene(loadFXML("test"));
//        scene = new Scene(FXMLLoader.load(getClass().getResource("test.fxml")));
        
//        Parent root = FXMLLoader.load(getClass().getResource("/View/test.fxml"));

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
//load overview_WITHANCHOR to stackpane
//Parent root = FXMLLoader.load(getClass().getResource("/View/MainLayoutTesting_NOANCHOR.fxml"));

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Parent root = FXMLLoader.load(getClass().getResource("/View/Main/MainLayoutTesting_WITHANCHOR.fxml"));  
Parent root_ = FXMLLoader.load(getClass().getResource("/View/Login/LoginView.fxml"));

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

//root.setOnMousePressed(event -> {
//            x = event.getSceneX();
//            y = event.getSceneY();
//        });
//        root.setOnMouseDragged(event -> {
//
//            stage.setX(event.getScreenX() - x);
//            stage.setY(event.getScreenY() - y);
//
//        });
//        
//        scene = new Scene(root);
        //-------------------------
        scene = new Scene(loadFXML("/View/Login/LoginView"));
        scene.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });
        scene.setOnMouseDragged(event -> {

            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);

        });
        stage.setScene(scene);
        
        stage.setResizable(true);
        stage.setMinHeight(700);
        stage.setMinWidth(1350);
//        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("BMS DATA MANAGER");
        
        
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}