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
 * This class adds a tag to a given photo.
 * @author Dhruti Shah
 * @author Kev Sharma
 */
public class AddTagController {
	
	/**
	 * TextField tagname takes a nonempty string.
	 */
	@FXML private TextField tagname;
	/**
	 * TextField tagvalue takes a nonempty string.
	 */
	@FXML private TextField tagvalue;
	/**
	 * ListView<String> listTags shows list of all tag names this User has used.
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
	 * The Session object we are using.
	 */
    Session sesh;
    /**
     * The User whose album we are using.
     */
	User thisUser;
	/**
	 * The album whose photo we are using. 
	 */
	Album thisAlbum;
	/**
	 * The photo to whom we will add a tag to.
	 */
	Photo thisPhoto;
	
	/**
	 * Background processes to set values to private attributes.
	 * @param mainStage The stage upon which the AddTag scene is.
	 * @param sesh The session object we are using.
	 * @param inUser The User we are operating on.
	 * @param inAlbum The album of the inUser which contains the photo.
	 * @param inPhoto The photo we are going to add a tag.
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
		
		// Populate tags.
		listTags.setItems(FXCollections.observableArrayList(thisUser.getDefinedTags()));
		
		mainStage.setOnCloseRequest(event -> {
			sesh.saveData();
			System.exit(0);
		});
		
		listTags.setOnMouseClicked(new EventHandler<MouseEvent>() {
	        public void handle(MouseEvent event) {
	            	String s = listTags.getSelectionModel().getSelectedItem();
	        		tagname.setText(s);
	        }
	    });
	}
	
	/**
	 * Returns the user to the Selected Album View screen.
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
	 * If the tag is valid, it adds it to the photo and returns the User to the Selected Album View screen.
	 */
	public void okPressed() {
		
		// ensure both TextFields are properly populated
		if(tagname.getText().trim().equals("") || tagvalue.getText().trim().equals("")) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning");
			alert.setHeaderText("Improper add");
			alert.setContentText("Each tag must have\na non-empty name & value.");
			alert.showAndWait();
			return;	
		}
		
		// add the tag
		if(thisPhoto.newTag(tagname.getText().trim(), tagvalue.getText().trim())) {
			sesh.saveData();
			
			for(User u : this.sesh.getUsers())
				if(u.equals(thisUser)) 
					thisUser = u;
				
			for(Album a : thisUser.getAlbums()) 
				if(a.equals(thisAlbum)) 
					thisAlbum = a;
				
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
				System.out.println("thrown from addTag .");
				exception.printStackTrace();
			}
		}
		else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning");
			alert.setHeaderText("Tag alread Found");
			alert.setContentText("This photo already contains a tag\nwith the same name|value pair.");
			alert.showAndWait();
			return;	
		}
	}
}

