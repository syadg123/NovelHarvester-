package top.unclez.ui.model;

import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import top.unclez.bean.GlobalValue;
import top.unclez.ui.view.MAlert;
import top.unclez.utils.ObjUtil;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class SetControl implements Initializable {
    @FXML
    TextField cheader;
    @FXML
    TextField ctail;
    @FXML
    TextField dtail;
    @FXML
    TextField dheader;
    @FXML
    JFXButton save;
    @FXML
    TextField cookie;
    @FXML
    JFXRadioButton t2s;
    @FXML
    JFXRadioButton ncr2cn;
    @FXML
    JFXRadioButton sort;
    @FXML
    JFXRadioButton filter;
    @FXML
    JFXTextField location;
    @FXML
    JFXButton cloction;
    @FXML
    JFXComboBox delay;
    @FXML
    JFXRadioButton listmark;
    @FXML
    JFXComboBox pagenum;
    ObservableList<String> delaylist = FXCollections.observableArrayList();
    ObservableList<String> pagenumlist = FXCollections.observableArrayList();
    @Override
    public void initialize(URL locations, ResourceBundle resources) {
        init();
        cloction.setOnMouseClicked(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File file = directoryChooser.showDialog(GlobalValue.stage);
            if (file != null) {
                String path = file.getPath();
                location.setText(path);
                GlobalValue.config.setPath(path);
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        location.setText(GlobalValue.config.getPath());
                    }
                });
            }
        }).start();
    }

    public void save(ActionEvent event) {
        save.setOnMouseClicked(e -> {
            GlobalValue.config.setCheader(cheader.getText());
            GlobalValue.config.setCtail(ctail.getText());
            GlobalValue.config.setDtail(dtail.getText());
            GlobalValue.config.setDheader(dheader.getText());
            GlobalValue.config.setCookie(cookie.getText());
            GlobalValue.config.setT2S(t2s.isSelected());
            GlobalValue.config.setNCR2CN(ncr2cn.isSelected());
            GlobalValue.config.setFilter(filter.isSelected());
            GlobalValue.config.setPage(Integer.parseInt(pagenum.getValue().toString()));
            GlobalValue.config.setDelay(Integer.parseInt(delay.getValue().toString()));
            GlobalValue.config.setSort(sort.isSelected());
            GlobalValue.config.setListmark(listmark.isSelected());
            ObjUtil.saveSetConfig();
            MAlert alert=new MAlert();
            alert.start(new Stage());
        });
    }
    private void init(){
        for (int i = 0; i < 16; i++) {
            delaylist.add(i * 100 + "");
            pagenumlist.add((i + 1) * 50 + "");
        }
        pagenum.setItems(pagenumlist);
        pagenum.setValue(GlobalValue.config.getPage()+"");
        delay.setItems(delaylist);
        delay.setValue(GlobalValue.config.getDelay()+"");
        listmark.setSelected(GlobalValue.config.isListmark());
        sort.setSelected(GlobalValue.config.isSort());
        save.setGraphic(new ImageView("/image/save.png"));
        cloction.setGraphic(new ImageView("/image/file.png"));
        filter.setSelected(GlobalValue.config.isFilter());
        t2s.setSelected(GlobalValue.config.isT2S());
        ncr2cn.setSelected(GlobalValue.config.isNCR2CN());
    }
}
