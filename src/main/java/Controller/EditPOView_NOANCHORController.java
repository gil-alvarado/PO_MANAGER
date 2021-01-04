/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.awt.Desktop;

import Model.ConnectionUtil;
import Model.FileHelper;
import Model.ModelManageDBTable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.GridPane;

import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.swing.text.NumberFormatter;

import net.ucanaccess.complex.Attachment;

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
    private Button BUTTONUpdateDatabase, BUTTONDeleteDatabase;
    
    //##########################################################################
    @FXML
    private TextArea TextAreaRWCOMMENTS, TextAreaFUNOTES;
    //similar to adding data from database. look at previous app version
    @FXML
    private ListView ListViewATTACHMENTS;
    
    
    
    private String POparameter,BRGparameter, CONFIRMEDparameter;
    private int BRGIDparameter;
    @FXML
    private Label currentPOLabel;
    
    private ManageDBViewController controller;
    private MainLayoutTesting_WITHANCHORController instance;
    
    private Attachment att[];//ucanaccess: read/create files and add to local dir
    List <String> removedFiles;// add for every removed file/item from listview
    
    private int listViewCount;
    
    private static final DecimalFormat LCformat = new DecimalFormat("#######00.00");
    private static final DecimalFormat IPformat = new DecimalFormat("#######00.000");
    
    //filename, file(path,name)
    private LinkedHashMap<String,File> map_files;
    @FXML
    private HBox HBoxButtons;
    @FXML
    private Button BUTTONrwComment, BUTTONFUnote;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        removedFiles = new ArrayList<>();
        map_files = new LinkedHashMap<>();
        ListViewATTACHMENTS.setEditable(true);
        ListViewATTACHMENTS.setCellFactory(TextFieldListCell.forListView());
        BUTTONrwComment.setDisable(true); BUTTONFUnote.setDisable(true);
        ListViewATTACHMENTS.setOnEditCommit(new EventHandler<ListView.EditEvent<String>>() {
            @Override
            public void handle(ListView.EditEvent<String> event) {
                System.out.println("OLD VALUE: "+ event.getSource().getItems().get(event.getIndex()));//print old value
                //set new value
                ListViewATTACHMENTS.getItems().set(event.getIndex(), event.getNewValue());
                System.out.println("NEW VALUE: " + event.getSource().getItems().get(event.getIndex()));
                        //attfiles.get(ListViewATTACHMENTS.getSelectionModel().getSelectedIndex()).getName());
            }
            

        });                
        
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
        
        TextFieldINVOICE.setTextFormatter(new TextFormatter<>((change) -> {
            NumberFormatter format;
            String text = change.getControlNewText();
            if (text.matches("\\d*\\.?\\d{0,3}")) {
                return change;
            } else {
                return null;
            }
        }));

//------------------------------------------------------------------------------
    
TextFieldLANDING.setTextFormatter(new TextFormatter<>((change) -> {
            NumberFormatter format;
            String text = change.getControlNewText();
            if (text.matches("\\d*\\.?\\d{0,2}")) {
                return change;
            } else {
                return null;
            }
        }));    
        //----------------------------------------------------------------------
        
        TextFieldQTY.setTextFormatter(new TextFormatter<>((change) -> {
            NumberFormatter format;
            String text = change.getControlNewText();
            if (text.matches("\\d*\\.?\\d{0,2}")) {
                return change;
            } else {
                return null;
            }
        }));

