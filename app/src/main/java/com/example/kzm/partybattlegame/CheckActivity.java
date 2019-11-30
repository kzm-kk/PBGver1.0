package com.example.kzm.partybattlegame;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class CheckActivity extends AppCompatActivity {
    String bind = "#FFD700", poison = "#9370DB";
    TextView tv;
    LinearLayout layout;
    int[][] state = new int[8][];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explain);
        ScrollView scrl = findViewById(R.id.explainscroll);
        layout = findViewById(R.id.explains);
        Button bt = findViewById(R.id.button);
        bt.setText("戻る");
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Intent i = this.getIntent();
        String[] name = i.getStringArrayExtra("name");
        boolean[] set = i.getBooleanArrayExtra("set");
        String[] ename = i.getStringArrayExtra("ename");
        boolean[] eset = i.getBooleanArrayExtra("eset");
        state[0]= i.getIntArrayExtra("state0");
        state[1]= i.getIntArrayExtra("state1");
        state[2]= i.getIntArrayExtra("state2");
        state[3]= i.getIntArrayExtra("state3");
        state[4]= i.getIntArrayExtra("state4");
        state[5]= i.getIntArrayExtra("state5");
        state[6]= i.getIntArrayExtra("state6");
        state[7]= i.getIntArrayExtra("state7");
        for(int s=0;s<4;s++) {
            if(!set[s]) continue;
            tv = new TextView(this);
            tv.setText(name[s]);
            layout.addView(tv);
            for (int k = 0; k < 17; k++) {
                if(state[s][k]>0) changeview(s,k);
            }
        }
        for(int s=0;s<4;s++) {
            if(!eset[s]) continue;
            tv = new TextView(this);
            tv.setText(ename[s]);
            layout.addView(tv);
            for (int k = 0; k < 17; k++) {
                if(state[s+4][k]>0) changeview(s+4,k);
            }
        }
    }

    public void changeview(int s,int a){
        String str="";
        if(a<3) str="攻撃";
        else if(a<5) str="魔攻";
        else if(a<7) str="防御";
        else if(a<9) str="魔防";
        else if(a<11) str="速さ";
        else if(a<13) str="命中";
        else if(a<15) str="回避";
        if(a==0){
            tv = new TextView(this);
            tv.setText("レゾナンス状態:一連のバトルが終わるまで解除されません");
            tv.setTextColor(Color.GREEN);
            layout.addView(tv);
        } else if(a<15 && a%2==1){
            tv = new TextView(this);
            tv.setText(str+"上昇:あと"+state[s][a]+"ターン");
            tv.setTextColor(Color.RED);
            layout.addView(tv);
        } else if(a<15 && a%2==0){
            tv = new TextView(this);
            tv.setText(str+"下降:あと"+state[s][a]+"ターン");
            tv.setTextColor(Color.BLUE);
            layout.addView(tv);
        } else if(a==15){
            tv = new TextView(this);
            tv.setText("拘束状態:あと"+state[s][a]+"ターン");
            tv.setTextColor(Color.parseColor(bind));
            layout.addView(tv);
        } else if(a==16){
            tv = new TextView(this);
            tv.setText("毒状態:あと"+state[s][a]+"ターン");
            tv.setTextColor(Color.parseColor(poison));
            layout.addView(tv);
        }
    }
}
