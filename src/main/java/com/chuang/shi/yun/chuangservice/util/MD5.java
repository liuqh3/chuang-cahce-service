package com.chuang.shi.yun.chuangservice.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    /**
     * 全局数组
     * 大写
     */
    //   private final static String[] strDigits={"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"};
    /**
     * 全局数组
     * 小写
     */
    private final static String[] strDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};

    public MD5() {
    }

    /**
     * 返回形式为数组跟字符串
     */
    private static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }

    /**
     * 返回形式只为数字
     */
    private static String byteToNum(byte bByte) {
        int iRet = bByte;
        if (iRet < 0) {
            iRet += 256;

        }
        return String.valueOf(iRet);
    }

    /**
     * 返回形式为混合的
     */
    public static String GetMD5Code(String obj) {
        String resultString = null;
        try {
            resultString = new String(obj);
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.digest();//该函数返回值为存放该哈希结果的byte数组
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return resultString;
    }

    private static String StringMD5(String pw) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = pw.getBytes();
            md5.update(bytes);
            byte[] digest = md5.digest();
            return byteArrayToHex(digest);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    private static String byteArrayToHex(byte[] byteArray) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        char[] resultCharArray=new char[byteArray.length * 2];
        int index=0;
        for (byte b : byteArray) {
            resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
            resultCharArray[index++]=hexDigits[b & 0xf];
        }
        return new String(resultCharArray);
    }

    public static void main(String[] args) {
        String cust_id = "678000";
        String item_id = "934d4d8f700f132d986e37ff0dcc1140";
        String userName = "chuangshiyun";
        String password = "c2s0y1Q9Video";
        System.out.println("MD5结果=" + MD5.GetMD5Code(item_id + cust_id + userName + password));
    }
}
