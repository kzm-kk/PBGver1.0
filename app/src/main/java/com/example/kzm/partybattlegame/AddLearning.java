package com.example.kzm.partybattlegame;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Adapter;
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

public class AddLearning extends AppCompatActivity {
    MyOpenHelper hp;
    SQLiteDatabase db;
    Cursor c,c2;
    ContentValues cv;
    TextView tv,tv2;
    Intent i;
    EditText et;
    Button bt, bt2, bt3;
    Spinner sp, sp2, sp3;
    String name;
    String element[] = {"fire", "ice", "thunder", "water", "wind", "ground", "light", "dark", "normal"};
    ArrayAdapter<String> myadapter,elementadapter[]=new ArrayAdapter[9];
    HashMap<String,Integer> hm,hm2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_learning);
        hp = new MyOpenHelper(this);
        db = hp.getWritableDatabase();
        et = findViewById(R.id.Lvedit);
        tv = findViewById(R.id.thiswindow4);
        tv.setText("この画面ではアビリティの習得に関して設定します");
        tv2 = findViewById(R.id.charatext);
        tv2.setText("覚えるキャラ");
        TextView tv3 = findViewById(R.id.elementtext2);
        tv3.setText("属性");
        TextView tv4 = findViewById(R.id.abilitytext);
        tv4.setText("技の一覧");
        hm = new HashMap<String, Integer>();
        ArrayList<String> mylist = new ArrayList<String>();
        c2 = db.query("person", new String[]{"code", "name", "nameview", "Lv", "HP", "MP", "ATK", "MTK", "DEF", "MEF", "SPD", "ACC", "EVA", "specialty", "resist", "EXP", "EXPlimit"}, null, null, null, null, null);
        boolean next = c2.moveToFirst();
        while (next) {
            hm.put(c2.getString(1),c2.getInt(0));
            mylist.add(c2.getString(1));
            next = c2.moveToNext();
        }
        c2.close();
        myadapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, mylist);
        // ドロップダウンのレイアウトを指定
        myadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
        sp = findViewById(R.id.charasp);
        sp.setAdapter(myadapter);
        sp3 = findViewById(R.id.abilitysp);

        sp2 = findViewById(R.id.elementkindsp);
        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //何も選択されなかった時の動作
            @Override
            public void onNothingSelected(AdapterView adapterView) {
            }

            @Override
            public void onItemSelected(AdapterView parent, View view, int position, long id) {
                sp3.setAdapter(adapterset());
            }
        });
        sp3.setAdapter(adapterset());
        bt = findViewById(R.id.buttons4).findViewById(R.id.stopbutton);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = new Intent(AddLearning.this, OptionActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });
        bt2 = (Button) findViewById(R.id.buttons4).findViewById(R.id.explain);
        /*bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = new Intent(AddLearning.this, ExplainActivity.class);
                i.putExtra("page",4);
                startActivity(i);
            }
        });*/
        bt3 = (Button) findViewById(R.id.buttons4).findViewById(R.id.nextbutton);
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cv = new ContentValues();
                String name = (String) sp.getSelectedItem();
                int code = hm.get(name);
                String str = (String)sp3.getSelectedItem();
                String lv = ((SpannableStringBuilder) et.getText()).toString();
                cv.put("code",code);
                cv.put("abilityID",abilityidset());
                cv.put("releaselv",Integer.parseInt(lv));
                db.insert("Learning",null,cv);
                Toast.makeText(AddLearning.this,
                        "登録完了\n名前:"+name+"\n技:"+str+"\n習得レベル:"+lv,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public SpinnerAdapter adapterset(){
        int num = sp2.getSelectedItemPosition();
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
