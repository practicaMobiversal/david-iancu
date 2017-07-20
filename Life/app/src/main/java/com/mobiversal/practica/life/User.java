package com.mobiversal.practica.life;

/**
 * Created by iancuu on 13.07.2017.
 */

public class User {
    private String Email;
    private String image;
    private String Name;
    private String tumbimg;
    private String Latitude;
    private String Longitude;
    private String GeoLimit;

    public void settumbimg(String tumbImage) {
        tumbimg = tumbImage;
    }

    public String gettumbimg() {
        return tumbimg;
    }

    public User()
    {
    }

    public User(String email, String Image, String name) {
        Email = email;
        image = Image;
        Name = name;
    }

    public String getGeoLimit() {
        return GeoLimit;
    }

    public void setGeoLimit(String geoLimit) {
        GeoLimit = geoLimit;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
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

