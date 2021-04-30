package com.example.mesfrigos4;

public class Aliment_frigidaire {

    private String id;
    private String name;
    private String category;
    private String expiration_date;

    public Aliment_frigidaire(String id, String name, String category, String expiration_date) {
        super();
        this.id = id;
        this.name = name;
        this.category = category;
        this.expiration_date=expiration_date;
    }
    public String  getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String nom) {
        this.name = nom;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getExpiration_date() {
        return expiration_date;
    }
    public void setExpiration_date(String expiration_date) {
        this.expiration_date = expiration_date;
    }
    @Override
    public String toString() {
        return "aliment_frigidaire [id ="+ id + ", name=" + name + ", category=" + category + ",expiration_date:"+ expiration_date +"]";
    }
}
