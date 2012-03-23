package model.classes;

public abstract class DatabaseEntry {
	// row id this entry
	private int id;
	
	// constructor
	public DatabaseEntry(int id) {
		this.id = id;		
	}
	
	// return a String representation of the entry
	public abstract String toString();

	// getters/setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public abstract String getTitle();
	public abstract String getDescription();
}
