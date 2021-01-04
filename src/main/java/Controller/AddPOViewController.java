/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.ConnectionUtil;
import Model.FileHelper;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
import javafx.scene.control.TextFormatter;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javax.swing.text.NumberFormatter;
import net.ucanaccess.complex.Attachment;
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
    private Button BUTTONaddToDatabase;
    //--------------------------------------------------------------------------
    @FXML
    private TextArea TextAreaRWCOMMENTS, TextAreaFUNOTES;
    @FXML
    private ListView<String> ListViewATTACHMENTS;
    
    //##########################################################################
    //      ATTACHMENTS
    //##########################################################################
    
    private LinkedHashMap<String,File> map_files;
    
    
    private List<String> removedFiles;
    private int listViewCount;
    private Attachment att[];
    
    private boolean textFieldsAreFilled = false;
    private boolean datePickersAreFilled = false;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        map_files = new LinkedHashMap<>();
        
        removedFiles = new ArrayList<>();
        
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
        TextFieldINVOICE.setTextFormatter(new TextFormatter<>((change) -> {
            NumberFormatter format;
            
            String text = change.getControlNewText();
            if (text.matches("\\d*\\.?\\d{0,3}")) {
                return change;
            } else {
                return null;
            }
        }));
        //----------------------------------------------------------------------
        TextFieldLANDING.setTextFormatter(new TextFormatter<>((change) -> {
            NumberFormatter format;
            String text = change.getControlNewText();
            if (text.matches("\\d*\\.?\\d{0,2}")) {
                return change;
            } else {
                return null;
            }
        }));
        TextFieldQTY.setTextFormatter(new TextFormatter<>((change) -> {
            NumberFormatter format;
            String text = change.getControlNewText();
            if (text.matches("\\d*\\.?\\d{0,2}")) {
                return change;
            } else {
                return null;
            }
        }));
        //----------------------------------------------------------------------
        
        BUTTONaddToDatabase.disableProperty().bind( 
                TextFieldPO.textProperty().isEmpty()
                .or(TextFieldSUPPLIER.textProperty().isEmpty()
                .or(TextFieldINVOICE.textProperty().isEmpty())
                .or(TextFieldBRG.textProperty().isEmpty())
                .or(TextFieldPARAMETER.textProperty().isEmpty()
                .or(TextFieldQTY.textProperty().isEmpty()
                .or(TextFieldLANDING.textProperty().isEmpty())
                .or(DatePickerORGSHIP.valueProperty().isNull())
                .or(DatePickerETABMS.valueProperty().isNull())
                .or(DatePickerCURSHIP.valueProperty().isNull())))));
     
    }    
    //##########################################################################
    // 1) check if PO exist before continuing. Cancel event/process if PO already exists
    // 2) check if PARAMETER is in table, if not, add to parent table
    // 3) check if supplier_id is in table, if not, add to table 
    // 4) check if new brg_id is in table, if not, add to parent table
    //##########################################################################
    
    //including FU date
    private final String 
            insert_purchase_orders_dates 
            = "INSERT INTO "
            + "purchase_orders(purchase_order, original_ship_date, current_ship_date, eta_bms, fu_date) " 
            + "values(?,?,?,?,?);";
    private final String insert_purchase_orders_nofu
            = "INSERT INTO "
            + "purchase_orders(purchase_order, original_ship_date, current_ship_date, eta_bms) " 
            + "values(?,?,?,?);";
    
    @FXML
    private void addToDatabase(ActionEvent event) {
        
        try{
            Connection con = ConnectionUtil.conDB();         
//------------------------------------------------------------------------------
//PURCHASE_ORDERS
//------------------------------------------------------------------------------
PreparedStatement pst = null;
        
                pst = con.prepareStatement("SELECT purchase_order FROM purchase_orders WHERE purchase_order = ?;");
                pst.setString(1, TextFieldPO.getText());
                
                ResultSet rs = pst.executeQuery();
        
                System.out.println("CHECKING IF ENTERED PO EXISTS. IF EXISTS, WARN USER AND END PROCESS");
        
                if(rs.next()){
                    System.out.println("PO ALREADY EXIST! ALERT USER, END PROCESS");
                    ButtonType NO = new ButtonType("Ok", ButtonBar.ButtonData.CANCEL_CLOSE);                     
                    Alert alert = new Alert(Alert.AlertType.NONE);
                    {
                        alert.setTitle("WARNING");
                        alert.getButtonTypes().clear();
                        alert.getDialogPane().getButtonTypes().addAll( NO );
                        alert.setContentText(TextFieldPO.getText() + " ALREADY EXISTS!");
                        alert.showAndWait();
                    }  
                    return;
                }
                //##############################################################
                //      Date Validation
                //##############################################################
                if(DatePickerFUDATE.getValue() != null){
                    pst = 
                        con.prepareStatement(insert_purchase_orders_dates);
           
                        System.out.println("ADDING NEW PO TO DATABASE WITH FUDATE");
                        pst.setString(1, TextFieldPO.getText());
                        pst.setDate(2, java.sql.Date.valueOf(DatePickerORGSHIP.getValue()));
                        pst.setDate(3, java.sql.Date.valueOf(DatePickerCURSHIP.getValue()));
                        pst.setDate(4, java.sql.Date.valueOf(DatePickerETABMS.getValue()));
                        pst.setDate(5, java.sql.Date.valueOf(DatePickerFUDATE.getValue()));
                }else if(DatePickerFUDATE.getValue() == null){
                    pst = 
                        con.prepareStatement(insert_purchase_orders_nofu);
                    System.out.println("ADDING NEW PO TO DATABASE WITHOUT FUDATE");
                        pst.setString(1, TextFieldPO.getText());
                        pst.setDate(2, java.sql.Date.valueOf(DatePickerORGSHIP.getValue()));
                        pst.setDate(3, java.sql.Date.valueOf(DatePickerCURSHIP.getValue()));
                        pst.setDate(4, java.sql.Date.valueOf(DatePickerETABMS.getValue()));
                }
                
                
                if(pst.executeUpdate() == 1)
                    System.out.println("SUCCESSFULLY ADDED TO PURCHASE_ORDER TABLE");
                    
//------------------------------------------------------------------------------
//ORDER_DETAILS
//------------------------------------------------------------------------------  

//PARAMETER 
pst = con.prepareStatement("SELECT PARAMETER\n" +
"FROM PARAMETER\n" +
"WHERE PARAMETER = ?;");
                pst.setString(1,TextFieldPARAMETER.getText());  
      
                rs = pst.executeQuery();
                
                System.out.println("CHECKING IF PARAMETER EXISTS");
                
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
                pst = con.prepareStatement("SELECT brg_id FROM bearings "
                        + "WHERE brg_name = ?  AND "
                        + "PARAMETER = ? AND "
                        + "supplier_id = ?;");
                            
                pst.setString(1, TextFieldBRG.getText());
                pst.setString(2, TextFieldPARAMETER.getText());
                pst.setString(3, TextFieldSUPPLIER.getText());
                
                rs = pst.executeQuery();
                
                if(rs.next()){//SUPPLIER_ID DNE, add to bearings then INSERT details
                    System.out.println(TextFieldBRG.getText()+" DOES EXIST" + " , ID = "  +rs.getString("brg_id"));
                    
                    //note that I did NOT inculde attachments, I'll UPDATE the table after succesffuly 
                    //insertings new data
                    
                    //update order details
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
//###########################################
//ATTACHMENTS
//###########################################
                    if(pst.executeUpdate() == 1){
                            
                            for(String rf : removedFiles){
                                    map_files.remove(rf);
                            }
                            
                            att = new Attachment[map_files.size()];
                            
                            Iterator<Map.Entry<String, File>> iterator = map_files.entrySet().iterator();
                            int i = 0;
                            while(iterator.hasNext() && i < map_files.size()){
                                Map.Entry<String, File> entry = iterator.next();
                                String path = entry.getValue().getAbsolutePath();
                                byte []attachmentData = java.nio.file.Files.readAllBytes(Paths.get(path));
                                att[i] = new Attachment(path,entry.getKey(),null, attachmentData,null,null);
                                i++;
                            }
                            
                            
                            pst = con.prepareStatement("UPDATE order_details SET attachments = ? WHERE purchase_order = ?;");

                            pst.setObject(1, att);
                            pst.setString(2, TextFieldPO.getText());//new/same PO parameter
                            
                            if(pst.executeUpdate() == 1){
//###############################################
//          NOTES/COMMENTS INSERT
//###############################################
                                System.out.println("EXECUTED UPDATE ATTACHMENTS");
                                
                                FileHelper.createDirectory(TextFieldPO.getText());
                                FileHelper.creatFile(TextFieldPO.getText());//
                                
                                //##############################################
                                //          VALIDATE EMPTY ENTRY
                                //##############################################
                                if(FileHelper.wirteToFile
                                    (TextFieldPO.getText(), LoginViewController.current_user, TextAreaRWCOMMENTS.getText().replaceAll("[\\t\\n\\r]+"," "), 
                                            TextAreaFUNOTES.getText().replaceAll("[\\t\\n\\r]+"," ")) )
                                {
                                    System.out.println("Successfuly created and wrote to file for RWCOMMENTS");
                                    System.out.println("NOW add file to attachments");
                                }else{
                                    System.out.println("failed to write to file");
                                }                                

                                System.out.println("ATTEMPTING TO INSERT DATA");
//                                pst.execute();
                                if(ConnectionUtil.updateNotesTable(TextFieldPO.getText()) == 1){
//                                
                                    Dialog<String> dialog = new Dialog<String>();{
                                    dialog.setTitle("Overview DIALOG");
                                    ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
                                    dialog.getDialogPane().getButtonTypes().add(type);
                                    dialog.setContentText("SUCCESSFULY ADDED: " + TextFieldPO.getText());
                                    dialog.showAndWait();
                    }
                                }
                                else{
                                    ButtonType OK = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                                    Alert alert = new Alert(Alert.AlertType.NONE);
                                    {
                                        alert.setTitle("update");
                                        alert.getButtonTypes().clear();
                                        alert.getDialogPane().getButtonTypes().addAll( OK );
                                        alert.setContentText("UNSUCCESSFULLY ADDED ATTACHMENTS");//now insert into po_notes
                                        alert.showAndWait();
                                    }
                                }
                                
                            }
                            else{
                                System.out.println("error ):");
                            }                                     
                    }
                
                }
                else{
                    System.out.println(TextFieldBRG.getText()+ " DOES NOT EXIST" );
                }
                System.out.println("successfuly added!");

                clearAllFields();
            


            } catch (SQLException ex) { 
            Logger.getLogger(EditPOView_NOANCHORController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AddPOViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
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
        
                Dragboard db = event.getDragboard();
                boolean success = false;
                
                if (db.hasFiles()) {
                    
                    success = true;
                    String filePath;
                    
                    if(map_files.isEmpty() || ListViewATTACHMENTS.getItems().isEmpty()){
                        for (File file_cursor : db.getFiles()) {
                            ListViewATTACHMENTS.getItems().add(file_cursor.getName());
                            map_files.put(file_cursor.getName(), file_cursor);
                        }
                    }
                    else{
                        for (File draggedFile : db.getFiles()) {

                            filePath = draggedFile.getAbsolutePath();

                                for(File attFile : map_files.values()){
                                    if( attFile.getName().equals(draggedFile.getName())  
                                            || attFile.equals(draggedFile)
                                            || attFile.getAbsolutePath().equals(filePath)
                                            || ListViewATTACHMENTS.getItems().contains(draggedFile.getName())){
                                        System.out.println("FILE ALREADY ADDED: " + draggedFile.getAbsolutePath());
                                        return;
                                    }                            
                                    else{

//                                        System.out.println("-----------------------------");
//                                        System.out.println("ADDING " + draggedFile.getName() + " to attfiles");
//                                        System.out.println("-----------------------------");
                                        ListViewATTACHMENTS.getItems().add(draggedFile.getName());
                                        map_files.put(draggedFile.getName(), draggedFile);
                                        listViewCount++;
                                    }
                                }

                            }
                    }
                }
                event.setDropCompleted(success);
                event.consume();
            
    }
    //##########################################################################
    
        private boolean isTextFieldAllFilled(){

        return !( TextFieldPO.getText().isEmpty() || TextFieldSUPPLIER.getText().isEmpty() 
                || TextFieldINVOICE.getText().isEmpty() || TextFieldBRG.getText().isEmpty()
                || TextFieldPARAMETER.getText().isEmpty() || TextFieldQTY.getText().isEmpty()
                || TextFieldLANDING.getText().isEmpty() );
    }
    //--------------------------------------------------------------------------
//    @FXML
//    private DatePicker DatePickerORGSHIP, DatePickerETABMS, DatePickerFUDATE, DatePickerCURSHIP;
    private boolean isDatePickerFilled(){
        
        return  !( DatePickerORGSHIP.getEditor().getText().isEmpty()
                || DatePickerETABMS.getEditor().getText().isEmpty()
                || DatePickerCURSHIP.getEditor().getText().isEmpty() );
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
        TextAreaRWCOMMENTS.clear();
        TextAreaFUNOTES.clear();
        ListViewATTACHMENTS.getItems().clear();
        map_files.clear();
        att = null; 
        removedFiles.clear();
        listViewCount = 0;
    }
    
//##############################################################################
    
    //LISTVIEW ACTION
    @FXML
    private void handleMouseClick(MouseEvent event) {
        
        if(listViewCount>0 && !ListViewATTACHMENTS.getItems().isEmpty() 
                && ListViewATTACHMENTS.getSelectionModel().getSelectedIndex() >=0){
           
            if(!Desktop.isDesktopSupported())//check if Desktop is supported by Platform or not  
            {  
                System.out.println("not supported");  
                return;  
            } 
            Desktop desktop = Desktop.getDesktop();  
            if(event.getClickCount() ==2){        //checks file exists or not  
                try {
                    System.out.println("ATTEMPTING TO OPEN FILE");
                    File tempFile = map_files.get(ListViewATTACHMENTS.getItems().get(ListViewATTACHMENTS.getSelectionModel().getSelectedIndex()));
                    desktop.open(tempFile);
                } catch (IOException ex) {
                    Logger.getLogger(EditPOView_NOANCHORController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
    }
//##############################################################################
    @FXML
    private void removeListItem(ActionEvent event) {
        if(listViewCount>0 && !ListViewATTACHMENTS.getItems().isEmpty() 
                && ListViewATTACHMENTS.getSelectionModel().getSelectedIndex() >=0){
            removedFiles.add((String) ListViewATTACHMENTS.getSelectionModel().getSelectedItem());
            ListViewATTACHMENTS.getItems().remove(ListViewATTACHMENTS.getSelectionModel().getSelectedIndex());
            listViewCount--;
        }
    }

    @FXML
    private void reloadListView(ActionEvent event) {
        ListViewATTACHMENTS.getItems().clear();
        removedFiles.clear();

        for (Iterator<Map.Entry<String, File>> it = map_files.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, File> cursor = it.next();
            ListViewATTACHMENTS.getItems().add(cursor.getKey());
            listViewCount++;
        }
    }
}
