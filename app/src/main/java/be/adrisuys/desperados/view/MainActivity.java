package be.adrisuys.desperados.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import be.adrisuys.desperados.R;
import be.adrisuys.desperados.models.Game;

public class MainActivity extends AppCompatActivity {

    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getIntent().getBooleanExtra("onGameEnded", false)){
            init();
        } else {
            retrieveSavedGame();
        }
    }

    private void init(){
        game = new Game();
        game.pickPlayingGangs();
        setUI();
    }

    private void retrieveSavedGame() {
        Gson gson = new Gson();
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        String json = sp.getString("previous_game", "");
        if (!json.equals("")){
            game = gson.fromJson(json, Game.class);
            displayDialogBox();
        } else {
            init();
        }
    }

    private void displayDialogBox() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.findViewById(R.id.btn_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                play();
            }
        });
        dialog.findViewById(R.id.btn_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                init();
            }
        });
        dialog.show();
    }

    private void setUI() {
        TextView yourGangName = findViewById(R.id.your_gang);
        yourGangName.setText(game.getOwnGangName());
        TextView computerGangName = findViewById(R.id.computer_gang);
        computerGangName.setText(game.getComputerGangName());
        LinearLayout topLinearLayout = findViewById(R.id.top_bg);
        LinearLayout bottomLinearLayout = findViewById(R.id.bottom_bg);
        setBg(topLinearLayout, game.getOwnGangName());
        setBg(bottomLinearLayout, game.getComputerGangName());
    }

    private void setBg(LinearLayout linearLayout, String name) {
        switch (name){
            case "Oleson":
                linearLayout.setBackgroundResource(R.drawable.farm);
                break;
            case "Los Libertadores":
                linearLayout.setBackgroundResource(R.drawable.desert);
                break;
            case "Red Damnation":
                linearLayout.setBackgroundResource(R.drawable.grand_canyon);
                break;
        }
    }

    public void onPlayClicked(View v){
        play();
    }

    public void play(){
        Intent i = new Intent(this, GameActivity.class);
        i.putExtra("game", game);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }
}
