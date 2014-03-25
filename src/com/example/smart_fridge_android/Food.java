package com.example.smart_fridge_android;

public class Food {

    private int id;
    private String name;
    private String description;
    private String expirationDate;
    private String category;
    private int calories;

    public Food(){

    }

    public Food( String name, String description, String expirationDate, String category, int calories) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.expirationDate = expirationDate;
        this.calories = calories;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
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

    @Override
    public String toString() {
        return "Food{" +
                "id='" + id + '\'' +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", expirationDate='" + expirationDate + '\'' +
                ", calories=" + calories +
                '}';
    }
}
