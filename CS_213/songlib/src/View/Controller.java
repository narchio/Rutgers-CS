/*
Authors: Nicolas Carchio and Adam Romano
(Each contributed to this file)
 */
package View;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.*;
import java.util.Comparator;
import java.util.Scanner;

public class Controller {
    @FXML
    private TextField add_name;

    @FXML
    private TextField add_artist;

    @FXML
    private TextField add_album;

    @FXML
    private TextField add_year;

    @FXML
    private TextField edit_name;

    @FXML
    private TextField edit_artist;

    @FXML
    private TextField edit_album;

    @FXML
    private TextField edit_year;

    @FXML
    private ListView<Song> song_list;

    @FXML
    private TextArea song_descrpition;

    Stage stage;
    Stage popup;

    public static ObservableList<Song> obslist = FXCollections.observableArrayList();

    public ObservableList<Song> getObslist(){
        return Controller.obslist;
    }
    /*
        Show list at start of the window
        - prepopulate the observable list (persistance)
     */
    public void start(Stage mainStage) {
        stage = mainStage; 
        if (obslist.isEmpty()) {
            decodeObsList();
        }
        // display the song_list, sorted, and select first item
        obslist.sort(Comparator.comparing(Song::getName, String.CASE_INSENSITIVE_ORDER).thenComparing(Song::getArtist, String.CASE_INSENSITIVE_ORDER));
        // now check
        song_list.setItems(obslist);
        song_list.getSelectionModel().select(0);
        // display the list that is clicked with this
        try {
            Song currentSong = song_list.getSelectionModel().getSelectedItem();
            if (currentSong != null) {
                song_descrpition.setText("Name : " + currentSong.getName() + "\nArtist: " + currentSong.getArtist() +
                        "\nAlbum: " + currentSong.getAlbum() + "\nYear: " + currentSong.getYear());
            }
        } catch (NullPointerException n) {
            n.printStackTrace();
        }
        song_list
                .getSelectionModel()
                .selectedIndexProperty()
                .addListener(
                        (obs, oldVal, newVal) ->
                                showSongDescription(mainStage));
    }

    @FXML
    /*
        Takes inputs from user, adds song
        **** added sorting ****
        To do:
        *** also can only add a song with minimum a name and artist
        *** cant add duplicate songs
     */
    public void addSong() {
        if (add_name.getText().equals("") || add_artist.getText().equals("")) {
            Alert bad_add_alert = new Alert(AlertType.CONFIRMATION, "You cannot add a song without a Name AND Artist", ButtonType.OK);
            bad_add_alert.showAndWait();
        } else {
            // if we found it in the list already
            for (Song s : obslist) {
                if (s.getName().toLowerCase().equals(add_name.getText().toLowerCase()) && s.getArtist().toLowerCase().equals(add_artist.getText().toLowerCase())) {
                    Alert duplicate_alert = new Alert(AlertType.CONFIRMATION, "You cannot add a duplicate song", ButtonType.OK);
                    duplicate_alert.showAndWait();
                    return;
                }
            }
            // if not, proceed
            Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to add " + add_name.getText() + " by " + add_artist.getText() + "?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            // if they confirm they would like to add the song
            if (alert.getResult() == ButtonType.YES) {
                // now create a new song
                Song newSong = new Song(add_name.getText(), add_artist.getText(), add_album.getText(), add_year.getText());

                // insert it into list and then sort list
                obslist.add(newSong);
                obslist.sort(Comparator.comparing(Song::getName, String.CASE_INSENSITIVE_ORDER).thenComparing(Song::getArtist, String.CASE_INSENSITIVE_ORDER));
                // display the addition in the song list and select first item
                song_list.setItems(obslist);
                Song selected = null;
                for (Song s : obslist) {
                    if (s.equals(newSong)) {
                        selected = s;
                        break;
                    }
                }
                // select the new song
                song_list.getSelectionModel().select(selected);
            }
        }
    }

