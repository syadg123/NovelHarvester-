package top.unclez.downloader;

import top.unclez.Utils.UrlFileter;
import top.unclez.Utils.Utils;
import top.unclez.bean.GlobalValue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class Downloder {
	private List<String> list;
	private DownloadConfig config;

	public void setList(List<String> list) {
		this.list = list;
	}

	public void setConfig(DownloadConfig config) {
		this.config = config;
	}

	public Downloder(List<String> list, DownloadConfig config) {
		this.list = list;
		this.config = config;
	}
	public void start() {
		File f=new File(config.getPath()+"/block");
		f.delete();
		f.mkdirs();
		int Threadnum=(int) Math.ceil(list.size()*1.0/config.getPage());
		ExecutorService service=Executors.newFixedThreadPool(Threadnum);
		GlobalValue.service=service;//管理线程
		int st = 0;
		int end = 0;
		List<Future<String>> Task = new ArrayList<>();
		for (int i = 0; i < Threadnum; i++) {
			if (i == Threadnum-1) {
				st = end;
				end = list.size();
			} else {
				st = end;
				end += config.getPage();
			}
			Task.add(service.submit(new CallDownloader(list,st, end,config)));
		}
		service.shutdown();
		for (Future<String> future : Task) {
			try {
				future.get();
			} catch (Exception e){
				System.out.println("线程异常终止");
			}
		}
		GlobalValue.isDownloaded=true;
		Utils.mergeFiles(config.getNovelname(),config.getPath(),true,config.getCharset());
	}
}
