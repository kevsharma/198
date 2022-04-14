package controller;

import java.util.ArrayList;
import javafx.scene.control.Alert.AlertType;
import model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.*;

/**
 * Loads the Screen with results from the Users search from SearchResults view after they click Create album from Search results.
 * @author Dhruti Shah
 * @author Kev Sharma
 */
public class AlbumFromSearchResultsController {
	
	/**
	 * Button ok has onAction method okPressed();
	 */
	@FXML private Button ok;		
	/**
	 * Button cancel has onAction method cancelPressed()
	 */
	@FXML private Button cancel;
	/**
	 * TextField newName takes in a nonempty String.
	 */
	@FXML private TextField newName;
	
	/**
	 * The session (containing Users) which we are working with.
	 */
	Session sesh;
	
	/**
	 * The user we searched photos upon within a session.
	 */
	User thisUser;
	
	/**
	 * The Search results from a Search done in AlbumsController.
	 */
	ArrayList<Photo> searchResults;
	
	/**
	 * Background processes to set values to private attributes.
	 * @param mainStage The stage upon which we display the results from a Search.
	 * @param sesh The session object we are using.
	 * @param inUser The User we are searched on.
	 * @param searchResults The results from a given Search which we have the option to turn into an Album and add to thisUser.
	 */
	public void start(Stage mainStage, Session sesh, User inUser, ArrayList<Photo> searchResults) {
		
		this.sesh = sesh;
		this.sesh.loadData();
		
		for(User u : this.sesh.getUsers()) {
			if(u.equals(inUser)) {
				thisUser = u;
			}
		}
		
		this.searchResults = searchResults;
		
		mainStage.setOnCloseRequest(event -> {
			sesh.saveData();
			System.exit(0);
		});
	}
	
	/**
	 * Given a valid name, we add turn searchResults into an Album and add it to thisUser.
	 */
	public void okPressed() {
		if(newName.getText().trim().equals("")) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning");
			alert.setHeaderText("No Album Name");
			alert.setContentText("Enter a name for your Album\nor choose to Cancel.");
			alert.showAndWait();
			return;	
		}
		
		Album a = new Album(newName.getText().trim()); 
		for(Album al : thisUser.getAlbums()) {
			if(a.equals(al)) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("ALERT");
				alert.setHeaderText("Duplicate Names");
				alert.setContentText("An album already has that name. \n Enter a new name.");
				alert.showAndWait();
				return;
			}
		}
		
		// Add to a all the photos existing.
		ArrayList<String> imgpaths = new ArrayList<String>();
		for(Photo p : searchResults) {
			for(Album alb : thisUser.getAlbums()) {
				for(Photo phot : alb.getPhotos()) {
					if(phot.getLocation().equals(p.getLocation()) && !imgpaths.contains(phot.getLocation())) {
						a.addPhoto(phot);
						imgpaths.add(phot.getLocation());
					}
				}
			}
		}
		
		// Give this user the album of photo references.
		for(User u : sesh.getUsers()) {
			if(u.equals(thisUser)) {
				u.addAlbumToUser(a);
			}
		}
		
		sesh.saveData();
		sesh.loadData();
		for(User u : sesh.getUsers()) {
			if(u.equals(thisUser)) {
				thisUser = u;
			}
		}
		
		// take the user back to Albums screen where they can see the new album.
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Albums.fxml"));
		try{
			Parent parent = (Parent) loader.load();
			AlbumsController controller = loader.getController();
			Scene scene = new Scene(parent);
			Stage stage = (Stage) ((Node) cancel).getScene().getWindow();
			
			controller.start(stage, sesh, thisUser);
			stage.setScene(scene);
			stage.centerOnScreen();
			stage.show();
		}
		catch (Exception exception) {
			System.out.println("afsrc ok");
			exception.printStackTrace();
		}
	}
	
	/**
	 * Takes us back to SearchResult screen.
	 */
	public void cancelPressed() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SearchResult.fxml"));
		try{
			Parent parent = (Parent) loader.load();
			SearchResultController controller = loader.getController();
			Scene scene = new Scene(parent);
			Stage stage = (Stage) ((Node) cancel).getScene().getWindow();
			
			controller.start(stage, sesh, thisUser, searchResults);
			stage.setScene(scene);
			stage.centerOnScreen();
			stage.show();
		}
		catch (Exception exception) {
			System.out.println("afsrc cancel");
			exception.printStackTrace();
		}
	}
}
	