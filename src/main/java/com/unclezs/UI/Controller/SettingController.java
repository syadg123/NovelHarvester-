package com.unclezs.UI.Controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXToggleButton;
import com.unclezs.Mapper.SettingMapper;
import com.unclezs.Model.DownloadConfig;
import com.unclezs.UI.Utils.DataManager;
import com.unclezs.UI.Utils.ToastUtil;
import com.unclezs.Utils.MybatisUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.SqlSession;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/*
 *设置控制器
 *@author unclezs.com
 *@date 2019.07.07 11:52
 */
public class SettingController implements Initializable {
    @FXML
    JFXToggleButton merge;
    @FXML
    JFXComboBox<Integer> chapterNum, delay;
    @FXML
    Label pathLabel, changePath;
    private static DownloadConfig config;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initData();
        initEventHandler();
    }

    void initData() {//初始化数据
        chapterNum.getItems().addAll(50, 100, 200, 400, 800, 1000, 1600, 3200, 6400);
        delay.getItems().addAll(0, 50, 500, 1000, 2000, 3000, 5000, 8000, 10000);
        SettingMapper mapper = MybatisUtils.getMapper(SettingMapper.class);
        config = mapper.querySetting();
        MybatisUtils.getCurrentSqlSession().close();
        merge.setSelected(config.isMergeFile());
        merge.setDisableVisualFocus(true);//禁用焦点过渡
        chapterNum.setValue(config.getPerThreadDownNum());
        delay.setValue(config.getSleepTime());
        pathLabel.setText(config.getPath());
    }

    //初始化事件监听
    void initEventHandler() {
        //值改变监听
        merge.selectedProperty().addListener(e -> {
            config.setMergeFile(merge.isScaleShape());
        });
        chapterNum.valueProperty().addListener(e -> {
            config.setPerThreadDownNum(chapterNum.getValue());
        });
        delay.valueProperty().addListener(e -> {
            config.setSleepTime(delay.getValue());
        });
        changePath.setOnMouseClicked(e -> {
            //文件选择
            DirectoryChooser chooser = new DirectoryChooser();
            File dir = new File(config.getPath());
            if (dir.exists())
                chooser.setInitialDirectory(dir);
            chooser.setTitle("选择下载位置");
            File file = chooser.showDialog(DataManager.mainStage);
            //防空
            if (file == null || !file.exists()) {
                return;
            }
            //更新
            String path = file.getAbsolutePath()+"\\";
            pathLabel.setText(path);
            config.setPath(path);
        });
    }

    //保存更新设置
    public static void updateSetting() {
        new Thread(() -> {
            SettingMapper mapper = MybatisUtils.getMapper(SettingMapper.class);
            mapper.updateSetting(config);
            MybatisUtils.getCurrentSqlSession().close();
        }).start();
    }
}
