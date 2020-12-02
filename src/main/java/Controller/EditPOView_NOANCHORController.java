/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.ConnectionUtil;
import static Model.ConnectionUtil.conDB;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

/**
 * FXML Controller class
 *
 * @author Gilbert Alvarado
 */
public class EditPOView_NOANCHORController implements Initializable {

    @FXML
    private GridPane paneTextFieldsONE, DataEntryPanes;
    @FXML
    private HBox HBoxButtons;
    @FXML
    private TextField TextFieldPO, TextFieldSUPPLIER, TextFieldINVOICE, TextFieldBRG
            ,TextFieldPARAMETER, TextFieldQTY, TextFieldLANDING;
    
    @FXML
    private ComboBox ComboBoxCONFIRMED;
    
    @FXML
    private DatePicker DatePickerORGSHIP, DatePickerCURSHIP, 
            DatePickerETABMS, DatePickerFUDATE;
    @FXML
    private Button BUTTONaddToDatabase, BUTTONUpdateDatabase, BUTTONDeleteDatabase;
    
    //##########################################################################
    @FXML
    private TextArea TextAreaRWCOMMENTS;
    //similar to adding data from database. look at previous app version
    @FXML
    private ListView ListViewATTACHMENTS;
    @FXML
    private TextArea TextAreaFUNOTES;
    
    @FXML
    private Label dragLABEL;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        System.out.println("hello from the edit po controller!");
        
        BUTTONaddToDatabase.setDisable(false);
        BUTTONUpdateDatabase.setDisable(false);
        BUTTONDeleteDatabase.setDisable(false);
        
        ComboBoxCONFIRMED.getItems().addAll("YES", "NO");
        ComboBoxCONFIRMED.setEditable(false);
        ComboBoxCONFIRMED.getSelectionModel().select("YES");
                
        //----------------------------------------------------------------------
        TextFieldPO.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { 
                    if(TextFieldPO.getText().isEmpty()){
                        //when it not matches the pattern (1.0 - 6.0)
                        //set the textField empty
                        TextFieldPO.setText("");
                    }
//                    else
//                        isValidated = true;
                }
        });
        //----------------------------------------------------------------------
        TextFieldINVOICE.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue) { 
                    if(!TextFieldINVOICE.getText().matches("\\d+(\\.\\d+)?")){
                        //when it not matches the pattern (1.0 - 6.0)
                        //set the textField empty
                        TextFieldINVOICE.setText("");
                    }
//                    else
//                        isValidated = true;
                }
        });
        //----------------------------------------------------------------------
        TextFieldLANDING.focusedProperty().addListener((observable, oldValue, newValue) -> {
//            setAddButtonSettings();
                    if (!newValue) { 
                        if(!TextFieldLANDING.getText().matches("\\d+(\\.\\d+)?")){
                            //when it not matches the pattern (1.0 - 6.0)
                            //set the textField empty
                            TextFieldLANDING.setText("");
                        }
                    }
        });
        
        //TODO: eliminate forloop, change to: for each node in paneTextFieldsONE
        //elminated oaneTextFieldTWO
        /*
        for(int i = 0; i < DataEntryPanes.getColumnCount(); i++){

            GridPane p = (GridPane)DataEntryPanes.getChildren().get(i);

                for(Node node: p.getChildren()){
                    if(node instanceof TextField || node instanceof DatePicker){ // if it's a TextField
                        if(node instanceof TextField){
                            ((TextField)node).textProperty().addListener((obs, old, newV)->{ // add a change listener to every TextField

                              if(!newV.trim().isEmpty()&& isTextFieldAllFilled(paneTextFieldsONE)){ 
                                  if(isDatePickerFilled(paneTextFieldsONE))
                                    BUTTONaddToDatabase.setDisable(false); // then make the button active again
                                  else{
                                      System.out.println("fill out dates");
                                      BUTTONaddToDatabase.setDisable(true);
                                  }
                              }
                              else{
                                  BUTTONaddToDatabase.setDisable(true); // or else, make it disable until it achieves the required condition 
                              }
                          });  
                        }
                        else{
                            ((DatePicker)node).valueProperty().addListener((obs, old, newV)->{ // add a change listener to every TextField

                              if(newV != null && isDatePickerFilled(paneTextFieldsONE)){ 
                                  if(isTextFieldAllFilled(paneTextFieldsONE))
                                    BUTTONaddToDatabase.setDisable(false); // then make the button active again
                                  else{
                                      System.out.println("fill out fields");
                                      BUTTONaddToDatabase.setDisable(true);
                                  }
                              }
                              else{
                                  BUTTONaddToDatabase.setDisable(true); // or else, make it disable until it achieves the required condition 
                              }
                          });  
                        }
                        
                    }//end instanceof TextField/DatePicker
                }//end forloop
        }
        
        */
    }    
    //#########################################################################
