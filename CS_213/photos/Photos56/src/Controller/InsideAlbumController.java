package Controller;

import Model.*;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * The class InsideAlbumController will provide functionality for the application when inside an a photo album
 * @author Nicolas Carchio
 * @author Adam Romano
 */
public class InsideAlbumController {
    /**
     * Label to hold current album name
     */
    @FXML
    private Label albumName;
    /**
     * Label to hold selected photo from the file picker
     */
    @FXML
    private Label filePickerSelectedPhoto;
    /**
     * TextField to get the inputted tagname / tagvalue
     */
    @FXML
    private TextField tagnamevalue;

    /**
     * TextField to get the inputted caption
     */
    @FXML
    private TextField caption;

    /**
     * List of photos displayed from the current album
     */
    @FXML
    private ListView<Photo> photolist;
    /**
     * TextField to get the album to copy/move photo to
     */
    @FXML
    private TextField albumField;
    /**
     * List of tags displayed for the current image
     */
    @FXML
    private ListView<Tag> taglist;
    /**
     * List of default tags to choose from
     */
    @FXML
    private ComboBox defaultTagComboBox;
    /**
     * Stores the chosen photo after a file pick
     */
    private File fd;
    /**
     * Observable list of photos that will be displayed to the user
     */
    private static ObservableList<Photo> obsPhotoList = FXCollections.observableArrayList();
    /**
     * Observable list of tags for the current photo that will be displayed to the user
     */
    private static ObservableList<Tag> obsTagList = FXCollections.observableArrayList();
    /**
     * Observable list of default tags to choose from
     */
    private static ObservableList<String> obsDefaultTagList = FXCollections.observableArrayList();
    /**
     * Current stage
     */
    private Stage stage;
    /**
     * Getter for photo list
     */
    public ListView<Photo> getPhotolist() {
        return photolist;
    }
    /**
     * Getter for obs photo list
     */
    public static ObservableList<Photo> getObsPhotoList() {
        return obsPhotoList;
    }
    /**
     * Getter for the obs tag list
     */
    public static ObservableList<Tag> getObsTagList() { return obsTagList; }
    /**
     * Loads the list of photos when scene boots up
     */
    public void initialize() {
        //override listview of photo list so thumbnails display instead of text
        photolist.setCellFactory(listView -> new ListCell<Photo>() {
            private ImageView imageView = new ImageView();
            private Label caption = new Label();
            @Override
            public void updateItem(Photo item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText("");
                } else {
                    caption.setText(item.getCaption());
                    imageView.setFitHeight(150);
                    imageView.setFitWidth(150);
                    imageView.setImage(item.getThumbnail().getImage());
                    caption.setGraphic(imageView);
                    caption.setContentDisplay(ContentDisplay.TOP);
                    setGraphic(caption);
                }
            }
        });

        stage = (Stage) Stage.getWindows().get(0);
        // set the albumname
        albumName.setText(Application.getCurrentUser().getCurrentAlbum().toString());
        try {
            // populate photo list with lists of photos and select first photo
            if (obsPhotoList.size() > 0 || (Application.getCurrentUser().getCurrentAlbum().getPhotos() != null)) {
                obsPhotoList.setAll(Application.getCurrentUser().getCurrentAlbum().getPhotos());
            }

            // sets the observable list for the photos
            photolist.setItems(obsPhotoList);
            photolist.getSelectionModel().select(0);
            photolist.setOrientation(Orientation.HORIZONTAL);

            //load the list of default tags
            obsDefaultTagList.setAll(Application.getCurrentUser().getDefaultTagsAsStrings());
            defaultTagComboBox.setItems(obsDefaultTagList);
            defaultTagComboBox.setPromptText("Please select...");


            //load the tags for the selected photo if any exist
            if(photolist.getSelectionModel().getSelectedItem() != null && photolist.getSelectionModel().getSelectedItem().getTags() != null && photolist.getSelectionModel().getSelectedItem().getTags().size() > 0){
                obsTagList.setAll(photolist.getSelectionModel().getSelectedItem().getTags());
                taglist.setItems(obsTagList);
            }

            photolist.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Photo>() {
                @Override
                public void changed(ObservableValue<? extends Photo> observable, Photo oldValue, Photo newValue) {
                    if (newValue != null) {
                        obsTagList.setAll(newValue.getTags());
                        taglist.setItems(obsTagList);
                    } else {
                        obsTagList.clear();
                        taglist.setItems(obsTagList);
                    }
                }
            });

