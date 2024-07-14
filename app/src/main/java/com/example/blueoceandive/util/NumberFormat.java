package com.example.blueoceandive.util;


import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicLong;

public class NumberFormat {

    public static String format(Integer price) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return "IDR " + formatter.format(price);
    }

    public static String format(Long price) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return "IDR " + formatter.format(price);
    }

    public static String format(AtomicLong price) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return "IDR " + formatter.format(price);
    }
}
