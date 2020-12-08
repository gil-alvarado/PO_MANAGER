/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.ConnectionUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
/**
 * FXML Controller class
 *
 * @author Gilbert Alvarado
 */
public class AddPOViewController implements Initializable {


    @FXML
    private GridPane DataEntryPanes;
    @FXML
    private GridPane paneTextFieldsONE;
    //--------------------------------------------------------------------------
    @FXML
    private ComboBox<String> ComboBoxCONFIRMED;
    @FXML
    private TextField TextFieldPO, TextFieldSUPPLIER, TextFieldINVOICE, TextFieldBRG,
            TextFieldPARAMETER, TextFieldQTY, TextFieldLANDING;
    //--------------------------------------------------------------------------
    @FXML
    private DatePicker DatePickerORGSHIP, DatePickerETABMS, DatePickerFUDATE, DatePickerCURSHIP;
    //--------------------------------------------------------------------------
    @FXML
    private Label dragLABEL;
    //--------------------------------------------------------------------------
    @FXML
    private Button BUTTONaddToDatabase;
    //--------------------------------------------------------------------------
    @FXML
    private TextArea TextAreaRWCOMMENTS;
    @FXML
    private ListView<String> ListViewATTACHMENTS;
    @FXML
    private TextArea TextAreaFUNOTES;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        BUTTONaddToDatabase.setDisable(true);
        
