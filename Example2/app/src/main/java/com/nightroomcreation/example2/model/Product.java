package com.nightroomcreation.example2.model;

/**
 * Created by Administrator on 18-Feb-17.
 */

public class Product {
    public String name;
    public long expirationTime;

    public Product(String name, long expirationTime){
        this.name = name;
        this.expirationTime = expirationTime;
    }
}
