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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javax.swing.text.NumberFormatter;
import net.ucanaccess.complex.Attachment;
import org.apache.commons.io.FileUtils;

/**
 * FXML Controller class
 *
 * @author Gilbert Alvarado
 */
public class AddPOViewController implements Initializable {

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
    private Attachment attachments[];

    @FXML
    private Button uploadButton;
    
    @FXML
    private Button refreshButton,removeButton,reloadButton;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        map_files = new LinkedHashMap<>();
        
        removedFiles = new ArrayList<>();
        
        if(!FileHelper.createEmailDirectory())
        try {    
            System.out.println("TEMP EMAIL DIR EXISTS, CLEANING OUT DIR");
            FileUtils.cleanDirectory(new File(FileHelper.getTempEmailDirectoryLocation()));
        } catch (IOException ex) {
            Logger.getLogger(EditPOViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        BUTTONaddToDatabase.setDisable(true);
        
        ComboBoxCONFIRMED.getItems().addAll("YES", "NO");
        ComboBoxCONFIRMED.setEditable(false);
        ComboBoxCONFIRMED.getSelectionModel().select("YES");
        
        TextFieldPO.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { 
                    if(TextFieldPO.getText().isEmpty()){
                        TextFieldPO.setText("");
                    }
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
        
        ListViewATTACHMENTS.disableProperty().bind(BUTTONaddToDatabase.disabledProperty());
        
        uploadButton.disableProperty().bind(ListViewATTACHMENTS.disabledProperty());
        refreshButton.disableProperty().bind(ListViewATTACHMENTS.disabledProperty());
        //----------------------------------------------------------------------
        reloadButton.disableProperty().bind(ListViewATTACHMENTS.disabledProperty());
        removeButton.disableProperty().bind(Bindings.isEmpty(ListViewATTACHMENTS.getItems()));
    }    
    
    
    //##########################################################################
    //
    //##########################################################################

    
    @FXML
    private void addToDatabase(ActionEvent event) {
        
        if(ConnectionUtil.poExists(TextFieldPO.getText().trim())){
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
        
        //DatePickerORGSHIP, DatePickerETABMS, DatePickerFUDATE, DatePickerCURSHIP;
        if( !ConnectionUtil.addPo(TextFieldPO.getText().trim(),
                DatePickerORGSHIP.getValue(), DatePickerCURSHIP.getValue(),
                DatePickerETABMS.getValue(), DatePickerFUDATE.getValue()) )
            return;
        
        if(!ConnectionUtil.parameterExists(TextFieldPARAMETER.getText())){
            return;
        }
        
        if( !ConnectionUtil.supplierExists(TextFieldSUPPLIER.getText()) )
            return;
        
        if( !ConnectionUtil.bearingsExists(TextFieldBRG.getText(), 
                TextFieldPARAMETER.getText(), TextFieldSUPPLIER.getText()) )
            return;
        
        int brg_id = ConnectionUtil.getBrgId(TextFieldBRG.getText(),
                TextFieldPARAMETER.getText(), TextFieldSUPPLIER.getText());
        
        //after inserting new bearings, insert order details
        if(brg_id >= 0){
            String confirmed =  (ComboBoxCONFIRMED.getValue().equals("YES")) ? "TRUE" : "FALSE";
            
            if(ConnectionUtil.insertOrderDetails(TextFieldPO.getText().trim(), 
                    brg_id, TextFieldQTY.getText(), TextFieldLANDING.getText(),
                    TextFieldINVOICE.getText(), confirmed)){
                
                //now update table/po for attachments
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
                if( ConnectionUtil.updateOrderDetailsATTACHMENTS(attachments, TextFieldPO.getText()) ){
                
                    if( FileHelper.createDirectory(TextFieldPO.getText()) )
                        if( FileHelper.createPoAttachmentsDirectory(TextFieldPO.getText()) ){
                        System.out.println("ATTACHMENTS DIRECTORY CREATED IN ADD PO");
                        }else{
                            System.out.println("DIRECTORY ALREADY CREATED");
                        }

                    //##############################################
                    //          VALIDATE EMPTY ENTRY
                    //##############################################
                if ( FileHelper.createCSVfiles(TextFieldPO.getText()) )
                    if( FileHelper.wirteToFile
                        (TextFieldPO.getText(), LoginViewController.current_user, TextAreaRWCOMMENTS.getText().replaceAll("[\\t\\n\\r]+"," "), 
                                TextAreaFUNOTES.getText().replaceAll("[\\t\\n\\r]+"," ")) )
                    {
                        System.out.println("Successfuly created and wrote to file for RWCOMMENTS");
                        System.out.println("NOW add file to attachments");
                    }else{
                        System.out.println("failed to write to file");
                    }                                

                    System.out.println("ATTEMPTING TO INSERT NOTES/COMMENTS TO DB");
                    
                    if( ConnectionUtil.updateNotesTable(TextFieldPO.getText()) ){
                        
                        Dialog<String> dialog = new Dialog<String>();
                        {
                        dialog.setTitle("Overview DIALOG");
                        ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
                        dialog.getDialogPane().getButtonTypes().add(type);
                        dialog.setContentText("SUCCESSFULY ADDED: " + TextFieldPO.getText());
                        dialog.showAndWait();
                        }
                        clearAllFields();
                    }
                    else{
                        ButtonType OK = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                        Alert alert = new Alert(Alert.AlertType.NONE);
                        {
                            alert.setTitle("update");
                            alert.getButtonTypes().clear();
                            alert.getDialogPane().getButtonTypes().addAll( OK );
                            alert.setContentText("UNSUCCESSFULLY ADDED: " + TextFieldPO.getText());//now insert into po_notes
                            alert.showAndWait();
                        }
                    }
                }
            }//end insert order details
            
        }
        
    }//end add
    //##########################################################################
    //LABEL AND ITEMLIST
    //##########################################################################
    @FXML
    private void handleDragOver(DragEvent event){
        
        if(event.getDragboard().hasFiles() ){
            event.acceptTransferModes(TransferMode.COPY);
            event.consume();
        }
    }
    @FXML
    private void handleDrop(DragEvent event) throws FileNotFoundException{
        
                Dragboard db = event.getDragboard();
                boolean success = false;
                
                if ( db.hasFiles() ){//|| db.hasHtml() || db.hasString() || db.hasUrl() || db.hasImage()) {
                    
                    success = true;
                    String filePath;
                    
                    if(map_files.isEmpty() || ListViewATTACHMENTS.getItems().isEmpty()){
                        for (File file_cursor : db.getFiles()) {
                            ListViewATTACHMENTS.getItems().add(file_cursor.getName());
                            map_files.put(file_cursor.getName(), file_cursor);
                            listViewCount++;
                        }
                    }
                    else{
                        
                        
                        LinkedHashMap<String,File> map_files_temp = new LinkedHashMap<>(map_files);
                        System.out.println("----------\nGOING THROUGH DRAGGED FILES\nNUM FILES: "+db.getFiles().size()+"\n----------");
                        for (File draggedFile : db.getFiles()) {

                            filePath = draggedFile.getAbsolutePath();

                                for(File attFile : map_files_temp.values()){
//                                    System.out.println("DRAGGED FILE NAME: " + draggedFile.getName()+"\n-------");
                                    
                                    if( attFile.getName().equals(draggedFile.getName())  
                                            || attFile.equals(draggedFile)
//                                            || attFile.getAbsolutePath().equals(filePath)
                                            || ListViewATTACHMENTS.getItems().contains(draggedFile.getName())){
                                        System.out.println("FILE ALREADY ADDED: " + draggedFile.getAbsolutePath());
//                                        return;//doesnt read through entire file list if already listed in listView
                                    }                            
                                    else{

                                        System.out.println("-----------------------------");
                                        System.out.println("ADDING " + draggedFile.getName() + " to attfiles");
                                        System.out.println("-----------------------------");
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
        attachments = null; 
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
                System.out.println("ATTEMPTING TO OPEN FILE");
                try {
                    
                    File tempFile = map_files.get((String)ListViewATTACHMENTS.getItems().get(ListViewATTACHMENTS.getSelectionModel().getSelectedIndex()));
                    desktop.open(tempFile);
                } catch (IOException ex) {
                    Logger.getLogger(EditPOViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
    }
//##############################################################################
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

        Optional<String> result = dialog.showAndWait();
        
        if ( result.isPresent() ) {
            if(result.get().equals("Balls22")){
                System.out.println("ATTEMPTING TO REMOVE FILE\nNumber Files: " + listViewCount);
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
                    alert.setTitle("WRONG INPUT");
                    alert.getButtonTypes().clear();
                    alert.getDialogPane().getButtonTypes().addAll( OK );
                    alert.setContentText("WRONG PASSWORD ENTERED");
                    alert.showAndWait();
                }
            }
        }
    }

    @FXML
    private void reloadListView(ActionEvent event) {
        ListViewATTACHMENTS.getItems().clear();
        removedFiles.clear();
        listViewCount = ListViewATTACHMENTS.getItems().size();//0
        for (Iterator<Map.Entry<String, File>> it = map_files.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, File> cursor = it.next();
            ListViewATTACHMENTS.getItems().add(cursor.getKey());
            listViewCount++;
        }
    }
    
    @FXML
    private void openTempEmailAttachmentsFolder(ActionEvent event) {
        try {
            FileHelper.createEmailDirectory();
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
                ListViewATTACHMENTS.getItems().add(attFile.getName());
                map_files.put(attFile.getName(), attFile);
                listViewCount++;
            }
        }
    }

}
