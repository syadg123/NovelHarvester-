package com.unclezs.crawl.special;

import cn.hutool.core.lang.Dict;
import com.unclezs.model.Chapter;
import com.unclezs.utils.RequestUtil;
import com.unclezs.utils.TextUtil;
import com.unclezs.utils.XpathUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * https://www.po18.tw/
 *
 * @author uncle
 * @date 2020/7/17 19:49
 */
public class Po18 {
    public static String content(String url, String cookie) throws IOException {
        String id = url.substring(url.lastIndexOf("/") + 1);
        String chapterId = TextUtil.remove(url, "https://www.po18.tw/books/", "/articles/" + id);
        String s = get(String.format("https://www.po18.tw/books/%s/articlescontent/%s", chapterId, id), cookie);
        Elements ps = Jsoup.parse(s, url).select("p");
        StringBuilder sb = new StringBuilder();
        ps.forEach(p -> {
            sb.append(p.text()).append("\r\n");
        });
        return sb.toString();
    }

    public static Dict chapters(String url, String cookie) throws IOException {
        String id = TextUtil.remove(url, "https://www.po18.tw/books/", "/articles");
        String s = get(String.format("https://www.po18.tw/books/%s/allarticles", id), cookie);
        Document parse = Jsoup.parse(s, url);
        Elements as = parse.select("a");
        String contentHtml = get(url, cookie);
        String title = Jsoup.parse(contentHtml).select(".book_name").text();
        return Dict.create().set("chapters", as.stream().map(a -> new Chapter(a.text(), a.absUrl("href"))).collect(Collectors.toList())).set("title", title);
    }

    public static void main(String[] args) throws IOException {
        String url = "https://www.po18.tw/books/720558/articles";
        String cookie = "_paabbcc=vihhcbtbkt254sdaa8v8mn46f5; _po18rf-tk001=1c745d088b4fcc220c645a51d0d0c4b33fb5e1ad45383aba000bd3e294b56207a%3A2%3A%7Bi%3A0%3Bs%3A13%3A%22_po18rf-tk001%22%3Bi%3A1%3Bs%3A32%3A%22bZJfnrCA2AjJiIZhb7Lrfk84QlGM38f2%22%3B%7D; _ga=GA1.2.1817444373.1594970578; _gid=GA1.2.201932416.1594970578; po18Limit=6d4c449fc3268973f92df90aad9af59af0885074fe02e29be9cf3d42d091ec89a%3A2%3A%7Bi%3A0%3Bs%3A9%3A%22po18Limit%22%3Bi%3A1%3Bs%3A1%3A%221%22%3B%7D; url=https%3A%2F%2Fwww.po18.tw; authtoken1=eGlhb2h1YTEyMTM4; authtoken2=NzEyMzNhNzJjZWRkMDFjOGFlM2YwZjRjZjljNDA4NWU%3D; authtoken3=2392904587; authtoken4=2648278218; authtoken5=1594970827; authtoken6=1; bgcolor=bg-default; word=select-m; _gat_gtag_UA_11633339_26=1";
        System.out.println(chapters(url, cookie));
    }

    public static String get(String url, String cookie) throws IOException {
        Map<String, String> cookieMap = new HashMap<>(16);
        Arrays.stream(cookie.split(";")).forEach(item -> {
            String[] ss = item.split("=");
            cookieMap.put(ss[0], ss[1]);
        });
        return Jsoup.connect(url).referrer(url).userAgent(RequestUtil.USER_AGENT).cookies(cookieMap).execute().charset("UTF-8").body();
    }
}
