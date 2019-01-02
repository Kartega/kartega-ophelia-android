package com.ahmetkilic.eaframework.object;

import java.io.Serializable;

public class Human implements Serializable,Cloneable {
    private String name;
    private Car car;

    public Human() {
    }

    public Human(String name, Car car) {
        this.name = name;
        this.car = car;
    }

    @Override
    public String toString() {
        return name + "  -  " + car.toString();
    }

    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
