package com.example.kzm.partybattlegame;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class CreditActivity extends AppCompatActivity {

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
        TextView tv = new TextView(this);
        tv.setTextSize(20);
        tv.setText("\nCredit Space\n\n");
        tv.setTextColor(Color.BLACK);
        tv.setGravity(Gravity.CENTER);
        layout.addView(tv);
        tv = new TextView(this);
        tv.setText("メイン製作\nKZM/一考真之\n");
        tv.setGravity(Gravity.CENTER);
        layout.addView(tv);
        tv = new TextView(this);
        tv.setText("デフォルトキャラアイコン及びアプリアイコン\nCHARAT\n(本家お借りしました。製作行為はKZM)\nURL:https://charat.me/avatarmaker/\n");
        tv.setGravity(Gravity.CENTER);
        layout.addView(tv);
        tv = new TextView(this);
        tv.setText("その他参考にさせて頂いたたくさんのコードを作った方々\n");
        tv.setGravity(Gravity.CENTER);
        layout.addView(tv);
        tv = new TextView(this);
        tv.setText("試作プレイして頂いた方々\n\n");
        tv.setGravity(Gravity.CENTER);
        layout.addView(tv);
        tv = new TextView(this);
        tv.setTextSize(20);
        tv.setPaintFlags(tv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tv.setTextColor(Color.BLACK);
        tv.setText("ご協力ありがとうございました!!");
        tv.setGravity(Gravity.CENTER);
        layout.addView(tv);
    }
}
