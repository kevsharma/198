package controller;

import java.util.ArrayList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.*;

/**
 * This controller class shows us results of a given Search performed in Albums view.
 * @author Dhruti Shah
 * @author Kev Sharma
 */
public class SearchResultController {

	/**
	 * Button createalbumfromresult has onAction method createalbumPressed()
	 */
	@FXML private Button createalbumfromresult; // create album of these photos
	/**
	 * Button close has onAction method closePressed();
	 */
	@FXML private Button close; // back to my albums
	/**
	 * Button logout has onAction method logoutPressed()
	 */
	@FXML private Button logout;
	/**
	 * Button quit has onAction method quitPressed();
	 */
	@FXML private Button quit;

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
	 * The Session which contains the User on whose albums we performed the search.
	 */
	Session sesh;
	/**
	 * User thisUser is the User instance upon whose albums we performed the search.
	 */
	User thisUser;
	/**
	 * ArrayList<Photo> searchResults maintains all Photos from thisUser which matched the search criteria.
	 */
	ArrayList<Photo> searchResults;
	
	/**
	 * Background processes to set values to private attributes.
	 * @param mainStage The stage upon which the Login scene is.
	 * @param sesh The session object we are using.
	 * @param inUser The User whose Photos we performed a search on.
	 * @param searchResults The successful matches of Photos from search criteria.
	 */
	public void start(Stage mainStage, Session sesh, User inUser, ArrayList<Photo> searchResults) {
		// TODO Auto-generated method stub
		this.sesh = sesh;
		this.sesh.loadData();
		
		for(User u : this.sesh.getUsers()) {
			if(u.equals(inUser)) {
				thisUser = u;
			}
		}
		
		this.searchResults = searchResults;
		
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
	 * Uses Display class to populate Tableview with ImageView | Caption | Date as the three columns.
	 */
	public void initializeTable() {
		// Use observable arrayList to show the shit.
		for(Photo p : searchResults) {
			Display d = new Display(p);
			table.getItems().add(d);
			//tableList.add(d);
			//table.setItems(tableList);
		}
	}
	
	// Onaction methods
	//==================
	
	/**
	 * Takes user to a screen where they can enter a name for this new album of Search Results.
	 */
	public void createalbumPressed() {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AlbumFromSearchResults.fxml"));
		try{
			Parent parent = (Parent) loader.load();
			AlbumFromSearchResultsController controller = loader.getController();
			Scene scene = new Scene(parent);
			
			// go is the button pressed action event
			Stage stage = (Stage) ((Node) logout).getScene().getWindow();
			controller.start(stage, sesh, thisUser, searchResults);
			stage.setScene(scene);
			stage.centerOnScreen();
			stage.show();
		}
		catch (Exception exception) {
			System.out.println("thrown from createAlbumPressed in SearchResultController.java.");
			exception.printStackTrace();
		}
	}
	
	/**
	 * Takes user to back to their Albums view.
	 */
	public void closePressed() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Albums.fxml"));
		try{
			Parent parent = (Parent) loader.load();
			AlbumsController controller = loader.getController();
			Scene scene = new Scene(parent);
			Stage stage = (Stage) ((Node) close).getScene().getWindow();
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
	 * Saves changes and logs out taking User back to the Login screen.
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
			System.out.println("thrown from logoutPressed in SearchResultController.");
			exception.printStackTrace();
		}
	}
	
	/**
	 * Saves changes and terminates application.
	 */
	public void quitPressed() {
		sesh.saveData();
		System.exit(0);
	}

}