            // set stage and add listener to check for a scene change (into a photo)
            photolist.setOnMouseClicked(mouseEvent -> {
                try {
                    insidePhoto(mouseEvent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            // if there is an error, report it
        } catch (NullPointerException n) {
            n.printStackTrace();
        }
    }

    /**
     * Handles event of a double click on a photo(changes scene)
     */
    public void insidePhoto(MouseEvent mouseEvent) throws IOException {
        Photo clicked;
        // if there are two clicks on the selected item, then we will change the scene
        if (mouseEvent.getClickCount() == 2) {
            //set the current scene
            Application.setCurrentScene(albumName.getScene());
            clicked = photolist.getSelectionModel().getSelectedItem();
            Application.getCurrentUser().setCurrentPhoto(clicked);
            // now change scene
            Parent insidePhotoRoot = FXMLLoader.load(getClass().getResource("/View/Photos_Photo_Selected:Slideshow.fxml"));
            Scene insidePhoto = new Scene(insidePhotoRoot, 1000, 750);
            //swap scenes
            stage.setScene(insidePhoto);
        }
    }


    /**
     * Add a photo to an album
     */
    public void addPhoto(ActionEvent event) {
        //check to see if photo was selected
        if(filePickerSelectedPhoto.getText().equals("")){
            Alert noSelectedPhoto = new Alert(Alert.AlertType.ERROR, "Please select a photo to add!");
            noSelectedPhoto.showAndWait();
            return;
        }
        //create a new photo from file chooser
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);
        File photo = fd.getAbsoluteFile();
        cal.setTimeInMillis(photo.lastModified());
        if(photo != null){
            Photo newPhoto = new Photo(photo.getName(), "no caption", new Thumbnail(new Image("file:"+photo.getPath())), cal);
            //check if the photo exists already
            for(Photo p: Application.getCurrentUser().getCurrentAlbum().getPhotos()){
                if(p.getName().equals(newPhoto.getName())){
                    Alert duplicatePhoto = new Alert(Alert.AlertType.ERROR, "Photo with name "+newPhoto.getName()+" already exists!");
                    duplicatePhoto.showAndWait();
                    return;
                }
            }
            Alert addPhoto = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to add "+newPhoto.getName()+" to album "+Application.getCurrentUser().getCurrentAlbum().getName()+"?", ButtonType.YES, ButtonType.NO);
            addPhoto.showAndWait();
            if(addPhoto.getResult() == ButtonType.YES){
                Application.getCurrentUser().getCurrentAlbum().getPhotos().add(newPhoto);
                obsPhotoList.add(newPhoto);
                photolist.setItems(obsPhotoList);
                photolist.getSelectionModel().select(newPhoto);
            }
            filePickerSelectedPhoto.setText("");
        }
    }

    /**
     * Remove a photo from an album
     */
    public void removePhoto() {
        if(photolist.getSelectionModel().getSelectedItem() == null){
            Alert invalidRemove = new Alert(Alert.AlertType.ERROR, "There are no photos to remove!");
            invalidRemove.showAndWait();
            return;
        }
        Alert removePhoto = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to remove "+photolist.getSelectionModel().getSelectedItem().getName()+" from "+Application.getCurrentUser().getCurrentAlbum()+"?", ButtonType.YES, ButtonType.NO);
        removePhoto.showAndWait();
        if(removePhoto.getResult() == ButtonType.YES){
            // get photo to be removed (selected photo)
            int index = photolist.getSelectionModel().getSelectedIndex();
            // remove it
            Application.getCurrentUser().getCurrentAlbum().getPhotos().remove(index);
            obsPhotoList.remove(index);
            photolist.setItems(obsPhotoList);
        }
    }

    /**
     * Choose a photo
     */
    public void choosePhoto() {
        // get the file from the picker
        FileChooser fileChooser = new FileChooser();
        fd = fileChooser.showOpenDialog(stage);
        if(fd == null){
            filePickerSelectedPhoto.setText("");
        }
        else{
            filePickerSelectedPhoto.setText(fd.getName());
        }
    }

    /**
     * Add a tag to a photo
     */
    public void addTag() {
        if(photolist.getSelectionModel().getSelectedItem() == null) {
            Alert badAdd = new Alert(Alert.AlertType.ERROR, "There are no photos to add a caption to!");
            badAdd.showAndWait();
            return;
        }
        else if(defaultTagComboBox.getSelectionModel().getSelectedItem() == null) {
            Alert badAdd = new Alert(Alert.AlertType.ERROR, "You must select a tag type before adding a tag!");
            badAdd.showAndWait();
            return;
        }
        //get the value of the combo box
        String comboBoxResult = defaultTagComboBox.getSelectionModel().getSelectedItem().toString();
        //get the value inputted into text field
        String tagField = tagnamevalue.getText().trim();
        if(tagField.equals("")){
            Alert badInput = new Alert(Alert.AlertType.ERROR, "Invalid input, please enter a tag name or tag value!");
            badInput.showAndWait();
            return;
        }
        else if(comboBoxResult.equals("Create new default tag")){
            Alert newDefaultTag = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to create new default tag "+tagField+"?", ButtonType.NO, ButtonType.YES);
            newDefaultTag.showAndWait();
            if(newDefaultTag.getResult() == ButtonType.YES){
                //create a new default tag
                Application.getCurrentUser().addDefaultTag(new Tag(tagField, ""));
                //reload default list of choices
                obsDefaultTagList.setAll(Application.getCurrentUser().getDefaultTagsAsStrings());
                defaultTagComboBox.setItems(obsDefaultTagList);
                //reset the combo box
                defaultTagComboBox.setPromptText("Please select...");
                tagnamevalue.clear();
            }
        }
        else{
            Alert addPhotoTag = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to add "+comboBoxResult+": "+tagField+" to "+photolist.getSelectionModel().getSelectedItem().getCaption()+"?", ButtonType.YES, ButtonType.NO);
            addPhotoTag.showAndWait();
            if(addPhotoTag.getResult() == ButtonType.YES){
                //add tag to the selected photo's list of tags
                photolist.getSelectionModel().getSelectedItem().addTag(new Tag(comboBoxResult, tagField));
                //reload tags for selected photo
                obsTagList.setAll(photolist.getSelectionModel().getSelectedItem().getTags());
                taglist.setItems(obsTagList);
            }
        }
        tagnamevalue.clear();
        defaultTagComboBox.setPromptText("Please select...");
    }

    /**
     * Delete a tag from a photo
     */
    public void deleteTag() {
        if(photolist.getSelectionModel().getSelectedItem() == null) {
            Alert badDelete = new Alert(Alert.AlertType.ERROR, "There are no photos which contain tags you can delete!");
            badDelete.showAndWait();
            return;
        }
        else if(taglist.getSelectionModel().getSelectedItem() == null) {
            Alert badDelete = new Alert(Alert.AlertType.ERROR, "There are no tags you can delete for this photo!");
            badDelete.showAndWait();
            return;
        }
        Tag tagToRemove = taglist.getSelectionModel().getSelectedItem();
        Alert deleteTag = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete tag "+tagToRemove.toString()+"?", ButtonType.NO, ButtonType.YES);
        deleteTag.showAndWait();
        if(deleteTag.getResult() == ButtonType.YES){
            //remove the tag from the observablelist
            obsTagList.remove(tagToRemove);
            //remove the tag from the actual list
            photolist.getSelectionModel().getSelectedItem().getTags().remove(tagToRemove);
            //reload the list of tags
            taglist.setItems(obsTagList);
        }
    }

    /**
     * Move a photo
     */
    public void moveTo() {
        if(photolist.getSelectionModel().getSelectedItem() == null) {
            Alert badMove = new Alert(Alert.AlertType.ERROR, "There are no photos for you to move!");
            badMove.showAndWait();
            return;
        }
        //get the inputted album
        String moveToAlbum = albumField.getText().trim();
        if(moveToAlbum.equals(Application.getCurrentUser().getCurrentAlbum().getName())){
            Alert badMove = new Alert(Alert.AlertType.ERROR, "Cannot move photo from current album into current album!");
            badMove.showAndWait();
            return;
        }
        //check to make sure the album exists
        boolean exists = false;
        for(Album a: Application.getCurrentUser().getAlbums()){
            if(a.getName().equals(moveToAlbum)){
                exists = true;
                if (a.inPhotoList(photolist.getSelectionModel().getSelectedItem())) {
                    Alert invalidDestination = new Alert(Alert.AlertType.ERROR, "Unable to move duplicate photo "+photolist.getSelectionModel().getSelectedItem()+"!");
                    invalidDestination.showAndWait();
                    return;
                }
                break;
            }
        }
        if(exists){
            //confirm action
            Alert movePhoto = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to move "+photolist.getSelectionModel().getSelectedItem().getCaption()+" to album "+moveToAlbum+"?", ButtonType.YES, ButtonType.NO);
            movePhoto.showAndWait();
            if(movePhoto.getResult() == ButtonType.YES){
                //get the photo
                Photo photo = photolist.getSelectionModel().getSelectedItem();
                int index = photolist.getSelectionModel().getSelectedIndex();
                //remove the photo from the current list
                Application.getCurrentUser().getCurrentAlbum().getPhotos().remove(photo);
                //reload the current photos lists
                obsPhotoList.setAll(Application.getCurrentUser().getCurrentAlbum().getPhotos());
                photolist.setItems(obsPhotoList);
                if (index > 0) {
                    photolist.getSelectionModel().select(index-1);
                }
                //move the photo
                Application.getCurrentUser().getAlbums().get(Application.getCurrentUser().getAlbumIndexInList(moveToAlbum)).addPhoto(photo);
                //clear the text field
                albumField.clear();
            }
        }
        else{
            Alert invalidDestination = new Alert(Alert.AlertType.ERROR, "Unable to move photo, album "+moveToAlbum+" does not exist!");
            invalidDestination.showAndWait();
        }

    }

    /**
     * Copy a photo
     */
    public void copyTo() {
        if(photolist.getSelectionModel().getSelectedItem() == null) {
            Alert badCopy = new Alert(Alert.AlertType.ERROR, "There are no photos for you to copy!");
            badCopy.showAndWait();
            return;
        }
        //get the inputted album
        String copyToAlbum = albumField.getText().trim();
        if(copyToAlbum.equals(Application.getCurrentUser().getCurrentAlbum().getName())){
            Alert badCopy = new Alert(Alert.AlertType.ERROR, "Cannot copy photo from current album into current album!");
            badCopy.showAndWait();
            return;
        }
        //check to make sure the album exists
        boolean exists = false;
        for(Album a: Application.getCurrentUser().getAlbums()){
            if(a.getName().equals(copyToAlbum)){
                exists = true;
                if (a.inPhotoList(photolist.getSelectionModel().getSelectedItem())) {
                    Alert invalidDestination = new Alert(Alert.AlertType.ERROR, "Unable to copy duplicate photo "+photolist.getSelectionModel().getSelectedItem()+"!");
                    invalidDestination.showAndWait();
                    return;
                }
                break;
            }
        }
        if(exists){
            //confirm action
            Alert movePhoto = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to copy "+photolist.getSelectionModel().getSelectedItem().getCaption()+" to album "+copyToAlbum+"?", ButtonType.YES, ButtonType.NO);
            movePhoto.showAndWait();
            if(movePhoto.getResult() == ButtonType.YES){
                //get the photo
                Photo photo = photolist.getSelectionModel().getSelectedItem();
                //copy the photo
                Application.getCurrentUser().getAlbums().get(Application.getCurrentUser().getAlbumIndexInList(copyToAlbum)).addPhoto(photo);
                //clear the text field
                albumField.clear();
            }
        }
        else{
            Alert invalidDestination = new Alert(Alert.AlertType.ERROR, "Unable to copy photo, album "+copyToAlbum+" does not exist!");
            invalidDestination.showAndWait();
        }
    }

    /**
     * Add a caption to a photo
     */
    public void addCaption() {
        //get the new caption to caption/recaption the photo with
        String inputtedCaption = caption.getText();
        //check to see that there is a caption inputted and not blank field
        if(inputtedCaption.equals("")){
            Alert badCaption = new Alert(Alert.AlertType.ERROR, "Please enter a valid caption!");
            badCaption.showAndWait();
            return;
        }
        //check to see that there is a photo to caption/recaption
        else if(photolist.getSelectionModel().getSelectedItem() == null){
            Alert invalidCaption = new Alert(Alert.AlertType.ERROR, "There are no photos to caption/recaption!");
            invalidCaption.showAndWait();
            return;
        }
        Alert captionPhoto = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to caption "+photolist.getSelectionModel().getSelectedItem().getCaption()+" to "+inputtedCaption+"?", ButtonType.YES, ButtonType.NO);
        //get the photo to modify
        Photo photoToCaption = photolist.getSelectionModel().getSelectedItem();
        captionPhoto.showAndWait();
        if(captionPhoto.getResult() == ButtonType.YES){
            //modify the photo
            List<Photo> photos = Application.getCurrentUser().getCurrentAlbum().getPhotos();
            for(Photo p: photos){
                if(p.equals(photoToCaption)){
                    p.setCaption(inputtedCaption);
                    break;
                }
            }
            //clear the text field
            caption.clear();
            //reload the photos
            obsPhotoList.setAll(Application.getCurrentUser().getCurrentAlbum().getPhotos());
            photolist.setItems(obsPhotoList);
        }
    }

    /**
     * Search the current album for certain criteria (scene change)
     */
    public void searchAlbum(ActionEvent event) throws IOException {
        //set the current scene
        Application.setCurrentScene(albumName.getScene());
        //load the fxml for the search screen
        Parent root = FXMLLoader.load(getClass().getResource("/View/Photos_Search.fxml"));
        //create the search screen scene
        Scene scene = new Scene(root, 1000, 750);
        //get the current stage
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        //switch the scenes
        stage.setScene(scene);
    }


    /**
     * Bring the user back to the list of albums
     */
    public void home(ActionEvent event) throws IOException {
        //serialize the users
        Application.writeUsers();
        //go back to the login screen
        Parent root = FXMLLoader.load(getClass().getResource("/View/Photos_Non_Admin.fxml"));
        Scene scene = new Scene(root, 1000, 750);
        //get the stage
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        //switch the scenes
        stage.setScene(scene);
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
    /**
     * Clear the search results
     */
    public void clearSearch(ActionEvent event) {
        obsPhotoList.setAll(Application.getCurrentUser().getCurrentAlbum().getPhotos());
        photolist.setItems(obsPhotoList);
        photolist.getSelectionModel().select(0);
    }
}
