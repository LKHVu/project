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
			s.list1(al);
			s.closeConnection();
			return al;
		}
		else if (tok.equals(s2_token)){
			s.list2(al);
			s.closeConnection();
			return al;
		}
		else if (tok.equals(s3_token)){
			s.list3(al);
			s.closeConnection();
			return al;
		}
		else if (tok.equals(ad_token)){
			s.list1(al);
			s.list2(al);
			s.list3(al);
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
			if (s.isNull1(barcode)) {
			s.closeConnection();
			HashMap<String, String> map = new HashMap<String, String>();
			List<HashMap> al = new ArrayList<HashMap>();
			map.put("error", "Product not found");
			al.add(map);
			return al;
			}
			else {
			List<Item> al = new ArrayList<Item>();
			s.listOneItem1(barcode, al);
			s.closeConnection();
			return al;
			}
			
		}
		else if (tok.equals(s2_token)){
			if (s.isNull2(barcode)) {
				s.closeConnection();
				HashMap<String, String> map = new HashMap<String, String>();
				List<HashMap> al = new ArrayList<HashMap>();
				map.put("error", "Product not found");
				al.add(map);
				return al;
			}
			else {
				List<Item> al = new ArrayList<Item>();
				s.listOneItem2(barcode, al);
				s.closeConnection();
				return al;
			}
		}
		else if (tok.equals(s3_token)){
			if (s.isNull3(barcode)) {
				s.closeConnection();
				HashMap<String, String> map = new HashMap<String, String>();
				List<HashMap> al = new ArrayList<HashMap>();
				map.put("error", "Product not found");
				al.add(map);
				return al;
			}
			else {
				List<Item> al = new ArrayList<Item>();
				s.listOneItem3(barcode, al);
				s.closeConnection();
				return al;
			}
		}
		else if (tok.equals(ad_token)){
			boolean i=s.isNull1(barcode);
			boolean i2=s.isNull2(barcode);
			boolean i3=s.isNull3(barcode);
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
				s.listOneItem1(barcode, al);
				s.listOneItem2(barcode, al);
				s.listOneItem3(barcode, al);
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
				if (s.isNull1(barcode) && s.isNull2(barcode) && s.isNull3(barcode)) {
					s.closeConnection();
					map.put("error", "Product not found");
					return map;
				}
				else {
					Item item = new Item(json_map.get("barcode"),
							 json_map.get("name"),
							 json_map.get("quantity"),
							 json_map.get("shop"));
					s.updateAdmin(item, barcode);
					s.closeConnection();
					map.put("success", "Product updated");
					return map;
				}
			}
			else if (tok.equals(s1_token)) {
				if (s.isNull1(barcode)) {
					s.closeConnection();
					map.put("error", "Product not found");
					return map;
				}
				else {
					s.updateShop1(json_map.get("type"),
							 json_map.get("quantity"),
							 barcode);
					s.closeConnection();
					map.put("success", "Product updated");
					return map;
				}
			}
			else if (tok.equals(s2_token)) {
				if (s.isNull2(barcode)) {
					s.closeConnection();
					map.put("error", "Product not found");
					return map;
				}
				else {
					s.updateShop2(json_map.get("type"),
							 json_map.get("quantity"),
							 barcode);
					s.closeConnection();
					map.put("success", "Product updated");
					return map;
				}
			}
			else if (tok.equals(s3_token)) {
				if (s.isNull3(barcode)) {
					s.closeConnection();
					map.put("error", "Product not found");
					return map;
				}
				else {
					s.updateShop3(json_map.get("type"),
							 json_map.get("quantity"),
							 barcode);
					s.closeConnection();
					map.put("success", "Product updated");
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
					boolean i = s.delete1(barcode);
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
					boolean i = s.delete2(barcode);
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
					boolean i = s.delete3(barcode);
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
					if (s.create1(item)) {
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
				else if (item.getShop().equals("2")) {
					if (s.create2(item)) {
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
				else if (item.getShop().equals("3")) {
					if (s.create3(item)) {
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
