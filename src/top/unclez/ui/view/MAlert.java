package top.unclez.ui.view.stage;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import top.unclez.bean.GlobalValue;

public class MAlert extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Pane pane=new Pane();
        Text text=new Text("\r\n\r\n\r\n             保存成功");
        text.setFont(new Font(20));
        text.setTextAlignment(TextAlignment.CENTER);
        pane.getChildren().addAll(text);
        Scene scene=new Scene(pane,250,150);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.setTitle("提示");
        primaryStage.initOwner(GlobalValue.stage);
        primaryStage.initStyle(StageStyle.UTILITY);
        primaryStage.show();
    }
}
