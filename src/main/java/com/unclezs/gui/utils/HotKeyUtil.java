package com.unclezs.gui.utils;

import cn.hutool.core.exceptions.ExceptionUtil;
import javafx.application.Platform;
import lombok.extern.slf4j.Slf4j;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.util.logging.LogManager;

/**
 * 全局唤醒热键Alt+U
 * https://github.com/kwhat/jnativehook
 * @author uncle
 * @date 2019.07.31
 */
@Slf4j
public class HotKeyUtil {
    /**
     * alt键按下
     */
    private static boolean ALT_PRESSED = false;
    /**
     * u键按下
     */
    private static boolean U_PRESSED = false;
    /**
     * 是否已经响应
     */
    private static boolean IS_RESPONSE = false;

    public static void bindListener() {
        LogManager.getLogManager().reset();
        try {
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(new NativeKeyListener() {
                @Override
                public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {
                }

                @Override
                public void nativeKeyPressed(NativeKeyEvent e) {
                    switch (e.getKeyCode()) {
                        case 22:
                            U_PRESSED = true;
                            break;
                        case 56:
                            ALT_PRESSED = true;
                            break;
                        default:
                            break;
                    }
                    if (ALT_PRESSED && U_PRESSED && !IS_RESPONSE) {//arl+U组合键一次按下只响应一次
                        Platform.runLater(() -> {
                            try {
                                if (DataManager.currentStage.isShowing()) {
                                    TrayUtil.tray();
                                } else {
                                    DataManager.currentStage.show();
                                }
                            } catch (Exception ignored) {
                            }
                        });
                        IS_RESPONSE = true;
                    }
                }

                @Override
                public void nativeKeyReleased(NativeKeyEvent e) {
                    switch (e.getKeyCode()) {
                        case 22:
                            U_PRESSED = false;
                            break;
                        case 56:
                            ALT_PRESSED = false;
                            break;
                        default:
                            break;
                    }
                    IS_RESPONSE = false;
                }
            });
        } catch (NativeHookException e) {
            log.error("全局热键注册失败:{}", ExceptionUtil.stacktraceToString(e));
        }
    }
}