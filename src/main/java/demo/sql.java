package demo;
import java.sql.*;
import java.util.*;

public class sql {
	Connection c = null;
	Statement stmt = null;
	
	// constructor
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
	
	
	// set a query
	String setQuery(String token) {
		String query;
		if (token.equals("admin")) {
			query = "SELECT * FROM shop1" + 
							" union" + 
							" SELECT * FROM shop2" + 
							" union" + 
							" SELECT * FROM shop3";
		} else if (token.equals("shop1")) {
			query = "SELECT * FROM shop1";
		} else if (token.equals("shop2")) {
			query = "SELECT * FROM shop2";
		} else {
			query = "SELECT * FROM shop3";
		}
		return query;
	}
	
	
	// show all items
	public void list(List<Item> al, String token) {
		try {
			String query = setQuery(token);
			
			
			this.stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				String barcode = rs.getString("Barcode");
				String name = rs.getString("Name");
				String quantity = rs.getString("Quantity");
				String shop = rs.getString("Shop");
				al.add(new Item(barcode, name, quantity, shop));
				
				System.out.println(barcode+"\t"+name+"\t"+quantity);
				
			}
		} catch(Exception e ) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	
	
	// show one item
	public List listOneItem(String barcode, String token) {
		try {
			this.stmt = c.createStatement();
			List<Item> al = new ArrayList<Item>();
			String query = setQuery(token);
			query = "SELECT * FROM ("+query+") WHERE Barcode=\"" + barcode + "\"";
			//System.out.println(query);
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				String name = rs.getString("Name");
				String quantity = rs.getString("Quantity");
				String shop = rs.getString("Shop");
				al.add(new Item(barcode, name, quantity, shop));
			}
				
			return al;
		} catch(Exception e) {
			System.out.println("Error: " + e.getMessage());
			return null;
		}
		
	}

	
	// update from admin
	public void updateAdmin(Item item, String token, String barcode) {
		String query1, query2, query3;
		query1 = "UPDATE shop1 SET ";
		query2 = "UPDATE shop2 SET ";
		query3 = "UPDATE shop3 SET ";
		if (item.getBarcode() != null) {
			barcode = item.getBarcode();
			query1 += "Barcode=\"" +barcode+ "\", ";
			query2 += "Barcode=\"" +barcode+ "\", ";
			query3 += "Barcode=\"" +barcode+ "\", ";
		}
		if (item.getName() != null) {
			query1 += "Name=\"" +item.getName()+ "\" ";
			query2 += "Name=\"" +item.getName()+ "\" ";
			query3 += "Name=\"" +item.getName()+ "\" ";
		}
		query1 += "WHERE Barcode=\"" +barcode+ "\"";
		query2 += "WHERE Barcode=\"" +barcode+ "\"";
		query3 += "WHERE Barcode=\"" +barcode+ "\"";
		
		System.out.println(query1);
		System.out.println(query2);
		System.out.println(query3);
		try {
			this.stmt = c.createStatement();
			stmt.executeUpdate(query1);
			stmt.executeUpdate(query2);
			stmt.executeUpdate(query3);
		} catch(Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	
	// update from shops
	public void updateShop(String type, String quantity, String token, String barcode) {
		
		try {
			this.stmt = c.createStatement();
			String query = "SELECT Quantity FROM " +token+ " WHERE Barcode=\"" +barcode+ "\"";
			ResultSet rs = stmt.executeQuery(query);
			
			if (type.equals("increase")) {
				quantity = Integer.toString((Integer.parseInt(rs.getString("Quantity")) + Integer.parseInt(quantity)));
			} else {
				quantity = Integer.toString((Integer.parseInt(rs.getString("Quantity")) - Integer.parseInt(quantity)));
			}
			
			//query = "UPDATE " +token+ " SET Quantity=\"" +quantity + "\"";
			query = "UPDATE " +token+ " SET Quantity=\"" +quantity+ "\" WHERE Barcode=\"" +barcode+ "\"";
			stmt.executeQuery(query);
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	
	// delete item
	public boolean delete(String barcode) {
		boolean i = isNull(barcode, null);
		if (i) {
			return false;
		} else {
			try {
				this.stmt = c.createStatement();
				String query = "DELETE FROM shop1 WHERE Barcode=\""+barcode+"\"";
				query+="; DELETE FROM shop2 WHERE Barcode=\"" +barcode+"\"";
				query+="; DELETE FROM shop3 WHERE Barcode=\"" +barcode+"\"";
				//System.out.println(query);
				stmt.executeUpdate(query);
				
				return true;
			} catch(Exception e) {
				System.out.println("Error: " + e.getMessage());
				return false;
			}
		}
	}
	
	
	// create item
	public boolean create(Item item) {
		//System.out.println(isNull(item.getBarcode()));
		boolean i = isNull(item.getBarcode(), item.getShop());
		
		if (i) {
			try {
				this.stmt = c.createStatement();
				String query = "INSERT INTO shop"+item.getShop()
								+" VALUES (\""+item.getBarcode() + "\", \""
								+item.getName() + "\", \""
								+item.getQuantity() + "\", \""
								+item.getShop()+"\")";
				System.out.println(query);
				stmt.executeUpdate(query);
				
				return true;
			} catch(Exception e) {
				System.out.println("Error: " + e.getMessage());
				return false;
			} 
		}else {
			return false;
		}
		
		
	}
	
	// check if a product is in inventory
	boolean isNull(String barcode, String shop) {
		try {
			this.stmt = c.createStatement();
			if (shop != null) {
				String query = "SELECT * FROM shop"+shop+" WHERE Barcode=\""+barcode+"\"";
				ResultSet rs = stmt.executeQuery(query);
				String str = rs.getString("Barcode");
			} else {
				String query = setQuery("admin");
				query = "SELECT * FROM("+query+") WHERE Barcode=\"" + barcode + "\"";
				ResultSet rs = stmt.executeQuery(query);
				String str = rs.getString("Barcode");
			}
			
			//String str = rs.getString("Barcode");
			return false;
		} catch (Exception e) {
			return true;
		}
	}
	
	// search and return token
	String searchToken(Account account) throws Exception {
	
		String query = "SELECT Token FROM account WHERE Name=\""
							+account.getName()+"\" AND Password=\""
							+account.getPassword()+"\"";
			
		this.stmt = c.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		String token = rs.getString("Token");
		return token;
	}
	
	// check if token is correct
	boolean checkToken(String token) {
		try {
			String query = "SELECT Token FROM account WHERE Token=\""
					+token+"\"";
			
			this.stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			token = rs.getString("Token");
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	
	public void closeConnection() {
		try {
			c.close();
		} catch(Exception e) {
			
		}
	}
	
}
