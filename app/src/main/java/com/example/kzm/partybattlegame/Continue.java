package com.example.kzm.partybattlegame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Continue extends AppCompatActivity {
    int explus,stage,level;
    boolean charaset[]=new boolean[4];
    TextView tv,tv2;
    Button bt,bt2;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continue);
        tv=findViewById(R.id.CorF);
        tv2=findViewById(R.id.CorFmessage);
        tv2.setText("『続ける』ボタンで次のバトル。負けるとこのレベルでの獲得経験値がリセットされます\n『終わる』ボタンでメイン画面に戻り、経験値を獲得します");
        i = this.getIntent();
        level = i.getIntExtra("level",1);
        stage = i.getIntExtra("stage",0);
        explus = i.getIntExtra("explus",0);
        String boss;
        if(stage==4) boss="次はボス戦となり、モンスターは必ず４体出てきます";
        else boss="";
        tv.setText("Continue or Finish?\n\n"+boss);
        tv.setGravity(Gravity.CENTER);
        bt=findViewById(R.id.continuebutton);
        bt.setText("続ける");
        bt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                i = new Intent(Continue.this, BattleActivity.class);
                i.putExtra("level",level);
                i.putExtra("stage",stage);
                i.putExtra("explus",explus);
                startActivity(i);
            }
        });
        bt2=findViewById(R.id.finishbutton);
        bt2.setText("終わる");
        bt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                i = new Intent(Continue.this, MainActivity.class);
                i.putExtra("set",charaset);
                i.putExtra("explus",explus);
                i.putExtra("battle",true);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}
