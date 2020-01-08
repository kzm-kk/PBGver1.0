package com.example.kzm.partybattlegame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Continue extends AppCompatActivity {
    String viewname[]=new String[4],specialty[]=new String[4],resist[]=new String[8];
    int chara[]=new int[4],Lv[]=new int[4];
    int HP[]=new int[4],MP[]=new int[4];
    int HPMAX[]=new int[4],MPMAX[]=new int[4];
    int explus,stage,level;
    double atk[]=new double[4],mtk[]=new double[4];
    double def[]=new double[4],mef[]=new double[4];
    double spd[]=new double[4],acc[]=new double[4],eva[]=new double[4];
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
        charaset = i.getBooleanArrayExtra("set");
        chara = i.getIntArrayExtra("chara");
        viewname = i.getStringArrayExtra("viewname");
        Lv = i.getIntArrayExtra("Lv");
        HP = i.getIntArrayExtra("HP");
        HPMAX = i.getIntArrayExtra("HPMAX");
        MP = i.getIntArrayExtra("MP");
        MPMAX = i.getIntArrayExtra("MPMAX");
        atk = i.getDoubleArrayExtra("ATK");
        mtk = i.getDoubleArrayExtra("MTK");
        def = i.getDoubleArrayExtra("DEF");
        mef = i.getDoubleArrayExtra("MEF");
        spd = i.getDoubleArrayExtra("SPD");
        acc = i.getDoubleArrayExtra("ACC");
        eva = i.getDoubleArrayExtra("EVA");
        specialty = i.getStringArrayExtra("specialty");
        resist = i.getStringArrayExtra("resist");
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
                i.putExtra("set",charaset);
                i.putExtra("chara",chara);
                i.putExtra("viewname",viewname);
                i.putExtra("Lv",Lv);
                i.putExtra("HP", HP);
                i.putExtra("HPMAX",HPMAX);
                i.putExtra("MP", MP);
                i.putExtra("MPMAX",MPMAX);
                i.putExtra("ATK",atk);
                i.putExtra("MTK",mtk);
                i.putExtra("DEF",def);
                i.putExtra("MEF",mef);
                i.putExtra("SPD",spd);
                i.putExtra("ACC",acc);
                i.putExtra("EVA",eva);
                i.putExtra("specialty",specialty);
                i.putExtra("resist",resist);
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
