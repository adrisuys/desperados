package be.adrisuys.desperados.models;

import java.io.Serializable;

public class Dice implements Serializable {

    private String value;
    private boolean isKeptAside;

    public Dice(){
        value = "";
        isKeptAside = false;
    }

    public void setValue(String value) {
        if (!isKeptAside){
            this.value = value;
        }
    }

    public String getValue() {
        return value;
    }

    public boolean isLocked() {
        return isKeptAside;
    }

    public void lock(){
        isKeptAside = true;
    }

    public void unlock(){
        isKeptAside = false;
    }
}
