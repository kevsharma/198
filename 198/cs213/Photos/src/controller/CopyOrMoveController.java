package controller;

import model.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.*;
import java.util.ArrayList;

/**
 * The copy or move controller moves or copies a selected photo from one album to another.
 * @author Dhruti Shah
 * @author Kev Sharma
 */
public class CopyOrMoveController {
	
	/**
	 * Button copy has onAction copyPressed();
	 */
	@FXML private RadioButton copy;
	/**
	 * Button move has onAction movePressed();
	 */
	@FXML private RadioButton move;
	/**
	 * Button cancel has onAction cancelPressed();
	 */
	@FXML private Button cancel;
	/**
	 * Button ok has onAction okPressed();
	 */
	@FXML private Button ok;
	/**
	 * albumlistView shows a list of Album names this User has. 
	 */
	@FXML private ListView<String> albumListView;
	
	/**
	 * Session attribute maintains the Session we are working with -> How many users -> how many albums per user -> how many photos in those albums.
	 */
    Session sesh;
    /**
     * User thisUser keeps track of which user from the session we are working with.
     */
	User thisUser;
	/**
	 * Album thisAlbum keeps track of which Album we are copying/moving from.
	 */
	Album thisAlbum; 
	
	/**
	 * int thisAlbumIndex keeps track of which Album we are copying/moving from.
	 */
	int thisAlbumIndex = -1;
	
	/**
	 * Photo thisPhoto keeps track of which Photo we selected to copy/move.
	 */
	Photo thisPhoto;
	
	/**
	 * Background processes to set values to private attributes.
	 * @param mainStage The stage upon which the Login scene is.
	 * @param sesh The session object we are using.
	 * @param inUser The User we are operating on.
	 * @param inAlbum The album of the inUser we'll copy move from.
	 * @param inPhoto The photo we are going to copy/move.
	 */
	public void start(Stage mainStage, Session sesh, User inUser, Album inAlbum, Photo inPhoto) {
		// TODO Auto-generated method stub
		this.sesh = sesh;
		this.sesh.loadData();
		
		for(User u : this.sesh.getUsers()) {
			if(u.equals(inUser)) {
				this.thisUser = u;
			}
		}
		
		int count = 0;
		for(Album a : thisUser.getAlbums()) {
			if(a.equals(inAlbum)) {
				this.thisAlbum = a;
				this.thisAlbumIndex = count;
			}
			++count;
		}
		
		for(Photo p : thisAlbum.getPhotos()) {
			if(p.equals(inPhoto)) {
				this.thisPhoto = p;
			}
		}
		
		// can't copy/move to same album
		ArrayList<String> temp = new ArrayList<>();
		for(Album a : thisUser.getAlbums())
			if(!a.equals(thisAlbum))
				temp.add(a.getAlbumName());
		
		
		albumListView.setItems(FXCollections.observableArrayList(temp));
		
		
		mainStage.setOnCloseRequest(event -> {
			sesh.saveData();
			System.exit(0);
		});
	}
	
	/**
	 * Canceling takes us back the Selected Album View scene in the Program.
	 */
	public void cancelPressed() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SelectedAlbumView.fxml"));
		try{
			Parent parent = (Parent) loader.load();
			SelectedAlbumViewController controller = loader.getController();
			Scene scene = new Scene(parent);
			Stage stage = (Stage) ((Node) cancel).getScene().getWindow();
			controller.start(stage, sesh, thisUser, thisAlbum);
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
	 * Pressing ok ensures the copy/move is valid, and if so takes us back to the album we copied/moved from.
	 */
	public void okPressed() {
		
		if(!copy.isSelected() && !move.isSelected()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning");
			alert.setHeaderText("No Operation chosen.");
			alert.setContentText("Select whether you'd like\nto copy or move this photo.");
			alert.showAndWait();
			return;	
		}
		
		if(albumListView.getSelectionModel().getSelectedItem() == null) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning");
			alert.setHeaderText("No Album Chosen");
			alert.setContentText("Select an album to copy/move to.");
			alert.showAndWait();
			return;	
		}

		int indexDestination = -1;
		for(int i=0; i<thisUser.getAlbums().size(); i++) {
			if(thisUser.getAlbums().get(i).getAlbumName().equals(albumListView.getSelectionModel().getSelectedItem()))
				indexDestination = i;
		}

		if(indexDestination!= -1) {
			if(copy.isSelected()) {
				thisUser.getAlbums().get(indexDestination).addPhoto(thisPhoto);
			}
			else if(move.isSelected()) {
				thisUser.getAlbums().get(thisAlbumIndex).removePhoto(thisPhoto);
				thisUser.getAlbums().get(indexDestination).addPhoto(thisPhoto);
			}
		}
		
		// Save the changes they have made.
		sesh.saveData();
		
		// Return them to SelectedAlbumView
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SelectedAlbumView.fxml"));
		try{
			Parent parent = (Parent) loader.load();
			SelectedAlbumViewController controller = loader.getController();
			Scene scene = new Scene(parent);
			Stage stage = (Stage) ((Node) cancel).getScene().getWindow();
			controller.start(stage, sesh, thisUser, thisAlbum);
			stage.setScene(scene);
			stage.centerOnScreen();
			stage.show();
		}
		catch (Exception exception) {
			System.out.println("thrown from openPressed in AlbumsController.");
			exception.printStackTrace();
		}
	}
	
}