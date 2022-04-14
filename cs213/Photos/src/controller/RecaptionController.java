package controller;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional; 

/**
 * This class recaptions a given Photo.
 * @author Dhruti Shah
 * @author Kev Sharma
 */
public class RecaptionController {
	
	/**
	 * Label oldcaption shows the Photo's previous caption, if any
	 */
	@FXML private Label oldcaption;
	/**
	 * TextField newcaption allows the Photo to take in a new caption.
	 */
	@FXML private TextField newcaption;
	/**
	 * Button cancel has onAction button cancelPressed()
	 */
	@FXML private Button cancel;
	/**
	 * Button ok has onAction button okPressed()
	 */
	@FXML private Button ok;
	
	/**
	 * The Session sesh object contains the users.
	 */
    Session sesh;
    /**
     * The user contains the albums out of which 1 contains the photo we wish to recaption.
     */
	User thisUser;
	/**
	 * The album contains the photo we wish to recaption.
	 */
	Album thisAlbum;
	/**
	 * The photo we wish to recaption.
	 */
	Photo thisPhoto;
	
	/**
	 * Background processes to set values to private attributes.
	 * @param mainStage The stage upon which the Recaption screen is..
	 * @param sesh The session object we are using.
	 * @param inUser The User we are operating on.
	 * @param inAlbum The album of the inUser.
	 * @param inPhoto The photo we are going to recaption.
	 */
	public void start(Stage mainStage, Session sesh, User inUser, Album inAlbum, Photo inPhoto) {
		// TODO Auto-generated method stub
		this.sesh = sesh;
		this.sesh.loadData();
		
		for(User u : this.sesh.getUsers()) {
			if(u.equals(inUser)) {
				thisUser = u;
			}
		}
		
		for(Album a : thisUser.getAlbums()) {
			if(a.equals(inAlbum)) {
				thisAlbum = a;
			}
		}
		
		for(Photo p : thisAlbum.getPhotos()) {
			if(p.equals(inPhoto)) {
				thisPhoto = p;
			}
		}
		
		oldcaption.setText(thisPhoto.getCaption());
		
		mainStage.setOnCloseRequest(event -> {
			sesh.saveData();
			System.exit(0);
		});
	}
	
	/**
	 * Takes user back to Selected Album view scene without making any changes.
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
	 * Recaptions thisPhoto to the properly formatted new name given.
	 */
	public void okPressed() {
		if(newcaption.getText().trim().equals("")) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning");
			alert.setHeaderText("Invalid Input");
			alert.setContentText("Insert text for the new Caption.");
			alert.showAndWait();
			return;	
		}
		
		// Save the changes they have made.
		thisPhoto.setCaption(newcaption.getText().trim());
		sesh.saveData();
		sesh.loadData();
		
		for(User u : this.sesh.getUsers()) {
			if(u.equals(thisUser)) {
				thisUser = u;
			}
		}
		
		for(Album a : thisUser.getAlbums()) {
			if(a.equals(thisAlbum)) {
				thisAlbum = a;
			}
		}
		
		for(Photo p : thisAlbum.getPhotos()) {
			if(p.equals(thisPhoto)) {
				thisPhoto = p;
			}
		}
		
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



























