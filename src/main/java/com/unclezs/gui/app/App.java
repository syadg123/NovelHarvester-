package com.unclezs.gui.app;

import cn.hutool.core.thread.ThreadUtil;
import com.jfoenix.controls.JFXPopup;
import com.unclezs.gui.animation.ScaleLargeTransition;
import com.unclezs.gui.components.StageDecorator;
import com.unclezs.gui.controller.IndexController;
import com.unclezs.gui.controller.components.SettingMenuController;
import com.unclezs.gui.controller.components.ThemeController;
import com.unclezs.gui.utils.*;
import com.unclezs.utils.ApiUtil;
import com.unclezs.utils.RequestUtil;
import fxlauncher.UpdateInfo;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * 程序入口
 *
 * @author uncle
 * @date today
 */
@Slf4j
public class App extends Application {
    /**
     * 版本信息
     */
    public static UpdateInfo versionInfo;
    private ScaleLargeTransition showAnn;
    public static Stage stage;
    public static StageDecorator root;
    public static StackPane background;
    public static BorderPane contentContainer;
    private static JFXPopup settingPopup;
    private VBox themePage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() {
        ThreadUtil.execute(() -> {
            //加载字体图标
            Font.loadFont(App.class.getResourceAsStream("/font/fontawesome-webfont.ttf"), 14);
            //热键注册
            HotKeyUtil.init();
            //忽略SSL
            RequestUtil.initSSL();
        });
        //加载设置
        ApplicationUtil.initConfig();
        ToolTipUtil.init();
        TrayUtil.init();
    }

    @Override
    public void start(Stage mainStage) throws IOException {
        stage = mainStage;
        DataManager.currentStage = mainStage;
        contentContainer = ResourceUtil.loadFxml(IndexController.class);
        root = new StageDecorator(mainStage, contentContainer);
        //设置菜单点击监听
        root.setOnSettingAction(this::showSetting);
        //主题点击
        root.setOnChangeThemeAction(e -> this.showThemeSetting());
        //关闭监听
        root.setOnCloseButtonAction(this::closeHandler);
        root.setTitle("Uncle小说");
        StackPane content = new StackPane();
        content.setStyle("-fx-effect: dropshadow(gaussian, #000,10, 0, 0, 0) !important;-fx-background-insets: 5");
        background = new StackPane();
        content.getChildren().addAll(background, root);
        Scene scene = new Scene(content);
        scene.getStylesheets().add(ResourceUtil.loadCss("/css/global.css"));
        scene.setFill(Color.TRANSPARENT);
        mainStage.setScene(scene);
        //加载主题
        themePage = ResourceUtil.loadFxml(ThemeController.class);
        mainStage.getIcons().add(new Image("/images/logo/favicon.ico"));
        root.setCustomMaximize(true);
        mainStage.setMinWidth(938);
        mainStage.setMinHeight(618);
        showAnn = new ScaleLargeTransition(content);
        showAnn.setOnFinished(e -> contentContainer.requestFocus());
        mainStage.show();
        //用户量统计
        ApiUtil.userAdd();
        contentContainer.requestFocus();
        stage.setOnShowing(e -> {
            Platform.setImplicitExit(true);
            DataManager.currentStage.setIconified(false);
            DataManager.currentStage = mainStage;
            showAnn.play();
            contentContainer.requestFocus();
        });
        //注册VM退出监听，保存数据
        Runtime.getRuntime().addShutdownHook(ThreadUtil.newThread(() -> {
            //注销热键
            HotKeyUtil.unbind();
            ApplicationUtil.storeConfig();
        }, "uncle exit store thread"));
    }

    /**
     * 显示更新内容
     */
    public static void showWhatNew() {
        Object userData = stage.getUserData();
        if (userData != null) {
            versionInfo = (UpdateInfo) userData;
            //是否更新了
            Boolean update = versionInfo.getUpdate();
            if (Boolean.TRUE.equals(update)) {
                ScrollPane updatePane = new ScrollPane();
                updatePane.getStyleClass().add("update-scroll");
                updatePane.setFitToWidth(true);
                Label content = new Label();
                content.setText(versionInfo.getWhatNew());
                updatePane.setContent(content);
                content.getStyleClass().add("what-new");
                AlertUtil.alert(String.format("%s 更新内容", versionInfo.getVersion()), updatePane);
            }
        }
    }

    /**
     * 处理退出
     */
    private void closeHandler() {
        switch (DataManager.application.getSetting().getExitHandler().get()) {
            case 0:
                AlertUtil.confirm("请选择退出操作\n可以到设置里面设置默认操作", null, "最小化到托盘", "退出程序", this::close);
                break;
            case 1:
                close(true);
                break;
            default:
                close(false);

        }
    }

    /**
     * 跟进行为关闭
     *
     * @param tray /
     */
    private void close(boolean tray) {
        if (tray) {
            try {
                TrayUtil.tray();
            } catch (Exception ex) {
                log.error("最小化到托盘失败：{}", ex.getMessage());
                close();
            }
        } else {
            close();
        }
    }

    /**
     * 程序退出 关闭UI
     */
    private void close() {
        stage.close();
        ContentUtil.destroy();
        Platform.setImplicitExit(true);
        Platform.exit();
        System.exit(0);
    }

    /**
     * 显示换肤设置页面
     */
    private void showThemeSetting() {
        AlertUtil.alert("换肤", "关闭", themePage);
    }

    /**
     * 显示设置菜单
     *
     * @param trigger /
     */
    private void showSetting(Node trigger) {
        try {
            if (settingPopup == null) {
                Pane region = ResourceUtil.loadFxml(SettingMenuController.class);
                settingPopup = new JFXPopup(NodeUtil.createBgPane(region));
            }
            settingPopup.show(trigger, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT, 0, 30);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭设置
     */
    public static void closeSetting() {
        settingPopup.hide();
    }
}
