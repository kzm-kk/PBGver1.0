package com.example.kzm.partybattlegame;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import java.net.URI;

public class MyContentProvider extends ContentProvider {
    private MyOpenHelper helper;
    /*String PATH = "content://com.example.kzm.try3.status/m_category";
    private Uri uri=Uri.parse(PATH);*/

    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = helper.getWritableDatabase();
        final int count=db.delete(uri.getPathSegments().get(0),selection,selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        //MiMEタイプのリクエスト
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = helper.getWritableDatabase();

        long newRowId = db.insert(uri.getPathSegments().get(0), null, values);
        Uri returnUri = ContentUris.withAppendedId(uri, newRowId);

        return returnUri;

    }

    @Override
    public boolean onCreate() {
        helper=new MyOpenHelper(getContext());//ヘルパーインスタンの作成
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(uri.getPathSegments().get(0));

        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor c = db.query(uri.getPathSegments().get(0)//テーブル名,
                , null, null, null, null, null, null);

        Log.d("ContentsPro","");
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,String[] selectionArgs) {
        SQLiteDatabase db = helper.getWritableDatabase();
        final int count = db.update(uri.getPathSegments().get(0),values,selection,selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }


}