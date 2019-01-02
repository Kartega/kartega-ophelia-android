package com.ahmetkilic.eaframework.object;

import java.io.Serializable;

public class Car implements Serializable,Cloneable {
    private String carname;

    public Car() {
    }

    public Car(String carname) {
        this.carname = carname;
    }

    @Override
    public String toString() {
        return carname;
    }

    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
