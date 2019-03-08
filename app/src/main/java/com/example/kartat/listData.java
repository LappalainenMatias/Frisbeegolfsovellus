package com.example.kartat;

public class listData {

    String name;
    String place;
    String amount_lanes;
    String imgstring;
    String webstring;

    public listData(String name, String place, String amount_lanes, String imgstring, String webstring) {
        this.name = name;
        this.place = place;
        this.amount_lanes = amount_lanes;
        this.imgstring = imgstring;
        this.webstring = webstring;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getAmount_lanes() {
        return amount_lanes;
    }

    public void setAmount_lanes(String amount_lanes) {
        this.amount_lanes = amount_lanes;
    }

    public String getImgstring() {
        return imgstring;
    }

    public void setImgstring(String imgstring) {
        this.imgstring = imgstring;
    }

    public String getWebstring() {
        return webstring;
    }

    public void setWebstring(String webstring) {
        this.webstring = webstring;
    }
}
