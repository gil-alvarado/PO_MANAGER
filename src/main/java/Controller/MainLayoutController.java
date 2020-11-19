/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
/**
 * FXML Controller class
 *
 * @author Gilbert Alvarado
 */
public class MainLayoutController implements Initializable {


    @FXML
    private Button overviewButton;
    @FXML
    private Button ManageDBbutton;
    @FXML
    private Button EditPObutton;
    @FXML
    private Button signOutButton;
    
    @FXML
    private Pane overviewPane,ManageDBPane,EditPOPane;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Node overviewForm = FXMLLoader.load(getClass().getResource("/View/OverviewView.fxml"));
//            Node overviewForm = FXMLLoader.load(getClass().getResource("/View/NewOverviewView.fxml"));
            overviewPane.getChildren().add(overviewForm);
//            overviewPane.setMinHeight(950);
//            overviewPane.setMinWidth(1500);
            overviewPane.setStyle("-fx-background-color : lime");
            overviewPane.toFront();
            //add/load NewEntry, ManageDB to their respective panes
            
//            Node newOrderForm = FXMLLoader.load(getClass().getResource("/View/NewEntry.fxml"));
//            EditPOPane.setStyle("-fx-background-color : #757575");
//            EditPOPane.getChildren().add(newOrderForm);
//            
//            Node ManageDBForm = FXMLLoader.load(getClass().getResource("/View/ManageDBView.fxml"));
//            ManageDBPane.setStyle("-fx-background-color : #757575");
//            ManageDBPane.getChildren().add(ManageDBForm);
            
            //HIDE BUTTON FOR NOW
//            btnViewUsers.setVisible(false);
            
            
        } catch (IOException ex) {
            Logger.getLogger(MainLayoutController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    @FXML
    private void closeApplication(ActionEvent event) {
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }

}
