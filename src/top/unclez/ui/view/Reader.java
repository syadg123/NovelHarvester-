package top.unclez.ui.view.stage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import top.unclez.ui.util.ResourceLoader;
import top.unclez.ui.util.StageUtil;

public class Reader extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage){
        stage.setTitle("Uncle小说阅读器");
        stage.setAlwaysOnTop(true);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ResourceLoader.getFXMLResource("reader.fxml"));
        Pane mpane= null;
        try {
            mpane = loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(mpane);
        StageUtil.showStage(stage,scene);
    }
}
