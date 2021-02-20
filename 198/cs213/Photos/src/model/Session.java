package model;
import java.util.ArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * The Session class aggregates User objects.
 * @author Dhruti Shah
 * @author Kev Sharma
 */
public class Session implements Serializable {

	/**
	 * users stores a collection of User objects.
	 */
	private ArrayList<User> users;
    
	/**
	 * this is used for saving.
	 */
	private static final String storeDir = "data";
	/**
	 * this is used for saving.
	 */
    private static final String storeFile = "data.dat";
    /**
	 * this is used for saving.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Initializes a Session object. To populate the Session's User objects, we must call loadData()
	 */
	public Session() {
		users = new ArrayList<>();
	}
	
	/**
	 * Adds a User to this Session object's aggregation of User objects.
	 * @param u User
	 */
	public void addUser(User u) {
		users.add(u);
	}
	
	/**
	 * Removes a User from this Session object's aggregation of User objects.
	 * @param u User
	 */
	public void removeUser(User u) {
		users.remove(u);
	}
	
	/**
	 * @return Returns an ArrayList of User objects aggregated by this Session object.
	 */
	public ArrayList<User> getUsers(){
		return users;
	}
	
	/**
	 * This method saves the session to a file data.dat under directory data.
	 */
	public void saveData() {
		try {
			FileOutputStream fos = new FileOutputStream(storeDir + File.separator + storeFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(users);
            oos.close();
            fos.close();
            
        } catch (Exception e) {
        	System.out.println("saveData() failed");
            e.printStackTrace();
        }
	}
	
	/**
	 * This method loads the saved data from the last time the program was launched and initializes the invoking Session object.
	 */
    public void loadData() {
	    try{
	    	FileInputStream fis = new FileInputStream(storeDir + File.separator + storeFile);
	        ObjectInputStream ois = new ObjectInputStream(fis);

			ArrayList<User> uwu = (ArrayList<User>) (ois.readObject());
	        
	        users.clear();
	        for(User u : uwu) {
	        	this.addUser(u);
	        }
	        
	        ois.close();
	        fis.close();
	        
	    } catch(Exception e) {
	    	System.out.println("loadData failed"); e.printStackTrace();
	    }
    }
}
