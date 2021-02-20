// Dhruti Shah & Kev Sharma

package app;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import view.Controller;

public class SongLib extends Application 
{
	@Override
	public void start(Stage primaryStage) throws IOException 
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/SongLibGUI.fxml"));
		
		AnchorPane root = (AnchorPane)loader.load();
		
		// Note do not use keyword new.
		Controller listController = loader.getController();
		listController.start(primaryStage);

		Scene scene = new Scene(root, 730, 630);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Song-Library");
		primaryStage.setResizable(false);
		primaryStage.show(); 
	}

	public static void main(String[] args) {
		launch(args);
	}

}