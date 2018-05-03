package demo;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import java.sql.*;
import java.util.*;

@RestController
public class InventoryController {
	
	//------------show inventory-------------//
	@RequestMapping(value = "/inventory", method = RequestMethod.GET)
	public List<Item> getInventory() {
		List<Item> al = new ArrayList<Item>();
		//Entity e = new Entity();
		sql s = new sql();
		s.list(al);
		s.closeConnection();
		return al;
	}
	
	//------------update inventory-------------//
	@RequestMapping(value = "/inventory/update", method = RequestMethod.PUT)
	public @ResponseBody String updateInventory(@RequestParam String barcode, @RequestParam Integer qty) {
		sql s = new sql();
		s.update(barcode, qty);
		s.list(new ArrayList<Item>());
		s.closeConnection();
		return "Updated";
	}
	
	//insert new item in inventory
	@RequestMapping(value = "/product", method = RequestMethod.POST)
	public ResponseEntity<Void> createItem(@RequestBody Item item, UriComponentsBuilder ucBuilder){
	    System.out.println("barcode: "+item.getBarcode());
	    System.out.println("name: "+item.getName());
	    System.out.println("quantity: "+item.getQuantity());
	    
	    sql s = new sql();
	    int i=s.isExist(item.getBarcode());
	    if(i==1){
	        System.out.println("An item with barcode "+item.getBarcode()+" already exist");
	        return new ResponseEntity<Void>(HttpStatus.CONFLICT);
	    }	
	    else if(i==2){
	        System.out.println("Unexpected error!");
	        return new ResponseEntity<Void>(HttpStatus.CONFLICT);
	    }
	    else {
	    s.insert(item.getBarcode(), item.getName(), item.getQuantity());
	  
	    HttpHeaders headers = new HttpHeaders();
	    headers.setLocation(ucBuilder.path("/inventory/{barcode}").buildAndExpand(item.getBarcode()).toUri());
	    return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	    }
	}
	
}
