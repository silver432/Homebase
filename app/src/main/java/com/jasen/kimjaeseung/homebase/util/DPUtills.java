package com.jasen.kimjaeseung.homebase.util;

import android.content.Context;

/**
 * Created by kimjaeseung on 2018. 5. 14..
 */

public class DPUtills {

    public static int getPx(Context context,int dimensionDp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dimensionDp * density + 0.5f);
    }
}
