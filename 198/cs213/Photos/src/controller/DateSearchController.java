/**
 * @author Dhruti Shah & Kev Sharma
 */
package controller;

import java.util.ArrayList;
import java.util.Calendar;
import javafx.scene.control.Alert.AlertType;
import model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.*;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.text.ParseException;
import java.text.SimpleDateFormat; 

/**
 * Date Search controller is used to search based on Date. It returns a result much like Tag Search.
 * @author Dhruti Shah
 * @author Kev Sharma
 *
 */
public class DateSearchController {
	/**
	 * Button ok has onAction method okPressed();
	 */
	@FXML private Button ok;
	/**
	 * Button logout has onAction method logoutPressed();
	 */
	@FXML private Button logout; 
	/**
	 * Button quit has onAction method quitpressed();
	 */
	@FXML private Button cancel;
	/**
	 * DatePicker startDate is a non editable field which returns a LocalDate.
	 */
	@FXML private DatePicker startDate;
	/**
	 * DatePicker endDate is a non editable field which returns a valid LocalDate
	 */
	@FXML private DatePicker endDate;
	
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
	 * @param mainStage The stage upon which the Date search scene is.
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
	 * Returns all Photos from the thisUser which fall within a given date range
	 * provided that startDate and endDate DatePicker fields are populated.
	 */
	public void okPressed() {
		// we have to pass in one of the choices.
		// if either startDate or endDate not initialized then we have to return error.
		if(startDate.getValue() == null || endDate.getValue() == null) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Error");
			alert.setHeaderText("Dates not recognized.");
			alert.setContentText("Must choose both dates\nAllow them to follow default format.");
			alert.showAndWait();
			return;
		}
		
		try {
			
			Date to = Date.valueOf(startDate.getValue());
			Date from = Date.valueOf(endDate.getValue());
			
			// properly formatted.
			if(to.equals(from) || to.compareTo(from)>0) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Error");
				alert.setHeaderText("Dates improper.");
				alert.setContentText("Second date must not\n equal or be less than the first date.");
				alert.showAndWait();
				return;
			}
			
			ArrayList<Photo> searchResults = PhotosThatFallWithinRange(to, from);
			
			if(searchResults.size()==0) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Error");
				alert.setHeaderText("No matches.");
				alert.setContentText("None of your photos fall within this range.");
				alert.showAndWait();
				return;
			}
			
			searchResults = removeDuplicateSearchResults(searchResults);
			
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
			
		} catch (ParseException e) {e.printStackTrace();}
					
	}
	
	/**
	 * @param sd Start Date
	 * @param ed End Date
	 * @return Returns an arrayList of photos that fall within the start date and end date.
	 * @throws ParseException Improper formatting of Dates (through DatePicker)
	 */
	public ArrayList<Photo> PhotosThatFallWithinRange(Date sd, Date ed) throws ParseException {
		ArrayList<Photo> searchResults = new ArrayList<>();

		//SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/YYYY");
		
		for(Album a : thisUser.getAlbums()) {
			for(Photo P : a.getPhotos()) {
				java.util.Date thisDate = P.getCal().getTime();
				if(thisDate.equals(sd) || thisDate.equals(ed) || (thisDate.compareTo(sd)>0 && thisDate.compareTo(ed)<0)) {
					searchResults.add(P);
				}
			}
		}
		
		return searchResults;
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
			System.out.println("thrown from logoutPressed in dateSearchController.");
			exception.printStackTrace();
		}
	}

	/**
	 * Finds duplicate photos by comparing image paths and removes them if they are a duplicate.
	 * @param searchResults Results of Search matches.
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

