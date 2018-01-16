package org.st.aliyun.ossuploader.util;

import java.security.MessageDigest;

/**
 * Created by YYH on 14-6-27.
 */
public class MD5Util {

    public final static char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};

    public final static String MD5(String s) {
        byte[] btInput = s.getBytes();
        return MD5(btInput);
    }

    public final static String MD5(byte[] data) {
        try {
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(data);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println(MD5Util.MD5(""));
        System.out.println(MD5Util.MD5("666666"));

        byte[] data = new byte[20*1024];
        for (int i = 0; i < 10000; ++i) {
            MD5Util.MD5(data);
        }
    }
}


