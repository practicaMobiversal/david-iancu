package com.mobiversal.practica.life;

/**
 * Created by iancuu on 13.07.2017.
 */

public class Show_Chat_Activity_Data_Items {
    private String Email;
    private String image;
    private String Name;
    private String tumbimg;

    public void settumbimg(String tumbImage) {
        tumbimg = tumbImage;
    }

    public String gettumbimg() {
        return tumbimg;
    }

    public Show_Chat_Activity_Data_Items()
    {
    }

    public Show_Chat_Activity_Data_Items(String email, String Image, String name) {
        Email = email;
        image = Image;
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getImage_Url() {
        return image;
    }

    public void setImage_Url(String image_Url) {
        image = image_Url;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}