        ComboBoxCONFIRMED.getItems().addAll("YES", "NO");
        ComboBoxCONFIRMED.setEditable(false);
        ComboBoxCONFIRMED.getSelectionModel().select("YES");
        
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
        TextFieldPO.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { 
                    if(TextFieldPO.getText().isEmpty()){
                        //when it not matches the pattern (1.0 - 6.0)
                        //set the textField empty
                        TextFieldPO.setText("");
                    }
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
                }
        });
        //----------------------------------------------------------------------
        TextFieldLANDING.focusedProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue) { 
                        if(!TextFieldLANDING.getText().matches("\\d+(\\.\\d+)?")){
                            //when it not matches the pattern (1.0 - 6.0)
                            //set the textField empty
                            TextFieldLANDING.setText("");
                        }
                    }
        });
        //------------------------------------------------------------------------------        
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

        
    }    
    //##########################################################################
    @FXML
    private void addToDatabase(ActionEvent event) {
        
        try{
            Connection con = ConnectionUtil.conDB();

//            PreparedStatement pst = con.prepareStatement("INSERT INTO purchaseorders(purchaseOrder, InvoicePrice,LandedCost,"
//                    + "Original_Ship_Date,ETA_BMS,CURRENT_SHIP_DATE,FUdate,Confirmed) values(?,?,?,?,?,?,?,?);");
            
//------------------------------------------------------------------------------
//PURCHASE_ORDERS
//------------------------------------------------------------------------------
PreparedStatement pst = 
                    con.prepareStatement("INSERT INTO "
                            + "purchase_orders(purchase_order, original_ship_date, current_ship_date,eta_bms,"
                            + "fu_date)  "
                            + "values(?,?,?,?,?);");
           
            if(isTextFieldAllFilled(paneTextFieldsONE) 
                    && isDatePickerFilled(paneTextFieldsONE)){
               //Format(FUdate,'Short Date')

                pst.setString(1, TextFieldPO.getText());
                pst.setDate(2, java.sql.Date.valueOf(DatePickerORGSHIP.getValue()));
                pst.setDate(3, java.sql.Date.valueOf(DatePickerCURSHIP.getValue()));
                pst.setDate(4, java.sql.Date.valueOf(DatePickerETABMS.getValue()));
                pst.setDate(5, java.sql.Date.valueOf(DatePickerFUDATE.getValue()));
                
                
                pst.executeUpdate();
//------------------------------------------------------------------------------
//ORDER_DETAILS
//------------------------------------------------------------------------------  

//PARAMETER 
pst = con.prepareStatement("SELECT PARAMETER\n" +
"FROM PARAMETER\n" +
"WHERE PARAMETER = ?;");
                pst.setString(1,TextFieldPARAMETER.getText());
      
                ResultSet rs = pst.executeQuery();
                
                System.out.println("CHECKING PARAMET IF EXISTS");
                
                if(rs.next()){//PARAMETER DNE, add to PARAMETER then INSERT details
                    System.out.println(TextFieldPARAMETER.getText() + ": DOES EXIST");
                }
                else{
                    System.out.println(TextFieldPARAMETER.getText() + " DOES NOT EXIST, ADDING TO PARAMETER" );
                    pst = 
                    con.prepareStatement("INSERT INTO PARAMETER "
                            + "( PARAMETER )"
                            + "values(?);");
                    pst.setString(1, TextFieldPARAMETER.getText());
                    pst.executeUpdate();
                    System.out.println("added PARAMETER : " + TextFieldPARAMETER.getText());
                }
//SUPPLIERS                
pst = con.prepareStatement("SELECT supplier_id FROM suppliers WHERE supplier_id = ?;");
                pst.setString(1, TextFieldSUPPLIER.getText());
                
                rs = pst.executeQuery();
                
                if(rs.next()){//SUPPLIER_ID DNE, add to bearings then INSERT details
                    System.out.println(TextFieldSUPPLIER.getText() + " DOES EXIST" );
                }
                else{
                    System.out.println(TextFieldSUPPLIER.getText() + " DOES NOT EXIST, ADDING TO SUPPLIERS" );
                    pst = 
                    con.prepareStatement("INSERT INTO suppliers "
                            + "( supplier_id )"
                            + "values(?);");
                    pst.setString(1, TextFieldSUPPLIER.getText());
                    pst.executeUpdate();
                    System.out.println("added SUPPLIER : " + TextFieldSUPPLIER.getText());
                }
//BEARINGS                
pst = con.prepareStatement("SELECT brg_name FROM bearings WHERE brg_name = ? "
        + "AND PARAMETER = ? AND supplier_id = ?;");
                pst.setString(1, TextFieldBRG.getText());
                pst.setString(2, TextFieldPARAMETER.getText());
                pst.setString(3, TextFieldSUPPLIER.getText());
                
                rs = pst.executeQuery();
                
                if(rs.next()){//SUPPLIER_ID DNE, add to bearings then INSERT details
                    System.out.println(TextFieldBRG.getText()+" DOES EXIST" );
                }
                else{
                    System.out.println(TextFieldBRG.getText()+ " DOES NOT EXIST, ADDING TO BEARINGS" );
                    pst = 
                    con.prepareStatement("INSERT INTO bearings "
                            + "( brg_name, PARAMETER, supplier_id )"
                            + "values(?,?,?);");
                    pst.setString(1, TextFieldBRG.getText());
                    pst.setString(2, TextFieldPARAMETER.getText());
                    pst.setString(3, TextFieldSUPPLIER.getText());
                    pst.executeUpdate();
                }
                
//ORDER DETAILS: ADD BRG NUMBER ID TO DETAILS
//                pst = 
//                    con.prepareStatement("INSERT INTO order_details "
//                            + "( purchase_order, brg_id, quantity, landed_cost, invoice_price, confirmed )"
//                            + "values(?,?,?,?,?,?);");
                pst = con.prepareStatement("SELECT brg_id FROM bearings "
                        + "WHERE brg_name = ?  AND "
                        + "PARAMETER = ? AND "
                        + "supplier_id = ?;");
                            
                pst.setString(1, TextFieldBRG.getText());
                pst.setString(2, TextFieldPARAMETER.getText());
                pst.setString(3, TextFieldSUPPLIER.getText());
                
                rs = pst.executeQuery();
                
                if(rs.next()){//SUPPLIER_ID DNE, add to bearings then INSERT details
                    System.out.println(TextFieldBRG.getText()+" DOES EXIST" + " ,ID = "  +rs.getString("brg_id"));
                    
                    pst = 
                    con.prepareStatement("INSERT INTO order_details "
                            + "( purchase_order, brg_id, quantity, landed_cost, invoice_price, confirmed )"
                            + "values(?,?,?,?,?,?);");
                    pst.setString(1, TextFieldPO.getText());
                    pst.setInt(2, Integer.parseInt(rs.getString("brg_id")));
                    pst.setString(3, TextFieldQTY.getText());
                    pst.setString(4, TextFieldLANDING.getText());
                    pst.setString(5, TextFieldINVOICE.getText());
                        if(ComboBoxCONFIRMED.getValue().equals("YES"))
                        pst.setString(6, "TRUE");
                    else
                        pst.setString(6, "FALSE");

                    pst.executeUpdate(); 
                
                }
                else{
                    System.out.println(TextFieldBRG.getText()+ " DOES NOT EXIST" );
                }
                
                
                

                Dialog<String> dialog = new Dialog<String>();{
                        dialog.setTitle("Overview DIALOG");
                        ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
                        dialog.getDialogPane().getButtonTypes().add(type);
                        dialog.setContentText("SUCCESSFULY ADDED: " + TextFieldPO.getText());
                        dialog.showAndWait();
                    }
                System.out.println("successfuly added!");

                clearAllFields();
            }


            } catch (SQLException ex) { 
            Logger.getLogger(EditPOView_NOANCHORController.class.getName()).log(Level.SEVERE, null, ex);
        }
//        clearAllFields();
    }
    //##########################################################################
    //LABEL AND ITEMLIST
    //##########################################################################
    @FXML
    private void handleDragOver(DragEvent event){
        if(event.getDragboard().hasFiles())
        event.acceptTransferModes(TransferMode.COPY);
    }
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
    //##########################################################################
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
}
