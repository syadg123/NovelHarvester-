package top.unclez.ui.view;

import com.jfoenix.controls.JFXButton;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import top.unclez.bean.GlobalValue;
import top.unclez.ui.util.ResourceLoader;

import java.io.IOException;

public class MAlert extends Application {
    private String text="保存成功";
    private String title="提示";
    private String messageImg="/image/success.jpg";
    private boolean isOK=false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        GlobalValue.alert=primaryStage;
        Pane pane=null;
        try {
           pane=FXMLLoader.load(ResourceLoader.getFXMLResource("alert.fxml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Label message = (Label) pane.getChildren().get(2);
        message.setText(text);
        ImageView imageView=(ImageView) pane.getChildren().get(3);
        imageView.setImage(new Image(messageImg));
        JFXButton btnOk=(JFXButton) pane.getChildren().get(0);
        JFXButton btnNo=(JFXButton) pane.getChildren().get(1);
        btnNo.setOnMouseClicked(event -> {
            isOK=false;
            primaryStage.close();
        });
        btnOk.setOnMouseClicked(event -> {
            isOK=true;
            primaryStage.close();
        });
        pane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode()== KeyCode.ENTER){
                    isOK=true;
                    primaryStage.close();
                }
                if(event.getCode()==KeyCode.ESCAPE){
                    primaryStage.close();
                }
            }
        });
        Scene scene=new Scene(pane,250,150);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.setTitle(title);
        primaryStage.initStyle(StageStyle.UTILITY);
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.initOwner(GlobalValue.stage);
        primaryStage.showAndWait();

    }
    public  String getText() {
        return text;
    }

    public  void setText(String text) {
        this.text = text;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public boolean isOK() {
        return isOK;
    }

    public void setMessageImg(String messageImg) {
        this.messageImg = messageImg;
    }
}
