package top.unclez.utils;

import top.unclez.bean.DownloadConfig;
import top.unclez.bean.GlobalValue;
import top.unclez.bean.ReaderConfig;

import java.io.*;

public class ObjUtil {
    /**
     * 保存配置文件
     * @param wobj 要保持的对象
     * @param name 保存成什么名字
     */
    public static void saveConfig(Object wobj,String name) {
        try {
            name="conf/"+name;
            new File("conf").mkdirs();
            ObjectOutputStream out=new ObjectOutputStream(new FileOutputStream(name));
            out.writeObject(wobj);
            out.flush();
            out.close();
            System.out.println("write object success! "+name);
        } catch (Exception e) {
            System.out.println("read object failed--"+name);
            e.printStackTrace();
        }
    }

    /**
     * 读取配置文件对象
     * @param name 对象的名字
     * @return 读取到的对象
     */
    public static Object loadConfig(String name){
        name="conf/"+name;
        File file =new File(name);
        FileInputStream in;
        if (!checkFileExist(name)) {
            return null;
        }
        try {
            in = new FileInputStream(file);
            ObjectInputStream oin=new ObjectInputStream(in);
            Object object=oin.readObject();
            oin.close();
            System.out.println("read object success!");
            return object;
        } catch (Exception e) {
            System.out.println("read object failed");
            return null;
        }
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
     * 保存阅读器配置
     * @param name
     */
    public static void saveReaderConfig(String name){
        saveConfig(GlobalValue.readerConfig,name);
    }

    /**
     * 加载阅读器配置
     * @param name
     */
    public static void loadReaderConfig(String name){
        Object obj=loadConfig(name);
        if(obj!=null){
            GlobalValue.readerConfig=(ReaderConfig) obj;
        }
    }
    public static void delReaderConfig(String name){
        new File("conf/"+name).delete();
    }
    /**
     * 保存设置
     */
    public static void saveSetConfig(){
        saveConfig(GlobalValue.config,"set.dat");
    }

    /**
     * 加载设置
     */
    public static void loadSetConfig(){
        Object obj=loadConfig("set.dat");
        if(obj!=null){
            GlobalValue.config=(DownloadConfig)obj;
        }
    }
}
