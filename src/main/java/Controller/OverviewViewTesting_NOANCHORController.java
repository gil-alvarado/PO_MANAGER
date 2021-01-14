/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.ConnectionUtil;
import Model.FileHelper;
import Model.ModelOverviewTable;
import java.io.File;
import java.io.IOException;
import static java.lang.Double.MAX_VALUE;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
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
import javafx.scene.Node;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
/**
 * FXML Controller class
 *
 * @author Gilbert Alvarado
 */
public class OverviewViewTesting_NOANCHORController implements Initializable {


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
    private TableColumn<ModelOverviewTable, String> CONFIRMEDcolumn, POcolumn,BRGcolumn,
            SUPPLIERcolumn, CURSHIP_column, DATEcolumn, NOTEScolumn;
    @FXML
    private TableColumn<ModelOverviewTable, String> IPcolumn, LCcolumn;
    
    private ObservableList<ModelOverviewTable> obList = FXCollections.observableArrayList();
    
    @FXML
    private TableView<ModelOverviewTable> OverviewTableView;

    
    @FXML
    private Button clearFieldsBUTTON;
    
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        CONFIRMEDcomboBox.getItems().addAll("YES","NO");
        CONFIRMEDcomboBox.setEditable(false);
        CONFIRMEDcomboBox.getSelectionModel().select("NO");
        
clearFieldsBUTTON.setStyle("-fx-font-size: 14px;-fx-font-weight: bold;\n" 
                                + "-fx-font-family: Georgia;");
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
        

        OverviewTableView.refresh();
        updateTableView();
        OverviewTableView.refresh();
        addButtonsToTable();//NOTEScolumn
//        addSearchFilters();
        
//        addComboBoxFilter();
        OverviewTableView.refresh();
        addMultipleSearchFilters();
        OverviewTableView.refresh();
    }
//##############################################################################    

    public void updateTableView(){
        
        OverviewTableView.getItems().clear();
        OverviewTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
//        OverviewTableView.setStyle("-fx-selection-bar-non-focused: green; -fx-selection-bar: brown;");
        
            try{
//                   ConnectionUtil.setDbLocation(MainLayoutTesting_WITHANCHORController.getDBLocation());
                   Connection con = ConnectionUtil.conDB();//ConnectionUtil.conDB();
                 
    ResultSet rs = 
            con.createStatement().executeQuery(
                    "SELECT "
                            + "bearings.supplier_id, purchase_orders.purchase_order, "
                            + "purchase_orders.current_ship_date, "//3
                            + "bearings.brg_name, order_details.invoice_price, "
                            + "order_details.landed_cost, "//6
                            + "order_details.confirmed, FORMAT(purchase_orders.fu_date,'Short Date')\n"//8 
                            + "FROM purchase_orders "
                                + "INNER JOIN (bearings INNER JOIN order_details "
                                + "ON bearings.[brg_id] = order_details.[brg_id]) "
                                + "ON purchase_orders.[purchase_order] = order_details.[purchase_order]\n" );
//                           + "WHERE order_details.confirmed = FALSE;");
    
                    SimpleDateFormat MMddyyyy = new SimpleDateFormat("MM/dd/yyyy");
                    
                    while(rs.next()){
                        
                        String confirmation;
                        
                        confirmation = (rs.getString("confirmed").equals("FALSE")) ? "NO" : "YES";
                        
                            obList.add(new 
                               ModelOverviewTable(confirmation,
                               rs.getString("purchase_order"),
                               rs.getString("brg_name"),//CHANGE TO name
                               rs.getString("supplier_id"),
                               MMddyyyy.format(rs.getDate(3)),//current ship
                               rs.getString(8),//FU date
                               
                               IPformat.format(Double.parseDouble(rs.getString("invoice_price"))),
                               LCformat.format(Double.parseDouble(rs.getString("landed_cost"))),
                               rs.getDate(3)));//original date
                   }
                        
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(OverviewViewTesting_NOANCHORController.class.getName()).log(Level.SEVERE, null, ex);
        }
        

    }
   
