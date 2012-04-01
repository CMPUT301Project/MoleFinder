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
	
	//constructor 1
	public ConditionUser(int id, String password, String role, String userName) {
		this(id, password, role);
		this.setUserName(userName);
	}
	
	//constructor 2
	public ConditionUser(int id, String password, String role) {
		super(id);
		this.setPassword(password);
		this.setRole(role);
	}
	
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

	@Override
	public String toString() {
		return new String(getId() + " " + getUserName() + 
				" " + getPassword() + " " + getRole());
	}

	@Override
	public String getTitle() {
		return getUserName();
	}

	@Override
	public String getDescription() {
		return getRole();
	}

}
