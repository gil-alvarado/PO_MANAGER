/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.ConnectionUtil;
import Model.ModelOverviewTable;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
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
public class OverviewViewTesting_NOANCHORController implements Initializable {


    @FXML
    private DatePicker CURSHIP_DatePicker;
    @FXML
    private DatePicker ETABMS_DatePicker;
    @FXML
    private TextField SUPPLIERTextField;
    @FXML
    private ComboBox<String> CONFIRMEDcomboBox;
    @FXML
    private TextField POTextField;
    @FXML
    private TextField BRGTextField;
    
    //--------------------------------------------------------------------------
    @FXML
    private TableColumn<ModelOverviewTable, String> CONFIRMEDcolumn, POcolumn,BRGcolumn,
            SUPPLIERcolumn, CURSHIP_column, DATEcolumn, NOTEScolumn;
    @FXML
    private TableColumn<ModelOverviewTable, String> IPcolumn, LCcolumn;
    
    
    ObservableList<ModelOverviewTable> obList = FXCollections.observableArrayList();
    
    @FXML
    private TableView<ModelOverviewTable> OverviewTableView;
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        CONFIRMEDcomboBox.getItems().addAll("YES","NO");
        CONFIRMEDcomboBox.setEditable(false);
        CONFIRMEDcomboBox.getSelectionModel().select("YES");
        //----------------------------------------------------------------------
//            @FXML
//    private TableColumn<ModelOverviewTable, String> CONFIRMEDcolumn, POcolumn,BRGcolumn,
//            SUPPLIERcolumn, CURSHIP_column, DATEcolumn, NOTEScolumn;
        CONFIRMEDcolumn.setCellValueFactory(new PropertyValueFactory<>("confirmed"));
        POcolumn.setCellValueFactory(new PropertyValueFactory<>("po"));
        BRGcolumn.setCellValueFactory(new PropertyValueFactory<>("brg"));
        SUPPLIERcolumn.setCellValueFactory(new PropertyValueFactory<>("supplier"));
        CURSHIP_column.setCellValueFactory(new PropertyValueFactory<>("cur"));
        DATEcolumn.setCellValueFactory(new PropertyValueFactory<>("da"));
//        NOTEScolumn.setCellValueFactory(new PropertyValueFactory<>("funotes"));
        
        IPcolumn.setCellValueFactory(new PropertyValueFactory<>("ip"));
        LCcolumn.setCellValueFactory(new PropertyValueFactory<>("lc"));
        
        updateTableView();
        addButtonsToTable();//NOTEScolumn
        
    }    
    //##########################################################################
    public void updateTableView(){
        
     try{
            Connection con = ConnectionUtil.conDB();
            System.out.println("attempting to upfate table");
            //THIS IS ONLY WORKING WITH ONE TABLE(purchaseorders). MAKE IT WORK WOTH THE TABLES RELATIONSHIPS 
//            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM purchaseorders;");
        
            ResultSet rs = con.createStatement().executeQuery("SELECT Confirmed,purchaseOrder,"
                    + "Format(CURRENT_SHIP_DATE,'Short Date'),Format(FUdate,'Short Date') FROM purchaseorders;");
//Format(FUdate,'Short Date')
            
        //(String confirmed,String po, String brg, String supplier, 
        //             String cur, String fudate,String funotes) {

//            OverviewTableView.getItems().clear();
            while(rs.next()){
                
                obList.add(new 
        ModelOverviewTable(rs.getString("Confirmed"),
                        rs.getString("purchaseOrder"),
                        "add BRG",
                        "add SUP",
                        rs.getString(3),
                        rs.getString(4),
                        "add FU-NOTE",
                        "ip: $100.505",
                        "lc: $55.01"));

//            System.out.println(rs.getString(1));
            }
            
            
            
        } catch (SQLException ex) {
            Logger.getLogger(OverviewViewTesting_NOANCHORController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        OverviewTableView.setItems(obList);
    }
        //##########################################################################
    private void addButtonsToTable(){
        
        //BUTTONcolumn
        Callback<TableColumn<ModelOverviewTable, String>, TableCell<ModelOverviewTable, String>> cellFactory;
        
        cellFactory = new Callback<TableColumn<ModelOverviewTable, String>,TableCell<ModelOverviewTable, String>>(){
            @Override
            public TableCell<ModelOverviewTable, String> call(TableColumn<ModelOverviewTable, String> param) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                final TableCell<ModelOverviewTable, String> cell = new TableCell<ModelOverviewTable, String>(){
                    
                    private final Button btn = new Button("View");
                    {
                        btn.setMaxHeight(50);
                        btn.setMaxWidth(100);
                        btn.setStyle("-fx-font-size: 18px;-fx-font-weight: bold;\n" 
                                + "-fx-font-family: Georgia;");
                    }
                    Dialog<String> dialog = new Dialog<String>();{
                            dialog.setTitle("Overview DIALOG");
                            ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
                            dialog.getDialogPane().getButtonTypes().add(type);
                    }
                    @Override
                    public void updateItem(String item, boolean empty){
                        super.updateItem(item, empty);
                        if(empty){
                            setGraphic(null);
                        }else{
                            btn.setOnAction(event -> {
                                ModelOverviewTable data = getTableView().getItems().get(getIndex());
                                System.out.println(data.getPo()
                                        + "   " + data.getConfirmed());
                                
                                dialog.setContentText("PO: " + data.getPo() + ", brg#: " + data.getLc()
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
        
        NOTEScolumn.setCellFactory(cellFactory);
//        ManageDBTable.getColumns().add(BUTTONcolumn);
        
    }
}
    