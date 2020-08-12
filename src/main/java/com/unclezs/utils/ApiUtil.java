package com.unclezs.utils;

import cn.hutool.core.thread.ThreadUtil;
import com.unclezs.gui.utils.DataManager;

import java.io.IOException;

/**
 * 用于与服务端简单交互
 *
 * @author uncle
 * @date 2020/5/24 19:32
 */
public class ApiUtil {
    private static final String BASE_URL = "http://novel.unclezs.com/novel-server/";
    /**
     * 用户统计接口
     */
    private static final String USER_COUNT_API = BASE_URL + "pcuser/addUser/";

    /**
     * 用户量增加
     */
    public static void userAdd() {
        ThreadUtil.execute(() -> {
            try {
                RequestUtil.get(USER_COUNT_API + DataManager.application.getAppId());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
