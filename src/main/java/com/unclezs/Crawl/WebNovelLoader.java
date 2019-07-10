package com.unclezs.Crawl;

import com.unclezs.Mapper.ChapterMapper;
import com.unclezs.Mapper.NovelMapper;
import com.unclezs.Model.Chapter;
import com.unclezs.UI.Utils.DataManager;
import com.unclezs.Utils.MybatisUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 *网络小说加载器
 *@author unclezs.com
 *@date 2019.06.26 20:55
 */
public class WebNovelLoader {
    private List<String> chapters;//章节列表
    private List<String> contentUrl;//正文URl
    private String content[];//正文内容
    private String charset;//小说编码
    private Integer aid;//书籍id
    private NovelSpider spider;
    public WebNovelLoader(Integer aid, String charset,NovelSpider spider) {
        this.aid = aid;
        this.charset = charset;
        this.spider=spider;
        initLoad();
    }

    //加载小说信息
    public void initLoad() {
        chapters = new ArrayList<>();
        contentUrl = new ArrayList<>();
        ChapterMapper mapper = MybatisUtils.getMapper(ChapterMapper.class);
        List<Chapter> cs = mapper.findAllChapter(aid);
        for (int i = 0; i < cs.size(); i++) {
            chapters.add(cs.get(i).getChapterName());
            contentUrl.add(cs.get(i).getChapterUrl());
        }
        content = new String[contentUrl.size()];//初始化缓存正文内容数组
    }

    //获取正文内容
    public String getContent(int index) {
        loadOnPage(index);
        new Thread(() -> cacheContent(index)).start();//开始异步缓存
        return content[index];
    }

    //获取章节名字信息
    public List<String> getChapters() {
        return chapters;
    }

    //获取章节url地址
    public List<String> getContentUrl() {
        return contentUrl;
    }

    //缓存前后5章节
    public void cacheContent(int index) {
        for (int i = index; i < index + 5 && i < contentUrl.size(); i++) {//缓存后5章节
            loadOnPage(i);
        }
        for (int i = index; i > index - 5 && i >= 0; i--) {
            loadOnPage(i);
        }
    }

    //爬去一章节，并且格式化处理
    public void loadOnPage(int index) {
        if (content[index] == null) {//已经缓存过不需要加载
            String content = spider.getContent(contentUrl.get(index), charset);
            //加载章节
            StringBuffer buffer = new StringBuffer();
            buffer.append("\r\n\r\n");//空两行显示题目
            buffer.append(content + "\r\n");
            buffer.append("本章完\r\n");//末尾显示本章完
            this.content[index] = buffer.toString();//进入缓存
        }
    }
}
