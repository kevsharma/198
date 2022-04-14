package controller;

import model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.*;
import java.io.IOException;

/**
 * Creates an Album for this User.
 * @author Dhruti Shah
 * @author Kev Sharma
 */
public class CreateAlbumController {
	
	/**
	 * Button ok has onAction method okPressed();
	 */
	@FXML private Button ok; 
	/**
	 * Button cancel has onAction method cancelPressed();
	 */
	@FXML private Button cancel;
	/**
	 * Button quit has onAction method quitpressed();
	 */
	@FXML private Button quit;
	/**
	 * TextField albumname takes in a non-empty String.
	 */
	@FXML private TextField albumname;
	
	/**
	 * Session sesh maintains a set of users upon whom we are operating.
	 */
	Session sesh;
	/**
	 * User thisUser maintains to which User we'll be adding an album.
	 */
	User thisUser;
	
	/**
	 * Background processes to set values to private attributes.
	 * @param mainStage The stage upon which the CreateAlbum scene is.
	 * @param sesh The session object we are using.
	 * @param inUser The User we will add to.
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
	 * Terminates Program and saves changes made.
	 */
	public void quitPressed() {
		sesh.saveData();
		System.exit(0);
	}
	
	/**
	 * If a valid name is given, this Program creates an album of that name for thisUser and then returns to Albums view.
	 */
	public void okPressed() {
		
		String inputtedAlbumname = albumname.getText().trim();
		
		if (inputtedAlbumname.equals("")) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("ALERT");
			alert.setHeaderText("Error");
			alert.setContentText("Must enter an album name.");
			alert.showAndWait();
			return;
		}

		// Ensure not a duplicate.
		Album newalbum = new Album(inputtedAlbumname);
		for(Album a : thisUser.getAlbums()) {
			if(a.equals(newalbum)) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("ALERT");
				alert.setHeaderText("Duplicate Names");
				alert.setContentText("An album already has that name. \n Enter a new name.");
				alert.showAndWait();
				return;
			}
		}
		
		/*
		 * Otherwise add the album, save the changes then go back over to albums.fxml
		 */
		thisUser.addAlbumToUser(newalbum);
		sesh.saveData(); // VERY IPMORTANT.
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Albums.fxml"));
		try{
			Parent parent = (Parent) loader.load();
			AlbumsController controller = loader.getController();
			Scene scene = new Scene(parent);
			Stage stage = (Stage) ((Node) ok).getScene().getWindow();
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
	 * If cancel is Pressed, we return back to the Albums view from which we came here.
	 */
	public void cancelPressed() {
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
			exception.printStackTrace();
		}
	}
}

