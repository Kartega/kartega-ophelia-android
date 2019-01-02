package com.ahmetkilic.eaframework.object;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InstanceTestItem implements Serializable,Cloneable {
    private String name;
    private int id;
    private boolean isFilled;
    private TempObject tempObject;
    private List<String> items;
    private HashMap<String,TempObject> map;
    private ArrayList<Human> humans;
    private HashMap<String,HashMap<String,String>> otherMap;

    public InstanceTestItem() {

    }

    public HashMap<String, HashMap<String, String>> getOtherMap() {
        return otherMap;
    }

    public void setOtherMap(HashMap<String, HashMap<String, String>> otherMap) {
        this.otherMap = otherMap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public List<String> getItems() {
        return items;
    }

    public HashMap<String, TempObject> getMap() {
        return map;
    }

    public void setMap(HashMap<String, TempObject> map) {
        this.map = map;
    }

    public ArrayList<Human> getHumans() {
        return humans;
    }

    public void setHumans(ArrayList<Human> humans) {
        this.humans = humans;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isFilled() {
        return isFilled;
    }

    public void setFilled(boolean filled) {
        isFilled = filled;
    }

    public TempObject getTempObject() {
        return tempObject;
    }

    public void setTempObject(TempObject tempObject) {
        this.tempObject = tempObject;
    }

    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
