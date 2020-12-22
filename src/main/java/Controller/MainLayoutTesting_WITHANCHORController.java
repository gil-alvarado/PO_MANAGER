/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import com.test.App;
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
import javafx.scene.control.Label;

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
    
    Node overviewForm;
    Node manageDBForm;
    
    FXMLLoader overview_loader, manage_loader, edit_loader,addpo_loader;
    @FXML
    private Button addPOBUTTON;
    @FXML
    private AnchorPane AddPOAnchor;
    @FXML
    private Label currentUserLabel;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        overviewButton.setDisable(true);
        currentUserLabel.setText(LoginViewController.current_user);
        
//        editPOButton.setDisable(true);
        
        try{
            
            overview_loader = new FXMLLoader(getClass().getResource("/View/Overview/OverviewViewTesting_NOANCHOR.fxml"));

    //        Node overviewForm = FXMLLoader.load(getClass().getResource("/View/Overview/OverviewViewTesting_NOANCHOR.fxml"));        
            overviewForm = overview_loader.load();        
            overviewAnchor.getChildren().add(overviewForm);
            AnchorPane.setTopAnchor(overviewForm, 0.0);
            AnchorPane.setBottomAnchor(overviewForm, 0.0);
            AnchorPane.setRightAnchor(overviewForm,0.0);
            AnchorPane.setLeftAnchor(overviewForm, 0.0);
            overviewAnchor.toFront();

            
            edit_loader = new FXMLLoader(getClass().getResource("/View/EditPO/EditPOView_NOANCHOR.fxml"));
    //        Node editPOForm = FXMLLoader.load(getClass().getResource("/View/EditPO/EditPOView_NOANCHOR.fxml"));
            Node editPOForm = edit_loader.load();
            EditPOAnchor.setStyle("-fx-background-color : magenta");
            AnchorPane.setTopAnchor(editPOForm, 0.0);
            AnchorPane.setBottomAnchor(editPOForm, 0.0);
            AnchorPane.setRightAnchor(editPOForm,0.0);
            AnchorPane.setLeftAnchor(editPOForm, 0.0);
            EditPOAnchor.getChildren().add(editPOForm);
//        
//        
            addpo_loader = new FXMLLoader(getClass().getResource("/View/EditPO/AddPOView.fxml"));
    //        Node editPOForm = FXMLLoader.load(getClass().getResource("/View/EditPO/EditPOView_NOANCHOR.fxml"));
            Node addPOForm = addpo_loader.load();
            AddPOAnchor.setStyle("-fx-background-color : orange");
            AnchorPane.setTopAnchor(addPOForm, 0.0);
            AnchorPane.setBottomAnchor(addPOForm, 0.0);
            AnchorPane.setRightAnchor(addPOForm,0.0);
            AnchorPane.setLeftAnchor(addPOForm, 0.0);
            AddPOAnchor.getChildren().add(addPOForm); 
        
        } catch (IOException ex) {
            Logger.getLogger(MainLayoutTesting_WITHANCHORController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }    
    
    @FXML
    private void closeApplication(ActionEvent event) throws IOException {
//        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
//        System.exit(0);
        App.setRoot("/View/Login/LoginView");
    }
    //##########################################################################
    @FXML
    private void openOverviewView(ActionEvent event){
        
        overviewButton.setDisable(true);
        manageDBButton.setDisable(false);
        editPOButton.setDisable(false);
        addPOBUTTON.setDisable(false);
        
        try{
            overviewAnchor.getChildren().removeAll(overviewForm);
            overview_loader = new FXMLLoader(getClass().getResource("/View/Overview/OverviewViewTesting_NOANCHOR.fxml"));

    //        Node overviewForm = FXMLLoader.load(getClass().getResource("/View/Overview/OverviewViewTesting_NOANCHOR.fxml"));        
            overviewForm = overview_loader.load();     

            overviewAnchor.getChildren().add(overviewForm);
            AnchorPane.setTopAnchor(overviewForm, 0.0);
            AnchorPane.setBottomAnchor(overviewForm, 0.0);
            AnchorPane.setRightAnchor(overviewForm,0.0);
            AnchorPane.setLeftAnchor(overviewForm, 0.0);
            overviewAnchor.toFront();
        } catch (IOException ex) {
            Logger.getLogger(MainLayoutTesting_WITHANCHORController.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    //##########################################################################
    @FXML
    private void openMangeDBView(ActionEvent event){
        
        overviewButton.setDisable(false);
        manageDBButton.setDisable(true);
        editPOButton.setDisable(false);
        addPOBUTTON.setDisable(false);
        try{
//            manageDBAnchor.getChildren().removeAll(manageDBForm);
            manage_loader = new FXMLLoader(getClass().getResource("/View/ManageDB/ManageDBView.fxml"));
            
    //        Node overviewForm = FXMLLoader.load(getClass().getResource("/View/Overview/OverviewViewTesting_NOANCHOR.fxml"));        
            manageDBForm = manage_loader.load();     

            manageDBAnchor.getChildren().add(manageDBForm);
            AnchorPane.setTopAnchor(manageDBForm, 0.0);
            AnchorPane.setBottomAnchor(manageDBForm, 0.0);
            AnchorPane.setRightAnchor(manageDBForm,0.0);
            AnchorPane.setLeftAnchor(manageDBForm, 0.0);
            
            ManageDBViewController controller = manage_loader.getController();
            controller.setEditPOController(edit_loader.getController());
            controller.setEditPOLoader(edit_loader);
            controller.setMainLayoutInstance(this);
        
        manageDBAnchor.toFront();
        } catch (IOException ex) {
            Logger.getLogger(MainLayoutTesting_WITHANCHORController.class.getName()).log(Level.SEVERE, null, ex);
        }      
    }
    public void editToTop(){
        EditPOAnchor.toFront();
        overviewButton.setDisable(false);
        manageDBButton.setDisable(false);
        editPOButton.setDisable(true);
        addPOBUTTON.setDisable(false);
    }
    //##########################################################################
    @FXML 
    private void openEditPOView(ActionEvent event){
        overviewButton.setDisable(false);
        manageDBButton.setDisable(false);
        editPOButton.setDisable(true);
        addPOBUTTON.setDisable(false);
//        overviewAnchor.toBack();
        EditPOView_NOANCHORController controller = edit_loader.getController();
        controller.setMainLayoutInstance(this);
        EditPOAnchor.toFront();
    }
      
    public void manageDBToTop(){
//        overviewButton.setDisable(false);
//        manageDBButton.setDisable(true);
//        editPOButton.setDisable(false);
//        addPOBUTTON.setDisable(false);
//        manageDBAnchor.toFront();
    }
    //##########################################################################
    @FXML
    private void openADDPOView(ActionEvent event) {
        overviewButton.setDisable(false);
        manageDBButton.setDisable(false);
        editPOButton.setDisable(false);
        addPOBUTTON.setDisable(true);
        
        AddPOAnchor.toFront();
    }
}
