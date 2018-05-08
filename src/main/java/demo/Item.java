package demo;

public class Item {
	private String barcode;
	private String name;
	private String quantity;
	private String shop;
	
	
	public Item(){
	}
	public Item(String barcode, String name, String quantity, String shop) {
		this.barcode = barcode;
		this.name = name;
		this.quantity = quantity;
		this.shop=shop;
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
	
	/*
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	*/
}
