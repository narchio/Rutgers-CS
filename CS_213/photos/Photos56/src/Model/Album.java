package Model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * The class Album will represent a photo album
 * @author Nicolas Carchio
 * @author Adam Romano
 */
public class Album implements Serializable{
	/**
	 * Serialization class version
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Name of the photo album
	 */
	private String name;
	/**
	 * List of photos inside the photo album
	 */
	private List<Photo> photos;
	/**
	 * Creating an instance of Album with specified album name
	 * @param name Name of the album
	 */
	public Album(String name) {
		this.name = name;
		this.photos = new ArrayList<Photo>();
	}
	/**
	 * Creating an instance of album with specified album name and list of photos
	 * @param name Name of the album
	 * @param photos Photos belonging to the album
	 */
	public Album(String name, List<Photo> photos) {
		this.name = name;
		this.photos = photos;
	}
	/**
	 * Returns the name of the photo album
	 * @return Name of album
	 */
	public String getName() {
		return this.name;
	}
	/**
	 * Assigns a name to photo album
	 * @param name Name for album
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * Returns the list of photos for the album
	 * @return List of photos
	 */
	public List<Photo> getPhotos(){
		return this.photos;
	}
	/**
	 * Assigns photos to the album
	 * @param photos Photos for the album
	 */
	public void setPhotos(List<Photo> photos) {
		this.photos = photos;
	}
	/**
	 * Inserts photo into list of photos for the album
	 * @param photo Photo to insert into list
	 */
	public void addPhoto(Photo photo) {
		this.photos.add(photo);
	}
	/**
	 * Removes photo from the list of photos for the album
	 * @param photo Photo to remove from the list
	 */
	public void deletePhoto(Photo photo) {
		this.photos.remove(photo);
	}
	/**
	 * Looks for duplicate photo in the album
	 * @param photo
	 * @return true/false
	 */
	public boolean inPhotoList(Photo photo) {
		for (Photo p : this.photos) {
			if (p.equals(photo)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the date range of photos in an album
	 * @return the toString() of th min and max dates
	 */
	public String getDateRange() {
		if (this.photos == null || this.photos.size() == 0) {
			return "{empty}";
		}
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar min = Calendar.getInstance();
		min.set(9999, 12, 31);
		min.set(Calendar.MILLISECOND,0);
		Calendar max = Calendar.getInstance();
		max.set(1, 12, 31);
		max.set(Calendar.MILLISECOND,0);
		for (Photo p : this.photos) {
			if (p.getDate().getTime().before(min.getTime())) {
				min.setTime(p.getDate().getTime());
			}
			if (p.getDate().getTime().after(max.getTime())) {
				max.setTime(p.getDate().getTime());
			}
		}
		// return dates as strings
		String minDate = dateFormat.format(min.getTime());
		String maxDate = dateFormat.format(max.getTime());
		return minDate + " to " + maxDate;
	}
	/**
	 * Equals method for Album object
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof Album)) {
			return false;
		}
		Album other = (Album)obj;
		return this.name.equals(other.getName());
	}
	/**
	 * To string method for album
	 */
	public String toString() {
		return this.name;
	}
}
