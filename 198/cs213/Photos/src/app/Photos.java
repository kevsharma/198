package app;

import java.io.IOException;

import controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Session;

/**
 * The Photos class is used to launch the Photo's assignment program.
 * @author Dhruti Shah
 * @author Kev Sharma
 */
public class Photos extends Application {
	
	/**
	 * Photos starts the main stage by extending Application
	 */
	public void start(Stage primaryStage) throws IOException {
		FXMLLoader loader = new FXMLLoader();   
		loader.setLocation(
				getClass().getResource("/view/Login.fxml"));
		
		
		AnchorPane root = (AnchorPane)loader.load();		
		LoginController listController = loader.getController();
		
		Session s = new Session();
		s.loadData();
		
		listController.start(primaryStage, s);
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.centerOnScreen();
		primaryStage.show(); 
	}

	/**
	 * @param args 
	 * The main method launches the application view the launch method from the extended Application class.
	 */
	public static void main(String[] args) {
		/*
		 	Session s = new Session();
			s.loadData();
			s.getUsers().get(0).getAlbums().get(0).addPhoto(new Photo("data/stock/img1.jpg"));
			s.getUsers().get(0).getAlbums().get(0).addPhoto(new Photo("data/stock/img2.jpg"));
			s.getUsers().get(0).getAlbums().get(0).addPhoto(new Photo("data/stock/img3.jpg"));
			s.getUsers().get(0).getAlbums().get(0).addPhoto(new Photo("data/stock/img4.jpg"));
			s.getUsers().get(0).getAlbums().get(0).addPhoto(new Photo("data/stock/img5.jpg"));
			s.saveData();
		*/
		
		launch(args);
	}

}
	
	
	
	