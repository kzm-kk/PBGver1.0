package com.example.kzm.partybattlegame;

import android.app.Application;
import android.content.Intent;
import android.os.Handler;
import android.view.View;

public class Commons extends Application{
    private final Handler handler = new Handler();
    private Runnable runnable;
    static int all,maxlock;
    static double expmul;
    MoveOption MO;
    MoveMain MM;
    MoveChara MC;
    MoveAdd MA;
    static class person{
        private int chara, Lv, exp, explimit;
        private double HP, MP, atk, mtk, def, mef, spd, acc, eva;
        private boolean charaset;
        private String name, viewname, specialty, resist;
        public person(){
            this.charaset = false;
            this.chara = 0;
            this.name = "";
            this.viewname = "";
            this.specialty = "";
            this.resist = "";
        }
        public int getdataint(String str){
            switch (str){
                case "chara":
                    return this.chara;
                case "Lv":
                    return this.Lv;
                case "exp":
                    return this.exp;
                case "explimit":
                    return this.explimit;
                default://基本使わない
                    return 1;
            }
        }

        public double getdatadouble(String str){
            switch (str){
                case "HP":
                    return this.HP;
                case "MP":
                    return this.MP;
                case "atk":
                    return this.atk;
                case "mtk":
                    return this.mtk;
                case "def":
                    return this.def;
                case "mef":
                    return this.mef;
                case "spd":
                    return this.spd;
                case "acc":
                    return this.acc;
                case "eva":
                    return this.eva;
                default://基本使わない
                    return 1.0;
            }
        }

        public String getdataString(String str){
            switch (str){
                case "name":
                    return this.name;
                case "viewname":
                    return this.viewname;
                case "specialty":
                    return this.specialty;
                case "resist":
                    return this.resist;
                default://基本使わない
                    return " ";
            }
        }

        public boolean getcharaset(){
            return this.charaset;
        }

        public void setdataint(String str, int data){
            switch (str){
                case "chara":
                    this.chara = data;
                    break;
                case "Lv":
                    this.Lv = data;
                    break;
                case "exp":
                    this.exp = data;
                    break;
                case "explimit":
                    this.explimit = data;
                    break;
                default://基本使わない
                    break;
            }
        }

        public void setdatadouble(String str, double data){
            switch (str){
                case "HP":
                    this.HP = data;
                    break;
                case "MP":
                    this.MP = data;
                    break;
                case "atk":
                    this.atk = data;
                    break;
                case "mtk":
                    this.mtk = data;
                    break;
                case "def":
                    this.def = data;
                    break;
                case "mef":
                    this.mef = data;
                    break;
                case "spd":
                    this.spd = data;
                    break;
                case "acc":
                    this.acc = data;
                    break;
                case "eva":
                    this.eva = data;
                    break;
                default://基本使わない
                    break;
            }
        }

        public void setdataString(String str, String data){
            switch (str){
                case "name":
                    this.name = data;
                    break;
                case "viewname":
                    this.viewname = data;
                    break;
                case "specialty":
                    this.specialty = data;
                    break;
                case "resist":
                    this.resist = data;
                    break;
                default://基本使わない
                    break;
            }
        }

        public void setcharaset(boolean charaset){
            this.charaset = charaset;
        }

    }
    static person[] person_data = new person[4];

    static class person_inbattle extends person{
        private int HPremain, MPremain, HPMAX, MPMAX;
        private boolean death;
        public person_inbattle(){
            this.HPremain = 0;
            this.MPremain = 0;
            this.HPMAX = 0;
            this.MPMAX = 0;
            this.death = true;
        }

        public void MAX_HPwrite(double HP){
            this.HPMAX = (int)HP;
        }

        public void MAX_MPwrite(double MP){
            this.MPMAX = (int)MP;
        }

        public int HPMP_MAX_read(String str){
            if(str.equals("HP")) {
                return this.HPMAX;
            } else if(str.equals("HP")){
                return this.MPMAX;
            } else {
                return 1;
            }
        }

        public void remaining_HPwrite(int nowHP){
            this.HPremain = nowHP;
        }

        public void remaining_MPwrite(int nowMP){
            this.MPremain = nowMP;
        }

        public int HPMP_remaining_read(String str){
            if(str.equals("HP")) {
                return this.HPremain;
            } else if(str.equals("MP")){
                return this.MPremain;
            } else {
                return 1;
            }
        }

        public boolean getDeath(){
            return this.death;
        }

        public void setDeath(boolean death){
            this.death = death;
        }

    }
    static person_inbattle[] person_data_inbattle = new person_inbattle[4];

    @Override
    public void onCreate(){
        super.onCreate();
        init();
    }

    public void init(){
        all=0;
        for(int s=0;s<4;s++){
            person_data[s] = new person();
            person_data_inbattle[s] = new person_inbattle();
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