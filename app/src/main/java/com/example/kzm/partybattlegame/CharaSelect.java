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
    String name[]=new String[4],sname;
    String viewname[]=new String[4],svn;
    String specialty[]=new String[4], sst;
    String resist[]=new String[4], sres;
    int chara[]=new int[4],Lv[]=new int[4];
    int exp[]=new int[4],explimit[]=new int[4];
    int n,k,set=0,all=20,code;
    int schara,sLv,sexp,sexplimit,maxdata=200;
    double sHP,sMP,satk,smtk,sdef,smef,sspd,sacc,seva,expmul;
    double HP[]=new double[4],MP[]=new double[4];
    double atk[]=new double[4],mtk[]=new double[4];
    double def[]=new double[4],mef[]=new double[4];
    double spd[]=new double[4],acc[]=new double[4],eva[]=new double[4];
    boolean charaset[]=new boolean[4], showdata=false, next,remove;
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
        for(k=0;k<4;k++)charaset[k]=false;
        i = this.getIntent();
        remove = i.getBooleanExtra("remove",false);
        commons = (Commons) getApplication();
        myView = findViewById(R.id.back2).findViewById(R.id.view);
        commons.backinit(myView);
        commons.makebutton();
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
                if(charaset[0]) {
                    commons.setall(all);
                    commons.datatransmission(chara,name,viewname,Lv,HP,MP,atk,mtk,def,mef,spd,acc,eva,specialty,resist,exp,explimit,charaset);
                    Toast.makeText(CharaSelect.this,
                            "編成を決定しました！"
                            +"\n"+viewname[0]+" "+viewname[1]+" "+viewname[2]+" "+viewname[3],
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
                    if(chara[k]==schara) check=1;
                }
                if(set<4 && check==0) {
                    if(showdata){
                        chara[set] = schara;
                        name[set] = sname;
                        viewname[set] = svn;
                        Lv[set] = sLv;
                        HP[set] = sHP;
                        MP[set] = sMP;
                        atk[set] = satk;
                        mtk[set] = smtk;
                        def[set] = sdef;
                        mef[set] = smef;
                        spd[set] = sspd;
                        acc[set] = sacc;
                        eva[set] = seva;
                        exp[set] = sexp;
                        specialty[set] = sst;
                        resist[set] = sres;
                        explimit[set] = sexplimit;
                        charaset[set] = true;
                        set++;
                        tv2.setText("Party\n" + name[0] + "\n" + name[1] + "\n" + name[2] + "\n" + name[3]);
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
            dataset(countup[n],set);
            dataview();
            showdata=true;
        }
    };


    public void dataset(int n,int set){
        if(set<4) {
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
    }

    public void dataview(){
        tv.setText("Name\n" + sname +"\nLv"+ sLv +" HP:"+ (int)sHP +"\nMP:"+ (int)sMP +" 攻撃:"+ (int)satk +"\n魔攻:"+ (int)smtk
                +" 防御:"+ (int)sdef +"\n魔防:"+ (int)smef +" 速さ:"+ (int)sspd +"\n命中:"+ (int)sacc +" 回避:"+ (int)seva +"\nEXP " + sexp + "/" + sexplimit);
    }

    public void allclean(){
        for(k=0;k<4;k++){
            charaset[k]=false;
            chara[k]=-1;
            name[k]="";
            viewname[k] = "";
            Lv[k] = 0;
            HP[k] = 0;
            MP[k] = 0;
            atk[k] = 0;
            mtk[k] = 0;
            def[k] = 0;
            mef[k] = 0;
            spd[k] = 0;
            acc[k] = 0;
            eva[k] = 0;
            specialty[k] = "";
            resist[k] = "";
            exp[k] = 0;
            explimit[k] = 1;
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
