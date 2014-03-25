package com.example.smart_fridge_android;

import java.util.List;

public class Recipe {

    private int id;
    private String name;
    private String directions;
    private String notes;
    private String ingredients;

    public Recipe(){

    }

    public Recipe(String name, String directions, String notes, String ingredients) {
        this.name = name;
        this.directions = directions;
        this.notes = notes;
        this.ingredients = ingredients;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirections() {
        return directions;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public String getIngredients() {
    	return ingredients;
    }
    
    public void setIngredients(String ingredients) {
    	this.ingredients = ingredients;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", directions='" + directions + '\'' +
                ", notes='" + notes + '\'' +
                ", ingredients='" + ingredients + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Recipe recipe = (Recipe) o;

        if (id != recipe.id) return false;
        if (directions != null ? !directions.equals(recipe.directions) : recipe.directions != null) return false;
        if (ingredients != null ? !ingredients.equals(recipe.ingredients) : recipe.ingredients != null) return false;
        if (name != null ? !name.equals(recipe.name) : recipe.name != null) return false;
        if (notes != null ? !notes.equals(recipe.notes) : recipe.notes != null) return false;

        return true;
    }
}
