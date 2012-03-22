package model.classes;

public class ConditionTag extends DatabaseEntry {
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
	
	@Override
	public String toString() {
		return new String(getId() + " " + getName());
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
