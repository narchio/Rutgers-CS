package Model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The User class will represent a user of the application.
 * @author Nicolas Carchio
 * @author Adam Romano
 */
public class User implements Serializable{
	/**
	 * Serialization class version
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * User's username
	 */
	private String username;
	/**
	 * User's albums
	 */
	private List<Album> albums;
	/**
	 * User's current Album
	 */
	private static Album currentAlbum;
	/**
	 * User's current photo
	 */
	private static Photo currentPhoto;
	/**
	 * List of default tags for the user
	 */
	private List<Tag> defaultTags;
	/**
	 * Creating instance of User with specified username
	 * @param username User's username
	 */
	public User(String username) {
		this.username = username;
		this.albums = new ArrayList<Album>();
		defaultTags = new ArrayList<Tag>(){
			{
				add(new Tag("Create new default tag", ""));
				add(new Tag("Person", ""));
				add(new Tag("Location", ""));
				add(new Tag("Event", ""));

			}
		};
	}
	/**
	 * Return index of particular album in list
	 */
	public int getAlbumIndexInList(String album){
		for(int i = 0; i < this.albums.size(); i++){
			if(this.albums.get(i).getName().equals(album)){
				return i;
			}
		}
		return -1;
	}
	/**
	 * Return the list of default tags
	 */
	public List<Tag> getDefaultTags() {
		return this.defaultTags;
	}
	/**
	 * Add a default tag to the list
	 */
	public void addDefaultTag(Tag tag) {
		this.defaultTags.add(tag);
	}
	/**
	 * Get list of default tags as strings for the combo box
	 */
	public List<String> getDefaultTagsAsStrings() {
		List<String> result = new ArrayList<String>();
		for(Tag t: this.defaultTags){
			result.add(t.getName());
		}
		return result;
	}
	/**
	 * Returns the user's username
	 * @return User's username
	 */
	public String getUsername() {
		return this.username;
	}
	/**
	 * Returns the user's albums
	 * @return User's albums
	 */
	public List<Album> getAlbums(){
		return this.albums;
	}
	/**
	 * Inserts album into user's list of albums
	 * @param album Album to insert into the list
	 */
	public void addAlbum(Album album) {
		this.albums.add(album);
	}
	/**
	 * Removes album from user's list of albums
	 * @param album Album to remove from the list
	 */
	public void deleteAlbum(Album album) {
		this.albums.remove(album);
	}
	/**
	 * Set User's current Album
	 */
	public void setCurrentAlbum(Album a){
		currentAlbum = a;
	}
	/**
	 * Get User's current album
	 */
	public Album getCurrentAlbum() {
		return currentAlbum;
	}
	/**
	 * Set User's current Photo
	 */
	public void setCurrentPhoto(Photo p){
		currentPhoto = p;
	}
	/**
	 * Get User's current photo
	 */
	public Photo getCurrentPhoto() {
		return currentPhoto;
	}
	/**
	 * Looks for duplicate in the album
	 * @param album
	 * @return
	 */
	public boolean inAlbumList(String album) {
		for (Album a : this.albums) {
			if (a.getName().equals(album)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * Equals method for User object
	 */
	public boolean equals(Object obj){
		if(obj == null || !(obj instanceof User)){
			return false;
		}
		User u = (User) obj;
		return this.username.equals(u.getUsername());
	}
	/**
	 * To string method for user
	 */
	public String toString() {
		return this.username;
	}
}
