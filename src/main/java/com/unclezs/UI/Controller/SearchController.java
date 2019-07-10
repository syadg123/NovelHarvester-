package com.unclezs.UI.Controller;

import com.alibaba.fastjson.JSON;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import com.unclezs.Crawl.NovelSpider;
import com.unclezs.Model.NovelInfo;
import com.unclezs.UI.Node.ProgressFrom;
import com.unclezs.UI.Node.SearchNode;
import com.unclezs.UI.Utils.ContentUtil;
import com.unclezs.UI.Utils.DataManager;
import com.unclezs.Utils.HttpUtil;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/*
 *搜索页面
 *@author unclezs.com
 *@date 2019.06.26 16:44
 */
public class SearchController implements Initializable {
    @FXML
    JFXTextField text;
    @FXML
    Pane searchPane, searchRoot;
    @FXML
    Label search;
    @FXML
    JFXListView list;
    //成员
    String[] sites = {"bezw", "bxwx", "hxj", "ldks", "snwx", "xsyd", "yzsk"};//网站列表
    private int overCount = 0;
    private NovelSpider spider=new NovelSpider(null);
    private ContextMenu menu=new ContextMenu();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        init();
    }

    private void init() {
        autoSize();//自适应
        initMenu();//初始化菜单
        //点击搜索
        search.setOnMouseClicked(e ->searchNovelByName());
        //列表菜单
        list.setOnMouseClicked(e -> {
           if(e.getButton()== MouseButton.SECONDARY){//右键菜单
               //右键菜单跟随鼠标
               menu.setX(e.getScreenX());
               menu.setY(e.getScreenY());
               //获取选中索引
               int index = list.getSelectionModel().getSelectedIndex();
               MenuItem copy = menu.getItems().get(1);//复制链接
               MenuItem analysis = menu.getItems().get(0);//解析书籍
               MenuItem browse = menu.getItems().get(2);//浏览器打开
               copy.setOnAction(ev -> {
                   copyLink(index);
               });
               analysis.setOnAction(ev->{
                   copyLink(index);
                   ContentUtil.setContent("/fxml/analysis.fxml");
               });
               browse.setOnAction(ev->{
                   try {
                       Desktop.getDesktop().browse(new URI(((SearchNode) list.getItems().get(index)).getInfo().getUrl()));
                   } catch (Exception ex) {
                       ex.printStackTrace();
                   }
               });
               menu.show(DataManager.mainStage);
           }
        });
        text.setOnKeyPressed(event -> {
            if (event.getCode()== KeyCode.ENTER){
                searchNovelByName();
            }
        });
    }
    //初始化菜单
    void initMenu(){
        MenuItem copy = new MenuItem("复制链接");
        MenuItem analysis = new MenuItem("解析此书");
        MenuItem browse = new MenuItem("浏览器打开");
        copy.setGraphic(new ImageView("images/搜索页/复制链接.jpg"));
        analysis.setGraphic(new ImageView("images/搜索页/解析此书.jpg"));
        browse.setGraphic(new ImageView("images/搜索页/在浏览器打开.jpg"));
        menu.getItems().addAll(analysis,copy,browse);
    }
    //自适应居中
    private void autoSize() {
        searchPane.layoutXProperty().bind(searchRoot.widthProperty().divide(2).subtract(searchPane.widthProperty().divide(2)));
        list.prefWidthProperty().bind(searchRoot.widthProperty());
        list.prefHeightProperty().bind(searchRoot.heightProperty().subtract(73));
    }
    //根据名字搜索小说
    private void searchNovelByName(){
        //防止多次点击
        search.setDisable(true);
        //开启Loading...
        ProgressFrom pf = new ProgressFrom(DataManager.mainStage);
        pf.activateProgressBar();
        overCount = 0;//完成线程数量清零
        list.getItems().clear();//上次搜索结果清空
        String name = this.text.getText();//小说名字获取
        //开启任务列表10个网站小说搜索
        for (int i = 0; i < sites.length; i++) {
            final int index = i;
            Task<List<SearchNode>> task = new Task<List<SearchNode>>() {
                @Override
                protected List<SearchNode> call() throws Exception {
                    String responce = HttpUtil.request("http://unclezs.com:8081/novel/info?name=" + name + "&site=" + sites[index]);
                    List<NovelInfo> novelInfos = JSON.parseArray(responce, NovelInfo.class);
                    List<SearchNode> nodeList = new ArrayList<>();
                    for (NovelInfo info : novelInfos) {
                        nodeList.add(new SearchNode(info, spider.crawlDescImage(name)));
                    }
                    return nodeList;
                }
            };
            new Thread(task).start();
            task.setOnSucceeded(event -> {
                list.getItems().addAll(task.getValue());
                overCount++;
                //如果每个网站都查询完了，都没有找到，则没有找到小说
                if (overCount == sites.length) {
                    if (list.getItems().size() == 0) {
                        Label label = new Label("哎！没有找到这本书，可以试试目录解析！");
                        label.setFont(new Font(16));
                        list.getItems().add(label);
                    }
                    search.setDisable(false);//释放搜索按钮
                }
                //取消Loading
                pf.cancelProgressBar();
            });
        }

    }
    //url到剪贴板
    private void copyLink(int index){
        Clipboard cb = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.put(DataFormat.PLAIN_TEXT, ((SearchNode) list.getItems().get(index)).getInfo().getUrl());
        cb.setContent(content);
    }
}
