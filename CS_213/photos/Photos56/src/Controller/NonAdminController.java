package Controller;

import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.List;

/***
 * The class NonAdminController will provide functionality on the non-admin screen for the photos application
 * @author Nicolas Carchio
 * @author Adam Romano
 */
public class NonAdminController {
    /**
     * Textfield to enter album name for new album or renaming an album
     */
    @FXML
    private TextField albumname;
    /**
     * TextArea to display the selected albumDetails to user
     */
    @FXML
    private TextArea albumdetails;
    /**
     * List of albums displayed on nonAdmin screen
     */
    @FXML
    private ListView<Album> albumlist;
    /**
     * Observable list of albums that will be displayed to the user
     */
    private static ObservableList<Album> obsList = FXCollections.observableArrayList();
    /**
     * Current stage
     */
    private Stage stage;
    /**
      * Loads the list of albums when scene boots up
     */
    public void initialize() {
        try {
            // populate observable list with albums
            if (obsList.size() > 0 || (Application.getCurrentUser().getAlbums() != null)) {
                obsList.setAll(Application.getCurrentUser().getAlbums());
            }
            albumlist.setItems(obsList);
            albumlist.getSelectionModel().select(0);
            // add the display
            Album currAlbum = albumlist.getSelectionModel().getSelectedItem();
            if (currAlbum != null) {
                showAlbumDetails();
            }
        } catch (NullPointerException n) {
            n.printStackTrace();
        }
        // add listener to update the album details
        albumlist.getSelectionModel().selectedIndexProperty().addListener(
                (obs, oldVal, newVal) -> showAlbumDetails());
        // set stage and add listener to check for a scene change (into an album)
        stage = (Stage) Stage.getWindows().get(0);
        albumlist.setOnMouseClicked(mouseEvent -> {
            try {
                insideAlbum(mouseEvent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    /**
     * Keeps updating the albumDetails text field
     */
    public void showAlbumDetails() {
        Album currentAlbum = albumlist.getSelectionModel().getSelectedItem();
        Application.getCurrentUser().setCurrentAlbum(currentAlbum);
        if (currentAlbum != null) {
            int index = Application.getCurrentUser().getAlbums().indexOf(currentAlbum);
            albumdetails.setText("Album Name: " + Application.getCurrentUser().getAlbums().get(index));
            albumdetails.appendText("\nPhoto Count: " + Application.getCurrentUser().getAlbums().get(index).getPhotos().size());
            albumdetails.appendText("\nDate Range: " + Application.getCurrentUser().getCurrentAlbum().getDateRange());
        }
    }
    /**
     * Handles event of a double click into an album (changes scene)
     */
    public void insideAlbum(MouseEvent mouseEvent) throws IOException {
        Album clicked;
        // if there are two clicks on the selected item, then we will change the scene
        if (mouseEvent.getClickCount() == 2) {
            clicked = albumlist.getSelectionModel().getSelectedItem();
            Application.getCurrentUser().setCurrentAlbum(clicked);
            // now change scene
            Parent insideAlbumRoot = FXMLLoader.load(getClass().getResource("/View/Photos_Inside_Album.fxml"));
            Scene insideAlbumScene = new Scene(insideAlbumRoot, 1000, 750);
            //swap scenes
            stage.setScene(insideAlbumScene);
        }
    }

    /**
     * Creates an album for the current user
     * @param event
     */
    public void createAlbum(ActionEvent event) {
        // get the user's album name
        String albumEntered = albumname.getText().strip();
        // check if it is a duplicate album
        if (Application.getCurrentUser().inAlbumList(albumEntered)) {
            Alert duplicateAlbum = new Alert(Alert.AlertType.ERROR, "Album with the name "+albumEntered + "already exists!");
            duplicateAlbum.showAndWait();
        }
        //check that an actual album name was inputted
        else if(albumEntered.trim().isEmpty()){
            Alert invalidAlbumAdd = new Alert(Alert.AlertType.ERROR, "Please enter a valid album name!");
            invalidAlbumAdd.showAndWait();
            return;
        }
        // if valid input, create the album and add it to the user's album list
        else {
            Alert addAlbum = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to create album "+albumEntered+" ?", ButtonType.YES, ButtonType.NO);
            addAlbum.showAndWait();
            if(addAlbum.getResult() == ButtonType.YES) {
                Album newAlbum = new Album(albumEntered);
                Application.getCurrentUser().addAlbum(newAlbum);
                // set the list and the UI
                obsList.setAll(Application.getCurrentUser().getAlbums());
                albumlist.setItems(obsList);
                albumlist.getSelectionModel().select(newAlbum);
                showAlbumDetails();
                albumname.clear();
            }
        }
    }
    /**
     * Renames an album of the current user
     * @param event
     */
    public void renameAlbum(ActionEvent event) {
        // get the user's album name
        String albumChange = albumname.getText().strip();
        Album currentAlbum = albumlist.getSelectionModel().getSelectedItem();
        //check if there is an album selected
        if(currentAlbum == null) {
            Alert invalidRename = new Alert(Alert.AlertType.ERROR, "There are no albums to rename!");
            invalidRename.showAndWait();
            return;
        }
        // check if it is a duplicate album name that they want to change it to
        if (Application.getCurrentUser().inAlbumList(albumChange)) {
            Alert duplicateAlbum = new Alert(Alert.AlertType.ERROR, "Album with the name "+albumChange+" already exists!");
            duplicateAlbum.showAndWait();
        }
        // if there is a blank input, Alert and ask them to input a valid alblum name
        else if (albumChange.equals("")) {
            Alert emptyInput = new Alert(Alert.AlertType.ERROR, "Album with no name is invalid, please enter a valid name.");
            emptyInput.showAndWait();
        }
        // if valid input, rename the album add it to the user's album list
        else {
            Alert renameAlbum = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to rename album "+currentAlbum.getName()+" to "+albumChange+"?", ButtonType.YES, ButtonType.NO);
            renameAlbum.showAndWait();
            if(renameAlbum.getResult() == ButtonType.YES){
                // rename the album
                int index = Application.getCurrentUser().getAlbums().indexOf(currentAlbum);
                Application.getCurrentUser().getAlbums().get(index).setName(albumChange);
                // set the list and the UI
                obsList.setAll(Application.getCurrentUser().getAlbums());
                albumlist.setItems(obsList);
                albumlist.getSelectionModel().select(Application.getCurrentUser().getAlbums().get(index));
                showAlbumDetails();
                albumname.clear();
            }
        }
    }
    /**
     * Deletes an album from the current user
     */
    public void deleteAlbum() {
        if(albumlist.getSelectionModel().getSelectedItem() == null){
            Alert invalidDelete = new Alert(Alert.AlertType.ERROR, "There are no albums to delete!");
            invalidDelete.showAndWait();
            return;
        }
        Alert deleteAlbum = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete album "+albumlist.getSelectionModel().getSelectedItem().getName()+"?", ButtonType.YES, ButtonType.NO);
        deleteAlbum.showAndWait();
        if(deleteAlbum.getResult() == ButtonType.YES){
            // get the selected item
            int index = albumlist.getSelectionModel().getSelectedIndex();
            // delete it
            Application.getCurrentUser().getAlbums().remove(index);
            obsList.remove(index);
            albumlist.setItems(obsList);
            // null check for deleting last item
            if (albumlist.getSelectionModel().getSelectedItem() != null) {
                albumlist.getSelectionModel().select(index);
                showAlbumDetails();
            } else {
                albumdetails.clear();
            }
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
