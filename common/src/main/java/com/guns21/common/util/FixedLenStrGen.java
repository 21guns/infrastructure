package com.guns21.common.util;

import java.util.Random;

/**
 * TODO 移除
 * 生成指定长度的字符串
 * Created by ljj on 17/7/5.
 */
public class FixedLenStrGen {
    private static String[] uppercase = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    /**
     * 生成指定长度的大写字符串
     * @param length
     * @return
     */
    public static String GenFixedLenUppercaseString(int length){
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++){
            builder.append(uppercase[random.nextInt(26)]);
        }

        return builder.toString();
    }
}
