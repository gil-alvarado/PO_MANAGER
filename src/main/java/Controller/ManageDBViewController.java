/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.BMSPurchaseOrderModel;
import Model.ConnectionUtil;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import javafx.scene.control.SelectionMode;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;



/**
 * FXML Controller class
 *
 * @author Gilbert Alvarado
 */
public class ManageDBViewController implements Initializable {

    //##########################################################################
    //          SEARCH FIELDS
    //##########################################################################
    @FXML
    private TextField SUPPLIERTextField;
    @FXML
    private ComboBox<String> CONFIRMEDcomboBox;
    @FXML
    private TextField POTextField;
    @FXML
    private TextField BRGTextField;
    @FXML
    private DatePicker curShipSTART, curShipEND;
    //##########################################################################
    //          TABLE FIELDS
    //##########################################################################    
    @FXML
    private TableView<BMSPurchaseOrderModel> ManageDBTable;
    @FXML
    private TableColumn<BMSPurchaseOrderModel, String> POcolumn, BRGcolumn, CURSHIP_column, BUTTONcolumn,
            STATUScolumn, ATTACHMENTScolumn;// PACKETcolumn
//    @FXML
//    private TableColumn<ModelManageDBTable, Boolean> PACKETcolumn;
    @FXML
    private Button printReportBUTTON;
    
    private final ObservableList<BMSPurchaseOrderModel> obList = FXCollections.observableArrayList();
    //##########################################################################

    private MainLayoutController instance;    
    
    //KEY = PO
    //VALUE = fetch data from Model OR 
    private LinkedHashMap<String, BMSPurchaseOrderModel> selectedPos;
    FilteredList<BMSPurchaseOrderModel> filterData;
    ObjectProperty<Predicate<BMSPurchaseOrderModel>> 
            supplierFilter, poFilter, brgFilter,dateFilter,confirmedFilter;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        ManageDBTable.setEditable(true);
        setInstance(this);
        CONFIRMEDcomboBox.getItems().addAll("YES","NO","VIEW ALL");
        CONFIRMEDcomboBox.setEditable(false);
        CONFIRMEDcomboBox.getSelectionModel().select("VIEW ALL");
        CONFIRMEDcomboBox.setDisable(false);
        
        POcolumn.setCellValueFactory(new PropertyValueFactory<>("purchase_order"));
        BRGcolumn.setCellValueFactory(new PropertyValueFactory<>("brg_number"));
        CURSHIP_column.setCellValueFactory(new PropertyValueFactory<>("curDateFormat"));
        STATUScolumn.setCellValueFactory(new PropertyValueFactory<>("supplier"));
        ATTACHMENTScolumn.setCellValueFactory(new PropertyValueFactory<>("attachmentLength"));
        printReportBUTTON.setDisable(true);
        
