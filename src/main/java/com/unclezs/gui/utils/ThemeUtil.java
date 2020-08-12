package com.unclezs.gui.utils;

import cn.hutool.core.lang.Dict;
import cn.hutool.extra.template.Template;
import com.unclezs.utils.FileUtil;
import com.unclezs.utils.TemplateUtil;
import javafx.scene.Scene;

import java.io.File;

/**
 * @author uncle
 * @date 2020/5/16 17:53
 */
public class ThemeUtil {
    /**
     * 切换主题
     */
    public static void setCss(Dict dict, Scene scene, String templates, String out) {
        Template template = TemplateUtil.getTemplate(templates);
        File css = FileUtil.currentDirFile(out);
        template.render(dict, css);
        if (scene.getStylesheets().size() == 1) {
            scene.getStylesheets().add(1, css.toURI().toString());
        } else {
            scene.getStylesheets().set(1, css.toURI().toString());
        }
    }
}
