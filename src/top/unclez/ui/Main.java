package top.unclez.ui;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import top.unclez.bean.GlobalValue;

import java.io.IOException;

// TODO: 2018/12/11
/*
  未完成：记录字体背景色，书架删除
 */
public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        GlobalValue.stage = primaryStage;
        primaryStage.setTitle("Uncle小说下载器");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("main.fxml"));
        Pane pane=loader.load();
        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("image/title.jpg"));
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
        primaryStage.show();
    }
}
