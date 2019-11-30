package com.example.kzm.partybattlegame;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class RemoveActivity extends AppCompatActivity {
    MyOpenHelper hp;
    SQLiteDatabase db;
    Cursor c,c2;
    ContentValues cv;
    TextView tv,tv2;
    Intent i;
    EditText et;
    Button bt, bt2, bt3;
    Spinner sp, sp3,sp4;
    String name;
    String element[] = {"fire", "ice", "thunder", "water", "wind", "ground", "light", "dark", "normal"};
    ArrayList<String> mylist;
    ArrayAdapter<String> myadapter,elementadapter[]=new ArrayAdapter[10],adapter;
    HashMap<String,Integer> hm,hm2;
    boolean cflag = true,remove = false;
    int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove);
        hp = new MyOpenHelper(this);
        db = hp.getWritableDatabase();
        tv = findViewById(R.id.thiswindow5);
        tv.setText("この画面ではキャラ、もしくはアビリティを削除出来ます");
        hm = new HashMap<String, Integer>();
        mylist = new ArrayList<String>();
        c2 = db.query("person", new String[]{"code", "name", "nameview", "Lv", "HP", "MP", "ATK", "MTK", "DEF", "MEF", "SPD", "ACC", "EVA", "specialty", "resist", "EXP", "EXPlimit"}, null, null, null, null, null);
        boolean next = c2.moveToFirst();
        while (next) {
            hm.put(c2.getString(1),c2.getInt(0));
            mylist.add(c2.getString(1));
            count++;
            next = c2.moveToNext();
        }
        c2.close();
        myadapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, mylist);
        // ドロップダウンのレイアウトを指定
        myadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.element));
        // ドロップダウンのレイアウトを指定
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hm2 = new HashMap<String, Integer>();
        for(int i=0;i<9;i++){
            ArrayList<String> elementlist = new ArrayList<String>();
            String sel2[]=new String[1];
            sel2[0]=element[i];
            c = db.query("ability", new String[] { "abilityID", "name", "usemp", "power","turn", "type", "target", "element"}, "element = ?",
                    sel2, null, null, null);
            boolean mov = c.moveToFirst();
            while (mov) {
                hm2.put(c.getString(1),c.getInt(0));
                elementlist.add(c.getString(1));
                mov = c.moveToNext();
            }
            c.close();
            elementadapter[i] = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, elementlist);
            // ドロップダウンのレイアウトを指定
            elementadapter[i].setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        }
        ArrayList<String> elementlist = new ArrayList<String>();
        elementlist.add("選択不可");
        elementadapter[9] = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, elementlist);
        // ドロップダウンのレイアウトを指定
        elementadapter[9].setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp = findViewById(R.id.charasp2);
        sp.setAdapter(myadapter);
        sp3 = findViewById(R.id.abilitysp2);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //何も選択されなかった時の動作
            @Override
            public void onNothingSelected(AdapterView adapterView) {
            }

            @Override
            public void onItemSelected(AdapterView parent, View view, int position, long id) {
                if(!cflag) sp3.setAdapter(adapterset());
            }
        });
        sp3.setAdapter(elementadapter[9]);
        sp4 = findViewById(R.id.selection);
        sp4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //何も選択されなかった時の動作
            @Override
            public void onNothingSelected(AdapterView adapterView) {
            }

            @Override
            public void onItemSelected(AdapterView parent, View view, int position, long id) {
                if(sp4.getSelectedItemPosition()==0 && count<5){
                    cflag = true;
                    myadapter.addAll(mylist);
                    sp.setAdapter(myadapter);
                    sp3.setAdapter(elementadapter[9]);
                } else if(sp4.getSelectedItemPosition()==1){
                    cflag = false;
                    sp.setAdapter(adapter);
                    sp3.setAdapter(adapterset());
                }
            }
        });
        bt = findViewById(R.id.stopbutton5);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(remove){
                    i = new Intent(RemoveActivity.this,CharaSelect.class);
                    boolean set[]={false,false,false,false};
                    i.putExtra("remove",remove);
                    i.putExtra("set",set);
                    Toast.makeText(RemoveActivity.this,
                            "パーティーを再編成します",
                            Toast.LENGTH_SHORT).show();
                    startActivity(i);
                } else finish();
            }
        });
        //bt2 = (Button) findViewById(R.id.explain3);
        /*bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = new Intent(AddLearning.this, ExplainActivity.class);
                i.putExtra("page",4);
                startActivity(i);
            }
        });*/
        bt3 = (Button) findViewById(R.id.removebutton);
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int num;
                String name = "";
                if(cflag){
                    num = hm.get(sp.getSelectedItem());
                    name = (String)sp.getSelectedItem();
                    String str[] = {Integer.toString(num)};
                    db.delete("person","code = ?",str);
                    db.delete("learning","code = ?",str);
                    db.delete("personimage","code = ?",str);
                    db.delete("growtable","code = ?",str);
                    remove = true;
                } else {
                    num = hm2.get(sp3.getSelectedItem());
                    name = (String)sp3.getSelectedItem();
                    String str[] = {Integer.toString(num)};
                    db.delete("learning","abilityID = ?",str);
                    db.delete("ability","abilityID = ?",str);
                    db.delete("Elearning","abilityID = ?",str);
                }
                Toast.makeText(RemoveActivity.this,
                        "削除完了:"+name,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    public SpinnerAdapter adapterset(){
        int num = sp.getSelectedItemPosition();
        switch (num){
            case 0:
                return elementadapter[0];
            case 1:
                return elementadapter[1];
            case 2:
                return elementadapter[2];
            case 3:
                return elementadapter[3];
            case 4:
                return elementadapter[4];
            case 5:
                return elementadapter[5];
            case 6:
                return elementadapter[6];
            case 7:
                return elementadapter[7];
            default:
                return elementadapter[8];
        }
    }

    public int abilityidset(){
        return hm2.get(sp3.getSelectedItem());
    }

}
