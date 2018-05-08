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
	
	// show item
	public void list(List<Item> al, int s) {
		try {
			this.stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM shop" + s);
			
			while(rs.next()) {
				String barcode = rs.getString("Barcode");
				String name = rs.getString("Name");
				String quantity = rs.getString("Quantity");
				String shop = rs.getString("Shop");
				al.add(new Item(barcode, name, quantity,shop));
				
				System.out.println(barcode+"\t"+name+"\t"+quantity+"\t"+shop);
				
			}
		} catch(Exception e ) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	// show one item 
	public void listOneItem(String barcode, List<Item> al, int s) {
		try {
			this.stmt = c.createStatement();
			String query = "SELECT * FROM shop" + s + " WHERE Barcode=\"" + barcode + "\""; 
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				String name = rs.getString("Name");
				String quantity = rs.getString("Quantity");
				String shop = rs.getString("Shop");
				al.add(new Item(barcode, name, quantity, shop));
			}
		} catch(Exception e) {
			System.out.println("Error: " + e.getMessage());
		}	
	}
	
				
				//update shop
				public void updateShop(String type, String quantity, String barcode, int s) {
					
					try {
						this.stmt = c.createStatement();
						String query = "SELECT Quantity FROM shop" + s + " WHERE Barcode=\"" +barcode+ "\"";
						ResultSet rs = stmt.executeQuery(query);
						
						if (type.equals("increase")) {
							quantity = Integer.toString((Integer.parseInt(rs.getString("Quantity")) + Integer.parseInt(quantity)));
						} else {
							quantity = Integer.toString((Integer.parseInt(rs.getString("Quantity")) - Integer.parseInt(quantity)));
						}
						
						//query = "UPDATE " +token+ " SET Quantity=\"" +quantity + "\"";
						query = "UPDATE shop" + s + " SET Quantity=\"" +quantity+ "\" WHERE Barcode=\"" +barcode+ "\"";
						stmt.executeQuery(query);
					} catch (Exception e) {
						System.out.println("Error: " + e.getMessage());
					}
				}
				
				
		//update admin
				public boolean updateAdmin(Item item, String barcode) {
					String query, query1, query2, query3, shop = item.getShop();
					query  = "UPDATE shop";
					query1 = "UPDATE shop1 SET ";
					query2 = "UPDATE shop2 SET ";
					query3 = "UPDATE shop3 SET ";
					
					if (shop == null) {
						try {
							boolean x = false;
							if (item.getBarcode() != null) {
								query1 += "Barcode=\"" +item.getBarcode()+ "\"";
								query2 += "Barcode=\"" +item.getBarcode()+ "\"";
								query3 += "Barcode=\"" +item.getBarcode()+ "\"";
								x = true;
							}
							if (item.getName() != null) {
								if (x) {
									query1 += ", Name=\"" +item.getName()+ "\"";
									query2 += ", Name=\"" +item.getName()+ "\"";
									query3 += ", Name=\"" +item.getName()+ "\"";
								} else {
									query1 += "Name=\"" +item.getName()+ "\"";
									query2 += "Name=\"" +item.getName()+ "\"";
									query3 += "Name=\"" +item.getName()+ "\"";
									x = true;
								}
							}
							if (item.getQuantity() != null) {
								if (x) {
									query1 += ", Quantity=\"" +item.getQuantity()+ "\"";
									query2 += ", Quantity=\"" +item.getQuantity()+ "\"";
									query3 += ", Quantity=\"" +item.getQuantity()+ "\"";
								} else {
									query1 += "Quantity=\"" +item.getQuantity()+ "\"";
									query2 += "Quantity=\"" +item.getQuantity()+ "\"";
									query3 += "Quantity=\"" +item.getQuantity()+ "\"";
								}
								
							}
							query1 += " WHERE Barcode=\"" +barcode+ "\"";
							query2 += " WHERE Barcode=\"" +barcode+ "\"";
							query3 += " WHERE Barcode=\"" +barcode+ "\"";
							
							this.stmt = c.createStatement();
							stmt.executeUpdate(query1);
							stmt.executeUpdate(query2);
							stmt.executeUpdate(query3);
							
						} catch(Exception e) {
							System.out.println("Error: " + e.getMessage());
						}
						
						return true;
						
					} else if (shop.equals("1") || shop.equals("2") || shop.equals("3")){
						try {
							boolean x = false;
							query += (item.getShop()+ " SET ");
							
							if (item.getBarcode() != null) {
								query += "Barcode=\"" +item.getBarcode()+ "\"";
								x = true;
							}
							if (item.getName() != null) {
								if (x) {
									query += ", Name=\"" +item.getName()+ "\"";
								} else {
									query += "Name=\"" +item.getName()+ "\"";
									x = true;
								}
							}
							if (item.getQuantity() != null) {
								if (x) {
									query += ", Quantity=\"" + item.getQuantity()+ "\"";
								} else {
									query += "Quantity=\"" +item.getQuantity()+ "\"";
								}
							}
							query += " WHERE Barcode=\"" +barcode+ "\"";
							this.stmt = c.createStatement();
							stmt.executeUpdate(query);
						} catch (Exception e) {
							
						}
						
						return true;
					} else {
						
						return false;
					}
					
				}
	
	 //delete item
	public boolean delete(String barcode, int s) {
		boolean i = isNull(barcode, s);
		if (i) {
			return false;
		} else {
			try {
				this.stmt = c.createStatement();
				stmt.executeUpdate("DELETE FROM shop" + s + " WHERE Barcode=\""+barcode+"\"");
				return true;
			} catch(Exception e) {
				System.out.println("Error: " + e.getMessage());
				return false;
			}
		}
	}
	
		
	// create item 
	public boolean create(Item item, int s) {
		boolean i = isNull(item.getBarcode(), s);
		if (i) {
			try {
				this.stmt = c.createStatement();
				stmt.executeUpdate("INSERT INTO shop" + s + " VALUES (\""+item.getBarcode()+"\", \""+item.getName()+"\", \""+item.getQuantity()+"\", \""+item.getShop()+"\")");
				return true;
			} catch(Exception e) {
				System.out.println("Error: " + e.getMessage());
				return false;
			} 
		}else {
			return false;
		}
	}
	
	
	
	//check if shop has that barcode
	boolean isNull(String barcode, int s) {
		try {
			this.stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM shop" + s + " WHERE Barcode=\""+barcode+"\"");
			String str = rs.getString("Barcode");
			return false;
		} catch (Exception e) {
			return true;
		}
	}
	
	
	
	public void closeConnection() {
		try {
			c.close();
		} catch(Exception e) {
			
		}
	}
	
}
