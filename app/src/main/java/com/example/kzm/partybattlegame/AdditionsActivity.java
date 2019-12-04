package com.example.kzm.partybattlegame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AdditionsActivity extends AppCompatActivity {
    LinearLayout layout;
    Intent i;
    private MainView myView;
    private Commons commons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additions);
        TextView tv = findViewById(R.id.additionstext);
        tv.setText("データ追加メニュー");
        layout = findViewById(R.id.addlayout);
        myView = findViewById(R.id.back3).findViewById(R.id.view);
        commons = (Commons) this.getApplication();
        commons.backinit(myView);
        commons.makebutton();
        Button AC = findViewById(R.id.moveac);
        AC.setText("キャラ追加");
        AC.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                i = new Intent(AdditionsActivity.this,AddCharacter.class);
                i.putExtra("allchara",commons.getall());
                i.putExtra("expmul",commons.getexpmul());
                startActivity(i);
            }
        });
        Button AA = findViewById(R.id.moveaa);
        AA.setText("アビリティ追加");
        AA.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                i = new Intent(AdditionsActivity.this,AddAbility.class);
                startActivity(i);
            }
        });
        Button AL = findViewById(R.id.moveal);
        AL.setText("アビリティをキャラに覚えさせる");
        AL.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                i = new Intent(AdditionsActivity.this,AddLearning.class);
                startActivity(i);
            }
        });
        Button RM = findViewById(R.id.moverm);
        RM.setText("データ削除");
        RM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = new Intent(AdditionsActivity.this, RemoveActivity.class);
                startActivity(i);
            }
        });;
        Button bty =findViewById(R.id.back3).findViewById(R.id.battle);
        bty.setOnClickListener(commons.MM);
        Button btx =findViewById(R.id.back3).findViewById(R.id.characters);
        btx.setOnClickListener(commons.MC);
        Button btz =findViewById(R.id.back3).findViewById(R.id.additions);
        Button bta =findViewById(R.id.back3).findViewById(R.id.options);
        bta.setOnClickListener(commons.MO);
    }

    @Override
    public void onBackPressed() {
    }

}
