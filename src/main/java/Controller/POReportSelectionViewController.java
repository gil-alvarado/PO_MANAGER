/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.ModelManageDBTable;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
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
    @FXML
    private ListView<ModelManageDBTable> sourceView, targetView;
    //--------------------------------------------------------------------------
    //      LISTVIEW FINAL STAGE
    //--------------------------------------------------------------------------
    @FXML
    private ListView<ModelManageDBTable> selectedPoOverviewListView;//FINAL STEP, USER CONFIRMS
	// Set the Custom Data Format
        //######################################################################
        //Data format identifier used as means of identifying the data stored on a clipboard/dragboard.
        //######################################################################    
    private static final DataFormat DB_LIST = new DataFormat("DbList");
    
    
    //##########################################################################
  
    @FXML
    private TextField fileNameTextField;
    @FXML
    private Button selectFolderBUTTON;
    @FXML
    private TextField fileLocationTextField;
    @FXML
    private Label finalLocationLabel;
    @FXML
    private GridPane selectItemsGridPane;//first step

    @FXML
    private Button transferSingleItemBUTTON,transferAllItemsBUTTON,removeSingleItemBUTTON,removeAllItemsButton;
    @FXML
    private ComboBox<String> POitemsComboBox;
    @FXML
    private ComboBox<String> reportItemsComboBox;
    @FXML
    private Button cancelBUTTON,backBUTTON,nextBUTTON,finishBUTTON;
    /**
     * Initializes the controller class.
     */
    
    private LinkedHashMap<String, ModelManageDBTable> data;
    
    private Stage fileDirectoryStage;
    
    /*
        STEPS
    
    1) initializeComponents
    2) initialize listeners
    3) build GUI
    4) populate DATA
    
    */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        confirmItemsGridPane.toBack();
        selectItemsGridPane.toFront();
        backBUTTON.setDisable(true);
//        finishBUTTON.setDisable(true);
        
        selectFolderBUTTON.disableProperty().bind(fileNameTextField.textProperty().isEmpty());
        fileLocationTextField.disableProperty().bind(fileNameTextField.textProperty().isEmpty());
        finishBUTTON.disableProperty().bind(fileNameTextField.textProperty().isEmpty()
                    .or(fileLocationTextField.textProperty().isEmpty()));
        
        
        
        fileDirectoryStage = new Stage();
        
