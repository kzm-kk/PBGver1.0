package com.example.kzm.partybattlegame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class InformationActivity extends AppCompatActivity {
    InputStream is = null;
    BufferedReader br = null;
    String text = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explain);
        ScrollView scrl = findViewById(R.id.explainscroll);
        LinearLayout layout = findViewById(R.id.explains);
        Button bt = findViewById(R.id.button);
        bt.setText("戻る");
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        try {
            try {
                // assetsフォルダ内の sample.txt をオープンする
                is = this.getAssets().open("info.txt");
                br = new BufferedReader(new InputStreamReader(is));

                // １行ずつ読み込み、改行を付加する
                String str;
                while ((str = br.readLine()) != null) {
                    text += str + "\n";
                }
            } finally {
                if (is != null) is.close();
                if (br != null) br.close();
            }
        } catch (Exception e){
            // エラー発生時の処理
        }
        TextView tv = new TextView(this);
        tv.setText("\nリリース遍歴\n");
        tv.setGravity(Gravity.CENTER);
        layout.addView(tv);
        tv = new TextView(this);
        tv.setText(text);
        tv.setGravity(Gravity.CENTER);
        layout.addView(tv);
    }
}
