package be.adrisuys.desperados.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Gang implements Serializable {

    private String name;
    private List<Bandit> bandits;
    private int money;
    private boolean isComputer;

    public Gang(String name, List<Bandit> bandits, int money, boolean isComputer, Game game){
        this.name = name;
        this.bandits = bandits;
        this.money = money;
        this.isComputer = isComputer;
    }

    public String makeBanditsAct(Ability ability, int power){
        for (Bandit bandit: bandits){
            if (bandit.getAbility().equals(ability)){
                String action = bandit.acts(power);
                if (action.equals(Ability.BRAIN.toString())){
                    action = name;
                }
                return action;
            }
        }
        return null;
    }

    public boolean canBeAttackedByBandit(Ability ability){
        if (ability.getValue() == 2) return true;
        for (Bandit bandit: bandits){
            if (bandit.getAbility().equals(ability)){
                return !bandit.isFreed();
            }
        }
        return true;
    }

    public boolean hasEverybodyFreed(){
        for (Bandit bandit: bandits){
            if (!bandit.isFreed()){
                return false;
            }
        }
        return true;
    }

    public boolean freeBandit(Ability ability, boolean canEvadeTotally){
        for (Bandit bandit: bandits){
            if (bandit.getAbility().equals(ability)){
                if (bandit.isFreed()){
                    return false;
                } else {
                    if (canEvadeTotally){
                        bandit.setFree();
                    } else {
                        bandit.evade();
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public void loseMoney(int amount){
        money -= amount;
    }

    public void robBank(int gain){
        money += gain;
    }

    public boolean isComputer() {
        return isComputer;
    }

    public int getMoney() {
        return money;
    }

    public String getName() {
        return name;
    }

    public void setAI(){
        isComputer = true;
    }

    public Bandit getBanditByAbility(Ability ability){
        for (Bandit bandit: bandits){
            if (bandit.getAbility().equals(ability)){
                return bandit;
            }
        }
        return null;
    }

    public Bandit getBandit(int i){
        return bandits.get(i);
    }

    public List<Bandit> getActiveBandit(List<Dice> dices){
        List<Bandit> bandits = new ArrayList<>();
        for (Dice dice: dices){
            if (!dice.getValue().equals("*")){
                Bandit bandit = getBanditByAbilityValue(Integer.parseInt(dice.getValue()));
                if (bandit != null && !bandits.contains(bandit)){
                    bandits.add(bandit);
                }
            }
        }
        return bandits;
    }

    private Bandit getBanditByAbilityValue(int value){
        for (Bandit bandit: bandits){
            if (bandit.getAbility().getValue() == value){
                return bandit;
            }
        }
        return null;
    }

    public Bandit getBestOptionsAI(List<Bandit> activeBandits, Gang otherGang){
        Bandit bestOptions;
        bestOptions = lookToCloseEverything(activeBandits, otherGang);
        return bestOptions;
    }

    private Bandit lookToCloseEverything(List<Bandit> activeBandits, Gang otherGang) {
        for (Bandit bandit: activeBandits){
            if (!bandit.isFreed()){
                return bandit;
            }
        }
        return lookForBetterAttacker(activeBandits, otherGang);
    }

    private Bandit lookForBetterAttacker(List<Bandit> activeBandits, Gang otherGang) {
        for (Bandit bandit: activeBandits){
            if (bandit.isFreed() && otherGang.canBeAttackedByBandit(bandit.getAbility())){
                return bandit;
            }
        }
        return bandits.get(bandits.size() - 1);
    }

    public List<Bandit> getBanditsInJail(){
        List<Bandit> bandits = new ArrayList<>();
        for (Bandit b: this.bandits){
            if (!b.isFreed()){
                bandits.add(b);
            }
        }
        return bandits;
    }
}
