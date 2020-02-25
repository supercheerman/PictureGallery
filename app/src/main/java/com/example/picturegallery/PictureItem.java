package com.example.picturegallery;

import java.util.Date;

public class PictureItem {
    private String mUrl;
    private String mDescription;
    private String mDate;

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public PictureItem(){
    }
    public String getUrl() {
        return mUrl;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public void setDescription(String description) {
        mDescription = description;
    }
}
