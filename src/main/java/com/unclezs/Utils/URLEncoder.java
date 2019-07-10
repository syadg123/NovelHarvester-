package com.unclezs.Utils;

import org.jsoup.Jsoup;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 *@author unclezs.com
 *@date 2019.07.08 19:46
 */
public class URLEncoder {
    private URLEncoder() {
    }

    //将url里的中文转Unicode
    public static String encode(String url, String charset) throws UnsupportedEncodingException {

        StringBuffer toUrl = new StringBuffer();
        for (char c : url.toCharArray()) {
            if (!isChinese(c)) {
                toUrl.append(c);
            } else {
                toUrl.append(java.net.URLEncoder.encode(c + "", charset));
            }
        }
        return toUrl.toString();
    }

    //判断是否为中文
    public static boolean isChinese(char c) {
        String reg = "[\\u4E00-\\u9FFF]";//中文
        return Pattern.matches(reg, c + "");
    }
}
