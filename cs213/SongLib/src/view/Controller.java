/*
 * Dhruti Shah & Kev Sharma
 * 
 */
package view;

import java.util.ArrayList;
import java.util.Optional;
import app.Song;
import app.SongList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;


public class Controller {
	
	ObservableList<Song> obsList = FXCollections.observableArrayList();
	SongList songList = new SongList();
	
	@FXML ListView<Song> listView;
	@FXML Button add, delete, edit;	
	@FXML Label songDetails,  songName, artist, album , year;
	@FXML TextField songField, artistField, albumField, yearField;
	@FXML TextField addSongField, addArtistField, addAlbumField, addYearField; 
	
	public void start(Stage mainStage){                

		songList.populateSongs();
		refresh();
		
		if(obsList.size() > 0)	// default is the first item selected
			listView.getSelectionModel().select(0);
		
		try {
			displaydetails();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
       //saves upon termination
		mainStage.setOnCloseRequest(event -> {
			songList.saveSongs();
		});
	}
	
	public void refresh(){
		ArrayList<Song> refresher = songList.getSongList();
		obsList.setAll(refresher);
		listView.setItems(obsList);
	}
	
	/*
	 *  onAction "#*" Methods below.
	 */
	public void userSelection(MouseEvent mouse) throws Exception {
		displaydetails();
	}
	
	//method tied to delete button to delete a song
	public void delete() throws Exception{

		if(obsList.isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("WARNING");
			alert.setHeaderText("The Library is Empty");
			alert.setContentText("The Library of Songs is empty, nothing to delete.");
			alert.showAndWait();
		}
		else {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("CONFIRMATION DIALOGUE");
			alert.setHeaderText("Are you sure you want to delete this song?");
			
			Optional<ButtonType> confirm = alert.showAndWait();
			
			if (confirm.get() == ButtonType.OK) {
				
				int selectedindex = listView.getSelectionModel().getSelectedIndex();
				selectedindex = songList.delete(selectedindex);
				refresh();
				
				if(selectedindex == -1) {
					displaydetails();
					return;
				}
				
				listView.getSelectionModel().select(selectedindex);
				displaydetails();			
			}
			else
				displaydetails();
		}
			
	}

	//Method tied to Add button to add a song
	public void addSong() throws Exception {
		String songname = addSongField.getText().trim();
		String artist = addArtistField.getText().trim();
		String album = addAlbumField.getText().trim();
		String year = addYearField.getText().trim();
		
		int check = songList.addable(songname, artist, album, year);
		
		if(check!=0) {
			checker(check);
			clearAddTextFields();
			return;
		}
		
		// Reaching this point implies all fields are correct, now ask to proceed.
		Song s = new Song(songname, artist, album, year);
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("CONFIRMATION DIALOGUE");
		alert.setHeaderText("Are you sure you want to add this song?");
		
		Optional<ButtonType> confirm = alert.showAndWait();
		
		if (confirm.get() == ButtonType.OK) {
			songList.add(s);
			refresh();
			
			int n = obsList.indexOf(s);
			listView.getSelectionModel().select(n);
			displaydetails();
			clearAddTextFields();
		}
		else 
			clearAddTextFields();
	}
		
	public void edit() throws Exception {
		// Nothing to edit.
		if(obsList.isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("WARNING");
			alert.setHeaderText("The Library is Empty");
			alert.setContentText("The Library of Songs is empty, nothing to edit.");
			alert.showAndWait();
			
			displaydetails();
			return;
		}

		int selectedindex = listView.getSelectionModel().getSelectedIndex();
		
		String songname = songField.getText().trim();
		String artist = artistField.getText().trim();
		String album = albumField.getText().trim();
		String year = yearField.getText().trim();
		
		int check = songList.editable(selectedindex, songname, artist, album, year);
		if(check!=0) {
			checker(check);
			return;
		}
		
		// Reaching this point implies all fields are correct, now ask to proceed.
		Song s = new Song(songname, artist, album, year);
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("CONFIRMATION DIALOGUE");
		alert.setHeaderText("Are you sure you want to edit this song?");
		
		Optional<ButtonType> confirm = alert.showAndWait();
		if (confirm.get() == ButtonType.OK) {
			songList.edit(selectedindex, s);
			refresh();
			
			int n = obsList.indexOf(s);
			listView.getSelectionModel().select(n);								
			displaydetails();
		}
		else
			displaydetails();
	}

	
	/*
	 * Helper methods for onAction methods to call.
	 */
	
	// Displays details for the currently selectedItem if list isn't empty. 
	// Otherwise shows empty details in TextField's above Edit.
	public void displaydetails() throws Exception {	
		if(!obsList.isEmpty()){
			
			Song selected = listView.getSelectionModel().getSelectedItem();
			
			songField.setText(selected.getSongName());			
			artistField.setText(selected.getArtist());			
			albumField.setText(selected.getAlbum());			
			yearField.setText(selected.getYear());	
		}
		else {
			songField.setText("");			
			artistField.setText("");			
			albumField.setText("");			
			yearField.setText("");			
		}
	}
	
	public void clearAddTextFields() {
		// Simple re-factoring for add method.
		addSongField.setText("");
		addArtistField.setText("");
		addAlbumField.setText("");
		addYearField.setText("");
	}
	
	// Note that the SongList.java class passes a check matching -1,-2,-3 or 0 (success).
	public void checker(int check) throws Exception{
		if(check == -1) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("WARNING");
			alert.setHeaderText("Invalid Format");
			alert.setContentText("Please include the song name and artist.");
			alert.showAndWait();
			
			displaydetails();
		}
		else if(check == -2) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("WARNING");
			alert.setHeaderText("Duplicate Song");
			alert.setContentText("A song already exists\n with the same name and artist.");
			alert.showAndWait();
			
			displaydetails();
		}
		else if(check == -3) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("WARNING");
			alert.setHeaderText("Invalid Format");
			alert.setContentText("The year must be a positive integer\n less than 2020");
			alert.showAndWait();
			
			displaydetails();
		}
	}

}