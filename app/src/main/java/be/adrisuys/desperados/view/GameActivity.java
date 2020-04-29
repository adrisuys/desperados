package be.adrisuys.desperados.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import be.adrisuys.desperados.Presenter;
import be.adrisuys.desperados.R;
import be.adrisuys.desperados.models.Game;

public class GameActivity extends AppCompatActivity implements GameViewInterface {

    private Game game;
    private Presenter presenter;
    //UI
    private Button bankUI, gangAmoney, gangBmoney;
    private ImageView dice1UI;
    private ImageView dice2UI;
    private ImageView dice3UI;
    private ImageView dice4UI;

    private ImageView brainAimg, uglyAimg, badAimg, ladyAimg, bossAimg;
    private ImageView brainBimg, uglyBimg, badBimg, ladyBimg, bossBimg;
    private TextView brainAName, uglyAName, badAName, ladyAName, bossAName;
    private TextView brainBName, uglyBName, badBName, ladyBName, bossBName;
    private TextView brainAjail, uglyAjail, badAjail, ladyAjail, bossAjail;
    private TextView brainBjail, uglyBjail, badBjail, ladyBjail, bossBjail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        game = (Game) getIntent().getSerializableExtra("game");
        presenter = new Presenter(this, game);
        setUI();
    }

    public void onClickDice(View v){
        int diceIndex = Integer.parseInt(v.getTag().toString());
        presenter.onClickOnDice(diceIndex);
    }

    public void rollDices(View v){
        presenter.rollDices();
    }

    public void stopRollingDices(View v){
        presenter.stopRollingDices();
    }

    public void onClickCharacter(View v){
        String charachterAbility = v.getTag().toString();
        presenter.onClickCharacter(charachterAbility);
    }

    @Override
    public void onBackPressed() {
        backUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        backUp();
    }

    @Override
    public void updateUI() {
        boolean[] states = presenter.getDiceState();
        dice1UI.setBackgroundResource(states[0] ? R.drawable.locked_dice : R.drawable.unlocked_dice);
        dice2UI.setBackgroundResource(states[1] ? R.drawable.locked_dice : R.drawable.unlocked_dice);
        dice3UI.setBackgroundResource(states[2] ? R.drawable.locked_dice : R.drawable.unlocked_dice);
        dice4UI.setBackgroundResource(states[3] ? R.drawable.locked_dice : R.drawable.unlocked_dice);
        setJailTimes();
        String[] values = presenter.getDiceValues();
        setFaces(dice1UI, values[0]);
        setFaces(dice2UI, values[1]);
        setFaces(dice3UI, values[2]);
        setFaces(dice4UI, values[3]);
    }

    private void setFaces(ImageView diceImg, String value) {
        switch (value){
            case "*":
                diceImg.setImageResource(R.drawable.dice_face_action);
                break;
            case "2":
                diceImg.setImageResource(R.drawable.dice_face_brain);
                break;
            case "3":
                diceImg.setImageResource(R.drawable.dice_face_ugly);
                break;
            case "4":
                diceImg.setImageResource(R.drawable.dice_face_bad);
                break;
            case "5":
                diceImg.setImageResource(R.drawable.dice_face_lady);
                break;
            case "6":
                diceImg.setImageResource(R.drawable.dice_face_boss);
                break;
        }
    }

    @Override
    public void displayMessage(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateMoneyCount(String bank, String gangMoney, String gangMoney1) {
        bankUI.setText(bank);
        gangAmoney.setText(gangMoney);
        gangBmoney.setText(gangMoney1);
    }

    @Override
    public void displayWin(String currentGangName) {

    }

    private void setUI() {
        bankUI = findViewById(R.id.bank);
        bankUI.setText(presenter.getBankAmount());
        gangAmoney = findViewById(R.id.gangAmoney);
        gangAmoney.setText("50 $");
        gangBmoney = findViewById(R.id.gangBmoney);
        gangBmoney.setText("50 $");

        dice1UI = findViewById(R.id.dice1);
        dice2UI = findViewById(R.id.dice2);
        dice3UI = findViewById(R.id.dice3);
        dice4UI = findViewById(R.id.dice4);

        setImages();
        setNames();
        setJailTimes();

    }

    private void setImages() {
        brainAimg = findViewById(R.id.brain_A_pic);
        uglyAimg = findViewById(R.id.ugly_A_pic);
        badAimg = findViewById(R.id.bad_A_pic);
        ladyAimg = findViewById(R.id.lady_A_pic);
        bossAimg = findViewById(R.id.boss_A_pic);
        setImagesForFirstGang();
        brainBimg = findViewById(R.id.brain_B_pic);
        uglyBimg = findViewById(R.id.ugly_B_pic);
        badBimg = findViewById(R.id.bad_B_pic);
        ladyBimg = findViewById(R.id.lady_B_pic);
        bossBimg = findViewById(R.id.boss_B_pic);
        setImagesForSecondGang();
    }

    private void setImagesForFirstGang() {
        switch(presenter.getGangName(0).trim()){
            case "Oleson":
                bossAimg.setBackgroundResource(R.drawable.oleson_boss);
                ladyAimg.setBackgroundResource(R.drawable.oleson_lady);
                badAimg.setBackgroundResource(R.drawable.oleson_bad);
                uglyAimg.setBackgroundResource(R.drawable.oleson_ugly);
                brainAimg.setBackgroundResource(R.drawable.oleson_brain);
                break;
            case "Los Libertadores":
                bossAimg.setBackgroundResource(R.drawable.los_libertadores_boss);
                ladyAimg.setBackgroundResource(R.drawable.los_libertadores_lady);
                badAimg.setBackgroundResource(R.drawable.los_libertadores_bad);
                uglyAimg.setBackgroundResource(R.drawable.los_libertadores_ugly);
                brainAimg.setBackgroundResource(R.drawable.los_libertadores_brain);
                break;
            case "Red Damnation":
                bossAimg.setBackgroundResource(R.drawable.red_damnation_boss);
                ladyAimg.setBackgroundResource(R.drawable.red_damnation_lady);
                badAimg.setBackgroundResource(R.drawable.red_damnation_bad);
                uglyAimg.setBackgroundResource(R.drawable.red_damnation_ugly);
                brainAimg.setBackgroundResource(R.drawable.red_damnation_brain);
                break;
        }
        bossAimg.setImageResource(R.drawable.jail_bars);
        ladyAimg.setImageResource(R.drawable.jail_bars);
        badAimg.setImageResource(R.drawable.jail_bars);
        uglyAimg.setImageResource(R.drawable.jail_bars);
        brainAimg.setImageResource(R.drawable.jail_bars);
    }

    private void setImagesForSecondGang() {
        switch(presenter.getGangName(1)){
            case "Oleson":
                bossBimg.setBackgroundResource(R.drawable.oleson_boss);
                ladyBimg.setBackgroundResource(R.drawable.oleson_lady);
                badBimg.setBackgroundResource(R.drawable.oleson_bad);
                uglyBimg.setBackgroundResource(R.drawable.oleson_ugly);
                brainBimg.setBackgroundResource(R.drawable.oleson_brain);
                break;
            case "Los Libertadores":
                bossBimg.setBackgroundResource(R.drawable.los_libertadores_boss);
                ladyBimg.setBackgroundResource(R.drawable.los_libertadores_lady);
                badBimg.setBackgroundResource(R.drawable.los_libertadores_bad);
                uglyBimg.setBackgroundResource(R.drawable.los_libertadores_ugly);
                brainBimg.setBackgroundResource(R.drawable.los_libertadores_brain);
                break;
            case "Red Damnation":
                bossBimg.setBackgroundResource(R.drawable.red_damnation_boss);
                ladyBimg.setBackgroundResource(R.drawable.red_damnation_lady);
                badBimg.setBackgroundResource(R.drawable.red_damnation_bad);
                uglyBimg.setBackgroundResource(R.drawable.red_damnation_ugly);
                brainBimg.setBackgroundResource(R.drawable.red_damnation_brain);
                break;
        }
        bossBimg.setImageResource(R.drawable.jail_bars);
        ladyBimg.setImageResource(R.drawable.jail_bars);
        badBimg.setImageResource(R.drawable.jail_bars);
        uglyBimg.setImageResource(R.drawable.jail_bars);
        brainBimg.setImageResource(R.drawable.jail_bars);
    }

    private void setNames(){
        bossAName = findViewById(R.id.bossA_name);
        bossAName.setText(presenter.getBandit(0, 0).getName());
        ladyAName = findViewById(R.id.ladyA_name);
        ladyAName.setText(presenter.getBandit(0, 1).getName());
        badAName = findViewById(R.id.badA_name);
        badAName.setText(presenter.getBandit(0, 2).getName());
        uglyAName = findViewById(R.id.uglyA_name);
        uglyAName.setText(presenter.getBandit(0, 3).getName());
        brainAName = findViewById(R.id.brainA_name);
        brainAName.setText(presenter.getBandit(0, 4).getName());

        bossBName = findViewById(R.id.bossB_name);
        bossBName.setText(presenter.getBandit(1, 0).getName());
        ladyBName = findViewById(R.id.ladyB_name);
        ladyBName.setText(presenter.getBandit(1, 1).getName());
        badBName = findViewById(R.id.badB_name);
        badBName.setText(presenter.getBandit(1, 2).getName());
        uglyBName = findViewById(R.id.uglyB_name);
        uglyBName.setText(presenter.getBandit(1, 3).getName());
        brainBName = findViewById(R.id.brainB_name);
        brainBName.setText(presenter.getBandit(1, 4).getName());
    }

    private void setJailTimes(){
        bossAjail = findViewById(R.id.bossA_jail);
        bossAjail.setText(presenter.getBandit(0, 0).getTimeInJail());
        ladyAjail = findViewById(R.id.ladyA_jail);
        ladyAjail.setText(presenter.getBandit(0, 1).getTimeInJail());
        badAjail = findViewById(R.id.badA_jail);
        badAjail.setText(presenter.getBandit(0, 2).getTimeInJail());
        uglyAjail = findViewById(R.id.uglyA_jail);
        uglyAjail.setText(presenter.getBandit(0, 3).getTimeInJail());
        brainAjail = findViewById(R.id.brainA_jail);
        brainAjail.setText(presenter.getBandit(0, 4).getTimeInJail());

        bossBjail = findViewById(R.id.bossB_jail);
        bossBjail.setText(presenter.getBandit(1, 0).getTimeInJail());
        ladyBjail = findViewById(R.id.ladyB_jail);
        ladyBjail.setText(presenter.getBandit(1, 1).getTimeInJail());
        badBjail = findViewById(R.id.badB_jail);
        badBjail.setText(presenter.getBandit(1, 2).getTimeInJail());
        uglyBjail = findViewById(R.id.uglyB_jail);
        uglyBjail.setText(presenter.getBandit(1, 3).getTimeInJail());
        brainBjail = findViewById(R.id.brainB_jail);
        brainBjail.setText(presenter.getBandit(1, 4).getTimeInJail());

        updateBackground();
    }

    private void updateBackground() {
        bossAimg.setImageResource(presenter.getBandit(0, 0).getTimeInJail() == "FREE" ? 0 : R.drawable.jail_bars);
        ladyAimg.setImageResource(presenter.getBandit(0, 1).getTimeInJail() == "FREE" ? 0 : R.drawable.jail_bars);
        badAimg.setImageResource(presenter.getBandit(0, 2).getTimeInJail() == "FREE" ? 0 : R.drawable.jail_bars);
        uglyAimg.setImageResource(presenter.getBandit(0, 3).getTimeInJail() == "FREE" ? 0 : R.drawable.jail_bars);
        brainAimg.setImageResource(presenter.getBandit(0, 4).getTimeInJail() == "FREE" ? 0 : R.drawable.jail_bars);
        bossBimg.setImageResource(presenter.getBandit(1, 0).getTimeInJail() == "FREE" ? 0 : R.drawable.jail_bars);
        ladyBimg.setImageResource(presenter.getBandit(1, 1).getTimeInJail() == "FREE" ? 0 : R.drawable.jail_bars);
        badBimg.setImageResource(presenter.getBandit(1, 2).getTimeInJail() == "FREE" ? 0 : R.drawable.jail_bars);
        uglyBimg.setImageResource(presenter.getBandit(1, 3).getTimeInJail() == "FREE" ? 0 : R.drawable.jail_bars);
        brainBimg.setImageResource(presenter.getBandit(1, 4).getTimeInJail() == "FREE" ? 0 : R.drawable.jail_bars);
    }

    private void backUp(){
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor spEditor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(game);
        spEditor.putString("previous_game", json);
        spEditor.commit();
    }

}
