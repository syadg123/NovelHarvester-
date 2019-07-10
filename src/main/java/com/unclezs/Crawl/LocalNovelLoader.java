package com.unclezs.Crawl;


import com.unclezs.UI.Utils.DataManager;
import com.unclezs.Utils.FileUtil;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 *本地小说解析器
 *@author unclezs.com
 *@date 2019.06.22 16:30
 */
public class LocalNovelLoader {
    private String regex = "(.*?第[\\s\\S]{1,5}[章卷节].+?)\r\n";
    private String path;
    private String content[];
    private String name;
    private boolean isExist = false;//是否存在
    private List<String> chapters = new ArrayList<>();

    public LocalNovelLoader(String path) {
        this.path = path;
        this.name = path.substring(path.lastIndexOf('\\') + 1, path.lastIndexOf('.'));
        initLoad();
    }

    public void initLoad() {
        try {
            getChapters();
            isExist = true;
        } catch (Exception e) {
            isExist = false;
        }
    }

    //只加载一次
    public List<String> getChapters() throws Exception {
        if (chapters.size() != 0) {
            return chapters;
        }
        String content = loadFile();
        List<String> chapterName = new ArrayList<>();//章节名字
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(content);
        while (m.find()) {
            chapterName.add(m.group(1));
        }
        this.chapters = chapterName;
        return chapterName;
    }

    //只加载一次
    public String getContent(int index) throws Exception {
        if (content == null) {
            loadFile();
        }
        return content[index + 1];
    }

    //加载读取文件
    String loadFile() throws Exception {
        String encode = FileUtil.codeFile(path);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), encode));
        String tmp;
        StringBuffer sb = new StringBuffer();
        while ((tmp = reader.readLine()) != null) {
            sb.append(tmp);
            sb.append("\r\n");
        }
        reader.close();
        content = sb.toString().split(regex);
        return sb.toString();
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    //根据下标加载章节内容
    public Map<String, String> getCPageByIndex(int index) throws Exception {
        Map<String, String> map = new HashMap<>();
        //加载章节
        StringBuffer buffer = new StringBuffer();
        buffer.append("\r\n\r\n");//空两行显示题目
        buffer.append(getContent(index));//正文
        map.put("content", buffer.toString());
        //加载标题
        map.put("title", chapters.get(index));
        return map;
    }

    public void setPath(String path) {
        this.path = path;
    }

    //释放文本
    public void free() {
        this.chapters = null;
        this.content = null;
    }

    public boolean isExist() {
        return isExist;
    }
}
