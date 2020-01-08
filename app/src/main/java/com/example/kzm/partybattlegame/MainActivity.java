package com.example.kzm.partybattlegame;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.AppLaunchChecker;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {
    String codename[]=new String[1], message,plus,minus;
    String name[]=new String[4],viewname[]=new String[4];
    String specialty[]=new String[4],resist[]=new String[4];
    int lock=1,maxlock=1,stage;
    int chara[]=new int[4],Lv[]=new int[4];
    int HPMAX[]=new int[4],MPMAX[]=new int[4];
    int exp[]=new int[4],explimit[]=new int[4];
    int n,k,plv=1,explus=0,expin,all;
    double HP[]=new double[4],MP[]=new double[4];
    double atk[]=new double[4],mtk[]=new double[4];
    double def[]=new double[4],mef[]=new double[4];
    double spd[]=new double[4],acc[]=new double[4],eva[]=new double[4];
    double pHP,pMP,patk,pmtk,pdef,pmef,pspd,pacc,peva,expchange,expmul=1.2;
    boolean charaset[]={false,false,false,false},next;
    TextView tv,tv2,tv3;
    Button bt,bt2,bt3,bt4,bt5;
    LinearLayout layout,Hlayout;
    Intent i;
    MyOpenHelper hp;
    SQLiteDatabase db;
    ContentValues cv;
    Cursor c;
    Spinner sp;
    Bitmap bitmap;
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    byte[] bytegazo = bos.toByteArray();
    private MainView myView;
    Commons commons;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hp = new MyOpenHelper(this);
        db = hp.getWritableDatabase();
        commons = (Commons) getApplication();
        if(AppLaunchChecker.hasStartedFromLauncher(this)){
            c = db.query("lastplay", new String[]{"one", "two", "three", "four", "level","chara"}, null, null, null, null, null);
            next = c.moveToFirst();
            while (next) {
                chara[0] = c.getInt(0);
                chara[1] = c.getInt(1);
                chara[2] = c.getInt(2);
                chara[3] = c.getInt(3);
                maxlock = c.getInt(4);
                all = c.getInt(5);
                next = c.moveToNext();
            }
            for (k = 0; k < 4; k++) {
                charaset[k]=false;
                lastdataset(k);
            }
            c.close();
            message = "『ステータス』ボタンを押して\nパーティーの編成状況を確認してください";
        } else {
            setmydata();
            setenemydata();
            setmyglows();
            allability();
            leveltoability();
            enemyability();
            setimage();
            all = 20;
            for(k=0;k<4;k++){
                chara[k]=-1*(k+1);
                charaset[k]=false;
            }
            message="『キャラ色々』ボタンを押して\nパーティーを編成してください";
        }
        AppLaunchChecker.onActivityCreate(this);
        myView = findViewById(R.id.back1).findViewById(R.id.view);
        commons.backinit(myView);
        commons.makebutton();
        layout=findViewById(R.id.message2);
        Hlayout=findViewById(R.id.status);
        stage=0;
        sp = (Spinner) findViewById(R.id.levelselect);
        tv = (TextView)findViewById(R.id.levelshow);
        tv.setText("レベルセレクト 現在の最高レベル:"+maxlock);
        tv.setGravity(Gravity.CENTER);
        tv2 = (TextView)findViewById(R.id.message);
        tv2.setText("始める前に"+message);
        tv2.setGravity(Gravity.CENTER);
        bt = (Button) findViewById(R.id.startbutton);
        bt.setText("バトル開始");
        bt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(commons.charaset[0])commons.datareception(chara, name, viewname, Lv, HP, MP, atk, mtk, def, mef, spd, acc, eva, specialty, resist, exp, explimit, charaset);
                if(charaset[0]) {
                    lock = sp.getSelectedItemPosition();
                    if(maxlock<lock) {
                        Toast.makeText(MainActivity.this,
                                "そのレベルへは行けません",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        if(lock>0) {
                            i = new Intent(MainActivity.this, BattleActivity.class);

                            setintentdata();
                            Toast.makeText(MainActivity.this,
                                    "レベル" + lock + "のステージへ向かいます",
                                    Toast.LENGTH_SHORT).show();
                            startActivityForResult(i, lock);
                        }
                    }
                }
            }
        });

        bt2 = (Button) findViewById(R.id.savebutton);
        bt2.setText("セーブ");
        bt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(commons.charaset[0])tv2.setText("ok "+commons.chara[0]+" "+chara[0]);
            }
        });
        bt3 = (Button) findViewById(R.id.statusbutton);
        bt3.setText("ステータス");
        bt3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(commons.charaset[0])commons.datareception(chara, name, viewname, Lv, HP, MP, atk, mtk, def, mef, spd, acc, eva, specialty, resist, exp, explimit, charaset);
                layout.removeAllViews();
                Hlayout.removeAllViews();
                if(charaset[0]==false) {
                    tv3 = new TextView(MainActivity.this);
                    tv3.setText("パーティーがセットされていません");
                    tv3.setGravity(Gravity.CENTER);
                    layout.addView(tv3);
                } else {
                    tv3 = new TextView(MainActivity.this);
                    tv3.setText("     ");
                    tv3.setGravity(Gravity.CENTER);
                    Hlayout.addView(tv3);
                    for(k=0;k<4;k++) {
                        if(charaset[k]){
                            tv3 = new TextView(MainActivity.this);
                            tv3.setText(viewname[k] + "\nLv" + Lv[k] +"\nHP:" + (int)HP[k] + "\nMP:" + (int)MP[k]
                                    + "\n攻撃:" + (int)atk[k] + "\n魔攻:" + (int)mtk[k] + "\n防御:" + (int)def[k]
                                    + "\n魔防:" + (int)mef[k] + "\n速さ:" + (int)spd[k] + "\n命中:" + (int)acc[k]
                                    + "\n回避:" + (int)eva[k] + "\nEXP " + exp[k] + "/" + explimit[k]);
                            tv3.setGravity(Gravity.CENTER);
                            Hlayout.addView(tv3);
                        }
                    }
                }
            }
        });
        Button bty =findViewById(R.id.back1).findViewById(R.id.battle);
        bt4 =findViewById(R.id.back1).findViewById(R.id.characters);
        bt4.setOnClickListener(commons.MC);
        Button btz =findViewById(R.id.back1).findViewById(R.id.additions);
        btz.setOnClickListener(commons.MA);
        bt5 = findViewById(R.id.back1).findViewById(R.id.options);
        bt5.setOnClickListener(commons.MO);
    }

    @Override
    protected void onNewIntent(Intent intent){
        this.setIntent(intent);
        stage=0;
        maxlock = Math.max(maxlock,intent.getIntExtra("level", 1));
        commons.setmaxlock(maxlock);
        tv.setText("レベルセレクト 現在の最高レベル:"+maxlock);
        tv2.setText("");
        charaset = intent.getBooleanArrayExtra("set");
        explus = intent.getIntExtra("explus", 0);
        boolean battle = intent.getBooleanExtra("battle",false);
        if(battle)levelup();
        if(intent.getBooleanExtra("movemain",false)){
            commons.datareception(chara, name, viewname, Lv, HP, MP, atk, mtk, def, mef, spd, acc, eva, specialty, resist, exp, explimit, charaset);
            all = commons.getall();
        }
        save();
    }

    protected void onResume(){
        super.onResume();
        if(charaset[0]) save();
        //commons.datareception(chara, name, viewname, Lv, HP, MP, atk, mtk, def, mef, spd, acc, eva, specialty, resist, exp, explimit, charaset);
    }

    protected void onPause(){
        super.onPause();
        commons.setmaxlock(maxlock);
        commons.setexpmul(expmul);
        commons.setall(all);
        commons.datatransmission(chara,name,viewname,Lv,HP,MP,atk,mtk,def,mef,spd,acc,eva,specialty,resist,exp,explimit,charaset);
        layout.removeAllViews();
        Hlayout.removeAllViews();
    }

    public void setintentdata(){
        for(k=0;k<4;k++){
            if(charaset[k]) {
                HPMAX[k] = (int) HP[k];
                MPMAX[k] = (int) MP[k];
            }
        }
        i.putExtra("level",lock);
        i.putExtra("stage",stage);
        i.putExtra("set",charaset);
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
        i.putExtra("resist",resist);
        i.putExtra("specialty",specialty);
        i.putExtra("explus",0);
    }

    public void save(){
        for(k=0;k<4;k++) {
            cv = new ContentValues();
            codename[0] = name[k];
            cv.put("Lv", Lv[k]);
            cv.put("HP", HP[k]);
            cv.put("MP", MP[k]);
            cv.put("ATK", atk[k]);
            cv.put("MTK", mtk[k]);
            cv.put("DEF", def[k]);
            cv.put("MEF", mef[k]);
            cv.put("SPD", spd[k]);
            cv.put("ACC", acc[k]);
            cv.put("EVA", eva[k]);
            cv.put("EXP", exp[k]);
            cv.put("EXPlimit", explimit[k]);
            db.update("person", cv, "name = ?", codename);
        }
        cv = new ContentValues();
        cv.put("one", chara[0]);
        cv.put("two", chara[1]);
        cv.put("three", chara[2]);
        cv.put("four", chara[3]);
        cv.put("level", maxlock);
        cv.put("chara",all);
        db.update("lastplay", cv, "",null);
    }


    public void levelup(){
        for(k=0;k<4;k++) {
            if (charaset[k]) {
                plv = Lv[k];
                expin = explus;
                while (expin != 0) {
                    exp[k]++;
                    if (exp[k] >= explimit[k]) {
                        exp[k] = exp[k] - explimit[k];
                        expchange = (double) explimit[k] * expmul;
                        explimit[k] = (int) expchange;
                        statustable(k);
                        datapluscor();
                        dataminuscor();
                        statusup(k);
                    }
                    expin--;
                }
                if (Lv[k] > plv) {
                    tv3 = new TextView(this);
                    tv3.setText("Level UP!!\n" + name[k] + " Lv" + plv + "→" + Lv[k]);
                    layout.addView(tv3);
                    tv3.setGravity(Gravity.CENTER);
                }
            }
        }
        commons.datatransmission(chara,name,viewname,Lv,HP,MP,atk,mtk,def,mef,spd,acc,eva,specialty,resist,exp,explimit,charaset);
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
        c.close();
    }

    public void statusup(int s){
        Lv[s]++;
        HP[s]=HP[s]+pHP;
        MP[s]=MP[s]+pMP;
        atk[s]=atk[s]+patk;
        mtk[s]=mtk[s]+pmtk;
        def[s]=def[s]+pdef;
        mef[s]=mef[s]+pmef;
        spd[s]=spd[s]+pspd;
        acc[s]=acc[s]+pacc;
        eva[s]=eva[s]+peva;
    }

    public void datapluscor(){
        double cor=1.1;
        switch (plus){
            case "HP":
                pHP=pHP*cor;
                break;
            case "MP":
                pMP=pMP*cor;
                break;
            case "ATK":
                patk=patk*cor;
                break;
            case "MTK":
                pmtk=pmtk*cor;
                break;
            case "DEF":
                pdef=pdef*cor;
                break;
            case "MEF":
                pdef=pdef*cor;
                break;
            case "SPD":
                pspd=pspd*cor;
                break;
            case "ACC":
                pacc=pacc*cor;
                break;
            case "EVA":
                peva=peva*cor;
                break;
            default:
                break;
        }
    }

    public void dataminuscor(){
        double cor=0.9;
        switch (minus){
            case "HP":
                pHP=pHP*cor;
                break;
            case "MP":
                pMP=pMP*cor;
                break;
            case "ATK":
                patk=patk*cor;
                break;
            case "MTK":
                pmtk=pmtk*cor;
                break;
            case "DEF":
                pdef=pdef*cor;
                break;
            case "MEF":
                pdef=pdef*cor;
                break;
            case "SPD":
                pspd=pspd*cor;
                break;
            case "ACC":
                pacc=pacc*cor;
                break;
            case "EVA":
                peva=peva*cor;
                break;
            default:
                break;
        }
    }

    public void lastdataset(int tmp){
        c = db.query("person", new String[]{"code", "name", "nameview", "Lv", "HP", "MP", "ATK", "MTK", "DEF", "MEF", "SPD", "ACC", "EVA", "specialty", "resist", "EXP", "EXPlimit"}, null, null, null, null, null);
        boolean next = c.moveToFirst();
        while (next) {
            n = c.getInt(0);
            if (chara[tmp] == n) {
                name[tmp] = c.getString(1);
                viewname[tmp] = c.getString(2);
                Lv[tmp] = c.getInt(3);
                HP[tmp] = c.getDouble(4);
                MP[tmp] = c.getDouble(5);
                atk[tmp] = c.getDouble(6);
                mtk[tmp] = c.getDouble(7);
                def[tmp] = c.getDouble(8);
                mef[tmp] = c.getDouble(9);
                spd[tmp] = c.getDouble(10);
                acc[tmp] = c.getDouble(11);
                eva[tmp] = c.getDouble(12);
                specialty[tmp] = c.getString(13);
                resist[tmp] = c.getString(14);
                exp[tmp] = c.getInt(15);
                explimit[tmp] = c.getInt(16);
                charaset[tmp] = true;
                break;
            }
            next = c.moveToNext();
        }
        c.close();
    }

    public void setmydata(){//resist '0,0,0,0,0,0,0,0' 火　氷　雷　水　風　地　光　闇
        db.execSQL("INSERT INTO person(code,name,nameview,Lv,HP,MP,ATK,MTK,DEF,MEF,SPD,ACC,EVA,specialty,resist,EXP,EXPlimit) VALUES(1,'エース・フォンバレン','エース',1,280,21,19,14,17,16,14,12,13,'SD','1,-1,0,0,0,0,0,0',0,27)");
        db.execSQL("INSERT INTO person(code,name,nameview,Lv,HP,MP,ATK,MTK,DEF,MEF,SPD,ACC,EVA,specialty,resist,EXP,EXPlimit) VALUES(2,'デュオ・スパクラーヴァ','デュオ',1,257,17,16,10,14,14,18,13,20,'SD','-1,1,0,0,0,0,0,0',0,27)");
        db.execSQL("INSERT INTO person(code,name,nameview,Lv,HP,MP,ATK,MTK,DEF,MEF,SPD,ACC,EVA,specialty,resist,EXP,EXPlimit) VALUES(3,'トライ・ハスティザート','トライ',1,261,20,17,13,15,14,16,14,16,'SD','0,0,-1,1,0,0,0,0',0,27)");
        db.execSQL("INSERT INTO person(code,name,nameview,Lv,HP,MP,ATK,MTK,DEF,MEF,SPD,ACC,EVA,specialty,resist,EXP,EXPlimit) VALUES(4,'フィーア・クラシオーネ','フィーア',1,224,24,13,18,13,15,14,17,15,'LD','0,0,1,-1,0,0,0,0',0,27)");
        db.execSQL("INSERT INTO person(code,name,nameview,Lv,HP,MP,ATK,MTK,DEF,MEF,SPD,ACC,EVA,specialty,resist,EXP,EXPlimit) VALUES(5,'フェーム・アルファーレ','フェーム',1,229,24,13,19,14,14,14,17,14,'LD','0,0,1,-1,0,0,0,0',0,27)");
        db.execSQL("INSERT INTO person(code,name,nameview,Lv,HP,MP,ATK,MTK,DEF,MEF,SPD,ACC,EVA,specialty,resist,EXP,EXPlimit) VALUES(6,'セイス・フィロハート','セイス',1,233,26,14,15,13,12,17,17,17,'DD','0,0,0,0,-1,1,0,0',0,27)");
        db.execSQL("INSERT INTO person(code,name,nameview,Lv,HP,MP,ATK,MTK,DEF,MEF,SPD,ACC,EVA,specialty,resist,EXP,EXPlimit) VALUES(7,'シェーテ・フューバレッタ','シェーテ',1,250,20,17,11,14,13,20,13,17,'SD','-1,1,0,0,0,0,0,0',0,27)");
        db.execSQL("INSERT INTO person(code,name,nameview,Lv,HP,MP,ATK,MTK,DEF,MEF,SPD,ACC,EVA,specialty,resist,EXP,EXPlimit) VALUES(8,'オクト・コーシェンツ','オクト',1,226,34,14,19,14,17,12,17,12,'LD','0,0,0,0,1,-1,0,0',0,27)");
        db.execSQL("INSERT INTO person(code,name,nameview,Lv,HP,MP,ATK,MTK,DEF,MEF,SPD,ACC,EVA,specialty,resist,EXP,EXPlimit) VALUES(9,'ノイン・カルトフシール','ノイン',1,210,36,10,20,12,15,17,15,16,'LD','1,-1,0,0,0,0,0,0',0,27)");
        db.execSQL("INSERT INTO person(code,name,nameview,Lv,HP,MP,ATK,MTK,DEF,MEF,SPD,ACC,EVA,specialty,resist,EXP,EXPlimit) VALUES(10,'ディエス・アークサジット','ディエス',1,264,24,16,14,16,15,12,20,12,'LD','0,0,0,0,-1,1,0,0',0,27)");
        db.execSQL("INSERT INTO person(code,name,nameview,Lv,HP,MP,ATK,MTK,DEF,MEF,SPD,ACC,EVA,specialty,resist,EXP,EXPlimit) VALUES(11,'エルフ・ソングナーデ','エルフ',1,240,26,14,14,13,13,18,15,18,'DD','0,0,0,0,-1,1,0,0',0,27)");
        db.execSQL("INSERT INTO person(code,name,nameview,Lv,HP,MP,ATK,MTK,DEF,MEF,SPD,ACC,EVA,specialty,resist,EXP,EXPlimit) VALUES(12,'トルヴ・フェルシルト','トルヴ',1,300,15,20,10,20,20,10,15,10,'SD','0,0,0,0,1,-1,0,0',0,27)");
        db.execSQL("INSERT INTO person(code,name,nameview,Lv,HP,MP,ATK,MTK,DEF,MEF,SPD,ACC,EVA,specialty,resist,EXP,EXPlimit) VALUES(13,'トレーズ・メイクーゲル','トレーズ',1,265,23,15,15,16,14,13,17,15,'DD','0,0,-1,1,0,0,0,0',0,27)");
        db.execSQL("INSERT INTO person(code,name,nameview,Lv,HP,MP,ATK,MTK,DEF,MEF,SPD,ACC,EVA,specialty,resist,EXP,EXPlimit) VALUES(14,'フォルテ・アウトスート','フォルテ',1,264,23,18,18,10,11,18,14,16,'DD','0,0,-1,1,0,0,0,0',0,27)");
        db.execSQL("INSERT INTO person(code,name,nameview,Lv,HP,MP,ATK,MTK,DEF,MEF,SPD,ACC,EVA,specialty,resist,EXP,EXPlimit) VALUES(15,'フィフェン・アウトスート','フィフェン',1,229,31,12,18,15,18,13,16,13,'LD','0,0,0,0,1,-1,0,0',0,27)");
        db.execSQL("INSERT INTO person(code,name,nameview,Lv,HP,MP,ATK,MTK,DEF,MEF,SPD,ACC,EVA,specialty,resist,EXP,EXPlimit) VALUES(16,'フローラ・スプリンコート','フローラ',1,228,30,13,17,13,14,15,19,14,'LD','0,0,1,-1,0,0,0,0',0,27)");
        db.execSQL("INSERT INTO person(code,name,nameview,Lv,HP,MP,ATK,MTK,DEF,MEF,SPD,ACC,EVA,specialty,resist,EXP,EXPlimit) VALUES(17,'セレシア・プラントリナ','セレシア',1,239,24,16,15,14,15,16,14,15,'DD','-1,1,0,0,0,0,0,0',0,27)");
        db.execSQL("INSERT INTO person(code,name,nameview,Lv,HP,MP,ATK,MTK,DEF,MEF,SPD,ACC,EVA,specialty,resist,EXP,EXPlimit) VALUES(18,'ミスト・スプラヴィーン','ミスト',1,267,26,14,17,14,14,17,14,15,'DD','0,0,0,0,-1,1,0,0',0,27)");
        db.execSQL("INSERT INTO person(code,name,nameview,Lv,HP,MP,ATK,MTK,DEF,MEF,SPD,ACC,EVA,specialty,resist,EXP,EXPlimit) VALUES(19,'マリー・フォンバレン','マリー',1,251,24,16,15,14,16,15,14,15,'DD','1,-1,0,0,0,0,0,0',0,27)");
        db.execSQL("INSERT INTO person(code,name,nameview,Lv,HP,MP,ATK,MTK,DEF,MEF,SPD,ACC,EVA,specialty,resist,EXP,EXPlimit) VALUES(20,'セラ・フォンバレン','セラ',1,234,30,13,18,13,17,14,16,14,'LD','0,0,1,-1,0,0,0,0',0,27)");
        db.execSQL("INSERT INTO lastplay(one,two,three,four,level,chara) VALUES(0,0,0,0,1,20)");
    }

    public void setmyglows(){
        db.execSQL("INSERT INTO growtable(code,HP,MP,ATK,MTK,DEF,MEF,SPD,ACC,EVA,plus,minus) VALUES(1,80,5,8,6,7,7,6,5,4,'HP','MP')");//ace
        db.execSQL("INSERT INTO growtable(code,HP,MP,ATK,MTK,DEF,MEF,SPD,ACC,EVA,plus,minus) VALUES(2,60,4,6,5,5,5,8,6,9,'EVA','DEF')");//duo
        db.execSQL("INSERT INTO growtable(code,HP,MP,ATK,MTK,DEF,MEF,SPD,ACC,EVA,plus,minus) VALUES(3,70,5,7,6,5,6,7,6,6,'SPD','MTK')");//tray
        db.execSQL("INSERT INTO growtable(code,HP,MP,ATK,MTK,DEF,MEF,SPD,ACC,EVA,plus,minus) VALUES(4,55,8,4,8,5,6,5,6,6,'MP','DEF')");//vier
        db.execSQL("INSERT INTO growtable(code,HP,MP,ATK,MTK,DEF,MEF,SPD,ACC,EVA,plus,minus) VALUES(5,55,7,5,7,5,7,5,7,5,'MTK','ATK')");//fem
        db.execSQL("INSERT INTO growtable(code,HP,MP,ATK,MTK,DEF,MEF,SPD,ACC,EVA,plus,minus) VALUES(6,60,5,6,6,5,5,7,7,7,'SPD','DEF')");//seis
        db.execSQL("INSERT INTO growtable(code,HP,MP,ATK,MTK,DEF,MEF,SPD,ACC,EVA,plus,minus) VALUES(7,65,4,7,4,7,5,9,5,7,'ATK','MP')");//siete
        db.execSQL("INSERT INTO growtable(code,HP,MP,ATK,MTK,DEF,MEF,SPD,ACC,EVA,plus,minus) VALUES(8,65,9,5,8,5,6,4,6,5,'MP','ATK')");//octio
        db.execSQL("INSERT INTO growtable(code,HP,MP,ATK,MTK,DEF,MEF,SPD,ACC,EVA,plus,minus) VALUES(9,45,9,3,9,3,4,8,7,5,'MTK','ATK')");//neun
        db.execSQL("INSERT INTO growtable(code,HP,MP,ATK,MTK,DEF,MEF,SPD,ACC,EVA,plus,minus) VALUES(10,75,6,6,6,6,5,5,9,5,'ATK','EVA')");//diez
        db.execSQL("INSERT INTO growtable(code,HP,MP,ATK,MTK,DEF,MEF,SPD,ACC,EVA,plus,minus) VALUES(11,65,6,6,6,5,5,7,5,8,'MTK','DEF')");//elf
        db.execSQL("INSERT INTO growtable(code,HP,MP,ATK,MTK,DEF,MEF,SPD,ACC,EVA,plus,minus) VALUES(12,85,4,9,3,9,8,3,8,4,'HP','MTK')");//tolv
        db.execSQL("INSERT INTO growtable(code,HP,MP,ATK,MTK,DEF,MEF,SPD,ACC,EVA,plus,minus) VALUES(13,70,7,6,7,5,5,5,8,5,'ATK','SPD')");//treize
        db.execSQL("INSERT INTO growtable(code,HP,MP,ATK,MTK,DEF,MEF,SPD,ACC,EVA,plus,minus) VALUES(14,70,6,8,8,4,4,8,5,5,'SPD','MEF')");//fourte
        db.execSQL("INSERT INTO growtable(code,HP,MP,ATK,MTK,DEF,MEF,SPD,ACC,EVA,plus,minus) VALUES(15,70,8,5,8,5,7,5,6,4,'MEF','HP')");//fifen
        db.execSQL("INSERT INTO growtable(code,HP,MP,ATK,MTK,DEF,MEF,SPD,ACC,EVA,plus,minus) VALUES(16,60,7,4,7,5,5,6,9,5,'MP','HP')");//flora
        db.execSQL("INSERT INTO growtable(code,HP,MP,ATK,MTK,DEF,MEF,SPD,ACC,EVA,plus,minus) VALUES(17,65,6,6,7,5,5,7,6,6,'ATK','DEF')");//ceresia
        db.execSQL("INSERT INTO growtable(code,HP,MP,ATK,MTK,DEF,MEF,SPD,ACC,EVA,plus,minus) VALUES(18,75,5,6,7,5,6,6,6,7,'EVA','HP')");//mist
        db.execSQL("INSERT INTO growtable(code,HP,MP,ATK,MTK,DEF,MEF,SPD,ACC,EVA,plus,minus) VALUES(19,70,6,6,7,6,6,6,5,6,'ATK','HP')");//marie
        db.execSQL("INSERT INTO growtable(code,HP,MP,ATK,MTK,DEF,MEF,SPD,ACC,EVA,plus,minus) VALUES(20,50,8,3,9,4,7,6,7,4,'MTK','SPD')");//cela
    }

    public void leveltoability(){
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(1,79,1)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(1,97,5)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(1,96,9)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(1,99,13)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(1,15,17)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(1,100,21)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(1,83,25)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(1,86,29)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(1,101,33)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(1,92,37)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(1,90,43)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(1,93,55)");

        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(2,4,1)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(2,76,5)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(2,37,9)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(2,49,11)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(2,55,17)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(2,58,22)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(2,75,25)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(2,44,29)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(2,39,33)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(2,64,38)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(2,71,45)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(2,72,53)");

        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(3,1,1)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(3,103,4)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(3,11,8)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(3,117,11)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(3,105,15)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(3,114,20)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(3,106,24)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(3,25,29)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(3,39,33)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(3,110,37)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(3,111,44)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(3,112,51)");

        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(4,118,1)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(4,138,4)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(4,132,9)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(4,38,13)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(4,140,17)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(4,122,22)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(4,133,26)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(4,141,29)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(4,124,33)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(4,125,37)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(4,127,45)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(4,129,53)");

        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(5,119,1)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(5,135,4)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(5,120,8)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(5,136,13)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(5,121,17)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(5,144,22)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(5,142,24)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(5,47,28)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(5,124,33)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(5,126,37)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(5,128,45)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(5,130,55)");

        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(6,147,1)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(6,166,4)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(6,14,9)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(6,164,13)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(6,20,17)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(6,156,22)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(6,42,24)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(6,24,29)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(6,39,33)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(6,167,38)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(6,161,45)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(6,32,53)");

        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(7,51,1)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(7,52,4)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(7,53,9)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(7,42,13)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(7,56,17)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(7,18,21)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(7,59,25)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(7,63,29)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(7,67,33)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(7,29,37)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(7,70,44)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(7,73,51)");

        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(8,78,1)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(8,146,4)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(8,184,7)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(8,172,13)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(8,183,17)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(8,116,21)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(8,139,25)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(8,174,28)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(8,181,32)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(8,66,37)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(8,178,41)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(8,179,52)");

        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(9,79,1)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(9,102,4)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(9,97,9)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(9,81,13)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(9,40,17)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(9,82,21)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(9,98,25)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(9,87,29)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(9,88,33)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(9,89,39)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(9,91,47)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(9,94,53)");

        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(10,5,1)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(10,166,5)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(10,150,9)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(10,151,13)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(10,165,17)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(10,156,21)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(10,153,26)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(10,26,29)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(10,27,33)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(10,159,37)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(10,160,49)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(10,162,54)");

        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(11,148,1)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(11,166,5)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(11,150,9)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(11,2,13)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(11,16,17)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(11,169,20)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(11,154,23)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(11,23,26)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(11,170,30)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(11,157,37)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(11,171,42)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(11,33,54)");

        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(12,6,1)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(12,45,5)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(12,12,10)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(12,41,14)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(12,21,17)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(12,137,21)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(12,43,25)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(12,175,30)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(12,181,34)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(12,177,38)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(12,130,42)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(12,34,57)");

        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(13,103,1)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(13,3,4)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(13,117,9)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(13,104,13)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(13,60,23)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(13,84,23)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(13,123,23)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(13,155,23)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(13,173,23)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(13,108,32)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(13,31,42)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(13,35,52)");

        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(14,103,1)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(14,11,4)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(14,36,9)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(14,104,13)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(14,16,17)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(14,106,21)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(14,23,24)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(14,115,28)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(14,107,32)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(14,108,37)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(14,109,44)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(14,113,52)");

        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(15,182,1)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(15,184,4)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(15,183,9)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(15,172,13)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(15,39,17)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(15,45,21)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(15,76,24)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(15,174,28)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(15,181,32)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(15,178,37)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(15,185,44)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(15,180,52)");

        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(16,7,1)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(16,138,4)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(16,46,9)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(16,136,13)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(16,140,17)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(16,144,21)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(16,142,24)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(16,133,28)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(16,22,32)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(16,141,37)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(16,143,44)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(16,145,52)");

        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(17,50,1)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(17,36,4)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(17,54,9)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(17,55,13)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(17,57,17)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(17,75,21)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(17,61,24)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(17,77,28)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(17,65,32)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(17,68,37)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(17,69,44)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(17,74,52)");

        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(18,149,1)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(18,150,5)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(18,168,9)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(18,152,13)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(18,169,17)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(18,154,21)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(18,153,25)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(18,22,29)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(18,28,33)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(18,158,37)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(18,160,43)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(18,163,55)");

        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(19,79,1)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(19,16,4)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(19,96,9)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(19,80,11)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(19,102,13)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(19,81,16)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(19,15,21)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(19,82,24)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(19,85,29)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(19,39,32)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(19,92,45)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(19,95,56)");

        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(20,119,1)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(20,134,6)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(20,138,9)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(20,136,13)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(20,121,17)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(20,144,21)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(20,142,24)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(20,124,33)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(20,125,36)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(20,128,45)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(20,130,52)");
        db.execSQL("INSERT INTO learning(code,abilityID,releaselv) VALUES(20,131,54)");
    }

    public void setenemydata(){
        db.execSQL("INSERT INTO enemy(code,name,HP,MP,ATK,MTK,DEF,MEF,SPD,ACC,EVA,resist,explus) VALUES(0,'BOSS',545,24,60,60,21,21,20,20,20,'0,0,0,0,0,0,0,0',25)");
        db.execSQL("INSERT INTO enemy(code,name,HP,MP,ATK,MTK,DEF,MEF,SPD,ACC,EVA,resist,explus) VALUES(1,'コヨーテ',140,3,15,10,8,6,19,13,15,'1,-1,0,-1,0,0,0,0',3)");
        db.execSQL("INSERT INTO enemy(code,name,HP,MP,ATK,MTK,DEF,MEF,SPD,ACC,EVA,resist,explus) VALUES(2,'下級悪魔',111,26,10,18,6,8,14,18,13,'0,0,0,0,0,0,1,-1',2)");
        db.execSQL("INSERT INTO enemy(code,name,HP,MP,ATK,MTK,DEF,MEF,SPD,ACC,EVA,resist,explus) VALUES(3,'魔鳥',131,2,13,11,7,7,13,15,22,'0,0,1,0,1,-1,0,0',2)");
        db.execSQL("INSERT INTO enemy(code,name,HP,MP,ATK,MTK,DEF,MEF,SPD,ACC,EVA,resist,explus) VALUES(4,'ゴーレム',240,0,30,3,24,11,9,20,5,'-1,-1,0,0,0,1,0,0',6)");
        db.execSQL("INSERT INTO enemy(code,name,HP,MP,ATK,MTK,DEF,MEF,SPD,ACC,EVA,resist,explus) VALUES(5,'一般兵士',164,19,21,18,17,17,14,17,10,'0,0,0,0,0,0,0,0',8)");
        db.execSQL("INSERT INTO enemy(code,name,HP,MP,ATK,MTK,DEF,MEF,SPD,ACC,EVA,resist,explus) VALUES(6,'ゴースト',130,50,10,28,17,10,11,15,35,'0,0,0,0,0,0,1,-1',9)");
        db.execSQL("INSERT INTO enemy(code,name,HP,MP,ATK,MTK,DEF,MEF,SPD,ACC,EVA,resist,explus) VALUES(7,'迎撃ロボ',225,30,20,20,30,30,20,47,15,'0,0,1,0,-1,0,0,0',11)");
        db.execSQL("INSERT INTO enemy(code,name,HP,MP,ATK,MTK,DEF,MEF,SPD,ACC,EVA,resist,explus) VALUES(8,'熟練兵士',212,26,30,15,25,20,21,20,15,'0,0,0,0,0,0,0,0',12)");
        db.execSQL("INSERT INTO enemy(code,name,HP,MP,ATK,MTK,DEF,MEF,SPD,ACC,EVA,resist,explus) VALUES(9,'ドラゴン',303,44,45,39,35,40,21,30,0,'-1,1,0,-1,-1,0,0,0',18)");
    }
    
    public void enemyability(){//6,2,4,3,2,4,7,3,4,4
        db.execSQL("INSERT INTO Elearning(code,abilityID,level) VALUES(0,13,1)");
        db.execSQL("INSERT INTO Elearning(code,abilityID,level) VALUES(0,148,2)");// ヴィント・チャクラム
        db.execSQL("INSERT INTO Elearning(code,abilityID,level) VALUES(0,82,3)");//魔法
        db.execSQL("INSERT INTO Elearning(code,abilityID,level) VALUES(0,61,4)");//魔法
        db.execSQL("INSERT INTO Elearning(code,abilityID,level) VALUES(0,117,3)");//モーメント

        db.execSQL("INSERT INTO Elearning(code,abilityID,level) VALUES(1,8,1)");

        db.execSQL("INSERT INTO Elearning(code,abilityID,level) VALUES(2,103,1)");
        db.execSQL("INSERT INTO Elearning(code,abilityID,level) VALUES(2,61,3)");//強力な爆発魔法
        db.execSQL("INSERT INTO Elearning(code,abilityID,level) VALUES(2,87,5)");//強力な氷魔法

        db.execSQL("INSERT INTO Elearning(code,abilityID,level) VALUES(3,9,1)");
        db.execSQL("INSERT INTO Elearning(code,abilityID,level) VALUES(3,166,3)");//ムーヴシフト

        db.execSQL("INSERT INTO Elearning(code,abilityID,level) VALUES(4,18,1)");

        db.execSQL("INSERT INTO Elearning(code,abilityID,level) VALUES(5,10,1)");
        db.execSQL("INSERT INTO Elearning(code,abilityID,level) VALUES(5,48,1)");//傷薬
        db.execSQL("INSERT INTO Elearning(code,abilityID,level) VALUES(5,79,1)");//リオートバレット

        db.execSQL("INSERT INTO Elearning(code,abilityID,level) VALUES(6,57,1)");//ブラム
        db.execSQL("INSERT INTO Elearning(code,abilityID,level) VALUES(6,81,1)");//リオート
        db.execSQL("INSERT INTO Elearning(code,abilityID,level) VALUES(6,104,1)");//ツオン
        db.execSQL("INSERT INTO Elearning(code,abilityID,level) VALUES(6,138,1)");//アイル・ヒーラ
        db.execSQL("INSERT INTO Elearning(code,abilityID,level) VALUES(6,154,1)");//ヴィント・ブレイド
        db.execSQL("INSERT INTO Elearning(code,abilityID,level) VALUES(6,172,1)");//アーデ・ロックブラスト

        db.execSQL("INSERT INTO Elearning(code,abilityID,level) VALUES(7,28,1)");
        db.execSQL("INSERT INTO Elearning(code,abilityID,level) VALUES(7,106,1)");//ツオン・インパクト

        db.execSQL("INSERT INTO Elearning(code,abilityID,level) VALUES(8,15,1)");//二刀乱舞
        db.execSQL("INSERT INTO Elearning(code,abilityID,level) VALUES(8,24,1)");
        db.execSQL("INSERT INTO Elearning(code,abilityID,level) VALUES(8,15,1)");//バフ

        db.execSQL("INSERT INTO Elearning(code,abilityID,level) VALUES(9,17,1)");//かぎ爪
        db.execSQL("INSERT INTO Elearning(code,abilityID,level) VALUES(9,62,1)");//ブレス
        db.execSQL("INSERT INTO Elearning(code,abilityID,level) VALUES(9,178,1)");//アーデ・ピラースラスト
    }

    public void allability(){
        //normal
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(1,'ソニックジャベリン',0,20,0,'direct','single','normal')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(2,'クイックアタック',0,20,0,'magic','single','normal')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(3,'クイックシュート',1,20,0,'indirect','single','normal')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(4,'強突き',0,30,0,'direct','single','normal')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(5,'精密一射',0,30,0,'indirect','single','normal')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(6,'正拳突き',0,30,0,'direct','single','normal')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(7,'狙い射ち',1,30,0,'indirect','single','normal')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(8,'噛みつき',0,30,0,'direct','single','normal')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(9,'はばたき',0,30,0,'indirect','single','normal')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(10,'振りかぶり斬り',0,40,0,'direct','single','normal')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(11,'斬り上げ',0,45,0,'direct','single','normal')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(12,'回し蹴り',0,45,0,'direct','single','normal')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(13,'強攻撃',1,50,0,'direct','single','normal')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(14,'全力突き',0,55,0,'direct','single','normal')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(15,'二刀乱舞',2,60,0,'direct','single','normal')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(16,'全力斬り',0,60,0,'direct','single','normal')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(17,'かぎ爪',0,60,0,'direct','single','normal')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(18,'ライトコンビネーション',17,65,0,'direct','single','normal')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(19,'右ストレート',0,65,0,'direct','single','normal')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(20,'フルーレバッシュ',4,70,0,'direct','single','normal')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(21,'戦車の如き突進',5,70,0,'direct','single','normal')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(22,'魔法弾',25,80,0,'magic','single','normal')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(23,'ラッシュアウェイ',7,85,0,'direct','single','normal')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(24,'スターストライクラッシュ',25,90,0,'direct','single','normal')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(25,'薙ぎ払い',0,95,0,'direct','all','normal')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(26,'乱れ射ち',22,95,0,'indirect','all','normal')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(27,'バニッシュアロー',20,95,0,'indirect','single','normal')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(28,'乱れ撃ち',27,100,0,'indirect','all','normal')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(29,'ヘビーコンビネーション',38,120,0,'direct','single','normal')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(30,'昇竜の如き拳',25,120,0,'direct','single','normal')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(31,'フルゲートキャノン',47,120,0,'indirect','all','normal')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(32,'ペンタグラム・スターラインズ',77,150,0,'direct','single','normal')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(33,'ヘキサグラム・クレッシェンド',68,150,0,'direct','single','normal')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(34,'入魂せし極限の一拳',45,150,0,'direct','single','normal')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(35,'トレンタルレイン・バレット',71,150,0,'indirect','all','normal')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(36,'応援',0,0,2,'atksupport','anyone','normal')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(37,'Dメモネタ・励まし',13,0,2,'atksupport','anyone','normal')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(38,'瞑想',0,0,2,'mtksupport','onlyme','normal')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(39,'集中',0,0,1,'mtksupport','onlyme','normal')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(40,'師匠のお言葉',0,0,1,'mtksupport','onlyme','normal')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(41,'身構える',0,0,1,'mefsupport','onlyme','normal')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(42,'見切り',0,0,2,'evasupport','onlyme','normal')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(43,'身崩し',4,0,2,'defdown','single','normal')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(44,'Dメモネタ・煽り',17,0,2,'mefdown','single','normal')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(45,'分析',1,0,2,'mefdown','all','normal')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(46,'祈る',0,20,0,'mpheal','onlyme','normal')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(47,'お手製の薬',0,15,0,'mpheal','anyone','normal')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(48,'傷薬',0,15,0,'hpheal','anyone','normal')");
        //fire
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(49,'バーニアストライク',0,20,0,'direct','single','fire')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(50,'ブラム・ファイア',4,30,0,'magic','single','fire')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(51,'バーニングナックル',2,30,0,'direct','single','fire')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(52,'至近距離ブラム・ファイアもどき',4,40,0,'magic','single','fire')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(53,'ファイヤーキック',8,45,0,'direct','single','fire')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(54,'火炎斬り',7,45,0,'direct','single','fire')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(55,'ブラム・バースト',13,50,0,'magic','all','fire')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(56,'ブレイズエルボー',12,55,0,'direct','single','fire')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(57,'ブラム・コメット',17,60,0,'magic','single','fire')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(58,'ファイアスラスト',16,70,0,'direct','single','fire')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(59,'ニーフレア',20,80,0,'direct','single','fire')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(60,'バレットセット・ブラム',23,80,0,'indirect','single','fire')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(61,'ブラム・イラプション',23,80,0,'magic','all','fire')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(62,'ブレス',4,85,0,'indirect','all','fire')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(63,'ロケットヘッド',28,95,0,'direct','single','fire')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(64,'ブレイズバッシュ',36,100,0,'direct','single','fire')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(65,'ブラム・ランサーズラッシュ',33,100,0,'magic','single','fire')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(66,'ペーパー ブラム・エクスプロージョン',36,105,0,'magic','all','fire')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(67,'エクスプロードタックル',32,110,0,'direct','single','fire')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(68,'ブラム・エクスプロージョン',45,110,0,'magic','all','fire')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(69,'ブラム・ミーティア',54,120,0,'magic','single','fire')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(70,'カーマインズダンス',47,130,0,'direct','single','fire')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(71,'ボルケニックトワリング',56,130,0,'direct','single','fire')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(72,'フレイムテンペストラーレ',66,150,0,'direct','single','fire')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(73,'オーバーヒートドライヴ',56,150,0,'direct','single','fire')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(74,'百花繚乱・焔色',76,150,0,'magic','all','fire')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(75,'ブラム・ウェアリング',14,0,6,'atksupport','onlyme','fire')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(76,'ブラム・ブースター',2,0,2,'spdsupport','onlyme','fire')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(77,'ブラム・フローイング',15,0,2,'evadown','single','fire')");
        //ice
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(78,'ペーパー リオート・バレット',3,25,0,'magic','single','ice')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(79,'リオート・バレット',3,30,0,'magic','single','ice')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(80,'リオート・レインバレット',12,50,0,'magic','all','ice')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(81,'リオート・キャノン',10,50,0,'magic','single','ice')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(82,'リオート・アロー',13,65,0,'magic','single','ice')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(83,'アイスナックル',12,75,0,'direct','single','ice')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(84,'バレットセット・リオート',23,80,0,'indirect','single','ice')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(85,'リオート・ミラージュバレット',23,85,0,'magic','single','ice')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(86,'キック・フリーズサイズ',18,85,0,'direct','single','ice')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(87,'リオート・バズーカ',26,85,0,'magic','single','ice')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(88,'リオート・バレットスコール',42,100,0,'magic','all','ice')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(89,'リオート・クルーエルアローズ',37,100,0,'magic','single','ice')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(90,'ソードダンス・リオートドライブ',57,130,0,'direct','single','ice')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(91,'リオート・インフィニティバレット',76,135,0,'magic','single','ice')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(92,'リオート・レイヴィス',40,140,0,'magic','all','ice')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(93,'レイジングブリザード',80,150,0,'direct','single','ice')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(94,'リオート・フルディメンションバレット',95,150,0,'magic','all','ice')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(95,'リオート・サークルオーダーバレット',77,150,0,'magic','single','ice')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(96,'リオート・メイクソード',4,0,2,'atksupport','onlyme','ice')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(97,'リオート・シールド',5,0,1,'defsupport','onlyme','ice')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(98,'リオート・スタックシールド',19,0,5,'defsupport','onlyme','ice')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(99,'リオート・シェル',10,0,3,'defsupport','onlyme','ice')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(100,'リオート・リフレクション',25,0,2,'defsupport','anyone','ice')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(101,'リオート・フォートレス',32,0,8,'mefsupport','allus','ice')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(102,'リオート・アイスフロア',10,0,4,'spddown','all','ice')");
        //thunder
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(103,'ツオン・ドロップ',4,30,0,'magic','single','thunder')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(104,'ツオン・スネイクバイト',14,65,0,'magic','single','thunder')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(105,'スラスト＆ショック',10,65,0,'direct','single','thunder')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(106,'ツオン・インパクト',20,80,0,'magic','single','thunder')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(107,'ツオン・サンダーフォール',28,95,0,'magic','all','thunder')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(108,'ツオン・ティアリング',37,110,0,'magic','single','thunder')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(109,'ライトニングダンス',31,120,0,'direct','single','thunder')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(110,'トライデントスパーク',56,125,0,'magic','all','thunder')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(111,'ツオン・ボルテックスピア',52,125,0,'magic','single','thunder')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(112,'疾風迅雷・槍刃',68,150,0,'direct','single','thunder')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(113,'ツオン・ヘビーサンダーストーム',77,150,0,'magic','all','thunder')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(114,'ツオン・アドブリッツ',8,0,2,'atksupport','onlyme','thunder')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(115,'ツオン・ライクライトニング',13,0,4,'spdsupport','onlyme','thunder')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(116,'ペーパー ツオン・ストップモーメント',8,0,1,'bind','single','thunder')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(117,'ツオン・ストップモーメント',11,0,2,'bind','single','thunder')");
        //water
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(118,'アイル・スフィアシュート',6,30,0,'magic','single','water')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(119,'アイル・ストライクウィップ',3,30,0,'magic','single','water')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(120,'アイル・バーストボム',8,45,0,'magic','single','water')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(121,'アイル・バーストバレット',14,60,0,'magic','single','water')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(122,'アイル・ツインシュート',20,75,0,'magic','single','water')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(123,'バレットセット・アイル',20,75,0,'indirect','single','water')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(124,'アイル・ストリームサーベル',23,100,0,'magic','single','water')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(125,'アイル・ノックアップエリア',35,110,0,'magic','all','water')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(126,'アイル・ドラゴンバイト',44,120,0,'magic','all','water')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(127,'アイル・ヘビーファイア',57,130,0,'magic','single','water')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(128,'アイル・カノンストリーム',57,130,0,'magic','single','water')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(129,'アイル・タイダルウェーブ',76,150,0,'magic','all','water')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(130,'アイル・セプテットアーチ',80,150,0,'magic','single','water')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(131,'アイル・インスフィアフルバースト',90,150,0,'magic','all','water')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(132,'アイル・カットウォール',7,0,2,'defsupport','onlyme','water')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(133,'アイル・プロテクトストリーム',25,0,5,'mefsupport','allus','water')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(134,'アイル・フロアストリーム',10,0,2,'spddown','single','water')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(135,'アイル・チェーンウィップ',6,0,1,'bind','single','water')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(136,'アイル・サーペンテイン',10,0,3,'bind','single','water')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(137,'ペーパー アイル・ヒーラ',5,8,0,'hpheal','anyone','water')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(138,'アイル・ヒーラ',5,10,0,'hpheal','anyone','water')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(139,'ペーパー アイル・ハイヒーラ',15,30,0,'hpheal','anyone','water')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(140,'アイル・ハイヒーラ',15,50,0,'hpheal','anyone','water')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(141,'アイル・フルヒーラ',40,100,0,'hpheal','anyone','water')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(142,'アイル・ホールヒーラ',25,20,0,'hpheal','allus','water')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(143,'アイル・ホールフルヒーラ',65,100,0,'hpheal','allus','water')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(144,'アイル・リカバリー',20,0,0,'illheal','anyone','water')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(145,'アイル・リライブ',100,1500,0,'relive','anyone','water')");
        //wind
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(146,'ペーパー ヴィント・ノーマルシュート',3,25,0,'magic','single','wind')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(147,'ヴィント・スパイラル',7,30,0,'magic','all','wind')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(148,'ヴィント・チャクラム',3,30,0,'magic','single','wind')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(149,'ヴィント・ノーマルシュート',2,30,0,'magic','single','wind')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(150,'ヴィント・ドレイン',4,35,0,'magic','single','wind')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(151,'ヴィント・トワイスアロー',10,45,0,'magic','single','wind')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(152,'ヴィント・オーバーシュート',10,50,0,'magic','single','wind')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(153,'ヴィント・ソニックバースト',16,70,0,'magic','single','wind')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(154,'ヴィント・ブレイドウェーブ',12,70,0,'magic','all','wind')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(155,'バレットセット・ヴィント',20,75,0,'indirect','single','wind')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(156,'ヴィント・サイクロン',18,80,0,'magic','all','wind')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(157,'ヴィント・ペアクロウズ',43,110,0,'magic','all','wind')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(158,'ヴィント・スパイラルシュート',32,110,0,'magic','single','wind')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(159,'ヴィント・バーストニードル',45,115,0,'magic','single','wind')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(160,'ヴィント・ダウンバースト',43,125,0,'magic','single','wind')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(161,'ヴィント・レイジタイフーン',57,125,0,'magic','all','wind')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(162,'アクセリング・スパイラルアロー',72,150,0,'indirect','single','wind')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(163,'ヴィント・フルリボルバー',64,150,0,'magic','all','wind')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(164,'ヴィント・アドスパイラル',11,0,4,'atksupport','onlyme','wind')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(165,'ヴィント・リバースフロー',13,0,2,'mefsupport','onlyme','wind')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(166,'ヴィント・アクセルフロー',5,0,2,'spdsupport','onlyme','wind')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(167,'ヴィント・テールウィンド',20,0,3,'spdsupport','allus','wind')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(168,'エアロステップ',4,0,2,'evasupport','onlyme','wind')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(169,'ヴィント・リトルフロー',14,0,2,'defdown','single','wind')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(170,'ヴィント・レスグラビティ',17,0,4,'spddown','single','wind')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(171,'ヴィント・ヘッドウィンド',20,0,3,'spddown','all','wind')");
        //ground
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(172,'アーデ・ロックブラスト',11,60,3,'magic','single','ground')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(173,'バレットセット・アーデ',20,75,0,'indirect','single','ground')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(174,'アーデ・グラウンドランチャー',27,95,0,'magic','single','ground')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(175,'岩纏せし剛拳',15,95,0,'direct','single','ground')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(176,'アーデ・ロックスプレッド',30,95,0,'magic','all','ground')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(177,'岩纏せし烈蹴',20,105,0,'direct','single','ground')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(178,'アーデ・ピラースラスト',67,125,0,'magic','all','ground')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(179,'アーデ・コメットマシンガン',88,150,0,'magic','single','ground')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(180,'アーデ・メテオストリーム',92,150,0,'magic','all','ground')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(181,'アーデ・フォートレス',25,0,4,'defsupport','allus','ground')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(182,'アーデ・ウォール',9,0,2,'mefsupport','anyone','ground')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(183,'アーデ・アドグラビティ',10,0,3,'spddown','all','ground')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(184,'アーデ・サンドカーテン',6,0,2,'accdown','single','ground')");
        db.execSQL("INSERT INTO ability(abilityID,name,usemp,power,turn,type,target,element) VALUES(185,'アーデ・ピラーズバインド',20,0,4,'bind','single','ground')");
        
        //light
        
        //dark
    }
    
    public void setimage(){
        for(int i=1;i<21;i++) {
            selectimage(i);
            bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bytegazo = bos.toByteArray();
            cv = new ContentValues();
            cv.put("code", i);
            cv.put("charaimage", bytegazo);
            db.insert("personimage", null, cv);
        }
    }

    public void selectimage(int a){
        switch (a) {
            case 1:
                bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ace);
                break;
            case 2:
                bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.duo);
                break;
            case 3:
                bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.tray);
                break;
            case 4:
                bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.vier);
                break;
            case 5:
                bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.fem);
                break;
            case 6:
                bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.seis);
                break;
            case 7:
                bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.siete);
                break;
            case 8:
                bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.octio);
                break;
            case 9:
                bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.neun);
                break;
            case 10:
                bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.diez);
                break;
            case 11:
                bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.elf);
                break;
            case 12:
                bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.tolv);
                break;
            case 13:
                bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.treize);
                break;
            case 14:
                bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.fourte);
                break;
            case 15:
                bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.fifen);
                break;
            case 16:
                bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.flora);
                break;
            case 17:
                bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ceresia);
                break;
            case 18:
                bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.mist);
                break;
            case 19:
                bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.marie);
                break;
            case 20:
                bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.cela);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        alertDialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("アプリを終了しますか？")
                .setPositiveButton(
                        "はい",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                moveTaskToBack(true);
                            }
                        })
                .setNegativeButton(
                        "いいえ",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alertDialog.dismiss();
                            }

                        })
                .show();

    }

    protected void onDestroy(){
        super.onDestroy();
        db.close();
    }

}