BUTTONUpdateDatabase.disableProperty().bind( 
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

if(LoginViewController.role_ID == 1){
    BUTTONDeleteDatabase.disableProperty().bind(TextFieldPO.textProperty().isEmpty());
}
else{
    BUTTONDeleteDatabase.setDisable(true);
}
    }    
    //#########################################################################

    public void setManageDBViewController(ManageDBViewController controller){
        this.controller = controller;
    }
    public void setMainLayoutInstance(MainLayoutTesting_WITHANCHORController instance){
        this.instance = instance;
    }
    
    //##########################################################################
    public void setItems(ModelManageDBTable data){
        //----------------------------------------------------------------------
        ResultSet rs_meta_data = ConnectionUtil.getAllData(data.getPo());//already executeds
        
        clearAllFields();
        System.out.println("ATTEMPTING TO EXECUTE STATEMENT");
        POparameter = data.getPo();//original PO
        System.out.println("SET ITEMS, PO = " +data.getPo());
        BRGparameter = data.getBrg();//original bearing number
        System.out.println("BRG = " + data.getBrg());
        CONFIRMEDparameter = data.getConfirmed();
        try{
            
            Connection con = ConnectionUtil.conDB();

            BRGIDparameter = rs_meta_data.getInt("brg_id");//rs.getInt("brg_id");

//CREATE NEW FILE FROM ATTACHMENT COLUMN

    //##########################################################################
            System.out.println("RS_META_DATA: GETTING ATTACHMENTS");
            att = (Attachment[]    )rs_meta_data.getObject("attachments");
            
            if(FileHelper.createPoAttachmentsDirectory(POparameter)){
                    System.out.println("DIRECTORY CREATED");
                    System.out.println("ADDING ATTACHMENTS TO DIRECTORY");
            }else{
                System.out.println("DIRECTORY ALREADY CREATED");
                System.out.println("ADDING ATTACHMENTS TO DIRECTORY");
            }
            for(Attachment file_cursor : att){
                
                //display existing fileNAMES on DB to listview
                ListViewATTACHMENTS.getItems().add(file_cursor.getName());
                
                //add existing files to attfiles, array of actual files
                    File tempFile = 
                            new File(ConnectionUtil.desktopLocation + "/BMStemp/" +POparameter + "/Attachments/"+ file_cursor.getName());
                    org.apache.commons.io.FileUtils.writeByteArrayToFile(
                            tempFile, file_cursor.getData());                    
                    //##########################################################
                    //          map_file implementation
                    //##########################################################
//                    System.out.println("FILE ATTACHMENT NAME: " + file_cursor.getName());
                    map_files.put(file_cursor.getName(), tempFile);
                    
                listViewCount++;
            }
    //##########################################################################        
            currentPOLabel.setText(POparameter);
            currentPOLabel.setVisible(true);
            
            TextFieldPO.setText(rs_meta_data.getString("purchase_order"));
            TextFieldSUPPLIER.setText(rs_meta_data.getString("supplier_id"));
            
            TextFieldBRG.setText(rs_meta_data.getString("brg_name"));
            TextFieldPARAMETER.setText(rs_meta_data.getString("PARAMETER"));
            TextFieldQTY.setText(rs_meta_data.getString("quantity"));
            
            TextFieldLANDING.setText(LCformat.format(Double.parseDouble(rs_meta_data.getString("landed_cost"))));
            TextFieldINVOICE.setText(IPformat.format(Double.parseDouble(rs_meta_data.getString("invoice_price"))));
            
            //----------------------------------------------------------------------
            if(rs_meta_data.getString("confirmed").equals("TRUE"))
                ComboBoxCONFIRMED.getSelectionModel().select("YES");
            else
                ComboBoxCONFIRMED.getSelectionModel().select("NO");
            //----------------------------------------------------------------------
            //DATES: validate empty input
            //-----------------------------------------------------------------------
            
            //ORIGINAL SHIP DATE
            String []tok = rs_meta_data.getString(2).split("/");
            DatePickerORGSHIP.setValue(LocalDate.of(Integer.parseInt(tok[2]),
                    Month.of(Integer.parseInt(tok[0])), Integer.parseInt(tok[1])));
            //----------------------------------------------------------------------
            //CURRENT SHIP DATE
            tok=rs_meta_data.getString(3).split("/");
            DatePickerCURSHIP.setValue(LocalDate.of(Integer.parseInt(tok[2]),
                     Month.of(Integer.parseInt(tok[0])), Integer.parseInt(tok[1])));
            //----------------------------------------------------------------------
            //ETA DATE
            tok=rs_meta_data.getString(4).split("/");
            DatePickerETABMS.setValue(LocalDate.of(Integer.parseInt(tok[2]),
                     Month.of(Integer.parseInt(tok[0])), Integer.parseInt(tok[1])));
            //----------------------------------------------------------------------
            //FOLLOW UP DATE
            String fu_date = rs_meta_data.getString(5);
            if(!fu_date.isEmpty()){
                tok=fu_date.split("/");
                DatePickerFUDATE.setValue(LocalDate.of(Integer.parseInt(tok[2]),
                         Month.of(Integer.parseInt(tok[0])), Integer.parseInt(tok[1])));
            }
            //##################################################################
            //  SET NOTES, MOST LIKELY CHANGE LAYOUT
            //##################################################################
            File tempNoteFile = FileHelper.getRWFile(POparameter);
            String notes_content = FileHelper.csvFileContent(tempNoteFile);//FileHelper.fileContent(tempNoteFile).toString();
            if(!notes_content.isEmpty()){
                TextAreaRWCOMMENTS.setText(notes_content);
            }
            tempNoteFile = FileHelper.getFUFile(POparameter);
            notes_content = FileHelper.csvFileContent(tempNoteFile);//fileContent(tempNoteFile).toString();
            if(!notes_content.isEmpty()){
                TextAreaFUNOTES.setText(notes_content);
            }
                    
            BUTTONrwComment.setDisable(false); BUTTONFUnote.setDisable(false);
            con.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(ManageDBViewController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) { 
            Logger.getLogger(EditPOView_NOANCHORController.class.getName()).log(Level.SEVERE, null, ex);
        }     
    }
//##############################################################################
    private final String UPDATEpurchase_orders = "UPDATE purchase_orders SET "
            + "purchase_order = ?, "
            + "original_ship_date = ?, "
            + "current_ship_date = ?, "
            + "eta_bms = ?, "
            + "fu_date = ? "
            + "WHERE "
            + "purchase_order = ?;";
    
    private final String UPDATEpurchase_orders_nofudate = "UPDATE purchase_orders SET "
            + "purchase_order = ?, "
            + "original_ship_date = ?, "
            + "current_ship_date = ?, "
            + "eta_bms = ?, "
            + "\n"
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
                 System.out.println("ATTEMPTING TO UPDATE PURCHASE_ORDERS TABLE BEFORE UPDATING DETAILS");
                 PreparedStatement update;
                 
                 if( DatePickerFUDATE.getValue() != null ){
                    update= con.prepareStatement(UPDATEpurchase_orders);

                    //UPDATING purchase_orders FIRST
                    update.setString(1, TextFieldPO.getText());//SET
                    update.setDate(2, java.sql.Date.valueOf(DatePickerORGSHIP.getValue()));
                    update.setDate(3, java.sql.Date.valueOf(DatePickerCURSHIP.getValue()));
                    update.setDate(4, java.sql.Date.valueOf(DatePickerETABMS.getValue()));
                    update.setDate(5, java.sql.Date.valueOf(DatePickerFUDATE.getValue()));
                    update.setString(6, POparameter);
                 }else{
                    update = con.prepareStatement(UPDATEpurchase_orders_nofudate);
                    update.setString(1, TextFieldPO.getText());//SET
                    update.setDate(2, java.sql.Date.valueOf(DatePickerORGSHIP.getValue()));
                    update.setDate(3, java.sql.Date.valueOf(DatePickerCURSHIP.getValue()));
                    update.setDate(4, java.sql.Date.valueOf(DatePickerETABMS.getValue()));
                    update.setString(5, POparameter);
                 }
                 
                 if(update.executeUpdate() == 1)
                     System.out.println("SUCCESFULLY UPDATED PURCHASE_ORDER TABLE");
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
                
                System.out.println("PARAMETER CHECK: CHECKING IF " + TextFieldPARAMETER.getText() + " EXISTS");
                if(rs.next())
                    System.out.println(TextFieldPARAMETER.getText() + ": DOES EXIST");
                else{
                    System.out.println(TextFieldPARAMETER.getText() + " DOES NOT EXIST, ADDING TO PARAMETER TABLE" );
                    update = 
                    con.prepareStatement("INSERT INTO PARAMETER "
                            + "( PARAMETER )"
                            + "values(?);");
                    update.setString(1, TextFieldPARAMETER.getText());
                    update.executeUpdate();
                    System.out.println("NEW PARAMETER ADDED: " + TextFieldPARAMETER.getText());
                }

//SUPPLIERS                
update = con.prepareStatement("SELECT supplier_id FROM suppliers WHERE supplier_id = ?;");
                update.setString(1, TextFieldSUPPLIER.getText());
                
                rs = update.executeQuery();
                System.out.println("SUPPLIER CHECK: CHECKING IF " + TextFieldSUPPLIER.getText() + " EXISTS");
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
                    System.out.println("NEW SUPPLIER ADDED: " + TextFieldSUPPLIER.getText());                    
                }

