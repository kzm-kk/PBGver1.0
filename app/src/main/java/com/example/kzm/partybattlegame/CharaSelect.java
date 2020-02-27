package com.example.kzm.partybattlegame;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CharaSelect extends AppCompatActivity {
    int n,k,set=0,all=20,code,maxdata=200;
    String sname, svn, sst, sres;
    int schara,sLv,sexp,sexplimit;
    double sHP,sMP,satk,smtk,sdef,smef,sspd,sacc,seva;
    boolean showdata=false, next, remove;
    TextView tv,tv2;
    Button bt,bt2,bt3,bt4;
    int countup[]=new int[maxdata];
    ImageView iv[]=new ImageView[maxdata];
    byte[] image;
    Intent i;
    MyOpenHelper hp;
    SQLiteDatabase db;
    Cursor c;
    LinearLayout layout;
    LinearLayout.LayoutParams params;
    private MainView myView;
    Commons commons;
    Commons.person[] person_tmps = new Commons.person[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charaselect);
        hp = new MyOpenHelper(this);
        db = hp.getWritableDatabase();
        layout= (LinearLayout) findViewById(R.id.charalist);
        tv=findViewById(R.id.statustext);
        tv.setText("");
        tv2=findViewById(R.id.partytext);
        tv2.setText("");
        bt=findViewById(R.id.decidebutton);
        bt.setText("編成決定");
        bt2=findViewById(R.id.setbutton);
        bt2.setText("パーティー編入");
        bt3=findViewById(R.id.removebutton);
        bt3.setText("リセット");
        i = this.getIntent();
        remove = i.getBooleanExtra("remove",false);
        commons = (Commons) getApplication();
        myView = findViewById(R.id.back2).findViewById(R.id.view);
        commons.backinit(myView);
        commons.makebutton();
        for(k=0;k<4;k++){
            person_tmps[k] = new Commons.person();
        }
        allclean();
        Button bty =findViewById(R.id.back2).findViewById(R.id.battle);
        bty.setOnClickListener(commons.MM);
        Button btx =findViewById(R.id.back2).findViewById(R.id.characters);
        Button btz =findViewById(R.id.back2).findViewById(R.id.additions);
        btz.setOnClickListener(commons.MA);
        bt4 = findViewById(R.id.back2).findViewById(R.id.options);
        bt4.setOnClickListener(commons.MO);
    }

    @Override
    protected void onResume(){
        super.onResume();
        layout.removeAllViews();
        c = db.query("personimage",new String[]{"code","charaimage"},null,null,null,null,null);
        next = c.moveToFirst();
        int j=1;
        while(next){//for(int j=1;j<=all;j++){
            code = c.getInt(0);
            countup[j] = code;
            iv[j]=new ImageView(this);
            image = c.getBlob(1);
            iv[j].setImageBitmap(getImage(image));
            params = new LinearLayout.LayoutParams(pxFromDp((float)141,this),pxFromDp((float)141,this));
            iv[j].setLayoutParams(params);
            layout.addView(iv[j]);
            iv[j].setOnClickListener(content);
            j++;
            next = c.moveToNext();
        }
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(person_tmps[0].getcharaset()) {
                    commons.setall(all);
                    datatransmission();
                    Toast.makeText(CharaSelect.this,
                            "編成を決定しました！\n"
                                    +Commons.person_data[0].getdataString("viewname")+" "
                                    +Commons.person_data[1].getdataString("viewname")+" "
                                    +Commons.person_data[2].getdataString("viewname")+" "
                                    +Commons.person_data[3].getdataString("viewname"),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CharaSelect.this,
                            "編成が完了していません",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int check=0;
                for(k=0;k<4;k++){
                    if(person_tmps[k].getdataint("chara")==schara) check=1;
                }
                if(set<4 && check==0) {
                    if(showdata){
                        person_tmps[set].setdataint("chara", schara);
                        person_tmps[set].setdataString("name", sname);
                        person_tmps[set].setdataString("viewname", svn);
                        person_tmps[set].setdataint("Lv", sLv);
                        person_tmps[set].setdatadouble("HP", sHP);
                        person_tmps[set].setdatadouble("MP", sMP);
                        person_tmps[set].setdatadouble("atk", satk);
                        person_tmps[set].setdatadouble("mtk", smtk);
                        person_tmps[set].setdatadouble("def", sdef);
                        person_tmps[set].setdatadouble("mef", smef);
                        person_tmps[set].setdatadouble("spd", sspd);
                        person_tmps[set].setdatadouble("acc", sacc);
                        person_tmps[set].setdatadouble("eva", seva);
                        person_tmps[set].setdataString("specialty", sst);
                        person_tmps[set].setdataString("resist", sres);
                        person_tmps[set].setdataint("exp", sexp);
                        person_tmps[set].setdataint("explimit", sexplimit);
                        person_tmps[set].setcharaset(true);
                        set++;
                        tv2.setText("Party\n" + person_tmps[0].getdataString("name")
                                + "\n" + person_tmps[1].getdataString("name")
                                + "\n" + person_tmps[2].getdataString("name")
                                + "\n" + person_tmps[3].getdataString("name"));
                        showdata=false;
                    }
                }
            }
        });

        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allclean();
            }
        });
    }

    View.OnClickListener content = new View.OnClickListener() {
        public void onClick(View view) {
            n = ((ViewGroup) view.getParent()).indexOfChild((View)view)+1;
            dataset(countup[n]);
            dataview();
            showdata=true;
        }
    };

    public void datatransmission(){
        for(int s=0;s<4;s++){
            commons.person_data[s].setcharaset(person_tmps[s].getcharaset());
            commons.person_data[s].setdataint("chara", person_tmps[s].getdataint("chara"));
            commons.person_data[s].setdataString("name", person_tmps[s].getdataString("name"));
            commons.person_data[s].setdataString("viewname", person_tmps[s].getdataString("viewname"));
            commons.person_data[s].setdataint("Lv", person_tmps[s].getdataint("Lv"));
            commons.person_data[s].setdatadouble("HP", person_tmps[s].getdatadouble("HP"));
            commons.person_data[s].setdatadouble("MP", person_tmps[s].getdatadouble("MP"));
            commons.person_data[s].setdatadouble("atk", person_tmps[s].getdatadouble("atk"));
            commons.person_data[s].setdatadouble("mtk", person_tmps[s].getdatadouble("mtk"));
            commons.person_data[s].setdatadouble("def", person_tmps[s].getdatadouble("def"));
            commons.person_data[s].setdatadouble("mef", person_tmps[s].getdatadouble("mef"));
            commons.person_data[s].setdatadouble("spd", person_tmps[s].getdatadouble("spd"));
            commons.person_data[s].setdatadouble("acc", person_tmps[s].getdatadouble("acc"));
            commons.person_data[s].setdatadouble("eva", person_tmps[s].getdatadouble("eva"));
            commons.person_data[s].setdataString("specialty", person_tmps[s].getdataString("specialty"));
            commons.person_data[s].setdataString("resist", person_tmps[s].getdataString("resist"));
            commons.person_data[s].setdataint("exp", person_tmps[s].getdataint("exp"));
            commons.person_data[s].setdataint("explimit", person_tmps[s].getdataint("explimit"));
        }
    }

    public void dataset(int n){
            c = db.query("person", new String[]{"code", "name", "nameview", "Lv", "HP", "MP", "ATK", "MTK", "DEF", "MEF", "SPD", "ACC", "EVA", "specialty", "resist", "EXP", "EXPlimit"}, null, null, null, null, null);
            next = c.moveToFirst();
            while (next) {
                schara = c.getInt(0);
                if (schara == n) {
                    sname = c.getString(1);
                    svn = c.getString(2);
                    sLv = c.getInt(3);
                    sHP = c.getDouble(4);
                    sMP = c.getDouble(5);
                    satk = c.getDouble(6);
                    smtk = c.getDouble(7);
                    sdef = c.getDouble(8);
                    smef = c.getDouble(9);
                    sspd = c.getDouble(10);
                    sacc = c.getDouble(11);
                    seva = c.getDouble(12);
                    sst = c.getString(13);
                    sres = c.getString(14);
                    sexp = c.getInt(15);
                    sexplimit = c.getInt(16);
                    break;
                }
                next = c.moveToNext();
            }
    }

    public void dataview(){
        tv.setText("Name\n" + sname +"\nLv"+ sLv +" HP:"+ (int)sHP +"\nMP:"+ (int)sMP +" 攻撃:"+ (int)satk +"\n魔攻:"+ (int)smtk
                +" 防御:"+ (int)sdef +"\n魔防:"+ (int)smef +" 速さ:"+ (int)sspd +"\n命中:"+ (int)sacc +" 回避:"+ (int)seva +"\nEXP " + sexp + "/" + sexplimit);
    }

    public void allclean(){
        for(k=0;k<4;k++){
            person_tmps[k].setdataint("chara", -1);
            person_tmps[k].setdataString("name", "");
            person_tmps[k].setdataString("viewname", "");
            person_tmps[k].setdataint("Lv", 0);
            person_tmps[k].setdatadouble("HP", 0);
            person_tmps[k].setdatadouble("MP", 0);
            person_tmps[k].setdatadouble("atk", 0);
            person_tmps[k].setdatadouble("mtk", 0);
            person_tmps[k].setdatadouble("def", 0);
            person_tmps[k].setdatadouble("mef", 0);
            person_tmps[k].setdatadouble("spd", 0);
            person_tmps[k].setdatadouble("acc", 0);
            person_tmps[k].setdatadouble("eva", 0);
            person_tmps[k].setdataString("specialty", "");
            person_tmps[k].setdataString("resist", "");
            person_tmps[k].setdataint("exp", 0);
            person_tmps[k].setdataint("explimit", 1);
            person_tmps[k].setcharaset(false);
        }
        set=0;
        tv2.setText("");
    }

    public static int pxFromDp(float dp, Context context){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5f);
    }

    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        c.close();
    }

}