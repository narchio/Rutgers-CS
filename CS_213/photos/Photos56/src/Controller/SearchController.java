package Controller;

import Model.Album;
import Model.Application;
import Model.Photo;
import Model.Tag;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * The class SearchController will provide search functionality within a photo album
 * @author Nicolas Carchio
 * @author Adam Romano
 */
public class SearchController {
    /**
     * Text field for single tag search
     */
    @FXML
    private TextField singleTag;
    /**
     * Date picker to pick date for lower bound of search
     */
    @FXML
    private DatePicker fromDate;
    /**
     * Date picker to pick date for upper bound of search
     */
    @FXML
    private DatePicker toDate;
    /**
     * Text field for first AND search clause
     */
    @FXML
    private TextField andField1;
    /**
     * Text field for second AND search clause
     */
    @FXML
    private TextField andField2;
    /**
     * Text field for first OR search clause
     */
    @FXML
    private TextField orField1;
    /**
     * Text field for second OR search clause
     */
    @FXML
    private TextField orField2;
    /**
     * Previous scene
     */
    private Scene previousScene;
    /**
     * List of searched photos
     */
    private List<Photo> searchedPhotos;

    /**
     * Initialize the screen
     */
    public void initialize() {
        //set the previous scene before updating it
        previousScene = Application.getCurrentScene();
        //update the current scene
        Application.setCurrentScene(fromDate.getScene());

    }
    /**
     * Go back to the album
     */
    public void back(ActionEvent event) {
        //swap scenes
        Stage stage = (Stage) Stage.getWindows().get(0);
        stage.setScene(previousScene);
    }
    /**
     * Logout of the application to the login screen
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
     * Search photos with the inputted parameters
     */
    public void search(ActionEvent event) throws IOException {
        searchedPhotos = new ArrayList<Photo>();
        //single tag search
        if(singleTag.getText().trim().isEmpty() == false && fromDate.getValue() == null && toDate.getValue() == null && andField1.getText().trim().isEmpty() && andField2.getText().trim().isEmpty() && orField1.getText().trim().isEmpty() && orField2.getText().trim().isEmpty()){
            if(singleTag.getText().trim().contains("=") == false){
                Alert badInput = new Alert(Alert.AlertType.ERROR, "Invalid input format, please enter using the following format: TAGNAME=TAGVALUE");
                badInput.showAndWait();
                return;
            }
            //get the tag inputted into the field
            String input = singleTag.getText().trim();
            Tag tag = new Tag(input.substring(0, input.indexOf('=')), input.substring(input.indexOf('=')+1));
            //loop through the photos to see if any have a matching tag
            for(Photo p: Application.getCurrentUser().getCurrentAlbum().getPhotos()){
                if(p.getTags().contains(tag)){
                    searchedPhotos.add(p);
                }
            }
        }
        //date range search
        else if(singleTag.getText().trim().isEmpty() && fromDate.getValue() != null && toDate.getValue() != null && andField1.getText().trim().isEmpty() && andField2.getText().trim().isEmpty() && orField1.getText().trim().isEmpty() && orField2.getText().trim().isEmpty()){
            //get the inputted dates
            Date date1 = Date.from(fromDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date date2 = Date.from(toDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            //loop through the photos to see if they fall within the dates
            for(Photo p: Application.getCurrentUser().getCurrentAlbum().getPhotos()){
                if(p.getDate().getTime().equals(date1) || p.getDate().getTime().equals(date2) || (p.getDate().getTime().after(date1) && p.getDate().getTime().before(date2))){
                    searchedPhotos.add(p);
                }
            }
        }
        //conjunctive search
        else if(singleTag.getText().trim().isEmpty() && fromDate.getValue() == null && toDate.getValue() == null && andField1.getText().trim().isEmpty() == false && andField2.getText().trim().isEmpty() == false && orField1.getText().trim().isEmpty() && orField2.getText().trim().isEmpty()){
            if(andField1.getText().trim().contains("=") == false || andField2.getText().trim().contains("=") == false){
                Alert badInput = new Alert(Alert.AlertType.ERROR, "Invalid input format, please enter using the following format: TAGNAME=TAGVALUE");
                badInput.showAndWait();
                return;
            }
            //get the inputted tags
            String input1 = andField1.getText().trim();
            String input2 = andField2.getText().trim();
            Tag tag1 = new Tag(input1.substring(0, input1.indexOf('=')), input1.substring(input1.indexOf('=')+1));
            Tag tag2 = new Tag(input2.substring(0, input2.indexOf('=')), input2.substring(input2.indexOf('=')+1));
            //loop through the photos
            for(Photo p: Application.getCurrentUser().getCurrentAlbum().getPhotos()){
                if(p.getTags().contains(tag1) && p.getTags().contains(tag2)){
                    searchedPhotos.add(p);
                }
            }
        }
        //disjunctive search
        else if(singleTag.getText().trim().isEmpty() && fromDate.getValue() == null && toDate.getValue() == null && andField1.getText().trim().isEmpty() && andField2.getText().trim().isEmpty() && orField1.getText().trim().isEmpty() == false && orField2.getText().trim().isEmpty() == false){
            if(orField1.getText().trim().contains("=") == false || orField2.getText().trim().contains("=") == false){
                Alert badInput = new Alert(Alert.AlertType.ERROR, "Invalid input format, please enter using the following format: TAGNAME=TAGVALUE");
                badInput.showAndWait();
                return;
            }
            //get the inputted tags
            String input1 = orField1.getText().trim();
            String input2 = orField2.getText().trim();
            Tag tag1 = new Tag(input1.substring(0, input1.indexOf('=')), input1.substring(input1.indexOf('=')+1));
            Tag tag2 = new Tag(input2.substring(0, input2.indexOf('=')), input2.substring(input2.indexOf('=')+1));
            //loop through the photos
            for(Photo p: Application.getCurrentUser().getCurrentAlbum().getPhotos()){
                if(p.getTags().contains(tag1) || p.getTags().contains(tag2)){
                    searchedPhotos.add(p);
                }
            }
        }
        //invalid search
        else{
            Alert invalidSearch = new Alert(Alert.AlertType.ERROR, "Invalid search input, please try again!");
            invalidSearch.showAndWait();
            return;
        }

        //check to see if there are search results
        if(searchedPhotos.size() == 0) {
            Alert noSearchResults = new Alert(Alert.AlertType.INFORMATION, "No search results found for given search parameters!");
            noSearchResults.showAndWait();
            return;
        }
        //there are search results
        else{
            //set the observable list
            ObservableList<Photo> obsList = InsideAlbumController.getObsPhotoList();
            obsList.setAll(searchedPhotos);
            //switch scenes back to the album
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            stage.setScene(previousScene);
        }
    }
    /**
     * Search photos and create album from the search results
     */
    public void createAlbumFromSearch(ActionEvent event) throws IOException {
        searchedPhotos = new ArrayList<Photo>();
        //single tag search
        if(singleTag.getText().trim().isEmpty() == false && fromDate.getValue() == null && toDate.getValue() == null && andField1.getText().trim().isEmpty() && andField2.getText().trim().isEmpty() && orField1.getText().trim().isEmpty() && orField2.getText().trim().isEmpty()){
            if(singleTag.getText().trim().contains("=") == false){
                Alert badInput = new Alert(Alert.AlertType.ERROR, "Invalid input format, please enter using the following format: TAGNAME=TAGVALUE");
                badInput.showAndWait();
                return;
            }
            //get the tag inputted into the field
            String input = singleTag.getText().trim();
            Tag tag = new Tag(input.substring(0, input.indexOf('=')), input.substring(input.indexOf('=')+1));
            //loop through the photos to see if any have a matching tag
            for(Photo p: Application.getCurrentUser().getCurrentAlbum().getPhotos()){
                if(p.getTags().contains(tag)){
                    searchedPhotos.add(p);
                }
            }
        }
        //date range search
        else if(singleTag.getText().trim().isEmpty() && fromDate.getValue() != null && toDate.getValue() != null && andField1.getText().trim().isEmpty() && andField2.getText().trim().isEmpty() && orField1.getText().trim().isEmpty() && orField2.getText().trim().isEmpty()){
            //get the inputted dates
            Date date1 = Date.from(fromDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date date2 = Date.from(toDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            //loop through the photos to see if they fall within the dates
            for(Photo p: Application.getCurrentUser().getCurrentAlbum().getPhotos()){
                if(p.getDate().getTime().equals(date1) || p.getDate().getTime().equals(date2) || (p.getDate().getTime().after(date1) && p.getDate().getTime().before(date2))){
                    searchedPhotos.add(p);
                }
            }
        }
        //conjunctive search
        else if(singleTag.getText().trim().isEmpty() && fromDate.getValue() == null && toDate.getValue() == null && andField1.getText().trim().isEmpty() == false && andField2.getText().trim().isEmpty() == false && orField1.getText().trim().isEmpty() && orField2.getText().trim().isEmpty()){
            if(andField1.getText().trim().contains("=") == false || andField2.getText().trim().contains("=") == false){
                Alert badInput = new Alert(Alert.AlertType.ERROR, "Invalid input format, please enter using the following format: TAGNAME=TAGVALUE");
                badInput.showAndWait();
                return;
            }
            //get the inputted tags
            String input1 = andField1.getText().trim();
            String input2 = andField2.getText().trim();
            Tag tag1 = new Tag(input1.substring(0, input1.indexOf('=')), input1.substring(input1.indexOf('=')+1));
            Tag tag2 = new Tag(input2.substring(0, input2.indexOf('=')), input2.substring(input2.indexOf('=')+1));
            //loop through the photos
            for(Photo p: Application.getCurrentUser().getCurrentAlbum().getPhotos()){
                if(p.getTags().contains(tag1) && p.getTags().contains(tag2)){
                    searchedPhotos.add(p);
                }
            }
        }
        //disjunctive search
        else if(singleTag.getText().trim().isEmpty() && fromDate.getValue() == null && toDate.getValue() == null && andField1.getText().trim().isEmpty() && andField2.getText().trim().isEmpty() && orField1.getText().trim().isEmpty() == false && orField2.getText().trim().isEmpty() == false){
            if(orField1.getText().trim().contains("=") == false || orField2.getText().trim().contains("=") == false){
                Alert badInput = new Alert(Alert.AlertType.ERROR, "Invalid input format, please enter using the following format: TAGNAME=TAGVALUE");
                badInput.showAndWait();
                return;
            }
            //get the inputted tags
            String input1 = orField1.getText().trim();
            String input2 = orField2.getText().trim();
            Tag tag1 = new Tag(input1.substring(0, input1.indexOf('=')), input1.substring(input1.indexOf('=')+1));
            Tag tag2 = new Tag(input2.substring(0, input2.indexOf('=')), input2.substring(input2.indexOf('=')+1));
            //loop through the photos
            for(Photo p: Application.getCurrentUser().getCurrentAlbum().getPhotos()){
                if(p.getTags().contains(tag1) || p.getTags().contains(tag2)){
                    searchedPhotos.add(p);
                }
            }
        }
        //invalid search
        else{
            Alert invalidSearch = new Alert(Alert.AlertType.ERROR, "Invalid search input, unable to create album!");
            invalidSearch.showAndWait();
            return;
        }

        //check to see if there are search results
        if(searchedPhotos.size() == 0) {
            Alert noSearchResults = new Alert(Alert.AlertType.INFORMATION, "No search results found for given search parameters, unable to create album!");
            noSearchResults.showAndWait();
            return;
        }
        //there are search results
        else{
            //create a new album with a random generate number at the end
            Random random = new Random();
            Album searchResultsAlbum = new Album("album"+random.nextInt(1000));
            while(Application.getCurrentUser().getAlbums().contains(searchResultsAlbum)){
                searchResultsAlbum.setName("album"+random.nextInt(1000));
            }
            //add the photos to the album
            searchResultsAlbum.setPhotos(searchedPhotos);
            //add the album to the user
            Application.getCurrentUser().addAlbum(searchResultsAlbum);
            //go back to non-admin screen
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            Parent nonAdminRoot = FXMLLoader.load(getClass().getResource("/View/Photos_Non_Admin.fxml"));
            Scene nonAdminScene = new Scene(nonAdminRoot, 1000, 750);
            stage.setScene(nonAdminScene);
        }
    }
}