//BEARINGS
update = con.prepareStatement("SELECT brg_name FROM bearings WHERE brg_name = ? "
        + "AND PARAMETER = ? AND supplier_id = ?;");
                update.setString(1, TextFieldBRG.getText());
                update.setString(2, TextFieldPARAMETER.getText());
                update.setString(3, TextFieldSUPPLIER.getText());
                
                rs = update.executeQuery();
                
                System.out.println("BEARINGS CHECK: CHECKING IF " + TextFieldBRG.getText() + " EXISTS");
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

                
                System.out.println("SLECTING brg_id FROM BEARINGS");
                //get brg_id, then update order details now that new/existing brg_id is obtainable
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
//                    System.out.println("LINE 456: " +TextFieldBRG.getText()+" DOES EXIST" + " ,ID = "  +rs.getString("brg_id"));

                    update = con.prepareStatement(UPDATEorder_details);
                    
                    update.setInt(1, (rs.getInt("brg_id")));
                    update.setString(2, TextFieldQTY.getText());
                    update.setString(3, TextFieldLANDING.getText());
                    update.setString(4, TextFieldINVOICE.getText());

                    System.out.println("SETTING CONFIRMED VALUE");
                    if(ComboBoxCONFIRMED.getValue().equals("YES"))
                       update.setString(5, "TRUE");
                    else
                       update.setString(5, "FALSE");
                    System.out.println("CONFIRMEDD VALUE SET");

                    update.setString(6, TextFieldPO.getText());//should automatically update
