package model.classes;


public class ConditionEntry extends DatabaseEntry {
	// database rows
	private String tag;
	private String comment;
	private String date;
	private String image;


	// constructor 1 (comment is optional)
	public ConditionEntry(int id, String tag, String image) {
		super(id);
		this.setTag(tag);
		this.setImage(image);
	}
	// constructor 2
	public ConditionEntry(int id, String tag, String image, String comment, String date) {
		this(id, tag, image);
		this.setComment(comment);
		this.setDate(date);
	}
	
	@Override
	public String toString() {
		return new String(getId() + " " + getTag() + 
				" " + getDate());
	}
	
	/** Primary descriptor of a ConditionEntry is the date.
	 * 
	 */
	@Override
	public String getTitle() {
		return getDate();
	}
	
	/** Secondary descriptor of a ConditionEntry is the associated tag. 
	 * 
	 */
	@Override
	public String getDescription() {
		return getTag();
	}
	
	// getters/setters
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getImage() {
		return image;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

}
