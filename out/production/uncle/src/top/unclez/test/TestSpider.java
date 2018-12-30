package top.unclez.test;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import top.unclez.utils.Utils;
import top.unclez.spider.MainSpider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class TestSpider {
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
    @Test
    void test5(){
        String s="dasdasdasda";
        System.out.println(s.indexOf(null));
    }
}
