package be.adrisuys.desperados.models;

import java.io.Serializable;

public class Bandit implements Serializable {

    private String name;
    private Ability ability;
    private int timeInJail;

    public Bandit(String name, Ability ability) {
        this.name = name;
        this.ability = ability;
        timeInJail = ability.getValue();
    }

    public Ability getAbility() {
        return ability;
    }

    public boolean isFreed() {
        return timeInJail <= 0;
    }

    public String acts(int power){
        if (isFreed()) {
            return ability.toString();
        } else {
            if (timeInJail < power){
                int remainingPower = power - timeInJail;
                timeInJail = 0;
                return acts(remainingPower);
            } else {
                timeInJail -= power;
                if (isFreed()){
                    return "FREE";
                } else {
                    return "NONE";
                }
            }
        }
    }

    public void setFree() {
        this.timeInJail = 0;
    }

    public void evade(){
        this.timeInJail -= 1;
    }

    public String getName() {
        return name;
    }

    public String getTimeInJail() {
        if (timeInJail == 0){
            return "FREE";
        }
        return String.valueOf(timeInJail);
    }
}
