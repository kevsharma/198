package controller;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import model.*;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
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
 * This class shows the user of the program a set of tags associated with a Photo 
 * in a list, from which they may delete a tag.
 * @author Dhruti Shah
 * @author Kev Sharma
 */
public class DeleteTagController {
	
	/**
	 * ListView<String> listTags shows all tags belonging to a Photo.
	 */
	@FXML private ListView<String> listTags;
	/**
	 * Button cancel has onAction method cancelPressed();
	 */
	@FXML private Button cancel;
	/**
	 * Button ok has onAction method okPressed();
	 */
	@FXML private Button ok;
	
	/**
	 * Session which contains the users.
	 */
    Session sesh;
    /**
     * Users which contains the Albums.
     */
	User thisUser;
	/**
	 * Albums which contains the Photos.
	 */
	Album thisAlbum;
	/**
	 * Photos which contains the Tags.
	 */
	Photo thisPhoto;
	
	/**
	 * Background processes to set values to private attributes.
	 * @param mainStage The stage upon which the delete tag scene is..
	 * @param sesh The session object we are using.
	 * @param inUser The User we are operating on.
	 * @param inAlbum The album of the inUser which contains the Photo..
	 * @param inPhoto The photo we are going to delete a tag from..
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
		
		// Populate tags.							// ERROR USE THE PARTICULAR PHOTO'S TAG.
		listTags.setItems(FXCollections.observableArrayList(thisPhoto.getTagsFormatted()));
		
		mainStage.setOnCloseRequest(event -> {
			sesh.saveData();
			System.exit(0);
		});
	}
	
	/**
	 * Takes the User to the selected Album view after Deleting.
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
	 * Takes the User to the Selected Album view after Deleting the tag (if valid delete).
	 */
	public void okPressed() {
		if(listTags.getSelectionModel().getSelectedItem() == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("No tag chosen");
			alert.setContentText("Choose a tag to delete.");
			alert.showAndWait();
			return;
		}

		String formattedTag = listTags.getSelectionModel().getSelectedItem();
		String key = formattedTag.substring(10, formattedTag.indexOf("\tTag Value: "));
		String value = formattedTag.substring(formattedTag.indexOf("\tTag Value: ") + "\tTag Value: ".length());
		
		thisPhoto.removeTag(key, value);
		sesh.saveData();
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SelectedAlbumView.fxml"));
		try{
			Parent parent = (Parent) loader.load();
			SelectedAlbumViewController controller = loader.getController();
			Scene scene = new Scene(parent);
			Stage stage = (Stage) ((Node) ok).getScene().getWindow();
			controller.start(stage, sesh, thisUser, thisAlbum);
			stage.setScene(scene);
			stage.centerOnScreen();
			stage.show();
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}

























