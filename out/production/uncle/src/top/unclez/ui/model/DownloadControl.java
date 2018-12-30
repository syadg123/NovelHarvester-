package top.unclez.ui.model;

import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import top.unclez.bean.ReaderConfig;
import top.unclez.ui.view.MAlert;
import top.unclez.ui.view.Main;
import top.unclez.utils.ObjUtil;
import top.unclez.utils.Utils;
import top.unclez.bean.GlobalValue;
import top.unclez.bean.DownloadConfig;
import top.unclez.downloader.Downloder;
import top.unclez.spider.MainSpider;
import top.unclez.ui.view.Reader;

import java.net.URL;
import java.util.*;

public class DownloadControl implements Initializable {

    @FXML
    TextField t_url;
    @FXML
    Button search;
    @FXML
    JFXButton download;
    @FXML
    JFXButton desc;
    @FXML
    JFXButton read;
    @FXML
    JFXRadioButton seall;//全选
    @FXML
    JFXTextArea content;
    @FXML
    JFXComboBox content_rule;
    @FXML
    JFXProgressBar progressbar;
    @FXML
    ListView<JFXCheckBox> chapter_list;
    Map<String, String> chapters;
    List<String> urls;
    List<String> task;
    ObservableList<JFXCheckBox> datalist;
    DownloadConfig config=GlobalValue.config;
    List<String> taskurl;

