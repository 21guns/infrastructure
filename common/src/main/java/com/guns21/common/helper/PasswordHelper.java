package com.guns21.common.helper;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.RandomStringGenerator;

import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.Random;

public class PasswordHelper {
    private static final int SALT_SIZE_IN_BYTES = 16;
    private static final int PASSWORDLENGTH = 6;

    private static final RandomStringGenerator generator = new RandomStringGenerator.Builder()
            .withinRange('a', 'z')
            .withinRange('A', 'Z')
            .withinRange(0, 9)
            .build();

    // Generate salt to secure password encryption
    public static String generateSalt() {
        byte[] buffer = new byte[SALT_SIZE_IN_BYTES];

//        (new RNGCryptoServiceProvider()).GetBytes(buffer);
//        return Convert.ToBase64String(buffer);

        SecureRandom random = new SecureRandom();
        random.nextBytes(buffer);
        return Base64.encodeBase64String(buffer);
    }

    /**
     * 0:salt
     * 1:password
     */
    public static String[] encodeBySalt() {
        return encodeBySalt(generator.generate(PASSWORDLENGTH));
    }

    public static String[] encodeBySalt(String pwd) {
        if (StringUtils.isBlank(pwd)) {
            pwd = "super123";//default password
        }

        String salt = generateSalt();//盐1
        String salt_ = new StringBuffer(salt).reverse().toString();//盐2

        pwd = salt_ + new StringBuffer(pwd).reverse().toString() + salt;
        pwd = DigestUtils.sha1Hex(DigestUtils.md5Hex(pwd));

        String[] result = new String[2];
        result[0] = salt;
        result[1] = pwd;

        return result;
    }

    public static boolean validBySalt(String pwd, String encryptPwd, String salt) {
        if (StringUtils.isBlank(salt)) {
            return false;
        }
        if (StringUtils.isBlank(pwd)) {
            pwd = "super123";//default password
        }

        String salt_ = new StringBuffer(salt).reverse().toString();//盐2

        pwd = salt_ + new StringBuffer(pwd).reverse().toString() + salt;
        pwd = DigestUtils.sha1Hex(DigestUtils.md5Hex(pwd));

        return encryptPwd.equalsIgnoreCase(pwd.toUpperCase());
    }

    public static String sHA1(String message) {
//    HashAlgorithm s = HashAlgorithm.Create("SHA1");
//    byte[] bRet = s.ComputeHash(Encoding.Unicode.GetBytes(message));
//    return Convert.ToBase64String(bRet);
        return String.valueOf(DigestUtils.sha1(message));
    }

    public static String uTF8SHA1(String message) {
//    HashAlgorithm s = HashAlgorithm.Create("SHA1");
//    byte[] bRet = s.ComputeHash(Encoding.UTF8.GetBytes(message));
//    return Convert.ToBase64String(bRet);

        return String.valueOf(DigestUtils.sha1(message.getBytes(Charset.forName("UTF-8"))));
    }


    //     Encode password
    public static String encodePassword(String user, String pass, String salt) {
//    byte[] bUser = Encoding.Unicode.GetBytes(user);
//    byte[] bSalt = Convert.FromBase64String(salt);
//    byte[] bPass = Encoding.Unicode.GetBytes(pass);
//    byte[] bAll = new byte[bUser.Length + bSalt.Length + bPass.Length];
//    byte[] bRet = null;
//
//    Buffer.BlockCopy(bUser, 0, bAll, 0, bUser.Length);
//    Buffer.BlockCopy(bSalt, 0, bAll, bUser.Length, bSalt.Length);
//    Buffer.BlockCopy(bPass, 0, bAll, bUser.Length + bSalt.Length, bPass.Length);
//
//    HashAlgorithm s = HashAlgorithm.Create("SHA1");
//    bRet = s.ComputeHash(bAll);
//    return Convert.ToBase64String(bRet);

        byte[] bUser = user.getBytes(Charset.forName("UTF-16LE"));
        byte[] bSalt = Base64.decodeBase64(salt);
        byte[] bPass = pass.getBytes(Charset.forName("UTF-16LE"));
        byte[] bAll = new byte[bUser.length + bSalt.length + bPass.length];
        byte[] bRet = null;

        System.arraycopy(bUser, 0, bAll, 0, bUser.length);
        System.arraycopy(bSalt, 0, bAll, bUser.length, bSalt.length);
        System.arraycopy(bPass, 0, bAll, bUser.length + bSalt.length, bPass.length);
        bRet = DigestUtils.sha1(bAll);
        return Base64.encodeBase64String(bRet);
    }

    private static final String passwordChars = "34678abdefgjmprtwy";
    private static Random random = new Random();

    public static String randomPassword(int length) {
        StringBuffer password = new StringBuffer();
        for (int i = 0; i < length; i++) {
            password.append(passwordChars.charAt(random.nextInt(passwordChars.length())));
        }
        return password.toString();
    }

    public static final String Alphabet = "abcdefghijklmnopqrstuvwyxzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static String randomAlphabetString(int length) {
        StringBuffer password = new StringBuffer();
        for (int i = 0; i < length; i++) {
            password.append(Alphabet.charAt(random.nextInt(Alphabet.length())));
        }
        return password.toString();
    }

    public static void main(String[] args) {
        for (String s :
                PasswordHelper.encodeBySalt()) {
            System.err.println(s);
        }
    }
}
