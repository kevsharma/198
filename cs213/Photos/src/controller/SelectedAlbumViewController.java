//Dhruti & Kev

package controller;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import model.*;
import javafx.collections.FXCollections;
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
 * This is launched when the User clicks to open an album from Albums view. Here there is a variety of options to manipulate photos in the chosen album.
 * @author Dhruti Shah
 * @author Kev Sharma
 */
public class SelectedAlbumViewController {

	@FXML private Label albumname;
	@FXML private Button backtoalbumlist;
	
	/**
	 * Button logout has onAction method logoutPressed()
	 */
	@FXML private Button logout; 	
	/**
	 * Button displayphoto has onAction method displayPhotoPressed();
	 */
	@FXML private Button displayphoto;
	/**
	 * Button addphoto has onAction method addPhotoPressed()
	 */
	@FXML private Button addphoto; 
	/**
	 * Button removephoto has onAction method removePhotoPressed()
	 */
	@FXML private Button removephoto;
	/**
	 * Button recaptionPhoto has onAction method recaptionPressed()
	 */
	@FXML private Button recaptionphoto; 
	/**
	 * Button quit has onAction method quitPressed();
	 */
	@FXML private Button quit; 
	/**
	 * Button addTag has onAction method addTagPressed()
	 */
	@FXML private Button addTag;
	/**
	 * Button deleteTag has onAction method deleteTagPressed()
	 */
	@FXML private Button deleteTag;
	/**
	 * Button copymove has onAction method copymovePressed();
	 */
	@FXML private Button copymove;
	
	/**
	 * TableView<Display> table is a tableview of Display objects.
	 */
	@FXML private TableView<Display> table;
	/**
	 * This is the first column of table, it shows an Imageview.
	 */
	@FXML private TableColumn<Display, ImageView> thumbnailCol; // image
	/**
	 * This is the second column of table, it shows a caption.
	 */
	@FXML private TableColumn<Display, String> captionCol; // caption
	/**
	 * This is the third column of the table, it shows dateCol
	 */
	@FXML private TableColumn<Display, String> dateCol; // date
	
	/**
	 * The session instance which contains the users.
	 */
    Session sesh;
    /**
     * The user which contains the album that was picked.
     */
	User thisUser;
	/**
	 * The album which was picked.
	 */
	Album thisAlbum;
	
	/**
	 * Background processes to set values to private attributes.
	 * @param mainStage The stage upon which the Selected Album View is.
	 * @param sesh The session object we are using.
	 * @param inUser The User which contains the album we are showing in detail.
	 * @param inAlbum The album of the inUser we'll show photos from and perform operations upon.
	 */
	public void start(Stage mainStage, Session sesh, User inUser, Album inAlbum) {

		this.sesh = sesh;
		this.sesh.loadData();
		
		for(User u : this.sesh.getUsers()) {
			if(u.equals(inUser)) {
				this.thisUser = u;
			}
		}
		
		for(Album a : this.thisUser.getAlbums()) {
			if(a.equals(inAlbum)) {
				this.thisAlbum = a;
			}
		}

		albumname.setText(this.thisAlbum.getAlbumName());

		thumbnailCol.setCellValueFactory(new PropertyValueFactory<Display, ImageView>("imgView"));
		captionCol.setCellValueFactory(new PropertyValueFactory<Display, String>("caption"));
		dateCol.setCellValueFactory(new PropertyValueFactory<Display, String>("date"));

		initializeTable();		
		
		mainStage.setOnCloseRequest(event -> {
			sesh.saveData();
			System.exit(0);
		});
	}
	
	/**
	 * Displays in the TableView 3 columns populated as: ThumbNail of an image | Caption | Date;
	 */
	public void initializeTable() {
		// Use observable arrayList to show the shit.
		table.setItems(FXCollections.observableArrayList());
		for(Photo p : thisAlbum.getPhotos()) {
			Display d = new Display(p);
			table.getItems().add(d);
		}
	}
	
