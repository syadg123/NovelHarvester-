package top.unclez.Utils;

import top.unclez.bean.GlobalValue;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class FileUtil {
    static String mpath = "bookRead/";

    static {
        new File("bookRead/").mkdirs();
    }

    /**
     * 打开一本书，把书籍的url解析出来，添加到全局变量data和taskurl中
     *
     * @return 书籍的阅读位置
     */
    public static void openBook() {
        String path = mpath + GlobalValue.config.getNovelname() + ".index";
        GlobalValue.config.setPath(getLoaction());
        if (!new File(path).exists()) {
            System.out.println("文件不存在！" + path);
            return;
        }
        try {
            BufferedReader buf = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
            String line;
            GlobalValue.data = new LinkedHashMap<>();
            GlobalValue.taskurl = new ArrayList<>();
            while ((line = buf.readLine()) != null) {
                GlobalValue.data.put(line.split("---")[0], line.split("---")[1]);
                GlobalValue.taskurl.add(line.split("---")[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

    /**
     * 加载对应书配置
     *
     * @return 配置的map集合, 文件不存在则返回null
     */
    public static Map<String, String> openConfig() {
        Map<String, String> config = new HashMap<>();
        String path = mpath + GlobalValue.config.getNovelname() + ".config";
        if (!checkFileExist(path))
            return null;
        try {
            BufferedReader buf = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
            String line;
            while ((line = buf.readLine()) != null) {
                String[] conf = line.split(":");
                config.put(conf[0], conf[1]);
                System.out.println(conf[0] + ":" + conf[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return config;
    }

    /**
     * 获取路径，如果全局路径不存在，就程序所在路径
     *
     * @return 程序路径
     */
    public static String getLoaction() {
        if (GlobalValue.config != null) {
            return GlobalValue.config.getPath();
        }
        String path = System.getProperty("java.class.path");
        int firstIndex = path.lastIndexOf(System.getProperty("path.separator")) + 1;
        int lastIndex = path.lastIndexOf(File.separator) + 1;
        path = path.substring(firstIndex, lastIndex);
        System.out.println(path);
        return path;
    }

    /**
     * 判断文件是否存在
     *
     * @param path 文件路径
     * @return 存在返回true
     */
    public static boolean checkFileExist(String path) {
        if (new File(path).exists())
            return true;
        return false;
    }

    /**
     * 改变保存路径
     *
     * @return
     */
    public static void changePath(String path) {
        try {
            BufferedWriter buf = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("user.txt")));
            buf.write(path);
            buf.flush();
            buf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取保存路径
     *
     * @return
     */
    public static String getPath() {
        String line = "F:/小说下载目录/";
        if (!checkFileExist("user.txt")) {
            return line;
        }
        try {
            BufferedReader buf = new BufferedReader(new InputStreamReader(new FileInputStream("user.txt")));
            line = buf.readLine();
            buf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return line;
    }
}
