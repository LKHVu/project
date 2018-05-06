package demo;

public class Item {
	private String barcode;
	private String name;
	private String quantity;
	private String shop;
	
	
	public Item() {
	}
	
	
	public Item(String barcode, String name, String quantity, String shop) {
		this.barcode = barcode;
		this.name = name;
		this.quantity = quantity;
		this.shop = shop;
	}
	
	
	public String getBarcode() {
		return barcode;
	}
	
	public String getName() {
		return name;
	}
	
	public String getQuantity() {
		return quantity;
	}
	
	public String getShop() {
		return shop;
	}
}
