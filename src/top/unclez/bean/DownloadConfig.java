package top.unclez.downloader;

import top.unclez.Utils.FileUtil;

import java.io.Serializable;

public class DownloadConfig implements Serializable {
    private String charset;
    private String path;
    private int page;
    private boolean T2S;
    private String rule;
    private String novelname;
    private boolean NCR2CN;
    private int delay;
    private boolean filter;
    private boolean sort;

    public DownloadConfig() {
        this.charset = "utf-8";
        this.path = "F:/小说下载目录/";
        this.page = 50;
        this.T2S = false;
        this.rule = "1";
        this.novelname = "默认";
        this.NCR2CN = false;
        this.delay = 0;
        this.filter=true;
        this.sort=false;
    }
    public boolean isFilter() {
        return filter;
    }

    public void setFilter(boolean filter) {
        this.filter = filter;
    }

    public boolean isSort() {
        return sort;
    }

    public void setSort(boolean sort) {
        this.sort = sort;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public boolean isNCR2CN() {
        return NCR2CN;
    }

    public void setNCR2CN(boolean NCR2CN) {
        this.NCR2CN = NCR2CN;
    }

    public String getCharset() {
        return charset;
    }

    public String getNovelname() {
        return novelname;
    }

    public void setNovelname(String novelname) {
        this.novelname = novelname;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public boolean isT2S() {
        return T2S;
    }

    public void setT2S(boolean t2S) {
        T2S = t2S;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    @Override
    public String toString() {
        return "DownloadConfig{" +
                "charset='" + charset + '\'' +
                ", path='" + path + '\'' +
                ", page='" + page + '\'' +
                ", T2S=" + T2S +
                ", rule='" + rule + '\'' +
                '}';
    }
}
