/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.ModelManageDBTable;
import java.io.File;
import java.net.URL;
import java.util.LinkedHashMap;
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
    @FXML
    private ListView<Student> selectedPoOverviewListView;//FINAL STEP, USER CONFIRMS
    
    //----------------------------------------------------------------
    @FXML
    private ListView<Student> poItemsListView =new ListView<Student>(),reportItemsListView =new ListView<Student>();
    
    private static final ObservableList<Student> leftList = FXCollections
      .observableArrayList();
    private static final ObservableList<Student> rightList = FXCollections
      .observableArrayList();
  
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
        
        initializeComponents();
        initializeListeners();
        
        populateData();
        
    }    
    
    //##########################################################################
    public void setSelecteditems(LinkedHashMap<String, ModelManageDBTable> selectedData){
        this.data = new LinkedHashMap<>(selectedData);
        //transferred data, now set listview items
        
        if(!this.data.isEmpty()){
//            reportItemsListView.getItems().add(data.get(""));
            for(Map.Entry<String, ModelManageDBTable> cursor : data.entrySet()){
//                reportItemsListView.getItems().add(cursor.getKey());
                
            }
        }
        
    }
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
//                selectedPoOverviewListView.getItems().add(cursor.getKey());
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
    
    private void populateData() {
    leftList.addAll(new Student("Adam"), new Student("Alex"), new Student(
        "Alfred"));
    System.out.println(leftList);
    poItemsListView.setItems(leftList);
    reportItemsListView.setItems(rightList);
    }
    
    //##########################################################################
    
    private void initializeComponents() {
        initializeListView(poItemsListView);//left

        initializeListView(reportItemsListView);//right
    }
    
    //##########################################################################
    
    private void initializeListeners(){
    // drag from left to right
    poItemsListView.setOnDragDetected(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        if (poItemsListView.getSelectionModel().getSelectedItem() == null) {
          return;
        }

        Dragboard dragBoard = poItemsListView.startDragAndDrop(TransferMode.COPY_OR_MOVE);
        
//        ArrayList<Student> selectedItems = this.get
        ClipboardContent content = new ClipboardContent();
        content.putString(poItemsListView.getSelectionModel().getSelectedItem()
            .getName());
        dragBoard.setContent(content);
      }
    });
    //--------------------------------------------------------------------------
    reportItemsListView.setOnDragOver(new EventHandler<DragEvent>() {
      @Override
      public void handle(DragEvent dragEvent) {
        dragEvent.acceptTransferModes(TransferMode.MOVE);
      }
    });
    //--------------------------------------------------------------------------
    reportItemsListView.setOnDragDropped(new EventHandler<DragEvent>() {
      @Override
      public void handle(DragEvent dragEvent) {
        String player = dragEvent.getDragboard().getString();
        reportItemsListView.getItems().addAll(new Student(player));
        leftList.remove(new Student(player));
        poItemsListView.setItems(leftList);
        dragEvent.setDropCompleted(true);
      }
    });
    // drag from right to left
    reportItemsListView.setOnDragDetected(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        Dragboard dragBoard = reportItemsListView.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent content = new ClipboardContent();
        content.putString(reportItemsListView.getSelectionModel().getSelectedItem()
            .getName());
        dragBoard.setContent(content);
      }
    });

    poItemsListView.setOnDragOver(new EventHandler<DragEvent>() {
      @Override
      public void handle(DragEvent dragEvent) {
        dragEvent.acceptTransferModes(TransferMode.MOVE);
      }
    });

    poItemsListView.setOnDragDropped(new EventHandler<DragEvent>() {
      @Override
      public void handle(DragEvent dragEvent) {
        String player = dragEvent.getDragboard().getString();
        poItemsListView.getItems().remove(new Student(player));

        rightList.remove(new Student(player));
        dragEvent.setDropCompleted(true);
      }
    });
  }

    //##########################################################################
    
    private void initializeListView(ListView<Student> listView) {
        listView.setEditable(false);
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listView.setCellFactory(new StringListCellFactory());
    }
    
    //##########################################################################
    
    class StringListCellFactory implements
      Callback<ListView<Student>, ListCell<Student>> {
    @Override
    public ListCell<Student> call(ListView<Student> playerListView) {
      return new StringListCell();
    }

    class StringListCell extends ListCell<Student> {
      @Override
      protected void updateItem(Student player, boolean b) {
        super.updateItem(player, b);

        if (player != null) {
          setText(player.getName());
        }
      }
    }
  }
}
 //##########################################################################
    class Student {
        private String name;

        public Student(String name) {
          this.name = name;
        }

        public String getName() {
          return name;
        }

        public void setName(String name) {
          this.name = name;
        }

        @Override
        public boolean equals(Object o) {
          if (this == o)
            return true;
          if (o == null || getClass() != o.getClass())
            return false;

          Student player = (Student) o;

          if (name != null ? !name.equals(player.name) : player.name != null)
            return false;

          return true;
        }

        @Override
        public int hashCode() {
          return name != null ? name.hashCode() : 0;
        }
    }
