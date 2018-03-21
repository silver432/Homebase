package com.jasen.kimjaeseung.homebase.schedule;

import android.widget.ImageView;

/**
 * Created by kimjaeseung on 2018. 3. 21..
 */

public class PHListItem {
    private ImageView minus;
    private String menu;
    private int num;
    private ImageView plus;

    public PHListItem(){}

    public PHListItem(ImageView minus, String menu, int num, ImageView plus) {
        this.minus = minus;
        this.menu = menu;
        this.num = num;
        this.plus = plus;
    }

    public ImageView getMinus() {
        return minus;
    }

    public void setMinus(ImageView minus) {
        this.minus = minus;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public ImageView getPlus() {
        return plus;
    }

    public void setPlus(ImageView plus) {
        this.plus = plus;
    }
}
