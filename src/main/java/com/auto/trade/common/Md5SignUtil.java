package com.auto.trade.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by gof on 18/6/27.
 */
public class Md5SignUtil {
    /***
     * MD5加码 生成32位md5??
     */
    public static String string2MD5(String plainText){
        String re_md5 = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            re_md5 = buf.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return re_md5;
    }
}
