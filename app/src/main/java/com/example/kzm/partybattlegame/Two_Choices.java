package com.example.kzm.partybattlegame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Two_Choices extends AppCompatActivity {
    int all;
    TextView tv,tv2;
    Button bt,bt2;
    Intent i;
    private Commons commons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continue);
        tv=findViewById(R.id.CorF);
        tv2=findViewById(R.id.CorFmessage);
        tv.setGravity(Gravity.CENTER);
        bt=findViewById(R.id.continuebutton);
        bt2=findViewById(R.id.finishbutton);
        i = this.getIntent();
        all = i.getIntExtra("allchara",20);
        commons = (Commons)getApplication();
        commons.setall(all);
        tv.setText("『続ける』ボタンで引き続きキャラを作成します\n『終わる』ボタンでパーティー編成画面に戻ります");
        bt.setText("続ける");
        bt.setEnabled(false);
        bt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                i = new Intent(Two_Choices.this, AddCharacter.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
        bt2.setText("終わる");
        bt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                i = new Intent(Two_Choices.this, AdditionsActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });

    }

    @Override
    public void onBackPressed() {
    }

}
