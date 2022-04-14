package f2cpgm;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene; // widgets will be in javafx.scene.control.*
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/*
 * This is a Programming layout meaning:
 * The GUI/interface logic is done alongside the programming/logic. 
 */

public class F2CPgm extends Application {

	// abstract method start from Application superclass which we must implement.
	public void start(Stage primaryStage) {
		GridPane root = makeGridPane();
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Fahrenheit-Celsius");
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
		// You pass args to launch
		// Program only terminates after launch terminates.
	}
	
	private static GridPane makeGridPane() {

		// all the widgets
		Text fText = new Text("Fahrenheit");
		Text cText = new Text("Celsius");
		TextField f = new TextField();
		TextField c = new TextField();
		Button f2c = new Button(">>>");
		Button c2f = new Button("<<<");

		GridPane gridPane = new GridPane();
		gridPane.add(fText, 0, 0);
		gridPane.add(f2c, 1, 0);
		gridPane.add(cText, 2, 0);
		gridPane.add(f, 0, 1);
		gridPane.add(c2f, 1, 1);
		gridPane.add(c, 2, 1);

		f.setPrefColumnCount(5);
		f.setPromptText("-40.0");
		c.setPrefColumnCount(5);
		c.setPromptText("-40.0");
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(10,10,10,10));
		GridPane.setValignment(fText, VPos.BOTTOM);
		GridPane.setValignment(cText, VPos.BOTTOM);
		
		// event handling
		/*
		 *  For the event handling code you need to do the following:
		 *  	Define a class that implements the EventHandler interface
		 *  	Create an instance of this class
		 *  	Pass the instance to the setOnAction method for f2c and c2f, 
		 *  		thereby registering event handlers with the conversion buttons.
		 *  
		 *  Sometimes it is much simpler not to explicitly define a class that 
		 *  implements an interface + create an instance to pass to setOnAction.
		 *  
		 *  You can do that entire thing implicitly by
		 *  new EventHandler<ActionEvent>(){
		 *  	public void hande(ActionEvent e) {...}
		 *  }
		 *  
		 *  This creates an instance of EventHandler<ActionEvent> and passes that to setOnAction.
		 *  Note that the class definition is within the top level curly braces
		 *  And the class name is not specified
		 *  But it is implicitly implementing the EventHandler interface
		 */
		f2c.setOnAction(new EventHandler<ActionEvent>() {
			// handle is a method of EventHandler which we must implement.
			// Note that EventHandler is an interface. 
			public void handle(ActionEvent e) {
			  
			   float fval = Float.valueOf(f.getText());
			   float cval = (fval-32)*5/9;
			   c.setText(String.format("%5.1f", cval));
	        }
		});

		
	    c2f.setOnAction(new EventHandler<ActionEvent>() {
	        public void handle(ActionEvent e) {
	        	
			   float cval = Float.valueOf(c.getText());
			   float fval = cval*9/5+32;
			   f.setText(String.format("%5.1f", fval));
	        }	
	    });	

		return gridPane;
	}
}