	/**
	 * Takes the User to the slide show screen to show more attributes of the Photo and the Photo in more detail.
	 */
	public void displayPhotoPressed() {
		// Ensure a photo selected and then pass on over to PhotosSlideshowController
		Display rowPicked = table.getSelectionModel().getSelectedItem();
		if(rowPicked == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("No Selection");
			alert.setContentText("Choose a Photo to Display.");
			alert.showAndWait();
			return;
		}
		
		Photo selectedPhoto = rowPicked.getPhoto();

		// Take them to the Recaption controller.
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/PhotosSlideshow.fxml"));
		try{
			Parent parent = (Parent) loader.load();
			PhotosSlideshowController controller = loader.getController();
			Scene scene = new Scene(parent);
			Stage stage = (Stage) ((Node) recaptionphoto).getScene().getWindow();
			controller.start(stage, sesh, thisUser, thisAlbum, selectedPhoto);
			stage.setScene(scene);
			stage.centerOnScreen();
			stage.show();
		}
		catch (Exception exception) {
			System.out.println("thrown from displayPP SAVC.");
			exception.printStackTrace();
		}
	}
	
	/**
	 * Removes a photo from this Album provided one is selected.
	 */
	public void removePhotoPressed() {

		Display rowPicked = table.getSelectionModel().getSelectedItem();
		if(rowPicked == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("No Selection");
			alert.setContentText("Choose a Photo to delete.");
			alert.showAndWait();
			return;
		}
		
		Photo selectedPhoto = rowPicked.getPhoto();
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete");
		alert.setHeaderText("Are you sure?");
		alert.setContentText("Do you want to delete this Photo?");
		
		Optional<ButtonType> confirm = alert.showAndWait();
		//if the user selects to delete this, remove selected photo from the album.
		if (!(confirm.get() == ButtonType.OK))
			return;
		
		thisAlbum.removePhoto(selectedPhoto);
		
		for(int i=0; i<thisUser.getAlbums().size(); i++) {
			if(thisUser.getAlbums().get(i).equals(thisAlbum)) {
				thisUser.getAlbums().set(i, thisAlbum);
				break;
			}
		}
		
		for(int i=0; i<sesh.getUsers().size(); i++) {
			if(sesh.getUsers().get(i).equals(thisUser)) {
				sesh.getUsers().set(i, thisUser);
				break;
			}
		}
		
		sesh.saveData();
		sesh.loadData();
		
		for(User u : this.sesh.getUsers()) {
			if(u.equals(thisUser)) {
				this.thisUser = u;
			}
		}
		
		for(Album a : this.thisUser.getAlbums()) {
			if(a.equals(thisAlbum)) {
				this.thisAlbum = a;
			}
		}
		
		initializeTable();
	}
	
	/**
	 * Adds a tag to a selected photo. Takes to the AddTagController.
	 */
	public void addTagPressed() {
		Display rowPicked = table.getSelectionModel().getSelectedItem();
		if(rowPicked == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Recaption expects Photo");
			alert.setContentText("Choose a Photo to add a tag to.");
			alert.showAndWait();
			return;
		}
		
		Photo selectedPhoto = rowPicked.getPhoto();

		// Take them to the Recaption controller.
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddTag.fxml"));
		try{
			Parent parent = (Parent) loader.load();
			AddTagController controller = loader.getController();
			Scene scene = new Scene(parent);
			Stage stage = (Stage) ((Node) addTag).getScene().getWindow();
			controller.start(stage, sesh, thisUser, thisAlbum, selectedPhoto);
			stage.setScene(scene);
			stage.centerOnScreen();
			stage.show();
		}
		catch (Exception exception) {
			System.out.println("thrown from addTagPresed SAVC.");
			exception.printStackTrace();
		}
	}
	
	/**
	 * Deletes a tag from a selected photo. Takes to the DeleteTagController
	 */
	public void deleteTagPressed() {
		Display rowPicked = table.getSelectionModel().getSelectedItem();
		if(rowPicked == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Recaption expects Photo");
			alert.setContentText("Choose a Photo to delete a tag from.");
			alert.showAndWait();
			return;
		}
		
		Photo selectedPhoto = rowPicked.getPhoto();
		if(selectedPhoto.getExistingTags() == null || selectedPhoto.getExistingTags().size()==0) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Delete Tag");
			alert.setContentText("This photo has no tags to delete.");
			alert.showAndWait();
			return;
		}
		
