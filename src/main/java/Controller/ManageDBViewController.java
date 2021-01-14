/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.ConnectionUtil;
import Model.ModelManageDBTable;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import javafx.scene.control.Dialog;
import javafx.scene.control.SelectionMode;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;



import net.ucanaccess.complex.Attachment;


/**
 * FXML Controller class
 *
 * @author Gilbert Alvarado
 */
public class ManageDBViewController implements Initializable {


    @FXML
    private TextField SUPPLIERTextField;
    @FXML
    private ComboBox<String> CONFIRMEDcomboBox;
    @FXML
    private TextField POTextField;
    @FXML
    private TextField BRGTextField;
    
    @FXML
    private TableView<ModelManageDBTable> ManageDBTable;
    @FXML
    private TableColumn<ModelManageDBTable, String> POcolumn, BRGcolumn, CURSHIP_column, BUTTONcolumn,
            STATUScolumn, ATTACHMENTScolumn;// PACKETcolumn;

    ObservableList<ModelManageDBTable> obList = FXCollections.observableArrayList();
    
    private EditPOView_NOANCHORController controller;//used to setItems in editPOView
    
    private FXMLLoader loader;
    private AnchorPane editPOAnchor;
    private MainLayoutTesting_WITHANCHORController instance;
    @FXML
    private TableColumn<ModelManageDBTable, Boolean> PACKETcolumn;
    
    @FXML
    private DatePicker curShipSTART, curShipEND;
    @FXML
    private Button clearFieldsBUTTON;
    @FXML
    private Button addToReportBUTTON;
    @FXML
    private Button printReportBUTTON;
    
    
    //KEY = PO
    //VALUE = fetch data from Model OR 
    private LinkedHashMap<String, ModelManageDBTable> selectedPos;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        ManageDBTable.setEditable(true);
        
//        CONFIRMEDcomboBox.getItems().addAll("YES","NO");
//        CONFIRMEDcomboBox.setEditable(false);
//        CONFIRMEDcomboBox.getSelectionModel().select("YES");
        POcolumn.setCellValueFactory(new PropertyValueFactory<>("po"));
        BRGcolumn.setCellValueFactory(new PropertyValueFactory<>("brg"));
        CURSHIP_column.setCellValueFactory(new PropertyValueFactory<>("cur"));
        STATUScolumn.setCellValueFactory(new PropertyValueFactory<>("supplier"));//replce with SUPPLIER,was confirmed
        ATTACHMENTScolumn.setCellValueFactory(new PropertyValueFactory<>("attachment"));
        
        
        PACKETcolumn.setCellValueFactory(new PropertyValueFactory<>("packet"));
//        checkBoxColumn.setCellValueFactory(new PropertyValueFactory<>("packet"));
//checkBoxColumn.setCellValueFactory(new PropertyValueFactory<ModelManageDBTable, Boolean>("packet"));


