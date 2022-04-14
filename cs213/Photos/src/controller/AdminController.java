
package controller;

import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import model.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.*;
import java.io.IOException; 
import java.util.Optional;


 /**
  * Loads the Administrator Dashboard where admin can add, delete and see a list of users.
  * @author Dhruti Shah
  * @author Kev Sharma
  */
public class AdminController {
	
	/**
	 * Button logout has onAction method logoutPressed();
	 */
	@FXML private Button logout; 
	/**
	 * Button quit  has onAction method quitPressed();
	 */
	@FXML private Button quit;
	/**
	 * Button createUser has onAction method createUserPressed();
	 */
	@FXML private Button createuser;	
	/**
	 * Button deleteUser has onAction method deleteUser();
	 */
	@FXML private Button deleteuser; 
	/**
	 * Button listusers has onAction method listUsers();
	 */
	@FXML private Button listusers;	
	/**
	 * TextField usertextfield takes in a name for a new user.
	 */
	@FXML private TextField usertextfield; 
	/**
	 * ListView<User> userslistview shows a list of user's names.
	 */
	@FXML private ListView<User> userslistview;	
	
	/**
	 * Session sesh attribute keeps track of which Session to work with. 
	 */
	Session sesh;
	
	/**
	 * Background processes to set values to private attributes.
	 * @param mainStage The Stage upon which the Admin scene is.
	 * @param sesh The session object we are using.
	 * @throws IOException
	 */
	public void start(Stage mainStage, Session sesh) throws IOException { 
		this.sesh = sesh;
		this.sesh.loadData();

		listUsersPressed();
		
		mainStage.setOnCloseRequest(event -> {
			this.sesh.saveData();
			System.exit(0);
		});
	}
	
	
	/**
	 * If this method is executed, then changes made while here are saved and the User gets taken to the Login Screen.
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
	 * If proper User name entered, this creates a User and aggregates it to the Session.
	 */
	public void createUserPressed() {
		String userid = usertextfield.getText().trim();
		if(userid.equals("")){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Incorrect Username");
			alert.setHeaderText("Please try again");
			alert.setContentText("You have not entered a username.");
			usertextfield.setText("");
			alert.showAndWait();
		}
		
		else {
			User newUser = new User(userid);
			
			for(User u: sesh.getUsers()) {
				if(u.equals(newUser)) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Incorrect Username");
					alert.setHeaderText("Please try again");
					alert.setContentText("A User already exists with a similar username.");
					usertextfield.setText("");
					alert.showAndWait();
					return;
				}
			}
			
			usertextfield.setText("");
			sesh.addUser(newUser);
			sesh.saveData();
			listUsersPressed();
		}
	}

	/**
	 * Deletes a User from a given Session.
	 */
	public void deleteUserPressed() {
		// No user to delete.
		if(sesh.getUsers().size() == 0) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Delete");
			alert.setHeaderText("Error deleting.");
			alert.setContentText("No");
			usertextfield.setText("");
			alert.showAndWait();
			return;
		}
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete");
		alert.setHeaderText("Delete User");
		alert.setContentText("Do you want to delete the selected User and its contents?");
		
		Optional<ButtonType> confirm = alert.showAndWait();
		
		// Confirmation of delete. Session handles saving.
		if (confirm.get() == ButtonType.OK) {
			sesh.removeUser(userslistview.getSelectionModel().getSelectedItem());
			sesh.saveData();
			listUsersPressed();
		}
	}

	/**
	 * Lists all Users in a session.
	 */
	public void listUsersPressed() {
		sesh.loadData();
		userslistview.setItems(FXCollections.observableArrayList(sesh.getUsers()));
		userslistview.getSelectionModel().select(0);
	}	
}	

