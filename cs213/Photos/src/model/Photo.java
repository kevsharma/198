package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.io.File;

/**
 * The Photo Class implements an Image, and its Date and caption.
 * @author Dhruti Shah
 * @author Kev Sharma
 */
public class Photo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	/**
	 * String caption represents what a Photo is captioned with.
	 */
	private String caption;
	/**
	 * String imgLocation represents what a Photo's filepath location is.
	 */
	private String imgLocation;
	/**
	 * Calendar cal represents what a Photo's corresponding file's last modified date is.
	 */
	private Calendar cal;
	/**
	 * HashMap Tags is used to represent a tag key,value pair.
	 */
	private HashMap<String, ArrayList<String>> Tags;
	
	/**
	 * Initialized this Photo's caption, date last modified, and imgpath with the given String parameter
	 * @param imgLocation This describes the absolute path of the Image located on the user's computer.
	 */
	public Photo(String imgLocation) {
		this.imgLocation = imgLocation;
		setCaption("");
		Tags = new HashMap<String, ArrayList<String>>();
		
		cal = Calendar.getInstance();
        cal.setTimeInMillis(new File(imgLocation).lastModified());
        
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
	}
	
	/**
	 * Sets the location of this img to the Specified absolute path given in String parameter.
	 * @param imgLocation
	 */
	public void setLocation(String imgLocation) {
		this.imgLocation = imgLocation;
	}
	
	/**
	 * Sets this Photo's caption to the given Caption.
	 * @param caption String
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}
	
	/**
	 * @return Return the String this photo is captioned with.
	 */
	public String getCaption() {
		return caption;
	}
	
	/**
	 * @return Returns the String imgLocation.
	 */
	public String getLocation() {
		return imgLocation;
	}
	
	/**
	 * @return Returns the Calendar attribute associated with this Photo which stores its last modified photo.
	 */
	public Calendar getCal() {
		return cal;
	}
	
	/**
	 * @return Returns the last modified date of this image formatted as MM/dd/YYYY
	 */
	public String getDate() {
		return new SimpleDateFormat("MM/dd/YYYY").format(cal.getTime());
	}
	
	/**
	 * @param key String tag name.
	 * @param value String tag value.
	 * @return Returns true if this tag was created and assigned to this Photo.
	 */
	public boolean newTag(String key, String value) {
		if(Tags.containsKey(key)) {
			// Location can only have 1 Tag.
			if(key.toLowerCase().equals("location")) {
				ArrayList<String> temp = Tags.get(key);
				temp.clear();
				temp.add(value);
				Tags.put(key, temp);
			}
			else {
				ArrayList<String> temp = Tags.get(key);
				// No duplicate.
				if(temp.contains(value)) {
					return false;
				}
				temp.add(value);
				Tags.put(key, temp);
			}
		}
		else {
			ArrayList<String> temp = new ArrayList<>();
			temp.add(value);
			Tags.put(key, temp);
		}
		return true;
	}
	
	/**
	 * @return Returns all Tags belonging to this Photo in an easy to read manner.
	 */
	public ArrayList<String> getTagsFormatted(){
		ArrayList<String> forListview = new ArrayList<>();
		
		for(String key : Tags.keySet()) 
			for(String value : Tags.get(key)) 
				forListview.add("Tag Name: " + key + "\tTag Value: " + value);
	
		return forListview;
	}
	
	/**
	 * @param key String
	 * @return Returns an arraylist of strings of tag values for a particular key.
	 */
	public ArrayList<String> getValue(String key) {
		return Tags.get(key);
	}
	
	/**
	 * Removes this Tag name|value pair from the Photo
	 * @param key 
	 * @param value 
	 */
	public void removeTag(String key, String value) {
		ArrayList<String> values = Tags.get(key);
		values.remove(value);
		
		if(values.size()==0)
			Tags.remove(key);
		else
			Tags.put(key, values);
	}
	
	/**
	 * @return Returns the number of tags this Photo is tagged with.
	 */
	public int numTags() {
		return Tags.size();
	}
	
	/**
	 * @return Returns the set of all Tags (implemented using a hashmap).
	 */
	public HashMap<String, ArrayList<String>> getTags(){
		return Tags;
	}
	
	/**
	 * @return Returns a set of all Tag names.
	 */
	public Set<String> getExistingTags(){
		return Tags.keySet();
	}
	
	/**
	 * @return Returns true if this object o is a Photo with the same image path as the invoker.
	 */
	public boolean equals(Object o) {
		if(!(o instanceof Photo))
			return false;
		
		Photo in = (Photo) o;
		if(in.getLocation().equals(this.getLocation()))
			return true;
		
		return false;
	}
}

