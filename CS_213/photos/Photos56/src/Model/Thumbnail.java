package Model;

import java.io.Serializable;
import java.util.List;

import javafx.scene.image.*;

/**
 * The Thumbnail class represents a thumbnail image for a photo
 * @author Nicolas Carchio
 * @author Adam Romano
 */
public class Thumbnail implements Serializable{
	/**
	 * Serialization class version
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Width of the thumbnail
	 */
	private int width;
	/**
	 * Height of the thumbnail
	 */
	private int height; 
	/**
	 * Pixels for the thumbnail
	 */
	private int[][] pixels;
	/**
	 * Creates an instance of Thumbnail based off an image
	 * @param image Image to create thumbnail from
	 */
	public Thumbnail(Image image) {
		this.width = (int) image.getWidth();
		this.height = (int) image.getHeight();
		this.pixels = new int[this.width][this.height];
		PixelReader pr = image.getPixelReader();
		for(int i = 0; i < this.width; i++) {
			for(int j = 0; j < this.height; j++) {
				this.pixels[i][j] = pr.getArgb(i, j);
			}
		}
	}
	/**
	 * Returns the width of the thumbnail
	 * @return Width of thumbnail
	 */
	public int getWidth() {
		return this.width;
	}
	/**
	 * Returns the height of the thumbnail
	 * @return Height of thumbnail
	 */
	public int getHeight() {
		return this.height;
	}
	/**
	 * Returns the thumbnail's pixels
	 * @return Thumbnail's pixels
	 */
	public int[][] getPixels() {
		return this.pixels;
	}
	/**
	 * Returns the original image object
	 * @return Original image
	 */
	public Image getImage() {
		WritableImage img = new WritableImage(this.width, this.height);
		PixelWriter pw = img.getPixelWriter();
		for(int i = 0; i < this.width; i++) {
			for(int j = 0; j < this.height; j++) {
				pw.setArgb(i, j, this.pixels[i][j]);
			}
		}
		return img;
	}
	/**
	 * Equals method for Thumbnail object
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof Thumbnail)) {
			return false;
		}
		Thumbnail other = (Thumbnail)obj;
		if(this.width != other.getWidth() || this.height != other.getHeight()) {
			return false;
		}
		for(int i = 0; i < this.width; i++) {
			for(int j = 0; j < this.height; j++) {
				if(this.pixels[i][j] != other.getPixels()[i][j]) {
					return false;
				}
			}
		}
		return true;
	}
}
