package com.example.kzm.partybattlegame;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;

public class AttackContents extends AppCompatActivity {
    int mp,chara,Lv,j=1,k,search,me,releaselv,decide=0,maxab=25;
    int power[] = new int[maxab], turn[] = new int[maxab];
    int type[] = new int[maxab], effectgo[] = new int[maxab];
    int usemp[] = new int[maxab], elenum[] = new int[maxab];
    String typecode[] = new String[maxab], appoint[] = new String[maxab];
    String seename,myname[]=new String[4], ename[]=new String[4];
    String abname[]=new String[maxab],element[]=new String[maxab];
    boolean mov;
    TextView tv;
    Button bt;
    RadioGroup rg;
    RadioButton rb[]=new RadioButton[maxab];
    Intent i;
    MyOpenHelper hp;
    SQLiteDatabase db;
    Cursor c,c2;
    Spinner sp;
    ArrayAdapter<String> adapter,myadapter,ouradapter,alladapter,deadadapter;
    HashMap<String,Integer> hm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attack_contents);
        hp = new MyOpenHelper(this);
        db = hp.getReadableDatabase();
        i = this.getIntent();
        chara = i.getIntExtra("chara", 1);
        me = i.getIntExtra("name",0);
        Lv = i.getIntExtra("Lv", 1);
        mp = i.getIntExtra("MP", 0);
        boolean death[] = i.getBooleanArrayExtra("mylive");
        myname = i.getStringArrayExtra("our");
        boolean enemydeath[] = i.getBooleanArrayExtra("live");
        ename = i.getStringArrayExtra("enemy");
        hm = new HashMap<String, Integer>();
        ArrayList<String> list = new ArrayList<String>();
        ArrayList<String> ourlist = new ArrayList<String>();
        ArrayList<String> alllist = new ArrayList<String>();
        ArrayList<String> mylist = new ArrayList<String>();
        ArrayList<String> deadlist = new ArrayList<String>();
        hm.put("all", 6);
        alllist.add("all");
        mylist.add(myname[me]);
        for(int i=0;i<4;i++){
            if(!enemydeath[i]){
                hm.put(ename[i],i);
                list.add(ename[i]);
            }
            if(!death[i]){
                hm.put(myname[i],i);
                ourlist.add(myname[i]);
            } else if(death[i]){
                hm.put(myname[i],i);
                deadlist.add(myname[i]);
            }
        }
        // Adapterの作成
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        // ドロップダウンのレイアウトを指定
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ouradapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, ourlist);
        // ドロップダウンのレイアウトを指定
        ouradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        alladapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, alllist);
        // ドロップダウンのレイアウトを指定
        alladapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myadapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, mylist);
        // ドロップダウンのレイアウトを指定
        myadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        deadadapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, deadlist);
        // ドロップダウンのレイアウトを指定
        deadadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp = (Spinner) findViewById(R.id.targetspinner);
        sp.setAdapter(adapter);
        tv = findViewById(R.id.detailtext);
        bt = (Button) findViewById(R.id.actionbutton);
        bt.setText("決定");
        bt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(decide==1) /*(mp >= usemp )*/{
                    i = new Intent();
                    i.putExtra("name", seename);
                    i.putExtra("usemp", usemp[search]);
                    i.putExtra("power", power[search]);
                    i.putExtra("turn", turn[search]);
                    i.putExtra("type", type[search]);
                    i.putExtra("effectgo", effectgo[search]);
                    i.putExtra("target", targetset());
                    i.putExtra("element", elenum[search]);
                    setResult(RESULT_OK, i);
                    finish();
                }
            }
        });
        rg = (RadioGroup) findViewById(R.id.skillgroup);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int id) {
                if (-1 == id) {
                    Toast.makeText(AttackContents.this,
                            "通常攻撃",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("AttackContents", "");
                    if (mp < usemp[search] || ((RadioButton)findViewById(id)).getText()=="") {
                        Toast.makeText(AttackContents.this,
                                "MPが足りません",
                                Toast.LENGTH_SHORT).show();
                        decide=0;
                    }else{
                        decide=1;
                    }
                }
            }
        });
        rb[0]=findViewById(R.id.normalRB);
        String sel[]=new String[1];
        sel[0]=Integer.toString(chara);
        int idnum[]=new int[maxab];
        c = db.query("learning", new String[] { "code", "abilityID", "releaselv"}, "code = ?",sel, null, null, null);
        mov = c.moveToFirst();
        while (mov) {
            k=c.getInt(0);
            releaselv = c.getInt(2);
            if (releaselv <= Lv) {
               idnum[j] = c.getInt(1);
               j++;
            }
            mov = c.moveToNext();
        }
        c.close();
        for(int i=1;i<j;i++){
            String sel2[]=new String[1];
            sel2[0]=Integer.toString(idnum[i]);
            c2 = db.query("ability", new String[] { "abilityID", "name", "usemp", "power","turn", "type", "target", "element"}, "abilityID = ?",
                    sel2, null, null, null);
            mov = c2.moveToFirst();
            while (mov) {
                abname[i] = c2.getString(1);
                usemp[i] = c2.getInt(2);
                power[i] = c2.getInt(3);
                turn[i] = c2.getInt(4);
                typecode[i] = c2.getString(5);
                appoint[i] = c2.getString(6);
                element[i] = c2.getString(7);
                checkmethod(i, typecode[i]);
                elenum[i]=elementset(element[i]);
                mov = c2.moveToNext();
            }
            c2.close();
        }
        for(int t=1;t<j;t++){
            rb[t] = new RadioButton(this);
            rb[t].setText(abname[t]);
            rb[t].setOnClickListener(event);
            rg.addView(rb[t]);
        }
        abname[0]="通常攻撃";
        rb[0].setText(abname[0]);
        rb[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seename=rb[0].getText().toString();
                search=0;
                power[0]=25;
                usemp[0]=0;
                turn[0]=0;
                typecode[0]="direct";
                appoint[0]="single";
                element[0]="normal";
                checkmethod(0, typecode[0]);
                adapterset(appoint[search]);
                elenum[0]=elementset(element[0]);
                detailset(0);
            }
        });
    }

    View.OnClickListener event = new View.OnClickListener() {
        public void onClick(View view) {
            RadioButton rbn=(RadioButton)view;
            seename=rbn.getText().toString();
            search = ((ViewGroup) view.getParent()).indexOfChild((View)view);
            adapterset(appoint[search]);
            detailset(search);
        }
    };

    public void detailset(int s){
        String target="",str,kind;
        switch (appoint[s]){
            case "single":
                target="敵単体";
                break;
            case "all":
                target="敵全体";
                break;
            case "onlyme":
                target="自分";
                break;
            case "anyone":
                target="味方単体";
                break;
            case "allus":
                target="味方全体";
                break;
            default:
                target = "";
                break;
        }
        switch (element[s]){
            case "fire":
                str = "炎";
                break;
            case "ice":
                str = "氷";
                break;
            case "thunder":
                str = "雷";
                break;
            case "water":
                str = "水";
                break;
            case "wind":
                str = "風";
                break;
            case "ground":
                str = "地";
                break;
            case "light":
                str = "光";
                break;
            case "dark":
                str = "闇";
                break;
            default:
                str = "無";
                break;
        }
        switch (typecode[s]) {
            case "resonance":
                kind = "レゾナンス";
                break;
            case "direct":
                kind = "直接攻撃";
                break;
            case "indirect":
                kind = "遠距離攻撃";
                break;
            case "magic":
                kind = "魔法攻撃";
                break;
            case "atksupport":
                kind = "攻撃上昇";
                break;
            case "mtksupport":
                kind = "魔攻上昇";
                break;
            case "defsupport":
                kind = "防御上昇";
                break;
            case "mefsupport":
                kind = "魔防上昇";
                break;
            case "spdsupport":
                kind = "速さ上昇";
                break;
            case "accsupport":
                kind = "命中上昇";
                break;
            case "evasupport":
                kind = "回避上昇";
                break;
            case "atkdown":
                kind = "攻撃下降";
                break;
            case "mtkdown":
                kind = "魔攻下降";
                break;
            case "defdown":
                kind = "防御下降";
                break;
            case "mefdown":
                kind = "魔防下降";
                break;
            case "spddown":
                kind = "速さ下降";
                break;
            case "accdown":
                kind = "命中下降";
                break;
            case "evadown":
                kind = "回避下降";
                break;
            case "bind":
                kind = "拘束付加";
                break;
            case "poison":
                kind = "毒付加";
                break;
            case "hpheal":
                kind = "HP回復";
                break;
            case "mpheal":
                kind = "MP回復";
                break;
            case "illheal":
                kind = "異常回復";
                break;
            case "relive":
                kind = "蘇生";
                break;
            default:
                kind = "";
                break;
        }
        if(type[s]<2 || type[s]>4)
            tv.setText(seename+"\n消費MP:"+usemp[s]+" 威力:"+power[s]+" 種類:"+kind+"\n属性:"+str+" 対象範囲:"+target);
        else if(type[s]>1 && type[s]<5)
            tv.setText(seename+"\n消費MP:"+usemp[s]+" 持続ターン:"+turn[s]+" 種類:"+kind+"\n属性:"+str+" 対象範囲:"+target);
    }

    public void checkmethod(int s,String str){
        switch (str) {
            case "resonance":
                type[s] = 0;
                effectgo[s] = 0;
                break;
            case "direct":
                type[s] = 1;
                effectgo[s] = 1;
                break;
            case "indirect":
                type[s] = 1;
                effectgo[s] = 2;
                break;
            case "magic":
                type[s] = 1;
                effectgo[s] = 3;
                break;
            case "atksupport":
                type[s] = 2;
                effectgo[s] = 1;
                break;
            case "mtksupport":
                type[s] = 2;
                effectgo[s] = 2;
                break;
            case "defsupport":
                type[s] = 2;
                effectgo[s] = 3;
                break;
            case "mefsupport":
                type[s] = 2;
                effectgo[s] = 4;
                break;
            case "spdsupport":
                type[s] = 2;
                effectgo[s] = 5;
                break;
            case "accsupport":
                type[s] = 2;
                effectgo[s] = 6;
                break;
            case "evasupport":
                type[s] = 2;
                effectgo[s] = 7;
                break;
            case "atkdown":
                type[s] = 3;
                effectgo[s] = 1;
                break;
            case "mtkdown":
                type[s] = 3;
                effectgo[s] = 2;
                break;
            case "defdown":
                type[s] = 3;
                effectgo[s] = 3;
                break;
            case "mefdown":
                type[s] = 3;
                effectgo[s] = 4;
                break;
            case "spddown":
                type[s] = 3;
                effectgo[s] = 5;
                break;
            case "accdown":
                type[s] = 3;
                effectgo[s] = 6;
                break;
            case "evadown":
                type[s] = 3;
                effectgo[s] = 7;
                break;
            case "bind":
                type[s] = 4;
                effectgo[s] = 1;
                break;
            case "poison":
                type[s] = 4;
                effectgo[s] = 2;
                break;
            case "hpheal":
                type[s] = 5;
                effectgo[s] = 1;
                break;
            case "mpheal":
                type[s] = 5;
                effectgo[s] = 2;
                break;
            case "illheal":
                type[s] = 5;
                effectgo[s] = 3;
                break;
            case "relive":
                type[s] = 5;
                effectgo[s] = 4;
                break;
            default:
                break;
        }
    }

    public int elementset(String str){
        switch (str){
            case "fire":
                return 0;
            case "ice":
                return 1;
            case "thunder":
                return 2;
            case "water":
                return 3;
            case "wind":
                return 4;
            case "ground":
                return 5;
            case "light":
                return 6;
            case "dark":
                return 7;
            default:
                return -1;
        }
    }

    public void adapterset(String target){
        if(target.equals("single")) sp.setAdapter(adapter);
        else if(target.equals("allus") || target.equals("all")) sp.setAdapter(alladapter);
        else if(target.equals("onlyme")) sp.setAdapter(myadapter);
        else if(target.equals("anyone")) sp.setAdapter(ouradapter);
        if(type[search]==5 && effectgo[search]==4) sp.setAdapter(deadadapter);
    }


    public int targetset(){
        return hm.get(sp.getSelectedItem());
    }

    @Override
    public void onBackPressed() {
    }

    protected void onStop(){
        super.onStop();
        //c.close();
    }
}
