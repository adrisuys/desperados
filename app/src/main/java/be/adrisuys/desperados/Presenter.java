package be.adrisuys.desperados;


import be.adrisuys.desperados.models.Bandit;
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

    public Bandit getBandit(int gangIndex, int banditIndex){
        return game.getBandit(gangIndex, banditIndex);
    }

    public void updateMoneyCount() {
        view.updateMoneyCount(game.getBank(), game.getGangMoney(0), game.getGangMoney(1));
    }

    public void updateUI() {
        view.updateUI();
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

    public void displayAiActions(String aiActions) {
        view.displayAiActions(aiActions);
    }

    public void highlight(int[] indexes) {
        view.highlight(indexes);
    }

    public void resetHighlight() {
        view.resetHighlight();
    }
}
