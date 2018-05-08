package demo;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

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
	Ran ran = new Ran();
	final String ad_token = ran.getRan();
	final String s1_token = ran.getRan();
	final String s2_token = ran.getRan();
	final String s3_token = ran.getRan();
	
	public List getOne(sql s, String barcode, int shop) {
		if (s.isNull(barcode, shop)) {
			s.closeConnection();
			HashMap<String, String> map = new HashMap<String, String>();
			List<HashMap> al = new ArrayList<HashMap>();
			map.put("error", "Product not found");
			al.add(map);
			return al;
			}
			else {
			List<Item> al = new ArrayList<Item>();
			s.listOneItem(barcode, al, shop);
			s.closeConnection();
			return al;
			}
	}
	public HashMap<String, String> updateShop(sql s, String barcode, int shop, Map<String, String> json_map){
	HashMap<String, String> map = new HashMap<String, String>();
	if (s.isNull(barcode, shop)) {
		s.closeConnection();
		map.put("error", "Product not found");
		return map;
	}
				else {
					s.updateShop(json_map.get("type"),
							 json_map.get("quantity"),
							 barcode, shop);
					s.closeConnection();
					map.put("success", "Product updated");
					return map;
				}
	}
	
public HashMap<String, String> createItem(sql s, Item item, int shop){
	HashMap<String, String> map = new HashMap<String, String>();
	if (s.create(item, shop)) {
		s.closeConnection();
		map.put("success", "Product added");
		return map;
	}
	else {
		s.closeConnection();
		map.put("error", "Product duplicated");
		return map;
	}
}
	
	@CrossOrigin(origins = "http://localhost:9000")
	@RequestMapping(value = "/auth", method = RequestMethod.POST)
	public @ResponseBody HashMap<String, String> create(@RequestBody Account acc) {
		HashMap<String, String> map = new HashMap<String, String>();
		if (acc.getUser().equals("admin") && acc.getPass().equals("admin")) {
			map.put("token", ad_token);
			map.put("role", "admin");
		}
		else if (acc.getUser().equals("shop1") && acc.getPass().equals("shop1")) {
			map.put("token", s1_token);
			map.put("role", "shop1");
		}
		else if (acc.getUser().equals("shop2") && acc.getPass().equals("shop2")) {
			map.put("token", s2_token);
			map.put("role", "shop2");
		}
		else if (acc.getUser().equals("shop3") && acc.getPass().equals("shop3")) {
			map.put("token", s3_token);
			map.put("role", "shop3");
		}
		else {
			map.put("Error", "Wrong username/password");
		}
		return map;
	}
	
	//------------show inventory-------------//
	@CrossOrigin(origins = "http://localhost:9000")
	@RequestMapping(value = "/product", method = RequestMethod.GET)
	public List<?> getInventory(@RequestParam(value="token", defaultValue="") String tok) {
		sql s = new sql();
		List<Item> al = new ArrayList<Item>();
		if (tok.equals(s1_token)){
			s.list(al, 1);
			s.closeConnection();
			return al;
		}
		else if (tok.equals(s2_token)){
			s.list(al, 2);
			s.closeConnection();
			return al;
		}
		else if (tok.equals(s3_token)){
			s.list(al, 3);
			s.closeConnection();
			return al;
		}
		else if (tok.equals(ad_token)){
			s.list(al, 1);
			s.list(al, 2);
			s.list(al, 3);
			s.closeConnection();
			return al;
		}
		else {
			HashMap<String, String> map = new HashMap<String, String>();
			List<HashMap> ab = new ArrayList<HashMap>();
			map.put("error", "Authentication failed");
			ab.add(map);
			s.closeConnection();
			return ab;
		}
	}
	
	
	@CrossOrigin(origins = "http://localhost:9000")
	@RequestMapping(value = "/product/{barcode}", method = RequestMethod.GET)
	public List<?> getItem(@PathVariable("barcode") String barcode,
							@RequestParam(value="token", defaultValue="") String tok) {
		sql s = new sql();
		if (tok.equals(s1_token)){
			return getOne(s, barcode, 1);
		}
		else if (tok.equals(s2_token)){
			return getOne(s, barcode, 2);
		}
		else if (tok.equals(s3_token)){
			return getOne(s, barcode, 3);
		}
		else if (tok.equals(ad_token)){
			boolean i=s.isNull(barcode, 1);
			boolean i2=s.isNull(barcode, 2);
			boolean i3=s.isNull(barcode, 3);
			if (i && i2 && i3) {
				s.closeConnection();
				HashMap<String, String> map = new HashMap<String, String>();
				List<HashMap> al = new ArrayList<HashMap>();
				map.put("error", "Product not found");
				al.add(map);
				return al;
			}
			else {
				List<Item> al = new ArrayList<Item>();
				s.listOneItem(barcode, al, 1);
				s.listOneItem(barcode, al, 2);
				s.listOneItem(barcode, al, 3);
				s.closeConnection();
				return al;
			}
			
		}
		else {
			HashMap<String, String> map = new HashMap<String, String>();
			List<HashMap> al = new ArrayList<HashMap>();
			map.put("error", "Authentication failed");
			al.add(map);
			s.closeConnection();
			return al;
		}
	}
	@CrossOrigin(origins = "http://localhost:9000")
	@RequestMapping(value = "/product/{barcode}", method = RequestMethod.PUT)
	public @ResponseBody Map<String, String> update (@PathVariable("barcode") String barcode, @RequestBody String json,
													@RequestParam(value="token", defaultValue="") String tok) {
		sql s = new sql();
		HashMap<String, String> map = new HashMap<String, String>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			Map<String, String> json_map = mapper.readValue(json, new TypeReference<Map<String, String>>(){});
			if (tok.equals(ad_token)) {
				if (s.isNull(barcode, 1) && s.isNull(barcode, 2) && s.isNull(barcode, 3)) {
					s.closeConnection();
					map.put("error", "Product not found");
					return map;
				}
				else {
					Item item = new Item(json_map.get("barcode"),
							 json_map.get("name"),
							 json_map.get("quantity"),
							 json_map.get("shop"));
					boolean i = s.updateAdmin(item, barcode);
					s.closeConnection();
					if (i) {
						map.put("success", "Product updated");
					}
					else {
						map.put("error", "Shop not exist");
					}
					
					return map;
				}
			}
			else if (tok.equals(s1_token)) {
				return updateShop(s, barcode, 1, json_map);
			}
			else if (tok.equals(s2_token)) {
				return updateShop(s, barcode, 2, json_map);
			}
			else if (tok.equals(s3_token)) {
				return updateShop(s, barcode, 3, json_map);
			}
			else {
				s.closeConnection();
				map.put("error", "Authentication failed");
				return map;
			}
		}catch(Exception e) {
			s.closeConnection();
			return null;
		}
		}
	
	@CrossOrigin(origins = "http://localhost:9000")
	@RequestMapping(value = "/product/{barcode}", method = RequestMethod.DELETE)
	public @ResponseBody Map<String, String> delete(@PathVariable("barcode") String barcode,
										@RequestParam(value="token", defaultValue="") String tok,
										@RequestBody String json) {
		sql s = new sql();
		HashMap<String, String> map = new HashMap<String, String>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			Map<String, String> json_map = mapper.readValue(json, new TypeReference<Map<String, String>>(){});
			if (tok.equals(ad_token)) {
				String d=json_map.get("shop");
				if (d.equals("1")) {
					boolean i = s.delete(barcode, 1);
					if (i) {
						s.closeConnection();
						map.put("success", "Product deleted");
						return map;
						
					} else {
						s.closeConnection();
						map.put("error", "Product not found");
						return map;
					}
				}
				else if (d.equals("2")) {
					boolean i = s.delete(barcode, 2);
					if (i) {
						s.closeConnection();
						map.put("success", "Product deleted");
						return map;
						
					} else {
						s.closeConnection();
						map.put("error", "Product not found");
						return map;
					}
				}
				else if (d.equals("3")) {
					boolean i = s.delete(barcode, 3);
					if (i) {
						s.closeConnection();
						map.put("success", "Product deleted");
						return map;
						
					} else {
						s.closeConnection();
						map.put("error", "Product not found");
						return map;
					}
				}
				else {
					System.out.print(d);
					s.closeConnection();
					map.put("error", "Shop not exist");
					return map;
				}
				
			}
			else {
				s.closeConnection();
				map.put("error", "Authentication failed");
				return map;
			}
		}catch(Exception e) {
			s.closeConnection();
			return null;
		}
		
	}
	
	@CrossOrigin(origins = "http://localhost:9000")
	@RequestMapping(value = "/product", method = RequestMethod.POST)
	public @ResponseBody  Map<String, String> create(@RequestBody Item item,
													@RequestParam(value="token", defaultValue="") String tok) {
		HashMap<String, String> map = new HashMap<String, String>();
		sql s = new sql();
		if (tok.equals(ad_token)) {
			if (item.getBarcode().equals("")) {
				map.put("error", "No barcode");
				return map;
			}
			else {
				if (item.getShop().equals("1")) {
					return createItem(s, item, 1);
				}
				else if (item.getShop().equals("2")) {
					return createItem(s, item, 2);
				}
				else if (item.getShop().equals("3")) {
					return createItem(s, item, 3);
				}
				else {
					s.closeConnection();
					map.put("error", "Shop doesn't exist");
					return map;
				}
			}
			
		}
		else {
			s.closeConnection();
			map.put("error", "Authentication failed");
			return map;
		}
	}
	
}
