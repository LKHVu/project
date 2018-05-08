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
	
	// show item shop1
	public void list1(List<Item> al) {
		try {
			this.stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM shop1");
			
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
	
	//show item shop2
	public void list2(List<Item> al) {
		try {
			this.stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM shop2");
			
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
	
	//show item shop3
		public void list3(List<Item> al) {
			try {
				this.stmt = c.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM shop3");
				
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
		
	
	// show one item of shop 1
	public void listOneItem1(String barcode, List<Item> al) {
		try {
			this.stmt = c.createStatement();
			String query = "SELECT * FROM shop1 WHERE Barcode=\"" + barcode + "\""; 
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
	
	// show one item of shop 2
		public void listOneItem2(String barcode, List<Item> al) {
			try {
				this.stmt = c.createStatement();
				String query = "SELECT * FROM shop2 WHERE Barcode=\"" + barcode + "\""; 
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
		
		// show one item of shop 3
				public void listOneItem3(String barcode, List<Item> al) {
					try {
						this.stmt = c.createStatement();
						String query = "SELECT * FROM shop3 WHERE Barcode=\"" + barcode + "\""; 
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
				
				//update shop1
				public void updateShop1(String type, String quantity, String barcode) {
					
					try {
						this.stmt = c.createStatement();
						String query = "SELECT Quantity FROM shop1 WHERE Barcode=\"" +barcode+ "\"";
						ResultSet rs = stmt.executeQuery(query);
						
						if (type.equals("increase")) {
							quantity = Integer.toString((Integer.parseInt(rs.getString("Quantity")) + Integer.parseInt(quantity)));
						} else {
							quantity = Integer.toString((Integer.parseInt(rs.getString("Quantity")) - Integer.parseInt(quantity)));
						}
						
						//query = "UPDATE " +token+ " SET Quantity=\"" +quantity + "\"";
						query = "UPDATE shop1 SET Quantity=\"" +quantity+ "\" WHERE Barcode=\"" +barcode+ "\"";
						stmt.executeQuery(query);
					} catch (Exception e) {
						System.out.println("Error: " + e.getMessage());
					}
				}
				
				//update shop2
				public void updateShop2(String type, String quantity, String barcode) {
					
					try {
						this.stmt = c.createStatement();
						String query = "SELECT Quantity FROM shop2 WHERE Barcode=\"" +barcode+ "\"";
						ResultSet rs = stmt.executeQuery(query);
						
						if (type.equals("increase")) {
							quantity = Integer.toString((Integer.parseInt(rs.getString("Quantity")) + Integer.parseInt(quantity)));
						} else {
							quantity = Integer.toString((Integer.parseInt(rs.getString("Quantity")) - Integer.parseInt(quantity)));
						}
						
						//query = "UPDATE " +token+ " SET Quantity=\"" +quantity + "\"";
						query = "UPDATE shop2 SET Quantity=\"" +quantity+ "\" WHERE Barcode=\"" +barcode+ "\"";
						stmt.executeQuery(query);
					} catch (Exception e) {
						System.out.println("Error: " + e.getMessage());
					}
				}
				
				//update shop3
				public void updateShop3(String type, String quantity, String barcode) {
					
					try {
						this.stmt = c.createStatement();
						String query = "SELECT Quantity FROM shop3 WHERE Barcode=\"" +barcode+ "\"";
						ResultSet rs = stmt.executeQuery(query);
						
						if (type.equals("increase")) {
							quantity = Integer.toString((Integer.parseInt(rs.getString("Quantity")) + Integer.parseInt(quantity)));
						} else {
							quantity = Integer.toString((Integer.parseInt(rs.getString("Quantity")) - Integer.parseInt(quantity)));
						}
						
						//query = "UPDATE " +token+ " SET Quantity=\"" +quantity + "\"";
						query = "UPDATE shop3 SET Quantity=\"" +quantity+ "\" WHERE Barcode=\"" +barcode+ "\"";
						stmt.executeQuery(query);
					} catch (Exception e) {
						System.out.println("Error: " + e.getMessage());
					}
				}
				
				
		//update admin
				public void updateAdmin(Item item, String barcode) {
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
	
	 //delete item shop1
	public boolean delete1(String barcode) {
		boolean i = isNull1(barcode);
		if (i) {
			return false;
		} else {
			try {
				this.stmt = c.createStatement();
				stmt.executeUpdate("DELETE FROM shop1 WHERE Barcode=\""+barcode+"\"");
				return true;
			} catch(Exception e) {
				System.out.println("Error: " + e.getMessage());
				return false;
			}
		}
	}
	
	//delete item shop2
	public boolean delete2(String barcode) {
		boolean i = isNull2(barcode);
		if (i) {
			return false;
		} else {
			try {
				this.stmt = c.createStatement();
				stmt.executeUpdate("DELETE FROM shop2 WHERE Barcode=\""+barcode+"\"");
				return true;
			} catch(Exception e) {
				System.out.println("Error: " + e.getMessage());
				return false;
			}
		}
	}
	
	
	//delete item shop3
		public boolean delete3(String barcode) {
			boolean i = isNull3(barcode);
			if (i) {
				return false;
			} else {
				try {
					this.stmt = c.createStatement();
					stmt.executeUpdate("DELETE FROM shop3 WHERE Barcode=\""+barcode+"\"");
					return true;
				} catch(Exception e) {
					System.out.println("Error: " + e.getMessage());
					return false;
				}
			}
		}
		
	// create item shop1
	public boolean create1(Item item) {
		boolean i = isNull1(item.getBarcode());
		if (i) {
			try {
				this.stmt = c.createStatement();
				stmt.executeUpdate("INSERT INTO shop1 VALUES (\""+item.getBarcode()+"\", \""+item.getName()+"\", \""+item.getQuantity()+"\", \""+item.getShop()+"\")");
				return true;
			} catch(Exception e) {
				System.out.println("Error: " + e.getMessage());
				return false;
			} 
		}else {
			return false;
		}
	}
	
	// create item shop2
		public boolean create2(Item item) {
			boolean i = isNull2(item.getBarcode());
			if (i) {
				try {
					this.stmt = c.createStatement();
					stmt.executeUpdate("INSERT INTO shop2 VALUES (\""+item.getBarcode()+"\", \""+item.getName()+"\", \""+item.getQuantity()+"\", \""+item.getShop()+"\")");
					return true;
				} catch(Exception e) {
					System.out.println("Error: " + e.getMessage());
					return false;
				} 
			}else {
				return false;
			}
		}
		
		// create item shop3
				public boolean create3(Item item) {
					boolean i = isNull3(item.getBarcode());
					if (i) {
						try {
							this.stmt = c.createStatement();
							stmt.executeUpdate("INSERT INTO shop3 VALUES (\""+item.getBarcode()+"\", \""+item.getName()+"\", \""+item.getQuantity()+"\", \""+item.getShop()+"\")");
							return true;
						} catch(Exception e) {
							System.out.println("Error: " + e.getMessage());
							return false;
						} 
					}else {
						return false;
					}
				}
	
	
		
		
	//}
	
	//check if shop 1 has that barcode
	boolean isNull1(String barcode) {
		try {
			this.stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM shop1 WHERE Barcode=\""+barcode+"\"");
			String str = rs.getString("Barcode");
			return false;
		} catch (Exception e) {
			return true;
		}
	}
	//check if shop 2 has that barcode
		boolean isNull2(String barcode) {
			try {
				this.stmt = c.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM shop2 WHERE Barcode=\""+barcode+"\"");
				String str = rs.getString("Barcode");
				return false;
			} catch (Exception e) {
				return true;
			}
		}
		
		//check if shop 3 has that barcode
				boolean isNull3(String barcode) {
					try {
						this.stmt = c.createStatement();
						ResultSet rs = stmt.executeQuery("SELECT * FROM shop3 WHERE Barcode=\""+barcode+"\"");
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
