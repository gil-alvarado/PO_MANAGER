/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.ConnectionUtil;
import Model.UserInfoModel;
import com.test.App;
import java.io.File;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.util.prefs.Preferences;
/**
 * FXML Controller class
 *
 * @author Gilbert Alvarado
 */
public class MainLayoutController implements Initializable {

    
    @FXML
    private Button overviewButton, manageDBButton, editPOButton;
    
    private Node overviewForm,manageDBForm,addPOForm,editPOForm;
    
    private FXMLLoader overview_loader, manage_loader, edit_loader,addpo_loader;
    private FXMLLoader manageUsers_loader;
    Parent manageUsers_parent ;
    
    @FXML
    private Button addPOBUTTON;
    
    @FXML
    private Label currentUserLabel;
    @FXML
    private Button manageUsersBUTTON;
    @FXML
    private StackPane displayStackPane;
    
//    private int role_ID;
    
    private Preferences DBpref;
    
    private static OverviewViewController overview_controller;
    private static ManageDBViewController manage_db_controller;
    private AddPOViewController add_po_controller;
    private static EditPOViewController edit_po_controller;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        DBpref = Preferences.userRoot().node(LoginViewController.class.getClass().getName());//Preferences.userNodeForPackage(LoginViewController.class);
        System.out.println("MainLayout: "+DBpref.absolutePath());
        DBpref.putBoolean("signedIn", true);
        System.out.println("SIGNING IN VALUE:" + DBpref.getBoolean("signedIn", true));
        
        overviewButton.setDisable(true);

        try{
            
            overview_loader = new FXMLLoader(getClass().getResource("/View/Overview/OverviewView.fxml"));
            addpo_loader = new FXMLLoader(getClass().getResource("/View/EditPO/AddPOView.fxml"));
            edit_loader = new FXMLLoader(getClass().getResource("/View/EditPO/EditPOView.fxml"));
            manage_loader = new FXMLLoader(getClass().getResource("/View/ManageDB/ManageDBView.fxml"));
            
            overviewForm = overview_loader.load(); 
            overview_controller = overview_loader.getController();
                    
            manageDBForm = manage_loader.load(); 
            manage_db_controller = manage_loader.getController();
            manage_db_controller.setMainLayoutContoller(this);
            setInstance(this);
            addPOForm = addpo_loader.load();
            add_po_controller = addpo_loader.getController();
            
            editPOForm = edit_loader.load();
            edit_po_controller = edit_loader.getController();
            
            displayStackPane.getChildren().add(overviewForm);
            displayStackPane.getChildren().add(manageDBForm);
            displayStackPane.getChildren().add(addPOForm);
            displayStackPane.getChildren().add(editPOForm);
            
            overviewForm.toFront();
        
        } catch (IOException ex) {
            Logger.getLogger(MainLayoutController.class.getName()).log(Level.SEVERE, null, ex);
        }        
        
    }
    
    public static OverviewViewController getOverviewViewController(){
        return overview_controller;
    }
    public static ManageDBViewController getManageDBViewController(){
        return manage_db_controller;
    }
    public static EditPOViewController getEditPOController(){
        return edit_po_controller;
    }
    
    private static MainLayoutController instance;
    private static void setInstance(MainLayoutController instance){
       MainLayoutController.instance = instance;
    }
    public static MainLayoutController getInstance(){
        return instance;
    }
    
    
    @FXML
    private void closeApplication(ActionEvent event) throws IOException {
//        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
//        System.exit(0);
        DBpref.putBoolean("signedIn", false);
        System.out.println("SIGNING OUT VALUE:" + DBpref.getBoolean("signedIn", true));
        App.setRoot("/View/Login/LoginView");
    }
    //##########################################################################
    @FXML
    private void openOverviewView(ActionEvent event){
        
        overviewButton.setDisable(true);
        manageDBButton.setDisable(false);
        editPOButton.setDisable(false);
        addPOBUTTON.setDisable(false);
        
        manageUsersBUTTON.setDisable(false);
        
        System.out.println("UPDATING OVERVIEW TABLEVIEW FROM MAINLAYOUT");
        overview_controller.updateTableView();
        overviewForm.toFront();
        
    }
    //##########################################################################
    @FXML
    private void openMangeDBView(ActionEvent event){

        overviewButton.setDisable(false);
        manageDBButton.setDisable(true);
        editPOButton.setDisable(false);
        addPOBUTTON.setDisable(false);
        
        manageUsersBUTTON.setDisable(false); 
        System.out.println("UPDATING MANAGEDB TABLEVIEW FROM MAINLAYOUT");
        manage_db_controller.updateTableView();
        manageDBForm.toFront();
        
    }
    
    //##########################################################################
    @FXML 
    public void openEditPOView(ActionEvent event){
        overviewButton.setDisable(false);
        manageDBButton.setDisable(false);
        editPOButton.setDisable(true);
        addPOBUTTON.setDisable(false);
        
        manageUsersBUTTON.setDisable(false);
        
        editPOForm.toFront();
                
    }
    
    //##########################################################################
    @FXML
    private void openADDPOView(ActionEvent event) {
        overviewButton.setDisable(false);
        manageDBButton.setDisable(false);
        editPOButton.setDisable(false);
        addPOBUTTON.setDisable(true);
        
        manageUsersBUTTON.setDisable(false);
        
        addPOForm.toFront();
        
    }

    //##########################################################################
    
    @FXML
    private void openManageUsersView(ActionEvent event) {

        overviewButton.setDisable(false);
        manageDBButton.setDisable(false);
        editPOButton.setDisable(false);
        addPOBUTTON.setDisable(false);

        manageUsersBUTTON.setDisable(true);

        try {

            manageUsers_loader = new FXMLLoader (getClass().getResource("/View/ManageDB/ManageUsersView.fxml"));
            manageUsers_parent = manageUsers_loader.load();
            Scene scene = new Scene(manageUsers_parent, 800, 500);
            Stage stage = new Stage();
            stage.setMinWidth(800);
            stage.setMinHeight(500);
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
            manageUsersBUTTON.setDisable(false);
            
        } catch (IOException ex) {
            Logger.getLogger(MainLayoutController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    //##########################################################################

    @FXML
    private void openFileChooserDialog(ActionEvent event) {
        
//        try {
//            
////            Parent manageUsers_parent  = selectDb_loader.load();
//            Scene scene = new Scene(manageUsers_parent, 500 ,300);
//            Stage stage = new Stage();
//            stage.setMinWidth(500);
//            stage.setMinHeight(300);
//            stage.setResizable(false);
//            stage.initModality(Modality.APPLICATION_MODAL);
//            stage.setScene(scene);
//        
//            stage.showAndWait();
//
//        } catch (IOException ex) {
//            Logger.getLogger(MainLayoutController.class.getName()).log(Level.SEVERE, null, ex);
//        }

    }
    private static UserInfoModel current_user;
    public void setCurrentUser(UserInfoModel user){
        MainLayoutController.current_user = user;
        if(current_user != null)
            System.out.println(MainLayoutController.current_user.getFirst() + " " + MainLayoutController.current_user.getLast() );
        currentUserLabel.setText(MainLayoutController.current_user.getUser());
        if(current_user.getRole_id() == 1){
            manageUsersBUTTON.setDisable(false);
            manageUsersBUTTON.setVisible(true);
        }else{
            manageUsersBUTTON.setDisable(true);     
            manageUsersBUTTON.setVisible(false);
        }
    }
    public static UserInfoModel getCurrentUser(){
        return current_user;
    }
    
}
