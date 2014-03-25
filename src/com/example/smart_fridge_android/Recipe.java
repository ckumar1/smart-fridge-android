package com.example.smart_fridge_android;

public class Recipe {

    private int id;
    private String name;
    private String directions;
    private String notes;

    public Recipe(){

    }

    public Recipe(String name, String directions, String notes) {
        this.name = name;
        this.directions = directions;
        this.notes = notes;
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

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", directions='" + directions + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}