		// Take them to the Recaption controller.
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DeleteTag.fxml"));
		try{
			Parent parent = (Parent) loader.load();
			DeleteTagController controller = loader.getController();
			Scene scene = new Scene(parent);
			Stage stage = (Stage) ((Node) addTag).getScene().getWindow();
			controller.start(stage, sesh, thisUser, thisAlbum, selectedPhoto);
			stage.setScene(scene);
			stage.centerOnScreen();
			stage.show();
		}
		catch (Exception exception) {
			System.out.println("thrown from deleteTagPressed SAVC.");
			exception.printStackTrace();
		}
	}
	
	/**
	 * By clicking the copy/move, a user can copy or move a photo from one album to another.
	 */
	public void copymovePressed() {
		// Select an Image to recaption
		Display rowPicked = table.getSelectionModel().getSelectedItem();
		if(rowPicked == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("No Photo Selected");
			alert.setContentText("Choose a Photo to Copy/Move\nto another album.");
			alert.showAndWait();
			return;
		}
		
		Photo selectedPhoto = rowPicked.getPhoto();

		// Take them to the Recaption controller.
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CopyMove.fxml"));
		try{
			Parent parent = (Parent) loader.load();
			CopyOrMoveController controller = loader.getController();
			Scene scene = new Scene(parent);
			Stage stage = (Stage) ((Node) copymove).getScene().getWindow();
			controller.start(stage, sesh, thisUser, thisAlbum, selectedPhoto);
			stage.setScene(scene);
			stage.centerOnScreen();
			stage.show();
		}
		catch (Exception exception) {
			System.out.println("thrown from recaptionPresed SAVC.");
			exception.printStackTrace();
		}
	}
	
	/**
	 * Removes a caption from a selected photo.
	 */
	public void recaptionPressed() {
		// Select an Image to recaption
		Display rowPicked = table.getSelectionModel().getSelectedItem();
		if(rowPicked == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Recaption expects Photo");
			alert.setContentText("Choose a Photo to recaption.");
			alert.showAndWait();
			return;
		}
		
		Photo selectedPhoto = rowPicked.getPhoto();

		// Take them to the Recaption controller.
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Recaption.fxml"));
		try{
			Parent parent = (Parent) loader.load();
			RecaptionController controller = loader.getController();
			Scene scene = new Scene(parent);
			Stage stage = (Stage) ((Node) recaptionphoto).getScene().getWindow();
			controller.start(stage, sesh, thisUser, thisAlbum, selectedPhoto);
			stage.setScene(scene);
			stage.centerOnScreen();
			stage.show();
		}
		catch (Exception exception) {
			System.out.println("thrown from recaptionPresed SAVC.");
			exception.printStackTrace();
		}
	}
	
	/**
	 * Returns the user to the album view list where they can see all their albums.
	 */
	public void backToAlbumListPressed() {
		sesh.saveData();
		
		// Go to login screen.
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Albums.fxml"));
		try{
			Parent parent = (Parent) loader.load();
			AlbumsController controller = loader.getController();
			Scene scene = new Scene(parent);
			
			// go is the button pressed action event
			Stage stage = (Stage) ((Node) backtoalbumlist).getScene().getWindow();
			controller.start(stage, sesh, thisUser);
			stage.setScene(scene);
			stage.centerOnScreen();
			stage.show();
		}
		catch (Exception exception) {
			System.out.println("thrown from backToAlbumListPressed() in SAV controller.");
			exception.printStackTrace();
		}
	}
	
	/**
	 * Adds a new Photo to this Album.
	 */
	public void addPhotoPressed() {
		FileChooser fp = new FileChooser();
		fp.setTitle("Choose Photo");
		fp.getExtensionFilters().addAll(new ExtensionFilter("Image Files", "*.png", "*.jpeg","*.gif", "*.bmp", "*.JPEG", "*.jpg"));
		
		Stage stage = (Stage) ((Node) addphoto).getScene().getWindow();
		
		File selectedFile = fp.showOpenDialog(stage);
		if (selectedFile != null) {
			String pathname = new String(selectedFile.getAbsolutePath());
			Photo p = new Photo(pathname);
			
			// However if that photo already exists, then according to forums I must change it to have same reference.
			for(Album al : thisUser.getAlbums()) {
				for(Photo pho : al.getPhotos()) {
					if(pho.getLocation().equals(pathname)) {
						p = pho;
					}
				}
			}
			
			thisAlbum.addPhoto(p);
			sesh.saveData();
		}
		
		initializeTable();
	}
	
	/**
	 * Saves changes and returns user of program to Login screen.
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
	 * Saves changes and terminates the Program.
	 */
	public void quitPressed() {
		sesh.saveData();
		System.exit(0);
	}
}	
