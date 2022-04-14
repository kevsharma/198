// Dhruti Shah & Kev Sharma

package app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

//import com.sun.tools.javac.parser.Scanner;

public class SongList {
	
	// Our list of Song objects.
	private ArrayList<Song> songList;
	
	public SongList() {
		songList = new ArrayList<Song>();
	}
	
	public ArrayList<Song> getSongList(){
		return songList;
	}

	/*
	 * 0 if successful add.
	 * -1 if either name/artist are empty
	 * -2 if duplicate song
	 * -3 if valid integer was not passed.
	 */
	public int addable(String name, String artist, String album, String year) throws IOException{
		if(name.isEmpty() || artist.isEmpty())
			return -1;
		
		for(Song s : songList)
			if(name.compareToIgnoreCase(s.getSongName()) == 0 && (artist.compareToIgnoreCase(s.getArtist()) == 0))
				return -2;
		
		int inputYear = 0;
		if(!year.isEmpty()) {
			try {
				inputYear = Integer.parseInt(year);
			  	if (inputYear<0 || inputYear>2020)
			  		return -3;
			} 
			catch (NumberFormatException e) {
				return -3;
			}
		}
		
		// Otherwise return 0.
		return 0;
	}
	
	public void add(Song s) {
		// This is the method to call after confirmation.
		songList.add(s);
		songList.sort(new Song());
	}
	
	/*
	 * Returns index of next song,
	 * if none exist then returns previous song.
	 * 
	 * If no songs left, returns -1.
	 */
	public int delete(int index){
		songList.remove(index);
		
		if(songList.size() == 0)
			return -1;
		else if(songList.size() == index)
			return index-1;
		
		return index;
	}
	
	/*
	 * 0 if successful edit.
	 * -1 if either name/artist are empty
	 * -2 if duplicate song (still allows changing of album/year this way)
	 * -3 if valid integer was not passed.
	 */
	public int editable(int index, String name, String artist, String album, String year) {
		if(name.isEmpty() || artist.isEmpty())
			return -1;
		
		for(int i=0; i<songList.size(); i++) {
			if(i==index)
				continue;
			
			Song s = songList.get(i);
			if(name.compareToIgnoreCase(s.getSongName()) == 0 && (artist.compareToIgnoreCase(s.getArtist()) == 0))
				return -2;
		}
			
		int inputYear = 0;
		if(!year.isEmpty()) {
			try {
				inputYear = Integer.parseInt(year);
			  	if (inputYear<0 || inputYear>2020)
			  		return -3;
			} 
			catch (NumberFormatException e) {
				return -3;
			}
		}
		
		return 0;
	}
	
	public void edit(int index, Song s) {
		songList.set(index, s);
		songList.sort(new Song());
	}
	
	public void populateSongs()
	{
		// Read from file
		try {
			File f = new File("src/view/library.txt");
			Scanner scan = new Scanner(f);

			// Populate each through file.
			while(scan.hasNext()) {
				Song newSong = new Song();
				for(int i=1; i<=4; i++) {
					switch(i) {
					case 1: newSong.name 	= scan.nextLine().trim(); break;
					case 2: newSong.artist	= scan.nextLine().trim(); break;
					case 3: newSong.album 	= scan.nextLine().trim(); break;
					case 4: newSong.year 	= scan.nextLine().trim(); break;

					default: System.out.println("We defaulted."); break;
					}
				}
				songList.add(newSong);
			}

			scan.close();
		}
		catch(FileNotFoundException e) {
			System.out.println("File not found in SongList.java populateSongs method");
		}
	}

	public void saveSongs() {
		// Write to file
		try{
			PrintWriter saver = new PrintWriter("src/view/library.txt");
			for(Song s : songList) {
				saver.println(s.name); 
				saver.println(s.artist);
				saver.println(s.album);
				saver.println(s.year); 
			}
			
			saver.close();

		} catch (FileNotFoundException e) {
			System.out.println("File not found in SongList.java populateSongs method");
		}
	}
}