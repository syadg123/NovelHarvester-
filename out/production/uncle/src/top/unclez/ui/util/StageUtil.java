package top.unclez.ui.util;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import top.unclez.bean.GlobalValue;

public class StageUtil {
    public static void showStage(Stage primaryStage,Scene scene){
        primaryStage.setScene(scene);
        primaryStage.setAlwaysOnTop(true);
        if(primaryStage.equals(GlobalValue.stage)){
            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    System.exit(0);
                }
            });
        }
        primaryStage.show();
    }
}
