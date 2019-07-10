package top.unclez.ui.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import top.unclez.ui.util.ResourceLoader;
import top.unclez.ui.util.StageUtil;

import java.io.IOException;


public class BookMark extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("GRIDPANE 自适应");
        Pane pane=FXMLLoader.load(ResourceLoader.getFXMLResource("bookmark2.fxml"));
        StageUtil.showStage(stage,new Scene(pane));
    }
}
