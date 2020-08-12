package com.unclezs.gui.utils;

import javafx.application.Platform;

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
public class TrayUtil {
    private static SystemTray tray;
    private static TrayIcon trayIcon;

    static {
        tray = SystemTray.getSystemTray();
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
                        Platform.runLater(() -> {
                            Platform.setImplicitExit(true);
                            DataManager.currentStage.show();
                            tray.remove(trayIcon);
                        });
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void tray() throws Exception {
        Platform.setImplicitExit(false);
        DataManager.currentStage.hide();
        DataManager.currentStage.setIconified(false);
        tray.add(trayIcon);
    }
}