//                    update.setString(7, BRGparameter);//original value//ERROR, CHANGE TO BRG NAME OR ID FOR WHERE CLAUSE

                    update.setInt(7, BRGIDparameter);//id of original brg_id, not newly added
                    System.out.println("CONFIRMED VALUE PARAMETER: " + CONFIRMEDparameter);
                    if(CONFIRMEDparameter.equals("YES"))
                        update.setString(8, "TRUE");
                    else
                        update.setString(8, "FALSE");
//###########################################
//ATTACHMENTS
//###########################################
                            
                        if(update.executeUpdate() == 1){
                            System.out.println("UPDATED DETAILS, NOW UPDATING ATTACHMENTS");
                            
                            for(String rf : removedFiles)
                                map_files.remove(rf);
                            
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
                            
                            update = con.prepareStatement("UPDATE order_details SET attachments = ? WHERE purchase_order = ?;");
//update = con.prepareStatement("INSERT INTO order_details(attachments) VALUES(?) WHERE purchase_order = ?;");

                            update.setObject(1, att);
                            update.setString(2, TextFieldPO.getText());//new/same PO parameter
                            if(update.executeUpdate() == 1){
                                System.out.println("EXECUTED UPDATE ATTACHMENTS");
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
                                System.out.println("error ):");
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

             } catch (IOException ex) {
                 Logger.getLogger(EditPOView_NOANCHORController.class.getName()).log(Level.SEVERE, null, ex);
             } 
             
            } catch (SQLException ex) { 
            Logger.getLogger(EditPOView_NOANCHORController.class.getName()).log(Level.SEVERE, null, ex);
        }
//        clearAllFields();
    }
