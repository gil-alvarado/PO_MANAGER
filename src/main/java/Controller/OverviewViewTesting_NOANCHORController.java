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
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import javafx.scene.paint.Color;
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
    
    private static DecimalFormat LCformat = new DecimalFormat("$###,###,###.00");
    private static DecimalFormat IPformat = new DecimalFormat("$###,###,###.000");
    //--------------------------------------------------------------------------
    @FXML
    private TableColumn<ModelOverviewTable, String> CONFIRMEDcolumn, POcolumn,BRGcolumn,
            SUPPLIERcolumn, CURSHIP_column, DATEcolumn, NOTEScolumn;
    @FXML
    private TableColumn<ModelOverviewTable, String> IPcolumn, LCcolumn;
    
    
    ObservableList<ModelOverviewTable> obList = FXCollections.observableArrayList();
//    ObservableList<ModelOverviewTable> dataList = FXCollections.observableArrayList();
    
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
//        setGreenText();
        
        searchPO();
    }
//##############################################################################    

    public void updateTableView(){
        
        OverviewTableView.getItems().clear();
        
            try{
                   Connection con = ConnectionUtil.conDB();

//                   ResultSet rs = con.createStatement().executeQuery("SELECT Confirmed,purchaseOrder,"
//                           + "Format(CURRENT_SHIP_DATE,'Short Date'),Format(FUdate,'Short Date') FROM purchaseorders;");
                    
    ResultSet rs = 
            con.createStatement().executeQuery(
                    "SELECT po.purchase_order, pd.confirmed, pd.invoice_price, pd.landed_cost, b.brg_name, "
                            + "s.supplier_id, "
                            + "FORMAT(po.current_ship_date,'Short Date'), "
                            + "FORMAT(po.fu_date,'Short Date'), po.fu_notes,"
                            + "FORMAT(pd.landed_cost, 'standard') "
                            + ""
                            + "FROM purchase_orders po, order_details pd, bearings b, suppliers s "
                            + ""
                            + "WHERE po.purchase_order = pd.purchase_order AND pd.brg_id = b.brg_id "
                            + "AND b.supplier_id = s.supplier_id;");
                   
                    while(rs.next()){

                        String confirmation;
                        if(rs.getString("confirmed").equals("TRUE"))
                            confirmation = "YES";
                        else
                            confirmation = "NO";
                        
                       obList.add(new 
                               ModelOverviewTable(confirmation,
                               rs.getString("purchase_order"),
                               rs.getString("brg_name"),//CHANGE TO name
                               rs.getString("supplier_id"),
                               rs.getString(7),//current ship
                               rs.getString(8),//FU date
                               "add FU-NOTE",
                               IPformat.format(Double.parseDouble(rs.getString("invoice_price"))),
                               LCformat.format(Double.parseDouble(rs.getString("landed_cost")))));   
                       
//                       System.out.println("LC FORMAT: " + LCformat.format(Double.parseDouble(rs.getString("landed_cost"))));
                   }
                        
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(OverviewViewTesting_NOANCHORController.class.getName()).log(Level.SEVERE, null, ex);
        }
        

    }

//##############################################################################
    public void searchPO() {
                //MAIN TABLE-begin filtering
        FilteredList<ModelOverviewTable> filterData = new FilteredList<>(obList,b->true);
        POTextField.textProperty().addListener((observable,oldValue,newValue)->{
            filterData.setPredicate(po -> {
                
                if(newValue == null || newValue.isEmpty())
                    return true;
                
                String lowercaseFilter = newValue.toLowerCase();
                
                if (po.getPo().toLowerCase().contains(lowercaseFilter) ) {
                    return true; // Filter matches username
                }
                else
                    return false;
                
            });
            
        });
        
        SortedList<ModelOverviewTable> sortedData = new SortedList<>(filterData);
        sortedData.comparatorProperty().bind(OverviewTableView.comparatorProperty());
                
        OverviewTableView.setItems(sortedData);   
    }    
//##############################################################################
    public void addButtonsToTable(){
        
        //BUTTONcolumn
        Callback<TableColumn<ModelOverviewTable, String>, TableCell<ModelOverviewTable, String>> cellFactory;
        
        cellFactory = new Callback<TableColumn<ModelOverviewTable, String>,TableCell<ModelOverviewTable, String>>(){
            @Override
            public TableCell<ModelOverviewTable, String> call(TableColumn<ModelOverviewTable, String> param) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                final TableCell<ModelOverviewTable, String> cell = new TableCell<ModelOverviewTable, String>(){
                    
                    private final Button btn = new Button("View\nNotes");
                    {
                        btn.setMaxHeight(50);
                        btn.setMaxWidth(100);
                        btn.setStyle("-fx-font-size: 18px;-fx-font-weight: bold;\n" 
                                + "-fx-font-family: Georgia;");
                    }
                    Dialog<String> dialog = new Dialog<String>();
                    {
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
                            +"\nFeature for MULTIPLE USERS in progress");
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
    }
    
    private void setGreenText(){
        
        Callback<TableColumn<ModelOverviewTable, String>, TableCell<ModelOverviewTable, String>> cellFactory;
        
        cellFactory = new Callback<TableColumn<ModelOverviewTable, String>,TableCell<ModelOverviewTable, String>>(){
            @Override
            public TableCell<ModelOverviewTable, String> call(TableColumn<ModelOverviewTable, String> param) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                final TableCell<ModelOverviewTable, String> cell = new TableCell<ModelOverviewTable, String>(){
                    
                    
                    @Override
                    public void updateItem(String item, boolean empty){
                        super.updateItem(item, empty);
                        if(empty){
                            setGraphic(null);
                        }else{
                            
                        }
                    }
                    
                };
                return cell;
            }
        };
    }

}
    