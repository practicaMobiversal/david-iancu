package com.mobiversal.practica.life;

/**
 * Created by DavidEu on 11-Jul-17.
 */

public class Users {


    public Users(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String name;

    public Users(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public String getImage() {

        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String image;

}
