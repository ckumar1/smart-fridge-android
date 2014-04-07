package com.example.smart_fridge_android;

public class Food {

    private int id; // Set by the internal db
    private String name;
    private String description;
    private String expirationDate;
    private String category;
    private int calories;
    private int quantity;

    public Food(){

    }

    public Food(String name, String description, String expirationDate, String category, int calories, int quantity) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.expirationDate = expirationDate;
        this.calories = calories;
        this.quantity = quantity;
    }

    public int getId(){
        return id;
    }

    public void setId(int id) { 
    	this.id = id; 
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getQuantity() { 
    	return quantity; 
    }

    public void setQuantity(int quantity) { 
    	this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Food{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", expirationDate='" + expirationDate + '\'' +
                ", calories=" + calories +
                ", quantity=" + quantity +
                '}';
    }

    /* Used for testing */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Food food = (Food) o;

        if (id != food.id) return false;
        if (calories != food.calories) return false;
        if (quantity != food.quantity) return false;
        if (category != null ? !category.equals(food.category) : food.category != null) return false;
        if (description != null ? !description.equals(food.description) : food.description != null) return false;
        if (expirationDate != null ? !expirationDate.equals(food.expirationDate) : food.expirationDate != null)
            return false;
        if (name != null ? !name.equals(food.name) : food.name != null) return false;

        return true;
    }
}
