package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import model.Album;
import model.Session;
import model.User;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.*;
import java.io.IOException; 

/**
 * This class is used to rename an existing album belonging to some User.
 * @author Dhruti Shah
 * @author Kev Sharma
 */
public class RenameAlbumController {
	
	/**
	 * Button ok has onAction method okPressed();
	 */
	@FXML private Button ok; 
	/**
	 * Button cancel has onAction method cancelPressed();
	 */
	@FXML private Button cancel;
	/**
	 * Label oldalbumname shows the album's previous name
	 */
	@FXML public Label oldalbumname;
	/**
	 * TextField newalbumname takes in a nonempty String to set as the Album's new name.
	 */
	@FXML public TextField newalbumname;
	
	/**
	 * Button quit has onAction method quitPressed();
	 */
	@FXML public Button quit;

	/**
	 * The session containing the user which contains the album we'll rename.
	 */
	Session sesh;
	/**
	 * The user which contains the album we wish to rename.
	 */
	User thisUser;
	/**
	 * The album we which to rename.
	 */
	Album thisAlbum;
	
	/**
	 * Background processes to set values to private attributes.
	 * @param mainStage The stage upon which Rename screen is.
	 * @param sesh The session object we are using.
	 * @param inUser The User we are operating on.
	 * @param inAlbum The album of the inUser we'll rename..
	 * @throws IOException
	 */
	public void start(Stage mainStage, Session sesh, User inUser, Album inAlbum) throws IOException {                
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
		
		for(Album a : thisUser.getAlbums()) {
			if(a.equals(inAlbum)) {
				thisAlbum = a;
			}
		}
		
		oldalbumname.setText(thisAlbum.getAlbumName());
		
		mainStage.setOnCloseRequest(event -> {
			sesh.saveData();
			System.exit(0);
		});
	}
	
	/**
	 * Saves data and changes, then terminates the program.
	 */
	public void quitPressed() {
		System.exit(0);
	}

	/**
	 * Checks if the input is a valid name for the album, if so renames the album and loads back to the screen with a list of albums.
	 */
	public void okPressed() {
		
		String inputtedAlbumname = newalbumname.getText().trim();
		
		// ensure not empty.
		if (inputtedAlbumname.equals("")) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("ALERT");
			alert.setHeaderText("Error");
			alert.setContentText("Must enter an album name.");
			alert.showAndWait();
			return;
		}
		
		// ensure not duplicate.
		Album newalbum = new Album(inputtedAlbumname);
		for(Album a : thisUser.getAlbums()) {
			if(a.equals(newalbum)) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("ALERT");
				alert.setHeaderText("Duplicate Names");
				alert.setContentText("An album already has that name.\nEnter a new name.");
				alert.showAndWait();
				return;
			}
		}
		
		
		/*
		 * Otherwise rename the album, save the changes then go back over to albums.fxml
		 */
		thisAlbum.setAlbumName(inputtedAlbumname);
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
	 * Returns the User to the Albums screen.
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

