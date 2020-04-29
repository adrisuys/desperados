package be.adrisuys.desperados.view;

public interface GameViewInterface {
    void updateUI();
    void displayMessage(String s);
    void updateMoneyCount(String bank, String gangMoney, String gangMoney1);
    void displayWin(String currentGangName);
}
