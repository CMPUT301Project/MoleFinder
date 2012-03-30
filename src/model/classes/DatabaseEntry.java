package model.classes;

/** DatabaseEntry is an abstract class representing rows in the application database.
 * This abstraction allows different types of DatabaseEntry objects to be displayed in
 * a ListView or Spinner. 
 * 
 * @author mbessett
 *
 */
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
	
	/** Provide the main identifier for this object.
	 * 
	 * @return A String value describing this object.
	 */
	public abstract String getTitle();
	
	/** Provide secondary identification for this object.
	 * 
	 * @return A String value containing secondary descriptive information.
	 */
	public abstract String getDescription();
	
}
