package com.unclezs.UI.Controller;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXToggleButton;
import com.unclezs.Mapper.NovelMapper;
import com.unclezs.Mapper.ReaderMapper;
import com.unclezs.Model.ReaderConfig;
import com.unclezs.UI.Node.ProgressFrom;
import com.unclezs.UI.Utils.DataManager;
import com.unclezs.UI.Utils.LayoutUitl;
import com.unclezs.Utils.MybatisUtils;
import com.unclezs.Utils.VoiceUtil;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

/*
 *阅读器
 *@author unclezs.com
 *@date 2019.06.22 16:09
 */
public class ReaderController implements Initializable {
    @FXML
    Label title;//标题
    @FXML
    JFXToggleButton voice,changePage;//朗读
    @FXML
    Label readLabel, fontAdd, fontless, fontText, chapter, hideSet,
            setting, pageAdd, pageLess, song, yahei, kaiti, pageWidth,
            fontStyle, pageSize,changePageLabel,preBtn,nextBtn,leftLabel,rightLabel;//设置页
    @FXML
    Label huyan, yangpi, heise, molv, anse, baise;//背景色
    @FXML
    Pane setPane;//设置页面
    @FXML
    JFXTextArea content;//内容
    @FXML
    JFXListView<String> list;//目录
    @FXML
    Pane root;//根容器
    int index = DataManager.book.getCpage();//当前章节
    private VoiceUtil voiceUtil;//朗读工具类
    int scrollNum = 0;//滚动事件次数
    int keyNum = 0;//判断是否需要方向键翻页
    double contentMaxWidth = 800;
    ScrollPane sp;//滚动
    //网络小说
    String text;//正文
    String novelTitle;//标题
    boolean firstLoading=true;//第一次加载标志
    boolean isPageTopOver=false;//一章节顶部标志
    boolean isPageDownOver=false;//一章节尾部标志
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //加载小说
        loadNovel();
        //初始化
        loadReaderConfig();//加载页面设置
        initLoad();//窗口初始化
        listSelect();//加载目录设置
        voiceRead();//初始化朗读
        initSetting();//初始化设置
    }

    //目录选择
    void listSelect() {
        list.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                int index = list.getSelectionModel().getSelectedIndex();
                this.index = index;
                loadChapter(index);
                changePageHandler();
                list.setVisible(false);
            }
        });
    }

    //自适应页宽
    void autoSize() {
        Platform.runLater(() -> {
            //获取当前窗口宽高
            double width = DataManager.readerStage.getWidth();
            //如果窗口宽度低于内容初始宽度，进行缩放，直至小于窗口最小宽度
            if (width < contentMaxWidth) {
                content.maxWidthProperty().bind(root.widthProperty().subtract(60));//60为边距
            } else if (width > contentMaxWidth) {
                content.maxWidthProperty().bind(root.widthProperty().subtract(width - contentMaxWidth + 60));
            }
        });
    }

    //初始化窗口事件，只一次
    void initLoad() {
        //加载窗口改变事件
        DataManager.readerStage.widthProperty().addListener(e -> {
            autoSize();
        });
        DataManager.readerStage.heightProperty().addListener(e -> {
            autoSize();
        });
        //渲染完后加载第一次窗口绑定
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                LayoutUitl.bindStageSize(DataManager.readerStage, root);
                LayoutUitl.bindHeight(root, list, content);
                LayoutUitl.bindWidth(root, content, title);
                title.maxWidthProperty().bind(root.maxWidthProperty());
                title.layoutYProperty().bind(content.scrollTopProperty().multiply(-1));//标题位置随滚动条内容变化
                content.layoutXProperty().bind((root.layoutXProperty().add(root.widthProperty().subtract(content.widthProperty()))).divide(2));
                //翻页区域绑定
                leftLabel.prefWidthProperty().bind(root.widthProperty().multiply(0.2));
                leftLabel.prefHeightProperty().bind(root.heightProperty());
                rightLabel.prefWidthProperty().bind(root.widthProperty().multiply(0.2));
                rightLabel.prefHeightProperty().bind(root.heightProperty());
                rightLabel.layoutXProperty().bind(root.layoutXProperty().add(root.widthProperty().subtract(rightLabel.widthProperty())));
                autoSize();
                //获取scrollPane
                sp = (ScrollPane) content.lookup(".scroll-pane");//获取textarea的scrollPane;
//                System.out.println("222222222222");
                content.setScrollTop(DataManager.book.getvValue());
                changePageHandler();
            }
        });
        //窗口关闭事件
        DataManager.readerStage.setOnCloseRequest(e -> {
            new Thread(() -> {
                //关闭语音
                if (voiceUtil != null) voiceUtil.stop();
                //保存阅读器配置
                ReaderMapper mapper = MybatisUtils.getMapper(ReaderMapper.class);
                DataManager.readerConfig.setFontSize(content.getFont().getSize());//字体大小
                DataManager.readerConfig.setFontStyle(content.getFont().getFamily());//字体样式
                DataManager.readerConfig.setPageWidth(contentMaxWidth);//页宽
                DataManager.readerConfig.setStageHeight(DataManager.readerStage.getHeight());//舞台高度
                DataManager.readerConfig.setStageWidth(DataManager.readerStage.getWidth());//舞台宽度
                mapper.updateConfig(DataManager.readerConfig);
                MybatisUtils.getCurrentSqlSession().close();
                //保存新的书信息阅读位置
                NovelMapper bookMapper = MybatisUtils.getMapper(NovelMapper.class);
                bookMapper.updateCPage(DataManager.book.getId(), index, content.getScrollTop());
                MybatisUtils.getCurrentSqlSession().close();
            }).start();
            DataManager.mainStage.show();
        });
    }

    //加载阅读器配置
    void loadReaderConfig() {
        ReaderConfig config = DataManager.readerConfig;
        //舞台宽高
        DataManager.readerStage.setHeight(config.getStageHeight());
        DataManager.readerStage.setWidth(config.getStageWidth());
        //背景色
        changeColor(config.getBgColor(), config.getFontColor());
        //字体大小,样式
        content.setFont(Font.font(config.getFontStyle(), config.getFontSize()));
        title.setTextFill(Color.valueOf(config.getFontColor()));
        //页面宽度
        contentMaxWidth = config.getPageWidth();
        pageSize.setText((int) contentMaxWidth + "");
        autoSize();
    }

    void loadNovel() {
        if (DataManager.book.getIsWeb() == 1) {//加载网络小说
            list.setItems(FXCollections.observableArrayList(DataManager.wns.getChapters()));//加载目录
        } else {//加载本地书
            try {
                list.setItems(FXCollections.observableArrayList(DataManager.lns.getChapters()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        loadChapter(index);
    }

    //加载一章节
    void loadChapter(int index) {
        //停止朗读
        if (voiceUtil != null) voiceUtil.stop();
        voice.setSelected(false);
        if (DataManager.book.getIsWeb() == 1) {//网络书
            if (index >= list.getItems().size()) {
                title.setText("全书完");
                content.setText("\r\n\r\n这么快就看完了，快去看下一本吧");
                this.index = this.list.getItems().size() - 1;
                return;
            } else if (index < 0) {
                this.index = 0;
                index = 0;
            }
            final int i = index;
            if(firstLoading){//第一次不用loading动画，为了绘制成功上次位置
                text = DataManager.wns.getContent(i);
                novelTitle = DataManager.wns.getChapters().get(i);
                this.content.setText(text);
                this.title.setText(novelTitle);
                firstLoading=false;
            }else {//加载loading
                Task task = new Task() {
                    @Override
                    protected Object call() throws Exception {
                        text = DataManager.wns.getContent(i);
                        novelTitle = DataManager.wns.getChapters().get(i);
                        return null;
                    }
                };
                new Thread(task).start();
                ProgressFrom pf = new ProgressFrom(DataManager.readerStage);
                task.setOnSucceeded(e -> {
                    pf.cancelProgressBar();
                    this.content.setText(text);
                    this.title.setText(novelTitle);
                    //翻页顶部标志，因为需要loading与到底部共存，不得已放在这里，等待内容加载完毕，跳转底部，与changePage(false)对应
                    if(isPageTopOver){
                        content.selectEnd();
                        content.deselect();
                        isPageDownOver=true;
                        isPageTopOver=false;
                    }
                });
                pf.activateProgressBar();
            }
            this.index = index;
        } else {//本地书
            try {
                //第一章与最后一章防止越界限
                if (index > DataManager.lns.getChapters().size() - 1) {
                    title.setText("全书完");
                    content.setText("\r\n\r\n这么快就看完了，快去看下一本吧");
                    this.index = DataManager.lns.getChapters().size() - 1;
                    return;
                } else if (index < 0) {
                    this.index = 0;
                    index = 0;
                }
                Map<String, String> page = DataManager.lns.getCPageByIndex(index);
                //加载章节到UI
                title.setText(page.get("title"));
                this.content.setText(page.get("content"));
                this.index = index;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        DataManager.readerStage.setTitle(title.getText());//设置标题
    }

    //加载章节
    //方向键，鼠标翻页换章
    void changePageHandler() {
        //方向键翻页、换章节
        content.requestFocus();
        content.setOnKeyPressed(e -> {
            e.consume();
            double vValue = sp.getVvalue();//获取当前滚动条位置
            switch (e.getCode()) {
                case RIGHT:
                    loadChapter(++index);
                    sp.setVvalue(0);
                    return;
                case LEFT:
                    loadChapter(--index);
                    sp.setVvalue(0);
                    return;
                case UP:
                    sp.setVvalue(vValue - 0.1);//每次向上滚动10%
                    if (sp.getVvalue() == 0.0) {
                        keyNum++;
                    }
                    break;
                case DOWN:
                    sp.setVvalue(vValue + 0.1);
                    if (sp.getVvalue() == 1.0) {
                        keyNum++;
                    }
                    break;
                case F11:
                    DataManager.readerStage.setFullScreen(true);
                    return;
            }
            if (sp.getVvalue() == 1.0 && keyNum == 3) {//方向键翻页，到底部后再按两次即可下一章
                loadChapter(index++);
                keyNum = 0;
                sp.setVvalue(0);
            } else if (sp.getVvalue() == 0.0 && keyNum == 3) {//到顶部后再按两次即可上一章
                loadChapter(index--);
                keyNum = 0;
            }
        });
        //滚动翻页换章节
        content.setOnScroll((ScrollEvent event) -> {
            event.consume();
            scrollNum++;
            if (event.getDeltaY() == 13 && scrollNum > 5) {//顶部（滚动次数大于10防止误触）
                loadChapter(--index);
                scrollNum = 0;
            } else if (event.getDeltaY() == -13 && scrollNum > 5) {//底部
                loadChapter(++index);
                content.setScrollTop(0);
                scrollNum = 0;
            }
        });

        //按钮翻页
        preBtn.setOnMouseClicked(event -> {
            loadChapter(--index);
            sp.setVvalue(0);
            return;
        });
        nextBtn.setOnMouseClicked(e->{
            loadChapter(++index);
            sp.setVvalue(0);
            return;
        });
    }

    //语音朗读
    void voiceRead() {
        voice.setOnAction(e -> {
            if (voice.isSelected()) {
                voiceUtil = new VoiceUtil();
                voiceUtil.readText(title.getText() + content.getText());
            } else {
                if (voiceUtil != null) voiceUtil.stop();
            }
            voice.setSelected(voice.isSelected());
        });
    }

    //设置
    void initSetting() {
        //绑定位置在右下角
        setting.layoutYProperty().bind(root.layoutYProperty().add(root.heightProperty()).subtract(65));
        setting.layoutXProperty().bind(root.layoutXProperty().add(root.widthProperty()).subtract(31));
        nextBtn.layoutXProperty().bind(setting.layoutXProperty().add(2));
        nextBtn.layoutYProperty().bind(setting.layoutYProperty().add(31));
        preBtn.layoutYProperty().bind(nextBtn.layoutYProperty());
        preBtn.layoutXProperty().bind(nextBtn.layoutXProperty().subtract(30));
        setPane.layoutYProperty().bind(setting.layoutYProperty().subtract(330));
        setPane.layoutXProperty().bind(root.layoutXProperty().add(root.widthProperty()).subtract(230));
        //背景色更换
        molv.setOnMouseClicked(e -> changeColor(" #5e8e87", "#F0F0F0"));
        huyan.setOnMouseClicked(e -> changeColor("#CEEBCE", "#333333"));
        anse.setOnMouseClicked(e -> changeColor("#808A87", "#ddd"));
        heise.setOnMouseClicked(e -> changeColor("#393D49", "#c2c2c2"));
        yangpi.setOnMouseClicked(e -> changeColor("#e6dbbf", "#333333"));
        baise.setOnMouseClicked(e -> changeColor("#F0F0F0", "#333333"));
        //翻页区域点击
        rightLabel.setOnMouseClicked(e->{
            e.consume();
            changePage(true);
        });
        leftLabel.setOnMouseClicked(e->{
            e.consume();
            changePage(false);
        });
        //显示隐藏目录
        chapter.setOnMouseClicked(e -> {
            e.consume();
            list.setVisible(!list.isVisible());
        });
        //字体大小改变
        fontAdd.setOnMouseClicked(e -> {
            content.setFont(Font.font(content.getFont().getFamily(), content.getFont().getSize() + 1));
        });
        fontless.setOnMouseClicked(e -> {
            content.setFont(Font.font(content.getFont().getFamily(), content.getFont().getSize() - 1));
        });
        //打开隐藏设置
        setting.setOnMouseClicked(e -> {
            setPane.setVisible(true);
            setting.setVisible(false);
        });
        //关闭设置
        hideSet.setOnMouseClicked(e -> {
            setPane.setVisible(false);
            setting.setVisible(true);
        });
        //页面宽度
        pageLess.setOnMouseClicked(e -> {
            if (contentMaxWidth <= 850) {
                return;
            }
            contentMaxWidth -= 50;
            pageSize.setText((int) contentMaxWidth + "");
            autoSize();
        });
        pageAdd.setOnMouseClicked(e -> {
            if (contentMaxWidth >= 1500) {
                return;
            }
            contentMaxWidth += 50;
            pageSize.setText((int) contentMaxWidth + "");
            autoSize();
        });
        //字体设置
        kaiti.setOnMouseClicked(event -> {
            content.setFont(Font.font("KaiTi", content.getFont().getSize()));
        });
        song.setOnMouseClicked(event -> {
            content.setFont(Font.font("SimSun", content.getFont().getSize()));
        });
        yahei.setOnMouseClicked(e -> {
            content.setFont(Font.font("Microsoft YaHei", content.getFont().getSize()));
        });
        //显示翻页按钮
        preBtn.visibleProperty().bind(changePage.selectedProperty());
        nextBtn.visibleProperty().bind(changePage.selectedProperty());
        //图标设置
        fontText.setGraphic(new ImageView("images/设置页/字体.png"));
        fontAdd.setGraphic(new ImageView("images/设置页/字体放大.png"));
        fontless.setGraphic(new ImageView("images/设置页/字体缩小.png"));
        readLabel.setGraphic(new ImageView("images/设置页/朗读.png"));
        hideSet.setGraphic(new ImageView("images/设置页/关闭.jpg"));
        chapter.setGraphic(new ImageView("images/设置页/目录.jpg"));
        pageAdd.setGraphic(new ImageView("images/设置页/阅读页_页面增大.jpg"));
        pageLess.setGraphic(new ImageView("images/设置页/阅读页_页面缩小.jpg"));
        pageWidth.setGraphic(new ImageView("images/设置页/页面大小.jpg"));
        fontStyle.setGraphic(new ImageView("images/设置页/字体样式.jpg"));
        changePageLabel.setGraphic(new ImageView("images/设置页/显示.png"));
        preBtn.setGraphic(new ImageView("images/设置页/上一章.jpg"));
        nextBtn.setGraphic(new ImageView("images/设置页/下一章.jpg"));
    }

    /**
     * 切换背景
     *
     * @param color     背景色
     * @param fontcolor 字体颜色
     */
    public void changeColor(String color, String fontcolor) {
        content.setStyle("-fx-text-fill: " + fontcolor + ";");
        root.setStyle("-fx-background-color: " + color + ";");
        title.setTextFill(Color.valueOf(fontcolor));
        //更新配置
        DataManager.readerConfig.setBgColor(color);
        DataManager.readerConfig.setFontColor(fontcolor);
    }
    //左右点击按键翻页
    private void changePage(boolean isRight){
        if(isRight){//向右边翻页，向到底部自动翻页到下一节
            content.setScrollTop(content.getScrollTop()+content.getHeight()-20);
            if(sp.getVvalue()==1.0){
                if(!isPageDownOver){
                    isPageDownOver=true;
                }else {
                    loadChapter(++index);
                    sp.setVvalue(0);
                    isPageDownOver=false;
                }
            }
        }else {//向左翻页，到底部自动加载上一页
            content.setScrollTop(content.getScrollTop()-content.getHeight()+20);
            if(sp.getVvalue()==0){
                if(!isPageTopOver){
                    isPageTopOver=true;
                }else {
                    loadChapter(--index);
                }
            }
        }
    }
}
