package com.canalbrewing.myabcdata.model;

import com.canalbrewing.myabcdata.resultsetmapper.annotation.DbColumn;

public class Relationship {

    @DbColumn(name = "id")
    private int id;

    @DbColumn(name = "relationship")
    private String name;

    public Relationship() {
    }

    public Relationship(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Relationship [id=" + id + ", name=" + name + "]";
    }

}