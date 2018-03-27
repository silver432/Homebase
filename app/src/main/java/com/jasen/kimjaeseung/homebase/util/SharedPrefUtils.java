package com.jasen.kimjaeseung.homebase.util;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by kimjaeseung on 2018. 3. 27..
 */

public class SharedPrefUtils {
    public static String getTeamCode(Context context){
        //get teamcode in local
        SharedPreferences pref = context.getSharedPreferences("teamPref", MODE_PRIVATE);
        return pref.getString("teamCode", "");
    }

    public static void storeTeamCode(Context context,String teamCode){
        //store teamcode in local
        SharedPreferences sharedPref = context.getSharedPreferences("teamPref",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("teamCode",teamCode);
        editor.apply();
    }
}
