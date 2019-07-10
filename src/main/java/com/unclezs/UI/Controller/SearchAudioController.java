package com.unclezs.UI.Controller;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.unclezs.Crawl.AudioNovelSpider;
import com.unclezs.Downloader.AudioDownloader;
import com.unclezs.Mapper.SettingMapper;
import com.unclezs.Model.AudioBook;
import com.unclezs.Model.AudioChapter;
import com.unclezs.Model.DownloadConfig;
import com.unclezs.UI.Node.ProgressFrom;
import com.unclezs.UI.Node.SearchAudioNode;
import com.unclezs.UI.Utils.DataManager;
import com.unclezs.UI.Utils.LayoutUitl;
import com.unclezs.UI.Utils.ToastUtil;
import com.unclezs.Utils.HttpUtil;
import com.unclezs.Utils.MybatisUtils;
import com.unclezs.Utils.URLEncoder;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.List;

/*
 *有声小说搜索控制类
 *@author unclezs.com
 *@date 2019.07.08 10:27
 */
public class SearchAudioController implements Initializable {
    @FXML
    JFXListView<SearchAudioNode> list;//搜索结果列表
    @FXML
    JFXListView<JFXCheckBox> chapterList;//章节列表
    @FXML
    JFXTextField input;//输入框
    @FXML
    JFXRadioButton jtw, ltw, htw, t56, t520, xsw, tsb;//7个源
    @FXML
    Pane searchPane, headerPane, root, setPane;
    @FXML
    Label search, changeSrc, save, closeList;
    private ObservableList<SearchAudioNode> listData = FXCollections.observableArrayList();//搜索结果listView数据
    private ObservableList<JFXCheckBox> chaptersData = FXCollections.observableArrayList();//章节listView数据
    private ObservableList<AudioChapter> chapters = FXCollections.observableArrayList();//章节数据
    private List<String> selectedSrc = new ArrayList<>();//选择的源
    private int count = 0;//加载完成数量
    private ContextMenu menu = new ContextMenu();//搜索结果菜单
    private ContextMenu chapterMenu = new ContextMenu();//章节菜单
    private String realUrl = "";
    private AudioBook book;//当前选中书籍
    private boolean startSelected = false;//shift多选开启标志
    private int startIndex;//多选开始位置


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        autoSite();//自适应
        initEventHandler();//事件监听
        initMenu();//初始化菜单
        loadSrc();//加载书源配置
        this.list.setItems(listData);
        this.chapterList.setItems(chaptersData);
    }

    //自适应
    private void autoSite() {
        //宽高自适应
        LayoutUitl.bind(DataManager.content, root);
        LayoutUitl.bindWidth(root, headerPane, list);
        //头部自适应
        headerPane.layoutXProperty().bind(root.layoutXProperty());
        searchPane.layoutXProperty().bind(
                root.layoutXProperty().add(
                        root.widthProperty().divide(2).subtract(searchPane.widthProperty().divide(2))));
        //listView自适应
        list.layoutYProperty().bind(headerPane.layoutYProperty().add(headerPane.heightProperty()));
        list.prefHeightProperty().bind(root.heightProperty().subtract(headerPane.heightProperty().add(15)));
        //换源菜单自适应
        setPane.layoutXProperty().bind(searchPane.layoutXProperty().add(changeSrc.layoutXProperty()));
        setPane.layoutYProperty().bind(headerPane.layoutYProperty().add(searchPane.layoutYProperty().add(changeSrc.layoutYProperty().add(changeSrc.heightProperty().add(5)))));
        //章节listView
        chapterList.layoutXProperty().bind(list.layoutXProperty().add(list.widthProperty()).subtract(chapterList.widthProperty()));
        chapterList.layoutYProperty().bind(list.layoutYProperty());
        LayoutUitl.bindHeight(list, chapterList);
        closeList.layoutXProperty().bind(chapterList.layoutXProperty().add(200));
        closeList.layoutYProperty().bind(chapterList.layoutYProperty().add(5));
    }

    //事件初始化
    private void initEventHandler() {
        //点击搜索
        search.setOnMouseClicked(e -> searchBooksByName());
        //回车搜索
        input.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                searchBooksByName();
            }
        });
        //换书源
        changeSrc.setOnMouseClicked(e -> setPane.setVisible(!setPane.isVisible()));
        //保存书源
        save.setOnMouseClicked(e -> {
            loadSrc();
            ToastUtil.toast("换源成功");
        });
        //关闭章节列表
        closeList.setOnMouseClicked(e -> chapterList.setVisible(false));
        closeList.visibleProperty().bindBidirectional(chapterList.visibleProperty());
        //右键菜单
        list.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                reLocation(e.getScreenX(), e.getScreenY(), menu);
                book=list.getSelectionModel().getSelectedItem().getInfo();
                //获取菜单项
                menu.getItems().get(0).setOnAction(event -> showChapters(book.getUrl()));//查看章节
                menu.getItems().get(2).setOnAction(event -> openInBrowser(book.getUrl()));//浏览去打开
                menu.show(DataManager.mainStage);
            }
        });
        chapterList.setOnMouseClicked(e -> {
            if (e.isShiftDown()) {//shift多选
                if (!startSelected) {
                    startIndex = chapterList.getSelectionModel().getSelectedIndex();
                    startSelected = true;
                    chapterList.getItems().get(startIndex).setSelected(!chapterList.getItems().get(startIndex).isSelected());
                    return;
                }
                if (startSelected) {
                    int end = chapterList.getSelectionModel().getSelectedIndex();
                    System.out.println(end+" "+startIndex);
                    for (int i = startIndex + 1; i <= end; i++) {
                        chapterList.getItems().get(i).setSelected(!chapterList.getItems().get(i).isSelected());
                    }
                    startSelected = false;
                }
            }else if(e.getButton()==MouseButton.SECONDARY){//其他事件
                reLocation(e.getScreenX(), e.getScreenY(), chapterMenu);
                int index = chapterList.getSelectionModel().getSelectedIndex();
                String url = chapters.get(index).getUrl();
                chapterMenu.getItems().get(0).setOnAction(event -> {
                    for (JFXCheckBox item : chapterList.getItems()) {
                        item.setSelected(true);
                    }
                });//全选
                chapterMenu.getItems().get(1).setOnAction(event -> {
                    for (JFXCheckBox item : chapterList.getItems()) {
                        item.setSelected(false);
                    }
                });//全不选
                chapterMenu.getItems().get(3).setOnAction(event -> addToSelf());//加入书架
                chapterMenu.getItems().get(4).setOnAction(event -> downloadSelectedAudio());//下载选中
                chapterMenu.getItems().get(6).setOnAction(event -> testSrcIsOK(url));//检测失效
                chapterMenu.getItems().get(7).setOnAction(event -> openInBrowser(url));//浏览器打开
                chapterMenu.getItems().get(8).setOnAction(event -> copyRealUrl(url));//复制音频链接
                chapterMenu.show(DataManager.mainStage);
            }
        });
    }

    //加载书源配置
    private void loadSrc() {
        selectedSrc.clear();
        if (jtw.isSelected()) {
            selectedSrc.add("audio699");
        }
        if (t56.isSelected()) {
            selectedSrc.add("ting56");
        }
        if (htw.isSelected()) {
            selectedSrc.add("ting89");
        }
        if (tsb.isSelected()) {
            selectedSrc.add("ysts8");
        }
        if (xsw.isSelected()) {
            selectedSrc.add("ysxs8");
        }
        if (t520.isSelected()) {
            selectedSrc.add("520tingshu");
        }
        if (ltw.isSelected()) {
            selectedSrc.add("ting55");
        }
        setPane.setVisible(false);
    }

    //搜索书籍
    private void searchBooksByName() {
        //开启loading
        count = 0;
        ProgressFrom pf = new ProgressFrom(DataManager.mainStage);
        pf.activateProgressBar();
        //清空上次搜索结果
        listData.clear();
        AudioNovelSpider spider = new AudioNovelSpider();
        for (String site : selectedSrc) {
            Task<List<SearchAudioNode>> task = new Task<List<SearchAudioNode>>() {
                @Override
                protected List<SearchAudioNode> call() throws Exception {
                    List<AudioBook> books = spider.searchBook(input.getText(), site);
                    List<SearchAudioNode> l = new ArrayList<>();
                    for (AudioBook book : books) {
                        l.add(new SearchAudioNode(book));
                    }
                    return l;
                }
            };
            new Thread(task).start();
            task.setOnSucceeded(ev -> {
                count++;
                listData.addAll(task.getValue());
                pf.cancelProgressBar();
                if (count == selectedSrc.size()) {
                    ToastUtil.toast("已加载全部！");
                }
            });
        }
    }

    //初始化菜单
    private void initMenu() {
        closeList.setGraphic(new ImageView("images/设置页/关闭.jpg"));
        MenuItem showChapter = new MenuItem("查看目录",new ImageView("images/解析页/查看.jpg"));
        MenuItem dowloadAll = new MenuItem("下载选中",new ImageView("images/菜单页/有声下载.jpg"));
        MenuItem openInBrowser = new MenuItem("浏览器打开",new ImageView("images/搜索页/在浏览器打开.jpg"));
        MenuItem openInBrowserC = new MenuItem("浏览器打开",new ImageView("images/搜索页/在浏览器打开.jpg"));
        MenuItem addToBookSelf = new MenuItem("加入书架",new ImageView("images/菜单页/书架.jpg"));
        MenuItem testOK = new MenuItem("检测失效",new ImageView("images/搜索页/检测.jpg"));
        MenuItem realUrl = new MenuItem("复制音频地址",new ImageView("images/搜索页/复制链接.jpg"));
        MenuItem selectAll = new MenuItem("全选",new ImageView("images/解析页/全选.jpg"));
        MenuItem selectNone = new MenuItem("全不选",new ImageView("images/解析页/反选.jpg"));
        menu.getItems().addAll(showChapter, addToBookSelf, new SeparatorMenuItem(), openInBrowser);
        chapterMenu.getItems().addAll(selectAll, selectNone, new SeparatorMenuItem(),
                addToBookSelf, dowloadAll, new SeparatorMenuItem(),
                testOK, openInBrowserC, realUrl);
    }

    //设置菜单x，y坐标
    private void reLocation(double x, double y, ContextMenu node) {
        node.setX(x);
        node.setY(y);
    }

    //浏览器打开URL
    private void openInBrowser(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //查看章节列表
    private void showChapters(String url) {
        chapters.clear();
        ProgressFrom loading = new ProgressFrom(DataManager.mainStage);//loading
        loading.activateProgressBar();
        Task<List<JFXCheckBox>> task = new Task<List<JFXCheckBox>>() {
            @Override
            protected List<JFXCheckBox> call() throws Exception {
                AudioNovelSpider spider = new AudioNovelSpider();
                chapters.addAll(spider.getChapters(url));
                List<JFXCheckBox> cList = new ArrayList<>();
                for (AudioChapter chapter : chapters) {
                    //创建条目
                    JFXCheckBox item = new JFXCheckBox(chapter.getTitle());
                    item.setSelected(true);
                    cList.add(item);
                }
                return cList;
            }
        };
        new Thread(task).start();
        task.setOnSucceeded(ev -> {
            //清除原有的
            chaptersData.clear();
            chaptersData.addAll(task.getValue());//添加进列表
            book.setChapters(chapters);
            chapterList.setVisible(true);//显示
            loading.cancelProgressBar();
        });
    }

    //测试音频源是否失效
    private void testSrcIsOK(String url) {
        ProgressFrom loading = new ProgressFrom(DataManager.mainStage);
        Task<Integer> task = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                realUrl = getRealUrl(url);
                return HttpUtil.getResponseCode(realUrl);
            }
        };
        new Thread(task).start();
        loading.activateProgressBar();
        task.setOnSucceeded(e -> {
            if (task.getValue() == 200) {
                ToastUtil.toast("音频有效！");
            } else {
                ToastUtil.toast("无效音频！");
            }
            loading.cancelProgressBar();
        });
    }

    //复制音频源
    private void copyRealUrl(String url) {
        ProgressFrom loading = new ProgressFrom(DataManager.mainStage);
        Task<String> task = new Task<String>() {
            @Override
            protected String call() throws Exception {
                return getRealUrl(url);
            }
        };
        new Thread(task).start();
        loading.activateProgressBar();
        task.setOnSucceeded(e -> {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            Map map = new HashMap<>();
            map.put(DataFormat.PLAIN_TEXT, task.getValue());
            clipboard.setContent(map);
            ToastUtil.toast("复制成功");
            loading.cancelProgressBar();
        });

    }

    //获取音频地址
    private String getRealUrl(String url) {
        AudioNovelSpider spider = new AudioNovelSpider();
        return spider.getSrc(url);
    }

    //下载选中音频
    private void downloadSelectedAudio(){
        //获取选中章节
        setSelectedChapters();
        //获取下载配置
        SettingMapper mapper = MybatisUtils.getMapper(SettingMapper.class);
        DownloadConfig config = mapper.querySetting();
        if(config.getPath().equals("")||config.getPath()==null){//路径不为空的时候使用当前路径
            config.setPath(Class.class.getResource("/").getPath().substring(1));
            mapper.updateSetting(config);
        }else if(!new File(config.getPath()).exists()){
            ToastUtil.toast("保存路径不存在！");
            return;
        }
        AudioDownloader downloader=new AudioDownloader(config,book);
        DownloadController.addTask(downloader);
        //开启异步下载
        new Thread(()->{
            downloader.start();
        }).start();
        ToastUtil.toast("添加下载成功！");
    }
    //加入书架
    private void addToSelf(){
        AudioBookSelfController.addBookToSelf(book);
        ToastUtil.toast("加入书架成功");
    }
    //设置选中章节
    private void setSelectedChapters(){
        List<AudioChapter> selected=new ArrayList<>();
        for (int i = 0; i < chapterList.getItems().size(); i++) {
            if(chapterList.getItems().get(i).isSelected()){
                selected.add(chapters.get(i));
            }
        }
        book.setChapters(selected);
    }
}
