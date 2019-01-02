package com.ahmetkilic.eaframework.object;

import java.io.Serializable;

public class Album implements Serializable {
    private int id;
    private int userID;
    private String title;

    public Album() {
    }

    public Album(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public int getUserID() {
        return userID;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Album)
            return getId() == ((Album) obj).getId();
        return super.equals(obj);
    }
}
