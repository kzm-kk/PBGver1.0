package com.example.kzm.partybattlegame;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class AddAbility extends AppCompatActivity {
    MyOpenHelper hp;
    SQLiteDatabase db;
    ContentValues cv;
    TextView tv,tv2;
    Intent i;
    EditText et[] = new EditText[9];
    Button bt, bt2, bt3;
    Spinner sp, sp2, sp3, sp4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ability);
        hp = new MyOpenHelper(this);
        db = hp.getWritableDatabase();
        tv = findViewById(R.id.thiswindow3);
        tv.setText("この画面ではアビリティを登録します");
        tv2 = findViewById(R.id.limitmessage3);
        tv2.setText("登録し終わる場合は\n戻るボタンを\n押してください");
        et[0] = findViewById(R.id.abilityname);
        et[1] = findViewById(R.id.usempnum);
        et[2] = findViewById(R.id.turnnum);
        sp = findViewById(R.id.powersp);
        sp2 = findViewById(R.id.typesp);
        sp3 = findViewById(R.id.targetsp);
        sp4 = findViewById(R.id.elementsp);
        bt = findViewById(R.id.buttons3).findViewById(R.id.stopbutton);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        bt2 = (Button) findViewById(R.id.buttons3).findViewById(R.id.explain);
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = new Intent(AddAbility.this, ExplainActivity.class);
                i.putExtra("page",3);
                startActivity(i);
            }
        });

        bt3 = findViewById(R.id.buttons3).findViewById(R.id.nextbutton);
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cv = new ContentValues();
                String str = ((SpannableStringBuilder) et[0].getText()).toString();
                cv.put("name",str);
                cv.put("usemp",Integer.parseInt(((SpannableStringBuilder) et[1].getText()).toString()));
                cv.put("power",Double.parseDouble((String) sp.getSelectedItem()));
                cv.put("turn",Integer.parseInt(((SpannableStringBuilder) et[2].getText()).toString()));
                cv.put("type",effectset());
                cv.put("target",targetset());
                cv.put("element",elementset());
                db.insert("ability",null,cv);
                Toast.makeText(AddAbility.this,
                        "技:"+str+"をデータベースに登録しました",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String effectset(){
        int num = sp2.getSelectedItemPosition();
        switch (num){
            case 0:
                return "direct";
            case 1:
                return "indirect";
            case 2:
                return "magic";
            case 3:
                return "atksupport";
            case 4:
                return "mtksupport";
            case 5:
                return "defsupport";
            case 6:
                return "mefsupport";
            case 7:
                return "spdsupport";
            case 8:
                return "accsupport";
            case 9:
                return "evasupport";
            case 10:
                return "atkdown";
            case 11:
                return "mtkdown";
            case 12:
                return "defdown";
            case 13:
                return "mefdown";
            case 14:
                return "spddown";
            case 15:
                return "accdown";
            case 16:
                return "evadown";
            case 17:
                return "hpheal";
            case 18:
                return "mpheal";
            case 19:
                return "illheal";
            case 20:
                return "relive";
            case 21:
                return "bind";
            case 22:
                return "poison";
            default:
                return "single";
        }
    }

    public String targetset(){
        int num = sp3.getSelectedItemPosition();
        switch (num){
            case 0:
                return "single";
            case 1:
                return "all";
            case 2:
                return "onlyme";
            case 3:
                return "anyone";
            case 4:
                return "allus";
            default:
                return "single";
        }
    }

    public String elementset(){
        int num = sp4.getSelectedItemPosition();
        switch (num){
            case 0:
                return "fire";
            case 1:
                return "ice";
            case 2:
                return "thunder";
            case 3:
                return "water";
            case 4:
                return "wind";
            case 5:
                return "ground";
            case 6:
                return "light";
            case 7:
                return "dark";
            default:
                return "normal";
        }
    }
}
