package demo;

public class Item {
	private String barcode;
	private String name;
	private Integer quantity;
	
	public Item(){
	}
	public Item(String barcode, String name, Integer quantity) {
		this.barcode = barcode;
		this.name = name;
		this.quantity = quantity;
	}
	
	
	public String getBarcode() {
		return barcode;
	}
	
	public String getName() {
		return name;
	}
	
	public Integer getQuantity() {
		return quantity;
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
