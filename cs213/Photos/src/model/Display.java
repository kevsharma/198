
package model;

import java.io.File;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * The Display class is used to show Thumbnail|Caption|Date of a given Photo in a TableView.
 * @author Dhruti Shah
 * @author Kev Sharma
 */
public class Display {
	
	/**
	 * An instance of Photo
	 */
	private Photo p;
	
	/**
	 * An imageView to show this Photo.
	 */
	private ImageView imgView;
	
	/**
	 * The Caption to show for this Photo
	 */
	private String caption;
	
	/**
	 * The last modified date of this photo.
	 */
	private String date;
	
	/**
	 * Returns a Display object to be used by a TableView.
	 * @param p A photo object.
	 */
	public Display(Photo p) {
		this.p = p;
		
		File fp = new File(p.getLocation());
		Image i = new Image(fp.toURI().toString(), 177,177,true,true);
		
		imgView = new ImageView(i);
		imgView.setFitWidth(177);
		
		caption = p.getCaption();
		date = p.getDate();
	}
	
	/**
	 * @return Returns an ImageView to match the Photo passed into constructor.
	 */
	public ImageView getImgView() {
		return imgView;
	}
	
	/**
	 * @return Returns the Photo's caption.
	 */
	public String getCaption() {
		return caption;
	}
	
	/**
	 * @return Returns the Photo's date.
	 */
	public String getDate() {
		return date;
	}
	
	/**
	 * Sets the Display Object's ImageView attribute to parameter.
	 * @param imgView ImageView calibrated to show image located at Photo's imgPath.
	 */
	public void setImgView(ImageView imgView) {
		this.imgView = imgView;
	}
	
	/**
	 * Sets the Display Object's Caption attribute to parameter.
	 * @param caption String to denote the Photo's caption.
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}
	
	/**
	 * Sets the Display Object's Date attribute to parameter.
	 * @param date String to denote to the Photo's last modified date.
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return Returns a Photo instance that this Display is representing.
	 */
	public Photo getPhoto() {
		return p;
	}
}