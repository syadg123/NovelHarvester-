package top.unclez.ui.view.stage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import top.unclez.ui.util.ResourceLoader;
import top.unclez.ui.util.StageUtil;

import java.io.IOException;

public class BookMark extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("阅读历史");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ResourceLoader.getFXMLResource("bookmark.fxml"));
        Pane pane= null;
        try {
            pane = loader.load();
        }catch (IOException e) {
            e.printStackTrace();
        }
        StageUtil.showStage(stage,new Scene(pane));
    }
}
