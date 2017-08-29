package com.guns21.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.guns21.common.constant.RegexConstants.MOBILE_REGEXP;
import static com.guns21.common.constant.RegexConstants.PASSWORD_REGEXP;

/**
 * Created by wang on 16/4/7.
 */
public class RegexChkUtils {


    public static boolean startCheck(String reg, String string) {
        boolean tem = false;

        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(string);

        tem = matcher.matches();
        return tem;
    }


    /**
     * 检验整数,适用于正整数、负整数、0，负整数不能以-0开头.
     * 正整数不能以0开头
     */
    public static boolean checkNr(String nr) {
        String reg = "^(-?)[1-9]+\\d*|0";
        return startCheck(reg, nr);
    }

    /**
     * 手机号码验证,11位，不知道详细的手机号码段，只是验证开头必须是1和位数.
     */
    public static boolean checkCellPhone(String cellPhoneNr) {
        return startCheck(MOBILE_REGEXP, cellPhoneNr);
    }

    /**
     * 检验空白符.
     */
    public static boolean checkWhiteLine(String line) {
        String regex = "(\\s|\\t|\\r)+";

        return startCheck(regex, line);
    }


    /**
     * 检查EMAIL地址.
     * 用户名和网站名称必须>=1位字符
     * 地址结尾必须是以com|cn|com|cn|net|org|gov|gov.cn|edu|edu.cn结尾
     */
    public boolean checkEmailWithSuffix(String email) {
        String regex = "\\w+\\@\\w+\\.(com|cn|com.cn|net|org|gov|gov.cn|edu|edu.cn)";

        return startCheck(regex, email);
    }


    /**
     * 检查EMAIL地址.
     * 用户名和网站名称必须>=1位字符
     * 地址结尾必须是2位以上，如：cn,test,com,info
     */
    public static boolean checkEmail(String email) {
        String regex = "\\w+\\@\\w+\\.\\w{2,}";

        return startCheck(regex, email);
    }

    /**
     * 检查邮政编码(中国),6位，第一位必须是非0开头，其他5位数字为0-9.
     */
    public static boolean checkPostcode(String postCode) {
        String regex = "^[1-9]\\d{5}";
        return startCheck(regex, postCode);
    }

    /**
     * 检验用户名.
     * 取值范围为a-z,A-Z,0-9,"_",汉字，不能以"_"结尾
     * 用户名有最小长度和最大长度限制，比如用户名必须是4-20位
     */
    public boolean checkUsername(String username, int min, int max) {
        String regex = "[\\w\u4e00-\u9fa5]{" + min + "," + max + "}(?<!_)";
        return startCheck(regex, username);
    }

    /**
     * 检验用户名.
     * 取值范围为a-z,A-Z,0-9,"_",汉字，不能以"_"结尾
     * 有最小位数限制的用户名，比如：用户名最少为4位字符
     */
    public static boolean checkUsername(String username, int min) {
        //[\\w\u4e00-\u9fa5]{2,}?
        String regex = "[\\w\u4e00-\u9fa5]{" + min + ",}(?<!_)";

        return startCheck(regex, username);
    }

    /**
     * 检验用户名.
     * 取值范围为a-z,A-Z,0-9,"_",汉字
     * 最少一位字符，最大字符位数无限制，不能以"_"结尾
     */
    public static boolean checkUsername(String username) {
        String regex = "[\\w\u4e00-\u9fa5]+(?<!_)";
        return startCheck(regex, username);
    }

    /**
     * 查看IP地址是否合法.
     */
    public static boolean checkIP(String ipAddress) {
        String regex = "(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\." +
                "(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\." +
                "(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\." +
                "(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])";

        return startCheck(regex, ipAddress);
    }

    /**
     * 验证国内电话号码.
     * 格式：010-67676767，区号长度3-4位，必须以"0"开头，号码是7-8位
     */
    public static boolean checkPhoneNr(String phoneNr) {
        String regex = "^[0]\\d{2,3}\\-\\d{7,8}";

        return startCheck(regex, phoneNr);
    }

    /**
     * 验证国内电话号码.
     * 格式：6767676, 号码位数必须是7-8位,头一位不能是"0"
     */
    public static boolean checkPhoneNrWithoutCode(String phoneNr) {
        String reg = "^[1-9]\\d{6,7}";

        return startCheck(reg, phoneNr);
    }

    /**
     * 验证国内电话号码.
     * 格式：0106767676，共11位或者12位，必须是0开头
     */
    public static boolean checkPhoneNrWithoutLine(String phoneNr) {
        String reg = "^[0]\\d{10,11}";

        return startCheck(reg, phoneNr);
    }

    /**
     * 验证国内身份证号码：15或18位，由数字组成，不能以0开头.
     */
    public static boolean checkIdCard(String idNr) {
        String reg = "^[1-9](\\d{14}|\\d{17})";

        return startCheck(reg, idNr);
    }

    /**
     * 网址验证<br>.
     * 符合类型：<br>
     * http://www.test.com<br>
     * http://163.com
     */
    public static boolean checkWebSite(String url) {
        //http://www.163.com
        String reg = "^(http)\\://(\\w+\\.\\w+\\.\\w+|\\w+\\.\\w+)";

        return startCheck(reg, url);
    }


    public static boolean checkPassword(String pwd) {
        return startCheck(PASSWORD_REGEXP, pwd);
    }

    public static void main(String[] args) {
        System.out.println("ff" + checkCellPhone("18513335636"));
    }
}
