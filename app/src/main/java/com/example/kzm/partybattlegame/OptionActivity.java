package com.example.kzm.partybattlegame;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OptionActivity extends AppCompatActivity {
    String message;
    String name[]=new String[4],plus,minus;
    String viewname[]=new String[4],specialty[]=new String[4],resist[]=new String[4];
    int prepare = 0;
    int chara[]=new int[4],Lv[]=new int[4];
    int HPMAX[]=new int[4],MPMAX[]=new int[4];
    int exp[]=new int[4],explimit[]=new int[4];
    int n,k;
    double HP[]=new double[4],MP[]=new double[4];
    double atk[]=new double[4],mtk[]=new double[4];
    double def[]=new double[4],mef[]=new double[4];
    double spd[]=new double[4],acc[]=new double[4],eva[]=new double[4];
    double pHP,pMP,patk,pmtk,pdef,pmef,pspd,pacc,peva;
    boolean charaset[]=new boolean[4],next;
    double cor[][]={{1,1,1,1,1,1,1,1,1},{1,1,1,1,1,1,1,1,1},{1,1,1,1,1,1,1,1,1},{1,1,1,1,1,1,1,1,1}};
    double pcor=1.1,mcor=0.9;
    TextView tv,tv2;
    Button bt,bt2,bt3,bt4;
    Intent i;
    MyOpenHelper hp;
    SQLiteDatabase db;
    Cursor c;
    private MainView myView;
    Commons commons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        hp = new MyOpenHelper(this);
        db = hp.getWritableDatabase();
        myView = findViewById(R.id.back4).findViewById(R.id.view);
        commons = (Commons) this.getApplication();
        commons.backinit(myView);
        commons.makebutton();
        tv = findViewById(R.id.optiontext);
        tv.setText("オプションメニュー");
        final LinearLayout layout = findViewById(R.id.maxlayout);
        i = this.getIntent();
        int lock = i.getIntExtra("level",1);
        bt = (Button) findViewById(R.id.credit);
        bt.setText("クレジット");
        bt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                i = new Intent(OptionActivity.this, CreditActivity.class);
                startActivity(i);
            }
        });
        bt2 = (Button) findViewById(R.id.maxbattle);
        bt2.setText("Max");
        /*if(lock<4) bt3.setEnabled(false);
        else bt3.setEnabled(true);*/
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(prepare==0 && charaset[0]){
                    prepare++;
                    levelup();
                    layout.removeAllViews();
                    tv2 = new TextView(OptionActivity.this);
                    tv2.setText("もう一度押すと最高レベルでの戦いとなります\nそれでも行きますか？");
                    layout.addView(tv2);
                    tv2 = new TextView(OptionActivity.this);
                    tv2.setText("↓現在のパーティー↓");
                    tv2.setGravity(Gravity.CENTER);
                    layout.addView(tv2);
                    for(k=0;k<4;k++) {
                        if(charaset[k]){
                            tv2 = new TextView(OptionActivity.this);
                            tv2.setText(name[k] + " Lv" + Lv[k] + "\nHP:" + (int) HP[k] + " MP:" + (int) MP[k] + " 攻撃:" + (int) atk[k] + " 魔力:" + (int) mtk[k]
                                    + " 防御:" + (int) def[k] + "\n魔防:" + (int) mef[k] + " 速さ:" + (int) spd[k] + " 命中:" + (int) acc[k] + " 回避:" + (int) eva[k]);
                            tv2.setGravity(Gravity.CENTER);
                            layout.addView(tv2);
                        }
                    }
                } else if(prepare==1){
                    prepare=0;
                    layout.removeAllViews();
                    i = new Intent(OptionActivity.this, BattleActivity.class);
                    for(k=0;k<4;k++){
                        if(charaset[k]) {
                            HPMAX[k] = (int) HP[k];
                            MPMAX[k] = (int) MP[k];
                        }
                    }
                    i.putExtra("level",100);
                    i.putExtra("stage",6);
                    i.putExtra("set",charaset);
                    i.putExtra("chara",chara);
                    i.putExtra("viewname",viewname);
                    i.putExtra("Lv",Lv);
                    i.putExtra("HP", HP);
                    i.putExtra("HPMAX",HP);
                    i.putExtra("MP", MP);
                    i.putExtra("MPMAX",MP);
                    i.putExtra("ATK",atk);
                    i.putExtra("MTK",mtk);
                    i.putExtra("DEF",def);
                    i.putExtra("MEF",mef);
                    i.putExtra("SPD",spd);
                    i.putExtra("ACC",acc);
                    i.putExtra("EVA",eva);
                    i.putExtra("specialty",specialty);
                    i.putExtra("resist",resist);
                    startActivity(i);
                } else if(!charaset[0]){
                    layout.removeAllViews();
                    tv2 = new TextView(OptionActivity.this);
                    tv2.setText("パーティーがセットされていません");
                    tv2.setGravity(Gravity.CENTER);
                    layout.addView(tv2);
                }
            }
        });
        bt3 = findViewById(R.id.reference);
        bt3.setText("辞典(未実装)");
        bt3.setEnabled(false);
        bt4 = findViewById(R.id.notification);
        bt4.setText("リリース情報");
        bt4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                i = new Intent(OptionActivity.this, InformationActivity.class);
                startActivity(i);
            }
        });
        Button bty =findViewById(R.id.back4).findViewById(R.id.battle);
        bty.setOnClickListener(commons.MM);
        Button btx =findViewById(R.id.back4).findViewById(R.id.characters);
        btx.setOnClickListener(commons.MC);
        Button btz =findViewById(R.id.back4).findViewById(R.id.additions);
        btz.setOnClickListener(commons.MA);
        Button bta =findViewById(R.id.back4).findViewById(R.id.options);
    }

    protected void onResume(){
        super.onResume();
        commons.datareception(chara,name,viewname,Lv,HP,MP,atk,mtk,def,mef,spd,acc,eva,specialty,resist,exp,explimit,charaset);
    }

    public void levelup(){
        for(k=0;k<4;k++) {
            if (charaset[k]) {
                statustable(k);
                datacor(plus,minus);
                HP[k]=HP[k]+pHP*(100-Lv[k])*cor[k][0];
                MP[k]=MP[k]+pMP*(100-Lv[k])*cor[k][1];
                atk[k]=atk[k]+patk*(100-Lv[k])*cor[k][2];
                mtk[k]=mtk[k]+pmtk*(100-Lv[k])*cor[k][3];
                def[k]=def[k]+pdef*(100-Lv[k])*cor[k][4];
                mef[k]=mef[k]+pmef*(100-Lv[k])*cor[k][5];
                spd[k]=spd[k]+pspd*(100-Lv[k])*cor[k][6];
                acc[k]=acc[k]+pacc*(100-Lv[k])*cor[k][7];
                eva[k]=eva[k]+peva*(100-Lv[k])*cor[k][8];
                Lv[k]=100;
            }
        }
    }

    public void statustable(int s) {
        c = db.query("growtable", new String[]{"code", "HP", "MP", "ATK", "MTK", "DEF", "MEF", "SPD", "ACC", "EVA", "plus", "minus"}, null, null, null, null, null);
        next = c.moveToFirst();
        while (next) {
            n = c.getInt(0);
            if (chara[s] == n) {
                pHP = c.getDouble(1);
                pMP = c.getDouble(2);
                patk = c.getDouble(3);
                pmtk = c.getDouble(4);
                pdef = c.getDouble(5);
                pmef = c.getDouble(6);
                pspd = c.getDouble(7);
                pacc = c.getDouble(8);
                peva = c.getDouble(9);
                plus = c.getString(10);
                minus = c.getString(11);
                break;
            }
            next = c.moveToNext();
        }
        if(s==3) c.close();
    }

    public void datacor(String plus,String minus){
        switch (plus){
            case "HP":
                cor[k][0] = pcor;
                break;
            case "MP":
                cor[k][1] = pcor;
                break;
            case "ATK":
                cor[k][2] = pcor;
                break;
            case "MTK":
                cor[k][3] = pcor;
                break;
            case "DEF":
                cor[k][4] = pcor;
                break;
            case "MEF":
                cor[k][5] = pcor;
                break;
            case "SPD":
                cor[k][6] = pcor;
                break;
            case "ACC":
                cor[k][7] = pcor;
                break;
            case "EVA":
                cor[k][8] = pcor;
                break;
            default:
                break;
        }
        switch (minus){
            case "HP":
                cor[k][0] = mcor;
                break;
            case "MP":
                cor[k][1] = mcor;
                break;
            case "ATK":
                cor[k][2] = mcor;
                break;
            case "MTK":
                cor[k][3] = mcor;
                break;
            case "DEF":
                cor[k][4] = mcor;
                break;
            case "MEF":
                cor[k][5] = mcor;
                break;
            case "SPD":
                cor[k][6] = mcor;
                break;
            case "ACC":
                cor[k][7] = mcor;
                break;
            case "EVA":
                cor[k][8] = mcor;
                break;
            default:
                break;
        }
    }


    @Override
    public void onBackPressed() {
    }

}
