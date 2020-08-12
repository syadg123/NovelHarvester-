package com.unclezs.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;

import java.util.Map;

/**
 * 模板工具
 *
 * @author uncle
 * @date 2020/2/26 16:25
 */
public class TemplateUtil {
    private static TemplateEngine engine = cn.hutool.extra.template.TemplateUtil.createEngine(new TemplateConfig("templates", TemplateConfig.ResourceMode.CLASSPATH));

    public static String render(Map dict, String templateStr) {
        //自动根据用户引入的模板引擎库的jar来自动选择使用的引擎
        //TemplateConfig为模板引擎的选项，可选内容有字符编码、模板路径、模板加载方式等，默认通过模板字符串渲染
        Template template = engine.getTemplate(templateStr);
        //Dict本质上为Map，此处可用Map
        return template.render(dict);
    }


    public static String renderByFile(Map dict, String templateLocation) {
        Template template = engine.getTemplate(templateLocation);
        //Dict本质上为Map，此处可用Map
        return template.render(dict);
    }

    public static Template getTemplate(String templateLocation) {
        return engine.getTemplate(templateLocation);
    }

    public static void main(String[] args) {
        Template template = getTemplate("epub/test.ftl");
        template.render(Dict.create().set("name","unclezs"), FileUtil.file("D:\\java\\NovelHarvester\\src\\main\\resources\\templates\\epub\\test.html"));
    }


}
