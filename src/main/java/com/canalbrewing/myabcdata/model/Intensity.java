package com.canalbrewing.myabcdata.model;

public class Intensity {

    private int id;
    private String intensity;

    public Intensity() {
    }

    public Intensity(int id, String intensity) {
        this.id = id;
        this.intensity = intensity;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIntensity() {
        return intensity;
    }

    public void setIntensity(String intensity) {
        this.intensity = intensity;
    }
        
}