//    @FXML
//    private TextField TextFieldPO, TextFieldSUPPLIER, TextFieldINVOICE, TextFieldBRG
//            ,TextFieldPARAMETER, TextFieldQTY, TextFieldLANDING;
    
//    @FXML
//    private ComboBox<?> ComboBoxCONFIRMED;
//    
//    @FXML
//    private DatePicker DatePickerORGSHIP, DatePickerCURSHIP, 
//            DatePickerETABMS, DatePickerFUDATE;    
    @FXML
    private void addToDatabase(ActionEvent event){
//        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close(); 
        //get TextField data and store to database
        
        
        try{
            
            Connection con = ConnectionUtil.conDB();
//            PreparedStatement pst = con.prepareStatement("INSERT INTO parameter_test ( PARAMETER, description )\n" +
//"VALUES ('SS', 'stainless steel');");
//int numRowsEx = pst.executeUpdate();
//            System.out.println("number of rows executed: " + numRowsEx);

            Statement statement = conDB().createStatement();
            ResultSet result = statement.executeQuery("SELECT COUNT(*) FROM parameter_test WHERE PARAMETER = "
                    + "'" + TextFieldPARAMETER.getText().toUpperCase()+ "';");
//            ResultSetMetaData rsmd = rs.getMetaData();
           System.out.println(result);
            
           result.next();
           if(Integer.parseInt(result.getString(1)) >=1){
           System.out.println("COUNT: " + result.getString(1));
           //System.out.println("...comment out line 184");
           }
           else
           {
               System.out.println("PARAMTER NOT FOUND, ADDING TO TABLE PARAMETER;");
           }
            
            /*
                Connection con = ConnectionUtil.conDB();

                PreparedStatement pst = con.prepareStatement("INSERT INTO purchaseorders(purchaseOrder, InvoicePrice,LandedCost,"
                        + "Original_Ship_Date,ETA_BMS,CURRENT_SHIP_DATE,FUdate,Confirmed) values(?,?,?,?,?,?,?,?);");
                if(EditPOView_NOANCHORController.isTextFieldAllFilled(paneTextFieldsONE) 
                        && EditPOView_NOANCHORController.isDatePickerFilled(paneTextFieldsONE)){
                   //Format(FUdate,'Short Date')

                    pst.setString(1, TextFieldPO.getText());
                    pst.setDouble(2, Double.parseDouble(TextFieldINVOICE.getText()));
                    pst.setDouble(3,Double.parseDouble(TextFieldLANDING.getText()));
                    pst.setDate(4, java.sql.Date.valueOf(DatePickerORGSHIP.getValue()));
                    pst.setDate(5, java.sql.Date.valueOf(DatePickerETABMS.getValue()));
                    pst.setDate(6, java.sql.Date.valueOf(DatePickerCURSHIP.getValue()));
                    pst.setDate(7, java.sql.Date.valueOf(DatePickerFUDATE.getValue()));

                    if(ComboBoxCONFIRMED.getValue().equals("YES"))
                        pst.setString(8, "TRUE");
                    else
                        pst.setString(8, "FALSE");

                    pst.executeUpdate();
                    System.out.println("successfuly added!");

                    clearAllFields();
                }
*/

            } catch (SQLException ex) { 
            Logger.getLogger(EditPOView_NOANCHORController.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        
//        System.out.println("SQL DATE: " + java.sql.Date.valueOf(DatePickerFUDATE.getValue()));
//        System.out.println("DatePicker Value: " + DatePickerFUDATE.getValue());
//        System.out.println("WORDS WITH SPACING  !");
//        System.out.println(("WORDS WITHout SPACING  !").replaceAll("\\s+", ""));
      

        //System.out.println("Column ONE filled: " + EditPOView_NOANCHORController.isTextFieldAllFilled(paneTextFieldsONE));
        
        //System.out.println("Column ONE DATE filled: " + EditPOView_NOANCHORController.isDatePickerFilled(paneTextFieldsONE));
        clearAllFields();
    }
    //#########################################################################
    @FXML
    private void updateDatabase(ActionEvent event){
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }
    //#########################################################################
    @FXML
    private void deleteFromDatabase(ActionEvent event){
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }
    //##########################################################################
    
    @FXML
    private void handleDragOver(DragEvent event){
        if(event.getDragboard().hasFiles())
        event.acceptTransferModes(TransferMode.COPY);
    }
    @FXML
    private ImageView imageView;
    @FXML
    private void handleDrop(DragEvent event) throws FileNotFoundException{
//        List <File> files = event.getDragboard().getFiles();
//        Image img = new Image(new FileInputStream(files.get(0)));
//        imageView.setImage(img); 
      Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasFiles()) {
                    success = true;
                    String filePath = null;
                    for (File file:db.getFiles()) {
                        filePath = file.getAbsolutePath();
                        System.out.println(filePath);
                        //add file to ListView
                        ListViewATTACHMENTS.getItems().add(filePath);
                    }
                }
                event.setDropCompleted(success);
                event.consume();
            
    }
    //----------------------------------------------------------------------
        private static final String PREFIX =
            "http://icons.iconarchive.com/icons/jozef89/origami-birds/72/bird";

    private static final String SUFFIX =
            "-icon.png";

    private static final ObservableList<String> birds = FXCollections.observableArrayList(
            "-black",
            "-blue",
            "-red",
            "-red-2",
            "-yellow",
            "s-green",
            "s-green-2"
    );
    //##########################################################################
//    @FXML
//    private TextField TextFieldPO, TextFieldSUPPLIER, TextFieldINVOICE, TextFieldBRG
//            ,TextFieldPARAMETER, TextFieldQTY, TextFieldLANDING;
    
//    @FXML
//    private ComboBox<?> ComboBoxCONFIRMED;
//    
//    @FXML
//    private DatePicker DatePickerORGSHIP, DatePickerCURSHIP, 
//            DatePickerETABMS, DatePickerFUDATE;
    
    private static boolean isTextFieldAllFilled(GridPane table){
        for(Node node : table.getChildren()){ // cycle through every component in the table (GridPane)
            if(node instanceof TextField){ // if it's a TextField
            // after removing the leading spaces, check if it's empty
                if(((TextField)node).getText().trim().isEmpty()){
                        return false; // if so, return false
                }
            }       
        }
        return true;
    }
    //--------------------------------------------------------------------------
    private static boolean isDatePickerFilled(GridPane table){
        for(Node node : table.getChildren()){
            if(node instanceof DatePicker){
                LocalDate date = ((DatePicker) node).getValue();
                if(((DatePicker) node).getValue() == null ||
                        ((DatePicker) node).getValue().toString().trim().isEmpty())
                    return false;
            }
        }
        return true;
    }
    //--------------------------------------------------------------------------
    private void clearAllFields(){
        for(Node node : paneTextFieldsONE.getChildren()){
            if(node instanceof TextField){
                ((TextField)node).clear();
            }
            else if(node instanceof DatePicker){
                ((DatePicker)node).getEditor().clear();
            }
        }
        
      
    }
    //--------------------------------------------------------------------------
    private void validateEntryForm(GridPane parent, Button button){
        for(int i = 0; i < parent.getColumnCount(); i++){

            GridPane p = (GridPane)parent.getChildren().get(i);

                for(Node node: p.getChildren())
                    if(node instanceof TextField){ // if it's a TextField
                        ((TextField)node).textProperty().addListener((obs, old, newV)->{ // add a change listener to every TextField

                          if(!newV.trim().isEmpty()&& isTextFieldAllFilled(paneTextFieldsONE) 
                                && isDatePickerFilled(paneTextFieldsONE)){ 
                             BUTTONaddToDatabase.setDisable(false); // then make the button active again
                          }
                          else{
                              BUTTONaddToDatabase.setDisable(true); // or else, make it disable until it achieves the required condition 
                          }
                      });
                }
        }        
    }
}
