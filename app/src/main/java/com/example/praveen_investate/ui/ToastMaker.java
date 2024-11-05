package com.example.praveen_investate.ui;

import android.content.Context;
import android.widget.Toast;

public class ToastMaker {
    public static void toast(String str, Context ct){
        Toast.makeText(ct,str,Toast.LENGTH_LONG).show();
    }
}
