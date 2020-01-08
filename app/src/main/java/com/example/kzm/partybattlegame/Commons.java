package com.example.kzm.partybattlegame;

import android.app.Application;
import android.content.Intent;
import android.os.Handler;
import android.view.View;

public class Commons extends Application{
    private final Handler handler = new Handler();
    private Runnable runnable;
    static String name[]=new String[4],viewname[]=new String[4];
    static String specialty[]=new String[4],resist[]=new String[4];
    static int chara[]=new int[4],Lv[]=new int[4];
    static int exp[]=new int[4],explimit[]=new int[4];
    static double HP[]=new double[4],MP[]=new double[4];
    static double atk[]=new double[4],mtk[]=new double[4];
    static double def[]=new double[4],mef[]=new double[4];
    static double spd[]=new double[4],acc[]=new double[4],eva[]=new double[4];
    static boolean charaset[]=new boolean[4];
    static int all,maxlock;
    static double expmul;
    MoveOption MO;
    MoveMain MM;
    MoveChara MC;
    MoveAdd MA;

    @Override
    public void onCreate(){
        super.onCreate();
        all=0;
        for(int s=0;s<4;s++){
            Commons.chara[s]=0;
            Commons.name[s]="";
            Commons.viewname[s]="";
            Commons.Lv[s]=0;
            Commons.HP[s]=0;
            Commons.MP[s]=0;
            Commons.atk[s]=0;
            Commons.mtk[s]=0;
            Commons.def[s]=0;
            Commons.mef[s]=0;
            Commons.spd[s]=0;
            Commons.acc[s]=0;
            Commons.eva[s]=0;
            Commons.specialty[s]="";
            Commons.resist[s]="";
            Commons.exp[s]=0;
            Commons.explimit[s]=1;
            Commons.charaset[s]=false;
        }
    }

    //全データ送信
    //chara,name,viewname,Lv,HP,MP,atk,mtk,def,mef,spd,acc,eva,specialty,resist,exp,explimit,charaset
    public void datatransmission
    (int a[],String b[],String c[],int d[],double e[],double f[],
     double g[],double h[],double i[],double j[],double k[],double l[],double m[],
     String n[],String o[],int p[],int q[],boolean r[]){
        for(int s=0;s<4;s++){
            Commons.chara[s]=a[s];
            Commons.name[s]=b[s];
            Commons.viewname[s]=c[s];
            Commons.Lv[s]=d[s];
            Commons.HP[s]=e[s];
            Commons.MP[s]=f[s];
            Commons.atk[s]=g[s];
            Commons.mtk[s]=h[s];
            Commons.def[s]=i[s];
            Commons.mef[s]=j[s];
            Commons.spd[s]=k[s];
            Commons.acc[s]=l[s];
            Commons.eva[s]=m[s];
            Commons.specialty[s]=n[s];
            Commons.resist[s]=o[s];
            Commons.exp[s]=p[s];
            Commons.explimit[s]=q[s];
            Commons.charaset[s]=r[s];
        }
    }

    //全データ受信
    //chara,name,viewname,Lv,HP,MP,atk,mtk,def,mef,spd,acc,eva,specialty,resist,exp,explimit,charaset
    public void datareception
     (int a[],String b[],String c[],int d[],double e[],double f[],
      double g[],double h[],double i[],double j[],double k[],double l[],double m[],
      String n[],String o[],int p[],int q[],boolean r[]){
        for(int s=0;s<4;s++){
            a[s]=Commons.chara[s];
            b[s]=Commons.name[s];
            c[s]=Commons.viewname[s];
            d[s]=Commons.Lv[s];
            e[s]=Commons.HP[s];
            f[s]=Commons.MP[s];
            g[s]=Commons.atk[s];
            h[s]=Commons.mtk[s];
            i[s]=Commons.def[s];
            j[s]=Commons.mef[s];
            k[s]=Commons.spd[s];
            l[s]=Commons.acc[s];
            m[s]=Commons.eva[s];
            n[s]=Commons.specialty[s];
            o[s]=Commons.resist[s];
            p[s]=Commons.exp[s];
            q[s]=Commons.explimit[s];
            r[s]=Commons.charaset[s];
        }
    }

    //背景セット
    public void backinit(final MainView myView) {
        runnable = new Runnable() {
            @Override
            public void run() {
                myView.changeCirclePosition();

                handler.postDelayed(this, 25);
            }
        };

        handler.post(runnable);
    }

    public void setall(int a){
        Commons.all = a;
    }

    public int getall(){
        return Commons.all;
    }

    public void setexpmul(double a){
        Commons.expmul = a;
    }

    public double getexpmul(){
        return Commons.expmul;
    }

    public void setmaxlock(int a){
        Commons.maxlock = a;
    }

    public int getmaxlock(){
        return Commons.maxlock;
    }

    public void makebutton(){
        MO = new MoveOption();
        MM = new MoveMain();
        MC = new MoveChara();
        MA = new MoveAdd();
    }

    public class MoveOption implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent i = new Intent(getApplicationContext(),OptionActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
    }

    public class MoveMain implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("movemain",true);
            startActivity(i);
        }
    }

    public class MoveChara implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent i = new Intent(getApplicationContext(),CharaSelect.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
    }

    public class MoveAdd implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent i = new Intent(getApplicationContext(),AdditionsActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
    }

}
