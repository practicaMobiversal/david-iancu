package com.mobiversal.practica.life;


public class Users {


    public Users(){}



    private String name;
    private String TumbImage;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTumbImage() {
        return TumbImage;
    }





    public Users(String name,String img) {
        this.name = name;
        this.TumbImage = img;
    }







}
