
package controller;

import model.*;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.*;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
 
/**
 * LoginController handles the login screen of the program.
 * @author Dhruti Shah
 * @author Kev Sharma
 */
public class LoginController {
	
	/**
	 * The Session sesh attribute maintains which Session we are working with. 
	 */
	Session sesh = null;
	
	/**
	 * Button go has onAction method goPressed()
	 */
	@FXML private Button go; 
	/**
	 * Button quit has onAction method quitPressed()
	 */
	@FXML private Button quit; 
	/**
	 * TextField username takes in a nonempty String.
	 */
	@FXML private TextField username;
	
	/**
	 * Background processes to set values to private attributes.
	 * @param mainStage The stage upon which the Login scene is.
	 * @param sesh The session object we are using.
	 */
	public void start(Stage mainStage, Session sesh) {
		this.sesh = sesh;
		this.sesh.loadData();
		
		mainStage.setOnCloseRequest(event -> {
			sesh.saveData();
			System.exit(0);
		});
	}
	
	/**
	 * Takes the user to the admin subsystem if inputted User is "admin", 
	 * or takes them to Album screen for a particular user if that inputted user exists.
	 * @param event On button click.
	 */
	public void goPressed(ActionEvent event) {
		
		String inputtedUsername = username.getText().trim();
		
		if(inputtedUsername.equals("admin")) {
			// take to admin subsystem.
			//System.out.println("wwe got here admin");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Admin.fxml"));
			try {
				((Node) (event.getSource())).getScene().getWindow().hide();
				Parent parent = (Parent) loader.load();
				AdminController controller = loader.getController();
				Scene scene = new Scene(parent);
				Stage stage = (Stage) ((Node) go).getScene().getWindow();
				controller.start(stage, sesh);
				stage.setScene(scene);
				stage.centerOnScreen();
				stage.show();
				return;
			} catch(Exception e) {System.out.println("failure logging into admin"); e.printStackTrace();};
		}
		
		else {
			User temp = null;
			this.sesh.loadData();
			// Find whether this user exists.
			for(User u : this.sesh.getUsers()) {
				if(u.equals(new User(inputtedUsername))) {
					// take to non-admin subsystem.
					temp = u;
				}
			}
				
			// no such user found:
			if(temp == null){
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Incorrect Username");
				alert.setHeaderText("Please try again");
				alert.setContentText("No such user exists.");
				username.setText("");
				alert.showAndWait();
				return;
			}
			
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Albums.fxml"));
				((Node) (event.getSource())).getScene().getWindow().hide();
				Parent parent = (Parent) loader.load();
				AlbumsController controller = loader.getController();
				Scene scene = new Scene(parent);
				Stage stage = (Stage) ((Node) go).getScene().getWindow();
				// passing to start very ipmortant.
				controller.start(stage, sesh, temp);
				stage.setScene(scene);
				stage.centerOnScreen();
				stage.show();
			} catch(Exception e) {System.out.println("failure logging into stock"); e.printStackTrace();};
			
			
		}
	}
}
