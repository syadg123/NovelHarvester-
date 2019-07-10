package com.unclezs.UI.Utils;

import com.unclezs.Crawl.LocalNovelLoader;
import com.unclezs.Crawl.WebNovelLoader;
import com.unclezs.Model.Book;
import com.unclezs.Model.ReaderConfig;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

/*
 *@author unclezs.com
 *@date 2019.06.21 11:32
 */
public class DataManager {
    public static Stage mainStage;//主舞台
    public static VBox root;//主窗口
    public static Pane content;//主窗口的内容面板
    public static Stage readerStage;//阅读器窗口
    public static Book book;//当前打开书
    public static LocalNovelLoader lns;//本地书加载器
    public static WebNovelLoader wns;//网络书加载器
    public static boolean needReloadBookSelf;//是否需要重新加载书架
    public static ReaderConfig readerConfig;//阅读器配置
}
