/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.BMSPurchaseOrderModel;
import Model.ConnectionUtil;
import Model.DOCXHandler;
import java.awt.Desktop;
import java.io.File;
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
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;

import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
/**
 * FXML Controller class
 *
 * @author Gilbert Alvarado
 */
public class POReportSelectionViewController implements Initializable {


    @FXML
    private GridPane confirmItemsGridPane;//last step
    
    //##########################################################################
    //      LIST VIEWS
    //##########################################################################    
    //--------------------------------------------------------------------------
    //      LISTVIEW FINAL STAGE
    //--------------------------------------------------------------------------
    
	// Set the Custom Data Format
        //######################################################################
        //Data format identifier used as means of identifying the data stored on a clipboard/dragboard.
        //######################################################################    
    private static final DataFormat DB_LIST = new DataFormat("DbList");
    
    private ObservableList<BMSPurchaseOrderModel> obListMASTERSourceData=FXCollections.observableArrayList();
    private ObservableList<BMSPurchaseOrderModel> obListMASTERTargetData=FXCollections.observableArrayList();
    //##########################################################################
  
    @FXML
    private TextField fileNameTextField;
    @FXML
    private Button selectFolderBUTTON;
    @FXML
    private TextField fileLocationTextField;
    @FXML
    private Label finalLocationLabel;

    ObjectProperty<Predicate<BMSPurchaseOrderModel>> 
            supplierFilter, poFilter, brgFilter,dateFilter,confirmedFilter;
    
    FilteredList<BMSPurchaseOrderModel> filterData;
    @FXML
    private Button nextBUTTON,finishBUTTON,backBUTTON;
    /**
     * Initializes the controller class.
     */
    
//    private LinkedHashMap<String, BMSPurchaseOrderModel> selectedTargetData;
//    private LinkedHashMap<String, BMSPurchaseOrderModel> allData;
    private Stage fileDirectoryStage;
    
    @FXML
    private GridPane selectItemsTableGridPane;
    @FXML
    private TableView<BMSPurchaseOrderModel> sourceTable,targetTable,finalTableView;
    @FXML
    private TableColumn<BMSPurchaseOrderModel, String> SupplierColumnALL,PoColumnALL,
            CurShipColumnALL,IpColumnALL,LcColumnALL,ConfirmedColumnALL,BrgColumnALL;
    @FXML
    private TableColumn<BMSPurchaseOrderModel, String> SupplierColumnFINAL,PoColumnFINAL,
            CurShipColumnFINAL,IpColumnFINAL,LcColumnFINAL,ConfirmedColumnFINAL,BrgColumnFINAL;    
    //--------------------------------------------------------------------------
    @FXML
    private TableColumn<BMSPurchaseOrderModel, String> SELECTEDsupplierColumn,
            SELECTEDpoColumn,SELECTEDcurColumn,SELECTEDipColumn,SELECTEDlcColumn,
            SELECTEDconfirmedColumn,SELECTEDbrgColumn;
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        confirmItemsGridPane.toBack();
finalLocationLabel.setVisible(false);
        selectItemsTableGridPane.toFront();
        selectItemsTableGridPane.setVisible(true);
        backBUTTON.setDisable(true);

        selectFolderBUTTON.disableProperty().bind(fileNameTextField.textProperty().isEmpty());
        fileLocationTextField.disableProperty().bind(fileNameTextField.textProperty().isEmpty());
        finishBUTTON.disableProperty().bind(fileNameTextField.textProperty().isEmpty()
                    .or(fileLocationTextField.textProperty().isEmpty()));
        
        fileDirectoryStage = new Stage();
        
