package Model;

import java.io.Serializable;

/**
 * The class Tag represents a tag for a photo
 * @author Nicolas Carchio
 * @author Adam Romano
 */
public class Tag implements Serializable{
	/**
	 * Serialization class version
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Name of the tag
	 */
	private String name;
	/**
	 * Value of the tag
	 */
	private String value;
	/**
	 * Creates an instance of Tag with specified tag name and tag value
	 * @param name
	 * @param value
	 */
	public Tag(String name, String value) {
		this.name = name;
		this.value = value;
	}
	/**
	 * Returns the name of the tag
	 * @return Name of tag
	 */
	public String getName() {
		return this.name;
	}
	/**
	 * Returns the value of the tag
	 * @return Value of tag
	 */
	public String getValue() {
		return this.value;
	}
	/**
	 * Returns a string representation of the tag
	 */
	@Override
	public String toString() {
		return this.name + ":" + this.value;
	}
	/**
	 * Equals method for Tag object
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof Tag)) {
			return false;
		}
		Tag other = (Tag)obj;
		return (this.name.equals(other.getName())) && (this.value.equals(other.getValue()));
	}
}
