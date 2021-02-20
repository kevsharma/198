package controller;

import java.util.ArrayList;
import javafx.scene.control.Alert.AlertType;
import model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.*;
import java.io.IOException; 

/**
 * Searches all of a User object's Albums for photos matching search criteria (type tag).
 * @author Dhruti Shah
 * @author Kev Sharma
 */
public class TagSearchController {

	/**
	 * Button ok has onAction method okPressed()
	 */
	@FXML private Button ok;	
	/**
	 * Button logout has onAction method logoutPressed()
	 */
	@FXML private Button logout;  
	/**
	 * Button cancel has onAction method cancelPressed()
	 */
	@FXML private Button cancel;
	
	/**
	 * ToggleGroup choice makes user choose between regular, or, and.
	 */
	@FXML private ToggleGroup choice;
	/**
	 * RadioButton regular allows users to perform a search one 1 tag.
	 */
	@FXML private RadioButton regular;
	/**
	 * RadioButton or allows users to perform a disjunctive search on 2 tags.
	 */
	@FXML private RadioButton or;
	/**
	 * RadionButton and allows users to perform a conjunctive search on 2 tags.
	 */
	@FXML private RadioButton and;
	/**
	 * This TextField takes in a nonempty string representing tag 1
	 */
	@FXML private TextField tag1name;
	/**
	 * This TextField takes in a nonempty string representing value 1
	 */
	@FXML private TextField tag1value;
	/**
	 * This TextField takes in a nonempty string representing tag 2
	 */
	@FXML private TextField tag2name;
	/**
	 * This TextField takes in a nonempty string representing value 2
	 */
	@FXML private TextField tag2value;
	
	/**
	 * The session which contains the user whose photos we will perform a search on.
	 */
	Session sesh; 
	/**
	 * The user upon whose photos we will perform a search.
	 */
	User thisUser;
	
	/**
	 * Background processes to set values to private attributes.
	 * @param mainStage The stage upon which the Tag Search scene is.
	 * @param sesh The session object we are using.
	 * @param inUser The User whose photos we are searching on.
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
	 * Given proper criteria, this method searches thisUser for all photos that match the given criteria.
	 */
	public void okPressed() {
		
		// All fields are guaranteed to have values.
		if(regular.isSelected() || or.isSelected() || and.isSelected()) {
			ArrayList<Photo> searchResults;
			
			/*
			 * Populate searchResults.
			 */
			if(regular.isSelected()) {
				if(tag1name.getText().equals("") || tag1value.getText().equals("")) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Warning");
					alert.setHeaderText("Incorrect Formatting!");
					alert.setContentText("Input non-empty search queries\n for tag1 name and tag1 value.");
					alert.showAndWait();
					clearTextFields();
					return;
				}
				searchResults = thisUser.searchRegular(tag1name.getText(), tag1value.getText());
			}
				
			else if(or.isSelected()) { 
				if(tag1name.getText().equals("") || tag1value.getText().equals("") || tag2name.getText().equals("") || tag2value.getText().equals("")) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Warning");
					alert.setHeaderText("Incorrect Formatting!");
					alert.setContentText("All searches must contain non-empty\n tag names and values.");
					alert.showAndWait();
					clearTextFields();
					return;
				}
				searchResults = thisUser.searchDisjunctive(tag1name.getText(), tag1value.getText(), tag2name.getText(), tag2value.getText());
			}
			else { 
				if(tag1name.getText().equals("") || tag1value.getText().equals("") || tag2name.getText().equals("") || tag2value.getText().equals("")) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Warning");
					alert.setHeaderText("Incorrect Formatting!");
					alert.setContentText("All searches must contain non-empty\n tag names and values.");
					alert.showAndWait();
					clearTextFields();
					return;
				}
				searchResults = thisUser.searchConjunctive(tag1name.getText(), tag1value.getText(), tag2name.getText(), tag2value.getText());
			}
			
			// If no photos come up with this search query, then we cannot let them proceed to SearchResult page otherwise they'd be able to create an album out of nothing.
			if(searchResults.size() == 0) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Warning");
				alert.setHeaderText("No results");
				alert.setContentText("This search query returned no results.");
				alert.showAndWait();
				return;
			}
			
			searchResults = removeDuplicateSearchResults(searchResults);
			
			// Pass to SearchResults the results we got.
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SearchResult.fxml"));
			try{
				Parent parent = (Parent) loader.load();
				SearchResultController controller = loader.getController();
				Scene scene = new Scene(parent);
				
				// go is the button pressed action event
				Stage stage = (Stage) ((Node) cancel).getScene().getWindow();
				controller.start(stage, sesh, thisUser, searchResults);
				stage.setScene(scene);
				stage.centerOnScreen();
				stage.show();
			}
			catch (Exception exception) {
				System.out.println("TSC ok");
				exception.printStackTrace();
			}
		}
		
		else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning");
			alert.setHeaderText("Choose again!");
			alert.setContentText("Select an option to search upon.");
			alert.showAndWait();
		}
	}

	/**
	 * Returns the user to the SearchPhotos screen where they may alter their search criteria.
	 */
	public void cancelPressed() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SearchPhotos.fxml"));
		try{
			Parent parent = (Parent) loader.load();
			SearchPhotosController controller = loader.getController();
			Scene scene = new Scene(parent);
			
			// go is the button pressed action event
			Stage stage = (Stage) ((Node) cancel).getScene().getWindow();
			controller.start(stage, sesh, thisUser);
			stage.setScene(scene);
			stage.centerOnScreen();
			stage.show();
		}
		catch (Exception exception) {
			System.out.println("DSC cancel");
			exception.printStackTrace();
		}
	}

	/**
	 * Saves changes and returns the user to the Login Screen.
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
			System.out.println("thrown from logoutPressed in dateSearchController..");
			exception.printStackTrace();
		}
	}

	/**
	 * Resets all text fields.
	 */
	public void clearTextFields() {
		tag1name.clear();
		tag1value.clear();
		tag2name.clear();
		tag2value.clear();
	}
	
	/**
	 * Finds duplicate photos by comparing image paths and removes them if they are a duplicate.
	 * @param searchResults
	 * @return Returns searchResults but without duplicate photos.
	 */
	public ArrayList<Photo> removeDuplicateSearchResults(ArrayList<Photo> searchResults){
		ArrayList<Photo> nonDuplicates = new ArrayList<>();
		ArrayList<String> imagePaths = new ArrayList<>();
		
		for(Photo p : searchResults) {
			if(!imagePaths.contains(p.getLocation())){
				nonDuplicates.add(p);
				imagePaths.add(p.getLocation());
			}
		}
		
		return nonDuplicates;
	}
}
