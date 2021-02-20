package controller;

import javafx.scene.control.Alert.AlertType;
import model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.*;
import java.io.IOException; 

/**
 * This class is used to Choose search criteria when searching from all photos from a given user.
 * @author Dhruti Shah
 * @author Kev Sharma
 */
public class SearchPhotosController {
	
	/**
	 * Button ok has onAction method okPressed();
	 */
	@FXML private Button ok; 
	/**
	 * Button cancel has onAction method cancelPressed();
	 */
	@FXML private Button cancel;
	/**
	 * Button logout has onAction method logoutPressed()
	 */
	@FXML private Button logout;
	/**
	 * RadioButton dates allows the user to search for photos by their search by Date
	 */
	@FXML private RadioButton dates;
	/**
	 * RadioButton tags allows the user to search for photos by their search by tags
	 */
	@FXML private RadioButton tags;
	/**
	 * ToggleGroup choice ensures users pick between dates or tags Radiobuttons.
	 */
	@FXML private ToggleGroup choice;
	
	/**
	 * Session containing users.
	 */
	Session sesh;
	/**
	 * The user we are trying to Search Photos on.
	 */
	User thisUser;
	
	/**
	 * Background processes to set values to private attributes.
	 * @param mainStage The stage upon which the SearchPhotos scene is.
	 * @param sesh The session object we are using.
	 * @param inUser The User we are operating on..
	 * @throws IOException
	 */
	public void start(Stage mainStage, Session sesh, User inUser) throws IOException {                
		this.sesh = sesh;
		this.sesh.loadData();
		
		/*
		 * Initializes thisUser attribute of the class to be exactly from sesh's arrayList (not anything else).
		 */
		for(User u : this.sesh.getUsers()) {
			if(u.equals(inUser)) {
				thisUser = u;
			}
		}
		
		mainStage.setOnCloseRequest(event -> {
			sesh.saveData();
			System.exit(0);
		});
	}

	
	/**
	 * Saves changes and switches scene to Login scene.
	 */
	public void logoutPressed() {
		sesh.saveData();
		// Go to login screen.
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
		try{
			Parent parent = (Parent) loader.load();
			LoginController controller = loader.getController();
			Scene scene = new Scene(parent);
			
			// go is the button pressed action event
			Stage stage = (Stage) ((Node) logout).getScene().getWindow();
			controller.start(stage, sesh);
			stage.setScene(scene);
			stage.centerOnScreen();
			stage.show();
		}
		catch (Exception exception) {
			System.out.println("thrown from logoutPressed in SearchPhotos.");
			exception.printStackTrace();
		}
	}
	
	/**
	 * Returns the user of the program to Albums view which shows the User object's albums.
	 */
	public void cancelPressed() {
		sesh.saveData();
		
		// Go to albums again
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Albums.fxml"));
		try{
			Parent parent = (Parent) loader.load();
			AlbumsController controller = loader.getController();
			Scene scene = new Scene(parent);
			
			// go is the button pressed action event
			Stage stage = (Stage) ((Node) cancel).getScene().getWindow();
			controller.start(stage, sesh, thisUser);
			stage.setScene(scene);
			stage.centerOnScreen();
			stage.show();
		}
		catch (Exception exception) {
			System.out.println("thrown from cancelPressed() in SearchPhotos controller.");
			exception.printStackTrace();
		}
	}
	
	/**
	 * Directs the program to either the Tag Search or Date search based on the Users choice of searching criteria.
	 */
	public void okPressed() {
		if(dates.isSelected()) {
			// Go to date controller.
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DateSearch.fxml"));
			try{
				Parent parent = (Parent) loader.load();
				DateSearchController controller = loader.getController();
				Scene scene = new Scene(parent);
				Stage stage = (Stage) ((Node) dates).getScene().getWindow();
				controller.start(stage, sesh, thisUser);
				stage.setScene(scene);
				stage.centerOnScreen();
				stage.show();
				return;
			}
			catch (Exception exception) {
				System.out.println("thrown from okPressed dates in SearchPhotos.");
				exception.printStackTrace();
			}
		}
		else if(tags.isSelected()) {
			// Go to date controller.
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TagSearch.fxml"));
			try{
				Parent parent = (Parent) loader.load();
				TagSearchController controller = loader.getController();
				Scene scene = new Scene(parent);
				Stage stage = (Stage) ((Node) tags).getScene().getWindow();
				controller.start(stage, sesh, thisUser);
				stage.setScene(scene);
				stage.centerOnScreen();
				stage.show();
				return;
			}
			catch (Exception exception) {
				System.out.println("thrown from okPressed dates in SearchPhotos.");
				exception.printStackTrace();
			}
		}
		
		// Neither were selected.
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("No Choice");
		alert.setContentText("Please choose which type\nof search to perform.");
		alert.showAndWait();
		return;
	}
}



