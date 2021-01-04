/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.ConnectionUtil;
import Model.ModelOverviewTable;
import Model.ModelUsersTable;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
/**
 * FXML Controller class
 *
 * @author Gilbert Alvarado
 */
public class ManageUsersViewController implements Initializable {


    @FXML
    private Button addUserBUTTON;
    @FXML
    private Button updateUserBUTTON;
    @FXML
    private Button removeUserBUTTON;
    @FXML
    private Button removeUserBUTTON1;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField usernameTextField;
    @FXML
    private ComboBox<String> userRoleComboBox;

    @FXML
    private Button clearFiledsBUTTON;
    //##########################################################
    @FXML
    private TableView<ModelUsersTable> manageUsersTable;
    @FXML
    private TableColumn<ModelUsersTable, String> firstNameColumn,
            lastNameColumn,usernameColumn,uerRoleColumn;
    
    ObservableList<ModelUsersTable> obList = FXCollections.observableArrayList();
    ModelUsersTable selected_item;
    
    private String firstNameParameter, lastNameParameter, userNameParameter, roleParameter;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        updateUserBUTTON.setDisable(true);
        removeUserBUTTON.setDisable(true);
        
        userRoleComboBox.getItems().addAll("ADMIN","STAFF");
        userRoleComboBox.setEditable(false);
//        userRoleComboBox.getSelectionModel().select("YES"); 
        
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("first"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("last"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("user"));
        uerRoleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        
        updateTableView();
//        setTextFields();
        manageUsersTable.getSelectionModel().selectedIndexProperty().addListener((obs, oldSelection, newSelection) -> {
    if (newSelection != null ) {
        selected_item = manageUsersTable.getSelectionModel().getSelectedItem();
        if( selected_item != null ){
//            addUserBUTTON.setDisable(true);
            updateUserBUTTON.setDisable(false);
            removeUserBUTTON.setDisable(false);
            
            firstNameTextField.setText(selected_item.getFirst());
            lastNameTextField.setText(selected_item.getLast());
            usernameTextField.setText(selected_item.getUser());
            
            firstNameParameter = selected_item.getFirst();
            lastNameParameter = selected_item.getLast();
            userNameParameter = selected_item.getUser();
            roleParameter = selected_item.getRole();            
            
            userRoleComboBox.getSelectionModel().select(selected_item.getRole());
                     
        }else{//
//            updateUserBUTTON.setDisable(true);
//            removeUserBUTTON.setDisable(true);
//            firstNameTextField.clear();
//            lastNameTextField.clear();
//            usernameTextField.clear();
        }
    }
});

        addUserBUTTON.disableProperty().bind(
                firstNameTextField.textProperty().isEmpty()
                .or(lastNameTextField.textProperty().isEmpty())
                .or(usernameTextField.textProperty().isEmpty())
                .or(Bindings.isNotEmpty(manageUsersTable.getSelectionModel().getSelectedItems()))
                .or(userRoleComboBox.valueProperty().isNull())
                );
    }
    