		// Add mouse event handlers for the source
		sourceTable.setOnDragDetected(new EventHandler <MouseEvent>()
		{
            public void handle(MouseEvent event){
//            	System.out.println("Event on Source: drag detected");
            	dragDetected(event, sourceTable);
            }
        });
        //----------------------------------------------------------------------        
		sourceTable.setOnDragOver(new EventHandler <DragEvent>()
		{
            public void handle(DragEvent event){
//            	System.out.println("Event on Source: drag over");
            	dragOver(event, sourceTable);
            }
        });
        //----------------------------------------------------------------------        
		sourceTable.setOnDragDropped(new EventHandler <DragEvent>()
		{
            public void handle(DragEvent event){
//            	System.out.println("Event on Source: drag dropped");
            	dragDropped(event, sourceTable);
            }
        });
        //----------------------------------------------------------------------
		sourceTable.setOnDragDone(new EventHandler <DragEvent>()
		{
            public void handle(DragEvent event){
//            	System.out.println("Event on Source: drag done");
                
                //obList
            	dragDone(event, sourceTable);
            }
        });
        //----------------------------------------------------------------------          
		// Add mouse event handlers for the target
		targetTable.setOnDragDetected(new EventHandler <MouseEvent>()
		{
            public void handle(MouseEvent event){
//            	System.out.println("Event on Target: drag detected");
            	dragDetected(event, targetTable);
            }
        });
        //----------------------------------------------------------------------        
		targetTable.setOnDragOver(new EventHandler <DragEvent>()
		{
            public void handle(DragEvent event){
//            	System.out.println("Event on Target: drag over");
            	dragOver(event, targetTable);
            }
        });
        //----------------------------------------------------------------------        
		targetTable.setOnDragDropped(new EventHandler <DragEvent>()
		{
            public void handle(DragEvent event){
//            	System.out.println("Event on Target: drag dropped");
            	dragDropped(event, targetTable);
            }
        });
        //----------------------------------------------------------------------        
		targetTable.setOnDragDone(new EventHandler <DragEvent>()
		{
            public void handle(DragEvent event){
//            	System.out.println("Event on Target: drag done");
                
                //selectedData
            	dragDone(event, targetTable);
            }
        });

//    //--------------------------------------------------------------------------
              
        ConfirmedColumnALL.setCellValueFactory(new PropertyValueFactory<>("confirmed"));
        PoColumnALL.setCellValueFactory(new PropertyValueFactory<>("purchase_order"));
        BrgColumnALL.setCellValueFactory(new PropertyValueFactory<>("brg_number"));
        SupplierColumnALL.setCellValueFactory(new PropertyValueFactory<>("supplier"));
        CurShipColumnALL.setCellValueFactory(new PropertyValueFactory<>("curDateFormat"));
        IpColumnALL.setCellValueFactory(new PropertyValueFactory<>("invoice_price"));
        LcColumnALL.setCellValueFactory(new PropertyValueFactory<>("landing_cost"));
        //----------------------------------------------------------------------
        SELECTEDconfirmedColumn.setCellValueFactory(new PropertyValueFactory<>("confirmed"));
        SELECTEDpoColumn.setCellValueFactory(new PropertyValueFactory<>("purchase_order"));
        SELECTEDbrgColumn.setCellValueFactory(new PropertyValueFactory<>("brg_number"));
        SELECTEDsupplierColumn.setCellValueFactory(new PropertyValueFactory<>("supplier"));
        SELECTEDcurColumn.setCellValueFactory(new PropertyValueFactory<>("curDateFormat"));    
        SELECTEDipColumn.setCellValueFactory(new PropertyValueFactory<>("invoice_price"));
        SELECTEDlcColumn.setCellValueFactory(new PropertyValueFactory<>("landing_cost"));   
        //----------------------------------------------------------------------
        ConfirmedColumnFINAL.setCellValueFactory(new PropertyValueFactory<>("confirmed"));
        PoColumnFINAL.setCellValueFactory(new PropertyValueFactory<>("purchase_order"));
        BrgColumnFINAL.setCellValueFactory(new PropertyValueFactory<>("brg_number"));
        SupplierColumnFINAL.setCellValueFactory(new PropertyValueFactory<>("supplier"));
        CurShipColumnFINAL.setCellValueFactory(new PropertyValueFactory<>("curDateFormat"));
        IpColumnFINAL.setCellValueFactory(new PropertyValueFactory<>("invoice_price"));
        LcColumnFINAL.setCellValueFactory(new PropertyValueFactory<>("landing_cost"));
//        //updaete table
        sourceTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        targetTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
 
