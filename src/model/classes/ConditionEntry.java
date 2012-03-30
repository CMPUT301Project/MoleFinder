package model.classes;


public class ConditionEntry extends DatabaseEntry {
	public static final String DUMMY_NAME = "_dummy_";
	public static final int DUMMY_ID = -1;
	public static final int IDENTICAL = 111;
	public static final int DIFF_NAME = 222;
	public static final String DUMMY_IMAGE = "1";
	public static final int DIFF_COMMENT = 333;
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
	// constructor 3
	public ConditionEntry(ConditionEntry input) {
		this(input.getId(), input.getTag(), input.getImage());
	}
	
	public static ConditionEntry createDummyEntry() {
		return new ConditionEntry(DUMMY_ID, DUMMY_NAME, DUMMY_IMAGE);
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
	
	/** Compare two ConditionTag objects. Item ID is not considered.
	 * 
	 * @param other The ConditionTag to compare to.
	 * @return -1: Comparing a dummy object
	 * 			0: Tags are the same
	 * 			1: Tags have a different name
	 * 			2: Tags have the same name, different comment
	 */
	public int compareTo(ConditionEntry other) {
		// comparing to dummy
		if (this.getId() == DUMMY_ID || other.getId() == DUMMY_ID) {
			return DUMMY_ID;
		}
		// same name
		else if (this.getTag().equals(other.getTag())) {
			// identical 
			if (this.getComment().equals(other.getComment())) {
				return IDENTICAL;
			}
			// different comments
			else {
				return DIFF_COMMENT;
			}
		}
		// different name
		else {
			return DIFF_COMMENT;
		}
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
