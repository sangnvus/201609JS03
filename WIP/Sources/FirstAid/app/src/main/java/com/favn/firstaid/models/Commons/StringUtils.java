package com.favn.firstaid.models.Commons;

import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * Created by Hung Gia on 11/30/2016.
 */

public class StringUtils {
    public static String unAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").replaceAll("Đ", "D").replaceAll("đ", "d");
    }
}
