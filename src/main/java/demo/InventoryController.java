package demo;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import java.sql.*;
import java.util.*;

@RestController
public class InventoryController {
	
	//------------show inventory-------------//
	@CrossOrigin(origins = "http://localhost:9000")
	@RequestMapping(value = "/product", method = RequestMethod.GET)
	public List<Item> getInventory() {
		List<Item> al = new ArrayList<Item>();
		//Entity e = new Entity();
		sql s = new sql();
		s.list(al);
		s.closeConnection();
		return al;
	}
	
	@CrossOrigin(origins = "http://localhost:9000")
	@RequestMapping(value = "/product/{barcode}", method = RequestMethod.GET)
	public Item getItem(@PathVariable("barcode") String barcode) {
		sql s = new sql();
		Item item = s.listOneItem(barcode);
		s.closeConnection();
		return item;
	}
	
	@CrossOrigin(origins = "http://localhost:9000")
	@RequestMapping(value = "/product/{barcode}", method = RequestMethod.PUT)
	public void updateInventory(@PathVariable("barcode") String barcode, @RequestBody Item item) {
		//Item currentItem = getItem(barcode);
		sql s = new sql();
		s.update(getItem(barcode), item);
		s.closeConnection();
	}
	
	@CrossOrigin(origins = "http://localhost:9000")
	@RequestMapping(value = "/product/{barcode}", method = RequestMethod.DELETE)
	public @ResponseBody String delete(@PathVariable("barcode") String barcode) {
		sql s = new sql();
		boolean i = s.delete(barcode);
		if (i) {
			s.closeConnection();
			return "Item deleted!";
			
		} else {
			s.closeConnection();
			return "No barcode matched!";
		}
	}
	
	@CrossOrigin(origins = "http://localhost:9000")
	@RequestMapping(value = "/product", method = RequestMethod.POST)
	public @ResponseBody String create(@RequestBody Item item) {
		
		sql s = new sql();
		if (s.create(item)) {
			s.closeConnection();
			return "Item created!";
		} else {
			s.closeConnection();
			return "Item duplicated!";
		}
	}
}
