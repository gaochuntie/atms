package com.highsys.systemchanger;

public class Devices {
    public String name;
    public int id;
    public  Devices(String nam){
        this.name=nam;
    }
    public String getName(){
        return this.name;
    }
    public int getId(){
        return this.id;
    }
}
