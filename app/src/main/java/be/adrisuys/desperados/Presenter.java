package be.adrisuys.desperados;


import java.util.List;

import be.adrisuys.desperados.models.Ability;
import be.adrisuys.desperados.models.Game;
import be.adrisuys.desperados.view.GameViewInterface;

public class Presenter {

    private GameViewInterface view;
    private Game game;

    public Presenter(GameViewInterface view, Game game){
        this.view = view;
        this.game = game;
        game.setPresenter(this);
    }

    public String getBankAmount() {
        return game.getBank();
    }

    public void onClickOnDice(int diceIndex) {
        game.lockDice(diceIndex);
        view.updateUI();
    }

    public boolean[] getDiceState(){
        return game.diceState();
    }

    public String[] getDiceValues(){
        return game.diceValues();
    }

    public void rollDices() {
        /*if (gameState.equals(GameState.DICE_ROLL)){
            if (rollCount > 3){
                view.displayMessage("You can only roll the dices 3 times !");
                setGameState(GameState.CHOOSE_ACTION);
            } else {
                game.rollDice();
                view.updateUI();
                rollCount++;
                if (rollCount > 3){
                    setGameState(GameState.CHOOSE_ACTION);
                }
            }
        } else {
            view.displayMessage("You cannot roll the dices now !");
        }*/
        game.rollDice();
    }

    public void stopRollingDices(){
        game.stopRollingDice();
    }

    public void onClickCharacter(String characterAbility) {
        game.playAction(characterAbility);
    }

    public String getBanditName(int gangIndex, int banditIndex){
        return game.getBandit(gangIndex, banditIndex).getName();
    }

    public String getBanditJailTime(int gangIndex, int banditIndex){
        return game.getBandit(gangIndex, banditIndex).getTimeInJail();
    }

    public void updateMoneyCount() {
        view.updateMoneyCount(game.getBank(), game.getGangMoney(0), game.getGangMoney(1));
    }

    public void updateUI() {
        view.updateUI();
    }

    public String getGangMoney(int i) {
        return game.getGangMoney(i);
    }

    public String getGangName(int i) {
        return game.getGangName(i);
    }

    public void displayMessage(String s) {
        view.displayMessage(s);
    }

    public void displayWin(String currentGangName) {
        view.displayWin(currentGangName);
    }

    public void highlight(int[] indexes) {
        view.highlight(indexes);
    }

    public void resetHighlight() {
        view.resetHighlight();
    }

    public void displayAiActions(List<Ability> aiChoices) {
        view.displayAiActions(aiChoices);
    }

    public void highlightInJail(int[] indexes) {
        System.out.println("presenter.highlightInJail");
        view.highlightInJail(indexes);
    }
}
