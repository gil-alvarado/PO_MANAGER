/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.BMSPurchaseOrderModel;
import Model.ConnectionUtil;
import Model.FileHelper;
import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
/**
 * FXML Controller class
 *
 * @author Gilbert Alvarado
 */
public class NotesViewController implements Initializable {

    @FXML
    private Label poDisplayLabel;
    @FXML
    private Label dateDisplayLabel, fuDataLabel;
    @FXML
    private TextArea existingMessageTextArea;
    @FXML
    private TextArea newMessageTextArea;
    
//    private String currentPO;
    private File csv_file;
    private BMSPurchaseOrderModel po;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        existingMessageTextArea.setEditable(false);
        
    }    
    
    @FXML
    private void addNote(ActionEvent event) {
        
        String new_content = newMessageTextArea.getText().replaceAll("[\\t\\n\\r]+"," ");
        ButtonType YES = new ButtonType("YES", ButtonBar.ButtonData.YES);
        ButtonType NO = new ButtonType("NO", ButtonBar.ButtonData.CANCEL_CLOSE);
        Optional<ButtonType> result;
        
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        {
            confirmation.setTitle("CONFIRM DIALOG");
            confirmation.getButtonTypes().clear();
            confirmation.getDialogPane().getButtonTypes().addAll( YES, NO );
            confirmation.setContentText("ARE YOU SURE YOU WANT TO ADD NOTE?");
        }
                    
        if( !new_content.trim().isEmpty() ){
            result = confirmation.showAndWait();
            
            //##################################################################
            //UPDATE FILE/ATTACHMENT
            //##################################################################
            
            if(result.orElse(NO) == YES){//user confirms yes to add note
                if(FileHelper.updateFUCsvFile(csv_file, poDisplayLabel.getText(),new_content) == true){//update/clear text areas
                    //now add/update file to database
                    if(ConnectionUtil.updateNotesAttachments(this.po.getPurchase_order()) == 1){
                        ButtonType OK = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                        Alert alert = new Alert(Alert.AlertType.NONE);
                        {
                            alert.setTitle("update");
                            alert.getButtonTypes().clear();
                            alert.getDialogPane().getButtonTypes().addAll( OK );
                            alert.setContentText("SUCCESSFULLY UPDATED");
                            alert.showAndWait();
                        }
                        newMessageTextArea.clear();
                        existingMessageTextArea.clear();
                        setContext(this.csv_file, this.po);
                    }
                }//else error
                else{
                    ButtonType OK = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                    Alert alert = new Alert(Alert.AlertType.NONE);
                        {
                            alert.setTitle("update");
                            alert.getButtonTypes().clear();
                            alert.getDialogPane().getButtonTypes().addAll( OK );
                            alert.setContentText("ERROR: CANNOT UPDATE");
                            alert.showAndWait();
                        }
                }    
            }
        }
    }

    @FXML
    private void closeDialog(ActionEvent event) {
        Node  source = (Node)  event.getSource(); 
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }
    
    private static final String lineBreak = "-----------------------------------\n";
    
    public void setContext(File csv_file, BMSPurchaseOrderModel po){
        if(this.csv_file == null)
            this.csv_file = csv_file;
        if(this.po == null)
            this.po = po;
        
        if(csv_file !=null){
            String file_content = FileHelper.csvFileContent(this.csv_file);
            existingMessageTextArea.setText(file_content);
        }
        else
            existingMessageTextArea.setPromptText("no notes added!");
        
        if(this.po.getFu_ship_date()!=null)
            dateDisplayLabel.setText(this.po.getFuDateFormat());
        else{
            dateDisplayLabel.setText(this.po.getFuDateFormat());
        }
        
        if(this.po.getPurchase_order() != null)
            poDisplayLabel.setText(this.po.getPurchase_order());
        else
            poDisplayLabel.setText("N/A");
            
    }
    public void clearFields(){
        existingMessageTextArea.clear();
        poDisplayLabel.setText("");
        dateDisplayLabel.setText("");
        newMessageTextArea.setText("");
    }

}
