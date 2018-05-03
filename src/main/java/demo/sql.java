package demo;
import java.sql.*;
import java.util.*;

public class sql {
	Connection c = null;
	Statement stmt = null;
		
	public sql(){
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:MyLittleShop");
			System.out.println("Connected to DB OK!");
			c.isClosed();
		}catch(Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	
	// // show all items
	public void list(List<Item> al) {
		try {
			this.stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM shop1");
			
			while(rs.next()) {
				String barcode = rs.getString("Barcode");
				String name = rs.getString("Name");
				Integer quantity = rs.getInt("Quantity");
				al.add(new Item(barcode, name, quantity));
				/*
				en.setBarcode(barcode);
				en.setName(name);
				en.setQuantity(quantity);
				al.add(en);
				*/
				System.out.println(barcode+"\t"+name+"\t"+quantity);
				//return en;
			}
		} catch(Exception e ) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	
	
	// update items
	public void update(String barcode, Integer qty) {
		try {
			this.stmt = c.createStatement();
			String query = "SELECT * FROM shop1 WHERE Barcode=\"" + barcode + "\"";
			//query+=barcode+"\"";
			ResultSet rs = stmt.executeQuery(query);
			Integer quantity = rs.getInt("Quantity");
			quantity-=qty;
			query = "UPDATE shop1 SET Quantity=\"" + Integer.toString(quantity) + "\" WHERE Barcode=\"" + barcode + "\"";
			stmt.executeQuery(query);
		} catch(Exception e) {
			
		}
	}
	
	//is exist
	public int isExist(String barcode) {
		try {
			this.stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM shop1 WHERE Barcode=\"" + barcode + "\"");
			
			if(!rs.next()) {
				return 0;
			}
			else {
				return 1;
			}
		
		} catch(Exception e ) {
			System.out.println("Error: " + e.getMessage());
			return 2;
		}
	}
	
	//insert
		public void insert(String barcode, String name, int quantity) {
			try {
				this.stmt = c.createStatement();
				stmt.executeQuery("INSERT INTO shop1 (Barcode,Name,Quantity) VALUES (\"" + barcode + "\", \"" + name + "\", \"" + quantity + "\")");
							
			} catch(Exception e ) {
				System.out.println("Error: " + e.getMessage());
			}
		}
	
	/*
	public void executeQuery(String query) {
		try {
			this.stmt = c.createStatement();
			stmt.executeQuery(query);
		} catch(Exception e) {
			
		}
	}
	
	*/
	public void closeConnection() {
		try {
			c.close();
		} catch(Exception e) {
			
		}
	}
	
	/*
	public static void main(String[] args) {
		//ArrayList<Entity> al=new ArrayList<Entity>();
		sql s = new sql();

		s.list();
		s.closeConnection();
		
		//String query = "INSERT INTO Shop1 VALUES(Wasser, 11052, 330)";
		
	}
	*/

}
