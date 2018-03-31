package com.simbest.boot.util;

import com.google.common.collect.Maps;
import com.simbest.boot.constants.ApplicationConstants;

import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * @author lishuyi
 */
public class CodeGenerator {
    public static final String ALLCHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static String recordDay = DateUtil.getCurrentStr();
    private static int seqDate = 0;
    private static int seqTime = 0;
    private static int maxCode = 99999999;
    private static Map<String, Integer> prefixs = Maps.newHashMap();
    private static char[] chars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    public static void main(String[] args) {
        System.out.println(nextSystemUUID());
        System.out.println(addLeftZeroForNum(4, 9));
        System.out.println(addRightZeroForNum(4, 9));
        Date date = new Date();                                                                    // 创建日期对象
        System.out.printf("全部日期和时间信息：%tc%n", date);
        for (int i = 0; i < 10; i++) {
            System.out.println(nextDateCode("B"));
        }
        System.out.println(nextDateCode());
        System.out.println(nextDateCode());
        System.out.println(nextDateTimeCode());
        System.out.println(nextDateTimeCode());
        System.out.println(nextUnLimitCode());
        System.out.println(nextUnLimitCode());
        for (int i = 0; i < 10000; i++) {
            System.out.println(nextUnLimitCode("OA"));
        }
        String s = "B-20150604-00000008";
        System.out.println(s.substring(s.lastIndexOf("-") + 1));
    }


    /**
     * 获取系统UUID，格式bd1bf93cd7e5401883693e3e7019884f
     *
     * @return
     */
    public static String nextSystemUUID() {
        return UUID.randomUUID().toString()
                .replace(ApplicationConstants.LINE, ApplicationConstants.EMPTY);
    }

    /**
     * @param num 返回随机数的位数, 如3则可能返回012
     * @return
     */
    public static String nextRandomInt(int num) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < num; i++) {
            sb.append((int) (Math.random() * (10)));
        }
        return sb.toString();
    }

    /**
     * 返回无限制编码，支持每毫秒3位随机数并发
     * 如：20160715003629069001，含义2016-07-15 00:36:29.069 001
     * <p>
     * 随机生成 a 到 b (不包含b)的整数:
     * (int)(Math.random()*(b-a))+a;
     * 随机生成 a 到 b (包含b)的整数:
     * (int)(Math.random()*(b-a+1))+a;
     *
     * @return
     */
    public static synchronized String nextUnLimitCode() {
        return String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS%1$tL%2$03d", DateUtil.getCurrent(), (int) (Math.random() * (899)) + 100);
    }

    public static synchronized String nextUnLimitCode(String prefix) {
        return prefix + String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS%1$tL%2$03d", DateUtil.getCurrent(), (int) (Math.random() * (899)) + 100);
    }

    /**
     * 获取全局日期序列， 每日上限99999999个，格式2016071500000002
     *
     * @return
     */
    public static synchronized String nextDateCode() {
        String today = DateUtil.getCurrentStr();
        if (!today.equals(recordDay)) {
            seqDate = 0; //隔日起始编号清零
            recordDay = today;
        }
        if (seqDate > maxCode)
            throw new RuntimeException("Not enough next date code");
        return String.format("%tY%<tm%<td%08d", DateUtil.parseDate(recordDay), ++seqDate);
    }

    /**
     * 获取全局时间序列，每日上限99999999个， 格式2016071500000000000000002
     *
     * @return
     */
    public static synchronized String nextDateTimeCode() {
        String today = DateUtil.getCurrentStr();
        if (!today.equals(recordDay)) {
            seqTime = 0; //隔日起始编号清零
            recordDay = today;
        }
        if (seqTime > maxCode)
            throw new RuntimeException("Not enough next timestamp code");
        return String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS%1$tL%2$08d", DateUtil.parseDate(recordDay), ++seqTime);
    }

    /**
     * 根据前缀获取日期序列， 每日上限999个，格式B-20150604-0008
     *
     * @param prefix
     * @return
     */
    public static synchronized String nextDateCode(String prefix) {
        if (prefixs.get(prefix) == null)
            prefixs.put(prefix, 0); //是否存在编码
        String today = DateUtil.getCurrentStr();
        if (!today.equals(recordDay)) {
            prefixs.put(prefix, 0); //隔日起始编号清零
            recordDay = today;
        }
        int next = prefixs.get(prefix) + 1;
        if (next > maxCode)
            throw new RuntimeException("Not enough next date code");
        prefixs.put(prefix, next);
        return prefix + ApplicationConstants.LINE + DateUtil.getDateStr(DateUtil.datePattern2) + ApplicationConstants.LINE + addLeftZeroForNum(4, next);
    }

    /**
     * 字符串不足位数补长
     *
     * @param num   长度
     * @param value 值
     * @return
     */
    public static String addLeftZeroForNum(int num, int value) {
        return String.format("%" + num + "d", value).replace(" ", "0");
    }

    /**
     * 字符串不足位数补长
     *
     * @param num   长度
     * @param value 值
     * @return
     */
    public static String addRightZeroForNum(int num, int value) {
        String str = String.valueOf(value);
        int strLen = str.length();
        if (strLen < num) {
            while (strLen < num) {
                StringBuffer sb = new StringBuffer();
                //sb.append("0").append(str);// 左补0
                sb.append(str).append("0");//右补0
                str = sb.toString();
                strLen = str.length();
            }
        }
        return str;
    }


    /**
     * 返回一个定长的随机字符串(只包含大小写字母、数字)
     *
     * @param length 随机字符串长度
     * @return 随机字符串
     */
    public static String generateString(int length) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(ALLCHAR.charAt(random.nextInt(ALLCHAR.length())));
        }
        return sb.toString().toUpperCase();
    }


    /**
     * The byte[] returned by MessageDigest does not have a nice textual
     * representation, so some form of encoding is usually performed.
     * <p>
     * This implementation follows the example of David Flanagan's book
     * "Java In A Nutshell", and converts a byte array into a String of hex
     * characters.
     * <p>
     * Another popular alternative is to use a "Base64" encoding.
     */
    private static String hexEncode(byte[] aInput) {
        StringBuilder result = new StringBuilder();
        for (int idx = 0; idx < aInput.length; ++idx) {
            byte b = aInput[idx];
            result.append(chars[(b & 0xf0) >> 4]);
            result.append(chars[b & 0x0f]);
        }
        return result.toString();
    }

}
