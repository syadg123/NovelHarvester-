package top.unclez.ui.view;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import top.unclez.bean.GlobalValue;
import top.unclez.ui.Main;

import java.io.IOException;

public class Reader extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage){
        stage.setTitle("Uncle小说阅读器");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Reader.class.getResource("reader.fxml"));
        Pane pane= null;
        try {
            pane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.getIcons().add(new Image("image/title.jpg"));
        stage.setResizable(false);
        stage.show();
    }
}
