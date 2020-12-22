/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.awt.Desktop;

import Model.ConnectionUtil;
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
import java.util.List;
import java.util.Optional;
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
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.GridPane;

import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
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
    
    @FXML
    private Label dragLABEL;
    
    
    private String POparameter,BRGparameter, CONFIRMEDparameter;
    private int BRGIDparameter;
    @FXML
    private Label currentPOLabel;
    
    private ManageDBViewController controller;
    private MainLayoutTesting_WITHANCHORController instance;
    
    private Attachment att[];//ucanaccess: read/create files and add to local dir
    List <File> attfiles;// list of ALL FILES from Database and user-added files
    List <String> removedFiles;// add for every removed file/item from listview
    
    @FXML
    private int listViewCount;
    
    private static DecimalFormat LCformat = new DecimalFormat("#######00.00");
    private static DecimalFormat IPformat = new DecimalFormat("#######00.000");
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        attfiles = new ArrayList<>();
        removedFiles = new ArrayList<>();
        
        ListViewATTACHMENTS.setCellFactory(TextFieldListCell.forListView());
                
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
//        final TextFormatter<Object> INVOICE_PRICEFormatter = new TextFormatter<>(change -> {
//            if (change.getControlNewText().isEmpty()) {
//                return change;
//            }
//            ParsePosition parsePosition = new ParsePosition(0);
//            Object object = IPformat.parse(change.getControlNewText(), parsePosition);
//
//            if (object == null || parsePosition.getIndex() < change.getControlNewText().length()) {
//                return null;
//            } else {
//                return change;
//            }
//        });
//        TextFieldINVOICE.setTextFormatter(INVOICE_PRICEFormatter);
        
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

//        TextFieldINVOICE.focusedProperty().addListener((observable, oldValue, newValue) -> {
//                if (!newValue) { 
//                    if(!TextFieldINVOICE.getText().matches("\\d*(\\.\\d{0,2})?")){
//                        //when it not matches the pattern (1.0 - 6.0)
//                        //set the textField empty
//                        TextFieldINVOICE.setText("");
//                    }
//                }
//        });
        //----------------------------------------------------------------------

//    final TextFormatter<Object> LANDING_COSTFormatter = new TextFormatter<>(change -> {
//            if (change.getControlNewText().isEmpty()) {
//                return change;
//            }
//            ParsePosition parsePosition = new ParsePosition(0);
//            Object object = LCformat.parse(change.getControlNewText(), parsePosition);
//
//            if (object == null || parsePosition.getIndex() < change.getControlNewText().length()) {
//                return null;
    //            } else {