        nextBUTTON.disableProperty().bind(Bindings.size(obListMASTERTargetData).isEqualTo(0));
        //##############################################################
        //END initialize 
        //##############################################################    
        prefs = Preferences.userRoot().node(POReportSelectionViewController.class.getClass().getName());
        System.out.println("POReportSelectionViewController PREFS: "+prefs.absolutePath());
        
    }    
    
    //##########################################################################

    public void setSelecteditems(LinkedHashMap<String, BMSPurchaseOrderModel> selected_po_map,
            ObservableList<BMSPurchaseOrderModel> obList_AllData,
            List<BMSPurchaseOrderModel> selected_po_list){
        
        obListMASTERSourceData.removeAll(obListMASTERSourceData);
        obListMASTERTargetData.removeAll(obListMASTERTargetData);
        
        for(BMSPurchaseOrderModel p : selected_po_list){
            obListMASTERTargetData.add(p);
        }
        for(BMSPurchaseOrderModel p : obList_AllData){
            obListMASTERSourceData.add(p);
        }
        obListMASTERSourceData.removeAll(obListMASTERTargetData);
 
        sourceTable.setItems(obListMASTERSourceData);
        targetTable.setItems(obListMASTERTargetData);


    }
    
	private void dragDetected(MouseEvent event, TableView<BMSPurchaseOrderModel> listView){
		// Make sure at least one item is selected
		int selectedCount = listView.getSelectionModel().getSelectedIndices().size();

		if (selectedCount == 0)
		{
			event.consume();
			return;
		}

		// Initiate a drag-and-drop gesture
		Dragboard dragboard = listView.startDragAndDrop(TransferMode.ANY);

		// Put the the selected items to the dragboard
		ArrayList<BMSPurchaseOrderModel> selectedItems = this.getSelectedFruits(listView);

		ClipboardContent content = new ClipboardContent();
		content.put(DB_LIST, selectedItems);

		dragboard.setContent(content);
		event.consume();
	}
        //######################################################################
	private void dragOver(DragEvent event, TableView<BMSPurchaseOrderModel> listView){
		// If drag board has an ITEM_LIST and it is not being dragged
		// over itself, we accept the MOVE transfer mode
		Dragboard dragboard = event.getDragboard();

		if (event.getGestureSource() != listView && dragboard.hasContent(DB_LIST))
		{
			event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
		}

		event.consume();
	}
        //######################################################################
	@SuppressWarnings("unchecked")
	private void dragDropped(DragEvent event, TableView<BMSPurchaseOrderModel> listView){
		boolean dragCompleted = false;

		// Transfer the data to the target
		Dragboard dragboard = event.getDragboard();

		if(dragboard.hasContent(DB_LIST))
		{
			ArrayList<BMSPurchaseOrderModel> list = (ArrayList<BMSPurchaseOrderModel>)dragboard.getContent(DB_LIST);
			listView.getItems().addAll(list);
			// Data transfer is successful
			dragCompleted = true;
		}

		// Data transfer is not successful
		event.setDropCompleted(dragCompleted);
		event.consume();
	}
        //######################################################################
	private void dragDone(DragEvent event, TableView<BMSPurchaseOrderModel> listView){
		// Check how data was transfered to the target
		// If it was moved, clear the selected items
		TransferMode tm = event.getTransferMode();

		if (tm == TransferMode.MOVE){
			removeSelectedFruits(listView);
		}

		event.consume();
	}
        //######################################################################
	private ArrayList<BMSPurchaseOrderModel> getSelectedFruits(TableView<BMSPurchaseOrderModel> listView){
		// Return the list of selected Fruit in an ArratyList, so it is
		// serializable and can be stored in a Dragboard.
		ArrayList<BMSPurchaseOrderModel> list = new ArrayList<>(listView.getSelectionModel().getSelectedItems());

		return list;
	}
        //######################################################################
	private void removeSelectedFruits(TableView<BMSPurchaseOrderModel> listView){

            // Get all selected Fruits in a separate list to avoid the shared list issue
            List<BMSPurchaseOrderModel> selectedList = new ArrayList<>();

            //MY ADDED CODE
            for(BMSPurchaseOrderModel item : listView.getSelectionModel().getSelectedItems())
                    selectedList.add(item);


            // Clear the selection
            listView.getSelectionModel().clearSelection();
            // Remove items from the selected list
            listView.getItems().removeAll(selectedList);
	}
    
    //##########################################################################
    //      MY FUNCTIONS
    //##########################################################################
    
    @FXML
    private void closeDialogButton(ActionEvent event) {
        Node  source = (Node) event.getSource(); 
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void backStepButton(ActionEvent event) {
        //        selectItemsGridPane.toFront();
        selectItemsTableGridPane.toFront();
        //        backBUTTON.setDisable(true);
        backBUTTON.setDisable(true);
//        nextBUTTON.disableProperty().bind(Bindings.isEmpty(targetTable.getItems()));
    }

    @FXML
    private void nextStepButton(ActionEvent event) {
        
        finalTableView.getItems().clear();
        
        confirmItemsGridPane.toFront();
        finalTableView.getItems().addAll(targetTable.getItems());
        
        backBUTTON.setDisable(false);
        
    }
    
    
    private Preferences prefs;
    
    @FXML//FINISH BUTTON
    private void createReport(ActionEvent event) {

        String fileAbsolutePath = fileLocationTextField.getText() + "\\" + fileNameTextField.getText() + ".docx";

        HashMap<String, List<BMSPurchaseOrderModel>> supplier_map = new HashMap<>();
        for(BMSPurchaseOrderModel po : targetTable.getItems()){
                if(!supplier_map.containsKey(po.getSupplier())){
                    System.out.print("ADDING " + po.getSupplier() + " (KEY) TO SUPPLIER_MAP");
                    supplier_map.put(po.getSupplier(), new ArrayList<>());
                }
        }
        //KEY GOES BY SUPPLIER
        for(Map.Entry<String, List<BMSPurchaseOrderModel>> entry : supplier_map.entrySet()){
            System.out.println("GETTING KEY: " + entry.getKey());
            for(BMSPurchaseOrderModel po_target : targetTable.getItems()){
                System.out.println("PO TARGET DATA: PO = " + po_target.getPurchase_order());
                if(po_target.getSupplier().equals(entry.getKey())){
                    System.out.println("MAP KEY MATCHES PO TARGET: (entry)" + entry.getKey()+" = " +po_target.getPurchase_order()+ "(target)");
                        supplier_map.get(po_target.getSupplier()).add(po_target);
                } 
            }
        }
        
        Alert finalAlert;
        
        
        
        if( DOCXHandler.generateReport( fileAbsolutePath, supplier_map ) ) {
            
            finalAlert = createAlertWithOptOut(AlertType.INFORMATION,"REPORT","SUCCESS","Report was successfuly created.",
                    "open file location", new Consumer<Boolean>() {
                @Override
                public void accept(Boolean param) {
                    prefs.putBoolean("openFolder", param == true ? true : false);
                }
            },ButtonType.OK);
            
        }else{
            finalAlert = createAlertWithOptOut(AlertType.INFORMATION,"REPORT","FAILURE","was not able to create report.",
                    "no action",null,ButtonType.OK);            
        }
        finalAlert.showAndWait();
        boolean open = prefs.getBoolean("openFolder", false);
        System.out.println("OPEN DIALOG: " + open);
        if(prefs.getBoolean("openFolder",false) == true){
            try {
                System.out.println("OPENING EXPLORER");
                //            if(!Desktop.isDesktopSupported())//check if Desktop is supported by Platform or not
//            {
//                System.out.println("not supported");
//                return;
//            }
//            Desktop desktop = Desktop.getDesktop();
//            System.out.println("FILE PATH: " + fileAbsolutePath );
//            desktop.browseFileDirectory(new File(fileAbsolutePath));
                Runtime.getRuntime().exec("explorer.exe /select," + fileAbsolutePath);
            } catch (IOException ex) {
                Logger.getLogger(POReportSelectionViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
            
        closeDialogButton(event);
    }

    @FXML
    private void openFileChooser(ActionEvent event) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setInitialDirectory(new File(System.getenv("USERPROFILE") + "\\Documents"));
        File selectedDir = chooser.showDialog(fileDirectoryStage);
        
        if(selectedDir!= null){
            fileLocationTextField.setText(selectedDir.getAbsolutePath());
            finalLocationLabel.setText(selectedDir.getAbsolutePath() +"\\"+ fileNameTextField.getText()+".docx");
            finalLocationLabel.setVisible(true);
        }
        
    }
    //##########################################################################
    
    public static Alert createAlertWithOptOut(AlertType type, String title, String headerText, 
                   String mainMessage, String checkBoxMessage, Consumer<Boolean> checkBoxAction, 
                   ButtonType buttonTypes) {
       Alert alert = new Alert(type);
       // Need to force the alert to layout in order to grab the graphic,
        // as we are replacing the dialog pane with a custom pane
        alert.getDialogPane().applyCss();
        Node graphic = alert.getDialogPane().getGraphic();
        // Create a new dialog pane that has a checkbox instead of the hide/show details button
        // Use the supplied callback for the action of the checkbox
     
        alert.setDialogPane(new DialogPane() {
//          @Override
          protected Node createDetailsButton() {
            CheckBox openReportLocation = new CheckBox();
            openReportLocation.setText(checkBoxMessage);
            openReportLocation.setOnAction(e -> checkBoxAction.accept( openReportLocation.isSelected()) );
            
            return openReportLocation;
          }
        });
        
        alert.getDialogPane().getButtonTypes().addAll(buttonTypes);
        alert.getDialogPane().setContentText(mainMessage);
        // Fool the dialog into thinking there is some expandable content
        // a Group won't take up any space if it has no children
        alert.getDialogPane().setExpandableContent(new Group());
        alert.getDialogPane().setExpanded(true);
        // Reset the dialog graphic using the default style
        alert.getDialogPane().setGraphic(graphic);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        
        return alert;
    }    
}