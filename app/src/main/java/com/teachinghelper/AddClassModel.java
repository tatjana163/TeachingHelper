package com.teachinghelper;

import java.io.Serializable;
import java.util.HashMap;

public class AddClassModel implements Serializable {

    private String name;
    private int order;
    private String note;


    public AddClassModel() {
    }

    public AddClassModel(String name,int order, String note) {
        this.name = name;
        this.order = order;
        this.note = note;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