    @FXML
    /*
        Takes inputs from user, edits current selected song
        **** added sorting ****
        To do:
        *** also can only add a song with minimum a name and artist
        *** cant add duplicate songs
     */
    public void editSong() {
        //get the selected song
        Song selectedSong = song_list.getSelectionModel().getSelectedItem();
        int selectedSongIndex = song_list.getSelectionModel().getSelectedIndex();
        //get the values from the edit fields
        String newName = edit_name.getText();
        String newArtist = edit_artist.getText();
        String newAlbum = edit_album.getText();
        String newYear = edit_year.getText();
        //case 1: all 4 fields are blank --> throw an error with popup
        if(newName.equals("") && newArtist.equals("") && newAlbum.equals("") && newYear.equals("")){
            Alert bad_edit_alert = new Alert(AlertType.CONFIRMATION, "Please make the desired changes in the correct fields", ButtonType.OK);
            bad_edit_alert.showAndWait();
        }
        //case 2: change just the song name, leave the artist, album, and year
        else if(!newName.equals("") && newArtist.equals("") && newAlbum.equals("") && newYear.equals("")){
            //check to make sure changing the song name and leaving the artist does not conflict
            boolean conflicts = false;
            for(Song s: obslist){
                if(s.getName().toLowerCase().equals(newName.toLowerCase()) && s.getArtist().toLowerCase().equals(selectedSong.getArtist().toLowerCase())){
                    conflicts = true;
                    break;
                }
            }
            if(conflicts == false){
                Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to edit the song name to "+newName+"?", ButtonType.YES, ButtonType.NO);
                alert.showAndWait();
                if(alert.getResult() == ButtonType.YES) {
                    selectedSong.updateName(newName);
                }
            }
            else{
                Alert bad_edit_alert = new Alert(AlertType.CONFIRMATION, "Song already exists with that Name AND Artist", ButtonType.OK);
                bad_edit_alert.showAndWait();
            }
        }
        //case 3: change the artist, leave the name, album, and year
        else if(newName.equals("") && !newArtist.equals("") && newAlbum.equals("") && newYear.equals("")){
            //check to make sure changing the artist and leaving the song name does not conflict
            boolean conflicts = false;
            for(Song s: obslist){
                if(s.getName().toLowerCase().equals(selectedSong.getName().toLowerCase()) && s.getArtist().toLowerCase().equals(newArtist.toLowerCase())){
                    conflicts = true;
                    break;
                }
            }
            if(conflicts == false){
                Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to edit the artist to "+newArtist+"?", ButtonType.YES, ButtonType.NO);
                alert.showAndWait();
                if(alert.getResult() == ButtonType.YES) {
                    selectedSong.updateArtist(newArtist);
                }

            }
            else{
                Alert bad_edit_alert = new Alert(AlertType.CONFIRMATION, "Song already exists with that Name AND Artist", ButtonType.OK);
                bad_edit_alert.showAndWait();
            }
        }
        //case 4: change the album, leave the name, artist, and year
        else if(newName.equals("") && newArtist.equals("") && !newAlbum.equals("") && newYear.equals("")){
            Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to edit the album to "+newAlbum+"?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if(alert.getResult() == ButtonType.YES) {
                selectedSong.updateAlbum(newAlbum);
            }
        }
        //case 5: change the year, leave the name, artist, and album
        else if(newName.equals("") && newArtist.equals("") && newAlbum.equals("") && !newYear.equals("")){
            Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to edit the year to "+newYear+"?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if(alert.getResult() == ButtonType.YES) {
                selectedSong.updateYear(newYear);
            }
        }
        //case 6: change the name and artist, leave the album and year
        else if(!newName.equals("") && !newArtist.equals("") && newAlbum.equals("") && newYear.equals("")){
            boolean conflicts = false;
            for(Song s: obslist){
                if(s.getName().toLowerCase().equals(newName.toLowerCase()) && s.getArtist().toLowerCase().equals(newArtist.toLowerCase())){
                    conflicts = true;
                    break;
                }
            }
            if(conflicts == false){
                Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to edit the name to "+newName+" and the artist to "+newArtist+"?", ButtonType.YES, ButtonType.NO);
                alert.showAndWait();
                if(alert.getResult() == ButtonType.YES) {
                    selectedSong.updateName(newName);
                    selectedSong.updateArtist(newArtist);
                }
            }
            else{
                Alert bad_edit_alert = new Alert(AlertType.CONFIRMATION, "Song already exists with that Name AND Artist", ButtonType.OK);
                bad_edit_alert.showAndWait();
            }
        }
        //case 7: change the name and album, leave artist and year
        else if(!newName.equals("") && newArtist.equals("") && !newAlbum.equals("") && newYear.equals("")){
            boolean conflicts = false;
            for(Song s: obslist){
                if(s.getName().toLowerCase().equals(newName.toLowerCase()) && s.getArtist().toLowerCase().equals(selectedSong.getArtist().toLowerCase())){
                    conflicts = true;
                    break;
                }
            }
            if(conflicts == false){
                Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to edit the name to "+newName+" and the album to "+newAlbum+"?", ButtonType.YES, ButtonType.NO);
                alert.showAndWait();
                if(alert.getResult() == ButtonType.YES) {
                    selectedSong.updateName(newName);
                    selectedSong.updateAlbum(newAlbum);
                }
            }
            else{
                Alert bad_edit_alert = new Alert(AlertType.CONFIRMATION, "Song already exists with that Name AND Artist", ButtonType.OK);
                bad_edit_alert.showAndWait();
            }
        }
        //case 8: change the name and year, leave the artist and album
        else if(!newName.equals("") && newArtist.equals("") && newAlbum.equals("") && !newYear.equals("")){
            boolean conflicts = false;
            for(Song s: obslist){
                if(s.getName().toLowerCase().equals(newName.toLowerCase()) && s.getArtist().toLowerCase().equals(selectedSong.getArtist().toLowerCase())){
                    conflicts = true;
                    break;
                }
            }
            if(conflicts == false){
                Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to edit the name to "+newName+" and the year to "+newYear+"?", ButtonType.YES, ButtonType.NO);
                alert.showAndWait();
                if(alert.getResult() == ButtonType.YES) {
                    selectedSong.updateName(newName);
                    selectedSong.updateYear(newYear);
                }
            }
            else{
                Alert bad_edit_alert = new Alert(AlertType.CONFIRMATION, "Song already exists with that Name AND Artist", ButtonType.OK);
                bad_edit_alert.showAndWait();
            }
        }
        //case 9: change the artist and album, leave the name and year
        else if(newName.equals("") && !newArtist.equals("") && !newAlbum.equals("") && newYear.equals("")){
            boolean conflicts = false;
            for(Song s: obslist){
                if(s.getName().toLowerCase().equals(selectedSong.getName().toLowerCase()) && s.getArtist().toLowerCase().equals(newArtist.toLowerCase())){
                    conflicts = true;
                    break;
                }
            }
            if(conflicts == false){
                Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to edit the artist to "+newArtist+" and the album to "+newAlbum+"?", ButtonType.YES, ButtonType.NO);
                alert.showAndWait();
                if(alert.getResult() == ButtonType.YES) {
                    selectedSong.updateArtist(newArtist);
                    selectedSong.updateAlbum(newAlbum);
                }
            }
            else{
                Alert bad_edit_alert = new Alert(AlertType.CONFIRMATION, "Song already exists with that Name AND Artist", ButtonType.OK);
                bad_edit_alert.showAndWait();
            }
        }
        //case 10: change the artist and year, leave the name and album
        else if(newName.equals("") && !newArtist.equals("") && newAlbum.equals("") && !newYear.equals("")){
            boolean conflicts = false;
            for(Song s: obslist){
                if(s.getName().toLowerCase().equals(selectedSong.getName().toLowerCase()) && s.getArtist().toLowerCase().equals(newArtist.toLowerCase())){
                    conflicts = true;
                    break;
                }
            }
            if(conflicts == false){
                Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to edit the artist to "+newArtist+" and the year to "+newYear+"?", ButtonType.YES, ButtonType.NO);
                alert.showAndWait();
                if(alert.getResult() == ButtonType.YES) {
                    selectedSong.updateArtist(newArtist);
                    selectedSong.updateYear(newYear);
                }
            }
            else{
                Alert bad_edit_alert = new Alert(AlertType.CONFIRMATION, "Song already exists with that Name AND Artist", ButtonType.OK);
                bad_edit_alert.showAndWait();
            }
        }
        //case 11: change the album and year, leave the name and artist
        else if(newName.equals("") && newArtist.equals("") && !newAlbum.equals("") && !newYear.equals("")){
            Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to edit the album to "+newAlbum+" and the year to "+newYear+"?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if(alert.getResult() == ButtonType.YES) {
                selectedSong.updateAlbum(newAlbum);
                selectedSong.updateYear(newYear);
            }
        }
        //case 12: change the name, artist, and album, leave the year
        else if(!newName.equals("") && !newArtist.equals("") && !newAlbum.equals("") && newYear.equals("")){
            boolean conflicts = false;
            for(Song s: obslist){
                if(s.getName().toLowerCase().equals(newName.toLowerCase()) && s.getArtist().toLowerCase().equals(newArtist.toLowerCase())){
                    conflicts = true;
                    break;
                }
            }
            if(conflicts == false){
                Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to edit the name to "+newName+" and artist to "+newArtist+" and the album to "+newAlbum+"?", ButtonType.YES, ButtonType.NO);
                alert.showAndWait();
                if(alert.getResult() == ButtonType.YES) {
                    selectedSong.updateName(newName);
                    selectedSong.updateArtist(newArtist);
                    selectedSong.updateAlbum(newAlbum);
                }
            }
            else{
                Alert bad_edit_alert = new Alert(AlertType.CONFIRMATION, "Song already exists with that Name AND Artist", ButtonType.OK);
                bad_edit_alert.showAndWait();
            }
        }
        //case 13: change the name, artist, and year, leave the album
        else if(!newName.equals("") && !newArtist.equals("") && newAlbum.equals("") && !newYear.equals("")){
            boolean conflicts = false;
            for(Song s: obslist){
                if(s.getName().toLowerCase().equals(newName.toLowerCase()) && s.getArtist().toLowerCase().equals(newArtist.toLowerCase())){
                    conflicts = true;
                    break;
                }
            }
            if(conflicts == false){
                Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to edit the name to "+newName+" and artist to "+newArtist+" and the year to "+newYear+"?", ButtonType.YES, ButtonType.NO);
                alert.showAndWait();
                if(alert.getResult() == ButtonType.YES) {
                    selectedSong.updateName(newName);
                    selectedSong.updateArtist(newArtist);
                    selectedSong.updateYear(newYear);
                }
            }
            else{
                Alert bad_edit_alert = new Alert(AlertType.CONFIRMATION, "Song already exists with that Name AND Artist", ButtonType.OK);
                bad_edit_alert.showAndWait();
            }
        }
        //case 14: change the name, album, and year, leave the artist
        else if(!newName.equals("") && newArtist.equals("") && !newAlbum.equals("") && !newYear.equals("")){
            boolean conflicts = false;
            for(Song s: obslist){
                if(s.getName().toLowerCase().equals(newName.toLowerCase()) && s.getArtist().toLowerCase().equals(selectedSong.getArtist().toLowerCase())){
                    conflicts = true;
                    break;
                }
            }
            if(conflicts == false){
                Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to edit the name to "+newName+" and album to "+newAlbum+" and the year to "+newYear+"?", ButtonType.YES, ButtonType.NO);
                alert.showAndWait();
                if(alert.getResult() == ButtonType.YES) {
                    selectedSong.updateName(newName);
                    selectedSong.updateAlbum(newAlbum);
                    selectedSong.updateYear(newYear);
                }
            }
            else{
                Alert bad_edit_alert = new Alert(AlertType.CONFIRMATION, "Song already exists with that Name AND Artist", ButtonType.OK);
                bad_edit_alert.showAndWait();
            }
        }
        //case 15: change artist, album, and year, leave the name
        else if(newName.equals("") && !newArtist.equals("") && !newAlbum.equals("") && !newYear.equals("")){
            boolean conflicts = false;
            for(Song s: obslist){
                if(s.getName().toLowerCase().equals(selectedSong.getName().toLowerCase()) && s.getArtist().toLowerCase().equals(newArtist.toLowerCase())){
                    conflicts = true;
                    break;
                }
            }
            if(conflicts == false){
                Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to edit the artist to "+newArtist+" and album to "+newAlbum+" and the year to "+newYear+"?", ButtonType.YES, ButtonType.NO);
                alert.showAndWait();
                if(alert.getResult() == ButtonType.YES) {
                    selectedSong.updateArtist(newArtist);
                    selectedSong.updateAlbum(newAlbum);
                    selectedSong.updateYear(newYear);
                }
            }
            else{
                Alert bad_edit_alert = new Alert(AlertType.CONFIRMATION, "Song already exists with that Name AND Artist", ButtonType.OK);
                bad_edit_alert.showAndWait();
            }
        }
        //case 16: change all 4 fields
        else if(!newName.equals("") && !newArtist.equals("") && !newAlbum.equals("") && !newYear.equals("")){
            boolean conflicts = false;
            for(Song s: obslist){
                if(s.getName().toLowerCase().equals(newName.toLowerCase()) && s.getArtist().toLowerCase().equals(newArtist.toLowerCase())){
                    conflicts = true;
                    break;
                }
            }
            if(conflicts == false){
                Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to edit the name to "+newName+" and artist to "+newArtist+" and album to "+newAlbum+" and the year to "+newYear+"?", ButtonType.YES, ButtonType.NO);
                alert.showAndWait();
                if(alert.getResult() == ButtonType.YES) {
                    selectedSong.updateName(newName);
                    selectedSong.updateArtist(newArtist);
                    selectedSong.updateAlbum(newAlbum);
                    selectedSong.updateYear(newYear);
                }
            }
            else{
                Alert bad_edit_alert = new Alert(AlertType.CONFIRMATION, "Song already exists with that Name AND Artist", ButtonType.OK);
                bad_edit_alert.showAndWait();
            }
        }
        obslist.set(selectedSongIndex, selectedSong);
        obslist.sort(Comparator.comparing(Song::getName, String.CASE_INSENSITIVE_ORDER).thenComparing(Song::getArtist, String.CASE_INSENSITIVE_ORDER));
        // display the addition in the song list and select first item
        song_list.setItems(obslist);
        song_list.getSelectionModel().select(selectedSong);

    }

