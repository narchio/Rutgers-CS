package Controller;

import Model.Application;
import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

/**
 * The LoginController class will handle login functionality for the photos application
 * @author Nicolas Carchio
 * @author Adam Romano
 */
public class LoginController {
    /**
     * Text field to enter the username on the login screen
     */
    @FXML
    private TextField usernameField;

    /**
     * Login handler for when username is entered and login button is pressed
     * @param event Button event
     */
    public void login(ActionEvent event) throws IOException, ClassNotFoundException {
        //load in the users from users.dat if any exist
        Application.readUsers();
        //get the inputted username
        String inputtedUsername = usernameField.getText();
        //check to see if its in the list
        if(Application.inList(inputtedUsername)){
            //get the stage
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            switch(inputtedUsername) {
                case "admin":
                    //create admin scene
                    Parent adminRoot = FXMLLoader.load(getClass().getResource("/View/Photos_Admin.fxml"));
                    Scene adminScene = new Scene(adminRoot, 1000, 750);
                    //swap scenes
                    stage.setScene(adminScene);
                    break;
                default:
                    // get current user
                    int u = Application.getIndexInUserList(inputtedUsername);
                    List<User> userList = Application.getUsers();
                    User currentUser = userList.get(u);
                    Application.setCurrentUser(currentUser);
                    //create non-admin scene
                    Parent nonAdminRoot = FXMLLoader.load(getClass().getResource("/View/Photos_Non_Admin.fxml"));
                    Scene nonAdminScene = new Scene(nonAdminRoot, 1000, 750);
                    //swap scenes
                    stage.setScene(nonAdminScene);
            }
        }
        //user is not in the list
        else{
            Alert invalidUserAlert = new Alert(Alert.AlertType.ERROR, inputtedUsername +" is not a valid user!");
            invalidUserAlert.showAndWait();
        }
    }
}