//        sourceView.getItems().addAll(this.getDBitems());
        
        sourceView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        targetView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		// Add mouse event handlers for the source
		sourceView.setOnDragDetected(new EventHandler <MouseEvent>()
		{
            public void handle(MouseEvent event){
            	System.out.println("Event on Source: drag detected");
            	dragDetected(event, sourceView);
            }
        });
        //----------------------------------------------------------------------        
		sourceView.setOnDragOver(new EventHandler <DragEvent>()
		{
            public void handle(DragEvent event){
            	System.out.println("Event on Source: drag over");
            	dragOver(event, sourceView);
            }
        });
        //----------------------------------------------------------------------        
		sourceView.setOnDragDropped(new EventHandler <DragEvent>()
		{
            public void handle(DragEvent event){
            	System.out.println("Event on Source: drag dropped");
            	dragDropped(event, sourceView);
            }
        });
        //----------------------------------------------------------------------
		sourceView.setOnDragDone(new EventHandler <DragEvent>()
		{
            public void handle(DragEvent event){
            	System.out.println("Event on Source: drag done");
            	dragDone(event, sourceView);
            }
        });
        //----------------------------------------------------------------------          
		// Add mouse event handlers for the target
		targetView.setOnDragDetected(new EventHandler <MouseEvent>()
		{
            public void handle(MouseEvent event){
            	System.out.println("Event on Target: drag detected");
            	dragDetected(event, targetView);
            }
        });
        //----------------------------------------------------------------------        
		targetView.setOnDragOver(new EventHandler <DragEvent>()
		{
            public void handle(DragEvent event){
            	System.out.println("Event on Target: drag over");
            	dragOver(event, targetView);
            }
        });
        //----------------------------------------------------------------------        
		targetView.setOnDragDropped(new EventHandler <DragEvent>()
		{
            public void handle(DragEvent event){
            	System.out.println("Event on Target: drag dropped");
            	dragDropped(event, targetView);
            }
        });
        //----------------------------------------------------------------------        
		targetView.setOnDragDone(new EventHandler <DragEvent>()
		{
            public void handle(DragEvent event){
            	System.out.println("Event on Target: drag done");
            	dragDone(event, targetView);
            }
        });
                //##############################################################
                //END initialize 
                //##############################################################         
        
    }    
    
    //##########################################################################
    public void setSelecteditems(LinkedHashMap<String, ModelManageDBTable> selectedData){
        this.data = new LinkedHashMap<>(selectedData);
        //transferred data, now set listview items
        
        if(!this.data.isEmpty()){
//            reportItemsListView.getItems().add(data.get(""));
            for(Map.Entry<String, ModelManageDBTable> cursor : data.entrySet()){
                targetView.getItems().add(cursor.getValue());
                selectedPoOverviewListView.getItems().add(cursor.getValue());
            }
        }
        
    }
    private ObservableList getDBitems(){
        
//        ObservableList<ModelManageDBTable> list = //FXCollections.<ModelManageDBTable>observableArrayList();
        
        return null;// ManageDBViewController.getList();
    }
    //##########################################################################
    //
    //##########################################################################    
    
	private void dragDetected(MouseEvent event, ListView<ModelManageDBTable> listView){
		// Make sure at least one item is selected
		int selectedCount = listView.getSelectionModel().getSelectedIndices().size();

		if (selectedCount == 0)
		{
			event.consume();
			return;
		}

		// Initiate a drag-and-drop gesture
		Dragboard dragboard = listView.startDragAndDrop(TransferMode.COPY_OR_MOVE);

		// Put the the selected items to the dragboard
		ArrayList<ModelManageDBTable> selectedItems = this.getSelectedFruits(listView);

		ClipboardContent content = new ClipboardContent();
		content.put(DB_LIST, selectedItems);

		dragboard.setContent(content);
		event.consume();
	}
        //######################################################################
	private void dragOver(DragEvent event, ListView<ModelManageDBTable> listView){
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
	private void dragDropped(DragEvent event, ListView<ModelManageDBTable> listView){
		boolean dragCompleted = false;

		// Transfer the data to the target
		Dragboard dragboard = event.getDragboard();

		if(dragboard.hasContent(DB_LIST))
		{
			ArrayList<ModelManageDBTable> list = (ArrayList<ModelManageDBTable>)dragboard.getContent(DB_LIST);
			listView.getItems().addAll(list);
			// Data transfer is successful
			dragCompleted = true;
		}

		// Data transfer is not successful
		event.setDropCompleted(dragCompleted);
		event.consume();
	}
        //######################################################################
	private void dragDone(DragEvent event, ListView<ModelManageDBTable> listView){
		// Check how data was transfered to the target
		// If it was moved, clear the selected items
		TransferMode tm = event.getTransferMode();

		if (tm == TransferMode.MOVE)
		{
			removeSelectedFruits(listView);
		}

		event.consume();
	}
        //######################################################################
	private ArrayList<ModelManageDBTable> getSelectedFruits(ListView<ModelManageDBTable> listView){
		// Return the list of selected Fruit in an ArratyList, so it is
		// serializable and can be stored in a Dragboard.
		ArrayList<ModelManageDBTable> list = new ArrayList<>(listView.getSelectionModel().getSelectedItems());

		return list;
	}
        //######################################################################
	private void removeSelectedFruits(ListView<ModelManageDBTable> listView){
		// Get all selected Fruits in a separate list to avoid the shared list issue
		List<ModelManageDBTable> selectedList = new ArrayList<>();

		for(ModelManageDBTable fruit : listView.getSelectionModel().getSelectedItems())
		{
			selectedList.add(fruit);
		}

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
        Node  source = (Node)  event.getSource(); 
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void backStepButton(ActionEvent event) {
        selectItemsGridPane.toFront();
        backBUTTON.setDisable(true);
        nextBUTTON.setDisable(false);
    }

    @FXML
    private void nextStepButton(ActionEvent event) {
        
        selectedPoOverviewListView.getItems().clear();
        
        confirmItemsGridPane.toFront();
        backBUTTON.setDisable(false);
        nextBUTTON.setDisable(true);
        if(!this.data.isEmpty()){
            for(Map.Entry<String, ModelManageDBTable> cursor : data.entrySet()){
                selectedPoOverviewListView.getItems().add(cursor.getValue());
            }
        }
    }

    @FXML
    private void createReport(ActionEvent event) {
    }

    @FXML
    private void openFileChooser(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        
        DirectoryChooser chooser = new DirectoryChooser();
        
        File selectedDir;
        File selectedFile;
//        selectedFile = fileChooser.showOpenDialog();
        
        selectedDir = chooser.showDialog(fileDirectoryStage);
        
        
    }
    
    //##########################################################################
}
