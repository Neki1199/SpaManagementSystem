package com.sms.BackEnd;

public class GetColor {
    public static String getColor(int index) {
        String[] colors = new String[]{"#FF6666", "#3F8A3F", "#4545F1", "#FCB66D", "#793979"};
        return colors[index % colors.length];
    }
}
