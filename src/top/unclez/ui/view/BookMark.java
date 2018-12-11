package top.unclez.ui.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class BookMark extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("阅读历史");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(BookMark.class.getResource("bookmark.fxml"));
        Pane pane= null;
        try {
            pane = loader.load();
        }catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.getIcons().add(new Image("image/title.jpg"));
        stage.setResizable(false);
        stage.show();
    }
}
