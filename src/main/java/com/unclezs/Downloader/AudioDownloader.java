package com.unclezs.Downloader;

import com.unclezs.Adapter.DownloadAdapter;
import com.unclezs.Crawl.AudioNovelSpider;
import com.unclezs.Model.AudioBook;
import com.unclezs.Model.AudioChapter;
import com.unclezs.Model.DownloadConfig;
import com.unclezs.Utils.FileUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/*
 *音频下载器
 *@author unclezs.com
 *@date 2019.07.08 20:23
 */
public class AudioDownloader implements DownloadAdapter {
    private DownloadConfig config;//下载配置
    private AudioBook book;
    private List<Integer> overNum;//完成数量
    private List<String> taskUrl;
    private ExecutorService service;
    private List<Boolean> isShutdown=new ArrayList<>();
    public AudioDownloader(DownloadConfig config, AudioBook book) {
        this.config = config;
        this.book = book;
        this.overNum= Collections.synchronizedList(new ArrayList<>(book.getChapters().size()));
        //获取任务列表
        this.taskUrl=new ArrayList<>(book.getChapters().size());
        for (AudioChapter chapter:book.getChapters()){
            this.taskUrl.add(chapter.getUrl());
        }
    }

    @Override
    public void start() {
        //保存封面
        new Thread(()->{
            String img = FileUtil.uploadFile("./image/audio/" + UUID.randomUUID() + ".jpg", book.getImageUrl());
            book.setImageUrl(img);
        });
        //更新路径
        config.setPath(config.getPath()+book.getTitle());
        //计算需要线程数量
        int threadNum=(int) Math.ceil(book.getChapters().size()*1.0/config.getPerThreadDownNum());
        service = Executors.newFixedThreadPool(threadNum % 50);//最大50开辟50个线程的线程池
        //任务分发
        int st;
        int end = 0;
        List<Future<String>> task = new ArrayList<>();//任务监控
        for (int i = 0; i < threadNum; i++) {
            isShutdown.add(true);//标志正在运行
            if (i == threadNum-1) {//最后一次不足每个线程下载章节数量，则全部下载
                st = end;
                end = book.getChapters().size();
            } else {//每次增加配置数量
                st = end;
                end += config.getPerThreadDownNum();
            }
            final int taskId=i;
            final int sIndex=st;//开始下标
            final int eIndex=end;//结束下标
            task.add(service.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    AudioNovelSpider spider=new AudioNovelSpider();
                    for (int j = sIndex; j < eIndex; j++) {
                        if(!isShutdown.get(taskId)){
                            return null;//监听中途停止
                        }
                        String src = spider.getSrc(taskUrl.get(j));
                        String path = getDownloadPath(j, src);
                        try {
                            File file = new File(path);
                            if(!file.exists()){
                                file.getParentFile().mkdirs();
                                file.createNewFile();
                            }
                            URL url = new URL(src);
                            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                            connection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36");
                            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(path));
                            BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
                            byte[] buffer = new byte[1024*1024];
                            int count = 0;
                            while ((count = in.read(buffer)) > 0) {
                                out.write(buffer, 0, count);
                            }
                            out.close();/*后面三行为关闭输入输出流以及网络资源的固定格式*/
                            in.close();
                            connection.disconnect();
                        } catch (Exception e) {
                            System.out.println(e.getCause());
                            System.out.println("下载失败"+src);
                        }
                        overNum.add(j);
                    }
                    return null;
                }
            }));
        }
        for (Future<String> future : task) {
            try {
                future.get();//阻塞等待完成
            } catch (Exception e){
                System.out.println("线程异常终止");//下载失败
            }
        }
        service.shutdown();//销毁线程池
    }

    @Override
    public void stop() {
        //全部标志为停止
        for (int i = 0; i <isShutdown.size() ; i++) {
            isShutdown.set(i,false);
        }
        service.shutdown();
        service.shutdownNow();
    }

    @Override
    public int getOverNum() {
        return overNum.size();
    }

    @Override
    public int getMaxNum() {
        return taskUrl.size();
    }

    @Override
    public String getImgPath() {
        return book.getImageUrl();
    }

    @Override
    public String getTitle() {
        return book.getTitle();
    }

    @Override
    public String getPath() {
        return config.getPath();
    }

    @Override
    public String getType() {
        return "音频文件";
    }
    //获取下载路径及文件名字
    private String getDownloadPath(int index,String src){
        String suffix=".mp3";
        if(src.contains("m4a")){
            suffix=".m4a";
        }
        return config.getPath()+"/"+book.getChapters().get(index).getTitle()+suffix;
    }
}