    @Override
    public void initialize(URL locations, ResourceBundle resources) {
        seall.setVisible(false);
        search.setGraphic(new ImageView("/image/search.png"));
        ObservableList<String> rulelist = FXCollections.observableArrayList();
        rulelist.add("正文规则1");
        rulelist.add("正文规则2");
        content_rule.setItems(rulelist);
        content_rule.setValue("正文规则2");
        t_url.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode()== KeyCode.ENTER){
                    search();
                }
            }
        });
    }
    /**
     * 解析目录
     *
     * @param event
     */
    public void onSearch(ActionEvent event) {
        search.setOnMouseClicked(e -> {
            search();
        });
    }

    /**
     * 加入书架
     *
     * @param event
     */
    public void showRead(ActionEvent event) {
        read.setOnMouseClicked(e -> {
            init();
            if(GlobalValue.taskurl.size()==0){
                content.setText("请先解析目录。");
                return;
            }
            clearDtaMap();
            GlobalValue.readerConfig=new ReaderConfig();
            GlobalValue.readerConfig.setReadlist(taskurl);
            GlobalValue.readerConfig.setReaddata(GlobalValue.data);
            GlobalValue.readerConfig.setCharset(GlobalValue.config.getCharset());
            GlobalValue.readerConfig.setRule(GlobalValue.config.getRule());
            GlobalValue.readerConfig.setCheader("");
            ObjUtil.saveReaderConfig(GlobalValue.config.getNovelname()+".conf");
            MAlert alert=new MAlert();
            alert.setMessageImg("/image/success.jpg");
            alert.setTitle("加入书架");
            alert.setText("加入成功");
            alert.start(new Stage());
        });
    }

    /**
     * 下载事件，异步下载
     *
     * @param event
     */
    public void onDownload(ActionEvent event) {
        download.setOnMouseClicked(e -> {
            init();
            if(GlobalValue.taskurl.size()==0){
                content.setText("请先解析目录。");
                return;
            }
            content.setText("下载中");
            content.setVisible(true);
            System.out.println(config.toString());
            for (String s : taskurl) {
                System.out.println(GlobalValue.data.get(s));
            }
            Downloder downloder = new Downloder(taskurl, config);
            GlobalValue.total = taskurl.size();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            download.setDisable(true);
                        }
                    });
                    downloder.start();
                }
            }).start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!GlobalValue.isDownloaded) {
                        progressbar.setProgress((GlobalValue.downloaded.size() * 1.0) / GlobalValue.total);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                content.setText("下载中 " +GlobalValue.downloaded.size()+ "章/"+ GlobalValue.total +"章");
                            }
                        });
                    }
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            progressbar.setProgress(1.0);
                            content.setText("下载完成 共" + GlobalValue.total + "章,失败" + GlobalValue.failed.size() + "章");
                            download.setDisable(false);
                        }
                    });
                }
            }).start();

        });
    }
    /**
     * 初始化
     * 进度条0，将taskurl，data，config加入，
     */
    private void init() {
        progressbar.setProgress(0);
        taskurl = new ArrayList<>();
        GlobalValue.init();
        config.setRule(content_rule.getValue().toString().charAt(4) + "");
        GlobalValue.config = config;
        int index = 0;
        int indexr = 1;
        if (datalist != null) {
            for (JFXCheckBox c : datalist) {
                if (c.isSelected()) {
                    taskurl.add(urls.get(index));
                    if (!c.getText().contains("章")) {
                        String names = "第" + indexr + "章  " + c.getText();
                        GlobalValue.data.put(urls.get(index), names);
                    }
                    indexr++;
                }
                index++;
            }
        }
        GlobalValue.taskurl = taskurl;
    }

    /**
     * 清除未选中的章节
     */
    public void clearDtaMap(){
        Map<String,String> selectedMap=new LinkedHashMap<>();
        for (String s : taskurl) {
            selectedMap.put(s,GlobalValue.data.get(s));
        }
        GlobalValue.data=selectedMap;
    }

    /**
     * 开始解析
     */
    private void search(){
        String url = t_url.getText().trim();
        if (url.length()>4) {
            System.out.println(url+"d");
            urls = new ArrayList<>();
            task = new ArrayList<>();
            datalist = FXCollections.observableArrayList();
            search.setDisable(true);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    chapters = MainSpider.getChapterList(url, GlobalValue.config.isFilter(),GlobalValue.config.isSort());
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            seall.setVisible(true);
                            search.setDisable(false);
                            config.setCharset(MainSpider.getConfig().get("charset"));
                            config.setNovelname(MainSpider.getConfig().get("title"));
                            GlobalValue.data.putAll(chapters);
                            for (String key : chapters.keySet()) {
                                JFXCheckBox checkBox = new JFXCheckBox();
                                checkBox.setSelected(true);
                                checkBox.setText(chapters.get(key));
                                datalist.add(checkBox);
                                urls.add(key);
                            }
                            read.setVisible(true);
                            if (datalist.size() == 0) {
                                JFXCheckBox tip = new JFXCheckBox();
                                tip.setText("没有匹配到书籍，请确认是否为目录链接");
                                datalist.add(tip);
                                chapter_list.setItems(datalist);
                                return;
                            }
                            chapter_list.setItems(datalist);
                            chapter_list.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent event) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                                                int index = chapter_list.getSelectionModel().getSelectedIndex();
                                                String text = MainSpider.getContent(urls.get(index), config.getCharset(), content_rule.getValue().toString().charAt(4) + "");
                                                if (GlobalValue.config.isNCR2CN()) {
                                                    text = Utils.NCR2Chinese(text);
                                                }
                                                if (GlobalValue.config.isT2S()) {
                                                    text = Utils.traditional2Simple(text);
                                                }
                                                final String texts = text;
                                                Platform.runLater(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        content.setWrapText(true);
                                                        content.setText(texts);
                                                    }
                                                });
                                            }
                                        }
                                    }).start();
                                }
                            });
                            seall.setOnMouseClicked(e2 -> {
                                boolean status = seall.isSelected();
                                for (JFXCheckBox c : datalist) {
                                    c.setSelected(status);
                                }
                                chapter_list.setItems(datalist);
                            });
                            desc.setOnMouseClicked(e->{
                                Collections.reverse(datalist);
                                Collections.reverse(urls);
                                chapter_list.setItems(datalist);
                            });
                        }
                    });
                }
            }).start();

        } else {
            content.setText("请先输入章节目录地址");
        }
    }
}
