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
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
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
    
    private final ObservableList<BMSPurchaseOrderModel> obList_AllData = FXCollections.observableArrayList();
    //##########################################################################

    private MainLayoutController instance;    
    
    
    //KEY = PO
    //VALUE = fetch data from Model OR 
    private LinkedHashMap<String, BMSPurchaseOrderModel> selected_po_map;
    FilteredList<BMSPurchaseOrderModel> filterData;
    ObjectProperty<Predicate<BMSPurchaseOrderModel>> 
            supplierFilter, poFilter, brgFilter,dateFilter,confirmedFilter;
    @FXML
    private GridPane mainGridPaneLayout;
    @FXML
    private GridPane searchRowGridPane;
    @FXML
    private Button clearFieldsBUTTON;
    @FXML
    private Button helpButton;
    final Tooltip tooltip = new Tooltip();

    
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
        
        tooltip.setText(
            "select rows from the table\n" +
            "to create a report.\n"+ 
            "to select multiple rows, press/hold Ctrl.\n"+
            "to select all rows, press/hold Ctrl + 'A'.");
   
        helpButton.setTooltip(
            tooltip
        );
        
        updateTableView();
        printReportBUTTON.disableProperty().bind(Bindings.isEmpty(ManageDBTable.getSelectionModel().getSelectedItems())); 
        
    }
    
    //##########################################################################
    
    public void setMainLayoutContoller(MainLayoutController instance){
        this.instance = instance;
    }
    //##########################################################################
    public void updateTableView(){
        
        obList_AllData.removeAll(obList_AllData);
        ManageDBTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        for(BMSPurchaseOrderModel data : ConnectionUtil.getAllData().values())  
            obList_AllData.add(data);
        addButtonsToTable();
        setupPredicates();
        setFilterType();
        for (TableColumn<BMSPurchaseOrderModel, ?> column : ManageDBTable.getColumns()) {
            addTooltipToColumnCells(column);
        }
    }
    //##########################################################################
    private void addButtonsToTable(){
  
        //BUTTONcolumn
        Callback<TableColumn<BMSPurchaseOrderModel, String>, TableCell<BMSPurchaseOrderModel, String>> cellFactory;
        
        cellFactory = (TableColumn<BMSPurchaseOrderModel, String> param) -> {
            final TableCell<BMSPurchaseOrderModel, String> cell = new TableCell<BMSPurchaseOrderModel, String>() 
            {
                private final Button edit_button = new Button("Edit");
                {
                    edit_button.setMaxHeight(50);
                    edit_button.setMaxWidth(100);
                    edit_button.setStyle("-fx-font-size: 14px;-fx-font-weight: bold;\n"
                            + "-fx-font-family: Georgia;");
                    edit_button.setStyle("-fx-font-size: 13px;-fx-font-weight: bold;\n" 
                                + "-fx-font-family: Georgia; -fx-background-color : #474747;"
                                + "-fx-text-fill : #a4ab1b;");
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
        };
        BUTTONcolumn.setCellFactory(cellFactory);        
    }
    
    private <T> void addTooltipToColumnCells(TableColumn<BMSPurchaseOrderModel,T> column) {

        Callback<TableColumn<BMSPurchaseOrderModel, T>, TableCell<BMSPurchaseOrderModel,T>> existingCellFactory 
            = column.getCellFactory();

        column.setCellFactory(c -> {
            TableCell<BMSPurchaseOrderModel, T> cell = existingCellFactory.call(c);

            Tooltip tooltip = new Tooltip();
            // can use arbitrary binding here to make text depend on cell
            // in any way you need:
            tooltip.textProperty().bind(cell.itemProperty().asString());

            cell.setTooltip(tooltip);
            return cell ;
        });
    }
    //##########################################################################
    //          FILTER 
    //##########################################################################
    
    private void setupPredicates(){
                    
        filterData = new FilteredList<>(obList_AllData);
        
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
        curShipSTART.setValue(null);
        curShipEND.setValue(null);
        CONFIRMEDcomboBox.getSelectionModel().select("VIEW ALL");
        setFilterType();
    }
    //##########################################################################
    //          BEGIN/LOAD REPORT
    //##########################################################################
    private List<BMSPurchaseOrderModel> selected_po_list;
    @FXML    
    private void printReportBUTTON(ActionEvent event) {
        selected_po_map = new LinkedHashMap<>();
        
        selected_po_list = new ArrayList<>();
        for(TablePosition<BMSPurchaseOrderModel,?> pos : ManageDBTable.getSelectionModel().getSelectedCells() ){
            int row = pos.getRow();
            BMSPurchaseOrderModel data = ManageDBTable.getItems().get(row);
            System.out.println("ROW SELECTED: " +row+ " | PO selected: " + data.getPurchase_order() );
//            selected_po_map.put(ManageDBTable.getItems().get(row).getPurchase_order(), ManageDBTable.getItems().get(row));  
            selected_po_map.put(data.getPurchase_order(), data);
            selected_po_list.add(data);
        }
        
        try {
            
            FXMLLoader loader = new FXMLLoader (getClass().getResource("/View/ManageDB/POReportSelectionView.fxml"));
            Parent parent  = loader.load();
            Scene scene = new Scene(parent, 1150 ,600);
            Stage stage = new Stage();
            stage.setMinWidth(1000);
            stage.setMinHeight(600);
//            stage.setMaxWidth(1150);
//            stage.setMaxHeight(750);            
            stage.setResizable(true);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            POReportSelectionViewController po_controller =loader.getController();
            
            //LocalDate minDate = curShipSTART.getValue();
            //LocalDate maxDate = curShipEND.getValue();
            
            po_controller.setSelecteditems(selected_po_map,obList_AllData,selected_po_list,
                    curShipSTART.getValue(),curShipEND.getValue());
            
            selected_po_map.clear();
            selected_po_list.clear();
            
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
    
    public ObservableList getObList(){
        return obList_AllData;
    }
    public List<BMSPurchaseOrderModel> getSelectedPoList(){
        return selected_po_list;
    }
    
}