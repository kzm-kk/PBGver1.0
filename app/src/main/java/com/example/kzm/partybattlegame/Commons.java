package com.example.kzm.partybattlegame;

import android.app.Application;
import android.os.Handler;


/**
 * グローバル変数を扱うクラス
 * Created by XXXXX on 20XX/XX/XX.
 */
public class Commons extends Application{

    private final Handler handler = new Handler();
    private Runnable runnable;

    /**
     * 変数を初期化する
     */
    public void init(final MainView myView) {
        runnable = new Runnable() {
            @Override
            public void run() {
                myView.changeCirclePosition();

                handler.postDelayed(this, 25);
            }
        };

        handler.post(runnable);
    }
}
