package top.unclez.downloader;

import top.unclez.utils.Utils;
import top.unclez.bean.DownloadConfig;
import top.unclez.bean.GlobalValue;
import top.unclez.spider.MainSpider;

import java.io.*;
import java.util.List;
import java.util.concurrent.Callable;


public class CallDownloader implements Callable<String>{
	public int st;
	public int end;
	List<String> urls;
	private DownloadConfig config;
	public CallDownloader(List<String> urls, int st, int end,
                          DownloadConfig config) {
		super();
		this.st = st;
		this.end = end;
		this.urls=urls;
		this.config=config;
	}
	@Override
	public String call() throws Exception {
			try(PrintWriter out=new PrintWriter(
					new OutputStreamWriter(
							new FileOutputStream(new File(config.getPath()+"/block/"+st+"-"+end+".txt")),config.getCharset()));){
				StringBuffer content;
				for(int i=st;i<end;i++) {
					content=new StringBuffer();
					for (int k = 0; k < 10; k++) {
						try {
							content.append(MainSpider.getContent(urls.get(i), config.getCharset(), config.getRule()));
							GlobalValue.downloaded.add("finished");
							break;
						} catch (Exception e) {
							if (k == 9) {
								System.out.println("下载失败" + urls.get(i));
								GlobalValue.failed.add(urls.get(i));
							}
							Thread.sleep(config.getDelay());
						}
					}
					if (config.isNCR2CN()) {
						String tmp=Utils.NCR2Chinese(content.toString());
						content=new StringBuffer();
						content.append(tmp);
					}
					if (config.isT2S()) {
						String tmp=Utils.traditional2Simple(content.toString());
						content=new StringBuffer();
						content.append(tmp);
					}
					String name = GlobalValue.data.get(urls.get(i));
					out.println(name);
					out.println(content.toString());
					out.flush();
					System.out.println((GlobalValue.downloaded.size() + 1) + "/" + urls.size() + " --- " + name + "--下载完成");
					Thread.sleep(config.getDelay());
				}
			} catch (Exception e) {
//				e.printStackTrace();
			}
		return "uncle";
	}
}
