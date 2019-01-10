package top.unclez.test;

import org.apache.bcel.generic.LREM;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import top.unclez.bean.GlobalValue;
import top.unclez.utils.Utils;
import top.unclez.spider.MainSpider;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Map;

public class TestSpider {
    public static void main(String[] args) throws Exception{
        //读取资源文件
        /*InputStream inputStream = Properties.class.getClassLoader().getResourceAsStream("");*/




        //请输入视频地址
        String url = "http://112.253.22.162/14/w/b/i/h/wbihzlouyfswbrkigfwphmuazytjqg/he.yinyuetai.com/7A930167EDD13CD98C4ED5A603BCBA0C.mp4?sc=812d7f55584d9835&br=3137&vid=3335581&aid=26226&area=ML&vst=0";

        //String serveradress = "http://cn-sdjn3-cu-v-02.acgvideo.com/upgcxcode/44/01/17040144/17040144-1-16.mp4?expires=1546242600&platform=html5&ssig=4O4OlbjvsQKsuFPL0fSy1Q&oi=2032031146&nfa=ZGlYLwTu0dW3o1gJGPmYTQ==&dynamic=1&hfa=2115692787&hfb=M2Y2ZWYwZjM2YmRiYmY5MDljYTBiOWE2ZmEwYjJmYTM=&trid=e11c532076a841688a270d6d6faf265e&nfb=maPYqpoel5MI3qOUX6YpRA==&nfc=1";
        HttpURLConnection con;
        FileOutputStream fs = null;
        InputStream is;
        BufferedInputStream bs = null;
        File file = new File("test.mp4");

        //创建文件
        file.createNewFile();

        try {
            con = (HttpURLConnection) new URL(url).openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36");

            //输入流
            is = con.getInputStream();
            bs = new BufferedInputStream(is);
            //outStream
            fs = new FileOutputStream(file);
            byte [] bytes = new byte[1024];

            int line ;
            //write
            while((line = bs.read(bytes))!= -1){
                fs.write(bytes, 0, line);
                fs.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            //close
            if(fs!= null){
                try {
                    fs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(bs!=null){
                try {
                    bs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * 章节列表获取
     */
    @Test
    void test1(){
        MainSpider.getChapterList("https://880vs.com/?s=%E8%BF%B7%E5%A5%B8",false,false);
    }
    /**
     * 简体转繁体，&#dddd格式转码
     */
    @Test
    void test2(){
        String content=MainSpider.getContent("http://www.17k.com/chapter/2749599/36492607.html","utf-8","1");
        System.out.println(content);
//        String s= utils.NCR2Chinese(content);
//        System.out.println(s);
//        String simple= ChineseUtils.toSimplified(s);
//        System.out.println(simple);
    }
    /**
     * 内容写入文件
     */
    @Test
    void test3(){
        String content=MainSpider.getContent("https://read.qidian.com/chapter/ndR0xQFaYQYVDwQbBL_r1g2/uEi6XptU1fz6ItTi_ILQ7A2","gb2312","2");
        File f=new File("测试.txt");
        try {
            PrintWriter out=new PrintWriter(f);
            out.println(Utils.traditional2Simple(Utils.NCR2Chinese(content)));
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    //模拟浏览器
    @Test
    void test4(){
//        System.setProperty("webdriver.chrome.driver","F:/chromedriver.exe");
        WebDriver driver=new HtmlUnitDriver();
        driver.get("https://www.52pojie.cn/forum-2-1.html");
        System.out.println(driver.getPageSource());
//        driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
        //实例化虚拟浏览器对象
//                 WebDriver driver = new HtmlUnitDriver();
//        //打开百度首页
//                 String url = "http://www.baidu.com";
//                 driver.get(url);
//                 //定位搜索框元素
//                 WebElement ele = driver.findElement(By.id("kw"));
//                 //输入需查询内容
//                ele.sendKeys("Cheese");
//               ele.submit();
//
//               //获取页面标题
//                 System.out.println("Page title is :" + driver.getTitle());
//                 //获取页面url
//                 System.out.println("Page url is :" + driver.getCurrentUrl());
//                 //关闭driver
//                 driver.close();
    }
    //批量下载
    @Test
    void test5(){
        try(CloseableHttpClient httpClient= HttpClientBuilder.create().build()){
            HttpGet get=new HttpGet("https://one.991video.com/common/wm/2018_12/29/wm_DbNXQwMU_wm/wm_DbNXQwMU.ts");
            InputStream content = httpClient.execute(get).getEntity().getContent();
            FileOutputStream fos=new FileOutputStream("tes2222t.ts");
            byte[] b=new byte[1024];
            String res="";
            int len=0;
            while ((len=content.read(b))>0){
                fos.write(b,0,len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
