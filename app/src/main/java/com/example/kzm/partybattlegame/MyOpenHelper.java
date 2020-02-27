package com.example.kzm.partybattlegame;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyOpenHelper extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;
    public MyOpenHelper(Context context) {
        super(context, "status", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table person(" +"code integer primary key autoincrement,"+ "name text," + "nameview text," + "Lv integer,"
                + "HP real,"+"MP real,"+"ATK real,"+"MTK real,"+"DEF real,"+"MEF real,"
                +"SPD real,"+"ACC real,"+"EVA real,"+"specialty text,"+"resist text,"+"EXP integer,"+"EXPlimit integer"+");");

        db.execSQL("create table personimage("+"code integer,"+"charaimage BLOB"+");");

        db.execSQL("create table learning("+"code integer,"+"abilityID integer,"+"releaselv integer"+");");

        db.execSQL("create table growtable(" +"code integer,"
                + "HP integer,"+"MP integer,"+"ATK integer,"+"MTK integer,"+"DEF integer,"+"MEF integer,"
                +"SPD integer,"+"ACC integer,"+"EVA integer,"+"plus text,"+"minus text"+");");

        db.execSQL("create table enemy("+ "code integer,"+"name text,"+"HP integer,"
                +"MP integer,"+"ATK integer,"+"MTK integer,"+"DEF integer,"+"MEF integer,"
                +"SPD integer,"+"ACC integer,"+"EVA integer,"+"resist text,"+"explus integer"+");");

        db.execSQL("create table Elearning("+"code integer,"+"abilityID integer,"+"level integer"+");");

        db.execSQL("create table ability("+ "abilityID integer primary key autoincrement,"+"name text," +"usemp integer,"+"power real,"
                +"turn integer," +"type text,"+"target text,"+"element text"+");");

        db.execSQL("create table lastplay("+"one integer,"+"two integer,"+"three integer,"+"four integer,"+"level integer,"+"chara integer"+");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
