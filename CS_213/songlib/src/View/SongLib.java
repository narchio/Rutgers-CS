/*
Authors: Nicolas Carchio and Adam Romano
(Each contributed to this file)
 */
package View;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SongLib extends Application {

    public static Controller listController;

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(
                getClass().getResource("/View/main.fxml"));
        Pane root = (Pane)loader.load();

        listController = loader.getController();
        listController.start(primaryStage);

        primaryStage.setTitle("Song Library - Nick Carchio and Adam Romano");
        primaryStage.setScene(new Scene(root, 750 , 500));
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
        listController.encodeObsList();
    }
}