    private void updateTableView(){
        try {

            manageUsersTable.getItems().clear();

            Connection con = ConnectionUtil.conDB();

            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM users;");
            while(rs.next()){
                String role = "";
                if(rs.getInt("role_id") == 1)
                                role = "ADMIN";
                            else
                                role = "STAFF";
            //                
                            obList.add(new ModelUsersTable(
                                    rs.getString("first_name"),
                                    rs.getString("last_name"),
                                    rs.getString("user_name"),
                                    role)
                            );
                            System.out.println(rs.getString("first_name"));
            }

            manageUsersTable.setItems(obList);

        } catch (SQLException ex) {
            Logger.getLogger(ManageUsersViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    //check if username exists
    //add listener
    private static final String admin = "ADMIN";
    private static final String staff = "STAFF";
    @FXML
    private void addUser(ActionEvent event) {
        ButtonType YES = new ButtonType("YES", ButtonBar.ButtonData.YES);
        ButtonType NO = new ButtonType("NO", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType OK = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        
        Optional<ButtonType> result;
        Alert alert = new Alert(Alert.AlertType.NONE);
        {
            alert.setTitle("user");
            alert.getButtonTypes().clear();
        }
        if(ConnectionUtil.userExists(usernameTextField.getText())){
            alert.getDialogPane().getButtonTypes().addAll( OK );
            alert.setContentText(usernameTextField.getText() + " EXISTS!");
            alert.showAndWait();
            
        }else{//verify that the user wants to add data
            alert.getDialogPane().getButtonTypes().addAll( YES, NO );
            alert.setContentText("ADD " +usernameTextField.getText() + " as " + userRoleComboBox.getValue());
            
            result = alert.showAndWait();
            
            if(result.orElse(NO) == YES){//add user
                int role = ((userRoleComboBox.getValue().equals(admin)) ? 1 : 2);
                if(ConnectionUtil.addNewUser(firstNameTextField.getText().trim(),
                        lastNameTextField.getText().trim(), usernameTextField.getText().trim(), role) == 1){
                    
                    Dialog<String> dialog = new Dialog<>();
                    {
                        dialog.setTitle("Overview DIALOG");
                        ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
                        dialog.getDialogPane().getButtonTypes().add(type);
                        dialog.setContentText("SUCCESSFULY ADDED: " + firstNameTextField.getText() + " as " + usernameTextField.getText());
                        dialog.showAndWait();
                    }
                updateTableView();
            }else{
                    Dialog<String> dialog = new Dialog<>();
                    {
                        dialog.setTitle("Overview DIALOG");
                        ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
                        dialog.getDialogPane().getButtonTypes().add(type);
                        dialog.setContentText("SHOULD NOT POP UP, check validations");
                        dialog.showAndWait();
                    }
                }
        }
        
    }
    }
    
    //update selected user
    @FXML
    private void updateUser(ActionEvent event) {
        
        ButtonType YES = new ButtonType("YES", ButtonBar.ButtonData.YES);
        ButtonType NO = new ButtonType("NO", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType OK = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        
        Optional<ButtonType> result;
        Alert alert = new Alert(Alert.AlertType.NONE);
        {
            alert.setTitle("user");
            alert.getButtonTypes().clear();
        }
        
        int current_role = (roleParameter.equals(admin)) ? 1 : 2;
        
        if(ConnectionUtil.preUpdateUser(firstNameParameter, lastNameParameter,current_role,userNameParameter) == true){

            alert.getDialogPane().getButtonTypes().addAll( YES, NO );
            alert.setContentText("UPDATE?");
            
            result = alert.showAndWait();
            
            if(result.orElse(NO) == YES){//user confirms
                try{
                    PreparedStatement pst = ConnectionUtil.conDB().
                            prepareStatement("UPDATE users SET "
                                    + "first_name = ?,"
                                    + "last_name = ?,"
                                    + "role_id = ?,"
                                    + "user_name = ? "
                                    + "WHERE first_name = ? AND last_name = ? AND role_id = ? AND user_name = ? ");

                    int new_role = (userRoleComboBox.getValue().equals(admin)) ? 1 : 2;

                    pst.setString(1, firstNameTextField.getText());
                    pst.setString(2, lastNameTextField.getText());
                    pst.setInt(3, new_role);
                    pst.setString(4, usernameTextField.getText());

                    pst.setString(5, firstNameParameter);
                    pst.setString(6, lastNameParameter);
                    pst.setInt(7, current_role);
                    pst.setString(8, userNameParameter);

                    if(pst.executeUpdate() == 1){
                        System.out.println("USER DETAILS UPDATED");
                        Alert notify = new Alert(Alert.AlertType.NONE);
                        {
                            notify.setTitle("update");
                            notify.getButtonTypes().clear();
                            notify.getDialogPane().getButtonTypes().addAll( OK );
                            notify.setContentText("SUCCESSFULLY UPDATED");
                            notify.showAndWait();
                        }
                        updateTableView();
                    }else{
                        Alert notify = new Alert(Alert.AlertType.NONE);
                        {
                            notify.setTitle("update");
                            notify.getButtonTypes().clear();
                            notify.getDialogPane().getButtonTypes().addAll( OK );
                            notify.setContentText("ERROR");
                            notify.showAndWait();
                        }
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(ManageUsersViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
    }
    
    //##########################################################################
    
    @FXML
    private void removeUser(ActionEvent event) {
        
        try {
            Connection con = ConnectionUtil.conDB();
            
            
            ButtonType YES = new ButtonType("YES", ButtonBar.ButtonData.YES);
            ButtonType NO = new ButtonType("NO", ButtonBar.ButtonData.CANCEL_CLOSE);
            ButtonType OK = new ButtonType("OK", ButtonBar.ButtonData.CANCEL_CLOSE);
            Optional<ButtonType> result;


            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            {
                confirmation.setTitle("DELETE DIALOG");
                confirmation.getButtonTypes().clear();
                confirmation.getDialogPane().getButtonTypes().addAll( YES, NO );
                confirmation.setContentText("ARE YOU SURE YOU WANT TO DELETE " + userNameParameter + "?");
            }
                    
                    result = confirmation.showAndWait();
                    
                    if(result.orElse(NO) == YES){//user confirms that they wish to delete
                        PreparedStatement pst  = con.prepareStatement("DELETE FROM users WHERE user_name = ?;");
                        pst.setString(1, userNameParameter);

                        if(pst.executeUpdate() == 1){//user has permission
                            
                           Alert alert = new Alert(Alert.AlertType.NONE);
                           {
                               alert.setTitle("update");
                               alert.getButtonTypes().clear();
                               alert.getDialogPane().getButtonTypes().addAll( OK );
                               alert.setContentText("SUCCESSFULLY DELETED");
                               clearFields();
                               alert.showAndWait();
                           }

                        }else{

                            Alert alert = new Alert(Alert.AlertType.NONE);
                            {
                                alert.setTitle("deletion");
                                alert.getButtonTypes().clear();
                                alert.getDialogPane().getButtonTypes().addAll( OK );
                                alert.setContentText("CHECK VALIDATION");
                                alert.showAndWait();
                            }
                        }
                        
                        updateTableView();
                    }            
            
            con.close();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        
    }
    
    //##########################################################################
    
    @FXML
    private void closeDialog(ActionEvent event) {
        Node  source = (Node)  event.getSource(); 
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void clearFields() {
        manageUsersTable.getSelectionModel().clearSelection();
        firstNameTextField.clear();
        lastNameTextField.clear();
        usernameTextField.clear();
        updateUserBUTTON.setDisable(true);
        removeUserBUTTON.setDisable(true);
    }

}
