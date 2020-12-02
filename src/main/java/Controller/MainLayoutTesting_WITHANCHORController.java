/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

//import Controller.MainLayoutTestingController;
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
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
/**
 * FXML Controller class
 *
 * @author Gilbert Alvarado
 */
public class MainLayoutTesting_WITHANCHORController implements Initializable {


//    @FXML
//    private StackPane displaysStackPane;
    @FXML
    private AnchorPane overviewAnchor;
    @FXML
    private AnchorPane manageDBAnchor;
    @FXML
    private AnchorPane EditPOAnchor;
    
    @FXML
    private Button overviewButton, manageDBButton, editPOButton, signOutButton;
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        overviewButton.setDisable(true);
        try{
        
        //ADD forms to their respective panes   
        
        //works. original load was View/Overview...NOANCHOR.fxml
        Node overviewForm = FXMLLoader.load(getClass().getResource("/View/Overview/OverviewViewTesting_NOANCHOR.fxml"));        
//        Node overviewForm = FXMLLoader.load(getClass().getResource("/View/Overview/OverviewViewTesting_NOANCHOR.fxml"));        
        overviewAnchor.getChildren().add(overviewForm);
        AnchorPane.setTopAnchor(overviewForm, 0.0);
        AnchorPane.setBottomAnchor(overviewForm, 0.0);
        AnchorPane.setRightAnchor(overviewForm,0.0);
        AnchorPane.setLeftAnchor(overviewForm, 0.0);
        overviewAnchor.toFront();
        
        Node manageDBForm = FXMLLoader.load(getClass().getResource("/View/ManageDB/ManageDBView.fxml"));
        manageDBAnchor.getChildren().add(manageDBForm);
        AnchorPane.setTopAnchor(manageDBForm, 0.0);
        AnchorPane.setBottomAnchor(manageDBForm, 0.0);
        AnchorPane.setRightAnchor(manageDBForm,0.0);
        AnchorPane.setLeftAnchor(manageDBForm, 0.0);

                

        //works: 
        //for new edit form: 
        /*
        BorderPane
        
        Center
        */
//        Node editPOForm = FXMLLoader.load(getClass().getResource("/View/EditPO/EditPOView_NOANCHOR_COPY.fxml"));
        Node editPOForm = FXMLLoader.load(getClass().getResource("/View/EditPO/EditPOView_NOANCHOR.fxml"));
        EditPOAnchor.setStyle("-fx-background-color : magenta");
        AnchorPane.setTopAnchor(editPOForm, 0.0);
        AnchorPane.setBottomAnchor(editPOForm, 0.0);
        AnchorPane.setRightAnchor(editPOForm,0.0);
        AnchorPane.setLeftAnchor(editPOForm, 0.0);
        EditPOAnchor.getChildren().add(editPOForm);
        
        
        //set constraints for newly added pane
        //RECALL: newly added pane DOES NOT HAVE AN ANCHOR. THAT'S WHY WE'RE
        //SETTING THE CONSTRAINTS PROGRAMMATICALLY 
        
        } catch (IOException ex) {
            Logger.getLogger(MainLayoutTestingController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }    
    
    @FXML
    private void closeApplication(ActionEvent event) {
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }
    //##########################################################################
    @FXML
    private void openOverviewView(ActionEvent event){
        
        overviewButton.setDisable(true);
        manageDBButton.setDisable(false);
        editPOButton.setDisable(false);
        
        overviewAnchor.toFront();
        System.out.println("overview button pressed!");
        
    }
    //##########################################################################
    @FXML
    private void openMangeDBView(ActionEvent event){
        
        overviewButton.setDisable(false);
        manageDBButton.setDisable(true);
        editPOButton.setDisable(false);
        manageDBAnchor.toFront();
        
    }
    //##########################################################################
    @FXML 
    private void openEditPOView(ActionEvent event){
        
        overviewButton.setDisable(false);
        manageDBButton.setDisable(false);
        editPOButton.setDisable(true);
        
//        overviewAnchor.toBack();
        EditPOAnchor.toFront();
        System.out.println("edit button pressed!");
    }
}
