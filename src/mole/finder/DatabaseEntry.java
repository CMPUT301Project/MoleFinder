package mole.finder;

public abstract class DatabaseEntry {
	// row id this entry
	private int id;
	
	// constructor
	public DatabaseEntry(int id) {
		this.id = id;		
	}

	// getters/setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
