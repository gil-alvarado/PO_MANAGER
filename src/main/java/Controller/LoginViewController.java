/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.ConnectionUtil;
import com.test.App;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
/**
 * FXML Controller class
 *
 * @author Gilbert Alvarado
 */
public class LoginViewController implements Initializable {

    @FXML
    private ComboBox<String> userComboBox;
    
    public static String current_user;
    public static int role_ID;
    
    private LinkedHashMap<Integer, String> users;
    
    @FXML
    private Button login_button;
    private static final String dbName = "BMS_DATABASE_TEST.accdb";
        private Stage fileChooserStage;
    private Preferences DBpref;
    private static String dbLocation;
    private static String dbDirectory;
    @FXML
    private Button exit_button;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO: populate combobox
        
//        users = ConnectionUtil.userIDS();
//        for(Map.Entry<Integer, String> cursor : users.entrySet()){
//            
//        }

//        DBpref = Preferences.userNodeForPackage(LoginViewController.class);
        DBpref = Preferences.userRoot().node(LoginViewController.class.getClass().getName());
        try {
            DBpref.clear();
        } catch (BackingStoreException ex) {
            Logger.getLogger(LoginViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("LoginCOntroller: "+DBpref.absolutePath());
        if(DBpref.getBoolean("firstStart", true)){
            //if first start have user select DB and THEN continue (use show and wait)
//            System.out.println(DBpref.getBoolean("firstStart", true));
            System.out.println("FIRST START");
            fileChooserStage = new Stage();
            showStartDialog();
        }else{
            //get selectedFile pref and set file location in COnnection util and continue
            System.out.println("NOT FIRST START: " +DBpref.get("dbLocation", "root"));
            System.out.println("SIGNED OUT VALUE:" + DBpref.getBoolean("signedIn", true));
            
            dbLocation = DBpref.get("dbLocation", "root");
            dbDirectory = DBpref.get("dbDirectory", "root");
            System.out.println("----------------------------");
            System.out.println("dbLocation: " + dbLocation);
            System.out.println("dbDirectory: " + dbDirectory );
            System.out.println("----------------------------");
            ConnectionUtil.setDbLocation(dbLocation, dbDirectory);
        }
        
        login_button.setDisable(true);
        for(Object un : ConnectionUtil.userIDS().values()){
            userComboBox.getItems().add(un.toString());
        }
//        userComboBox.getSelectionModel().select(0);
        userComboBox.getStyleClass().add("center-aligned");
        userComboBox.setOnAction((event) -> {
            login_button.setDisable(false);
        });
        
    }    
    
    
    
    
    //show new dialog
    private void showStartDialog(){
        
        try {
            
            //open view and use FIleChooser in the new view
            FXMLLoader loader = new FXMLLoader (getClass().getResource("/View/Login/SelectDBView.fxml"));
            Parent parent  = loader.load();
            Scene scene = new Scene(parent, 500 ,300);
            Stage stage = new Stage();
            stage.setMinWidth(500);
            stage.setMinHeight(300);
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
        
            stage.showAndWait();

        } catch (IOException ex) {
            Logger.getLogger(MainLayoutTesting_WITHANCHORController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    public static String getDBLocation(){
        return dbLocation;
    }
    
    @FXML
    private void exitApplication(ActionEvent event) {
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
        System.exit(0);
    }

    @FXML
    private void loginBMS(ActionEvent event) throws IOException  {
        //LOAD MAIN LAYOUT
        
        current_user = (String)userComboBox.getSelectionModel().getSelectedItem();
        try {
            role_ID = ConnectionUtil.userVerification(current_user).getInt("role_id");
//        for(Map.Entry<Integer, String> cursor : users.entrySet()){
//            if(curs)
//        }
        } catch (SQLException ex) {
            role_ID = -1;
            Logger.getLogger(LoginViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

        App.setRoot("/View/Main/MainLayoutTesting_WITHANCHOR");
        
    }
    
}