    @FXML
     /*
        Deletes current selected song
     */
    public void deleteSong() {
        // current song
        Song currentSong = song_list.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to delete " + currentSong.getName() + " by " + currentSong.getArtist() + "?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        // if they confirm they would like to add the song
        if (alert.getResult() == ButtonType.YES) {
            // get selected song
            int currentIndex = song_list.getSelectionModel().getSelectedIndex();
            // delete from list
            obslist.remove(currentIndex);
            // display the addition in the song list and select first item
            song_list.setItems(obslist);
            song_list.getSelectionModel().select(currentIndex);

            // null check for if deleting the last item of the list
            if (song_list.getSelectionModel().getSelectedItem() != null) {
                song_list.getSelectionModel().select(currentIndex);
                showSongDescription(stage);
            } else {
                song_descrpition.clear();
            }
        }
    }

    /*
        Decodes the file into the obslist before starting
     */
    public void decodeObsList() {
        // read items from file and load into obslist
        File f = new File("src/View/backup.txt");
        Scanner sc = null;
        try {
            sc = new Scanner(f).useDelimiter(">_<");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // read from backup file
        while (sc.hasNextLine()) {
            String name = sc.next();
            String artist = sc.next();
            String album = sc.next();
            String year = sc.next();
            Song newSong = new Song(name, artist, album, year);
            obslist.add(newSong);
            if(sc.nextLine() != null) {
                continue;
            }
        }
        // close scanner
        sc.close();
    }

    /*
        Encodes the obslist into the file before exiting
        - by making controller and list static, we can access them in different classes
     */
    public void encodeObsList() {
        // encode current obslist into backup.txt
        try (FileWriter f = new FileWriter("src/View/backup.txt")) {
            int i = 0;
            while (i < obslist.size()) {
                if (obslist.size() <= 0) {
                    break;
                } else {
                    Song curr = obslist.get(i);
                    f.write(curr.getName() + ">_<" + curr.getArtist() + ">_<" +
                            curr.getAlbum() + ">_<" + curr.getYear() + ">_<" + "\n");
                    i++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
        Shows song description in the TextArea
     */
    public void showSongDescription(Stage mainstage) {
        Song currentSong = song_list.getSelectionModel().getSelectedItem();
        if (currentSong != null) {
            song_descrpition.setText("Name : " + currentSong.getName() + "\nArtist: " + currentSong.getArtist() +
                    "\nAlbum: " + currentSong.getAlbum() + "\nYear: " + currentSong.getYear());
        }
    }
}
