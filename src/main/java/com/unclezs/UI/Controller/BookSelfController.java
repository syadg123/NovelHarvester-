package com.unclezs.UI.Controller;

import com.unclezs.Crawl.LocalNovelLoader;
import com.unclezs.Crawl.NovelSpider;
import com.unclezs.Crawl.WebNovelLoader;
import com.unclezs.Mapper.AnalysisMapper;
import com.unclezs.Mapper.ChapterMapper;
import com.unclezs.Mapper.NovelMapper;
import com.unclezs.Mapper.ReaderMapper;
import com.unclezs.Model.Book;
import com.unclezs.UI.App.Reader;
import com.unclezs.UI.Node.BookNode;
import com.unclezs.UI.Node.ProgressFrom;
import com.unclezs.UI.Node.SearchNode;
import com.unclezs.UI.Utils.ContentUtil;
import com.unclezs.UI.Utils.DataManager;
import com.unclezs.UI.Utils.LayoutUitl;
import com.unclezs.Utils.FileUtil;
import com.unclezs.Utils.MybatisUtils;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.ibatis.session.SqlSession;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/*
 *书架页面
 *@author unclezs.com
 *@date 2019.06.22 09:31
 */
public class BookSelfController implements Initializable {
    @FXML
    GridPane books;//书架
    @FXML
    ScrollPane sp;//书架滚动
    @FXML
    VBox root;//主容器
    @FXML
    Pane bottom;//底部菜单
    @FXML
    Label add;//导入书
    //成员
    private List<BookNode> list = new ArrayList();
    private ContextMenu menu;//添加导入菜单
    private ContextMenu rightMenu;//右键菜单
    private NovelSpider spider=new NovelSpider(null);
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        queyBook();//查询所有书
        initBindSize();//绑定宽高
        initMenu();//初始化菜单
        initEventHandler();//初始化事件监听
    }

    //绑定窗口，自适应
    private void initBindSize() {
        LayoutUitl.bind(root, bottom, sp);
        LayoutUitl.bind(sp, books);
    }

    //初始化所有事件监听
    private void initEventHandler() {
        //自适应书架
        sp.widthProperty().addListener(e -> {
            reloadBookMark();
        });
        //添加图书
        add.setOnMouseClicked(e -> {
            menu.setX(e.getScreenX());
            menu.setY(e.getScreenY());
            menu.show(DataManager.mainStage);
        });
        //文件拖入事件
        root.setOnDragOver((DragEvent e) -> {
            File file = e.getDragboard().getFiles().get(0);
            if (file.getAbsolutePath().endsWith(".txt")) {
                e.acceptTransferModes(TransferMode.LINK);
            }
        });
        root.setOnDragDropped((DragEvent e) -> {
            File file = e.getDragboard().getFiles().get(0);
            if (e.isAccepted()) {
                addBook(file);//添加
            }
        });
    }

    //重新加载书架
    private void reloadBookMark() {
        double width = sp.getWidth();
        books.getChildren().clear();
        int hNum = (int) (width / 180);//BookNode的左右Padding+宽度
        int vNum = (list.size() / hNum) + 1;
        int lastRowNodes = list.size() % hNum;
        books.setHgap(hNum);
        books.setVgap(vNum);
        int k = 0;
        for (int i = 0; i < vNum; i++) {
            if (i == vNum - 1) {
                hNum = lastRowNodes;
            }
            for (int j = 0; j < hNum; j++) {
                rightKeyMenu(list.get(k));//右左键菜单事件
                books.add(list.get(k), j, i);
                k++;
            }
        }
    }

    //导入本地书
    private void importLoaclBook() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("文本文件", "*.txt");
        fileChooser.getExtensionFilters().addAll(filter);
        File file = fileChooser.showOpenDialog(DataManager.mainStage);
        if (file == null)
            return;
        addBook(file);//添加
    }

    //导入网络书籍
    private void importWebBook() {
        ContentUtil.setContent("/fxml/search.fxml");
    }

    //添加一本书
    private void addBook(File file) {
        //加载loading
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                String path = file.getPath();
                LocalNovelLoader loader = new LocalNovelLoader(path);
                String name = loader.getName();
                String img = spider.crawlDescImage(name);
                String imgPath = FileUtil.uploadFile("./image/" + name + ".jpg", img);
                System.out.println(imgPath);
                //存库
                NovelMapper novelMapper = MybatisUtils.getMapper(NovelMapper.class);
                novelMapper.save(new Book(name, path, imgPath));//添加
                //入架
                list.add(new BookNode(novelMapper.findLastOne()));
                //归还sqlsession
                MybatisUtils.getCurrentSqlSession().close();
                return null;
            }
        };
        new Thread(task).start();
        ProgressFrom pf = new ProgressFrom(DataManager.mainStage);
        task.setOnSucceeded(e -> {
            pf.cancelProgressBar();
            Platform.runLater(() -> reloadBookMark());
        });
        pf.activateProgressBar();
    }

    //初始化添加菜单
    private void initMenu() {
        //初始化导入菜单
        menu = new ContextMenu();
        MenuItem addloacl = new MenuItem("导入本地小说");
        addloacl.setGraphic(new ImageView("images/菜单页/本地.jpg"));
        addloacl.setOnAction(i -> importLoaclBook());
        MenuItem addweb = new MenuItem("添加网络小说");
        addweb.setOnAction(e -> importWebBook());
        addweb.setGraphic(new ImageView("images/菜单页/网络.jpg"));
        menu.getItems().addAll(addloacl, new SeparatorMenuItem(), addweb);
        //初始化右键菜单
        rightMenu = new ContextMenu();
        MenuItem delete = new MenuItem("删除此书");
        MenuItem cover = new MenuItem("更换封面");
        MenuItem cookies = new MenuItem("更新Cookies");
        MenuItem link = new MenuItem("复制链接");
        MenuItem reName = new MenuItem("修改书名");
        delete.setGraphic(new ImageView("images/菜单页/删除.jpg"));
        cover.setGraphic(new ImageView("images/菜单页/封面.jpg"));
        cookies.setGraphic(new ImageView("images/书架/更新.jpg"));
        reName.setGraphic(new ImageView("images/书架/修改.jpg"));
        link.setGraphic(new ImageView("images/搜索页/复制链接.jpg"));
        rightMenu.getItems().addAll(reName, cover, delete, new SeparatorMenuItem(), cookies, link);
    }

    //查询数据
    private void queyBook() {
        long l = System.currentTimeMillis();
        list.clear();
        List<Book> books = MybatisUtils.getMapper(NovelMapper.class).findAll();
        for (Book book : books) {
            list.add(new BookNode(book));
        }
        MybatisUtils.getCurrentSqlSession().close();//归还session
        System.out.println("书架查库：" + (System.currentTimeMillis() - l));
    }

    //节点右左键菜单
    private void rightKeyMenu(BookNode node) {
        node.setOnMouseClicked(e -> {
            //右键事件
            if (e.getButton() == MouseButton.SECONDARY) {
                //获取位置
                rightMenu.setY(e.getScreenY());
                rightMenu.setX(e.getScreenX());
                //事件绑定
                MenuItem reName = rightMenu.getItems().get(0);
                MenuItem cover = rightMenu.getItems().get(1);
                MenuItem delete = rightMenu.getItems().get(2);
                MenuItem cookies = rightMenu.getItems().get(4);
                MenuItem link = rightMenu.getItems().get(5);
                //删除事件
                delete.setOnAction(event -> {
                    list.remove(node);//移除书架
                    //删库
                    Integer id = node.getBook().getId();
                    //异步删除数据库信息
                    new Thread(()->{
                        SqlSession session = MybatisUtils.openSqlSession(true);
                        session.getMapper(NovelMapper.class).deleteById(id);
                        session.getMapper(ChapterMapper.class).deleteAllChapters(id);
                        session.getMapper(AnalysisMapper.class).deleteSpiderConfig(id);
                        session.close();
                    }).start();
                    reloadBookMark();//刷新书架
                });
                //重命名事件
                reName.setOnAction(event -> reNameBook(node));
                //更换封面事件
                cover.setOnAction(event -> newCover(node));
                //更新Cookies事件
                cookies.setOnAction(event -> newCookies(node.getBook().getId()));
                //复制链接
                link.setOnAction(event -> copyLink(node));
                rightMenu.show(DataManager.mainStage);
            }
            //左键事件
            if (e.getButton() == MouseButton.PRIMARY) {
                Book book = node.getBook();
                openBook(book);
            }
        });
    }

    //打开一本书
    private void openBook(Book book) {
        //加载loading动画
        Task openBook = new Task() {
            @Override
            protected Object call() {
                SqlSession session = MybatisUtils.openSqlSession(true);
                DataManager.readerConfig = session.getMapper(ReaderMapper.class).queryConfig();//加载阅读器配置
                NovelMapper novelMapper = session.getMapper(NovelMapper.class);
                DataManager.book = novelMapper.findById(book.getId());//加载书籍信息
                if (DataManager.book.getIsWeb() == 0) {//判断是否为网络书籍
                    DataManager.lns = new LocalNovelLoader(book.getPath());//加载本地书籍
                } else {
                    spider.setConf(session.getMapper(AnalysisMapper.class).queryAnalysisConfig(book.getId()));//获取网络小说解析配置
                    DataManager.wns = new WebNovelLoader(book.getId(), book.getCharset(),spider);//加载网络书籍
                    DataManager.wns.loadOnPage(book.getCpage());
                }
                session.close();
                return null;
            }
        };
        new Thread(openBook).start();
        ProgressFrom pf = new ProgressFrom(DataManager.mainStage);
        openBook.setOnSucceeded(e -> {
            try {
                pf.cancelProgressBar();
                //打开本地书或者打开网络书
                if (DataManager.lns == null || DataManager.lns.isExist() || DataManager.book.getIsWeb() == 1) {
                    Reader reader = new Reader();
                    reader.start(new Stage());
                    DataManager.mainStage.close();
                } else {
                    //书籍不存在则提示
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.initOwner(DataManager.mainStage);
                    alert.setHeaderText("书籍不存在");
                    alert.setTitle("信息");
                    alert.setContentText("请确认本书是否被移动或者删除了！");
                    alert.show();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        pf.activateProgressBar();//关闭loading

    }

    //重命名书籍
    private void reNameBook(BookNode book) {
        TextInputDialog dialog = getInputDialog("输入新的书名");
        dialog.getEditor().setText(book.getLabel().getText());
        Optional result = dialog.showAndWait();
        if (result.isPresent()) {
            String newName = dialog.getEditor().getText();
            Task task=new Task<String>() {
                @Override
                protected String call() throws Exception {
                    //更新数据库
                    MybatisUtils.getMapper(NovelMapper.class).updateBookName(book.getBook().getId(), newName);
                    MybatisUtils.getCurrentSqlSession().close();
                    //获取新的封面
                    String imgUrl = spider.crawlDescImage(newName);
                    String path = FileUtil.uploadFile("./image/" + newName + ".jpg", imgUrl);
                    //更新库
                    MybatisUtils.getMapper(NovelMapper.class).updateBookCover(book.getBook().getId(), path);
                    MybatisUtils.getCurrentSqlSession().close();
                    return path;
                }
            };

            new Thread(task).start();
            book.getLabel().setText(newName);
            list.set(list.indexOf(book), book);
            task.setOnSucceeded(e->book.getImg().setImage(new Image("file:" + task.getValue())));
        }
    }

    //更换封面事件
    private void newCover(BookNode book) {
        FileChooser chooser = new FileChooser();
        //文件类型过滤
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("图片", "*.jpg", "*.jpeg", "*.png");
        chooser.getExtensionFilters().addAll(filter);
        chooser.setTitle("请选择一张图片");
        File file = chooser.showOpenDialog(DataManager.mainStage);
        if (file != null) {
            String path = file.getAbsolutePath();
            book.getImg().setImage(new Image("file:" + path));
            //更新库
            new Thread(() -> {
                MybatisUtils.getMapper(NovelMapper.class).updateBookCover(book.getBook().getId(), path);
                MybatisUtils.getCurrentSqlSession().close();
            }).start();
        }
    }

    //更新Cookies事件
    private void newCookies(Integer aid) {
        TextInputDialog dialog = getInputDialog("请输入新的Cookies");
        Optional result = dialog.showAndWait();
        if (result.isPresent()) {
            String newV = dialog.getEditor().getText();
            //更新Cookies
            new Thread(() -> {
                MybatisUtils.getMapper(AnalysisMapper.class).updateCookies(aid, newV);
                MybatisUtils.getCurrentSqlSession().close();
            }).start();
        }
    }

    //复制链接
    private void copyLink(BookNode book) {
        Clipboard cb = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.put(DataFormat.PLAIN_TEXT, book.getBook().getPath());
        cb.setContent(content);
    }

    //获取输入框
    private TextInputDialog getInputDialog(String title) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(null);
        dialog.getDialogPane().setStyle("-fx-graphic: url('images/1.png');");//去除图标
        dialog.initModality(Modality.APPLICATION_MODAL);//模态
        dialog.initOwner(DataManager.mainStage);
        return dialog;
    }
}