        updateTableView();
        printReportBUTTON.disableProperty().bind(Bindings.isEmpty(ManageDBTable.getSelectionModel().getSelectedItems()));            
    }
    
    //##########################################################################
    
    public void setMainLayoutContoller(MainLayoutController instance){
        this.instance = instance;
    }
    //##########################################################################
    public void updateTableView(){
        
        obList.removeAll(obList);
        ManageDBTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        for(BMSPurchaseOrderModel data : ConnectionUtil.getAllData().values())  
            obList.add(data);
        addButtonsToTable();
        setupPredicates();
        setFilterType();
        
    }
    //##########################################################################
    private void addButtonsToTable(){
  
        //BUTTONcolumn
        Callback<TableColumn<BMSPurchaseOrderModel, String>, TableCell<BMSPurchaseOrderModel, String>> cellFactory;
        
        cellFactory = new Callback<TableColumn<BMSPurchaseOrderModel, String>,TableCell<BMSPurchaseOrderModel, String>>(){
            @Override
            public TableCell<BMSPurchaseOrderModel, String> call(TableColumn<BMSPurchaseOrderModel, String> param) {
               final TableCell<BMSPurchaseOrderModel, String> cell = new TableCell<BMSPurchaseOrderModel, String>(){   
                    private final Button edit_button = new Button("Edit");
                    {
                        edit_button.setMaxHeight(50);
                        edit_button.setMaxWidth(100);
                        edit_button.setStyle("-fx-font-size: 14px;-fx-font-weight: bold;\n" 
                                + "-fx-font-family: Georgia;");
                    }      
                    
                    ButtonType YES = new ButtonType("YES", ButtonBar.ButtonData.YES);
                    ButtonType NO = new ButtonType("NO", ButtonBar.ButtonData.CANCEL_CLOSE);                     
                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    {
                        alert.setTitle("Manage DIALOG");
                        alert.getButtonTypes().clear();
                        alert.getDialogPane().getButtonTypes().addAll( YES, NO );
                    }          
                    @Override
                    public void updateItem(String item, boolean empty){
                        super.updateItem(item, empty);
                        if(empty){
                            setGraphic(null);
                        }else{
                            setGraphic(edit_button);

                            edit_button.setOnAction((ActionEvent event) -> {

                            BMSPurchaseOrderModel data = getTableView().getItems().get(getIndex());

                            alert.setContentText("Edit " + data.getPurchase_order() + "?\n");

                            Optional<ButtonType> result = alert.showAndWait();
                            //goto EditPOView
                            if (result.orElse(NO) == YES){
                                MainLayoutController.getEditPOController().setItems(data);//EditPOView controller
                                instance.openEditPOView(null);//MainLayoutTesting_WITHANCHORController

                            }
                            
                        });
                        }
                    }
                };
                return cell;
            }
        };
        BUTTONcolumn.setCellFactory(cellFactory);        
    }
    
    //##########################################################################
    //          FILTER 
    //##########################################################################
    
    private void setupPredicates(){
                    
        filterData = new FilteredList<>(obList);
        
        supplierFilter = new SimpleObjectProperty<>();
        supplierFilter.bind(Bindings.createObjectBinding(() -> 
            supplier -> supplier.getSupplier().toLowerCase().contains(SUPPLIERTextField.getText().toLowerCase()), 
            SUPPLIERTextField.textProperty()));
        //----------------------------------------------------------------------
        poFilter = new SimpleObjectProperty<>();
        poFilter.bind(Bindings.createObjectBinding(() -> 
            po -> po.getPurchase_order().toLowerCase().contains(POTextField.getText().toLowerCase()), 
            POTextField.textProperty()));
        //----------------------------------------------------------------------
        brgFilter = new SimpleObjectProperty<>();
        brgFilter.bind(Bindings.createObjectBinding(() -> 
            brg -> brg.getBrg_number().toLowerCase().contains(BRGTextField.getText().toLowerCase()), 
            BRGTextField.textProperty()));
        //----------------------------------------------------------------------
        dateFilter = new SimpleObjectProperty<>();
        dateFilter.bind(Bindings.createObjectBinding(() -> {
        
            LocalDate minDate = curShipSTART.getValue();
            LocalDate maxDate = curShipEND.getValue();

            // get final values != null
            final LocalDate finalMin = minDate == null ? LocalDate.MIN : minDate;
            final LocalDate finalMax = maxDate == null ? LocalDate.MAX : maxDate;

            return search_field ->!finalMin.isAfter(LocalDate.parse( search_field.getCurrent_ship_date().toString())) 
                    && !finalMax.isBefore(LocalDate.parse( search_field.getCurrent_ship_date().toString() ) );
        },
            curShipSTART.valueProperty(),
            curShipEND.valueProperty()));
        //----------------------------------------------------------------------  
        confirmedFilter = new SimpleObjectProperty<>();
        confirmedFilter.bind(Bindings.createObjectBinding(() ->
                
                confirmed -> confirmed.getConfirmed().equals(CONFIRMEDcomboBox.getValue())                
                , CONFIRMEDcomboBox.valueProperty()));
        
        //----------------------------------------------------------------------
        SortedList<BMSPurchaseOrderModel> sortedData = new SortedList<>(filterData);
        sortedData.comparatorProperty().bind(ManageDBTable.comparatorProperty());
        ManageDBTable.setItems(sortedData);
        
    }
    
    private void addMultipleSearchFilters(){
        
        filterData.predicateProperty().bind(Bindings.createObjectBinding(
                () -> supplierFilter.get().and(poFilter.get().and(brgFilter.get().and(dateFilter.get().and(confirmedFilter.get())))), 
                supplierFilter, poFilter, brgFilter, dateFilter,confirmedFilter));
    }
    //--------------------------------------------------------------------------
    //      "removed" comboBox Filter
    //--------------------------------------------------------------------------
    private void viewAllItems(){
        filterData.predicateProperty().bind(Bindings.createObjectBinding(
                () -> supplierFilter.get().and(poFilter.get().and(brgFilter.get().and(dateFilter.get()))), 
                supplierFilter, poFilter, brgFilter, dateFilter));
    }
    //##########################################################################
    //          END FILTER 
    //##########################################################################
    @FXML
    private void clearFields(ActionEvent event) {
        SUPPLIERTextField.clear();
        POTextField.clear();
        BRGTextField.clear();
        curShipSTART.getEditor().clear();
        curShipEND.getEditor().clear();
        CONFIRMEDcomboBox.setValue("VIEW ALL");
//        setFilterType();
    }
    //##########################################################################
    //          BEGIN/LOAD REPORT
    //##########################################################################
    private List<BMSPurchaseOrderModel> po;
    @FXML    
    private void printReportBUTTON(ActionEvent event) {
        selectedPos = new LinkedHashMap<>();
        
        po = new ArrayList<>();
        for(TablePosition<BMSPurchaseOrderModel,?> pos : ManageDBTable.getSelectionModel().getSelectedCells()){
            int row = pos.getRow();
            BMSPurchaseOrderModel data = ManageDBTable.getItems().get(row);
            System.out.println("ROW SELECTED: " +row+ " | PO selected: " + data.getPurchase_order() );
            selectedPos.put(ManageDBTable.getItems().get(row).getPurchase_order(), ManageDBTable.getItems().get(row));  
            po.add(data);
        }
        
        try {
            
            FXMLLoader loader = new FXMLLoader (getClass().getResource("/View/ManageDB/POReportSelectionView.fxml"));
            Parent parent  = loader.load();
            Scene scene = new Scene(parent, 1150 ,600);
            Stage stage = new Stage();
            stage.setMinWidth(1000);
            stage.setMinHeight(600);
            stage.setResizable(true);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            POReportSelectionViewController po_controller =loader.getController();
            
     
           
//            po_controller.setSelecteditems(selectedPos,obList,po);
            
            selectedPos.clear();
            stage.showAndWait();
            
        } catch (IOException ex) {
            Logger.getLogger(ManageDBViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void setFilterType() {
        
        if(CONFIRMEDcomboBox.getValue().equals("VIEW ALL"))
            viewAllItems();
        else
            addMultipleSearchFilters();
    }
    
    private static ManageDBViewController ManageInstance;
    private void setInstance(ManageDBViewController instance){
        ManageDBViewController.ManageInstance = instance;
    }
    
    public static ManageDBViewController getInstance(){
        return ManageInstance;
    }
    
    public ObservableList getList(){
        return obList;
    }
    public List<BMSPurchaseOrderModel> getSelectedList(){
        return po;
    }
}