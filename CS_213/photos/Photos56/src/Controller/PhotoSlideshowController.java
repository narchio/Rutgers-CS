package Controller;

import Model.Album;
import Model.Application;
import Model.Photo;
import Model.Tag;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;

/**
 * The class PhotoSlideshowController will provide slideshow functionality for an album
 * @author Nicolas Carchio
 * @author Adam Romano
 */
public class PhotoSlideshowController {
    /**
     * Label to hold current album name
     */
    @FXML
    private Label photoCaptionLabel;
    /**
     * ImageView to display the current image
     */
    @FXML
    private ImageView imageView;
    /**
     * List view of tags for the current photo
     */
    @FXML
    private ListView<Tag> photoTags;
    /**
     * Observable list of photos that will be looked through
     */
    private ObservableList<Photo> obsPhotoList = InsideAlbumController.getObsPhotoList();
    /**
     * Previous Scene
     */
    private Scene previousScene;

    /**
     * Called on scene change, initializes scene to display photo caption, date-time capture of photo, photo, and photo's tags
     */ 
    public void initialize() {
        previousScene = Application.getCurrentScene();
        Application.setCurrentScene(photoCaptionLabel.getScene());
        // need to select current photo in the observable list and display it
        Photo currentPhoto = Application.getCurrentUser().getCurrentPhoto();
        // set the photo caption
        photoCaptionLabel.setText(currentPhoto.getCaption()+"\n"+currentPhoto.getDate().getTime());
        // display in imageView
        imageView.setFitHeight(450);
        imageView.setFitWidth(450);
        imageView.setImage(currentPhoto.getThumbnail().getImage());
        photoCaptionLabel.setGraphic(imageView);
        photoCaptionLabel.setContentDisplay(ContentDisplay.BOTTOM);
        //set the tags for the photo
        ObservableList<Tag> tags = FXCollections.observableArrayList();
        tags.setAll(Application.getCurrentUser().getCurrentPhoto().getTags());
        photoTags.setItems(tags);
    }


    /**
     * Displays the previous photo if applicable
     * @param event
     */
    public void goLeft(ActionEvent event) {
        // get the index of the current pic
        Photo p = Application.getCurrentUser().getCurrentPhoto();
        int index = Application.getCurrentUser().getCurrentAlbum().getPhotos().indexOf(p);
        // if the current index - 1 is greater than or equal to 0
        if (index-1 >= 0) {
            // now new photo
            Photo newPhoto = obsPhotoList.get(index-1);
            Application.getCurrentUser().setCurrentPhoto(newPhoto);
            // set the photo caption
            photoCaptionLabel.setText(newPhoto.getCaption()+"\n"+newPhoto.getDate().getTime());
            // display in imageView
            imageView.setFitHeight(450);
            imageView.setFitWidth(450);
            imageView.setImage(newPhoto.getThumbnail().getImage());
            photoCaptionLabel.setGraphic(imageView);
            photoCaptionLabel.setContentDisplay(ContentDisplay.BOTTOM);
            //update the tags
            ObservableList<Tag> tags = FXCollections.observableArrayList();
            tags.setAll(Application.getCurrentUser().getCurrentPhoto().getTags());
            photoTags.setItems(tags);

        } // else, then it is out of bounds and display an error
        else {
            Alert outOfBounds = new Alert(Alert.AlertType.ERROR, "You are currently on the first photo, please go forward!");
            outOfBounds.showAndWait();
            return;
        }
    }

    /**
     * Displays the next photo if applicable
     * @param event
     */
    public void goRight(ActionEvent event) {
        // get the index of the current pic
        Photo p = Application.getCurrentUser().getCurrentPhoto();
        int index = Application.getCurrentUser().getCurrentAlbum().getPhotos().indexOf(p);
        // if the next index is less than or equal to the last index, display next pic
        if (index+1 <= Application.getCurrentUser().getCurrentAlbum().getPhotos().size()-1) {
            // now new photo
            Photo newPhoto = obsPhotoList.get(index+1);
            Application.getCurrentUser().setCurrentPhoto(newPhoto);
            // set the photo caption
            photoCaptionLabel.setText(newPhoto.getCaption()+"\n"+newPhoto.getDate().getTime());
            // display in imageView
            imageView.setFitHeight(450);
            imageView.setFitWidth(450);
            imageView.setImage(newPhoto.getThumbnail().getImage());
            photoCaptionLabel.setGraphic(imageView);
            photoCaptionLabel.setContentDisplay(ContentDisplay.BOTTOM);
            //update the tags
            ObservableList<Tag> tags = FXCollections.observableArrayList();
            tags.setAll(Application.getCurrentUser().getCurrentPhoto().getTags());
            photoTags.setItems(tags);
        } // else, then it is out of bounds and display an error
        else {
            Alert outOfBounds = new Alert(Alert.AlertType.ERROR, "There are no photos left, please go back!");
            outOfBounds.showAndWait();
            return;
        }
    }


    /**
     * Brings the user back to the album
     * @param event
     */
    public void back(ActionEvent event) throws IOException {
        //get the stage
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        //switch the scenes
        stage.setScene(previousScene);
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
