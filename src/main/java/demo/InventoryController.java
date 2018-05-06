package demo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@CrossOrigin(origins="http://localhost:8080/product")
@RestController
@RequestMapping("/product")
public class InventoryController {
	
	//------------show inventory-------------//
	//@RequestMapping(value = "/product", method = RequestMethod.GET)
	@GetMapping
	public List<?> getInventory(@RequestParam("token") String token) {
		sql s = new sql();
		if (!token.isEmpty()) {
			boolean i = s.checkToken(token);
			if (i) {
				List<Item> al = new ArrayList<Item>();
				s.list(al, token);
				s.closeConnection();
				return al;
			} 
				else {
				HashMap<String, String> map = new HashMap<String, String>();
				List<HashMap> al = new ArrayList<HashMap>();
				map.put("error", "Authentication failed");
				al.add(map);
				s.closeConnection();
				return al; 
			}
		} else {
			HashMap<String, String> map = new HashMap<String, String>();
			List<HashMap> al = new ArrayList<HashMap>();
			map.put("error", "Not logged in");
			al.add(map);
			s.closeConnection();
			return al; 
		}
		
	}
	
	//@RequestMapping(value = "/product/{barcode}", method = RequestMethod.GET)
	/***************************
	 * 
	 * @GetMapping("/{barcode}")
		public Item getItem(@PathVariable("barcode") String barcode, @RequestParam("token") String token) {
		sql s = new sql();
		Item item = s.listOneItem(barcode);
		s.closeConnection();
		return item;
	}
	 ***************************/
	
	// get one item
	@GetMapping("/{barcode}")
	public List<?> getItem(@PathVariable("barcode") String barcode, @RequestParam("token") String token) {
		sql s = new sql();
		if (!token.isEmpty()) {
			List<Item> al = new ArrayList<Item>();
			al = s.listOneItem(barcode, token);
			s.closeConnection();
			return al;
		} else {
			HashMap<String, String> map = new HashMap<String, String>();
			List<HashMap> al = new ArrayList<HashMap>();
			map.put("error", "Not logged in");
			al.add(map);
			s.closeConnection();
			return al;
		}
		
	}
	
	/*
	//@RequestMapping(value = "/product/{barcode}", method = RequestMethod.PUT)
	@PutMapping("/{barcode}")
	public void updateInventory(@PathVariable("barcode") String barcode, @RequestParam("token") String token, @RequestBody Item item) {
		//Item currentItem = getItem(barcode);
		sql s = new sql();
		s.update(getItem(barcode, token), item);
		s.closeConnection();
	}
	*/
	
	/*
	@PutMapping("/{barcode}")
	public @ResponseBody Map<String, String> update (@PathVariable("barcode") String barcode, @RequestParam("token") String token, @RequestBody Item item) {
		sql s = new sql();
		HashMap<String, String> map = new HashMap<String, String>();
		
		if (token.equals("admin")) {
			if (s.isNull(barcode, null)) {
				s.closeConnection();
				map.put("error", "Product not found");
				return map;
			} else {
				s.update(item, token, barcode);
				
				s.closeConnection();
				map.put("success", "Product updated");
				return map;
			}
		} else {
			s.closeConnection();
			map.put("error", "Authentication failed");
			return map;
		}
	}
	*/
	
	// update
	@PutMapping("/{barcode}")
	public @ResponseBody Map<String, String> update (@PathVariable("barcode") String barcode, @RequestParam("token") String token, @RequestBody String json) {
		sql s = new sql();
		HashMap<String, String> map = new HashMap<String, String>();
		
		if (!token.isEmpty()) {
			
			ObjectMapper mapper = new ObjectMapper();
			
			try {
				Map<String, String> json_map = mapper.readValue(json, new TypeReference<Map<String, String>>(){});
				if (token.equals("admin")) {
					if (s.isNull(barcode, null)) {
						s.closeConnection();
						map.put("error", "Product not found");
						return map;
					} else {
						Item item = new Item(json_map.get("barcode"),
								 json_map.get("name"),
								 json_map.get("quantity"),
								 json_map.get("shop"));
						s.updateAdmin(item, token, barcode);
						
						s.closeConnection();
						map.put("success", "Product updated");
						return map;
					}
				} else {
					if (token.equals("shop1") || token.equals("shop2") || token.equals("shop3")) {
						s.updateShop(json_map.get("type"),
									 json_map.get("quantity"),
									 token, barcode);
						
						s.closeConnection();
						map.put("success", "Product updated");
						return map;
						
					} else {
						s.closeConnection();
						map.put("error", "Authentication failed");
						return map;
					}
				}
			} catch(Exception e) {
				s.closeConnection();
				return null;
			}
		} else {
			
			map.put("error", "Not logged in");
			s.closeConnection();
			return map;
		}
	}
	
	// delete a product
	//@RequestMapping(value = "/product/{barcode}", method = RequestMethod.DELETE)
	@DeleteMapping("/{barcode}")
	public @ResponseBody Map<String, String> delete(@PathVariable("barcode") String barcode, @RequestParam("token") String token, @RequestBody String json) {
		sql s = new sql();
		HashMap<String, String> map = new HashMap<String, String>();
			
		if (!token.isEmpty()) {
			if (token.equals("admin")) {
				boolean i = s.delete(barcode);
				if (i) {
					s.closeConnection();
					map.put("success", "Product deleted");
					return map;
						
				} else {
					s.closeConnection();
					map.put("error", "Product not found");
					return map;
				}
			} else {
				s.closeConnection();
				map.put("error", "Authentication failed");
				return map;
			}
		} else {
			s.closeConnection();
			map.put("error", "Not logged in");
			return map;
		}
	}
	
	//@RequestMapping(value = "/product", method = RequestMethod.POST)
	
	// create a new product
	@PostMapping
	public @ResponseBody Map<String, String> create(@RequestBody Item item, @RequestParam("token") String token) {
		HashMap<String, String> map = new HashMap<String, String>();
		sql s = new sql();
		
		if (!token.isEmpty()) {
			if (token.equals("admin")) {
				if (s.create(item)) {
					s.closeConnection();
					map.put("success", "Product added");
					return map;
				} else {
					s.closeConnection();
					map.put("error", "Product duplicated");
					return map;
				}
			} else {
					s.closeConnection();
					map.put("error", "Authentication failed");
					return map;
			}
		} else {
			s.closeConnection();
			map.put("error", "Not logged in");
			return map;
		}
	}
	
	
	/*
	@PostMapping
	public @ResponseBody Map<String, String> create(@RequestBody String json, @RequestParam("token") String token) {
		HashMap<String, String> map = new HashMap<String, String>();
		sql s = new sql();
		ObjectMapper mapper = new ObjectMapper();
		try {
			Map<String, String> json_map = mapper.readValue(json, new TypeReference<Map<String, String>>(){});
			
			if (token.equals("admin")) {
				
				if (s.create(item)) {
					s.closeConnection();
					map.put("success", "Product added");
					return map;
				} else {
					s.closeConnection();
					map.put("error", "Product duplicated");
					return map;
				}
			} else {
					
					map.put("error", "Authentication failed");
					return map;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	*/
	
	// authenticate
	@PostMapping("/auth")
	public Map<String, String> authenticate(@RequestBody Account account) {

		HashMap<String, String> map = new HashMap<String, String>();
		sql s = new sql();
		String token = "Wrong Username or Password";
		try {
			token = s.searchToken(account);
			map.put("token", token);
		} catch (Exception e) {
			map.put("error", token);
		}
		s.closeConnection();
	    return map;
	}
}	
