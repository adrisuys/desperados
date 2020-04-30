package be.adrisuys.desperados.view;

import java.util.List;

import be.adrisuys.desperados.models.Ability;
import be.adrisuys.desperados.models.Bandit;

public interface GameViewInterface {
    void updateUI();
    void displayMessage(String s);
    void updateMoneyCount(String bank, String gangMoney, String gangMoney1);
    void displayWin(String currentGangName);
    void highlight(int[] indexes);
    void resetHighlight();
    void displayAiActions(List<Ability> aiChoice);
    void highlightInJail(int[] indexes);
}
