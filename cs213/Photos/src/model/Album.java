package model;
import java.util.ArrayList;
import java.util.Calendar;
import java.io.Serializable;
import java.text.SimpleDateFormat;

/**
 * The Album class aggregates Photo objects and performs operations on them.
 * @author Dhruti Shah
 * @author Kev Sharma
 */
public class Album implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * The list of Photos this album aggregates.
	 */
	private ArrayList<Photo> photos; 
	
	/**
	 * The name (String) of this Album instance.
	 */
	private String albumName;

	/**
	 * Lower bound in the range of dates of photos aggregated by this Instance.
	 */
	private Calendar lowerbound;
	/**
	 * Upper bound in the range of dates of photos aggregated by this Instance.
	 */
	private Calendar upperbound;
	
	/**
	 * Instantiates an Album object and gives it a name.
	 * @param albumName String name.
	 */
	public Album(String albumName) {
		this.albumName = albumName;
		lowerbound = upperbound = null;
		photos = new ArrayList<>();
	}
	
	/**
	 * Every album has a name, this method returns it.
	 * @return String This album instance's name.
	 */
	public String getAlbumName() {
		return albumName;
	}
	
	/**
	 * Every album has a name, this method sets it to the parameter String.
	 * @param name String
	 */
	public void setAlbumName(String name) {
		albumName = name;
	}
	
	/**
	 * @return Returns the number of Photos aggregated in this instance.
	 */
	public int numPhotos() {
		return photos.size();
	}
	
	/**
	 * @return Returns the Photo objects this class is aggregating.
	 */
	public ArrayList<Photo> getPhotos(){
		return photos;
	}
	
	/**
	 * This method adds the given Photo parameter to this instance's aggregation of Photo instances.
	 * @param p Photo
	 */
	public void addPhoto(Photo p) {
		photos.add(p);
		setBounds();
	}
	
	/**
	 * This method removes the given Photo parameter from this instance's aggregation of Photo instances.
	 * @param p Photo
	 */
	public void removePhoto(Photo p) {
		photos.remove(p);
		setBounds();
	}
	
	/**
	 * This method determines the range of Dates of the Photo instances this object aggregates.
	 */
	public void setBounds() {
		if(photos.size() == 0)
			return;
		
		lowerbound = upperbound = photos.get(0).getCal();
		// Min algorithm to find lowerbound
		for(int i=1; i<photos.size(); i++) {
			if(photos.get(i).getCal().after(upperbound)){
				upperbound = photos.get(i).getCal();
			}
			if(photos.get(i).getCal().before(lowerbound)) {
				lowerbound = photos.get(i).getCal();
			}
		}
	}
	
	/**
	 * @return Returns a string representation of the Album adjusted to account for whether any photos exist in the album.
	 */
	public String toString() {
		
		if(lowerbound!=null && upperbound!=null) {
			String lb = new SimpleDateFormat("MM/dd/YYYY").format(lowerbound.getTime());
			String ub = new SimpleDateFormat("MM/dd/YYYY").format(upperbound.getTime());
			return "Album: " + this.getAlbumName() + "\t\t# Photos: "+this.numPhotos() + "\t\tDate range: " + lb + " to " + ub;
		}
		return "Album: " + this.getAlbumName() + "\t\t# Photos: "+this.numPhotos() + "\t\tNo date range (Album empty)";
	}
	
	/**
	 * @param o Object
	 * @return Returns true if Object passed is an Album with the same name as invoker.
	 */
	public boolean equals(Object o) {
		if(!(o instanceof Album)) {
			return false;
		}
		
		Album temp = (Album) o;
		if(temp.getAlbumName().equals(albumName)) {
			return true;
		}
		
		return false;
	}
}