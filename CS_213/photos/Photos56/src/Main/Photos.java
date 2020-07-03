package Main;

import Model.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * The class Photos is the driver for the photos application
 * @author Nicolas Carchio
 * @author Adam Romano
 */
public class Photos extends Application{
	/**
	 * Overridden start method that loads the login screen when application is run
	 * @throws Exception
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/View/Photos_Login.fxml"));
		Scene scene = new Scene(root, 1000, 750);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.setTitle("Photos Application");
		primaryStage.show();
		
	}

	/**
	 * Overridden stop method which serializes user data when application is exited
	 */
	@Override
	public void stop(){
		Model.Application.writeUsers();
	}

	/**
	 * Main method
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
