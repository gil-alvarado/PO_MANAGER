/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.BMSPurchaseOrderModel;
import Model.ConnectionUtil;
import Model.DOCXHandler;
import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Predicate;
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
import javafx.scene.Node;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
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
    
//    this.obListMASTERSourceData = FXCollections.observableArrayList(ConnectionUtil.getAllData().values());
//this.obListMASTERTargetData = FXCollections.observableArrayList();
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
//        selectItemsGridPane.setVisible(false);
finalLocationLabel.setVisible(false);
        selectItemsTableGridPane.toFront();
        selectItemsTableGridPane.setVisible(true);
        backBUTTON.setDisable(true);
//        ConfirmedComboBox.getItems().addAll("YES","NO","VIEW ALL");
//        ConfirmedComboBox.getSelectionModel().select("VIEW ALL");

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
        
//        for(Map.Entry<String, BMSPurchaseOrderModel> entry : ConnectionUtil.getAllData().entrySet())
//            obListMASTERSourceData.add(entry.getValue());

        //updaete table
        sourceTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        targetTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        obListMASTERSourceData.addAll(ManageDBViewController.getInstance().getList());
        obListMASTERTargetData.addAll(ManageDBViewController.getInstance().getSelectedList());
        obListMASTERSourceData.removeAll(obListMASTERTargetData);
        sourceTable.setItems(obListMASTERSourceData);
        targetTable.setItems(obListMASTERTargetData);
        nextBUTTON.disableProperty().bind(Bindings.isEmpty(targetTable.getItems()));
//        setupPredicates();
//        addMultipleSearchFilters();
        //##############################################################
        //END initialize 
        //##############################################################         

    }    
    
    //##########################################################################

    //update table
    public void setSelecteditems(LinkedHashMap<String, BMSPurchaseOrderModel> selectedData,
            ObservableList<BMSPurchaseOrderModel> obList,
            List<BMSPurchaseOrderModel> po){
                
//        this.obListMASTERSourceData = FXCollections.observableArrayList(ConnectionUtil.getAllData().values());
//        this.obListMASTERTargetData = FXCollections.observableArrayList();
//        for(Map.Entry<String, BMSPurchaseOrderModel> cursor : selectedData.entrySet()){
//        for(BMSPurchaseOrderModel cursor : po)
//            this.obListMASTERSourceData.remove(cursor);
//            this.obListMASTERTargetData.add(cursor.getValue());
//        }
//        obListMASTERSourceData.removeAll(po);
//        obListMASTERTargetData.addAll(po);
        
//        if( !selectedData.isEmpty() ){
//            for( Map.Entry<String, BMSPurchaseOrderModel> cursor : selectedData.entrySet() ){
//                targetTable.getItems().add( cursor.getValue() );
//            }
//        }
//        
//        if( !this.obListMASTERSourceData.isEmpty() ){
//            for(BMSPurchaseOrderModel i : this.obListMASTERSourceData){
//                sourceTable.getItems().add(i);
//            }
//        }
        
//        sourceTable.setItems(obListMASTERSourceData);
//        targetTable.setItems(obListMASTERTargetData);


    }
    
    /*
    private void  setupPredicates(){
        
        filterData = new FilteredList<>(obListMASTERSourceData, p -> true);
        
        supplierFilter = new SimpleObjectProperty<>();
        supplierFilter.bind(Bindings.createObjectBinding(() -> 
            supplier -> supplier.getSupplier().toLowerCase().contains(SupplierTextField.getText().toLowerCase()), 
            SupplierTextField.textProperty()));
        //----------------------------------------------------------------------
        poFilter = new SimpleObjectProperty<>();
        poFilter.bind(Bindings.createObjectBinding(() -> 
            po -> po.getPurchase_order().toLowerCase().contains(PoTextField.getText().toLowerCase()), 
            PoTextField.textProperty()));
        //----------------------------------------------------------------------
        brgFilter = new SimpleObjectProperty<>();
        brgFilter.bind(Bindings.createObjectBinding(() -> 
            brg -> brg.getBrg_number().toLowerCase().contains(BrgTextField.getText().toLowerCase()), 
            BrgTextField.textProperty()));
        //----------------------------------------------------------------------
        dateFilter = new SimpleObjectProperty<>();
        dateFilter.bind(Bindings.createObjectBinding(() -> {
        
            LocalDate minDate = CurShipDateSTART.getValue();
            LocalDate maxDate = CurShipDateEND.getValue();

            // get final values != null
            final LocalDate finalMin = minDate == null ? LocalDate.MIN : minDate;
            final LocalDate finalMax = maxDate == null ? LocalDate.MAX : maxDate;

            return search_field ->!finalMin.isAfter(LocalDate.parse( search_field.getCurrent_ship_date().toString())) 
                    && !finalMax.isBefore(LocalDate.parse( search_field.getCurrent_ship_date().toString() ) );
        },
            CurShipDateSTART.valueProperty(),
            CurShipDateEND.valueProperty()));
        //--------------------------------------------------------------------    
        confirmedFilter = new SimpleObjectProperty<>();
        confirmedFilter.bind(Bindings.createObjectBinding(() ->
                
                confirmed -> confirmed.getConfirmed().equals(ConfirmedComboBox.getValue())                
                , ConfirmedComboBox.valueProperty()));
        //----------------------------------------------------------------------
        
        SortedList<BMSPurchaseOrderModel> sortedData = new SortedList<>(filterData);
        sortedData.comparatorProperty().bind(sourceTable.comparatorProperty());
        sourceTable.setItems(sortedData);
//        FilteredList<BMSPurchaseOrderModel> filterDataTest = new FilteredList<>(obListMASTERTargetData, p -> true);
//        sortedData = new SortedList<>(filterDataTest);
//        sortedData.comparatorProperty().bind(targetTable.comparatorProperty());
//        targetTable.setItems(sortedData);
        
    }    
        private void addMultipleSearchFilters(){
        
        filterData.predicateProperty().bind(Bindings.createObjectBinding(
            () -> supplierFilter.get().and(poFilter.get().and(brgFilter.get().
                    and(dateFilter.get().and(confirmedFilter.get())))),
                supplierFilter, poFilter, brgFilter, dateFilter, confirmedFilter));
        
    }
        */
    //##########################################################################
    //
    //##########################################################################    
    
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
        nextBUTTON.disableProperty().bind(Bindings.isEmpty(targetTable.getItems()));
    }

    @FXML
    private void nextStepButton(ActionEvent event) {
        
        finalTableView.getItems().clear();
        
        confirmItemsGridPane.toFront();
        finalTableView.getItems().addAll(targetTable.getItems());
        
        backBUTTON.setDisable(false);
        
    }
    
    @FXML
    private void createReport(ActionEvent event) {

        String fileAbsolutePath = fileLocationTextField.getText() + "\\" + fileNameTextField.getText() + ".docx";

        HashMap<String, List<BMSPurchaseOrderModel>> supplier_map = new HashMap<>();
        for(BMSPurchaseOrderModel po : targetTable.getItems()){
                if(!supplier_map.containsKey(po.getSupplier())){
                    supplier_map.put(po.getSupplier(), new ArrayList<>());
                }
        }
        
        for(Map.Entry<String, List<BMSPurchaseOrderModel>> supplier_map_entry : supplier_map.entrySet()){
            for(BMSPurchaseOrderModel po : targetTable.getItems()){
                if(po.getSupplier().equals(supplier_map_entry.getKey())){
                        supplier_map.get(po.getSupplier()).add(po);
                } 
            }
        }
        
        DOCXHandler.generateRport( fileAbsolutePath,supplier_map
        );
        
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
}
