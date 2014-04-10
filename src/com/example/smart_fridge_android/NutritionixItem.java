package com.example.smart_fridge_android;

public class NutritionixItem {

	private String item_name;
	private String brand_name;
	private String item_description;
	
	NutritionixItem() {
		item_name = "";
		brand_name = "";
		item_description = "";
	}
	
	NutritionixItem(String itemName, String brandName, String itemDescription) {
		item_name = itemName;
		brand_name = brandName;
		item_description = itemDescription;
	}
	
	public String get_item_name() {
		return item_name;
	}
	
	public String get_brand_name() {
		return brand_name;
	}
	
	public String get_item_description() {
		return item_description;
	}
	
	public void set_item_name(String itemName) {
		item_name = itemName;
	}
	
	public void set_brand_name(String brandName) {
		brand_name = brandName;
	}
	
	public void set_item_description(String itemDescription) {
		item_description = itemDescription;
	}
}
