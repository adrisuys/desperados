package be.adrisuys.desperados.models;

import android.os.Handler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import be.adrisuys.desperados.Presenter;

public class Game implements Serializable {

    private final String[] dice_faces;
    private List<Gang> gangs;
    private int bank;
    private int currentGangIndex;
    private List<Dice> dices;
    private Presenter presenter;
    private int nbOfBankRobbery;
    private int nbOfAttacks;
    private int nbOfEvasion;
    private int nbActionsForRound;
    private int rollCount;
    private GameState gameState;

    public Game(){
        dice_faces = new String[]{"*", "2", "3", "4", "5", "6"};
        bank = 0;
        currentGangIndex = 0;
        setGangs();
        dices = new ArrayList<>();
        for (int i = 0; i < 4; i++){
            dices.add(new Dice());
        }
        nbOfBankRobbery = 0;
        nbOfAttacks = 0;
        nbOfEvasion = 0;
        nbActionsForRound = 0;
        rollCount = 1;
        gameState = GameState.DICE_ROLL;
    }

    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    public String getOwnGangName(){
        return gangs.get(0).getName();
    }

    public String getComputerGangName(){
        return gangs.get(1).getName();
    }

    private void setGangs() {
        List<Gang> gangs = new ArrayList<>();
        gangs.add(createRedDamnationGang());
        gangs.add(createLosLibertadoresGang());
        gangs.add(createOlesonGang());
        this.gangs = gangs;
    }

    public void pickPlayingGangs(){
        Collections.shuffle(gangs);
        gangs.remove(2);
        gangs.get(1).setAI();
    }

    public String getBank() {
        return bank + "$";
    }

    public void lockDice(int index){
        Dice dice = dices.get(index);
        if (dice.isLocked()){
            dice.unlock();
        } else {
            dice.lock();
        }
    }

    public boolean[] diceState(){
        boolean[] states = new boolean[4];
        for (int i = 0; i < dices.size(); i++){
            states[i] = dices.get(i).isLocked();
        }
        return states;
    }

    public String[] diceValues(){
        String[] values = new String[4];
        for (int i = 0; i < dices.size(); i++){
            values[i] = dices.get(i).getValue();
        }
        return values;
    }

    public Bandit getBandit(int gangIndex, int banditIndex) {
        return gangs.get(gangIndex).getBandit(banditIndex);
    }

    public String getGangMoney(int i){
        return gangs.get(i).getMoney() + "$";
    }

    public String getGangName(int i) {
        return gangs.get(i).getName();
    }

    public void rollDice(){
        if (gameState.equals(GameState.DICE_ROLL)){
            if (rollCount > 3){
                presenter.displayMessage("You can only roll the dices 3 times !");
                setGameState(GameState.CHOOSE_ACTION);
            } else {
                rollDiceOnce();
                presenter.updateUI();
                rollCount++;
                if (rollCount > 3){
                    setGameState(GameState.CHOOSE_ACTION);
                }
            }
        } else {
            //presenter.displayMessage("You cannot roll the dices now !");
        }
    }

    public void stopRollingDice(){
        setGameState(GameState.CHOOSE_ACTION);
    }

    public void playAction(String characterAbility){
        if (gameState.equals(GameState.OLESON_MODE)){
            boolean isOK = makeUnclePatAction(characterAbility);
            if (isOK){
                presenter.updateUI();
                if (nbActionsForRound > 0){
                    setGameState(GameState.CHOOSE_ACTION);
                } else {
                    setGameState(GameState.CHANGE_PLAYER);
                }
            }
        } else if (gameState.equals(GameState.CHOOSE_ACTION)){
            if (nbActionsForRound > 0){
                if (playCharacter(Ability.valueOf(characterAbility))){
                    nbActionsForRound--;
                } else {
                    presenter.displayMessage("You cannot do that action !");
                }
                if (nbActionsForRound <= 0){
                    if (currentGangIndex == 0){
                        presenter.displayMessage("You are done with your actions");
                    } else {
                        presenter.displayMessage("The computer is done with its actions");
                    }
                    setGameState(GameState.CHANGE_PLAYER);
                }
            } else {
                presenter.displayMessage("You cannot do any actions");
                setGameState(GameState.CHANGE_PLAYER);
            }
        }
    }

    // private methods

