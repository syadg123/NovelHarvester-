package top.unclez.bean;

import javafx.stage.Stage;
import top.unclez.downloader.DownloadConfig;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public class GlobalValue {
    public static List<String> downloaded = new ArrayList<>();//已经下载的
    public static int total = 0;//总的任务数量
    public static boolean isDownloaded = false;//是否下载完成
    public static Map<String, String> data = new LinkedHashMap<>();//小说章节url为key，标题为value
    public static List<String> taskurl = new ArrayList<>();
    public static List<String> failed = new ArrayList<>();//下载失败的章节
    public static DownloadConfig config;
    public static Stage stage;//fx主页面
    public static Stage readstage;//阅读页面
    public static Stage bookstage;//书架页面
    public static ExecutorService service;//下载线程管理，停止下载等
    public static String path;

    public static void init() {
        downloaded = new ArrayList<>();
        total = 0;
        isDownloaded = false;
        failed = new ArrayList<>();
    }
}
