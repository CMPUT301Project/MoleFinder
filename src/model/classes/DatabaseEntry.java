package model.classes;

/** This abstract class represents rows in the application database.
 * This abstraction allows different types of DatabaseEntry objects to be displayed in
 * a ListView or Spinner. 
 * 
 * @author mbessett
 */

public abstract class DatabaseEntry {
	// row id this entry
	private int id;
	 
	/** Basic Constructor.
	 * 
	 * @param id The RowID of this entry
	 */
	public DatabaseEntry(int id) {
		this.id = id;		
	}
	
	/** Return the string representation of this object.
	 * 
	 */
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
