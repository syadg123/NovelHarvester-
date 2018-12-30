package top.unclez.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 阅读器配置
 */
public class ReaderConfig implements Serializable {
    private String userColor; //背景色
    private String userfontColor; //字体颜色
    private int index;  //阅读位置
    private String charset;  //编码
    private int fontsize; //字体大小
    private List<String> readlist; //文章章节信息
    private Map<String,String> readdata;
    private String cookie;
    private String cheader;//章节开头
    private String ctail;//章节结尾
    private String dheader;//正文结尾
    private String dtail;//正文开头
    private String rule;

    public ReaderConfig() {
        userColor="#CEEBCE";
        userfontColor="#333333";
        index=0;
        charset="gbk";
        fontsize=16;
        rule="1";
        readdata=new LinkedHashMap<>();
        readlist=new ArrayList<>();
    }
    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }
    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getCheader() {
        return cheader;
    }

    public void setCheader(String cheader) {
        this.cheader = cheader;
    }

    public String getCtail() {
        return ctail;
    }

    public void setCtail(String ctail) {
        this.ctail = ctail;
    }

    public String getDheader() {
        return dheader;
    }

    public void setDheader(String dheader) {
        this.dheader = dheader;
    }

    public String getDtail() {
        return dtail;
    }

    public void setDtail(String dtail) {
        this.dtail = dtail;
    }

    public String getUserColor() {
        return userColor;
    }

    public void setUserColor(String userColor) {
        this.userColor = userColor;
    }

    public String getUserfontColor() {
        return userfontColor;
    }

    public void setUserfontColor(String userfontColor) {
        this.userfontColor = userfontColor;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public int getFontsize() {
        return fontsize;
    }

    public void setFontsize(int fontsize) {
        this.fontsize = fontsize;
    }

    public List<String> getReadlist() {
        return readlist;
    }

    public void setReadlist(List<String> readlist) {
        this.readlist = readlist;
    }

    public Map<String, String> getReaddata() {
        return readdata;
    }

    public void setReaddata(Map<String, String> readdata) {
        this.readdata = readdata;
    }
}
