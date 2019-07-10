package com.unclezs.UI.App;
/*
 *@author unclezs.com
 *@date 2019.06.22 14:48
 */

import com.unclezs.UI.Utils.DataManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;


public class Reader extends Application{

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage readerStage) throws IOException {
        DataManager.readerStage=readerStage;
        readerStage.getIcons().add(new Image("/images/阅读页/阅读.jpg"));
        readerStage.setMinHeight(600);
        readerStage.setMinWidth(500);
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(Main.class.getResource("/fxml/reader.fxml"));
        Pane root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/css/reader.css").toExternalForm());
        readerStage.setScene(scene);
        readerStage.show();
    }

}
