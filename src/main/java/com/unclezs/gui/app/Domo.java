package com.unclezs.gui.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Domo extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: rgba(255, 255, 255, 1);-fx-effect: dropshadow(gaussian, black, 50, 0, 0, 0);-fx-background-insets: 50 !important;");
        root.getChildren().add(new StackPane());
        //设置窗体面板和大小
        Scene scene = new Scene(root, 400, 400);
        scene.setFill(Color.TRANSPARENT);
        //设置窗体样式
//        primaryStage.initStyle(StageStyle.TRANSPARENT);
        //设置窗口标题
        primaryStage.setTitle("Demo From");
        primaryStage.setScene(scene);
        primaryStage.getScene().getStylesheets().add("CSS file path");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
