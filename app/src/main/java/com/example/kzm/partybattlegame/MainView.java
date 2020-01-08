package com.example.kzm.partybattlegame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

public class MainView extends View {
    Paint paint;
    int x, y, dx, dy,x2,y2,dx2,dy2,x3,y3,x4,y4;
    int width, height;

    public MainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        Random rnd=new Random();
        int r = rnd.nextInt(200)+1;
        x= 0; y = 0; dx = 10; dy = 10; x2=r; y2=r; dx2=5; dy2=5;
    }

    public void changeCirclePosition() {
        if(x<0 || x > width) dx = -dx;
        if(y<0 || y > height) dy = -dy;
        x += dx;
        y += dy;
        if(x2<0 || x2 > width) dx2 = -dx2;
        if(y2<0 || y2 > height) dy2 = -dy2;
        x2 += dx2;
        y2 += dy2;
        x3 += dx2;
        y3 += dy;
        x4 += dx;
        y4 += dy2;
        this.invalidate();
    }

    @Override
    protected void onDraw (Canvas canvas){
        super.onDraw(canvas);
        width = canvas.getWidth();  // Canvasの横幅の最大値を取得
        height = canvas.getHeight(); // Canvasの縦幅の最大値を取得
        canvas.drawColor(Color.parseColor("#66CDAA"));

        // 円
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(30);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        // (x1,y1,r,paint) 中心x1座標, 中心y1座標, r半径
        canvas.drawCircle(x, y, 10, paint);

        paint.setStrokeWidth(30);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        // (x1,y1,r,paint) 中心x1座標, 中心y1座標, r半径
        canvas.drawCircle(x2, y2, 10, paint);

        paint.setStrokeWidth(30);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        // (x1,y1,r,paint) 中心x1座標, 中心y1座標, r半径
        canvas.drawCircle(x3, y3, 15, paint);

        paint.setStrokeWidth(30);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        // (x1,y1,r,paint) 中心x1座標, 中心y1座標, r半径
        canvas.drawCircle(x4, y4, 5, paint);

    }
}
