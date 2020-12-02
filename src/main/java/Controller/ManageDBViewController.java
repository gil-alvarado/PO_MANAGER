/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.ConnectionUtil;
import Model.ModelManageDBTable;
import Model.ModelOverviewTable;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
/**
 * FXML Controller class
 *
 * @author Gilbert Alvarado
 */
public class ManageDBViewController implements Initializable {


    @FXML
    private DatePicker CURSHIP_DatePicker;
    @FXML
    private DatePicker ETABMS_DatePicker;
    @FXML
    private TextField SUPPLIERTextField;
    @FXML
    private ComboBox<?> CONFIRMEDcomboBox;
    @FXML
    private TextField POTextField;
    @FXML
    private TextField BRGTextField;
    
    @FXML
    private TableView<ModelManageDBTable> ManageDBTable;
    @FXML
    private TableColumn<ModelManageDBTable, String> POcolumn, BRGcolumn, CURSHIP_column, BUTTONcolumn,
            STATUScolumn, ATTACHMENTScolumn, PACKETcolumn;
    
    ObservableList<ModelManageDBTable> obList = FXCollections.observableArrayList();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
//        @FXML
//    private TableColumn<ModelManageDBTable, String> POcolumn, BRGcolumn, CURSHIP_column, BUTTONcolumn,
//            STATUScolumn, ATTACHMENTScolumn, PACKETcolumn;
            POcolumn.setCellValueFactory(new PropertyValueFactory<>("po"));
            BRGcolumn.setCellValueFactory(new PropertyValueFactory<>("brg"));
            CURSHIP_column.setCellValueFactory(new PropertyValueFactory<>("cur"));
            
            //CONFIRMED
            STATUScolumn.setCellValueFactory(new PropertyValueFactory<>("confirmed"));
            ATTACHMENTScolumn.setCellValueFactory(new PropertyValueFactory<>("po"));
            PACKETcolumn.setCellValueFactory(new PropertyValueFactory<>("packet"));
            
           // BUTTONcolumn.setCellValueFactory(new PropertyValueFactory<>("button"));
            
            updateTableView();
            addButtonsToTable();
            
            
    }    
    //##########################################################################
    private void updateTableView(){
        
     try{
            Connection con = ConnectionUtil.conDB();
            System.out.println("attempting to upfate table");
            //THIS IS ONLY WORKING WITH ONE TABLE(purchaseorders). MAKE IT WORK WOTH THE TABLES RELATIONSHIPS 
//            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM purchaseorders;");
        
            ResultSet rs = con.createStatement().executeQuery("SELECT Confirmed,purchaseOrder,"
                    + "Format(CURRENT_SHIP_DATE,'Short Date') FROM purchaseorders;");
//Format(FUdate,'Short Date')
            
        //(String confirmed,String po, String brg, String supplier, 
        //             String cur, String fudate,String funotes) {

//            OverviewTableView.getItems().clear();
            while(rs.next()){
//ModelManageDBTable(String po, String brg, String cur, String attachment, String packet)
                obList.add(new 
        ModelManageDBTable(rs.getString("Confirmed"),
                        rs.getString("purchaseOrder"),                       
                        "add BRG",
                        rs.getString(3),
                        "num att",
                        "add packet box"));

//            System.out.println(rs.getString(1));
            }
            
            
            
        } catch (SQLException ex) {
            Logger.getLogger(ManageDBViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        ManageDBTable.setItems(obList);
    }
    //##########################################################################
    private void addButtonsToTable(){
                                    
                            //-------------------------------------------------
//                            Dialog<String> dialog = new Dialog<String>();
//                            //Setting the title
//                            dialog.setTitle("Dialog");
//                            ButtonType type = new ButtonType("Ok", ButtonData.OK_DONE);
//                            //Setting the content of the dialog
//                            dialog.setContentText("PO: " + dbItem.getPo() + ", brg#: " + dbItem.getBrg()
//                            +"\nCREAT MODEL FOR ENTIRE DB, SHAR THAT MODEL THROUGHT THE ENTIRE PROGRAM");
//                            //Adding buttons to the dialog pane
//                            dialog.getDialogPane().getButtonTypes().add(type);
//                            dialog.showAndWait();
        //BUTTONcolumn
        Callback<TableColumn<ModelManageDBTable, String>, TableCell<ModelManageDBTable, String>> cellFactory;
        
        cellFactory = new Callback<TableColumn<ModelManageDBTable, String>,TableCell<ModelManageDBTable, String>>(){
            @Override
            public TableCell<ModelManageDBTable, String> call(TableColumn<ModelManageDBTable, String> param) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                final TableCell<ModelManageDBTable, String> cell = new TableCell<ModelManageDBTable, String>(){
                    
//                    ModelManageDBTable dbItem = getTableView().getItems().get(getIndex());
                    
                    private final Button btn = new Button("Action");
                    {
                        btn.setMaxHeight(50);
                        btn.setMaxWidth(100);
                        btn.setStyle("-fx-font-size: 18px;-fx-font-weight: bold;\n" 
                                + "-fx-font-family: Georgia;");
                    }
                    
                    Dialog<String> dialog = new Dialog<String>();{
                            dialog.setTitle("Manage DIALOG");
                            ButtonType type = new ButtonType("Ok", ButtonData.OK_DONE);
                            dialog.getDialogPane().getButtonTypes().add(type);
                    }
                    @Override
                    public void updateItem(String item, boolean empty){
                        super.updateItem(item, empty);
                        if(empty){
                            setGraphic(null);
                        }else{
                            btn.setOnAction((ActionEvent event) -> {
                            ModelManageDBTable data = getTableView().getItems().get(getIndex());
                            System.out.println("Selected Data: " + data);
                            
                            dialog.setContentText("PO: " + data.getPo() + ", brg#: " + data.getCur()
                            +"\nCREAT MODEL FOR ENTIRE DB, SHARE THAT MODEL THROUGHT THE ENTIRE PROGRAM");
                            dialog.showAndWait();
                        });
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };
        
        BUTTONcolumn.setCellFactory(cellFactory);
//        ManageDBTable.getColumns().add(BUTTONcolumn);
        
    }
}