//                return change;
//            }
//        });
//    
//    TextFieldLANDING.setTextFormatter(LANDING_COSTFormatter);


    
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
//        TextFieldLANDING.focusedProperty().addListener((observable, oldValue, newValue) -> {
////            setAddButtonSettings();
//                    if (!newValue) { 
//                        if(!TextFieldLANDING.getText().matches("\\d+(\\.\\d+)?")){
//                            //when it not matches the pattern (1.0 - 6.0)
//                            //set the textField empty
//                            TextFieldLANDING.setText("");
//                        }
//                    }
//        });
        //----------------------------------------------------------------------
        // BUTTONUpdateDatabase, BUTTONDeleteDatabase;
            GridPane p = (GridPane)DataEntryPanes.getChildren().get(0);

                for(Node node: p.getChildren()){
                    if(node instanceof TextField ){ 
                        if(node instanceof TextField){
                            ((TextField)node).textProperty().addListener((obs, old, newV)->{ 
                                
                              if(!newV.trim().isEmpty()&& isTextFieldAllFilled(paneTextFieldsONE)){ 
//                                  if(isDatePickerFilled(paneTextFieldsONE)){
////                                    BUTTONaddToDatabase.setDisable(false);
                                    BUTTONUpdateDatabase.setDisable(false);
                                    BUTTONDeleteDatabase.setDisable(false);
//                                  }
                              }
                              else{
//                                  BUTTONaddToDatabase.setDisable(true); 
                                  BUTTONUpdateDatabase.setDisable(true);
                                  BUTTONDeleteDatabase.setDisable(true);
                              }
                          });  
                        }
//                        else{
//                            ((DatePicker)node).valueProperty().addListener((obs, old, newV)->{
//
//                              if(newV != null && isDatePickerFilled(paneTextFieldsONE)){ 
//                                  if(isTextFieldAllFilled(paneTextFieldsONE)){
////                                    BUTTONaddToDatabase.setDisable(false);
//                                    BUTTONUpdateDatabase.setDisable(false);
//                                    BUTTONDeleteDatabase.setDisable(false);
//                                  }
//                                  else{
////                                      BUTTONaddToDatabase.setDisable(true);
//                                      BUTTONUpdateDatabase.setDisable(true);
//                                      BUTTONDeleteDatabase.setDisable(true);
//                                  }
//                              }
//                              else{
////                                  BUTTONaddToDatabase.setDisable(true); 
//                                  BUTTONUpdateDatabase.setDisable(true);
//                                  BUTTONDeleteDatabase.setDisable(true);
//                              }
//                          });  
//                        }               
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
    public void setItems(ModelManageDBTable data){
        //----------------------------------------------------------------------
        
        clearAllFields();
        System.out.println("ATTEMPTING TO EXECUTE STATEMENT");
        POparameter = data.getPo();//original PO
        System.out.println("SET ITEMS, PO = " +data.getPo());
        BRGparameter = data.getBrg();//original bearing number
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
//               
//ADDING EXISTING/DRAGGED FILE TO ATTACHMENT COLUMN
//( String url, String name, String type, byte[] data, Date timeStamp, Integer flags) 
// new Attachment(null (URL), "sample.pdf"(NAME), "pdf"(TYPE), attachmentData(DATA), new java.util.Date()(DATE), null (FLAGS))
//new Attachment (filePath, )

//CREATE NEW FILE FROM ATTACHMENT COLUMN

            att = (Attachment[]    )rs.getObject(13);//get attachments from Database
            for(Attachment f : att){
                System.out.println("FILE NAME: " + f.getName());
                System.out.println("FILE TYPE: " + f.getType());
                
                //display existing fileNAMES on DB to listview
                ListViewATTACHMENTS.getItems().add(f.getName());
                
                //add existing files to attfiles, array of actual files
                File tempDir = new File(ConnectionUtil.desktopLocation + "/BMStemp");
                boolean fileExec = tempDir.mkdir();
                if(fileExec){
                    System.out.println("dir exists!!");
                }
                else{//create files at tempDirectory, write data to file from Database, add to list of ALL FILES
                    File tempFile = new File(ConnectionUtil.desktopLocation + "/BMStemp/" + f.getName());
                    org.apache.commons.io.FileUtils.writeByteArrayToFile(
                            tempFile, f.getData());
                    attfiles.add(tempFile);
                    
                }
                listViewCount++;
            }            
            
            System.out.println("EDIT: NUMBER ATTACHMENTS for : " + POparameter + " = "  + att.length);
            
            currentPOLabel.setText(data.getPo());
            currentPOLabel.setVisible(true);
            
            TextFieldPO.setText(rs.getString("purchase_order"));
            TextFieldSUPPLIER.setText(rs.getString("supplier_id"));
            
//            TextFieldINVOICE.setText(IPformat.format(Double.parseDouble(rs.getString("invoice_price"))));
//            TextFieldINVOICE.setText(LCformat.format(Double.parseDouble(rs.getString(""))));

//            System.out.println("LANDED COST: " + LCformat.format(Double.parseDouble(rs.getString("landed_cost"))));
//            System.out.println("IP: " + IPformat.format(Double.parseDouble(rs.getString("invoice_price"))));
            
            TextFieldBRG.setText(rs.getString("brg_name"));
            TextFieldPARAMETER.setText(rs.getString("PARAMETER"));
            TextFieldQTY.setText(rs.getString("quantity"));
            
            TextFieldLANDING.setText(LCformat.format(Double.parseDouble(rs.getString("landed_cost"))));
            TextFieldINVOICE.setText(IPformat.format(Double.parseDouble(rs.getString("invoice_price"))));
            
            //----------------------------------------------------------------------
            if(rs.getString("confirmed").equals("TRUE"))
                ComboBoxCONFIRMED.getSelectionModel().select("YES");
            else
                ComboBoxCONFIRMED.getSelectionModel().select("NO");
            //----------------------------------------------------------------------
            //DATES
            //-----------------------------------------------------------------------
            //ORIGINAL SHIP DATE
            String []tok = rs.getString(6).split("/");
            DatePickerORGSHIP.setValue(LocalDate.of(Integer.parseInt(tok[2]),
                    Month.of(Integer.parseInt(tok[0])), Integer.parseInt(tok[1])));
            //----------------------------------------------------------------------
            //CURRENT SHIP DATE
            tok=rs.getString(6).split("/");
            DatePickerCURSHIP.setValue(LocalDate.of(Integer.parseInt(tok[2]),
                     Month.of(Integer.parseInt(tok[0])), Integer.parseInt(tok[1])));
            //----------------------------------------------------------------------
            //ETA DATE
            tok=rs.getString(8).split("/");
            DatePickerETABMS.setValue(LocalDate.of(Integer.parseInt(tok[2]),
                     Month.of(Integer.parseInt(tok[0])), Integer.parseInt(tok[1])));
            //----------------------------------------------------------------------
            //FOLLOW UP
            tok=rs.getString(9).split("/");
            DatePickerFUDATE.setValue(LocalDate.of(Integer.parseInt(tok[2]),
                     Month.of(Integer.parseInt(tok[0])), Integer.parseInt(tok[1])));
//                }
            con.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(ManageDBViewController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) { 
            Logger.getLogger(EditPOView_NOANCHORController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //add actions for each button
//        BUTTONaddToDatabase.setDisable(true);
        BUTTONDeleteDatabase.setDisable(false);
        BUTTONUpdateDatabase.setDisable(false);        
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
                 PreparedStatement update = con.prepareStatement(UPDATEpurchase_orders);
                 
                 //UPDATING purchase_orders FIRST
                 update.setString(1, TextFieldPO.getText());//SET
                 update.setDate(2, java.sql.Date.valueOf(DatePickerORGSHIP.getValue()));
                 update.setDate(3, java.sql.Date.valueOf(DatePickerCURSHIP.getValue()));
                 update.setDate(4, java.sql.Date.valueOf(DatePickerETABMS.getValue()));
                 update.setDate(5, java.sql.Date.valueOf(DatePickerFUDATE.getValue()));
                 update.setString(6, POparameter);
                 
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

                    //get new brg_id from bearings, should be added
//                    update.setInt(1, Integer.parseInt(rs.getString("brg_id")));
                    
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
//select attachment field from db add/update attachment field
//public Attachment
//               ( String url, String name, String type, byte[] data, Date timeStamp, Integer flags) 
// new Attachment(null (URL), "sample.pdf"(NAME), "pdf"(TYPE), attachmentData(DATA), new java.util.Date()(DATE), null (FLAGS))
//new Attachment (filePath, )
                            
//    private Attachment att[];//ucanaccess: read/create files and add to local dir
//    List <File> attfiles;// list of ALL FILES from Database and user-added files
//    List <String> removedFiles;// add for every removed file/item from listview

                            //get removed files from listview
                            List<File> tempFileList = new ArrayList<>(attfiles);
                            
                            for(String rf : removedFiles){
                                for(int i =0 ; i < attfiles.size(); i++){
                                    if(attfiles.get(i).getName().equals(rf)){
                                        System.out.println("USER REMOVED :" + attfiles.get(i).getName() + ", REMOVING FROM FIELD");
                                        
                                        tempFileList.remove(i);//ERROR HERE, SIZE OF LIST CHANGES ONCED REMOVAL, TRY REMOVE BY NAME
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
                BUTTONUpdateDatabase.setDisable(true);
                BUTTONDeleteDatabase.setDisable(true);
                
                
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
            
            //prompt user before deletion
            //verify user credentials: 
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
//------------------------------------------------------------------------------                    
                    if(result.orElse(NO) == YES){//user confirms that they wish to delete
                        PreparedStatement pst  = con.prepareStatement("DELETE FROM purchase_orders WHERE purchase_order = ?;");
                        pst.setString(1, TextFieldPO.getText());

                        if(pst.executeUpdate() == 1){//user has permission
        //                        currentPOLabel.setVisible(false);
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
//            clearAllFields();
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
                    
                    else{//attfiles is NOT empty
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
        attfiles.clear();
        att = null;
        removedFiles.clear();
        listViewCount = 0;
    }
    //--------------------------------------------------------------------------
    //open selected file
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

    @FXML
    private void removeListItem(ActionEvent event) {
        if(listViewCount>0 && !ListViewATTACHMENTS.getItems().isEmpty() 
                && ListViewATTACHMENTS.getSelectionModel().getSelectedIndex() >=0){
            removedFiles.add((String) ListViewATTACHMENTS.getSelectionModel().getSelectedItem());
            System.out.println("ADDED " + removedFiles + " TO REMOVED FILES LIST!" );
            ListViewATTACHMENTS.getItems().remove(ListViewATTACHMENTS.getSelectionModel().getSelectedIndex());
            listViewCount--;
        }
    }

    @FXML
    private void reloadListView(ActionEvent event) {
        ListViewATTACHMENTS.getItems().clear();
        removedFiles.clear();
        for(File d: attfiles){
            ListViewATTACHMENTS.getItems().add(d.getName());
            listViewCount++;
        }
    }
}
