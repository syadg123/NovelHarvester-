package top.unclez.Utils;

import com.luhuiguo.chinese.ChineseUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    /**
     * 相对路径转绝对路径
     *
     * @param base 根路径
     * @param url  相对路径
     * @return 绝对地址
     */
    public static String getAbsUrl(String base, String url) {
        if (!base.endsWith("/")) {
            base = base + "/";
        }
        String absurl = "";
        try {
            URL fromurl = new URL(base);
            absurl = new URL(fromurl, url).toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return absurl;
    }

    /**
     * &#x编码转换成汉字
     *
     * @param src 字符集
     * @return 解码后的字符集
     */
    public static String unescape(String src) {
        StringBuffer tmp = new StringBuffer();
        tmp.ensureCapacity(src.length());
        int lastPos = 0, pos = 0;
        char ch;
        src = src.replace("&#x", "%u").replace(";", "");
        while (lastPos < src.length()) {
            pos = src.indexOf("%", lastPos);
            if (pos == lastPos) {
                if (src.charAt(pos + 1) == 'u') {
                    ch = (char) Integer.parseInt(src.substring(pos + 2, pos + 6), 16);
                    tmp.append(ch);
                    lastPos = pos + 6;
                } else {
                    ch = (char) Integer.parseInt(src.substring(pos + 1, pos + 3), 16);
                    tmp.append(ch);
                    lastPos = pos + 3;
                }
            } else {

                if (pos == -1) {
                    tmp.append(src.substring(lastPos));
                    lastPos = src.length();
                } else {
                    tmp.append(src.substring(lastPos, pos));
                    lastPos = pos;
                }
            }
        }
        return tmp.toString();
    }

    /**
     * 将&#类得字符转化为汉字
     *
     * @param src 字符集&#20491;&#30007;&#20154;&#30475;
     * @return 转码后得字符集
     */
    public static String NCR2Chinese(String src) {
        Pattern pattern = Pattern.compile("&#([\\d]{2,6});");
        src = src.replace("\r\n", "&#92;&#114;&#92;&#110;");//换行符处理
        Matcher m = pattern.matcher(src);
        while (m.find()) {
            src = src.replace(m.group(0), (char) Integer.parseInt(m.group(1)) + "");
        }
        return src.replace("\\r\\n", "\r\n");
    }

    /**
     * 获取网页编码
     *
     * @param html 解码的网页源码（正则匹配<meta>标签的编码）
     * @return 编码格式
     */
    public static String getEncode(String html) {
        String code = "utf-8";
        try {
            Pattern r = Pattern.compile("charset=[\"\']{0,1}([\\w\\-]{2,8}?)[\"\']");
            Matcher m = r.matcher(html);
            while (m.find()) {
                code = m.group(1);
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return code.replace("\"", "").trim();
    }

    /**
     * 获取网页源码
     *
     * @param url     网页url地址
     * @param charset 网页编码
     * @return 网页源码
     */
    public static String getHtml(String url, String charset) {
        String content = null;
        //重试5次
        HttpRequestRetryHandler retry = new StandardHttpRequestRetryHandler(5, true);
        //请求头
        List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36"));
        headers.add(new BasicHeader("Accept-Charset", "utf-8,gbk;q=0.7,*"));
        headers.add(new BasicHeader("Accept-Language", "zh-cn,zh;q=0.5"));
        //延迟
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(10000)
                .setConnectionRequestTimeout(10000)
                .setSocketTimeout(10000)
                .build();
        try (CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultHeaders(headers)
                .setDefaultRequestConfig(config)
                .setRetryHandler(retry)
                .build()) {
            HttpGet get = new HttpGet(url);
//            System.out.println(httpClient.execute(get).getStatusLine().getStatusCode()+url);
            HttpEntity entity = httpClient.execute(get).getEntity();
            byte[] bytes = EntityUtils.toByteArray(entity);
            if (charset.contains("gb")) {
                charset = "gbk";
            }
            content = new String(bytes, charset);
//            System.out.println(content);
        } catch (Exception e) {
            System.out.println("源码抓取失败" + url);
            e.printStackTrace();
        }
        return content.trim();
    }

    /**
     * 繁体转简体
     *
     * @param src 原字符
     * @return 简体字符集
     */
    public static String traditional2Simple(String src) {
        return ChineseUtils.toSimplified(src);
    }

    // 合并多线程下载的分块文件isdel若为true则删除分块文件
    public static String mergeFiles(String name, String path, boolean isdel, String code) {
        // 过滤出分块文件
        File[] files = new File(path + "/block").listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.getName().split("\\-").length < 2)
                    return false;
                else {
                    return true;
                }
            }
        });
        Arrays.sort(files, new FileSort());
        try (PrintWriter out = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(new File(path + "/" + name.replaceAll("[^\\u4E00-\\u9FFF]", "") + ".txt")), code));) {
            for (File file : files) {
                BufferedReader buf = new BufferedReader(
                        new InputStreamReader(new FileInputStream(file.getAbsolutePath()), code));
                String line = null;
                while ((line = buf.readLine()) != null)
                    out.println(line);
                out.println();
                buf.close();
                if (isdel)
                    file.delete();
            }
            return path;
        } catch (Exception e) {
            throw new RuntimeException("文件合并失败");
        }
    }

    // 排序合并文件的序列
    private static class FileSort implements Comparator<File> {
        @Override
        public int compare(File o1, File o2) {
            int o1index = Integer.parseInt(o1.getName().split("\\-")[0]);
            int o2index = Integer.parseInt(o2.getName().split("\\-")[0]);
            return o1index > o2index ? 1 : -1;
        }
    }
}
