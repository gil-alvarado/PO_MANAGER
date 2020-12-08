/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.ConnectionUtil;
import Model.ModelManageDBTable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;

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
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;

import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

import net.ucanaccess.complex.Attachment;

//import net.ucanaccess.jdbc.UcanaccessPreparedStatement;
//
//import net.ucanaccess.jdbc.JackcessOpenerInterface;
//import net.ucanaccess.complex.ComplexBase;



/**
 * FXML Controller class
 *  retrieve data from ModelManageDBView
 * @author Gilbert Alvarado
 */
public class EditPOView_NOANCHORController implements Initializable {

    @FXML
    private GridPane paneTextFieldsONE, DataEntryPanes;

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
    
    
    private String POparameter,BRGparameter, CONFIRMEDparameter;
    private int BRGIDparameter;
    @FXML
    private Label currentPOLabel;
    
    private ManageDBViewController controller;
    private MainLayoutTesting_WITHANCHORController instance;
    
    private Attachment att[];
    
    List <File> attfiles;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        System.out.println("hello from the edit po controller!");
        
//        BUTTONaddToDatabase.setDisable(true);
        BUTTONUpdateDatabase.setDisable(true);
        BUTTONDeleteDatabase.setDisable(true);
        
        ComboBoxCONFIRMED.getItems().addAll("YES", "NO");
        ComboBoxCONFIRMED.setEditable(false);
        ComboBoxCONFIRMED.getSelectionModel().select("YES");
        
        //----------------------------------------------------------------------
        TextFieldPO.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { 
                    if(TextFieldPO.getText().isEmpty()){
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
//            setAddButtonSettings();
                    if (!newValue) { 
                        if(!TextFieldLANDING.getText().matches("\\d+(\\.\\d+)?")){
                            //when it not matches the pattern (1.0 - 6.0)
                            //set the textField empty
                            TextFieldLANDING.setText("");
                        }
                    }
        });
        //----------------------------------------------------------------------
        // BUTTONUpdateDatabase, BUTTONDeleteDatabase;
            GridPane p = (GridPane)DataEntryPanes.getChildren().get(0);

