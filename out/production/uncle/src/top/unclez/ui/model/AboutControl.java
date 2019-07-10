package top.unclez.ui.model;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import top.unclez.ui.view.Reward;

import java.awt.*;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

public class AboutControl implements Initializable {
    @FXML
    JFXButton reward;
    @FXML
    Hyperlink source;
    @FXML
    Hyperlink blog;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //打赏
        reward.setGraphic(new ImageView("/image/reward.jpg"));
        reward.setOnMouseClicked(event -> {
            Reward reward=new Reward();
            try {
                reward.start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        //博客地址跳转
        blog.setOnMouseClicked(event -> {
            try {
                Desktop.getDesktop().browse(new URI("http://unclez.top"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        //github源码跳转
        source.setOnMouseClicked(event -> {
            try {
                Desktop.getDesktop().browse(new URI("https://github.com/1585503310/ReadnovelOnWin"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
