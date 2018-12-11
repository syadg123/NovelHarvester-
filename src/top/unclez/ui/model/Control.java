package top.unclez.ui.model;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import top.unclez.Utils.FileUtil;
import top.unclez.Utils.Utils;
import top.unclez.bean.GlobalValue;
import top.unclez.downloader.DownloadConfig;
import top.unclez.downloader.Downloder;
import top.unclez.spider.MainSpider;
import top.unclez.ui.view.BookMark;
import top.unclez.ui.view.Reader;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class Control implements Initializable {

    @FXML
    TextField t_url;
    @FXML
    Button search;
    @FXML
    Button download;
    @FXML
    Button bookmark;
    @FXML
    Button cloction;
    @FXML
    Button read;
    @FXML
    Button help;
    @FXML
    Label location;
    @FXML
    CheckBox sort;
    @FXML
    CheckBox filter;
    @FXML
    CheckBox seall;//全选
    @FXML
    CheckBox t2s;
    @FXML
    CheckBox ncr2cn;
    @FXML
    TextArea content;
    @FXML
    ChoiceBox content_rule;
    @FXML
    ProgressBar progressbar;
    @FXML
    ChoiceBox delay;
    @FXML
    ChoiceBox pagenum;
    @FXML
    Label finished;
    @FXML
    ListView<CheckBox> chapter_list;
    Map<String, String> chapters;
    List<String> urls;
    List<String> task;
    ObservableList<CheckBox> datalist;
    DownloadConfig config;
    List<String> taskurl;

    @Override
    public void initialize(URL locations, ResourceBundle resources) {
        read.setVisible(false);
        filter.setSelected(true);
        sort.setSelected(false);
        seall.setSelected(true);
        seall.setVisible(false);
        config = new DownloadConfig();
        ObservableList<String> rulelist = FXCollections.observableArrayList();
        ObservableList<String> delaylist = FXCollections.observableArrayList();
        ObservableList<String> pagenumlist = FXCollections.observableArrayList();
        for (int i = 0; i < 16; i++) {
            delaylist.add(i * 100 + "");
            pagenumlist.add((i + 1) * 50 + "");
        }
        rulelist.add("正文规则1");
        rulelist.add("正文规则2");
        content_rule.setItems(rulelist);
        content_rule.setValue("正文规则2");
        pagenum.setItems(pagenumlist);
        pagenum.setValue("50");
        delay.setItems(delaylist);
        delay.setValue("300");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        location.setText(FileUtil.getPath());
                    }
                });
            }
        }).start();
    }

    /**
     * 解析目录
     *
     * @param event
     */
    public void onSearch(ActionEvent event) {
        search.setOnMouseClicked(e -> {
            String url = t_url.getText().trim();
            if (url != null) {
                urls = new ArrayList<>();
                task = new ArrayList<>();
                datalist = FXCollections.observableArrayList();
                search.setDisable(true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        chapters = MainSpider.getChapterList(url, filter.isSelected(), sort.isSelected());
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                seall.setVisible(true);
                                search.setDisable(false);
                                config.setCharset(MainSpider.getConfig().get("charset"));
                                config.setNovelname(MainSpider.getConfig().get("title"));
                                GlobalValue.data.putAll(chapters);
                                for (String key : chapters.keySet()) {
                                    CheckBox checkBox = new CheckBox();
                                    checkBox.setSelected(true);
                                    checkBox.setText(chapters.get(key));
                                    datalist.add(checkBox);
                                    urls.add(key);
                                }
                                read.setVisible(true);
                                if (datalist.size() == 0) {
                                    CheckBox tip = new CheckBox();
                                    tip.setText("没有匹配到书籍，请确认是否位目录链接");
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
                                                    if (ncr2cn.isSelected()) {
                                                        text = Utils.NCR2Chinese(text);
                                                    }
                                                    if (t2s.isSelected()) {
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
                                    for (CheckBox c : datalist) {
                                        c.setSelected(status);
                                    }
                                    chapter_list.setItems(datalist);
                                });
                            }
                        });
                    }
                }).start();

            } else {
                // TODO: 2018/12/8
            }
        });
    }

    /**
     * 打开书架
     *
     * @param event
     */
    public void openBookMark(ActionEvent event) {
        bookmark.setOnMouseClicked(e -> {
            init();
            GlobalValue.bookstage = new Stage();
            BookMark bookMark = new BookMark();
            bookMark.start(GlobalValue.bookstage);
        });
    }

    /**
     * 打开阅读界面，传递配置信息
     *
     * @param event
     */
    public void showRead(ActionEvent event) {
        read.setOnMouseClicked(e -> {
            Reader reader = new Reader();
            init();
            GlobalValue.readstage = new Stage();
            reader.start(GlobalValue.readstage);
            GlobalValue.stage.close();
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
            finished.setText("下载中");
            finished.setVisible(true);
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
                    }
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            progressbar.setProgress(1.0);
                            finished.setText("下载完成 共" + GlobalValue.total + "章,失败" + GlobalValue.failed.size() + "章");
                            download.setDisable(false);
                        }
                    });
                }
            }).start();

        });
    }

    /**
     * 选择下载路径
     *
     * @param event
     */
    public void choosePath(ActionEvent event) {
        cloction.setOnMouseClicked(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File file = directoryChooser.showDialog(GlobalValue.stage);
            if (file != null) {
                String path = file.getPath();
                location.setText(path);
                config.setPath(path);
            }
        });
    }

    /**
     * 显示帮助内容
     */
    public void showHelp() {
        help.setOnMouseClicked(e -> {
            StringBuffer helpword = new StringBuffer();
            helpword.append("                   使用帮助\n\r\n");
            helpword.append("    Uncle小说下载器支持绝大多数章节目录匹配正文匹配，不支持vip章节，可以自己找免费的网站源，几乎都能找到。\n\r\n");
            helpword.append("    章节如果没有匹配到的话可以取消章节过滤试试。\n\r\n");
            helpword.append("    正文如果匹配的不准确可以试试换一个正文规则，规则1匹配的比较准确，规则2匹配的更完全，支持网站数更多。\n\r\n");
            helpword.append("    支持繁体转简体，和把NCR(&#dddd;)格式的正文转化成汉字，一般网站使用的繁体字才会有这种NCR的正文，比如嘛就不说了😂。\n\r\n");
            helpword.append("    如果有下载失败的章节，就增加每个线程的下载章节数量，也可以适当调整延迟，下载过慢的话可以根据情况调整延迟和每个线程的任务量。\n\r\n");
            helpword.append("    乱序重排功能没必要的就不用勾选，匹配更加快速。\n\r\n");
            content.setText(helpword.toString());
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
        config.setT2S(t2s.isSelected());
        config.setNCR2CN(ncr2cn.isSelected());
        config.setRule(content_rule.getValue().toString().charAt(4) + "");
        config.setPage(Integer.parseInt(pagenum.getValue().toString()));
        config.setDelay(Integer.parseInt(delay.getValue().toString()));
        GlobalValue.config = config;
        int index = 0;
        int indexr = 1;
        if (datalist != null) {
            for (CheckBox c : datalist) {
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
}
