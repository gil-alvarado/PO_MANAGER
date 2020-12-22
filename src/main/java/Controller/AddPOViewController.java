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
    private Label dragLABEL;
    //--------------------------------------------------------------------------
    @FXML
    private Button BUTTONaddToDatabase;
    //--------------------------------------------------------------------------
    @FXML
    private TextArea TextAreaRWCOMMENTS, TextAreaFUNOTES;
    @FXML
    private ListView<String> ListViewATTACHMENTS;
    
    private List<File> attfiles;
    private List<String> removedFiles;
    private int listViewCount;
    private Attachment att[];
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        removedFiles = new ArrayList<>();
        attfiles = new ArrayList<>();
        
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
        //------------------------------------------------------------------------------        
//        for(int i = 0; i < DataEntryPanes.getColumnCount(); i++){

            GridPane p = (GridPane)DataEntryPanes.getChildren().get(0);

                for(Node node: p.getChildren()){
                    if(node instanceof TextField){ // if it's a TextField
                        if(node instanceof TextField){
                            ((TextField)node).textProperty().addListener((obs, old, newV)->{ // add a change listener to every TextField

                              if(!newV.trim().isEmpty()&& isTextFieldAllFilled(paneTextFieldsONE)) 
//                                  if(isDatePickerFilled(paneTextFieldsONE))
                                    BUTTONaddToDatabase.setDisable(false); // then make the button active again
                              else{
                                  BUTTONaddToDatabase.setDisable(true); // or else, make it disable until it achieves the required condition 
                              }
                          });  
                        }
//                        else{
//                            ((DatePicker)node).valueProperty().addListener((obs, old, newV)->{ // add a change listener to every TextField
//
//                              if(newV != null && isDatePickerFilled(paneTextFieldsONE)){ 
//                                  if(isTextFieldAllFilled(paneTextFieldsONE))
//                                    BUTTONaddToDatabase.setDisable(false); // then make the button active again
//                                  else{
//                                      System.out.println("fill out fields");
//                                      BUTTONaddToDatabase.setDisable(true);
//                                  }
//                              }
//                              else{
//                                  BUTTONaddToDatabase.setDisable(true); // or else, make it disable until it achieves the required condition 
//                              }
//                          });  
//                        }
                        
                    }//end instanceof TextField/DatePicker
                }//end forloop
