package com.jasen.kimjaeseung.homebase.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by kimjaeseung on 2018. 1. 7..
 */

public class ToastUtils {

    public static void showToast(Context context,String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
