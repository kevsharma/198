/**
 * @author Dhruti Shah & Kev Sharma
 */
package controller;

import java.util.Optional;
import javafx.scene.control.Alert.AlertType;
import model.*;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.*;
import java.io.IOException; 

/**
 * Loads the Screen with user's list of albums, showing their name, date range and number of photos
 * Handles action to create/delete/rename/display an album or search through all of user's albums
 * @author Dhruti Shah
 * @author Kev Sharma
 */
public class AlbumsController {
	
	/**
	 * Button quit has onAction quitPressed();
	 */
	@FXML private Button quit; 
	/**
	 * Button open has onAction openPressed();
	 */
	@FXML private Button open;	
	/**
	 * Button create has onAction createPressed();
	 */
	@FXML private Button create;
	/**
	 * Button rename has onAction renamePressed();
	 */
	@FXML private Button rename;
	/**
	 * Button delete has onAction deletePressed();
	 */
	@FXML private Button delete; 	
	/**
	 * Button searc has onAction searchPressed();
	 */
	@FXML private Button search;		
	/**
	 * Button logout has onAction logoutPressed();
	 */
	@FXML private Button logout;
	/**
	 * albumListView shows a list of Albums belonging to this user.
	 */
	@FXML ListView<Album> albumListView;
	
	/**
	 * Session sesh maintains which Session we are working with.
	 */
	Session sesh; 
	/**
	 * User thisUser maintains which user's albums we are showing.
	 */
	User thisUser;
	
	/**
	 * Background processes to set values to private attributes.
	 * @param mainStage The stage upon which the Login scene is.
	 * @param sesh The session object we are using.
	 * @param inUser The User we are operating on.
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
		
		albumListView.setItems(FXCollections.observableArrayList(thisUser.getAlbums()));
		
		mainStage.setOnCloseRequest(event -> {
			sesh.saveData();
			System.exit(0);
		});
	}
	
	/**
	 * Deletes a given album, if the delete is valid.
	 */
	public void deletePressed() {
		sesh.loadData();
		// If nothing to delete.
		if(thisUser.getAlbums().size() == 0) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Delete");
			alert.setContentText("No album to delete.");
			alert.showAndWait();
			return;
		}
		
		// If no album selected.
		if (albumListView.getSelectionModel().getSelectedItem() == null) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("ALERT");
			alert.setHeaderText("Error");
			alert.setContentText("Must select an album to delete");
			alert.showAndWait();
			return;	
		}
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete");
		alert.setHeaderText("Delete Album");
		alert.setContentText("Do you want to delete the selected album and its contents?");
		
		Optional<ButtonType> confirm = alert.showAndWait();
		//if the user selects to delete this, remove selected album from the user's stored albums.
		if (confirm.get() == ButtonType.OK) {
			thisUser.removeAlbumFromUser(albumListView.getSelectionModel().getSelectedItem());
		}
		
		for(int i=0; i<sesh.getUsers().size(); i++) {
			if(sesh.getUsers().get(i).equals(thisUser)) {
				sesh.getUsers().set(i, thisUser);
			}
		}
		
		sesh.saveData();
		albumListView.setItems(FXCollections.observableArrayList(thisUser.getAlbums()));
		sesh.loadData();
	}

	/**
	 * logoutPressed() takes the user to the Login screen handled by LoginController.java
	 * All changes made are saved.
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
			System.out.println("thrown from logoutPressed in AlbumsController.");
			exception.printStackTrace();
		}
	}
	
	/**
	 * Quits the application and saves changes.
	 */
	public void quitPressed() {
		sesh.saveData();
		System.exit(0);
	}
	
	/**
	 * Opens the selected album in the SelectedAlbumViewController.java
	 */
	public void openPressed() {
		sesh.loadData();
		
		Album selectedalbum = albumListView.getSelectionModel().getSelectedItem();
		if (selectedalbum == null) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("ALERT");
				alert.setHeaderText("Error");
				alert.setContentText("Select an album to open.");
				alert.showAndWait();
				return;	
		}
		
		// start method and shift to SelectedAlbumViewController.java
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SelectedAlbumView.fxml"));
		try{
			Parent parent = (Parent) loader.load();
			SelectedAlbumViewController controller = loader.getController();
			Scene scene = new Scene(parent);
			Stage stage = (Stage) ((Node) open).getScene().getWindow();
			// passing in selectedalbum
			controller.start(stage, sesh, thisUser, selectedalbum);
			stage.setScene(scene);
			stage.centerOnScreen();
			stage.show();
		}
		catch (Exception exception) {
			System.out.println("thrown from openPressed in AlbumsController.");
			exception.printStackTrace();
		}
	}
	
	/**
	 * Creates a new album for this user.
	 */
	public void createPressed() {
		sesh.loadData();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CreateAlbum.fxml"));
		try{
			Parent parent = (Parent) loader.load();
			CreateAlbumController controller = loader.getController();
			Scene scene = new Scene(parent);
			Stage stage = (Stage) ((Node) create).getScene().getWindow();
			controller.start(stage, sesh, thisUser);
			stage.setScene(scene);
			stage.centerOnScreen();
			stage.show();
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * Renames an existing album belonging to the User.
	 */
	public void renamePressed() {
		sesh.loadData();
		//get selected album
		Album selectedalbum = albumListView.getSelectionModel().getSelectedItem();
		if (selectedalbum == null) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("ALERT");
				alert.setHeaderText("Error");
				alert.setContentText("Must select an album to rename.");
				alert.showAndWait();
				return;	
		}
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/RenameAlbum.fxml"));
		try{
			Parent parent = (Parent) loader.load();
			RenameAlbumController controller = loader.getController();
			Scene scene = new Scene(parent);
			Stage stage = (Stage) ((Node) rename).getScene().getWindow();
			controller.start(stage, sesh, thisUser, selectedalbum);
			stage.setScene(scene);
			stage.centerOnScreen();
			stage.show();
		}
		catch (Exception exception) {
			exception.printStackTrace();
		} 	
	}
	
	/**
	 * Searches all Albums for Photos that match some criteria.
	 */
	public void searchPressed() {
		sesh.loadData();

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SearchPhotos.fxml"));
		try{
			Parent parent = (Parent) loader.load();
			SearchPhotosController controller = loader.getController();
			Scene scene = new Scene(parent);
			Stage stage = (Stage) ((Node) search).getScene().getWindow();
			// passing in selectedalbum
			controller.start(stage, sesh, thisUser);
			stage.setScene(scene);
			stage.centerOnScreen();
			stage.show();
		}
		catch (Exception exception) {
			System.out.println("thrown from searchPresed in AlbumsController.");
			exception.printStackTrace();
		}
	}
	
}
