package Controller;

import Model.Application;
import Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Comparator;

/**
 * The class AdminController will provide functionality on the admin screen to the photos application
 * @author Nicolas Carchio
 * @author Adam Romano
 */
public class AdminController {
    /**
     * Textfield to enter a username for a new user
     */
    @FXML
    private TextField usernameField;
    /**
     * List of users displayed on admin screen
     */
    @FXML
    private ListView<User> userList;
    /**
     * Observable list for list of users
     */
    private static ObservableList<User> obsList = FXCollections.observableArrayList();
    /**
     * Loads the list of users when scene boots up
     */
    public void initialize() {
        obsList.setAll(Application.getUsers());
        userList.setItems(obsList);
        userList.getSelectionModel().select(0);
    }
    /**
     * Adds a user to the list of users who have access to the photos application
     * @param event
     */
    public void addUser(ActionEvent event) {
        //get the username that they entered
        String usernameEntered = usernameField.getText();
        //check if that username exists already
        if(Application.inList(usernameEntered)){
            Alert duplicateUser = new Alert(Alert.AlertType.ERROR, "User with username "+usernameEntered+" already exists!");
            duplicateUser.showAndWait();
            return;
        }
        //check that a username was actually entered
        else if(usernameEntered.trim().isEmpty()) {
            Alert badInput = new Alert(Alert.AlertType.ERROR, "Please enter a valid username!");
            badInput.showAndWait();
            return;
        }
        //add the user
        else{
            Alert verifyAdd = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to create new user "+usernameEntered + " ?", ButtonType.NO, ButtonType.YES);
            verifyAdd.showAndWait();
            if(verifyAdd.getResult() == ButtonType.YES) {
                User newUser = new User(usernameEntered);
                Application.addUser(newUser);
                int newSelectionIndex = Application.getIndexInUserList(usernameEntered);
                obsList.setAll(Application.getUsers());
                userList.setItems(obsList);
                userList.getSelectionModel().select(newSelectionIndex);
                usernameField.clear();
            }
        }
    }

    /**
     * Removes a user from the list of users who have access to the photos application
     * @param event
     */
    public void deleteUser(ActionEvent event) {
        //get the selected user
        int selectedIndex = userList.getSelectionModel().getSelectedIndex();
        if(selectedIndex < 0) {
            Alert invalidDelete = new Alert(Alert.AlertType.ERROR, "There are no users to delete!");
            invalidDelete.showAndWait();
            return;
        }
        Alert verifyDelete = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete user "+userList.getSelectionModel().getSelectedItem().getUsername() + " ?", ButtonType.NO, ButtonType.YES);
        verifyDelete.showAndWait();
        if(verifyDelete.getResult() == ButtonType.YES) {
            obsList.remove(selectedIndex);
            Application.getUsers().remove(selectedIndex);
            userList.setItems(obsList);
        }
    }

    /**
     * Brings the user back to the login screen
     * @param event
     */
    public void logout(ActionEvent event) throws IOException {
        Alert verifyLogout = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to logout?", ButtonType.NO, ButtonType.YES);
        verifyLogout.showAndWait();
        if(verifyLogout.getResult() == ButtonType.YES){
            //serialize the users
            Application.writeUsers();
            //go back to the login screen
            Parent root = FXMLLoader.load(getClass().getResource("/View/Photos_Login.fxml"));
            Scene scene = new Scene(root, 1000, 750);
            //get the stage
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            //switch the scenes
            stage.setScene(scene);
        }
    }
}
