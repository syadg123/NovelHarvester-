package com.unclezs.gui.utils;

import javafx.application.Platform;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * 托盘工具
 *
 * @author uncle
 * @date 2019.07.30.
 */
@Slf4j
@UtilityClass
public class TrayUtil {
    private static final SystemTray TRAY;
    private static TrayIcon trayIcon;

    public void init() {
        //init
    }

    static {
        TRAY = SystemTray.getSystemTray();
        //设置右键菜单
        try {
            BufferedImage image = ImageIO.read(TrayUtil.class.getResourceAsStream("/images/logo/tray.png"));
            trayIcon = new TrayIcon(image, "Uncle小说", null);
            trayIcon.setImageAutoSize(true);
            trayIcon.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        Platform.runLater(() -> DataManager.currentStage.show());
                    }
                }
            });
//            MenuItem item = new MenuItem("exit");
//            item.addActionListener(e -> {
//            });
//            PopupMenu popupMenu = new PopupMenu();
//            popupMenu.add(item);
//            trayIcon.setPopupMenu(popupMenu);
            TRAY.add(trayIcon);
        } catch (Exception e) {
            log.error("托盘初始化失败", e);
            e.printStackTrace();
        }
    }

    public static void tray() {
        Platform.setImplicitExit(false);
        DataManager.currentStage.setIconified(false);
        DataManager.currentStage.hide();
    }
}