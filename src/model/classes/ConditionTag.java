package model.classes;

/**
 * This data class converts Cursor objects returned from the database
 * into ConditionTag objects.
 * 
 * @author mbessett
 */

public class ConditionTag extends DatabaseEntry {
	public static final String DUMMY_NAME = "_dummy_";
	public static final int DUMMY_ID = -1;
	public static final int IDENTICAL = 111;
	public static final int DIFF_NAME = 222;
	public static final int DIFF_COMMENT = 333;
	
	private String name;
	private String comment;

	/** Constructor 1. Tag without a comment.
	 *  
	 * @param id The RowID of this tag
	 * @param name The name of this tag
	 */
	public ConditionTag(int id, String name) {
		super(id);
		this.setName(name);
	}
	/** Constructor 2. Comment constructor
	 * 
	 * @param id The RowID of this tag
	 * @param name The name of this tag
	 * @param comment The commments associated with this tag
	 */
	public ConditionTag(int id, String name, String comment) {
		this(id, name);
		this.setComment(comment);
	}
	/** Copy Constructor.
	 * 
	 * @param input The ConditionTag to duplicate
	 */
	public ConditionTag(ConditionTag input) {
		this(input.getId(), input.getName(), input.getComment());
	}
	
	/**  Return a fake (placeholder) ConditionTag object.
	 * 
	 * @return The placeholder tag.
	 */
	public static ConditionTag createDummyTag() {
		return new ConditionTag(DUMMY_ID, DUMMY_NAME);
	}
	
	/** Compare two ConditionTag objects. Item ID is not considered.
	 * 
	 * @param other The ConditionTag to compare to.
	 * @return Tag comparison result code.
	 */
	public int compareTo(ConditionTag other) {
		// comparing to dummy
		if (this.getId() == DUMMY_ID || other.getId() == DUMMY_ID) {
			return DUMMY_ID;
		}
		// same name
		else if (this.getName().equals(other.getName())) {
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
			return DIFF_NAME;
		}
	}
	
	/** Return the name of this tag.
	 * 
	 */
	@Override
	public String toString() {
		return getName();
	}
	
	/** Primary descriptor of a ConditionTag is the name of the tag.
	 * 
	 */
	@Override
	public String getTitle() {
		return getName();
	}
	
	/** Secondary descriptor of a ConditionTag is the comment.
	 * 
	 */
	@Override
	public String getDescription() {
		return getComment();
	}

	
	// getters/setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
