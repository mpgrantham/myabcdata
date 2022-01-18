package com.canalbrewing.myabcdata.model;

import com.canalbrewing.myabcdata.resultsetmapper.annotation.DbColumn;

public class Intensity {

    @DbColumn(name = "id")
    private int id;

    @DbColumn(name = "intensity")
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