package com.example.kzm.partybattlegame;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class AddCharacter extends AppCompatActivity {
    public final static int REQUEST_CODE_CHOOSER = 101;
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 102;
    public final static int REQUEST_CODE_CROP = 103;

    public static final List<String> types = Collections
            .unmodifiableList(new LinkedList<String>() {
                {
                    add("image/jpeg");
                    add("image/jpg");
                    add("image/png");
                }
            });

    private static String[] PERMISSION_READ_EXTERNAL_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE};
    private ImageView selectedImage;
    MyOpenHelper hp;
    SQLiteDatabase db;
    Cursor c;
    TextView tv;
    Intent i;
    int all,level = 1,n,tmp,code;
    double expmul;
    Spinner sp,sp2,sp3;
    EditText et[]=new EditText[12];
    SpannableStringBuilder sb[] = new SpannableStringBuilder[12];
    Bitmap bitmap;
    LinearLayout layout;
    LinearLayout.LayoutParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_character);
        TextView stv = findViewById(R.id.specialtytext);
        TextView thistv = findViewById(R.id.thiswindow);
        thistv.setText("この画面では初期ステータスを設定します");
        tv = findViewById(R.id.limitmessage);
        tv.setText("Lv以外のパラメータは\nLv1時点のステータスを\n設定してください\n次の画面で設定する\n成長度合を用いて\n最終パラメータを\n算出します");
        TextView cut = findViewById(R.id.cuttext);
        TextView twice = findViewById(R.id.twicetext);
        i = this.getIntent();
        hp = new MyOpenHelper(this);
        db = hp.getWritableDatabase();
        sp = findViewById(R.id.specialtysp);
        sp2 = findViewById(R.id.cutsp);
        sp3 = findViewById(R.id.twicesp);
        all = i.getIntExtra("allchara", 20);
        expmul = i.getDoubleExtra("expmul", 1.2);
        et[0] = findViewById(R.id.fullname);
        et[1] = findViewById(R.id.viewname);
        et[2] = findViewById(R.id.level);
        et[3] = findViewById(R.id.HPnum);
        et[4] = findViewById(R.id.MPnum);
        et[5] = findViewById(R.id.ATKnum);
        et[6] = findViewById(R.id.MTKnum);
        et[7] = findViewById(R.id.DEFnum);
        et[8] = findViewById(R.id.MEFnum);
        et[9] = findViewById(R.id.SPDnum);
        et[10] = findViewById(R.id.ACCnum);
        et[11] = findViewById(R.id.EVAnum);
        tmp=1;
        code=1;
        c = db.query("person", new String[]{"code", "name", "nameview", "Lv", "HP", "MP", "ATK", "MTK", "DEF", "MEF", "SPD", "ACC", "EVA", "specialty", "resist", "EXP", "EXPlimit"}, null, null, null, null, null);
        boolean next = c.moveToFirst();
        while (next) {
            n = c.getInt(0);
            if ((n-tmp)>0) {
                break;
            }
            tmp++;
            code = tmp;
            next = c.moveToNext();
        }
        c.close();
        Button stop = findViewById(R.id.buttons1).findViewById(R.id.stopbutton);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Button fab = (Button) findViewById(R.id.buttons1).findViewById(R.id.nextbutton);
        fab.setText("次へ");
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                byte[] bytegazo = bos.toByteArray();
                i = new Intent(AddCharacter.this, AddGrows.class);
                i.putExtra("allchara", all);
                i.putExtra("code",code);
                i.putExtra("image", bytegazo);
                i.putExtra("fullname",((SpannableStringBuilder) et[0].getText()).toString());
                i.putExtra("viewname",((SpannableStringBuilder) et[1].getText()).toString());
                i.putExtra("Lv", Integer.parseInt(((SpannableStringBuilder) et[2].getText()).toString()));
                i.putExtra("HP", Integer.parseInt(((SpannableStringBuilder) et[3].getText()).toString()));
                i.putExtra("MP", Integer.parseInt(((SpannableStringBuilder) et[4].getText()).toString()));
                i.putExtra("ATK", Integer.parseInt(((SpannableStringBuilder) et[5].getText()).toString()));
                i.putExtra("MTK", Integer.parseInt(((SpannableStringBuilder) et[6].getText()).toString()));
                i.putExtra("DEF", Integer.parseInt(((SpannableStringBuilder) et[7].getText()).toString()));
                i.putExtra("MEF", Integer.parseInt(((SpannableStringBuilder) et[8].getText()).toString()));
                i.putExtra("SPD", Integer.parseInt(((SpannableStringBuilder) et[9].getText()).toString()));
                i.putExtra("ACC", Integer.parseInt(((SpannableStringBuilder) et[10].getText()).toString()));
                i.putExtra("EVA", Integer.parseInt(((SpannableStringBuilder) et[11].getText()).toString()));
                i.putExtra("specialty", specialtyset());
                i.putExtra("resist",resistset());
                i.putExtra("explimit",(int)(27 * Math.pow(expmul,Integer.parseInt(((SpannableStringBuilder) et[2].getText()).toString())-1)));
                if(elementflags()) startActivity(i);
            }
        });
        layout = findViewById(R.id.charalayout);
        selectedImage = new ImageView(this);
        params = new LinearLayout.LayoutParams(pxFromDp((float)141,this),pxFromDp((float)141,this));
        selectedImage.setLayoutParams(params);
        layout.addView(selectedImage);
        Button btnSelectImage = (Button) findViewById(R.id.decideimage);
        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });
        Button bt = (Button) findViewById(R.id.buttons1).findViewById(R.id.explain);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = new Intent(AddCharacter.this, ExplainActivity.class);
                i.putExtra("page",1);
                startActivity(i);
            }
        });
    }

    public String specialtyset(){
        int num = sp.getSelectedItemPosition();
        switch (num){
            case 0:
                return "SD";
            case 1:
                return "LD";
            default:
                return "DD";
        }
    }

    public String resistset(){
        String str = "";
        for(int i=0;i<8;i++){
            if(i==sp2.getSelectedItemPosition()) str=str.concat("-1");
            else if(i==sp3.getSelectedItemPosition()) str=str.concat("1");
            else str=str.concat("0");
            if(i<7) str=str.concat(",");
        }
        return str;
    }

    public boolean elementflags(){
        int check1 = sp2.getSelectedItemPosition();
        int check2 = sp3.getSelectedItemPosition();
        if(check1==check2 && check1<8){
            Toast.makeText(AddCharacter.this,
                    "エラー：半減属性と弱点属性が同じです",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        else return true;
    }

    public static int pxFromDp(float dp, Context context){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5f);
    }

    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // READ_EXTERNAL_STORAGE permission has not been granted.
            requestExternalStoragePermission();
        } else {
            startExternalAppSelectableImage();
        }
    }

    private void requestExternalStoragePermission() {
        // Contact permissions have not been granted yet. Request them directly.
        ActivityCompat.requestPermissions(this, PERMISSION_READ_EXTERNAL_STORAGE, this.REQUEST_READ_EXTERNAL_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case (AddCharacter.REQUEST_READ_EXTERNAL_STORAGE):
                if (verifyPermissions(grantResults)) {
                    startExternalAppSelectableImage();
                } else {
                    Toast.makeText(this, getString(R.string.permissions_not_granted), Toast.LENGTH_LONG).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    /**
     * Check that all given permissions have been granted by verifying that each entry in the
     * given array is of the value {@link PackageManager#PERMISSION_GRANTED}.
     */
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

    /**
     * Called when the '画像を選択する' button is clicked.
     */
    private void startExternalAppSelectableImage() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        // Filter to only show results that can be "opened", such as a
        // file (as opposed to a list of contacts or timezones)
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.putExtra(Intent.EXTRA_MIME_TYPES, types.toArray());
        }
        startActivityForResult(Intent.createChooser(intent, null), this.REQUEST_CODE_CHOOSER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case (AddCharacter.REQUEST_CODE_CHOOSER):
                if (resultCode != RESULT_OK) {
                    Toast.makeText(this, getString(R.string.image_unselected_message), Toast.LENGTH_LONG).show();
                    return;
                }
                Uri file = data.getData();
                startCrop(file);
                break;
            case (REQUEST_CODE_CROP):
                if (resultCode != RESULT_OK) {
                    Toast.makeText(this, getString(R.string.crop_image_failure_message), Toast.LENGTH_LONG).show();
                    return;
                }
                selectedImage.setImageURI(data.getData());
                bitmap = ((BitmapDrawable)selectedImage.getDrawable()).getBitmap();
                deleteExternalStoragePublicPicture();
                break;
            default:
                break;
        }
    }

    private void startCrop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //intent.putExtra("aspectX", 16);
        //intent.putExtra("aspectY", 9);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("scale", "true");
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.name());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getExternalStorageTempStoreFilePath()));
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivityForResult(intent, AddCharacter.REQUEST_CODE_CROP);
    }

    /**
     * 一時保存ファイルパスを取得する
     *
     * @return
     */
    private File getExternalStorageTempStoreFilePath() {
        File path = getExternalFilesDir(
                Environment.DIRECTORY_PICTURES);
        File file = new File(path, "selected_temp_image.jpg");
        return file;
    }

    /**
     * Delete temporary stored file.
     */
    private void deleteExternalStoragePublicPicture() {
        // Create a path where we will place our picture in the user's
        // public pictures directory and delete the file.  If external
        // storage is not currently mounted this will fail.
        File file = getExternalStorageTempStoreFilePath();
        if (file != null) {
            // Log.d("ImageSelectionCropDemo", file.getAbsolutePath() + " is " + file.exists());
            if (!file.delete()) {
                Log.e("ImageSelectionCropDemo", "File deletion failed.");
            }
        }
    }

    @Override
    public void onBackPressed() {
    }
}