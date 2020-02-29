package com.example.kzm.partybattlegame;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

public class DBhandling extends AppCompatActivity {
    public final static int REQUEST_CODE_CHOOSER = 101;
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 102;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 104;
    File sd_dir;
    String sd_stt;
    String db_file;
    String filename = "PBGdata.csv";//db";
    private static String[] PERMISSION_READ_EXTERNAL_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE};
    private static String[] PERMISSION_WRITE_EXTERNAL_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    SQLiteDatabase db;
    MyOpenHelper hp;
    Commons commons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explain);
        ScrollView scrl = findViewById(R.id.explainscroll);
        LinearLayout layout = findViewById(R.id.explains);
        boolean b;
        hp = new MyOpenHelper(getApplicationContext());
        db = hp.getWritableDatabase();
        TextView tv=new TextView(this);
        tv.setText("\n\n何をしますか？\n");
        tv.setGravity(Gravity.CENTER);
        layout.addView(tv);
        sd_dir = getExternalFilesDir(
                Environment.DIRECTORY_DOCUMENTS);
        sd_stt=Environment.getExternalStorageState();           //SDカードの状態を取得
        db_file=db.getPath();   //DBのディレクトリとファイル名
        b=sd_stt.equals(Environment.MEDIA_MOUNTED);     //SDカードの状態
        if(b==false) {  //書込み状態でマウントされていない。
            Toast.makeText(this, "SDメモリが書込み状態でマウントされていません。", Toast.LENGTH_LONG).show();
            return;         //ディレクトリ作成失敗
        }

        Button bt = new Button(this);
        bt.setText("データベースのインポート");
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionR();
            }
        });
        Button bt2 = new Button(this);
        bt2.setText("データベースのエクスポート");
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionW();
            }
        });
        layout.addView(bt);
        layout.addView(bt2);
        Button back = findViewById(R.id.button);
        back.setText("戻る");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void checkPermissionR() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // READ_EXTERNAL_STORAGE permission has not been granted.
            requestExternalStoragePermission("R");
        } else {
            startExternalAppSelectableDB();
        }
    }

    private void checkPermissionW() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // READ_EXTERNAL_STORAGE permission has not been granted.
            requestExternalStoragePermission("W");
        } else {
            filecopy(db_file, sd_dir+"/"+filename);  //DBのファイルをSDにコピー
        }
    }

    private void requestExternalStoragePermission(String str) {
        // Contact permissions have not been granted yet. Request them directly.
        if(str.equals("R"))ActivityCompat.requestPermissions(this, PERMISSION_READ_EXTERNAL_STORAGE, this.REQUEST_READ_EXTERNAL_STORAGE);
        if(str.equals("W"))ActivityCompat.requestPermissions(this, PERMISSION_WRITE_EXTERNAL_STORAGE, this.REQUEST_WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case (DBhandling.REQUEST_READ_EXTERNAL_STORAGE):
                if (verifyPermissions(grantResults)) {
                    startExternalAppSelectableDB();
                } else {
                    Toast.makeText(this, getString(R.string.permissions_not_granted), Toast.LENGTH_LONG).show();
                }
                break;
            case (DBhandling.REQUEST_WRITE_EXTERNAL_STORAGE):
                if (verifyPermissions(grantResults)) {
                    filecopy(db_file, sd_dir+"/"+filename);  //DBのファイルをSDにコピー
                } else {
                    Toast.makeText(this, getString(R.string.permissions_not_granted), Toast.LENGTH_LONG).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    public boolean verifyPermissions(int[] grantResults) {
        // At least one result must be checked.
        if (grantResults.length < 1) {
            return false;
        }

        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void startExternalAppSelectableDB() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        String mime = "text/comma-separated-values";
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType(mime);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mime);
        }
        startActivityForResult(Intent.createChooser(intent, null), this.REQUEST_CODE_CHOOSER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case (AddCharacter.REQUEST_CODE_CHOOSER):
                if (resultCode != RESULT_OK) {
                    Toast.makeText(this, getString(R.string.dbfile_unselected_message), Toast.LENGTH_LONG).show();
                    return;
                }
                Uri file = data.getData();
                DBoverwrite(file);
                break;
            default:
                break;
        }
    }

    private void DBoverwrite(Uri file){

        String filepath = getPath(DBhandling.this, file) + "/";

        try {
            FileInputStream mInput = new FileInputStream(filepath);
            // デフォルトのデータベースパスに作成した空のDB
            OutputStream mOutput = new FileOutputStream(db_file);

            // コピー
            byte[] buffer = new byte[1024];
            int size;
            while ((size = mInput.read(buffer)) > 0) {
                mOutput.write(buffer, 0, size);
            }

            // Close the streams
            mOutput.flush();
            mOutput.close();
            mInput.close();

            SQLiteDatabase checkDb = null;
            try {
                checkDb = SQLiteDatabase.openDatabase(db_file, null, SQLiteDatabase.OPEN_READWRITE);
            } catch (SQLiteException e) {
            }

            if (checkDb != null) {
                checkDb.setVersion(db.getVersion());
                checkDb.close();
            }
            rollbackdata();
            Toast.makeText(DBhandling.this, "データベースのインポートが完了しました",Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            throw new Error("Error copying database");
        }
    }

    private void filecopy(String file_src, String file_dist) {
        int err;
        FileInputStream fis;
        FileOutputStream fos;

        err=0;
        File fi = new File(file_src);
        File fo = new File(file_dist);
        try {
            fis=new FileInputStream(fi);
            FileChannel chi = fis.getChannel();

            fos=new FileOutputStream(fo);
            FileChannel cho = fos.getChannel();

            chi.transferTo(0, chi.size(), cho);
            chi.close();
            cho.close();
        }
        catch (FileNotFoundException e) {
            err=1;
            Toast.makeText(this, "FileNotFoundException "+e.getMessage(), Toast.LENGTH_LONG).show();
        }
        catch (IOException e) {
            err=2;
            Toast.makeText(this, "IOException" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        if(err==0) {
            Toast.makeText(this, "DBをSDカードにコピーしました。\nファイル名:"+filename+
                    "\n場所:"+file_dist, Toast.LENGTH_LONG).show();
        }
    }

    public static String getPath(Context context, Uri uri) {
        ContentResolver contentResolver = context.getContentResolver();
        String[] columns = { MediaStore.Images.Media.DATA };
        Cursor cursor = contentResolver.query(uri, columns, null, null, null);
        cursor.moveToFirst();
        String path = cursor.getString(0);
        cursor.close();
        return path;
    }

    public void rollbackdata(){
        String name[]=new String[4],viewname[]=new String[4];
        String specialty[]=new String[4],resist[]=new String[4];
        int chara[]=new int[4],Lv[]=new int[4];
        int exp[]=new int[4],explimit[]=new int[4];
        double HP[]=new double[4],MP[]=new double[4];
        double atk[]=new double[4],mtk[]=new double[4];
        double def[]=new double[4],mef[]=new double[4];
        double spd[]=new double[4],acc[]=new double[4],eva[]=new double[4];
        boolean charaset[]={false,false,false,false};
        commons = (Commons)getApplication();
        Cursor c2 = db.query("lastplay", new String[]{"one", "two", "three", "four", "level","chara"}, null, null, null, null, null);
        boolean next = c2.moveToFirst();
        while (next) {
            chara[0] = c2.getInt(0);
            chara[1] = c2.getInt(1);
            chara[2] = c2.getInt(2);
            chara[3] = c2.getInt(3);
            commons.setmaxlock(c2.getInt(4));
            commons.setall(c2.getInt(5));
            next = c2.moveToNext();
        }
        c2.close();
        Cursor c = db.query("person", new String[]{"code", "name", "nameview", "Lv", "HP", "MP", "ATK", "MTK", "DEF", "MEF", "SPD", "ACC", "EVA", "specialty", "resist", "EXP", "EXPlimit"}, null, null, null, null, null);
        next = c.moveToFirst();
        for(int tmp=0;tmp<4;tmp++) {
            charaset[tmp] = false;
            while (next) {
                int n = c.getInt(0);
                if (chara[tmp] == n) {
                    name[tmp] = c.getString(1);
                    viewname[tmp] = c.getString(2);
                    Lv[tmp] = c.getInt(3);
                    HP[tmp] = c.getDouble(4);
                    MP[tmp] = c.getDouble(5);
                    atk[tmp] = c.getDouble(6);
                    mtk[tmp] = c.getDouble(7);
                    def[tmp] = c.getDouble(8);
                    mef[tmp] = c.getDouble(9);
                    spd[tmp] = c.getDouble(10);
                    acc[tmp] = c.getDouble(11);
                    eva[tmp] = c.getDouble(12);
                    specialty[tmp] = c.getString(13);
                    resist[tmp] = c.getString(14);
                    exp[tmp] = c.getInt(15);
                    explimit[tmp] = c.getInt(16);
                    charaset[tmp] = true;
                    break;
                }
                next = c.moveToNext();
            }
        }
        c.close();
        for(int s=0;s<4;s++){
            commons.person_data[s].setcharaset(charaset[s]);
            commons.person_data[s].setdataint("chara", chara[s]);
            commons.person_data[s].setdataString("name", name[s]);
            commons.person_data[s].setdataString("viewname", viewname[s]);
            commons.person_data[s].setdataint("Lv", Lv[s]);
            commons.person_data[s].setdatadouble("HP", HP[s]);
            commons.person_data[s].setdatadouble("MP", MP[s]);
            commons.person_data[s].setdatadouble("atk", atk[s]);
            commons.person_data[s].setdatadouble("mtk", mtk[s]);
            commons.person_data[s].setdatadouble("def", def[s]);
            commons.person_data[s].setdatadouble("mef", mef[s]);
            commons.person_data[s].setdatadouble("spd", spd[s]);
            commons.person_data[s].setdatadouble("acc", acc[s]);
            commons.person_data[s].setdatadouble("eva", eva[s]);
            commons.person_data[s].setdataString("specialty", specialty[s]);
            commons.person_data[s].setdataString("resist", resist[s]);
            commons.person_data[s].setdataint("exp", exp[s]);
            commons.person_data[s].setdataint("explimit", explimit[s]);
        }
    }


}
