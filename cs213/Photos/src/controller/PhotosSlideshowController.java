package controller;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Album;
import model.Display;
import model.Photo;
import model.Session;
import model.User;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.io.IOException;
 
/**
 * Handles the screen with Photos slideshow, displays the currently selected photo with its tags and caption.
 * Allows user to slide let or right for a slide show of photos in the current album.
 * @author Dhruti Shah
 * @author Kev Sharma
 *
 */
public class PhotosSlideshowController {
	
	/**
	 * Button back has onAction method backPressed();
	 */
	@FXML private Button back; 
	/**
	 * Button logout has onAction method logoutPressed()
	 */
	@FXML private Button logout;	
	/**
	 * Button slideLeft has onAction methhod slideLeftPressed()
	 */
	@FXML private Button slideLeft;
	/**
	 * Button slideRight has onAction method slideRightPressed()
	 */
	@FXML private Button slideRight;
	/**
	 * Label Caption shows a photo's caption.
	 */
	@FXML private Label caption;
	/**
	 * Label date displays a photo's date formatted as MM/dd/YYYY
	 */
	@FXML private Label date;
	/**
	 * Button quit has onAction method quitPressed()
	 */
	@FXML private Button quit;
	/**
	 * ImageView imageview shows an Image.
	 */
	@FXML private ImageView imageview;
	/**
	 * ListView<String> tagListView shows all tags associated with a Photo.
	 */
	@FXML private ListView<String> tagListView;

	/**
	 * The session containing the users.
	 */
	Session sesh;
	/**
	 * The user containing the Album we will show.
	 */
	User thisUser;
	/**
	 * The album which contains the Photo which begins the slideshow.
	 */
	Album thisAlbum;
	/**
	 * The photo which we'll display.
	 */
	Photo thisPhoto;
	/**
	 * The index of Photo we will display.
	 */
	int photoIndex; 
	
	/**
	 * Background processes to set values to private attributes.
	 * @param mainStage The stage upon which the Photo slide show scene is shown.
	 * @param sesh The session object we are using.
	 * @param inUser The User we are operating on.
	 * @param inAlbum The album of the inUser whose photos we'll show in a slide show.
	 * @param inPhoto The photo we are going to display.
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
		
		int count = 0;
		for(Photo p : thisAlbum.getPhotos()) {
			if(p.equals(inPhoto)) {
				photoIndex = count;
				thisPhoto = p;
			}
			
			++count;
		}
		
		initializeAll(thisPhoto);
		
		mainStage.setOnCloseRequest(event -> {
			sesh.saveData();
			System.exit(0);
		});
	}
	
	/**
	 * Shows the photo in an album which comes after current photo.
	 */
	public void slideRightPressed() {
		if(photoIndex + 1 < thisAlbum.getPhotos().size()) {
			++photoIndex;
			thisPhoto = thisAlbum.getPhotos().get(photoIndex);
		}

		initializeAll(thisPhoto);
	}
	
	/**
	 * Shows the photo in an album which comes before the current photo.
	 */
	public void slideLeftPressed() {
		if(photoIndex - 1 >= 0) {
			--photoIndex;
			thisPhoto = thisAlbum.getPhotos().get(photoIndex);
		}
		
		initializeAll(thisPhoto);
	}
	
	/**
	 * Takes the user back to SelectedAlbum view where they can see the photos in current album.
	 */
	public void backPressed() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SelectedAlbumView.fxml"));
		try{
			Parent parent = (Parent) loader.load();
			SelectedAlbumViewController controller = loader.getController();
			Scene scene = new Scene(parent);
			Stage stage = (Stage) ((Node) back).getScene().getWindow();
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
	 * Saves changes and takes the user back to the Login screen.
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
			exception.printStackTrace();
		}
	}
	
	/**
	 * Saves changes and terminates the program.
	 */
	public void quitPressed() {
		sesh.saveData();
		System.exit(0);
	}
	
	/**
	 * Initalizes the Imageview with the Photo passed in.
	 * @param p Photo object to show.
	 */
	public void initializeAll(Photo p) {
		caption.setText(p.getCaption());
		date.setText(p.getDate());
		
		tagListView.setItems(FXCollections.observableArrayList(p.getTagsFormatted()));
		
		File fp = new File(p.getLocation());
		Image i = new Image(fp.toURI().toString(), 330,330,true,true);
		
		imageview.setImage(i);
		imageview.setFitWidth(350);
	}
	
}	

