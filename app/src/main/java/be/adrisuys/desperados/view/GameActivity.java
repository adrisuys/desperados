package be.adrisuys.desperados.view;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.List;

import be.adrisuys.desperados.Presenter;
import be.adrisuys.desperados.R;
import be.adrisuys.desperados.models.Ability;
import be.adrisuys.desperados.models.Game;

public class GameActivity extends AppCompatActivity implements GameViewInterface {

    private Game game;
    private Presenter presenter;
    //UI
    private Button bankUI, gangAmoney, gangBmoney;
    private ImageView dice1UI, dice2UI, dice3UI, dice4UI;
    private ImageView brainAimg, uglyAimg, badAimg, ladyAimg, bossAimg;
    private ImageView brainBimg, uglyBimg, badBimg, ladyBimg, bossBimg;
    private TextView brainAName, uglyAName, badAName, ladyAName, bossAName;
    private TextView brainBName, uglyBName, badBName, ladyBName, bossBName;
    private LinearLayout highligthBoss, highligthLady, highligthBad, highligthUgly, highligthBrain;
    private ImageView aiActionOne, aiActionTwo, aiActionThree;

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
        String characterAbility = v.getTag().toString();
        presenter.onClickCharacter(characterAbility);
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
        dice1UI.setBackgroundResource(states[0] ? R.drawable.dice_locked : R.drawable.dice_unlocked);
        dice2UI.setBackgroundResource(states[1] ? R.drawable.dice_locked : R.drawable.dice_unlocked);
        dice3UI.setBackgroundResource(states[2] ? R.drawable.dice_locked : R.drawable.dice_unlocked);
        dice4UI.setBackgroundResource(states[3] ? R.drawable.dice_locked : R.drawable.dice_unlocked);
        updateJailtime();
        String[] values = presenter.getDiceValues();
        setFaces(dice1UI, values[0]);
        setFaces(dice2UI, values[1]);
        setFaces(dice3UI, values[2]);
        setFaces(dice4UI, values[3]);
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
        final Dialog dialog = new Dialog(GameActivity.this);
        dialog.setContentView(R.layout.custom_win_dialog);
        TextView tv = dialog.findViewById(R.id.text_message);
        tv.setText(currentGangName + " has won !");
        dialog.findViewById(R.id.btn_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                Intent i = new Intent(GameActivity.this, MainActivity.class);
                i.putExtra("onGameEnded", true);
                startActivity(i);
            }
        });
        dialog.show();
    }

    @Override
    public void resetHighlight(){
        LinearLayout[] layouts = new LinearLayout[]{highligthBoss, highligthLady, highligthBad, highligthUgly, highligthBrain};
        for (LinearLayout ll: layouts){
            ll.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void displayAiActions(List<Ability> aiChoice) {
        if (aiChoice.size() == 4){
            return;
        }
        ImageView[] imgs = new ImageView[]{aiActionOne, aiActionTwo, aiActionThree};
        for (int i = 0; i < aiChoice.size(); i++){
            switch (aiChoice.get(i)){
                case BRAIN:
                    imgs[i].setBackgroundResource(R.drawable.ai_action_brain);
                    break;
                case UGLY:
                    imgs[i].setBackgroundResource(R.drawable.ai_action_ugly);
                    break;
                case BAD:
                    imgs[i].setBackgroundResource(R.drawable.ai_action_bad);
                    break;
                case LADY:
                    imgs[i].setBackgroundResource(R.drawable.ai_action_lady);
                    break;
                case BOSS:
                    imgs[i].setBackgroundResource(R.drawable.ai_action_boss);
                    break;
            }
        }
        for (int i = aiChoice.size(); i < 3; i++){
            imgs[i].setBackgroundResource(0);
        }
    }

    @Override
    public void highlightInJail(int[] indexes) {
        System.out.println("view.highlightjail");
        LinearLayout[] layouts = new LinearLayout[]{highligthBoss, highligthLady, highligthBad, highligthUgly, highligthBrain};
        for (int i: indexes){
            layouts[i].setBackgroundColor(getResources().getColor(R.color.fluo_purple));
        }
    }

    @Override
    public void highlight(int[] index){
        LinearLayout[] layouts = new LinearLayout[]{highligthBoss, highligthLady, highligthBad, highligthUgly, highligthBrain};
        for (int i: index){
            layouts[i].setBackgroundColor(getResources().getColor(R.color.fluo));
        }
    }

    private void setUI() {
        bankUI = findViewById(R.id.bank);
        bankUI.setText(presenter.getBankAmount());
        gangAmoney = findViewById(R.id.gangAmoney);
        gangAmoney.setText(presenter.getGangMoney(0));
        gangBmoney = findViewById(R.id.gangBmoney);
        gangBmoney.setText(presenter.getGangMoney(1));

        dice1UI = findViewById(R.id.dice1);
        dice2UI = findViewById(R.id.dice2);
        dice3UI = findViewById(R.id.dice3);
        dice4UI = findViewById(R.id.dice4);

        aiActionOne = findViewById(R.id.ai_action_one);
        aiActionTwo = findViewById(R.id.ai_action_two);
        aiActionThree = findViewById(R.id.ai_action_three);

        highligthBoss = findViewById(R.id.highlight_boss);
        highligthLady = findViewById(R.id.highlight_lady);
        highligthBad = findViewById(R.id.highlight_bad);
        highligthUgly = findViewById(R.id.highlight_ugly);
        highligthBrain = findViewById(R.id.highlight_brain);
        resetHighlight();

        setImages();
        setNames();
        updateJailtime();

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
                bossAimg.setBackgroundResource(R.drawable.character_oleson_boss);
                ladyAimg.setBackgroundResource(R.drawable.character_oleson_lady);
                badAimg.setBackgroundResource(R.drawable.character_oleson_bad);
                uglyAimg.setBackgroundResource(R.drawable.character_oleson_ugly);
                brainAimg.setBackgroundResource(R.drawable.character_oleson_brain);
                break;
            case "Los Libertadores":
                bossAimg.setBackgroundResource(R.drawable.character_los_libertadores_boss);
                ladyAimg.setBackgroundResource(R.drawable.character_los_libertadores_lady);
                badAimg.setBackgroundResource(R.drawable.character_los_libertadores_bad);
                uglyAimg.setBackgroundResource(R.drawable.character_los_libertadores_ugly);
                brainAimg.setBackgroundResource(R.drawable.character_los_libertadores_brain);
                break;
            case "Red Damnation":
                bossAimg.setBackgroundResource(R.drawable.character_red_damnation_boss);
                ladyAimg.setBackgroundResource(R.drawable.character_red_damnation_lady);
                badAimg.setBackgroundResource(R.drawable.character_red_damnation_bad);
                uglyAimg.setBackgroundResource(R.drawable.character_red_damnation_ugly);
                brainAimg.setBackgroundResource(R.drawable.character_red_damnation_brain);
                break;
        }
    }

    private void setImagesForSecondGang() {
        switch(presenter.getGangName(1)){
            case "Oleson":
                bossBimg.setBackgroundResource(R.drawable.character_oleson_boss);
                ladyBimg.setBackgroundResource(R.drawable.character_oleson_lady);
                badBimg.setBackgroundResource(R.drawable.character_oleson_bad);
                uglyBimg.setBackgroundResource(R.drawable.character_oleson_ugly);
                brainBimg.setBackgroundResource(R.drawable.character_oleson_brain);
                break;
            case "Los Libertadores":
                bossBimg.setBackgroundResource(R.drawable.character_los_libertadores_boss);
                ladyBimg.setBackgroundResource(R.drawable.character_los_libertadores_lady);
                badBimg.setBackgroundResource(R.drawable.character_los_libertadores_bad);
                uglyBimg.setBackgroundResource(R.drawable.character_los_libertadores_ugly);
                brainBimg.setBackgroundResource(R.drawable.character_los_libertadores_brain);
                break;
            case "Red Damnation":
                bossBimg.setBackgroundResource(R.drawable.character_red_damnation_boss);
                ladyBimg.setBackgroundResource(R.drawable.character_red_damnation_lady);
                badBimg.setBackgroundResource(R.drawable.character_red_damnation_bad);
                uglyBimg.setBackgroundResource(R.drawable.character_red_damnation_ugly);
                brainBimg.setBackgroundResource(R.drawable.character_red_damnation_brain);
                break;
        }
    }

    private void setNames(){
        bossAName = findViewById(R.id.bossA_name);
        bossAName.setText(presenter.getBanditName(0, 0));
        ladyAName = findViewById(R.id.ladyA_name);
        ladyAName.setText(presenter.getBanditName(0, 1));
        badAName = findViewById(R.id.badA_name);
        badAName.setText(presenter.getBanditName(0, 2));
        uglyAName = findViewById(R.id.uglyA_name);
        uglyAName.setText(presenter.getBanditName(0, 3));
        brainAName = findViewById(R.id.brainA_name);
        brainAName.setText(presenter.getBanditName(0, 4));

        bossBName = findViewById(R.id.bossB_name);
        bossBName.setText(presenter.getBanditName(1, 0));
        ladyBName = findViewById(R.id.ladyB_name);
        ladyBName.setText(presenter.getBanditName(1, 1));
        badBName = findViewById(R.id.badB_name);
        badBName.setText(presenter.getBanditName(1, 2));
        uglyBName = findViewById(R.id.uglyB_name);
        uglyBName.setText(presenter.getBanditName(1, 3));
        brainBName = findViewById(R.id.brainB_name);
        brainBName.setText(presenter.getBanditName(1, 4));
    }

    private void updateJailtime() {
        updateJailBars(presenter.getBanditJailTime(0, 0), bossAimg);
        updateJailBars(presenter.getBanditJailTime(0, 1), ladyAimg);
        updateJailBars(presenter.getBanditJailTime(0, 2), badAimg);
        updateJailBars(presenter.getBanditJailTime(0, 3), uglyAimg);
        updateJailBars(presenter.getBanditJailTime(0, 4), brainAimg);
        updateJailBars(presenter.getBanditJailTime(1, 0), bossBimg);
        updateJailBars(presenter.getBanditJailTime(1, 1), ladyBimg);
        updateJailBars(presenter.getBanditJailTime(1, 2), badBimg);
        updateJailBars(presenter.getBanditJailTime(1, 3), uglyBimg);
        updateJailBars(presenter.getBanditJailTime(1, 4), brainBimg);
    }

    private void updateJailBars(String jailTime, ImageView iv){
        switch (jailTime){
            case "FREE":
                iv.setImageResource(0); break;
            case "1":
                iv.setImageResource(R.drawable.jail_bar_one); break;
            case "2":
                iv.setImageResource(R.drawable.jail_bar_two); break;
            case "3":
                iv.setImageResource(R.drawable.jail_bar_three); break;
            case "4":
                iv.setImageResource(R.drawable.jail_bar_four); break;
            case "5":
                iv.setImageResource(R.drawable.jail_bar_five); break;
            case "6":
                iv.setImageResource(R.drawable.jail_bar_six); break;
        }
    }

    private void backUp(){
        SharedPreferences sp = getApplicationContext().getSharedPreferences("Desperados", MODE_PRIVATE);
        SharedPreferences.Editor spEditor = sp.edit();
        Gson gson = new Gson();
        game.setPresenter(null);
        String json = gson.toJson(game);
        spEditor.putString("previous_game", json);
        spEditor.commit();
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

}
