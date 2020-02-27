package com.example.kzm.partybattlegame;

import android.content.Intent;
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

public class ExplainActivity extends AppCompatActivity {

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
        tv.setText("\n設定事項の説明をします\n");
        tv.setGravity(Gravity.CENTER);
        layout.addView(tv);
        Intent i = this.getIntent();
        int page = i.getIntExtra("page",1);
        InputStream is = null;
        BufferedReader br = null;
        String text = "";
        String txtfile = "";
        switch(page){
            case 1:
                txtfile = "charadata_explain.txt";
                break;
            case 2:
                txtfile = "charagrowdata_explain.txt";
                break;
            case 3:
                txtfile = "abilitydata_explain.txt";
                break;
            default:
                txtfile = "charadata_explain.txt";
                break;
        }
        try {
            try {
                is = this.getAssets().open(txtfile);
                br = new BufferedReader(new InputStreamReader(is));

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
        tv = new TextView(this);
        tv.setText(text);
        tv.setGravity(Gravity.CENTER);
        layout.addView(tv);
        /*if(page==1){
            tv = new TextView(this);
            tv.setText("画像: アイコン用です。キャラセレクトやバトルで使います\n");
            layout.addView(tv);
            tv = new TextView(this);
            tv.setText("フルネーム:viewnameが同じキャラを混同させないために使用します\n");
            layout.addView(tv);
            tv = new TextView(this);
            tv.setText("ニックネーム: 戦闘画面などで使われる短めの名前です\n");
            layout.addView(tv);
            tv = new TextView(this);
            tv.setText("HP: 体力です。０になると戦闘不能になります\n");
            layout.addView(tv);
            tv = new TextView(this);
            tv.setText("MP: 魔力量です。これを消費して使う特技があります\n");
            layout.addView(tv);
            tv = new TextView(this);
            tv.setText("攻撃: 物理攻撃力です。物理攻撃のダメージに関係します\n");
            layout.addView(tv);
            tv = new TextView(this);
            tv.setText("魔攻: 魔法攻撃力です。魔法攻撃のダメージに関係します\n");
            layout.addView(tv);
            tv = new TextView(this);
            tv.setText("防御: 物理防御力です。高いと物理ダメージが減ります\n");
            layout.addView(tv);
            tv = new TextView(this);
            tv.setText("魔防: 魔法防御力です。高いと魔法ダメージが減ります\n");
            layout.addView(tv);
            tv = new TextView(this);
            tv.setText("速さ: 行動順に関係します。高ければより早く動けます\n");
            layout.addView(tv);
            tv = new TextView(this);
            tv.setText("命中: 正確性です。高いと攻撃が当たりやすいです\n");
            layout.addView(tv);
            tv = new TextView(this);
            tv.setText("回避: 回避力です。高いと攻撃を回避しやすいです\n");
            layout.addView(tv);
            tv = new TextView(this);
            tv.setText("得意な距離: このキャラが近遠どちらが得意か、です。右の欄から選んでください。以下解説\n" +
                    "  近距離: 近接命中上昇、遠隔命中下降。\n" +
                    "  遠距離: 遠隔命中上昇、近接命中下降。\n" +
                    "  両方: どちらも同じくらい当たります。\n");
            layout.addView(tv);
            tv = new TextView(this);
            tv.setText("弱点/半減: このキャラの耐性を表します。半減と弱点を１つずつ選んでください\n");
            layout.addView(tv);
        } else if(page==2){
            tv = new TextView(this);
            tv.setText("各パラメータ: レベルが１上がった時の上昇値です。\nデフォルトキャラはHPを除きすべて3～9の間にしてあります\n");
            layout.addView(tv);
            tv = new TextView(this);
            tv.setText("プラス補正能力:指定パラメータの伸びに"+plus+"倍の補正がかかります\nテキスト右横の欄から選んで下さい\n");
            layout.addView(tv);
            tv = new TextView(this);
            tv.setText("マイナス補正能力:指定パラメータの伸びに"+minus+"倍の補正がかかります\nテキスト右横の欄から選んで下さい\n");
            layout.addView(tv);
        } else if(page == 3){
            tv = new TextView(this);
            tv.setText("技の名前: 技の名前です。同じものを二回登録することも出来ます\n");
            layout.addView(tv);
            tv = new TextView(this);
            tv.setText("消費MP: 技の発動のために消費する魔力値です\n");
            layout.addView(tv);
            tv = new TextView(this);
            tv.setText("威力: 技の威力です。高いほど大きな威力になりますが大きな負荷で行動しにくくなります。\n通常攻撃は威力25に設定されています\n変化技は０にしてください\n");
            layout.addView(tv);
            tv = new TextView(this);
            tv.setText("持続ターン: 変化効果の続くターン数です。一定以上長いターンを設定すると負荷が大きくなり行動しにくくなります。\n攻撃技は０にしてください\n");
            layout.addView(tv);
            tv = new TextView(this);
            tv.setText("技の分類: 以下の通りに分けられます\n" +
                    "  直接攻撃: 近接の攻撃です。攻撃と近接命中の得意さが関係します\n" +
                    "  遠距離攻撃: 遠距離で放つ攻撃です。攻撃と遠隔命中の得意さが関係します\n" +
                    "  魔法攻撃: 魔法を使った攻撃です。魔攻と遠隔命中の得意さが関係します\n" +
                    "  --上昇: --の部分に書かれた能力を上げます\n" +
                    "  --下降: --の部分に書かれた能力を下げます\n" +
                    "  --回復: 回復技です。--の部分に書かれたものを回復します\n" +
                    "  蘇生: 蘇生技です。戦闘不能はこの属性を持つアビリティでのみ回復出来ます\n" +
                    "  拘束付加: 拘束する技です。持続ターンの間相手を動けなくさせます\n" +
                    "  毒付加: 相手を毒状態にする技です\n");
            layout.addView(tv);
            tv = new TextView(this);
            tv.setText("効果対象: 技の対象範囲です\n"+
                    "  敵単体: 敵パーティーの中の１体のみに対して行います\n" +
                    "  敵全体: 敵パーティー全体に対して行います\n" +
                    "  自分: 自分に対して行います\n" +
                    "  味方単体: 自分を含む味方の誰かに対して行います\n" +
                    "  味方全体: 自分を含む味方パーティーに対して行います\n");
            layout.addView(tv);
            tv = new TextView(this);
            tv.setText("属性: 技の属性です。炎、氷、雷、水、風、地、光、\n　　闇、無があります\n");
            layout.addView(tv);
        }*/
    }
}
