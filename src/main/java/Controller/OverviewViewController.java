/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.BMSPurchaseOrderModel;
import Model.ConnectionUtil;
import Model.FileHelper;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Map;
//import java.time.Month;
//import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
//import javafx.application.Platform;
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
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
/**
 * FXML Controller class
 *
 * @author Gilbert Alvarado
 */
public class OverviewViewController implements Initializable {

    @FXML
    private TextField SUPPLIERTextField,POTextField,BRGTextField;
    @FXML
    private ComboBox<String> CONFIRMEDcomboBox;

    @FXML
    private DatePicker curShipSTART,curShipEND;
    
    private static DecimalFormat LCformat = new DecimalFormat("$###,###,##0.00");
    private static DecimalFormat IPformat = new DecimalFormat("$###,###,##0.000");
    //--------------------------------------------------------------------------
    @FXML
    private TableColumn<BMSPurchaseOrderModel, String> CONFIRMEDcolumn, POcolumn,BRGcolumn,
            SUPPLIERcolumn, CURSHIP_column, DATEcolumn, NOTEScolumn;
    @FXML
    private TableColumn<BMSPurchaseOrderModel, String> IPcolumn, LCcolumn;
    
    private ObservableList<BMSPurchaseOrderModel> obListMasterData = FXCollections.observableArrayList();
    
    @FXML
    private TableView<BMSPurchaseOrderModel> OverviewTableView;
    
    ObjectProperty<Predicate<BMSPurchaseOrderModel>> 
            supplierFilter, poFilter, brgFilter,dateFilter,confirmedFilter;
    
    FilteredList<BMSPurchaseOrderModel> filterData;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        CONFIRMEDcomboBox.getItems().addAll("YES","NO","VIEW ALL");
        CONFIRMEDcomboBox.setEditable(false);
        CONFIRMEDcomboBox.getSelectionModel().select("VIEW ALL");
        
//        clearFieldsBUTTON.setStyle("-fx-font-size: 14px;-fx-font-weight: bold;\n" 
//                                + "-fx-font-family: Georgia;");
        //----------------------------------------------------------------------
        CONFIRMEDcolumn.setCellValueFactory(new PropertyValueFactory<>("confirmed"));
        POcolumn.setCellValueFactory(new PropertyValueFactory<>("purchase_order"));
        BRGcolumn.setCellValueFactory(new PropertyValueFactory<>("brg_number"));
        SUPPLIERcolumn.setCellValueFactory(new PropertyValueFactory<>("supplier"));
        CURSHIP_column.setCellValueFactory(new PropertyValueFactory<>("curDateFormat"));
        DATEcolumn.setCellValueFactory(new PropertyValueFactory<>("fuDateFormat"));
        
        IPcolumn.setCellValueFactory(new PropertyValueFactory<>("invoice_price"));
        LCcolumn.setCellValueFactory(new PropertyValueFactory<>("landing_cost"));
        
        updateTableView();
            
    }
//##############################################################################    

    public void updateTableView(){
        obListMasterData.removeAll(obListMasterData);
        OverviewTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
//        OverviewTableView.setStyle("-fx-selection-bar-non-focused: green; -fx-selection-bar: brown;");
        for(Map.Entry<String, BMSPurchaseOrderModel> entry : ConnectionUtil.getAllData().entrySet())
            obListMasterData.add(entry.getValue());
        addButtonsToTable();
        setupPredicates();
        setFilterType();
        for (TableColumn<BMSPurchaseOrderModel, ?> column : OverviewTableView.getColumns()) {
            addTooltipToColumnCells(column);
        }
    }
    
    //###########################################################################
    
    private void setupPredicates(){
        
        filterData = new FilteredList<>(obListMasterData, p -> true);
        
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

            final LocalDate finalMin = minDate == null ? LocalDate.MIN : minDate;
            final LocalDate finalMax = maxDate == null ? LocalDate.MAX : maxDate;

            return search_field ->!finalMin.isAfter(LocalDate.parse( search_field.getCurrent_ship_date().toString())) 
                    && !finalMax.isBefore(LocalDate.parse( search_field.getCurrent_ship_date().toString() ) );
        },
            curShipSTART.valueProperty(),
            curShipEND.valueProperty()));
        //--------------------------------------------------------------------    
        confirmedFilter = new SimpleObjectProperty<>();
        confirmedFilter.bind(Bindings.createObjectBinding(() ->
                
                confirmed -> confirmed.getConfirmed().equals(CONFIRMEDcomboBox.getValue())                
                , CONFIRMEDcomboBox.valueProperty()));
        //----------------------------------------------------------------------
        
        SortedList<BMSPurchaseOrderModel> sortedData = new SortedList<>(filterData);
        sortedData.comparatorProperty().bind(OverviewTableView.comparatorProperty());
        OverviewTableView.setItems(sortedData);
    }
    
