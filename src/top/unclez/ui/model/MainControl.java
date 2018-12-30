package top.unclez.ui.model;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import top.unclez.Utils.FileUtil;
import top.unclez.ui.util.ResourceLoader;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TestControl implements Initializable {
    @FXML
    Button bookmark;
    @FXML
    Button download;
    @FXML
    Button about;
    @FXML
    Button set;
    @FXML
    VBox MainPane;
    @FXML
    Pane menu;
    @FXML
    HBox mainBox;
    @FXML
    Pane content;
    private static Pane downloadPane;
    private static Pane setPane;
    private static Pane aboutPane;
    private static Pane bookmarkPane;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FileUtil.loadConfig();
        init();
        changeBtn(0);
        try {
            bookmarkPane=FXMLLoader.load(ResourceLoader.getFXMLResource("bookmark.fxml"));
            bookmarkPane.prefHeightProperty().bind(content.heightProperty());
            bookmarkPane.prefWidthProperty().bind(content.widthProperty());
            content.getChildren().addAll(bookmarkPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void init(){
        mainBox.prefHeightProperty().bind(MainPane.heightProperty());
        mainBox.prefWidthProperty().bind(MainPane.widthProperty());
        set.setOnMouseClicked(e->{
            changeBtn(2);
            content.getChildren().clear();
            if(setPane==null){
                try {
                    setPane=new Pane();
                    setPane=FXMLLoader.load(ResourceLoader.getFXMLResource("setting.fxml"));
                    setPane.prefHeightProperty().bind(content.heightProperty());
                    setPane.prefWidthProperty().bind(content.widthProperty());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            content.getChildren().addAll(setPane);
        });
        download.setOnMouseClicked(e->{
            changeBtn(1);
            content.getChildren().clear();
            if(downloadPane==null){
                try {
                    downloadPane=new Pane();
                    downloadPane=FXMLLoader.load(ResourceLoader.getFXMLResource("download.fxml"));
                    downloadPane.prefHeightProperty().bind(content.heightProperty());
                    downloadPane.prefWidthProperty().bind(content.widthProperty());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            content.getChildren().addAll(downloadPane);
        });
        about.setOnMouseClicked(e->{
            changeBtn(3);
            content.getChildren().clear();
            if(aboutPane==null){
                try {
                    aboutPane=new Pane();
                    aboutPane=FXMLLoader.load(ResourceLoader.getFXMLResource("about.fxml"));
                    aboutPane.prefHeightProperty().bind(content.heightProperty());
                    aboutPane.prefWidthProperty().bind(content.widthProperty());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            content.getChildren().addAll(aboutPane);
        });
        bookmark.setOnMouseClicked(e->{
            changeBtn(0);
            content.getChildren().clear();
            try {
                bookmarkPane=FXMLLoader.load(ResourceLoader.getFXMLResource("bookmark.fxml"));
                bookmarkPane.prefHeightProperty().bind(content.heightProperty());
                bookmarkPane.prefWidthProperty().bind(content.widthProperty());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            content.getChildren().addAll(bookmarkPane);
        });
    }

    /**
     * 改变菜单选中颜色
     * @param flag 哪一个菜单被点击 0开始
     */
    private void changeBtn(int flag){
        int index=flag*4;
        String[] select={"bookmark.jpg","down_no.jpg","set_no.png","help_no.png"
                             ,"bookmark_no.png","down.png","set_no.png","help_no.png"
                             ,"bookmark_no.png","down_no.jpg","set.png","help_no.png"
                             ,"bookmark_no.png","down_no.jpg","set_no.png","help.png"};
        bookmark.setGraphic(new ImageView(new Image(ResourceLoader.getImage(select[index]))));
        download.setGraphic(new ImageView(new Image(ResourceLoader.getImage(select[index+1]))));
        about.setGraphic(new ImageView(new Image(ResourceLoader.getImage(select[index+3]))));
        set.setGraphic(new ImageView(new Image(ResourceLoader.getImage(select[index+2]))));
    }
}
