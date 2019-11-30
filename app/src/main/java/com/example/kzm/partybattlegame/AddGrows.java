package com.example.kzm.partybattlegame;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class AddGrows extends AppCompatActivity {
    MyOpenHelper hp;
    SQLiteDatabase db;
    ContentValues cv;
    TextView tv;
    Intent i;
    int all, code, Lv,HP,MP,explimit;
    double atk,mtk,def,mef,spd,acc,eva;
    double pHP,pMP,patk,pmtk,pdef,pmef,pspd,pacc,peva;
    double cor[]={1,1,1,1,1,1,1,1,1};
    double pcor=1.1,mcor=0.9;
    String fullname,viewname,specialty,plus,minus,resist;
    Spinner sp,sp2;
    EditText et[]=new EditText[9];
    SpannableStringBuilder sb[] = new SpannableStringBuilder[12];
    byte[] image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_grows);
        hp = new MyOpenHelper(this);
        db = hp.getWritableDatabase();
        sp = findViewById(R.id.plussp);
        sp2 = findViewById(R.id.minussp);
        TextView thistv = findViewById(R.id.thiswindow2);
        thistv.setText("この画面ではレベルアップ時のステータスの伸び方を設定します");
        TextView plustv = findViewById(R.id.plustext);
        TextView minustv = findViewById(R.id.minustext);
        tv = findViewById(R.id.limitmessage2);
        tv.setText("");
        i = this.getIntent();
        all = i.getIntExtra("allchara", 20);
        code = i.getIntExtra("code", 21);
        tv.setText("");
        image = i.getByteArrayExtra("image");
        fullname = i.getStringExtra("fullname");
        viewname = i.getStringExtra("viewname");
        Lv = i.getIntExtra("Lv",1);
        HP = i.getIntExtra("HP",250);
        MP = i.getIntExtra("MP",20);
        atk = i.getDoubleExtra("ATK",15);
        mtk = i.getDoubleExtra("MTK",15);
        def = i.getDoubleExtra("DEF",15);
        mef = i.getDoubleExtra("MEF",15);
        spd = i.getDoubleExtra("SPD",15);
        acc = i.getDoubleExtra("ACC",15);
        eva = i.getDoubleExtra("EVA",15);
        specialty = i.getStringExtra("specialty");
        resist = i.getStringExtra("resist");
        explimit = i.getIntExtra("explimit",27);
        et[0] = findViewById(R.id.HPnumgrow);
        et[1] = findViewById(R.id.MPnumgrow);
        et[2] = findViewById(R.id.ATKnumgrow);
        et[3] = findViewById(R.id.MTKnumgrow);
        et[4] = findViewById(R.id.DEFnumgrow);
        et[5] = findViewById(R.id.MEFnumgrow);
        et[6] = findViewById(R.id.SPDnumgrow);
        et[7] = findViewById(R.id.ACCnumgrow);
        et[8] = findViewById(R.id.EVAnumgrow);
        Button stop = findViewById(R.id.buttons2).findViewById(R.id.stopbutton);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = new Intent(AddGrows.this, CharaSelect.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });
        Button fab = (Button) findViewById(R.id.buttons2).findViewById(R.id.explain);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = new Intent(AddGrows.this, ExplainActivity.class);
                i.putExtra("plus",pcor);
                i.putExtra("minus",mcor);
                i.putExtra("page",2);
                startActivity(i);
            }
        });

        Button bt = (Button) findViewById(R.id.buttons2).findViewById(R.id.nextbutton);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                all++;
                pHP = Double.parseDouble(((SpannableStringBuilder) et[0].getText()).toString());
                pMP = Double.parseDouble(((SpannableStringBuilder) et[1].getText()).toString());
                patk = Double.parseDouble(((SpannableStringBuilder) et[2].getText()).toString());
                pmtk = Double.parseDouble(((SpannableStringBuilder) et[3].getText()).toString());
                pdef = Double.parseDouble(((SpannableStringBuilder) et[4].getText()).toString());
                pmef = Double.parseDouble(((SpannableStringBuilder) et[5].getText()).toString());
                pspd = Double.parseDouble(((SpannableStringBuilder) et[6].getText()).toString());
                pacc = Double.parseDouble(((SpannableStringBuilder) et[7].getText()).toString());
                peva = Double.parseDouble(((SpannableStringBuilder) et[8].getText()).toString());
                plus = plusandminsset(sp.getSelectedItemPosition());
                minus = plusandminsset(sp2.getSelectedItemPosition());
                statusup();
                if(!plus.equals(minus))complete();
                else {
                    Toast.makeText(AddGrows.this,
                            "エラー：プラス補正能力とマイナス補正能力が同じです",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void complete(){
        cv = new ContentValues();
        cv.put("code", code);
        cv.put("charaimage", image);
        db.insert("personimage", null, cv);
        cv = new ContentValues();
        cv.put("code", code);
        cv.put("name", fullname);
        cv.put("nameview", viewname);
        cv.put("Lv", Lv);
        cv.put("HP", HP);
        cv.put("MP", MP);
        cv.put("ATK", atk);
        cv.put("MTK", mtk);
        cv.put("DEF", def);
        cv.put("MEF", mef);
        cv.put("SPD", spd);
        cv.put("ACC", acc);
        cv.put("EVA", eva);
        cv.put("specialty", specialty);
        cv.put("resist", resist);
        cv.put("EXP", 0);
        cv.put("EXPlimit", explimit);
        db.insert("person", null, cv);
        cv = new ContentValues();
        cv.put("code", code);
        cv.put("HP", pHP);
        cv.put("MP", pMP);
        cv.put("ATK", patk);
        cv.put("MTK", pmtk);
        cv.put("DEF", pdef);
        cv.put("MEF", pmef);
        cv.put("SPD", pspd);
        cv.put("ACC", pacc);
        cv.put("EVA", peva);
        cv.put("plus", plus);
        cv.put("minus", minus);
        db.insert("growtable", null, cv);
        cv = new ContentValues();
        cv.put("chara", all);
        db.update("lastplay", cv, null, null);
        /*i = new Intent(AddGrows.this, CharaSelect.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);*/
        i = new Intent(AddGrows.this, Two_Choices.class);
        i.putExtra("allchara", all);
        i.putExtra("page",1);
        Toast.makeText(AddGrows.this,
                fullname+"を登録しました",
                Toast.LENGTH_SHORT).show();
        startActivity(i);
    }

    public String plusandminsset(int num){
        switch (num){
            case 0:
                return "HP";
            case 1:
                return "MP";
            case 2:
                return "ATK";
            case 3:
                return "MTK";
            case 4:
                return "DEF";
            case 5:
                return "MEF";
            case 6:
                return "SPD";
            case 7:
                return "ACC";
            case 8:
                return "EVA";
            default:
                return "HP";
        }
    }

    public void statusup(){
        datacor(plus,minus);
        HP=(int)((double)HP+pHP*(Lv-1)*cor[0]);
        MP=(int)((double)MP+pMP*(Lv-1)*cor[1]);
        atk=atk+patk*(Lv-1)*cor[2];
        mtk=mtk+pmtk*(Lv-1)*cor[3];
        def=def+pdef*(Lv-1)*cor[4];
        mef=mef+pmef*(Lv-1)*cor[5];
        spd=spd+pspd*(Lv-1)*cor[6];
        acc=acc+pacc*(Lv-1)*cor[7];
        eva=eva+peva*(Lv-1)*cor[8];
    }

    public void datacor(String plus,String minus){
        switch (plus){
            case "HP":
                cor[0] = pcor;
                break;
            case "MP":
                cor[1] = pcor;
                break;
            case "ATK":
                cor[2] = pcor;
                break;
            case "MTK":
                cor[3] = pcor;
                break;
            case "DEF":
                cor[4] = pcor;
                break;
            case "MEF":
                cor[5] = pcor;
                break;
            case "SPD":
                cor[6] = pcor;
                break;
            case "ACC":
                cor[7] = pcor;
                break;
            case "EVA":
                cor[8] = pcor;
                break;
            default:
                break;
        }
        switch (minus){
            case "HP":
                cor[0] = mcor;
                break;
            case "MP":
                cor[1] = mcor;
                break;
            case "ATK":
                cor[2] = mcor;
                break;
            case "MTK":
                cor[3] = mcor;
                break;
            case "DEF":
                cor[4] = mcor;
                break;
            case "MEF":
                cor[5] = mcor;
                break;
            case "SPD":
                cor[6] = mcor;
                break;
            case "ACC":
                cor[7] = mcor;
                break;
            case "EVA":
                cor[8] = mcor;
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
    }

}