        updateTableView();
        addButtonsToTable();

//        addPacketControls();

//addCheckboxControls();

addCheckBoxTest();

//        addSearchFilters();
addMultipleSearchFilters();
            
    }    
    //##########################################################################  
    public void setEditPOController(EditPOView_NOANCHORController controller){
        this.controller = controller;
    }
    public void setEditPOLoader(FXMLLoader loader){
        this.loader = loader;
    }
    public void setAnchorEditPO(AnchorPane editPOAnchor){
        this.editPOAnchor = editPOAnchor;
    }
    public void setMainLayoutInstance(MainLayoutTesting_WITHANCHORController instance){
        this.instance = instance;
    }
    //##########################################################################
    public void updateTableView(){
        ManageDBTable.getItems().clear();
        ManageDBTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
     try{
            Connection con = ConnectionUtil.conDB();
            
            ResultSet rs =con.createStatement().executeQuery(
            "SELECT purchase_orders.purchase_order, "
                    + "bearings.supplier_id, "
                    + "bearings.brg_name, "
                    + "purchase_orders.current_ship_date, "//4
                    + "order_details.attachments, "//5
                    + "order_details.moved_to_packet \n" 
           +"FROM purchase_orders "
                    + "INNER JOIN "
                    + "(bearings INNER JOIN order_details ON bearings.[brg_id] = order_details.[brg_id]) "
                    + "ON purchase_orders.[purchase_order] = order_details.[purchase_order];");
            
            ModelManageDBTable dbItem;
            SimpleDateFormat MMddyyyy = new SimpleDateFormat("MM/dd/yyyy");
            
            
            while(rs.next()){
                
//                System.out.println("MMddyyyy TABLE DATE FORMAT: "+MMddyyyy.format(rs.getDate(4)));
//                System.out.println("rs.getDate() FORMAT: "+rs.getDate(4));
                
                String moved_to_packet;
                if(rs.getString("moved_to_packet").equals("TRUE"))
                    moved_to_packet = "YES";
                else
                    moved_to_packet = "NO";
                        
                dbItem = new 
                ModelManageDBTable(
                        rs.getString("purchase_order"),
                        rs.getString("supplier_id"),
                        rs.getString("brg_name"),
                        MMddyyyy.format(rs.getDate(4)),//CUR SHIP
                        moved_to_packet,
                        rs.getDate(4));
                
                Attachment att []= (Attachment[])rs.getObject(5);
                dbItem.setAtt(att);
                dbItem.displayAttachmentCHANGE();
                dbItem.setAttachment(String.valueOf(dbItem.getNumberAttachments()));
                obList.add(dbItem);

            }
            con.close();

           
           //###################################################################
        } catch (SQLException ex) {
            Logger.getLogger(ManageDBViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    //##########################################################################
    private void addButtonsToTable(){
  
        //BUTTONcolumn
        Callback<TableColumn<ModelManageDBTable, String>, TableCell<ModelManageDBTable, String>> cellFactory;
        
        cellFactory = new Callback<TableColumn<ModelManageDBTable, String>,TableCell<ModelManageDBTable, String>>(){
            @Override
            public TableCell<ModelManageDBTable, String> call(TableColumn<ModelManageDBTable, String> param) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                final TableCell<ModelManageDBTable, String> cell = new TableCell<ModelManageDBTable, String>(){
                    
//                    ModelManageDBTable dbItem = getTableView().getItems().get(getIndex());
                    
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
                            //ACTION EVENT FOR BUTTON/ROW SELECTED
                            
                                setGraphic(edit_button);
                                
                                
                                edit_button.setOnAction((ActionEvent event) -> {

                                ModelManageDBTable data = getTableView().getItems().get(getIndex());

                                alert.setContentText("Edit " + data.getPo() + "?\n");

    //                            alert.showAndWait();
                                Optional<ButtonType> result = alert.showAndWait();
                                //goto EditPOView
                                if (result.orElse(NO) == YES){
                                    controller.setItems(data);//EditPOView controller
                                    instance.editToTop();//MainLayoutTesting_WITHANCHORController
                                    
                                    
                                   
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
    
    private void addPacketControls(){
//        Callback<TableColumn<ModelManageDBTable, String>, TableCell<ModelManageDBTable, String>> cellFactory;
//        
//        cellFactory = new Callback<TableColumn<ModelManageDBTable, String>,TableCell<ModelManageDBTable, String>>(){
//            @Override
//            public TableCell<ModelManageDBTable, String> call(TableColumn<ModelManageDBTable, String> param) {
//                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//                final TableCell<ModelManageDBTable, String> cell = new TableCell<ModelManageDBTable, String>(){
//      
//                    private final CheckBox checkBox = new CheckBox();
//                    
//                    ButtonType YES = new ButtonType("YES", ButtonBar.ButtonData.YES);
//                    ButtonType NO = new ButtonType("NO", ButtonBar.ButtonData.CANCEL_CLOSE);                     
//                    Alert alert = new Alert(AlertType.CONFIRMATION);
//                    {
//                        alert.setTitle("Manage DIALOG");
//                        alert.getButtonTypes().clear();
//                        alert.getDialogPane().getButtonTypes().addAll( YES, NO );
//                    }          
//                    private final Button report_button = new Button("report");
//                    {
//                        report_button.setMaxHeight(50);
//                        report_button.setMaxWidth(100);
//                        report_button.setStyle("-fx-font-size: 14px;-fx-font-weight: bold;\n" 
//                                + "-fx-font-family: Georgia;");
//                    }
//                    
//                    
//                    @Override
//                    public void updateItem(String item, boolean empty){
//                        super.updateItem(item, empty);
//                        if(empty){
//                            setGraphic(null);
//                        }else{
//                            //ACTION EVENT FOR BUTTON/ROW SELECTED
//                            ModelManageDBTable data = getTableView().getItems().get(getIndex());
//                            
//                            HBox b = new HBox(checkBox, report_button);
//                            b.setAlignment(Pos.CENTER);
//                            b.setSpacing(30);
//                            
//                            setGraphic(b);
//                            
//                            if(data.getPacket().equals("YES")){// && data.getConfirmed().equals("YES")){
//                                checkBox.selectedProperty().setValue(true);
//                                report_button.setDisable(false);
//                            }else{
//                                checkBox.selectedProperty().set(false);
//                                report_button.setDisable(true);
//                            }
//                            
//                            
//                            //##################################################
//                            //          UPDATE CHECKBOX HERE
//                            //##################################################
//                            checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
//
//                                @Override
//                                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//                                    // TODO Auto-generated method stub
//                                    
//                                    String newPacketValue = (newValue == true) ? "TRUE" : "FALSE";
//                                    if(newValue){
//                                        
//                                        if(ConnectionUtil.updatePacketStatus(data.getPo(), newPacketValue) ==1){
////                                        report_button.setDisable(false);
//System.out.println("VALUE SHOULD CHANGE, CHECK ACCESS");
//                                        update();}
//                                    }else{
//                                        if(ConnectionUtil.updatePacketStatus(data.getPo(), newPacketValue) ==1){
////                                        report_button.setDisable(true);
//System.out.println("FAIL");
//                                        update();}
//                                    }
//                                }
//                            });
//                            
//                                report_button.setOnAction((ActionEvent event) -> {
//
//                                alert.setContentText("Create report for " + data.getPo() + "?\n");
//
//    //                            alert.showAndWait();
//                                Optional<ButtonType> result = alert.showAndWait();
//                                //goto EditPOView
//                                if (result.orElse(NO) == YES){
//                                    Dialog<String> dialog = new Dialog<String>();
//      //Setting the title
//      dialog.setTitle("Dialog");
//      ButtonType type = new ButtonType("Ok", ButtonData.OK_DONE);
//      //Setting the content of the dialog
//      dialog.setContentText("report created, press ok to view");
//      //Adding buttons to the dialog pane
//      dialog.getDialogPane().getButtonTypes().add(type);
//       dialog.showAndWait();
//                                }
//                            
//                        });
//                                                                
//                        }
//                    }
//                };
//                return cell;
//            }
//        };
//        PACKETcolumn.setCellFactory(cellFactory);        
    }
    
    //##########################################################################
    
    private void addCheckBoxTest(){
        
        PACKETcolumn.setCellValueFactory(
new Callback<CellDataFeatures<ModelManageDBTable,Boolean>,ObservableValue<Boolean>>()
{
    //This callback tell the cell how to bind the data model 'Registered' property to
    //the cell, itself.
    @Override
    public ObservableValue<Boolean> call(CellDataFeatures<ModelManageDBTable, Boolean> param)
    {
        System.out.println("SELECTED PO VALUE: "+ param.getValue().getPo() + ", PACKET VALUE: " + param.getValue().getPacket());
        return param.getValue().registeredProperty();
    }   
});
        
        PACKETcolumn.setCellFactory( CheckBoxTableCell.forTableColumn(PACKETcolumn) );
        
    }
    
    //##########################################################################
    
    private void addCheckboxControls(){
        Callback<TableColumn<ModelManageDBTable, Boolean>, TableCell<ModelManageDBTable, Boolean>> cellFactory;
        
        cellFactory = new Callback<TableColumn<ModelManageDBTable, Boolean>,TableCell<ModelManageDBTable, Boolean>>(){
            @Override
            public TableCell<ModelManageDBTable, Boolean> call(TableColumn<ModelManageDBTable, Boolean> param) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                final TableCell<ModelManageDBTable, Boolean> cell = new TableCell<ModelManageDBTable, Boolean>(){
      
                    private final CheckBox checkBox = new CheckBox();
                    
                    ButtonType YES = new ButtonType("YES", ButtonBar.ButtonData.YES);
                    ButtonType NO = new ButtonType("NO", ButtonBar.ButtonData.CANCEL_CLOSE);                     
                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    {
                        alert.setTitle("Manage DIALOG");
                        alert.getButtonTypes().clear();
                        alert.getDialogPane().getButtonTypes().addAll( YES, NO );
                    }          
                    
                    
                    
                    @Override
                    public void updateItem(Boolean item, boolean empty){
                        super.updateItem(item, empty);
                        if(empty){
                            setGraphic(null);
                        }else{
                            //ACTION EVENT FOR BUTTON/ROW SELECTED
                            ModelManageDBTable data = getTableView().getItems().get(getIndex());
                            
                            
                            
                            setGraphic(checkBox);
                            
                            if(data.getPacket()){// && data.getConfirmed().equals("YES")){
                                checkBox.selectedProperty().setValue(true);
                            }else{
                                checkBox.selectedProperty().set(false);
                            }
                            
                            
                            //##################################################
                            //          UPDATE CHECKBOX HERE
                            //##################################################
                            checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {

                                @Override
                                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                                    // TODO Auto-generated method stub
                                    
                                    String newPacketValue = (newValue == true) ? "TRUE" : "FALSE";
                                    if(newValue){
                                        if(ConnectionUtil.updatePacketStatus(data.getPo(), newPacketValue) ==1){
//                                        
System.out.println("VALUE SHOULD CHANGE, CHECK ACCESS");
                                        update();}
                                    }else{
                                        if(ConnectionUtil.updatePacketStatus(data.getPo(), newPacketValue) ==1){
//                                        report_button.setDisable(true);
System.out.println("FAIL");
                                        update();}
                                    }
                                }
                            });
                                                                
                        }
                    }
                };
                return cell;
            }
        };
        
        PACKETcolumn.setCellFactory(cellFactory);        
    }
    
    //##########################################################################
    private void update(){
        obList.clear();
        ManageDBTable.getItems().clear();
        updateTableView();
        addButtonsToTable();
//        addPacketControls();
//addCheckboxControls();

    }
    private boolean updatePo(String purchase_order, String moved_to_packet){
        return (ConnectionUtil.updatePacketStatus(purchase_order, moved_to_packet) == 1);
    }
    //##############################################################################
    public void addSearchFilters() {
                
        FilteredList<ModelManageDBTable> filterData = new FilteredList<>(obList,b->true);
        SortedList<ModelManageDBTable> sortedData = new SortedList<>(filterData);
        sortedData.comparatorProperty().bind(ManageDBTable.comparatorProperty());
        ManageDBTable.setItems(sortedData); 
        
        filterData.predicateProperty().bind(Bindings.createObjectBinding(() -> {
        
            LocalDate minDate = curShipSTART.getValue();
            LocalDate maxDate = curShipEND.getValue();

            // get final values != null
            final LocalDate finalMin = minDate == null ? LocalDate.MIN : minDate;
            final LocalDate finalMax = maxDate == null ? LocalDate.MAX : maxDate;

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d"); 

    //        SimpleDateFormat sdp = new SimpleDateFormat("yyyy-MM-dd");//LocalDatePattern

            // values for openDate need to be in the interval [finalMin, finalMax]
            return search_field -> search_field.getSupplier().contains(SUPPLIERTextField.getText())
               && search_field.getPo().contains(POTextField.getText()) 
                && search_field.getBrg().contains(BRGTextField.getText())
                    && !finalMin.isAfter(LocalDate.parse( search_field.getOriginalDate().toString())) 
                    && !finalMax.isBefore(LocalDate.parse( search_field.getOriginalDate().toString()));
        },

            SUPPLIERTextField.textProperty(),
            POTextField.textProperty(),
            BRGTextField.textProperty(),
            curShipSTART.valueProperty(),
            curShipEND.valueProperty()

        ));
        
    }
    //##########################################################################
    //          NEW FILTER 
    //##########################################################################
    FilteredList<ModelManageDBTable> filterData;
        private void addMultipleSearchFilters(){
        ObjectProperty<Predicate<ModelManageDBTable>> supplierFilter = new SimpleObjectProperty<>();
        supplierFilter.bind(Bindings.createObjectBinding(() -> 
            supplier -> supplier.getSupplier().toLowerCase().contains(SUPPLIERTextField.getText().toLowerCase()), 
            SUPPLIERTextField.textProperty()));
        //----------------------------------------------------------------------
        ObjectProperty<Predicate<ModelManageDBTable>> poFilter = new SimpleObjectProperty<>();
        poFilter.bind(Bindings.createObjectBinding(() -> 
            po -> po.getPo().toLowerCase().contains(POTextField.getText().toLowerCase()), 
            POTextField.textProperty()));
        //----------------------------------------------------------------------
        ObjectProperty<Predicate<ModelManageDBTable>> brgFilter = new SimpleObjectProperty<>();
        brgFilter.bind(Bindings.createObjectBinding(() -> 
            brg -> brg.getBrg().toLowerCase().contains(BRGTextField.getText().toLowerCase()), 
            BRGTextField.textProperty()));
        //----------------------------------------------------------------------
        ObjectProperty<Predicate<ModelManageDBTable>> dateFilter = new SimpleObjectProperty<>();
        dateFilter.bind(Bindings.createObjectBinding(() -> {
        
            LocalDate minDate = curShipSTART.getValue();
            LocalDate maxDate = curShipEND.getValue();

            // get final values != null
            final LocalDate finalMin = minDate == null ? LocalDate.MIN : minDate;
            final LocalDate finalMax = maxDate == null ? LocalDate.MAX : maxDate;

            return search_field ->!finalMin.isAfter(LocalDate.parse( search_field.getOriginalDate().toString())) 
                    && !finalMax.isBefore(LocalDate.parse( search_field.getOriginalDate().toString() ) );
        },
            curShipSTART.valueProperty(),
            curShipEND.valueProperty()));
//        //--------------------------------------------------------------------    
//        ObjectProperty<Predicate<ModelManageDBTable>> confirmedFilter = new SimpleObjectProperty<>();
//        confirmedFilter.bind(Bindings.createObjectBinding(() ->
//                
//                confirmed -> confirmed.getConfirmed().equals(CONFIRMEDcomboBox.getValue())                
//                , CONFIRMEDcomboBox.valueProperty()));
        
        //----------------------------------------------------------------------
        
        filterData = new FilteredList<>(FXCollections.observableList(obList));
        ManageDBTable.setItems(filterData);
        
        filterData.predicateProperty().bind(Bindings.createObjectBinding(
                () -> supplierFilter.get().and(poFilter.get().and(brgFilter.get().and(dateFilter.get()))), 
                supplierFilter, poFilter, brgFilter, dateFilter));
               
        
    }

    @FXML
    private void clearFields(ActionEvent event) {
        SUPPLIERTextField.clear();
        POTextField.clear();
        BRGTextField.clear();
        curShipSTART.getEditor().clear();
        curShipEND.getEditor().clear();
        CONFIRMEDcomboBox.setValue("NO");
    }

    @FXML
    private void printReportBUTTON(ActionEvent event) {
        selectedPos = new LinkedHashMap<>();
        for(TablePosition<ModelManageDBTable,?> pos : ManageDBTable.getSelectionModel().getSelectedCells()){
            int row = pos.getRow();
            ModelManageDBTable data = ManageDBTable.getItems().get(row);
            System.out.println("PO selected: " + data.getPo() );
            selectedPos.put(ManageDBTable.getItems().get(row).getPo(), ManageDBTable.getItems().get(row));
        }
        
        try {
            
            //open view and use FIleChooser in the new view
            FXMLLoader loader = new FXMLLoader (getClass().getResource("/View/ManageDB/POReportSelectionView.fxml"));
            Parent parent  = loader.load();
            Scene scene = new Scene(parent, 600 ,400);
            Stage stage = new Stage();
            stage.setMinWidth(600);
            stage.setMinHeight(400);
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            POReportSelectionViewController controller =loader.getController();
//            controller.setSelecteditems(selectedPos);
            stage.showAndWait();

        } catch (IOException ex) {
            Logger.getLogger(MainLayoutTesting_WITHANCHORController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    class Row{
    
    //PO | SUPPLIER | BRG# | CUR SHIP | #ATT | (future) checkBox + reportButton | edit button
    //idea: remove edit button, double click item to prompt user to edit
    
    
    private String purchase_order, supplier, bearing, currentShip, number_attachments;
    
//    private List<String> rowData;
    
    public Row(String purchase_order, String supplier, String bearing, String currentShip, String number_attachments) {
        this.purchase_order = purchase_order;
        this.supplier = supplier;
        this.bearing = bearing;
        this.currentShip = currentShip;
        this.number_attachments = number_attachments;
    }

    public String getPurchase_order() {
        return purchase_order;
    }

    public void setPurchase_order(String purchase_order) {
        this.purchase_order = purchase_order;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getBearing() {
        return bearing;
    }

    public void setBearing(String bearing) {
        this.bearing = bearing;
    }

    public String getCurrentShip() {
        return currentShip;
    }

    public void setCurrentShip(String currentShip) {
        this.currentShip = currentShip;
    }

    public String getNumber_attachments() {
        return number_attachments;
    }

    public void setNumber_attachments(String number_attachments) {
        this.number_attachments = number_attachments;
    }
    
}
    
}

