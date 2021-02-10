/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.ConnectionUtil;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Gilbert Alvarado
 */
public class SelectDBViewController implements Initializable {

    @FXML
    private Button selectDbBUTTON;
    @FXML
    private Button okBUTTON;
    @FXML
    private Button cancelBUTTON;
    @FXML
    private TextField dbLocationTextField;
    @FXML
    private Label dbStatusLABEL;
    
    private Preferences DBpref;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        DBpref = Preferences.userRoot().node(LoginViewController.class.getClass().getName());
        
        String dbLocation = DBpref.get("dbLocation", "no db selected");
        dbLocationTextField.setText(dbLocation);
       if(dbLocation.contains(dbName))
           dbStatusLABEL.setText("correct DB already selected");
       else{//should not print since DB is already set  on first start
           dbStatusLABEL.setText("locate and slecect correct DB");
           dbStatusLABEL.setStyle("-fx-text-fill : red;");
       }
       
        okBUTTON.setDisable(true);
        dbLocationTextField.setDisable(true);
        fileChooserStage = new Stage();
        
    }    
    
private static final String dbName = "BMS_DATABASE.accdb";
private Stage fileChooserStage;
private static String dbLocation;
File selectedFile;

    //--------------------------------------------------------------------------
    @FXML
    private void openFileChooserDialog(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        DBpref = Preferences.userRoot().node(LoginViewController.class.getClass().getName());
                
        selectedFile = fileChooser.showOpenDialog(fileChooserStage);
        
        if(selectedFile != null)
            if(selectedFile.getName().equals(dbName)){
 
                dbLocation = selectedFile.getAbsolutePath();
                String dbDirectory = selectedFile.getParent();
                System.out.println("DATABASE SUB DIR: "  + dbDirectory);
                //--------------------------------------------------------------
                //  set new parameters in setDbLocation
                //--------------------------------------------------------------
                ConnectionUtil.setDbLocation(dbLocation, dbDirectory);
                //--------------------------------------------------------------
                DBpref.putBoolean("firstStart", false);
                DBpref.put("dbLocation", dbLocation);//setting DB path
                DBpref.put("dbDirectory", dbDirectory);
                DBpref.putBoolean("dbSet", true);
                System.out.println("SelectDBViewController: full path: "  +DBpref.get("dbLocation", "root"));
                System.out.println("SelectDBViewController: directory: "  +DBpref.get("dbDirectory", "root") );
                okBUTTON.setDisable(false);
                dbStatusLABEL.setText("correct DB selected");
                dbStatusLABEL.setStyle("-fx-text-fill : #06f50a;");
                dbLocationTextField.setText(selectedFile.getAbsolutePath());     
                fileChooserStage.close();
            }else{
                okBUTTON.setDisable(true);
                dbStatusLABEL.setText("locate and slecect correct DB");
                dbStatusLABEL.setStyle("-fx-text-fill : red;");
                dbLocationTextField.setText(selectedFile.getAbsolutePath());
            }

    }

    //confirm and continue
    //update pref
    @FXML//ok button
    private void closeDialog(ActionEvent event) {
        Node  source = (Node)  event.getSource(); 
        Stage stage  = (Stage) source.getScene().getWindow();
        DBpref.put("dbLocation", dbLocation);
        
        stage.close();
    }

    //CLOS APPLICATION
    @FXML
    private void close(ActionEvent event) {
//        Node  source = (Node)  event.getSource(); 
//        Stage stage  = (Stage) source.getScene().getWindow();
//        stage.close();
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
        System.exit(0);
    }
    
}
