package com.unclezs.Test;

import com.unclezs.Crawl.AudioNovelSpider;
import com.unclezs.Mapper.AudioBookMapper;
import com.unclezs.Model.AudioBook;
import com.unclezs.Model.AudioChapter;
import com.unclezs.Utils.FileUtil;
import com.unclezs.Utils.HtmlUtil;
import com.unclezs.Utils.HttpUtil;
import com.unclezs.Utils.MybatisUtils;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.apache.http.client.utils.URLEncodedUtils;
import org.junit.jupiter.api.Test;

import javax.swing.text.html.ImageView;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;

/*
 *@author unclezs.com
 *@date 2019.06.20 21:27
 */
public class AppTest {

    @Test
    void test() throws IOException {
    }
    //动态网页抓取
    @Test
    void testDymaic() throws IOException {
        System.out.println(Class.class.getResource("/").getPath().substring(1));
    }

    //查库
    @Test
    void testQuery(){
        long l = System.currentTimeMillis();
        System.out.println(System.currentTimeMillis()-l);
    }
    //56听书
    @Test
    void testLocalFile() throws Exception {
        System.out.println(HttpUtil.getResponseCode("http://q1.audio699.com/asdasdasd/234/7/1562659260/265aa25f4f0a4ff4d9af6d45c9b338ed/5783d275e5bad6f74ac30f951c202602.m4a"));
    }
    //有声听书吧
    @Test
    void testUrl() throws UnsupportedEncodingException {
        String curl="http://mp3-d.ting89.com:9090/玄幻小说/完美世界_晗玉/024.mp3";
        String path="C:\\Users\\uncle\\Desktop/完美世界(昊儒版)/[第008集].mp3";
        String b = com.unclezs.Utils.URLEncoder.encode(curl,"utf-8");
        FileUtil.uploadFile(path,b,1024*1024);
    }


    @Test
    void test22(){
        String cookies="login_from=qq; _xmLog=xm_1561903213746_jxj0ugtu0oc8jm; x_xmly_traffic=utm_source%253A%2526utm_medium%253A%2526utm_campaign%253A%2526utm_content%253A%2526utm_term%253A%2526utm_from%253A; device_id=xm_1562317565500_jxpvjgosnjy8mx";
        String s = HtmlUtil.getHtml("https://www.ximalaya.com/revision/play/album?albumId=12168585&pageNum=1&sort=0&pageSize=30","utf-8",cookies,null);
        System.out.println(s);
    }
}
