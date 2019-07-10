package com.unclezs.Model;

/*
 *@author unclezs.com
 *@date 2019.06.26 21:33
 */
public class Chapter {
    private String chapterName;//章节名字
    private String chapterUrl;//章节url
    private Integer aid;//小说id

    public Chapter(String chapterName, String chapterUrl, Integer aid) {
        this.chapterName = chapterName;
        this.chapterUrl = chapterUrl;
        this.aid = aid;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getChapterUrl() {
        return chapterUrl;
    }

    public void setChapterUrl(String chapterUrl) {
        this.chapterUrl = chapterUrl;
    }

    public Integer getAid() {
        return aid;
    }

    public void setAid(Integer aid) {
        this.aid = aid;
    }

    @Override
    public String toString() {
        return "Chapter{" +
                "chapterName='" + chapterName + '\'' +
                ", chapterUrl='" + chapterUrl + '\'' +
                ", aid=" + aid +
                '}';
    }
}
