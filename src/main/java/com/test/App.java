package com.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App          
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

//Parent root = FXMLLoader.load(getClass().getResource("/View/Main/MainLayoutTesting_WITHANCHOR.fxml"));  
//Parent root_ = FXMLLoader.load(getClass().getResource("/View/Login/LoginView.fxml"));

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
        
        scene = new Scene(loadFXML("/View/Login/LoginView"));
//scene = new Scene(loadFXML("/View/Main/MainLayout_SINGLE"));
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
        stage.setMinHeight(800);
        stage.setMinWidth(1250);
        //########################################
        //stage.initStyle(StageStyle.UNDECORATED);
        //########################################
        stage.setTitle("BMS PO MANAGER");
        
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }
//    public static MainLayoutController getController(){
//        return fxmlLoader.getController();
//    }
//    private static FXMLLoader fxmlLoader;
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}