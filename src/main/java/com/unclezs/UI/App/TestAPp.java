package com.unclezs.UI.App;

/*
 *@author unclezs.com
 *@date 2019.07.06 10:06
 */

//import com.unclezs.UI.Media.Media;
//import com.unclezs.UI.Media.MediaPlayer;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXListView;
import javafx.application.Application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;


import javafx.scene.control.TextField;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;


public class TestAPp extends Application {
    int st=0;
    boolean startSelected=false;
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws URISyntaxException, MalformedURLException {
        JFXListView<JFXCheckBox> b=new JFXListView<>();
        ObservableList<JFXCheckBox> list=FXCollections.observableArrayList();
        b.setItems(list);
        for (int i = 0; i < 20; i++) {
            list.add(new JFXCheckBox(i+""));
        }

        b.setOnMouseClicked(e->{
            if(e.isShiftDown()&&!startSelected){
                startSelected=true;
                st=b.getSelectionModel().getSelectedIndex();
                list.get(st).setSelected(!list.get(st).isSelected());
                return;
            }
            if(e.isShiftDown()&startSelected){
                int end = b.getSelectionModel().getSelectedIndex();
                for (int i = st+1; i <= end; i++) {
                    list.get(i).setSelected(!list.get(i).isSelected());
                }
                startSelected=false;
            }
        });
//        b.setOnKeyReleased(e->{
//            if(!e.isShiftDown()){
//                System.out.println("停止");
//            }
//        });
        Scene scene=new Scene(b,200,666);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}