//##############################################################################
    //BUTTONS- OPEN DIALOG/VIEW NOTES
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
                        btn.setMaxHeight(25);
                        btn.setMaxWidth(75);
                        btn.setStyle("-fx-font-size: 18px;-fx-font-weight: bold;\n" 
                                + "-fx-font-family: Georgia;");
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
                            
                            ModelOverviewTable data = getTableView().getItems().get(getIndex());
                                
                            System.out.println("FETCHING FU FILE");
                            
                            //##################################################
                            //  GET/CREATE NOTES
                            //##################################################
                            File tempFile;
                            if(FileHelper.getFUFile(data.getPo()).isFile()){
                                System.out.println("FILE EXISTS");
//                                dialog.setContentText(FileHelper.getFUFile(data.getPo()).getAbsolutePath());
                                tempFile = FileHelper.getFUFile(data.getPo());
                                
                            }
                            else {
                                //SHOULD UPDATE DATABASE IF FILE IS CREATED
//                                System.out.println("FILE DNE");
//                                System.out.println("CREATE FILE FOR " + data.getPo());
                                FileHelper.createDirectory(data.getPo());
                                FileHelper.createPoAttachmentsDirectory(data.getPo());
                                FileHelper.creatFile(data.getPo());//CREATE AND UPDATE TABLE
                                
                                if(FileHelper.getFUFile(data.getPo()).isFile()){
//                                    dialog.setContentText("FILE CREATED");
                                    tempFile = FileHelper.getFUFile(data.getPo());
                                }
                                else{
//                                    dialog.setContentText("ERROR CREATING FILES");
                                    tempFile = null;
                                }
                            }
                            //##################################################
                            //  OPEN FORM/DIALOG
                            //##################################################
                            
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
                                NotesViewController controller = loader.getController();
                                
                                if(tempFile!=null){

                                    String notes_content = FileHelper.fileContent(tempFile).toString();
                                    if(!notes_content.isEmpty()){ 
                                        controller.setPoLabel(data.getPo());
                                        controller.setFuDateLabel(data.getDa());
//                                        controller.setContext(notes_content, data);
                                        controller.setContext(tempFile);
                                    }
                                    else{
                                        controller.setPoLabel(data.getPo());
                                        controller.setFuDateLabel(data.getDa());
                                        controller.setContext(null);
                                    }
                                }
                                else{
//                                    dialog.setContentText("did you happen to delete the file?!");
                                }
                                stage.showAndWait();
                            } catch (IOException ex) {
                                    Logger.getLogger(OverviewViewTesting_NOANCHORController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            
                            });
                            setGraphic(btn);
                            OverviewTableView.refresh();
                        }
                    }
                };
                return cell;
            }
        };
        NOTEScolumn.setCellFactory(cellFactory);
    }
    
    //##############################################################################
    //private ObservableList<ModelOverviewTable> obList = FXCollections.observableArrayList();
    //#####################################
    FilteredList<ModelOverviewTable> filterData;
    public void addSearchFilters() {
                
        filterData = new FilteredList<>(obList,b->true);
//        filterData.setPredicate(prdct);
        SortedList<ModelOverviewTable> sortedData = new SortedList<>(filterData);
        sortedData.comparatorProperty().bind(OverviewTableView.comparatorProperty());
        OverviewTableView.setItems(sortedData); 
        
        filterData.predicateProperty().bind(Bindings.createObjectBinding(() -> {
        
            LocalDate minDate = curShipSTART.getValue();
            LocalDate maxDate = curShipEND.getValue();

            // get final values != null
            final LocalDate finalMin = minDate == null ? LocalDate.MIN : minDate;
            final LocalDate finalMax = maxDate == null ? LocalDate.MAX : maxDate;

            return search_field -> search_field.getSupplier().contains(SUPPLIERTextField.getText())
               && search_field.getPo().contains(POTextField.getText()) 
                && search_field.getBrg().contains(BRGTextField.getText())
                    && !finalMin.isAfter(LocalDate.parse( search_field.getOriginalDate().toString())) 
                    && !finalMax.isBefore(LocalDate.parse( search_field.getOriginalDate().toString() ) );
//                    && search_field.getConfirmed().equals(CONFIRMEDcomboBox.getValue()) ;//CONFIRMEDcomboBox
        },

            SUPPLIERTextField.textProperty(),
            POTextField.textProperty(),
            BRGTextField.textProperty(),
            curShipSTART.valueProperty(),
            curShipEND.valueProperty()
//            CONFIRMEDcomboBox.valueProperty()
        ));
        
    }
    
    private void addMultipleSearchFilters(){
        ObjectProperty<Predicate<ModelOverviewTable>> supplierFilter = new SimpleObjectProperty<>();
        supplierFilter.bind(Bindings.createObjectBinding(() -> 
            supplier -> supplier.getSupplier().toLowerCase().contains(SUPPLIERTextField.getText().toLowerCase()), 
            SUPPLIERTextField.textProperty()));
        //----------------------------------------------------------------------
        ObjectProperty<Predicate<ModelOverviewTable>> poFilter = new SimpleObjectProperty<>();
        poFilter.bind(Bindings.createObjectBinding(() -> 
            po -> po.getPo().toLowerCase().contains(POTextField.getText().toLowerCase()), 
            POTextField.textProperty()));
        //----------------------------------------------------------------------
        ObjectProperty<Predicate<ModelOverviewTable>> brgFilter = new SimpleObjectProperty<>();
        brgFilter.bind(Bindings.createObjectBinding(() -> 
            brg -> brg.getBrg().toLowerCase().contains(BRGTextField.getText().toLowerCase()), 
            BRGTextField.textProperty()));
        //----------------------------------------------------------------------
        ObjectProperty<Predicate<ModelOverviewTable>> dateFilter = new SimpleObjectProperty<>();
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
        ObjectProperty<Predicate<ModelOverviewTable>> confirmedFilter = new SimpleObjectProperty<>();
        confirmedFilter.bind(Bindings.createObjectBinding(() ->
                
                confirmed -> confirmed.getConfirmed().equals(CONFIRMEDcomboBox.getValue())                
                , CONFIRMEDcomboBox.valueProperty()));
        
        //----------------------------------------------------------------------
        
        filterData = new FilteredList<>(FXCollections.observableList(obList));
        OverviewTableView.setItems(filterData);
        
        filterData.predicateProperty().bind(Bindings.createObjectBinding(
                () -> supplierFilter.get().and(poFilter.get().and(brgFilter.get().and(dateFilter.get().and(confirmedFilter.get())))), 
                supplierFilter, poFilter, brgFilter, dateFilter, confirmedFilter));
               
        
    }
    
    //##########################################################################
    @FXML
    private void clearFields(ActionEvent event) {
        SUPPLIERTextField.clear();
        POTextField.clear();
        BRGTextField.clear();
        curShipSTART.getEditor().clear();
        curShipEND.getEditor().clear();
        CONFIRMEDcomboBox.setValue("NO");
//        filterData.getSource().remove(confirmedFilter);//removeFilter

    }
    //##########################################################################
    
    private void addComboBoxFilter(){
        ObjectProperty<Predicate<ModelOverviewTable>> confirmedFilter = new SimpleObjectProperty<>();
        /*
        if: comboBox == ALL, remove predicate
        else: add predicate
        */
    }

   
}
    