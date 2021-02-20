//Dhruti Shah and Kev Sharma

package app;

import java.util.Comparator;

public class Song implements Comparator<Song>{
	
	String name;
	String artist;
	String album;
	String year;
	
	public Song() {
		name = "";
		artist = "";
		album = "";
		year = "";
	}
	
	public Song(String name, String artist) {
		this.name = name;
		this.artist = artist;
	}

	public Song(String name, String artist, String album, String year) {
		this.name = name;
		this.artist = artist;
		this.album = album;
		this.year = year;
	}
	
	
	public String getSongName() {
		return name;
	}
	
	public String getArtist() {
		return artist;
	}
	
	public String getAlbum() {
		if (album==null) {
			return "";
		}
		else return album;
	}
	
	public String getYear() {
		if (year==null) {
			return "";
		}
		else return year;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setArtist(String artist) {
		this.artist = artist;	
	}
	
	public void setAlbum(String album) {
		this.album = album;
	}
	
	public void setYear(String year) {
		this.year = year;
	}
	
	public String toString() {
		return (name +", " + artist);
	}

	@Override
	public int compare(Song o1, Song o2) {
		// Compare the song names, if they are the same, then compare the artist names.
		if(o1.getSongName().compareToIgnoreCase(o2.getSongName().toUpperCase()) != 0)
			return ((o1.getSongName().toUpperCase()).compareTo(o2.getSongName().toUpperCase()));
		
		return (o1.getArtist().compareToIgnoreCase(o2.getArtist().toUpperCase()));
	}
}
	

