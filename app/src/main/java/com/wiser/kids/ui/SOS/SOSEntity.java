package com.wiser.kids.ui.SOS;

import java.io.Serializable;

public class SOSEntity implements Serializable{

    public String number;

    public String name;

    public SOSEntity(String number, String name) {
        this.number = number;
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }
}
