/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.ConnectionUtil;
import Model.ModelManageDBTable;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;



import net.ucanaccess.complex.Attachment;


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
    private ComboBox<String> CONFIRMEDcomboBox;
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
    
    private EditPOView_NOANCHORController controller;//used to setItems in editPOView
    
    private FXMLLoader loader;
    private AnchorPane editPOAnchor;
    private MainLayoutTesting_WITHANCHORController instance;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        CONFIRMEDcomboBox.getItems().addAll("YES","NO");
        CONFIRMEDcomboBox.setEditable(false);
        CONFIRMEDcomboBox.getSelectionModel().select("YES");
        POcolumn.setCellValueFactory(new PropertyValueFactory<>("po"));
        BRGcolumn.setCellValueFactory(new PropertyValueFactory<>("brg"));
        CURSHIP_column.setCellValueFactory(new PropertyValueFactory<>("cur"));
        STATUScolumn.setCellValueFactory(new PropertyValueFactory<>("confirmed"));
        ATTACHMENTScolumn.setCellValueFactory(new PropertyValueFactory<>("attachment"));
        PACKETcolumn.setCellValueFactory(new PropertyValueFactory<>("packet"));

        updateTableView();
        addButtonsToTable();

        searchPO();
            
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
     try{
            Connection con = ConnectionUtil.conDB();
            
            //Format(FUdate,'Short Date')
            ResultSet rs =con.createStatement().executeQuery(
                    "SELECT po.purchase_order, pd.confirmed,  pd.attachments, b.brg_name, "
                            + "FORMAT(po.current_ship_date,'Short Date') "
                            + ""
                            + "FROM purchase_orders po, order_details pd, bearings b "
                            + ""
                            + "WHERE po.purchase_order = pd.purchase_order AND pd.brg_id = b.brg_id");
            
            ModelManageDBTable dbItem;
            while(rs.next()){
                
                String confirmation;
                if(rs.getString("confirmed").equals("TRUE"))
                    confirmation = "YES";
                else
                    confirmation = "NO";
                        
                dbItem = new 
                ModelManageDBTable(confirmation,
                        rs.getString("purchase_order"),                       
                        rs.getString("brg_name"),
                        rs.getString(5),//CUR SHIP
                        "num att",
                        "add packet box");
                Attachment att []= (Attachment[])rs.getObject(3);
                dbItem.setAtt(att);
                dbItem.displayAttachmentCHANGE();
                dbItem.setAttachment(String.valueOf(dbItem.getNumberAttachments()));
                obList.add(dbItem);

            }
            con.close();
            
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
                    
                    private final Button btn = new Button("Edit");
                    {
                        btn.setMaxHeight(50);
                        btn.setMaxWidth(100);
                        btn.setStyle("-fx-font-size: 18px;-fx-font-weight: bold;\n" 
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
                                setGraphic(btn);
                                btn.setOnAction((ActionEvent event) -> {

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
    //##############################################################################
    public void searchPO() {
                //MAIN TABLE-begin filtering
        FilteredList<ModelManageDBTable> filterData = new FilteredList<>(obList,b->true);
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
        
        SortedList<ModelManageDBTable> sortedData = new SortedList<>(filterData);
        sortedData.comparatorProperty().bind(ManageDBTable.comparatorProperty());
                
        ManageDBTable.setItems(sortedData);   
    } 
}
