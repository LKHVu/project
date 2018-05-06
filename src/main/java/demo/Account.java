package demo;

public class Account {
	private String name;
	private String password;
	
	public Account(){
	}
	
	public Account(String name, String password) {
		this.name = name;
		this.password = password;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPassword() {
		return password;
	}
}
