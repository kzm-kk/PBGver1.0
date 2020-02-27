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
    String plus,minus;
    int prepare = 0,n,k;
    boolean next;
    double grows[] = {1,1,1,1,1,1,1,1,1};
    TextView tv,tv2;
    Button bt,bt2,bt3,bt4,bt5;
    Intent i;
    MyOpenHelper hp;
    SQLiteDatabase db;
    Cursor c;
    private MainView myView;
    Commons commons;
    Commons.person[] person_shows_op = new Commons.person[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        hp = new MyOpenHelper(this);
        db = hp.getWritableDatabase();
        myView = findViewById(R.id.back4).findViewById(R.id.view);
        commons = (Commons) this.getApplication();
        for (k = 0; k < 4; k++) {
            person_shows_op[k] = new Commons.person();
        }
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
                if(prepare==0 && person_shows_op[0].getcharaset()){
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
                        if(person_shows_op[k].getcharaset()){
                            tv2 = new TextView(OptionActivity.this);
                            tv2.setText(person_shows_op[k].getdataString("name")
                                    + " Lv" + person_shows_op[k].getdataint("Lv")
                                    + "\nHP:" + (int)person_shows_op[k].getdatadouble("HP")
                                    + " MP:" + (int)person_shows_op[k].getdatadouble("MP")
                                    + " 攻撃:" + (int)person_shows_op[k].getdatadouble("atk")
                                    + " 魔攻:" + (int)person_shows_op[k].getdatadouble("mtk")
                                    + " 防御:" + (int)person_shows_op[k].getdatadouble("def")
                                    + "\n魔防:" + (int)person_shows_op[k].getdatadouble("mef")
                                    + " 速さ:" + (int)person_shows_op[k].getdatadouble("spd")
                                    + " 命中:" + (int)person_shows_op[k].getdatadouble("acc")
                                    + " 回避:" + (int)person_shows_op[k].getdatadouble("eva"));
                            tv2.setGravity(Gravity.CENTER);
                            layout.addView(tv2);
                        }
                    }
                } else if(prepare==1){
                    prepare=0;
                    layout.removeAllViews();
                    i = new Intent(OptionActivity.this, BattleActivity.class);
                    for(k=0;k<4;k++) {
                        if (person_shows_op[k].getcharaset()) {
                            Commons.person_data_inbattle[k].MAX_HPwrite(person_shows_op[k].getdatadouble("HP"));
                            Commons.person_data_inbattle[k].MAX_MPwrite(person_shows_op[k].getdatadouble("MP"));
                            Commons.person_data_inbattle[k].remaining_HPwrite((int) person_shows_op[k].getdatadouble("HP"));
                            Commons.person_data_inbattle[k].remaining_MPwrite((int) person_shows_op[k].getdatadouble("MP"));
                        }
                    }
                    i.putExtra("level",100);
                    i.putExtra("stage",6);
                    /*i.putExtra("set",charaset);
                    i.putExtra("chara",chara);
                    i.putExtra("viewname",viewname);
                    i.putExtra("Lv",Lv);
                    i.putExtra("HP", HPMAX);
                    i.putExtra("HPMAX",HPMAX);
                    i.putExtra("MP", MPMAX);
                    i.putExtra("MPMAX",MPMAX);
                    i.putExtra("ATK",atk);
                    i.putExtra("MTK",mtk);
                    i.putExtra("DEF",def);
                    i.putExtra("MEF",mef);
                    i.putExtra("SPD",spd);
                    i.putExtra("ACC",acc);
                    i.putExtra("EVA",eva);
                    i.putExtra("specialty",specialty);
                    i.putExtra("resist",resist);*/
                    startActivity(i);
                } else if(!person_shows_op[0].getcharaset()){
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
        bt5 = findViewById(R.id.DBrestore);
        bt5.setText("データベース管理");
        bt5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                i = new Intent(OptionActivity.this, DBhandling.class);
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
            for(int s=0;s<4;s++){
                person_shows_op[s].setcharaset(commons.person_data[s].getcharaset());
                person_shows_op[s].setdataint("chara", commons.person_data[s].getdataint("chara"));
                person_shows_op[s].setdataString("name", commons.person_data[s].getdataString("name"));
                person_shows_op[s].setdataString("viewname", commons.person_data[s].getdataString("viewname"));
                person_shows_op[s].setdataint("Lv", commons.person_data[s].getdataint("Lv"));
                person_shows_op[s].setdatadouble("HP", commons.person_data[s].getdatadouble("HP"));
                person_shows_op[s].setdatadouble("MP", commons.person_data[s].getdatadouble("MP"));
                person_shows_op[s].setdatadouble("atk", commons.person_data[s].getdatadouble("atk"));
                person_shows_op[s].setdatadouble("mtk", commons.person_data[s].getdatadouble("mtk"));
                person_shows_op[s].setdatadouble("def", commons.person_data[s].getdatadouble("def"));
                person_shows_op[s].setdatadouble("mef", commons.person_data[s].getdatadouble("mef"));
                person_shows_op[s].setdatadouble("spd", commons.person_data[s].getdatadouble("spd"));
                person_shows_op[s].setdatadouble("acc", commons.person_data[s].getdatadouble("acc"));
                person_shows_op[s].setdatadouble("eva", commons.person_data[s].getdatadouble("eva"));
                person_shows_op[s].setdataString("specialty", commons.person_data[s].getdataString("specialty"));
                person_shows_op[s].setdataString("resist", commons.person_data[s].getdataString("resist"));
                person_shows_op[s].setdataint("exp", commons.person_data[s].getdataint("exp"));
                person_shows_op[s].setdataint("explimit", commons.person_data[s].getdataint("explimit"));
            }
    }

    public void statusup(int num, int lvup){
        String parameters[] = {"HP", "MP", "atk", "mtk", "def", "mef", "spd", "acc", "eva"};
        for(int s=0;s<9;s++){
            double predata = person_shows_op[num].getdatadouble(parameters[s]);
            predata = predata + grows[s] * lvup;
            person_shows_op[num].setdatadouble(parameters[s], predata);
        }
    }

    public void levelup(){
        for(k=0;k<4;k++) {
            int Lv = person_shows_op[k].getdataint("Lv");
            if (person_shows_op[k].getcharaset()) {
                statustable(k);
                datacorretion(plus, 1.1);
                datacorretion(minus, 0.9);
                statusup(k, 100 - Lv);
                person_shows_op[k].setdataint("Lv", 100);
            }
        }
    }

    public void statustable(int tmp) {
        c = db.query("growtable", new String[]{"code", "HP", "MP", "ATK", "MTK", "DEF", "MEF", "SPD", "ACC", "EVA", "plus", "minus"}, null, null, null, null, null);
        next = c.moveToFirst();
        while (next) {
            n = c.getInt(0);
            if (person_shows_op[tmp].getdataint("chara") == n) {
                grows[0] = c.getDouble(1);
                grows[1] = c.getDouble(2);
                grows[2] = c.getDouble(3);
                grows[3] = c.getDouble(4);
                grows[4] = c.getDouble(5);
                grows[5] = c.getDouble(6);
                grows[6] = c.getDouble(7);
                grows[7] = c.getDouble(8);
                grows[8] = c.getDouble(9);
                plus = c.getString(10);
                minus = c.getString(11);
                break;
            }
            next = c.moveToNext();
        }
        if(tmp==3)c.close();
    }

    public void datacorretion(String corretion, double parameter){
        switch (corretion){
            case "HP":
                grows[0] *= parameter;
                break;
            case "MP":
                grows[1] *= parameter;
                break;
            case "ATK":
                grows[2] *= parameter;
                break;
            case "MTK":
                grows[3] *= parameter;
                break;
            case "DEF":
                grows[4] *= parameter;
                break;
            case "MEF":
                grows[5] *= parameter;
                break;
            case "SPD":
                grows[6] *= parameter;
                break;
            case "ACC":
                grows[7] *= parameter;
                break;
            case "EVA":
                grows[8] *= parameter;
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
    }

}