//##############################################################################
//                  BUTTONS- OPEN DIALOG/VIEW NOTES
//##############################################################################
    
    public void addButtonsToTable(){
        
        //BUTTONcolumn
        Callback<TableColumn<BMSPurchaseOrderModel, String>, TableCell<BMSPurchaseOrderModel, String>> cellFactory;
        
        cellFactory = new Callback<TableColumn<BMSPurchaseOrderModel, String>,TableCell<BMSPurchaseOrderModel, String>>(){
            @Override
            public TableCell<BMSPurchaseOrderModel, String> call(TableColumn<BMSPurchaseOrderModel, String> param) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                final TableCell<BMSPurchaseOrderModel, String> cell = new TableCell<BMSPurchaseOrderModel, String>(){
                    
                    private final Button btn = new Button("View\nNotes");
                    {
                        btn.setMaxHeight(50);
                        btn.setMaxWidth(100);
                        btn.setAlignment(Pos.CENTER);
                        btn.setStyle("-fx-font-size: 13px;-fx-font-weight: bold;\n" 
                                + "-fx-font-family: Georgia; -fx-background-color : #474747;"
                                + "-fx-text-fill : #a4ab1b;");
                    }
                    Dialog<String> dialog = new Dialog<>();
                    {
                            dialog.setTitle("Overview DIALOG");
                            ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
                            dialog.getDialogPane().getButtonTypes().add(type);
                    }
                    
                    //##########################################################
                    // LOAD NOTES VIEW ON ACTION
                    //##########################################################
                    @Override
                    public void updateItem(String item, boolean empty){
                        super.updateItem(item, empty);
                        if(empty){
                            setGraphic(null);
                        }else{
                            //##################################################
                            //  OPEN FORM/DIALOG ON ACTION
                            //##################################################
                            btn.setOnAction(event -> {
                            
                            BMSPurchaseOrderModel selected_item = getTableView().getItems().get(getIndex());
                                
                            System.out.println("FETCHING FU FILE");
                            
                            //##################################################
                            //  GET/CREATE NOTES
                            //##################################################
                            File tempFile = null;
                            if(FileHelper.getFUFile( selected_item.getPurchase_order()).isFile() ){
                                System.out.println("FILE EXISTS");
                                tempFile = FileHelper.getFUFile(selected_item.getPurchase_order());   
                            }
                            else {
                                System.out.println("CREATING FILES REWUIRED TO OPERATE NOTES");
                                FileHelper.createDirectory(selected_item.getPurchase_order());
                                FileHelper.createPoAttachmentsDirectory(selected_item.getPurchase_order());
                                FileHelper.createCSVfiles(selected_item.getPurchase_order());//CREATE AND UPDATE TABLE
                                
                                if(FileHelper.getFUFile(selected_item.getPurchase_order()).isFile())
                                    tempFile = FileHelper.getFUFile(selected_item.getPurchase_order());
                                
                            }
                            //##################################################
                            //  OPEN FORM/DIALOG
                            //##################################################
                            FXMLLoader notes_loader = new FXMLLoader (getClass().getResource("/View/Overview/NotesView.fxml"));
                            try {
                                
                                Parent parent  = notes_loader.load();
                                Scene scene = new Scene(parent, 600, 800);
                                Stage stage = new Stage();
                                stage.setMinWidth(600);
                                stage.setMinHeight(800);
                                stage.setResizable(false);
                                stage.initModality(Modality.APPLICATION_MODAL);
                                stage.setScene(scene);
                                NotesViewController controller = notes_loader.getController();
                                
                                if(tempFile!=null)
                                    controller.setContext(tempFile, selected_item);
                                
                                stage.showAndWait();
                            } catch (IOException ex) {
                                    Logger.getLogger(OverviewViewController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            
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
    //##############################################################################
    
    private void addMultipleSearchFilters(){
        
        filterData.predicateProperty().bind(Bindings.createObjectBinding(
            () -> supplierFilter.get().and(poFilter.get().and(brgFilter.get().
                    and(dateFilter.get().and(confirmedFilter.get())))),
                supplierFilter, poFilter, brgFilter, dateFilter, confirmedFilter));
        
    }
    
    //##########################################################################
    private void viewAllItems(){
        
        filterData.predicateProperty().bind(Bindings.createObjectBinding(
                () -> supplierFilter.get().and(poFilter.get().and(brgFilter.get().and(dateFilter.get()))), 
                supplierFilter, poFilter, brgFilter, dateFilter));
        
    }
    
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

    @FXML
    private void setFilterType() {
        
        if(CONFIRMEDcomboBox.getValue().equals("VIEW ALL"))
            viewAllItems();          
        else
            addMultipleSearchFilters();
        
    }
}
    