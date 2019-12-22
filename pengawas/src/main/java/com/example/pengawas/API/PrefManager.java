package com.example.pengawas.API;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {

    public static final String SP_Pengawas = "spPengawas";
    private static final String SP_TOKEN = null;


    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;

    public PrefManager(Context context){
        sp = context.getSharedPreferences(SP_Pengawas, Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    public void saveToken(String value){
        spEditor.putString(SP_TOKEN, value);
        spEditor.commit();
    }

    public String getToken(){
        return sp.getString(SP_TOKEN, "");
    }



}

