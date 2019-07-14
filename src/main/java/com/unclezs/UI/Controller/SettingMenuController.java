package com.unclezs.UI.Controller;

import com.unclezs.UI.Utils.AlertUtil;
import com.unclezs.UI.Utils.DataManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

/*
 *设置页面
 *@author unclezs.com
 *@date 2019.06.22 13:30
 */
public class SettingMenuController implements Initializable {
    @FXML
    Label github, help, setting, about, safe,update;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initIcon();
        initEventHandler();
    }

    //图标初始化
    void initIcon() {
        github.setGraphic(new ImageView("images/设置页/github.jpg"));
        setting.setGraphic(new ImageView("images/设置页/头设置.jpg"));
        safe.setGraphic(new ImageView("images/设置页/免责.jpg"));
        about.setGraphic(new ImageView("images/设置页/关于.jpg"));
        help.setGraphic(new ImageView("images/设置页/打赏.jpg"));
        update.setGraphic(new ImageView("images/菜单页/更新.jpg"));
        changeBackColor(github, setting, safe, about, help,update);
    }

    //事件初始化
    void initEventHandler() {
        //git源码
        github.setOnMouseClicked(e -> {
            try {
                Desktop.getDesktop().browse(new URI("https://github.com/unclezs/NovelHarvester"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        //设置
        setting.setOnMouseClicked(e -> {
            Stage stage=getStage();
            try {
                Pane pane = FXMLLoader.load(getClass().getResource("/fxml/setting.fxml"));
                Scene scene=new Scene(pane);
                stage.setTitle("设置");
                stage.setScene(scene);
                stage.show();
                stage.setOnCloseRequest(event->{//关闭时保存设置
                    SettingController.updateSetting();
                });
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        });
        //帮助
        help.setOnMouseClicked(e -> {
            Stage stage=getStage();
            try {
                Pane pane = FXMLLoader.load(getClass().getResource("/fxml/reward.fxml"));
                Scene scene=new Scene(pane);
                stage.setTitle("打赏作者");
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        //赞赏
        //关于
        about.setOnMouseClicked(e -> {
            AlertUtil.getAlert("关于", "如果有问题欢迎反馈给我😄\r\n\r\n版本号：V3.0\r\n开发者QQ：1585503310\r\n开发者邮箱：1585503310@qq.com").show();
        });
        //免责声明
        safe.setOnMouseClicked(e -> {
            AlertUtil.getAlert("免责声明", "软件仅供技术交流，请勿用于商业及非法用途，\r\n如产生法律纠纷与本人无关，如有侵权请联系我删除.").show();
        });
        update.setOnMouseClicked(e->{
            try {
                Desktop.getDesktop().browse(new URI("https://www.lanzous.com/b833477"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    //改变菜单鼠标移入移出背景色
    void changeBackColor(Label... label) {
        for (Label l : label) {
            l.setOnMouseMoved(e -> {
                l.setStyle("-fx-background-color: rgb(189,189,189)");
            });
            l.setOnMouseExited(e -> {
                l.setStyle("-fx-background-color: #F0F0F0");
            });
        }

    }

    //获取默认舞台
    private Stage getStage(){
        Stage stage=new Stage();
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(DataManager.mainStage);
        stage.getIcons().add(new Image("/images/图标/圆角图标.png"));
        return stage;
    }
}
