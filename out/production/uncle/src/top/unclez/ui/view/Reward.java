package top.unclez.ui.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import top.unclez.bean.GlobalValue;
import top.unclez.ui.util.ResourceLoader;

import java.awt.*;
import java.net.URI;

public class Reward extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Pane pane  = FXMLLoader.load(ResourceLoader.getFXMLResource("reward.fxml"));
        Scene scene=new Scene(pane);
        stage.setTitle("打赏Uncle");
        stage.setScene(scene);
        stage.getIcons().add(new Image("/image/reward.jpg"));
        stage.setAlwaysOnTop(true);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(GlobalValue.stage);
        stage.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
