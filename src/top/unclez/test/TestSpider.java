package top.unclez.test;

import com.luhuiguo.chinese.ChineseUtils;
import org.junit.jupiter.api.Test;
import top.unclez.Utils.Utils;
import top.unclez.spider.MainSpider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigInteger;

public class TestSpider {
    /**
     * 章节列表获取
     */
    @Test
    void test1(){
        MainSpider.getChapterList("https://www.huaxiangju.com/58223/#yuedu",false,false);
    }
    /**
     * 简体转繁体，&#dddd格式转码
     */
    @Test
    void test2(){
        String content=MainSpider.getContent("http://www.read126.cn/194c6894-51d5-4df3-a4bc-fa1282139f82!6363679b-ea1d-4156-8524-186a2215f3d2.html","gb2312","2");
        System.out.println(content);
//        String s= Utils.NCR2Chinese(content);
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
        System.out.println("色情小说 | 黄色小说 @ 绝色小说".replaceAll("[^\\u4E00-\\u9FFF]",""));
    }
}