//        }

        
    }    
    //##########################################################################
    // 1) check if PO exist before continuing. Cancel event/process if PO already exists
    // 2) check if PARAMETER is in table, if not, add to parent table
    // 3) check if supplier_id is in table, if not, add to table 
    // 4) check if new brg_id is in table, if not, add to parent table
    //##########################################################################
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
                
                pst = 
                    con.prepareStatement("INSERT INTO "
                            + "purchase_orders(purchase_order, original_ship_date, current_ship_date,eta_bms,"
                            + "fu_date)  "
                            + "values(?,?,?,?,?);");
           
            if(isTextFieldAllFilled(paneTextFieldsONE) 
                    && isDatePickerFilled(paneTextFieldsONE)){
               //Format(FUdate,'Short Date')
                System.out.println("ADDING NEW PO TO DATABASE");
                pst.setString(1, TextFieldPO.getText());
                pst.setDate(2, java.sql.Date.valueOf(DatePickerORGSHIP.getValue()));
                pst.setDate(3, java.sql.Date.valueOf(DatePickerCURSHIP.getValue()));
                pst.setDate(4, java.sql.Date.valueOf(DatePickerETABMS.getValue()));
                pst.setDate(5, java.sql.Date.valueOf(DatePickerFUDATE.getValue()));
                
                
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
                    if(pst.executeUpdate() == 1){//now insert attachments
                        //attfiles contains all files that was added to listview
                        //user can only remove "file" from listview
                        //read attfiles and compare to files listed on listview
                        //files listed on listview will get added to db
                        List<File> tempFileList = new ArrayList<>(attfiles);
                            
                            for(String rf : removedFiles){
                                for(int i =0 ; i < attfiles.size(); i++){
                                    if(attfiles.get(i).getName().equals(rf)){
                                        System.out.println("USER REMOVED :" + attfiles.get(i).getName() + " REMOVING FROM FIELD");
                                        tempFileList.remove(i);
                                    }
                                }
                            }
                            
                            attfiles = new ArrayList<>(tempFileList);
                            att = new Attachment[attfiles.size()];
                            
                            for(int i = 0; i < attfiles.size(); i++){
                                //DIDNT REMOVE items that were removed from listviews
                                String path = attfiles.get(i).getAbsolutePath();
                                byte[] attachmentData = java.nio.file.Files.readAllBytes(Paths.get(path));
                                att[i] = new Attachment(path,attfiles.get(i).getName(),null,attachmentData,null,null);
                            } 
                            
                            pst = con.prepareStatement("UPDATE order_details SET attachments = ? WHERE purchase_order = ?;");

                            pst.setObject(1, att);
                            pst.setString(2, TextFieldPO.getText());//new/same PO parameter
                            
                            if(pst.executeUpdate() == 1){
                                System.out.println("EXECUTED UPDATE ATTACHMENTS");
                                ButtonType OK = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                                Alert alert = new Alert(Alert.AlertType.NONE);
                                {
                                    alert.setTitle("update");
                                    alert.getButtonTypes().clear();
                                    alert.getDialogPane().getButtonTypes().addAll( OK );
                                    alert.setContentText("SUCCESSFULLY ADDED ATTACHMENTS");//now insert into po_notes
                                    alert.showAndWait();
                                }
                                
                                FileHelper.createDirectory(TextFieldPO.getText());
                                FileHelper.creatFile(TextFieldPO.getText());
                                
                                if(FileHelper.wirteToFile
                                    (TextFieldPO.getText(), TextAreaRWCOMMENTS.getText(), TextAreaFUNOTES.getText()) )
                                {
                                    System.out.println("Successfuly created and wrote to file for RWCOMMENTS");
                                    System.out.println("NOW add file to attachments");
                                }else{
                                    System.out.println("failed to write to file");
                                }
                                
                                pst = con.prepareStatement("INSERT INTO "
                                        + "po_notes( purchase_order, rw_comment, fu_note ) "
                                        + "VALUES (?,?,?)"    );
                                pst.setString(1, TextFieldPO.getText());
                                
                                File file = FileHelper.getRWFile(TextFieldPO.getText());
                                byte[] attachmentData = java.nio.file.Files.readAllBytes(Paths.get(file.getAbsolutePath()));
                                Attachment attachment = 
                                        new Attachment(file.getAbsolutePath(), file.getName(),null,attachmentData,null,null);
                                pst.setObject(2, attachment);
                                
                                
                                
                            }
                            else{
                                System.out.println("error ):");
                            }                                     
                    }
                
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
        } catch (IOException ex) {
            Logger.getLogger(AddPOViewController.class.getName()).log(Level.SEVERE, null, ex);
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
        
                Dragboard db = event.getDragboard();
                boolean success = false;
                
                if (db.hasFiles()) {
                    
                    success = true;
                    String filePath;
                    
                    if(attfiles.isEmpty() || ListViewATTACHMENTS.getItems().isEmpty()){
                        for (File file : db.getFiles()) {
                            ListViewATTACHMENTS.getItems().add(file.getName());
                            attfiles.add(file);
                        }
                    }
                    else{
                        for (File draggedFile : db.getFiles()) {

                            filePath = draggedFile.getAbsolutePath();

//                                for(int i = 0; i < attfiles.size() ;i++){
                                for(File attFile : attfiles){
                                    if( attFile.getName().equals(draggedFile.getName())  
//                                            || attfiles.contains(file) 
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
                                        attfiles.add(draggedFile);
                                        listViewCount++;
                                    }
                                }

                            }
                    }
                }
                event.setDropCompleted(success);
                event.consume();
                
                System.out.println("-----------------------------------------");
                System.out.println("PRINTING ATTFILES");
                System.out.println("-----------------------------------------");
                for(File f : attfiles){
                    System.out.println("File Name: " + f.getName() + ", FILE PATH; " + f.getAbsolutePath());
                }
            
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
        
        ListViewATTACHMENTS.getItems().clear();
        attfiles.clear();
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
            System.out.println("clicked on " + ListViewATTACHMENTS.getSelectionModel().getSelectedItem());
            System.out.println("FULLPATH: " + attfiles.get(ListViewATTACHMENTS.getSelectionModel().getSelectedIndex()));
    //        ListViewATTACHMENTS.getItems().remove(
    //                ListViewATTACHMENTS.getSelectionModel().getSelectedIndex());
            if(!Desktop.isDesktopSupported())//check if Desktop is supported by Platform or not  
            {  
                System.out.println("not supported");  
                return;  
            } 
            Desktop desktop = Desktop.getDesktop();  
            if(event.getClickCount() ==2){        //checks file exists or not  
                try {
                    desktop.open(attfiles.get(ListViewATTACHMENTS.getSelectionModel().getSelectedIndex()));              //opens the specified file  
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
        
        for(File d: attfiles){
            ListViewATTACHMENTS.getItems().add(d.getName());
            listViewCount++;
        }
    }
}