package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * The class Photo represents a photo in a photo album
 * @author Nicolas Carchio
 * @author Adam Romano
 */
public class Photo implements Serializable{
	/**
	 * Serialization class version
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Name of the photo
	 */
	private String name;
	/**
	 * Caption of the photo
	 */
	private String caption;
	/**
	 * Tags associated with the photo
	 */
	private List<Tag> tags;
	/**
	 * Capture date of the photo
	 */
	private Calendar date;
	/**
	 * Thumbnail image of the photo
	 */
	private Thumbnail thumbnail;
	/**
	 * Creates instance of Photo with specified name, caption, thumbnail, and capture date
	 * @param name Name of the photo
	 * @param caption Caption of the photo
	 * @param thumbnail Thumbnail image
	 * @param date Capture date of the photo
	 */
	public Photo(String name, String caption, Thumbnail thumbnail, Calendar date) {
		this.name = name;
		this.caption = caption;
		this.tags = new ArrayList<Tag>();
		this.date = date;
		this.date.set(Calendar.MILLISECOND,0);
		this.thumbnail = thumbnail;
	}
	/**
	 * Returns the name of the photo
	 * @return Name of the photo
	 */
	public String getName() {
		return this.name;
	}
	/**
	 * Returns the caption of the photo
	 * @return Caption of the photo
	 */
	public String getCaption() {
		return this.caption;
	}
	/**
	 * Assigns a caption to the photo
	 * @param caption Caption for the photo
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}
	/**
	 * Returns the tags associated with the photo
	 * @return List of tags for the photo
	 */
	public List<Tag> getTags() {
		return this.tags;
	}
	/**
	 * Returns the thumbnail of the photo
	 * @return Thumbnail for the photo
	 */
	public Thumbnail getThumbnail() {return this.thumbnail;}
	/**
	 * Inserts a tag into the list of tags associated with the photo
	 * @param tag Tag to insert into list
	 */
	public void addTag(Tag tag) {
		this.tags.add(tag);
	}
	/**
	 * Removes a tag from list of tags associated with the photo
	 * @param tag Tag to remove from list
	 */
	public void deleteTag(Tag tag) {
		this.tags.remove(tag);
	} 
	/**
	 * Returns the capture date of the photo
	 * @return Capture date of photo
	 */
	public Calendar getDate() {
		return this.date;
	}
	/**
	 * Assigns a new capture date for the photo
	 * @param date Capture date for photo
	 */
	public void setDate(Calendar date) {
		this.date = date;
		this.date.set(Calendar.MILLISECOND, 0);
	}
	/**
	 * Equals method for Photo object
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof Photo)) {
			return false;
		}
		Photo other = (Photo)obj;
		return this.name.equals(other.getName());
	}
	/**
	 * Converts photo to a String by using its name
	 * @return name of string
	 */
	@Override
	public String toString() {
		return this.name;
	}
}
