package model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The User Class aggregates Album objects.
 * @author Dhruti Shah
 * @author Kev Sharma
 */
public class User implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Each user has a username.
	 */
	private String username;
	
	/**
	 * Each user aggregates zero or more Album objects.
	 */
	private ArrayList<Album> albums;
	
	/**
	 * Initializes a User with the given String parameter.
	 * @param username String
	 */
	public User(String username) {
		this.username = username;
		albums = new ArrayList<>();
	}
	
	/**
	 * @return Returns the User's String username attribute..
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * @return Returns this users aggregated Albums.
	 */
	public ArrayList<Album> getAlbums() {
		return albums;
	}
	
	/**
	 * @param a Adds one more album to the User's aggregation of Album objects.
	 */
	public void addAlbumToUser(Album a) {
		albums.add(a);
	}
	
	/**
	 * This method removes an Album from the User's aggregation of Albums.
	 * @param a Album
	 */
	public void removeAlbumFromUser(Album a){
		albums.remove(a);
	}
	
	/**
	 * @return String Returns the User object's username.
	 */
	public String toString() {
		return getUsername();
	}
	
	/**
	 * @param o Object
	 * @return Returns True if the Object is a user with same username attribute as invoker.
	 */
	public boolean equals(Object o) {
		if(!(o instanceof User)) {
			return false;
		}
		
		User temp = (User) o;
		if(temp.getUsername().equals(this.getUsername())) {
			return true;
		}
		
		return false;
	}
		
	/**
	 * @return Returns all Tags for All Photos of Every Album this User aggregates.
	 */
	public ArrayList<String> getDefinedTags(){
		ArrayList<String> t = new ArrayList<>();
		
		for(Album a : albums) {
			for(Photo p : a.getPhotos()) {
				for(String key : p.getTags().keySet()) {
					if(!t.contains(key))
						t.add(key);
				}
			}
		}
		
		return t;
	}
	
	/**
	 * Searches all Photos of every Album of this User for a particular tag.
	 * @param key1 First Tag name
	 * @param value1 First Tag value
	 * @return Returns an ArrayList of Photo objects which have this Tag.
	 */
	public ArrayList<Photo> searchRegular(String key1, String value1){
		ArrayList<Photo> matches = new ArrayList<>();
		for(Album a : albums) {
			for(Photo tempPhoto : a.getPhotos()) {
				// For every single photo, determine whether there the key exists.
				if(tempPhoto.getTags().containsKey(key1)){
					if(tempPhoto.getTags().get(key1) != null) {
						if(tempPhoto.getTags().get(key1).contains(value1)) {
							matches.add(tempPhoto);
						}
					}
				}
			}
		}
		
		return matches;
	}
	
	/**
	 * Searches all Photos of every Album of this User for either Tag 1 or Tag 2
	 * @param key1 First Tag Name
	 * @param value1 First Tag Value
	 * @param key2 Second Tag Name
	 * @param value2 Second Tag Value
	 * @return Returns an ArrayList of Photo Objects which have either of the two tags.
	 */
	public ArrayList<Photo> searchDisjunctive(String key1, String value1, String key2, String value2){
		ArrayList<Photo> matches = new ArrayList<>();
		
		for(Album a : albums) {
			for(Photo tempPhoto : a.getPhotos()) {
				// Check whether the key exists, and whether value exists for that key.
				if(tempPhoto.getTags().containsKey(key1) && tempPhoto.getTags().get(key1)!=null) {
					if(tempPhoto.getTags().get(key1).contains(value1)) 
						matches.add(tempPhoto);	
				}
				
				if(tempPhoto.getTags().containsKey(key2) && tempPhoto.getTags().get(key2)!=null) {				
					if(tempPhoto.getTags().get(key2).contains(value2))
						matches.add(tempPhoto);
				}
			}
		}
		
		return matches;	
	}
	
	/**
	 * Searches all Photos of every Album of this User for both Tag 1 and Tag 2
	 * @param key1 First Tag Name
	 * @param value1 First Tag Value
	 * @param key2 Second Tag Name
	 * @param value2 Second Tag Value
	 * @return Returns an ArrayList of Photo objects which have both these tags.
	 */
	public ArrayList<Photo> searchConjunctive(String key1, String value1, String key2, String value2){
		ArrayList<Photo> matches = new ArrayList<>();
		
		for(Album a : albums) {
			for(Photo tempPhoto : a.getPhotos()) {
				// For every single photo, determine whether there the key exists, then whether value for those keys exists.
				if(tempPhoto.getTags().containsKey(key1) && tempPhoto.getTags().containsKey(key2)) {
					// Value for these keys exist.
					if(tempPhoto.getTags().get(key1)!=null && tempPhoto.getTags().get(key2)!=null) {
						if(tempPhoto.getTags().get(key1).contains(value1) && tempPhoto.getTags().get(key2).contains(value2))
							matches.add(tempPhoto);
					}
				}
			}
		}
		
		return matches;	
	}
	
	public boolean keyvalPairExists(String key, String value) {
		return true;
	}
	
	public ArrayList<Photo> searchByPerson(String personValue){
		ArrayList<Photo> results = new ArrayList<>();
		for(Album a : albums) {
			for(Photo p : a.getPhotos()) {
				if(keyvalPairExists("person", personValue)) {
					results.add(p);
				}
			}
		}
		return results;
	}
	
	public ArrayList<Photo> searchByLocation(String locationValue){
		ArrayList<Photo> results = new ArrayList<>();
		for(Album a : albums) {
			for(Photo p : a.getPhotos()) {
				if(keyvalPairExists("location", locationValue)) {
					results.add(p);
				}
			}
		}
		return results;
	}
	
	public ArrayList<Photo> searchByPersonOrLocation(String personValue, String locationValue){
		ArrayList<Photo> results = new ArrayList<>();
		for(Album a : albums) {
			for(Photo p : a.getPhotos()) {
				if(keyvalPairExists("location", locationValue) ||
						keyvalPairExists("person", personValue)) {
					results.add(p);
				}
			}
		}
		return results;
	}
	
	public ArrayList<Photo> searchByPersonAndLocation(String personValue, String locationValue){
		ArrayList<Photo> results = new ArrayList<>();
		for(Album a : albums) {
			for(Photo p : a.getPhotos()) {
				if(keyvalPairExists("location", locationValue) &&
						keyvalPairExists("person", personValue)) {
					results.add(p);
				}
			}
		}
		return results;
	}
}

























