/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
/**
 * FXML Controller class
 *
 * @author Gilbert Alvarado
 */
public class EditPOView_NOANCHOR_COPYController implements Initializable {


    @FXML
    private GridPane DataEntryPanes;
    @FXML
    private GridPane paneTextFieldsONE;
    @FXML
    private TextField TextFieldPO;
    @FXML
    private TextField TextFieldSUPPLIER;
    @FXML
    private ComboBox<?> ComboBoxCONFIRMED;
    @FXML
    private TextField TextFieldINVOICE;
    @FXML
    private DatePicker DatePickerORGSHIP;
    @FXML
    private DatePicker DatePickerCURSHIP;
    @FXML
    private DatePicker DatePickerORGSHIP1;
    @FXML
    private DatePicker DatePickerORGSHIP2;
    @FXML
    private TextField TextFieldPO1;
    @FXML
    private TextField TextFieldPO2;
    @FXML
    private TextField TextFieldPO21;
    @FXML
    private TextField TextFieldPO22;
    @FXML
    private HBox HBoxButtons;
    @FXML
    private Button BUTTONaddToDatabase;
    @FXML
    private Button BUTTONUpdateDatabase;
    @FXML
    private Button BUTTONDeleteDatabase;
    @FXML
    private TextArea TextAreaRWCOMMENTS;
    @FXML
    private ListView<?> ListViewATTACHMENTS;
    @FXML
    private TextArea TextAreaFUNOTES;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    private void addToDatabase(ActionEvent event) {
    }

    @FXML
    private void updateDatabase(ActionEvent event) {
    }

    @FXML
    private void deleteFromDatabase(ActionEvent event) {
    }

}
