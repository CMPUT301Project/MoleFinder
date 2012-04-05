package model.classes;

/**
 * This the ConditionUser class used to store entries from the database into ConditionUser objects.
 * 
 * @author jletourn
 */

public class ConditionUser extends DatabaseEntry {

	private String userName;
	private String password;
	private String role;
	
	/** Explicit constructor.
	 * 
	 * @param id The RowID of this user
	 * @param password The user's password
	 * @param role Patient/Doctor
	 * @param userName The username
	 */
	public ConditionUser(int id, String password, String role, String userName) {
		this(id, password, role);
		this.setUserName(userName);
	}
	
	/** Minimal constructor.
	 * 
	 * @param id The RowID of this user
	 * @param password This user's password
	 * @param role Patient/Doctor
	 */
	public ConditionUser(int id, String password, String role) {
		super(id);
		this.setPassword(password);
		this.setRole(role);
	}
	
	/** Return a string uniquely identifying this user.
	 * 
	 */
	@Override
	public String toString() {
		return new String(getId() + " " + getUserName() + 
				" " + getPassword() + " " + getRole());
	}

	/** Primary descriptor of a User is username.
	 * 
	 */
	@Override
	public String getTitle() {
		return getUserName();
	}

	/** Secondary descriptor of a User is role.
	 * 
	 */
	@Override
	public String getDescription() {
		return getRole();
	}
	
	
	// getters/setters
	public void setUserName(String userName){
		this.userName = userName;
	}
	
	public void setPassword(String password){
		this.password = password;
	}
	
	public void setRole(String role){
		this.role = role;
	}
	
	public String getUserName(){
		return userName;
	}
	
	public String getPassword(){
		return password;
	}
	
	public String getRole(){
		return role;
	}
}
