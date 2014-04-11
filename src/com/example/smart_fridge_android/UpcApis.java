package com.example.smart_fridge_android;

/*
* Being that there are many UPC databases out there with a lot of different API's
* to choose from, this class serves to hold all the different API's we can query.
*/
public class UpcApis {

public static String getNutritionixUrl() {
return "https://api.nutritionix.com/v1_1/item";
}

public static String getNutritionixAppId() {
return "5538930e";
}

public static String getNutritionixAppKey() {
return "e5e7dd3f04da8c55a349ac6ed45c7b47";
}
}