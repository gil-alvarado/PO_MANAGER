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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
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
    
    private static DecimalFormat LCformat = new DecimalFormat("$###,###,##0.00");
    private static DecimalFormat IPformat = new DecimalFormat("$###,###,##0.000");
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
    @FXML
    private Button refreshButton;
    
    
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
        
        searchPO();
    }
//##############################################################################    

//    @FXML
//    private void updateTableFXML(){
//        updateTableView();
//    }
    public void updateTableView(){
        
        OverviewTableView.getItems().clear();
        
            try{
                   Connection con = ConnectionUtil.conDB();
                 
    ResultSet rs = 
            con.createStatement().executeQuery(
                    "SELECT po.purchase_order, pd.confirmed, pd.invoice_price, pd.landed_cost, b.brg_name, "
                            + "s.supplier_id, "
                            + "FORMAT(po.current_ship_date,'Short Date'), "//7
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
                                System.out.println("FILE DNE");
                                FileHelper.createDirectory(data.getPo());
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
    