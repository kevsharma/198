package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import view.ListController;

public class ListApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader();   
		loader.setLocation(getClass().getResource("/view/list.fxml"));
		AnchorPane root = (AnchorPane)loader.load();
		
		
		// This creates a controller instance
		// Note that loader.getController() refers to ListController.java
		// line 9 in list.fxml helps specify
		// NOTE it is very important not to have new keyword in here.
		ListController listController = loader.getController();
		listController.start(primaryStage); 
		//  ^ this calls ListController.java method start

		Scene scene = new Scene(root, 200, 300);
		primaryStage.setScene(scene);
		primaryStage.show(); 

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);

	}

}