                for(Node node: p.getChildren()){
                    if(node instanceof TextField || node instanceof DatePicker){ 
                        if(node instanceof TextField){
                            ((TextField)node).textProperty().addListener((obs, old, newV)->{ 
                                
                              if(!newV.trim().isEmpty()&& isTextFieldAllFilled(paneTextFieldsONE)){ 
                                  if(isDatePickerFilled(paneTextFieldsONE)){
//                                    BUTTONaddToDatabase.setDisable(false);
                                    BUTTONUpdateDatabase.setDisable(false);
                                    BUTTONDeleteDatabase.setDisable(false);
                                  }
                              }
                              else{
//                                  BUTTONaddToDatabase.setDisable(true); 
                                  BUTTONUpdateDatabase.setDisable(true);
                                  BUTTONDeleteDatabase.setDisable(true);
                              }
                          });  
                        }
                        else{
                            ((DatePicker)node).valueProperty().addListener((obs, old, newV)->{

                              if(newV != null && isDatePickerFilled(paneTextFieldsONE)){ 
                                  if(isTextFieldAllFilled(paneTextFieldsONE)){
//                                    BUTTONaddToDatabase.setDisable(false);
                                    BUTTONUpdateDatabase.setDisable(false);
                                    BUTTONDeleteDatabase.setDisable(false);
                                  }
                                  else{
//                                      BUTTONaddToDatabase.setDisable(true);
                                      BUTTONUpdateDatabase.setDisable(true);
                                      BUTTONDeleteDatabase.setDisable(true);
                                  }
                              }
                              else{
//                                  BUTTONaddToDatabase.setDisable(true); 
                                  BUTTONUpdateDatabase.setDisable(true);
                                  BUTTONDeleteDatabase.setDisable(true);
                              }
                          });  
                        }               
                    }//end instanceof TextField/DatePicker   
        }
    }    
    //#########################################################################
    /*@FXML
    private TextField TextFieldPO, TextFieldSUPPLIER, TextFieldINVOICE, TextFieldBRG
            ,TextFieldPARAMETER, TextFieldQTY, TextFieldLANDING;
    
    @FXML
    private ComboBox ComboBoxCONFIRMED;
    
    @FXML
    private DatePicker DatePickerORGSHIP, DatePickerCURSHIP, 
            DatePickerETABMS, DatePickerFUDATE;
    @FXML
    private Button BUTTONaddToDatabase, BUTTONUpdateDatabase, BUTTONDeleteDatabase;
    */
    public void setManageDBViewController(ManageDBViewController controller){
        this.controller = controller;
    }
    public void setMainLayoutInstance(MainLayoutTesting_WITHANCHORController instance){
        this.instance = instance;
    }
    
    //##########################################################################
    //execute STATEMENT to get data from DB
    //use po 
    public void setItems(ModelManageDBTable data){
        //----------------------------------------------------------------------
        
        clearAllFields();
        System.out.println("ATTEMPTING TO EXECUTE STATEMENT");
        POparameter = data.getPo(); 
        System.out.println("SET ITEMS, PO = " +data.getPo());
        BRGparameter = data.getBrg();//
        System.out.println("BRG = " + data.getBrg());
        CONFIRMEDparameter = data.getConfirmed();
        try{
            
            Connection con = ConnectionUtil.conDB();
            System.out.println("INSIDE TRY CLAUSE, BEFORE RESULT SET"); 

            PreparedStatement pst = con.prepareStatement("SELECT brg_id FROM order_details WHERE purchase_order = ?");
            pst.setString(1, POparameter);

            ResultSet rs = pst.executeQuery();
            rs.next();
            BRGIDparameter = rs.getInt("brg_id");
            System.out.println("BRG_ID ===== " + BRGIDparameter);
            pst = 
                    con.prepareStatement("SELECT "
                        + "purchase_orders.purchase_order, order_details.quantity, order_details.landed_cost, "
                        + " order_details.invoice_price, order_details.confirmed, "
                        + " FORMAT(purchase_orders.original_ship_date, 'Short Date'), " //6
                        + " FORMAT(purchase_orders.current_ship_date, 'Short Date'), " //7
                        + " FORMAT(purchase_orders.eta_bms, 'Short Date')  , " //8
                        + " FORMAT(purchase_orders.fu_date, 'Short Date') , " //9
                        + " bearings.brg_name, bearings.PARAMETER, bearings.supplier_id, order_details.attachments " 
                        + "FROM purchase_orders , order_details , bearings  " 
                        + "WHERE purchase_order = ? AND bearings.brg_id = order_details.brg_id  AND order_details.brg_id = ?;" );


            pst.setString(1, POparameter);
            pst.setInt(2, BRGIDparameter);

            System.out.println("AFTER RESULT SET");

            rs = pst.executeQuery();
        
            rs.next();
            
            
            
            //NOW POPULTAE LIST
            att = (Attachment[]    )rs.getObject(13);
            for(Attachment f : att){
                System.out.println("FILE NAME: " + f.getName());
                System.out.println("FILE TYPE: " + f.getType());
                ListViewATTACHMENTS.getItems().add(f.getName());
            }
            
            //Attachment -> File -> add to attfiles!
            
            
            System.out.println("EDIT: NUMBER ATTACHMENTS for : " + POparameter + " = "  + att.length);
            
            currentPOLabel.setText(data.getPo());
            currentPOLabel.setVisible(true);
            
            System.out.println("ATTEMPTING TO PRINT DATE");
//            System.out.println(rs.getString("original_ship_date"));
            
            TextFieldPO.setText(rs.getString("purchase_order"));
            TextFieldSUPPLIER.setText(rs.getString("supplier_id"));
            TextFieldINVOICE.setText(rs.getString("invoice_price"));
            TextFieldBRG.setText(rs.getString("brg_name"));
            TextFieldPARAMETER.setText(rs.getString("PARAMETER"));
            TextFieldQTY.setText(rs.getString("quantity"));
            TextFieldLANDING.setText(rs.getString("landed_cost"));
            //----------------------------------------------------------------------
            if(rs.getString("confirmed").equals("TRUE"))
                ComboBoxCONFIRMED.getSelectionModel().select("YES");
            else
                ComboBoxCONFIRMED.getSelectionModel().select("NO");
            //----------------------------------------------------------------------
            //DATES
            //-----------------------------------------------------------------------
            
            String []tok = rs.getString(6).split("/");
    //        System.out.println("org format date: " + data.getCur());
            DatePickerORGSHIP.setValue(LocalDate.of(Integer.parseInt(tok[2]),
                    Month.of(Integer.parseInt(tok[0])), Integer.parseInt(tok[1])));
            //----------------------------------------------------------------------
            tok=rs.getString(6).split("/");
            DatePickerCURSHIP.setValue(LocalDate.of(Integer.parseInt(tok[2]),
                     Month.of(Integer.parseInt(tok[0])), Integer.parseInt(tok[1])));
            //----------------------------------------------------------------------
            tok=rs.getString(8).split("/");
            DatePickerETABMS.setValue(LocalDate.of(Integer.parseInt(tok[2]),
                     Month.of(Integer.parseInt(tok[0])), Integer.parseInt(tok[1])));
            //----------------------------------------------------------------------
            tok=rs.getString(9).split("/");
            DatePickerFUDATE.setValue(LocalDate.of(Integer.parseInt(tok[2]),
                     Month.of(Integer.parseInt(tok[0])), Integer.parseInt(tok[1])));

            con.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(ManageDBViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //add actions for each button
//        BUTTONaddToDatabase.setDisable(true);
        BUTTONDeleteDatabase.setDisable(false);
        BUTTONUpdateDatabase.setDisable(false);        
    }
    //#########################################################################
    private final String UPDATEpurchase_orders = "UPDATE purchase_orders SET "
            + "purchase_order = ?, "
            + "original_ship_date = ?, "
            + "current_ship_date = ?, "
            + "eta_bms = ?, "
            + "fu_date = ? "
            + "WHERE "
            + "purchase_order = ?;";
    
    //error: foreign key no parent: check if new parameters/suppliers/bearings exist before updating
    //add if not exist
    //get brg_id FIRST then execute
    private final String UPDATEorder_details = "UPDATE order_details SET "
            + "order_details.brg_id = ?, "
            + "order_details.quantity = ?, "
            + "order_details.landed_cost = ?, "
            + "order_details.invoice_price = ?, "
            + "order_details.confirmed = ? "
            + "WHERE "
            + "order_details.purchase_order = ? AND "
            + " order_details.brg_id = ? AND "
            + " order_details.confirmed = ?;" ;
 
    @FXML
    private void updateDatabase(ActionEvent event){
   
         try{            
             try (Connection con = ConnectionUtil.conDB()) {
                 
                 //try select before execute
                 System.out.println("ATTEMPTING TO UPDATE PURCHASE ORDERS BEFOR EXECUTION");
                 PreparedStatement update = con.prepareStatement(UPDATEpurchase_orders);
                 
                 //purchase_orders
                 update.setString(1, TextFieldPO.getText());//SET
                 update.setDate(2, java.sql.Date.valueOf(DatePickerORGSHIP.getValue()));
                 update.setDate(3, java.sql.Date.valueOf(DatePickerCURSHIP.getValue()));
                 update.setDate(4, java.sql.Date.valueOf(DatePickerETABMS.getValue()));
                 update.setDate(5, java.sql.Date.valueOf(DatePickerFUDATE.getValue()));
                 update.setString(6, POparameter);
                 
                 update.executeUpdate();
                 System.out.println("ATTEMPTING TO UPDATE PURCHASE ORDERS  AFTER");
//##############################################################################
//3)check if new brg_id is in table, if not, add to parent table
//2)check if PARAMETER is in table, if not, add to parent table
//1)check if supplier_id is in table, if not, add to table
//DO NOT CASCADE RELATED FIELDS
//YOU CAN CHECK AND ADD TO TABLE IN ADDPO 

//PARAMETER 
update = con.prepareStatement("SELECT PARAMETER\n" +
" FROM PARAMETER \n" +
" WHERE PARAMETER = ?;");
                update.setString(1,TextFieldPARAMETER.getText());
      
                ResultSet rs = update.executeQuery();
                
                System.out.println("CHECKING PARAMETer IF EXISTS: " + TextFieldPARAMETER.getText());
                if(rs.next())
                    System.out.println(TextFieldPARAMETER.getText() + ": DOES EXIST");
                else{
                    System.out.println(TextFieldPARAMETER.getText() + " DOES NOT EXIST, ADDING TO PARAMETER" );
                    update = 
                    con.prepareStatement("INSERT INTO PARAMETER "
                            + "( PARAMETER )"
                            + "values(?);");
                    update.setString(1, TextFieldPARAMETER.getText());
                    update.executeUpdate();
                    System.out.println("added new PARAMETER : " + TextFieldPARAMETER.getText());
                }

//SUPPLIERS                
update = con.prepareStatement("SELECT supplier_id FROM suppliers WHERE supplier_id = ?;");
                update.setString(1, TextFieldSUPPLIER.getText());
                
                rs = update.executeQuery();
                System.out.println("CHECKING IF EXISTS: " + TextFieldSUPPLIER.getText());
                if(rs.next())
                    System.out.println(TextFieldSUPPLIER.getText() + " DOES EXIST" );
                else{
                    System.out.println(TextFieldSUPPLIER.getText() + " DOES NOT EXIST, ADDING TO SUPPLIERS" );
                    update = 
                    con.prepareStatement("INSERT INTO suppliers "
                            + "( supplier_id )"
                            + "values(?);");
                    update.setString(1, TextFieldSUPPLIER.getText());
                    update.executeUpdate();
                    System.out.println("added new SUPPLIER : " + TextFieldSUPPLIER.getText());                    
                }

//BEARINGS, checking if new brg form textfield exists
update = con.prepareStatement("SELECT brg_name FROM bearings WHERE brg_name = ? "
        + "AND PARAMETER = ? AND supplier_id = ?;");
                update.setString(1, TextFieldBRG.getText());
                update.setString(2, TextFieldPARAMETER.getText());
                update.setString(3, TextFieldSUPPLIER.getText());
                
                rs = update.executeQuery();
                
                System.out.println("CHECKING IF EXISTS: " + TextFieldBRG.getText());
                if(rs.next())
                    System.out.println(TextFieldBRG.getText()+" DOES EXIST" );
                else{
                    System.out.println(TextFieldBRG.getText()+ " DOES NOT EXIST, ADDING TO BEARINGS" );
                    update = 
                        con.prepareStatement("INSERT INTO bearings "
                                + "( brg_name, PARAMETER, supplier_id )"
                                + "values(?,?,?);");//
                    update.setString(1, TextFieldBRG.getText());
                    update.setString(2, TextFieldPARAMETER.getText());
                    update.setString(3, TextFieldSUPPLIER.getText());
                    update.executeUpdate();                    
                }

/*
    "UPDATE order_details 
                SET "
                    + "order_details.brg_id = ?, "
                    + "order_details.quantity = ?, "
                    + "order_details.landed_cost = ?, "
                    + "order_details.invoice_price = ?, "
                    + "order_details.confirmed = ? "
            + "WHERE "
                    + " order_details.purchase_order = ? AND "
                    + " order_details.brg_id = ? AND "
                    + " order_details.confirmed = ?;"             
                */
                
                //get brg_id, then update details now that new/existing brg_id is obtainable
                update = con.prepareStatement("SELECT brg_id FROM bearings "
                        + "WHERE brg_name = ?  AND "
                        + "PARAMETER = ? AND "
                        + "supplier_id = ?;");
                
                //brg_id from textfield should exist
                update.setString(1, TextFieldBRG.getText());
                update.setString(2, TextFieldPARAMETER.getText());
                update.setString(3, TextFieldSUPPLIER.getText());
                
                rs = update.executeQuery();
                
                if(rs.next()){
                    System.out.println("LINE 456: " +TextFieldBRG.getText()+" DOES EXIST" + " ,ID = "  +rs.getString("brg_id"));
                    
                    update = con.prepareStatement(UPDATEorder_details);

                    //get new brg_id from bearings, shoudl be added
                    update.setInt(1, Integer.parseInt(rs.getString("brg_id")));
                    update.setString(2, TextFieldQTY.getText());
                    update.setString(3, TextFieldLANDING.getText());
                    update.setString(4, TextFieldINVOICE.getText());

                    if(ComboBoxCONFIRMED.getValue().equals("YES"))
                       update.setString(5, "TRUE");
                    else
                       update.setString(5, "FALSE");

                    update.setString(6, TextFieldPO.getText());//should automaticall update
//                    update.setString(7, BRGparameter);//original value//ERROR, CHANGE TO BRG NAME OR ID FOR WHERE CLAUSE

                    update.setInt(7, BRGIDparameter);//id of original brg_id, not newly added
                    
                    update.setString(8, CONFIRMEDparameter);//original value
                    
                        if(update.executeUpdate() == 1){
                        //succesful execution, now update attachments
                        //use UCANACCESS update feature, wont work with standard SQL
//                        update = super.ucanaccess.prepareStatement("");

//PreparedStatement ps = super.ucanaccess.prepareStatement( "SELECT * FROM T1", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE, ResultSet.CLOSE_CURSORS_AT_COMMIT);
                                
//                            update = con.prepareStatement( "SELECT * FROM T1",
//                                        ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE,
//                                        ResultSet.CLOSE_CURSORS_AT_COMMIT);
                                
//UcanaccessPreparedStatement ups= con.prepareStatement( "SELECT * FROM T1",
//                                        ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE,
//                                        ResultSet.CLOSE_CURSORS_AT_COMMIT);


//select attachment field from db add/update attachment field
                            for (File file:attfiles) {
                                String filePath = file.getAbsolutePath();
                                System.out.println(filePath);
                                //add file to ListView
//                                ListViewATTACHMENTS.getItems().add(filePath);
                        
                    }
                            ButtonType OK = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                            Alert alert = new Alert(Alert.AlertType.NONE);
                            {
                                alert.setTitle("update");
                                alert.getButtonTypes().clear();
                                alert.getDialogPane().getButtonTypes().addAll( OK );
                                alert.setContentText("SUCCESSFULLY UPDATED");
                                alert.showAndWait();
                            }
                        }
                        else{
                            Dialog<String> dialog = new Dialog<>();{
                            dialog.setTitle("update");
                            ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
                            dialog.getDialogPane().getButtonTypes().add(type);
                            dialog.setContentText("UNSUCCESSFUL: parameter: " + POparameter + ", BRG_ID: " + rs.getString("brg_id"));
                            dialog.showAndWait();
                           }
                        }
                    }
                
                 con.close();

             }
                BUTTONUpdateDatabase.setDisable(true);
                BUTTONDeleteDatabase.setDisable(true);
                clearAllFields();
                
            } catch (SQLException ex) { 
            Logger.getLogger(EditPOView_NOANCHORController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    //#########################################################################
    @FXML
    private void deleteFromDatabase(ActionEvent event){
//        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
         try {
            Connection con = ConnectionUtil.conDB();
//            Statement stmt;
//            stmt = con.createStatement();
//
//            int result = 
//                    stmt.executeUpdate("DELETE FROM purchaseorders WHERE purchaseOrder = "
//                            + "'"+POparameter+"'" );
//            stmt.close();
            
            PreparedStatement pst  = con.prepareStatement("DELETE FROM purchase_orders WHERE purchase_order = ?;");

            pst.setString(1, TextFieldPO.getText());

            if(pst.executeUpdate() == 1){
                
                ButtonType OK = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                                 Alert alert = new Alert(Alert.AlertType.NONE);
                                {
                                    alert.setTitle("update");
                                    alert.getButtonTypes().clear();
                                    alert.getDialogPane().getButtonTypes().addAll( OK );
                                    alert.setContentText("SUCCESSFULLY DELETED BUT SHOULD PROMPT");
                                    alert.showAndWait();
                                }
                
                }else{
                
                System.err.println("ERROR: key vilation for : " + TextFieldPO.getText() );
                                
                ButtonType OK = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                                Alert alert = new Alert(Alert.AlertType.NONE);
                                {
                                    alert.setTitle("deletion");
                                    alert.getButtonTypes().clear();
                                    alert.getDialogPane().getButtonTypes().addAll( OK );
                                    alert.setContentText("NO RECORD FOUND FOR: " + TextFieldPO.getText());
                                    alert.showAndWait();
                                }
            }
            
            
            
            
            con.close();
            clearAllFields();
            BUTTONUpdateDatabase.setDisable(true);
            BUTTONDeleteDatabase.setDisable(true);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
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



                //att = Attachment
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasFiles()) {
                    success = true;
                    String filePath;
                    
                    attfiles = event.getDragboard().getFiles();
                    
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
//    @FXML
//    private TextField TextFieldPO, TextFieldSUPPLIER, TextFieldINVOICE, TextFieldBRG
//            ,TextFieldPARAMETER, TextFieldQTY, TextFieldLANDING;
    
//    @FXML
//    private ComboBox<?> ComboBoxCONFIRMED;
//    
//    @FXML
//    private DatePicker DatePickerORGSHIP, DatePickerCURSHIP, 
//            DatePickerETABMS, DatePickerFUDATE;
    
    public static boolean isTextFieldAllFilled(GridPane table){
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
    public static boolean isDatePickerFilled(GridPane table){
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
//                ((DatePicker)node).getEditor().clear();
                    ((DatePicker)node).setValue(null);
            }
        }
        
        ListViewATTACHMENTS.getItems().clear();
    }
    //--------------------------------------------------------------------------
}
