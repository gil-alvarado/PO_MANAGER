/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.BMSPurchaseOrderModel;
import java.awt.Desktop;

import Model.ConnectionUtil;
import Model.FileHelper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;

import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.swing.text.NumberFormatter;

import net.ucanaccess.complex.Attachment;
import org.apache.commons.io.FileUtils;

/**
 * FXML Controller class
 *  retrieve data from ModelManageDBView
 * @author Gilbert Alvarado
 */
public class EditPOViewController implements Initializable {

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
    
    
    
    private String POparameter;
    @FXML
    private Label currentPOLabel;
    
    private ManageDBViewController controller;//
    private MainLayoutController instance;
    
    private Attachment attachments[];//ucanaccess: read/create files and add to local dir
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
    @FXML
    private Button uploadButton, refreshButton, reloadButton, removeButton;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        removedFiles = new ArrayList<>();
        map_files = new LinkedHashMap<>();
        
        if(!FileHelper.createEmailDirectory())
        try {    
            System.out.println("TEMP EMAIL DIR EXISTS, CLEANING OUT DIR");
            FileUtils.cleanDirectory(new File(FileHelper.getTempEmailDirectoryLocation()));
        } catch (IOException ex) {
            Logger.getLogger(EditPOViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        ListViewATTACHMENTS.setEditable(true);
        
        BUTTONrwComment.setDisable(true); BUTTONFUnote.setDisable(true);
        
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
            String text = change.getControlNewText();
            if (text.matches("\\d*\\.?\\d{0,3}")) {
                return change;
            } else {
                return null;
            }
        }));

        //----------------------------------------------------------------------
    
        TextFieldLANDING.setTextFormatter(new TextFormatter<>((change) -> {
            String text = change.getControlNewText();
            if (text.matches("\\d*\\.?\\d{0,2}")) {
                return change;
            } else {
                return null;
            }
        }));    
        //----------------------------------------------------------------------
        
        TextFieldQTY.setTextFormatter(new TextFormatter<>((change) -> {
            String text = change.getControlNewText();
            if (text.matches("\\d*\\.?\\d{0,2}")) {
                return change;
            } else {
                return null;
            }
        }));
        TextFieldPO.setDisable(true);
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
        
        ListViewATTACHMENTS.disableProperty().bind(BUTTONUpdateDatabase.disabledProperty());
        
        uploadButton.disableProperty().bind(ListViewATTACHMENTS.disabledProperty());
        refreshButton.disableProperty().bind(ListViewATTACHMENTS.disabledProperty());
        reloadButton.disableProperty().bind(ListViewATTACHMENTS.disabledProperty());
        removeButton.disableProperty().bind(ListViewATTACHMENTS.disabledProperty());
        //uploadButton, refreshButton, reloadButton, removeButton;
        // BUTTONrwComment, BUTTONFUnote;
        BUTTONrwComment.disableProperty().bind(BUTTONUpdateDatabase.disabledProperty());
        BUTTONFUnote.disableProperty().bind(BUTTONUpdateDatabase.disabledProperty());
        if(LoginViewController.role_ID == 1){
            BUTTONDeleteDatabase.disableProperty().bind(BUTTONUpdateDatabase.disabledProperty());
        }
    }
    
    //#########################################################################

    public void setManageDBViewController(ManageDBViewController controller){
        this.controller = controller;
    }
    public void setMainLayoutInstance(MainLayoutController instance){
        this.instance = instance;
    }
    
    //##########################################################################
    public void setItems(BMSPurchaseOrderModel data){
        
        if(!FileHelper.createEmailDirectory())
        try {    
            System.out.println("TEMP EMAIL DIR EXISTS, CLEANING OUT DIR");
            FileUtils.cleanDirectory(new File(FileHelper.getTempEmailDirectoryLocation()));
        } catch (IOException ex) {
            Logger.getLogger(EditPOViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
 
        clearAllFields();
        
        System.out.println("ATTEMPTING TO EXECUTE STATEMENT");
        POparameter = data.getPurchase_order();
        
        attachments = data.getAttachments();

        if(FileHelper.createPoAttachmentsDirectory(POparameter)){
                System.out.println("ATTACHMENTS DIRECTORY CREATED IN EDIT");
                System.out.println("ADDING ATTACHMENTS TO DIRECTORY");
        }else{
            System.out.println("DIRECTORY ALREADY CREATED");
            System.out.println("ADDING ATTACHMENTS TO DIRECTORY");
        }

        for(Attachment file_cursor : attachments){

            try {
                ListViewATTACHMENTS.getItems().add(file_cursor.getName());
                
                File tempFile = new File(FileHelper.getPoAttachmentDirectory(TextFieldPO.getText()) +"/"+file_cursor.getName());
                org.apache.commons.io.FileUtils.writeByteArrayToFile(
                        tempFile, file_cursor.getData());
                map_files.put(file_cursor.getName(), tempFile);
                listViewCount++;
            } catch (IOException ex) {
                Logger.getLogger(EditPOViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        

        currentPOLabel.setText(POparameter);
        currentPOLabel.setVisible(true);

        TextFieldPO.setText(data.getPurchase_order());
        TextFieldSUPPLIER.setText(data.getSupplier());

        TextFieldBRG.setText(data.getBrg_number());
        TextFieldPARAMETER.setText(data.getParameter());
        TextFieldQTY.setText(data.getQuantity());

        
        //NUMBER FORMAT
        NumberFormat format = NumberFormat.getCurrencyInstance();
        try {
            
            Number number = format.parse(data.getInvoice_price());
            TextFieldINVOICE.setText(IPformat.format(Double.parseDouble(number.toString())));
            number = format.parse(data.getLanding_cost());
            TextFieldLANDING.setText(LCformat.format(Double.parseDouble(number.toString())));
            
        } catch (ParseException ex) {
            Logger.getLogger(MainLayoutController.class.getName()).log(Level.SEVERE, null, ex);
        }        

        ComboBoxCONFIRMED.getSelectionModel().select(data.getConfirmed());

        DatePickerORGSHIP.setValue(data.getOrg_ship_date().toLocalDate());
        DatePickerCURSHIP.setValue(data.getCurrent_ship_date().toLocalDate());
        DatePickerETABMS.setValue(data.getEta_ship_date().toLocalDate());
        if(data.getFu_ship_date()!=null){
            DatePickerFUDATE.setValue(data.getFu_ship_date().toLocalDate());
        }
        //##################################################################
        //  SET NOTES, MOST LIKELY CHANGE LAYOUT
        //##################################################################
        File tempNoteFile = FileHelper.getRWFile(data.getPurchase_order());
        String notes_content = FileHelper.csvFileContent(tempNoteFile);//FileHelper.fileContent(tempNoteFile).toString();
        if(!notes_content.isEmpty()){
            TextAreaRWCOMMENTS.setText(notes_content);
        }
        tempNoteFile = FileHelper.getFUFile(data.getPurchase_order());
        notes_content = FileHelper.csvFileContent(tempNoteFile);//fileContent(tempNoteFile).toString();
        if(!notes_content.isEmpty()){
            TextAreaFUNOTES.setText(notes_content);
        }

    }
    
//##############################################################################
    
    @FXML
    private void updateDatabase(ActionEvent event){
        
        //DatePickerORGSHIP, DatePickerCURSHIP,DatePickerETABMS, DatePickerFUDATE;
        //1: UPDATE PURCHASE_ORDERS
        if( !ConnectionUtil.updatePurchaseOrders( TextFieldPO.getText(),DatePickerORGSHIP.getValue(),
               DatePickerCURSHIP.getValue(),  DatePickerETABMS.getValue(), DatePickerFUDATE.getValue() )){
            return;
        }
        //2: check parameter exists, will add
        if ( !ConnectionUtil.parameterExists(TextFieldPARAMETER.getText() ) ){
            return;
        }
        //3: check supplier exists
        if ( !ConnectionUtil.supplierExists(TextFieldSUPPLIER.getText()) ) {
            return;
        }
        //4: check bearing exists
        if( !ConnectionUtil.bearingsExists(TextFieldBRG.getText(), 
                TextFieldPARAMETER.getText(), TextFieldSUPPLIER.getText()) ){
            return;
        }
        
        //now update order details
        int brg_id = ConnectionUtil.getBrgId(TextFieldBRG.getText(), 
                TextFieldPARAMETER.getText(), TextFieldSUPPLIER.getText());
        
        String confirmed = ( ComboBoxCONFIRMED.getValue().equals("YES")) ? "TRUE" : "FALSE";
        if( !ConnectionUtil.updateOrderDetails(brg_id, TextFieldQTY.getText(), TextFieldLANDING.getText(),
                TextFieldINVOICE.getText(), confirmed, TextFieldPO.getText())){
            return;
        }
        
        //###########################################
        //ATTACHMENTS
        //###########################################  
        for(String rf : removedFiles)
            map_files.remove(rf);

        attachments = new Attachment[map_files.size()];

        Iterator<Map.Entry<String, File>> iterator = map_files.entrySet().iterator();
        int i = 0;
        while(iterator.hasNext() && i < map_files.size()){
            try {
                Map.Entry<String, File> entry = iterator.next();
                String path = entry.getValue().getAbsolutePath();
                byte []attachmentData = java.nio.file.Files.readAllBytes(Paths.get(path));
                attachments[i] = new Attachment(path,entry.getKey(),null, attachmentData,null,null);
                i++;
            } catch (IOException ex) {
                Logger.getLogger(MainLayoutController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(!ConnectionUtil.updateOrderDetailsATTACHMENTS(attachments, TextFieldPO.getText())){
            return;
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
    
    @FXML
    private void deleteFromDatabase(ActionEvent event){
          
        ButtonType YES = new ButtonType("YES", ButtonBar.ButtonData.YES);
        ButtonType NO = new ButtonType("NO", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType OK = new ButtonType("OK", ButtonBar.ButtonData.CANCEL_CLOSE);
        Optional<ButtonType> result;
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        
        Alert updateAlert = new Alert(Alert.AlertType.NONE);
        {
           updateAlert.setTitle("update");
           updateAlert.getButtonTypes().clear();
           updateAlert.getDialogPane().getButtonTypes().addAll( OK );
        }
                    
        if(MainLayoutController.getCurrentUser().getRole_id() == 1 
                && MainLayoutController.getCurrentUser().getRole().equals("ADMIN")){
           
            {
                confirmation.setTitle("DELETE DIALOG");
                confirmation.getButtonTypes().clear();
                confirmation.getDialogPane().getButtonTypes().addAll( YES, NO );
                confirmation.setContentText("ARE YOU SURE YOU WANT TO DELETE " + POparameter + "?");
            }

            result = confirmation.showAndWait();

            if(result.orElse(NO) == YES){//user confirms that they wish to delete

                if(ConnectionUtil.deleteFromDatabase(POparameter)){
                    updateAlert.setContentText("SUCCESSFULLY DELETED");
                    clearAllFields();
                    MainLayoutController.getOverviewViewController().updateTableView();
                    MainLayoutController.getManageDBViewController().updateTableView();
                    currentPOLabel.setText("");
                    currentPOLabel.setVisible(false);
                    updateAlert.showAndWait();
                }else{

                    System.err.println("ERROR: key vilation for : " + TextFieldPO.getText() );

                    {
                        updateAlert.setAlertType(Alert.AlertType.ERROR);
                        updateAlert.setTitle("update error");
                        updateAlert.getButtonTypes().clear();
                        updateAlert.getDialogPane().getButtonTypes().addAll( OK );
                        updateAlert.setContentText("NO RECORD FOUND FOR: " + TextFieldPO.getText());
                        updateAlert.showAndWait();
                    }
                }

            }
        }
        else{
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
                                listViewCount++;
                        }
                    }
                    
                    else{//attfiles is NOT empty
                        
                        LinkedHashMap<String,File> map_files_temp = new LinkedHashMap<>(map_files);
                        for (File draggedFile : db.getFiles()) {

                            filePath = draggedFile.getAbsolutePath();//cant update map_files at runtime

                                for(File file_cursor : map_files_temp.values()){
                                    if( file_cursor.getName().equals(draggedFile.getName())  
                                            || file_cursor.equals(draggedFile)
                                            || file_cursor.getAbsolutePath().equals(filePath)
                                            || ListViewATTACHMENTS.getItems().contains(draggedFile.getName())){
                                        System.out.println("FILE ALREADY ADDED: " + draggedFile.getAbsolutePath());
                                    }                            
                                    else{
                                        ListViewATTACHMENTS.getItems().add(draggedFile.getName());
                    //##########################################################
                    //          map_file implementation
                    //########################################################## 
                                    System.out.println("ADDING FLIE TO MAP: " + draggedFile.getName());
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
  
    private void clearAllFields(){
        for(Node node : paneTextFieldsONE.getChildren()){
            if(node instanceof TextField){
                ((TextField)node).clear();
            }
            else if(node instanceof DatePicker){
                    ((DatePicker)node).setValue(null);
            }
        }
        TextAreaRWCOMMENTS.clear();
        TextAreaFUNOTES.clear();
        ListViewATTACHMENTS.getItems().clear();
        attachments = null;
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
                    System.out.println("ATTEMPTING TO OPEN FILE\nNumber Files: " + listViewCount);
                    File tempFile = map_files.get((String)ListViewATTACHMENTS.getItems().get(ListViewATTACHMENTS.getSelectionModel().getSelectedIndex()));
                    desktop.open(tempFile);
                } catch (IOException ex ) {
                    Logger.getLogger(EditPOViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @FXML
    private void removeListItem(ActionEvent event) {
        
        
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Confirm");
        dialog.setHeaderText("ATTACHMENT REMOVAL");
        dialog.setGraphic(new Circle(15, Color.RED)); // Custom graphic
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        PasswordField pwd = new PasswordField();
        HBox content = new HBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        content.getChildren().addAll(new Label("Enter password to confirm removal: "), pwd);
        dialog.getDialogPane().setContent(content);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return pwd.getText();
            }
            return null;
        });
        Optional<String> result = null;
        
        if( ListViewATTACHMENTS.getSelectionModel().getSelectedIndex() >=0)
            result = dialog.showAndWait();
        
        if(result !=null)
        if ( result.isPresent()  ) {
            if(result.get().equals("Balls22")){
                if(listViewCount>0 && !ListViewATTACHMENTS.getItems().isEmpty() 
                    && ListViewATTACHMENTS.getSelectionModel().getSelectedIndex() >=0){

                removedFiles.add((String) ListViewATTACHMENTS.getSelectionModel().getSelectedItem());//adding file name
                System.out.println("ADDED " + removedFiles + " TO REMOVED FILES LIST!" );
                ListViewATTACHMENTS.getItems().remove(ListViewATTACHMENTS.getSelectionModel().getSelectedIndex());
                listViewCount--;
                }
            }else{
                ButtonType OK = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                Alert alert = new Alert(Alert.AlertType.NONE);
                {
                    alert.setTitle("WRONG PASSWORD");
                    alert.getButtonTypes().clear();
                    alert.getDialogPane().getButtonTypes().addAll( OK );
                    alert.setContentText("WRONG PASSWORD ENTERED");
                    alert.showAndWait();
                }
            }
        }    
        
    }
    //--------------------------------------------------------------------------
    @FXML
    private void reloadListView(ActionEvent event) {
        ListViewATTACHMENTS.getItems().clear();
        removedFiles.clear();
        listViewCount = 0;
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
            
            System.out.println("RwCommentDialog: "+tempNoteFile.getAbsolutePath());
            
            NotesViewController note_controller = loader.getController();
            note_controller.setContext(tempNoteFile, 
                    (BMSPurchaseOrderModel)ConnectionUtil.getAllData().get(POparameter));
           
            stage.showAndWait();
            //UPDATING TEXTAREA
            String notes_content = FileHelper.csvFileContent(tempNoteFile);//fileContent(tempNoteFile).toString();
            TextAreaRWCOMMENTS.clear();
            if(!notes_content.isEmpty()){
                TextAreaRWCOMMENTS.setText(notes_content);
            }
            
        } catch (IOException ex) {
                Logger.getLogger(OverviewViewController.class.getName()).log(Level.SEVERE, null, ex);
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

            note_controller.setContext(tempNoteFile,ConnectionUtil.getAllData().get(POparameter));
            
            stage.showAndWait();
            String notes_content = FileHelper.csvFileContent(tempNoteFile);//fileContent(tempNoteFile).toString();
            TextAreaFUNOTES.clear();
            if(!notes_content.isEmpty()){
                TextAreaFUNOTES.setText(notes_content);
            }
            
        } catch (IOException ex) {
                Logger.getLogger(OverviewViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

    @FXML
    private void openTempEmailAttachmentsFolder(ActionEvent event) {
        try {
            if(!FileHelper.createEmailDirectory())//should already be created
                Desktop.getDesktop().open(new File(FileHelper.getTempEmailDirectoryLocation()));
        } catch (IOException ex) {
            Logger.getLogger(AddPOViewController.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }

    @FXML
    private void refreshListView(ActionEvent event) {
        File folder = new File(FileHelper.getTempEmailDirectoryLocation());
        File[] files = folder.listFiles();
        
        for(File attFile : files){
            if( ListViewATTACHMENTS.getItems().contains(attFile.getName())){
                System.out.println("FILE ALREADY ADDED: " + attFile.getAbsolutePath());
            }                            
            else{

                System.out.println("-----------------------------");
                System.out.println("ADDING " + attFile.getName() + " to map");
                System.out.println("-----------------------------");
                ListViewATTACHMENTS.getItems().add(attFile.getName());
                map_files.put(attFile.getName(), attFile);
                listViewCount++;
            }
        }
    }
   
}
