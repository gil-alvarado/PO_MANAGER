/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.MainLayoutTestingController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;

import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
/**
 * FXML Controller class
 *
 * @author Gilbert Alvarado
 */
public class MainLayoutTesting_NOANCHORController implements Initializable {


    @FXML
    private StackPane displaysStackPane;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try{
            
        Node overviewForm = FXMLLoader.load(getClass().getResource("/View/OverviewViewTesting_WITHANCHOR.fxml"));
        
        //below two lines work. use OverviewViewTesting fxml file that has anchor pane.
        displaysStackPane.getChildren().add(overviewForm);
        displaysStackPane.getChildren().get(0).toFront();
        

        } catch (IOException ex) {
            Logger.getLogger(MainLayoutTestingController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    @FXML
    private void closeApplication(ActionEvent event) {
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }

}