    private String getCurrentGangName(){
        return gangs.get(currentGangIndex).getName();
    }

    private boolean makeUnclePatAction(String charachterAbility) {
        Gang currentGang = gangs.get(currentGangIndex);
        Ability ability = Ability.valueOf(charachterAbility);
        nbOfEvasion++;
        boolean isOkay = true;
        if (nbOfEvasion <= 2){
            isOkay = currentGang.freeBandit(ability, false);
        } else if (nbOfEvasion == 3){
            isOkay = currentGang.freeBandit(ability, true);
        }
        if (!isOkay){
            nbOfEvasion--;
            return false;
        } else {
            return true;
        }
    }

    private boolean isWon() {
        Gang richerGang = getRicherGang();
        if (richerGang == null) return false;
        return richerGang.hasEverybodyFreed();
    }

    private void reinit() {
        nbOfBankRobbery = 0;
        nbOfAttacks = 0;
        nbOfEvasion = 0;
    }

    private void switchPlayer() {
        if (currentGangIndex == 0){
            currentGangIndex = 1;
        } else {
            currentGangIndex = 0;
        }
        if (gangs.get(currentGangIndex).isComputer()){
            playAsComputer();
        }
    }

    private Gang createRedDamnationGang(){
        Bandit boss = new Bandit("Boss", Ability.BOSS);
        Bandit lady = new Bandit("Lady", Ability.LADY);
        Bandit bad = new Bandit("Bad", Ability.BAD);
        Bandit ugly = new Bandit("Ugly", Ability.UGLY);
        Bandit brain = new Bandit("Brain", Ability.BRAIN);
        List<Bandit> bandits = new ArrayList<>(Arrays.asList(boss, lady, bad, ugly, brain));
        return new Gang("Red Damnation", bandits, 50, false, this);
    }

    private Gang createLosLibertadoresGang(){
        Bandit boss = new Bandit("Boss", Ability.BOSS);
        Bandit lady = new Bandit("Lady", Ability.LADY);
        Bandit bad = new Bandit("Bad", Ability.BAD);
        Bandit ugly = new Bandit("Ugly", Ability.UGLY);
        Bandit brain = new Bandit("Brain", Ability.BRAIN);
        List<Bandit> bandits = new ArrayList<>(Arrays.asList(boss, lady, bad, ugly, brain));
        return new Gang("Los Libertadores", bandits, 50, false, this);
    }

    private Gang createOlesonGang(){
        Bandit boss = new Bandit("Boss", Ability.BOSS);
        Bandit lady = new Bandit("Lady", Ability.LADY);
        Bandit bad = new Bandit("Bad", Ability.BAD);
        Bandit ugly = new Bandit("Ugly", Ability.UGLY);
        Bandit brain = new Bandit("Brain", Ability.BRAIN);
        List<Bandit> bandits = new ArrayList<>(Arrays.asList(boss, lady, bad, ugly, brain));
        return new Gang("Oleson", bandits, 50, false, this);
    }

    private void unlockAllDices() {
        for (Dice dice: dices){
            dice.unlock();
        }
    }

    private int getNumberOfAction(){
        int n = 0;
        for (Dice dice: dices){
            if (dice.getValue().equals("*")){
                n++;
            }
        }
        return n;
    }

    private boolean playCharacter(Ability ability) {
        if (!Arrays.asList(diceValues()).contains(String.valueOf(ability.getValue()))){
            System.out.println("not containing");
            return false;
        }
        Gang currentGang = gangs.get(currentGangIndex);
        String action = currentGang.makeBanditsAct(ability, 1);
        if (action == null){
            return false;
        } else {
            return handleActions(action, ability, currentGang);
        }
    }

    private void rollDiceOnce(){
        for (Dice dice: dices){
            int random = (int)(Math.random() * 6 + 1);
            String face = dice_faces[random - 1];
            dice.setValue(face);
        }
    }

    private Gang getRicherGang() {
        if (gangs.get(0).getMoney() > gangs.get(1).getMoney()){
            return gangs.get(0);
        }
        if (gangs.get(0).getMoney() < gangs.get(1).getMoney()){
            return gangs.get(1);
        }
        return null;
    }

    private void playAsComputer() {
        aiThrowDice();
        unlockAllDices();
        presenter.updateUI();
        aiHandleActions();
        switchPlayer();

    }

