package com.guns21.common.helper;

import java.security.MessageDigest;
import java.util.Random;

/**
 * TODO 移除
 * 用户权限管理中的hash操作
 * Created by ljj on 17/5/17.
 */
public class UserEncrypt {
    private static String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};

    /**
     * 由于进行hash的系统级哈希因子，初始完成后，不能修改。（增加该key将造成一些耦合）
     */
    private static final String ENCRYPTKEY = "123456";

    /**
     * 生成10000-1000000之间的随机数作为哈希salt
     *
     * @return
     */
    public static int getSalt() {
        return new Random().nextInt(1000000) % (1000000 - 10000 + 1) + 10000;
    }

    /**
     * 用户密码hash机制，采用两次hash方法，并结合USERS表中的salt字段和固定的ENCRYPTKEY值来混合hash。
     * 先进行SHA-256进行哈希，然后在使用MD5进行哈希，最后转成32位长度的字符串值返回。
     *
     * @param salt     创建用户时随机生成的随机数
     * @param password 用户录入的原始密码
     * @return
     * @throws Exception
     */
    public static String encryptUserPassword(int salt, String password) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        MessageDigest sh256 = MessageDigest.getInstance("SHA-256");

        StringBuilder builder = new StringBuilder();
        builder.append(password);
        builder.append(salt);
        //builder.append(ENCRYPTKEY);

        byte[] shBytes = sh256.digest(builder.toString().getBytes());
        byte[] md5Bytes = md5.digest(shBytes);

        return byteArrayToHexString(md5Bytes);
    }

    /**
     * 字节数组转成十六进制字符串
     *
     * @param b
     * @return
     */
    private static String byteArrayToHexString(byte[] b) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            int n = b[i];
            if (n < 0) {
                n += 256;
            }
            int d1 = n / 16;
            int d2 = n % 16;
            result.append(hexDigits[d1] + hexDigits[d2]);
        }
        return result.toString();
    }

}
