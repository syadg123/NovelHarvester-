package com.unclezs.test;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.extra.ftp.Ftp;
import cn.hutool.extra.ftp.FtpMode;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.unclezs.crawl.LocalNovelLoader;
import com.unclezs.crawl.TextNovelSpider;
import com.unclezs.enmu.SearchKeyType;
import com.unclezs.mapper.SearchTextRuleMapper;
import com.unclezs.model.NovelInfo;
import com.unclezs.model.rule.SearchTextRule;
import com.unclezs.utils.MybatisUtil;
import com.unclezs.utils.RequestUtil;
import com.unclezs.utils.XpathUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import us.codecraft.xsoup.Xsoup;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author unclezs.com
 * @date 2019.06.20 21:27
 */
public class AppTest {

    public static void main(String[] args) throws IOException {
        boolean a = true;
        if (a != true) {

        }
        String s1 = IoUtil.read(AppTest.class.getResourceAsStream("/conf/search_text_rule.json")).toString("utf-8");
        System.out.println(s1);
        String keyword = "完美世界";
        String[] ignores = {"searchKey", "searchLink", "resultList", "name", "site"};
        List<String> ignored = Arrays.stream(ignores).collect(Collectors.toList());
        List<SearchTextRule> rules = null;
        for (SearchTextRule rule : rules) {
            HttpRequest s = HttpUtil.createGet(rule.getSearchLink())
                    .form(rule.getSearchKey(), keyword)
                    .header("Referer", rule.getSearchLink())
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.162 Safari/537.36");
            HttpResponse execute = s.execute();
            String body = execute.body();
            Document document = Jsoup.parse(body, rule.getSearchLink());
            Elements elements = Xsoup.compile(rule.getResultList()).evaluate(document).getElements();
            for (Element element : elements) {
                NovelInfo map = XpathUtil.xpath(element, rule, ignored, NovelInfo.class);
                System.out.println(map);
            }
        }
    }

    @Test
    void mybatisPlusTest() throws Exception {
        LocalNovelLoader localNovelLoader = new LocalNovelLoader();
        boolean b = localNovelLoader.load("F:\\小说\\完美世界.txt");
        if (b) {
            String content = localNovelLoader.content(0);
            System.out.println(content);
            localNovelLoader.store();
        }
    }

    @Test
    void serializableTest() throws NoSuchFieldException, IllegalAccessException, IOException {
        String text = FileUtil.readString("D:\\java\\NovelHarvester\\2.txt", CharsetUtil.UTF_8);
        String s = new String(text.getBytes(), StandardCharsets.UTF_8);
//        text = Convert.convertCharset(text, "GBK",  CharsetUtil.UTF_8);
        System.out.println(s);
    }


    @Test
    public void testAudio() throws IOException {
        SearchTextRule execute = MybatisUtil.execute(SearchTextRuleMapper.class, mapper -> mapper.selectById("x23qb.com"));
        TextNovelSpider spider = new TextNovelSpider();
        List<NovelInfo> search = spider.search("完美", execute, SearchKeyType.TITLE);
        System.out.println(search);
//        System.out.println(spider.getAudioLink("http://www.lrts.me/ajax/playlist/2/35746/6"));
    }

    @Test
    public void testStr() throws IOException {
        String baiduApi = "https://www.baidu.com/s?wd=%s&pn=%s";
        Document html = RequestUtil.doc(String.format(baiduApi, "完美世界 小说章节目录", 0));
        Elements items = html.select(".t a");
        for (Element item : items) {
//            System.out.println(Re);
            System.out.println(item.text()+"     "+ item.absUrl("href"));
        }
    }
}
