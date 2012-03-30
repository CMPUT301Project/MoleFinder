package model.classes;

public class ConditionTag extends DatabaseEntry {
	public static final String DUMMY_NAME = "_dummy_";
	public static final int DUMMY_ID = -1;
	public static final int IDENTICAL = 111;
	public static final int DIFF_NAME = 222;
	public static final int DIFF_COMMENT = 333;
	
	private String name, comment;

	// constructor 1 (comment optional)
	public ConditionTag(int id, String name) {
		super(id);
		this.setName(name);
	}
	// constructor 2
	public ConditionTag(int id, String name, String comment) {
		this(id, name);
		this.setComment(comment);
	}
	// constructor 3
	public ConditionTag(ConditionTag input) {
		this(input.getId(), input.getName(), input.getComment());
	}
	
	public static ConditionTag createDummyTag() {
		return new ConditionTag(DUMMY_ID, DUMMY_NAME);
	}
	
	/** Compare two ConditionTag objects. Item ID is not considered.
	 * 
	 * @param other The ConditionTag to compare to.
	 * @return -1: Comparing a dummy object
	 * 			0: Tags are the same
	 * 			1: Tags have a different name
	 * 			2: Tags have the same name, different comment
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
