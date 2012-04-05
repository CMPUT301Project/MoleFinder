package model.classes;

/**
 * This the ConditionEntry class used to store entries from the database into ConditionEntry objects.
 * 
 * @author jletourn
 */

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


	/** Constructor 1. Entry with no comment.
	 * 
	 * @param id The RowID of the entry
	 * @param tag The tag grouping of this image
	 * @param image The image name
	 */
	public ConditionEntry(int id, String tag, String image) {
		super(id);
		this.setTag(tag);
		this.setImage(image);
	}
	/** Constructor 2. Explicit constructor.
	 * 
	 * @param id The RowID of the entry
	 * @param tag The tag grouping of the image
	 * @param image The image name
	 * @param comment The image comments
	 * @param date The date in yyyy-MM-dd format
	 */
	public ConditionEntry(int id, String tag, String image, String comment, String date) {
		this(id, tag, image);
		this.setComment(comment);
		this.setDate(date);
	}
	/** Copy constructor
	 * 
	 * @param input The ConditionEntry to duplicate
	 */
	public ConditionEntry(ConditionEntry input) {
		this(input.getId(), input.getTag(), input.getImage());
	}
	
	/** Create a fake (placeholder) ConditionEntry
	 * 
	 * @return The placeholder object.
	 */
	public static ConditionEntry createDummyEntry() {
		return new ConditionEntry(DUMMY_ID, DUMMY_NAME, DUMMY_IMAGE);
	}
	
	/** Return a String uniquely identifying this entry.
	 * 
	 */
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
	
	/** Compare two ConditionEntry objects. Item ID is not considered.
	 * 
	 * @param other The ConditionEntry to compare to.
	 * @return The result code of the comparison.
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