//##############################################################################
    @FXML
    private void deleteFromDatabase(ActionEvent event){
        
         try {
            Connection con = ConnectionUtil.conDB();
            
            ResultSet rs = ConnectionUtil.userVerification(LoginViewController.current_user);
            
            ButtonType YES = new ButtonType("YES", ButtonBar.ButtonData.YES);
            ButtonType NO = new ButtonType("NO", ButtonBar.ButtonData.CANCEL_CLOSE);
            ButtonType OK = new ButtonType("OK", ButtonBar.ButtonData.CANCEL_CLOSE);
            Optional<ButtonType> result;

//##############################################################################
            //check if user exists and has permission
            if(rs != null){
//##############################################################################                
                if(rs.getInt(2) == 1){//user has permission
                    Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
                    {
                        confirmation.setTitle("DELETE DIALOG");
                        confirmation.getButtonTypes().clear();
                        confirmation.getDialogPane().getButtonTypes().addAll( YES, NO );
                        confirmation.setContentText("ARE YOU SURE YOU WANT TO DELETE " + POparameter + "?");
                    }
                    
                    result = confirmation.showAndWait();
                    
                    if(result.orElse(NO) == YES){//user confirms that they wish to delete
                        PreparedStatement pst  = con.prepareStatement("DELETE FROM purchase_orders WHERE purchase_order = ?;");
                        pst.setString(1, TextFieldPO.getText());

                        if(pst.executeUpdate() == 1){//user has permission
                            
                                Alert alert = new Alert(Alert.AlertType.NONE);
                               {
                                   alert.setTitle("update");
                                   alert.getButtonTypes().clear();
                                   alert.getDialogPane().getButtonTypes().addAll( OK );
                                   alert.setContentText("SUCCESSFULLY DELETED");
                                   clearAllFields();
                                   alert.showAndWait();
                               }

                                }else{

                                    System.err.println("ERROR: key vilation for : " + TextFieldPO.getText() );

                                    Alert alert = new Alert(Alert.AlertType.NONE);
                                    {
                                        alert.setTitle("deletion");
                                        alert.getButtonTypes().clear();
                                        alert.getDialogPane().getButtonTypes().addAll( OK );
                                        alert.setContentText("NO RECORD FOUND FOR: " + TextFieldPO.getText());
                                        alert.showAndWait();
                                    }
                            }
                    }
                }
//##############################################################################                
                else if (rs.getInt(2) == 2){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    {
                        alert.setTitle("NOTICE DIALOG");
                        alert.getButtonTypes().clear();
                        alert.getDialogPane().getButtonTypes().addAll( OK );
                        alert.setContentText("YOU DO NOT HAVE PERMISSION TO DELETE");
                        alert.showAndWait();
                    }
                }
                
            }
            else{
                System.out.println("no record found for user");
            }
            con.close();
        }
        catch(SQLException e) {
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
        
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasFiles()) {
                    
                    success = true;
                    String filePath;
                    
                    if(map_files.isEmpty() || ListViewATTACHMENTS.getItems().isEmpty()){
                        for (File file_cursor : db.getFiles()) {
                            ListViewATTACHMENTS.getItems().add(file_cursor.getName());
                    //##########################################################
                    //          map_file implementation
                    //##########################################################                            
                            map_files.put(file_cursor.getName(), file_cursor);
                            
                        }
                    }
                    
                    else{//attfiles is NOT empty
                        for (File draggedFile : db.getFiles()) {

                            filePath = draggedFile.getAbsolutePath();

                                for(File file_cursor : map_files.values()){
                                    if( file_cursor.getName().equals(draggedFile.getName())  
                                            || file_cursor.equals(draggedFile)
                                            || file_cursor.getAbsolutePath().equals(filePath)
                                            || ListViewATTACHMENTS.getItems().contains(draggedFile.getName())){
                                        System.out.println("FILE ALREADY ADDED: " + draggedFile.getAbsolutePath());
                                        return;
                                    }                            
                                    else{
                                        ListViewATTACHMENTS.getItems().add(draggedFile.getName());
                    //##########################################################
                    //          map_file implementation
                    //########################################################## 
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
        TextAreaRWCOMMENTS.clear();
        TextAreaFUNOTES.clear();
        ListViewATTACHMENTS.getItems().clear();
        att = null;
        removedFiles.clear();
        listViewCount = 0;
                    //##########################################################
                    //          map_file implementation
                    //########################################################## 
                    
                    map_files.clear();
    }
    //--------------------------------------------------------------------------
    //open selected file
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
                } catch (IOException ex ) {
                    Logger.getLogger(EditPOView_NOANCHORController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @FXML
    private void removeListItem(ActionEvent event) {
        if(listViewCount>0 && !ListViewATTACHMENTS.getItems().isEmpty() 
                && ListViewATTACHMENTS.getSelectionModel().getSelectedIndex() >=0){
            
            removedFiles.add((String) ListViewATTACHMENTS.getSelectionModel().getSelectedItem());//adding file name
            System.out.println("ADDED " + removedFiles + " TO REMOVED FILES LIST!" );
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

//##################################################
//  OPEN FORM/DIALOG
    //update TextAreas whenever dialog is closed
//##################################################
    private void openNoteViewDialog(ActionEvent event) {

    }

    @FXML
    private void openRwCommentDialog(ActionEvent event) {
        
        File tempNoteFile = FileHelper.getRWFile(POparameter);
        
        FXMLLoader loader = new FXMLLoader (getClass().getResource("/View/Overview/NotesView.fxml"));
        try {
            Parent parent  = loader.load();
            Scene scene = new Scene(parent, 600, 800);
            Stage stage = new Stage();
            stage.setMinWidth(600);
            stage.setMinHeight(800);
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
                NotesViewController note_controller = loader.getController();

            if(tempNoteFile!=null){

                String notes_content = FileHelper.fileContent(tempNoteFile).toString();
                if(!notes_content.isEmpty()){ 
                    note_controller.setPoLabel(POparameter);
//                    note_controller.setFuDateLabel(DatePickerFUDATE.getValue().toString());
if(DatePickerFUDATE.getValue() !=null)
note_controller.setFuDateLabel(DatePickerFUDATE.getValue().toString());
else
    note_controller.setFuDateLabel(null);
//                                        controller.setContext(notes_content, data);
                    note_controller.setContext(tempNoteFile);
                }
                else{
                    note_controller.setPoLabel(POparameter);
//                    note_controller.setFuDateLabel(DatePickerFUDATE.getValue().toString());
note_controller.setFuDateLabel("no date");
                    note_controller.setContext(null);
                }
            }
            else{
//                                    dialog.setContentText("did you happen to delete the file?!");
            }
            stage.showAndWait();
            String notes_content = FileHelper.csvFileContent(tempNoteFile);//fileContent(tempNoteFile).toString();
            TextAreaRWCOMMENTS.clear();
            if(!notes_content.isEmpty()){
                TextAreaRWCOMMENTS.setText(notes_content);
            }
            
        } catch (IOException ex) {
                Logger.getLogger(OverviewViewTesting_NOANCHORController.class.getName()).log(Level.SEVERE, null, ex);
            }
        
    }
    //##########################################################################
    @FXML
    private void openFuNoteDialog(ActionEvent event) {
        
        File tempNoteFile = FileHelper.getFUFile(POparameter);
        FXMLLoader loader = new FXMLLoader (getClass().getResource("/View/Overview/NotesView.fxml"));
        try {
            Parent parent  = loader.load();
            Scene scene = new Scene(parent, 600, 800);
            Stage stage = new Stage();
            stage.setMinWidth(600);
            stage.setMinHeight(800);
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            NotesViewController note_controller = loader.getController();

            if(tempNoteFile!=null){

                String notes_content = FileHelper.fileContent(tempNoteFile).toString();
                if(!notes_content.isEmpty()){ 
                    note_controller.setPoLabel(POparameter);
//                    note_controller.setFuDateLabel(DatePickerFUDATE.getValue().toString());
if(DatePickerFUDATE.getValue() !=null)
note_controller.setFuDateLabel(DatePickerFUDATE.getValue().toString());
else
    note_controller.setFuDateLabel(null);
//                                        controller.setContext(notes_content, data);
                    note_controller.setContext(tempNoteFile);
                }
                else{
                    note_controller.setPoLabel(POparameter);
//                    note_controller.setFuDateLabel(DatePickerFUDATE.getValue().toString());
note_controller.setFuDateLabel("no date");
                    note_controller.setContext(null);
                }
            }
            else{
//                                    dialog.setContentText("did you happen to delete the file?!");
            }
            stage.showAndWait();
            String notes_content = FileHelper.csvFileContent(tempNoteFile);//fileContent(tempNoteFile).toString();
            TextAreaFUNOTES.clear();
            if(!notes_content.isEmpty()){
                TextAreaFUNOTES.setText(notes_content);
            }
            
        } catch (IOException ex) {
                Logger.getLogger(OverviewViewTesting_NOANCHORController.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
   
}
