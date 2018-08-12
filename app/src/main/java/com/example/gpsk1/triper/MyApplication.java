package com.example.gpsk1.triper;

import android.app.Application;

public class MyApplication extends Application {
    // 전역 변수 설정
    private int mode; // 관광객 - 0 / 가이드 - 1

    public int getMode(){
        return mode;
    }

    public void setMode(int val){
        this.mode = val;
    }
}
