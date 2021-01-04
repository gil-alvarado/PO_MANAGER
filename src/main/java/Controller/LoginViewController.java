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
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
