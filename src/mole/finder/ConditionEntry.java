package mole.finder;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;


public class ConditionEntry extends DatabaseEntry {
	// database rows
	private String tag, comment;
	private Bitmap image;

	// constructor 1 (comment is optional)
	public ConditionEntry(int id, String tag, byte[] image) {
		super(id);
		this.setTag(tag);
		this.image = BitmapFactory.decodeByteArray(image, 0, image.length); 
	}
	// constructor 2
	public ConditionEntry(int id, String tag, byte[] image, String comment) {
		this(id, tag, image);
		this.setComment(comment);
	}

	// return the image as a byte array, use for saving entry to DB
	public byte[] getBitmapAsByteArray() { 
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); 
		this.image.compress(CompressFormat.PNG, 0, outputStream); 
		return outputStream.toByteArray(); 
	} 
	
	// getters/setters, changing the image doesn't make any
	// sense => only need to set on tag and comment
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Bitmap getImage() {
		return this.image;
	}
}