    private void aiThrowDice(){
        for (int i = 0; i < 2; i++){
            rollDiceOnce();
            saveActions();
        }
        rollDiceOnce();
    }

    private void saveActions() {
        for (int i = 0; i < dices.size(); i++){
            if (dices.get(i).getValue().equals("*")){
                lockDice(i);
            }
        }
    }

    private void aiHandleActions(){
        int nbActions = getNumberOfAction();
        if (nbActions == 0) return;
        Gang currentGang = gangs.get(1);
        List<Bandit> activeBandits = currentGang.getActiveBandit(dices);
        while (nbActions > 0){
            if (activeBandits.size() == 1){
                playCharacter(activeBandits.get(0).getAbility());
            } else {
                Bandit bestOption = currentGang.getBestOptionsAI(activeBandits, gangs.get(0));
                playCharacter(bestOption.getAbility());
            }
            nbActions--;
        }
    }

    private boolean handleActions(String action, Ability ability, Gang currentGang) {
        Bandit bandit = currentGang.getBanditByAbility(ability);
        switch (action){
            case "NONE": case "FREE":
                presenter.updateUI();
                break;
            case "Los libertadores":
                robTheBank(currentGang);
                presenter.updateMoneyCount();
                break;
            case "Oleson":
                setGameState(GameState.OLESON_MODE);
                break;
            case "Red Damnation":
                brainAttack();
                presenter.updateMoneyCount();
                break;
            case "BOSS": case "LADY": case "BAD": case "UGLY":
                attack(bandit);
                presenter.updateMoneyCount();
                break;
            default:
                return false;
        }
        return true;
    }

    private void robTheBank(Gang currentGang) {
        nbOfBankRobbery++;
        if (nbOfBankRobbery == 1){
            takeMoneyFromBank(2, currentGang);
        } else if (nbOfBankRobbery == 2){
            takeMoneyFromBank(3, currentGang);
        } else if (nbOfBankRobbery == 3){
            takeMoneyFromBank(4, currentGang);
        }
    }

    private void takeMoneyFromBank(int i, Gang currentGang) {
        if (bank >= i){
            currentGang.robBank(i);
            bank -= i;
        } else {
            currentGang.robBank(bank);
            bank = 0;
        }
    }

    private void brainAttack() {
        Gang ennemy;
        if (currentGangIndex == 0){
            ennemy = gangs.get(1);
        } else {
            ennemy = gangs.get(0);
        }
        nbOfAttacks++;
        if (nbOfAttacks == 1){
            ennemy.loseMoney(1);
            bank += 1;
        } else if (nbOfAttacks == 2){
            ennemy.loseMoney(1);
            bank += 1;
        } else if (nbOfAttacks == 3){
            ennemy.loseMoney(8);
            bank += 8;
        }
    }

    private void attack(Bandit bandit) {
        Gang ennemy;
        if (currentGangIndex == 0){
            ennemy = gangs.get(1);
        } else {
            ennemy = gangs.get(0);
        }
        Ability ability = bandit.getAbility();
        if (ennemy.canBeAttackedByBandit(ability)){
            ennemy.loseMoney(bandit.getAbility().getValue());
            bank += bandit.getAbility().getValue();
        }
    }

    private void setGameState(GameState newState){
        gameState = newState;
        if (gameState.equals(GameState.CHANGE_PLAYER)){
            boolean isWon = isWon();
            if (isWon){
                presenter.displayWin(getCurrentGangName());
            } else {
                nbActionsForRound = 0;
                reinit();
                switchPlayer();
                setGameState(GameState.DICE_ROLL);
            }
        } else if (gameState.equals(GameState.CHOOSE_ACTION)){
            rollCount = 1;
            unlockAllDices();
            presenter.updateUI();
            nbActionsForRound = getNumberOfAction();
            if (nbActionsForRound == 0 || nbActionsForRound == 4){
                presenter.displayMessage("You cannot do any actions... Next player");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setGameState(GameState.CHANGE_PLAYER);
                    }
                }, 2000);
            } else {
                presenter.displayMessage("You can do " + nbActionsForRound + " actions");
            }
        } else if (gameState.equals(GameState.OLESON_MODE)){
            presenter.displayMessage("Pick a bandit that will be affected by Uncle Pat");
        }
    }
}

