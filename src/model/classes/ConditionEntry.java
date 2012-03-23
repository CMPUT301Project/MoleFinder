package model.classes;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;


public class ConditionEntry extends DatabaseEntry {
	// database rows
	private String tag, comment, date;
	private Bitmap image;

	// constructor 1 (comment is optional)
	public ConditionEntry(int id, String tag, byte[] image) {
		super(id);
		this.setTag(tag);
		this.image = BitmapFactory.decodeByteArray(image, 0, image.length); 
	}
	// constructor 2
	public ConditionEntry(int id, String tag, byte[] image, String comment, String date) {
		this(id, tag, image);
		this.setComment(comment);
		this.setDate(date);
	}

	// return the image as a byte array, use for saving entry to DB
	public byte[] getBitmapAsByteArray() { 
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); 
		this.image.compress(CompressFormat.PNG, 0, outputStream); 
		return outputStream.toByteArray(); 
	} 
	
	@Override
	public String toString() {
		return new String(getId() + " " + getTag() + 
				" " + getDate());
	}
	
	// getters/setters
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
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	@Override
	public String getTitle() {
		return getDate();
	}
	@Override
	public String getDescription() {
		return getTag();
	}
}
