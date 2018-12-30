package top.unclez.spider;

import top.unclez.utils.BloomFileter;
import top.unclez.utils.SortUrl;
import top.unclez.utils.UrlFileter;
import top.unclez.utils.Utils;
import top.unclez.bean.GlobalValue;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 自动识别文章编码，支持乱序重排，过滤重复，过滤多余非章节的url
 * @author Uncle
 */
public class MainSpider {
    public static String defcharset="utf-8";
    public static String noveltitle="默认";
    /**
     *
     * @param html 章节列表源码
     * @return 小说名字（没有匹配到就返回uncle小说）
     */
    public static String getTitle(String html){
        String title="uncle小说";
        Pattern pattern=Pattern.compile("<title>([\\s\\S]{1,10})最新");
        Matcher m=pattern.matcher(html);
        if(m.find()) {
            title=m.group(1);
        }else {
            Matcher m2=Pattern.compile("<title>([\\s\\S]{10})").matcher(html);
            if(m2.find()){
                title=m2.group(1);
            }
        }
        return title.replaceAll("[\\pP]","");
    }

    /**
     *
     * @param url 章节目录地址
     * @param isFilter 是否需要过滤
     * @param isSort 是否需要排序
     * @return 章节列表
     */
    public static Map<String,String> getChapterList(String url,boolean isFilter,boolean isSort){
        List<String> urls=new ArrayList<>();
        Map<String,String> title=new HashMap<>();
        Map<String,String> chapter=new LinkedHashMap<>();
        String charset="gbk";
        try{
            BloomFileter fileter=new BloomFileter(BloomFileter.MisjudgmentRate.HIGH,10000,null);//布隆过滤器去重复url
            //抓取源码自动识别网页编码
            String html=Utils.getHtml(url,charset);
            charset=Utils.getEncode(html);
            if(!(charset.toLowerCase().equals("gbk"))){
                html=Utils.getHtml(url,charset);
            }
            System.out.println("网页编码"+charset);
            //自定义范围
            html=getDelHtml(GlobalValue.config.getCheader(),GlobalValue.config.getCtail(),html);
            //解析章节列表的所有url
            String regex="<a[\\s\\S]*?href[ ]{0,1}=[\"\']([^jJ][^#<>\\s]{3,}?)[\"\'][\\s\\S]*?>([^<>]*?)(</strong>){0,1}[\\s]*?(</span>){0,1}[\\s]*?</a>";
            Pattern p=Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
            Matcher m=p.matcher(html);
            while (m.find()){
                String res=Utils.getAbsUrl(url.substring(0,url.lastIndexOf("/")),m.group(1));
//                System.out.println(res+m.group(2));
                if(m.group(2).replace("\n","").length()==0||res.contains("#")||m.group(2).trim().length()==0)
                    continue;
                if(!fileter.addIfNotExist(res)){//布隆去重
                    urls.add(res);
                    title.put(res,m.group(2).trim());
                }
            }
            //过滤出不需要的
            if(isFilter){
                System.out.println("基准"+UrlFileter.getBaseUrl(urls));
                urls=UrlFileter.getChapterFilterRes(urls);
            }
            //乱序重排
            if(isSort){
                Collections.sort(urls,new SortUrl());
            }
            //预处理完后放入map集合
            int index=0;
            for (String s:urls){
                System.out.println(s+"   ---  "+title.get(s));
                String names=title.get(s);
                chapter.put(s,names);
                index++;
            }
//            System.out.println(getTitle(html));
            noveltitle=getTitle(html);
            defcharset=charset;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chapter;
    }

    /**
     *
     * @param url 正文url
     * @param encode 编码格式
     * @return 正文内容
     */
    public static String getContent(String url,String encode,String rule) {
        StringBuffer content=new StringBuffer();
        String ch_punctuation="~\\u000A\\u0009\\u00A0\\u0020\\u3000\\uFEFF";//中文标点符号
        String unicode_azAZ09="\\uFF41-\\uFF5a\\uFF21-\\uFF3a\\uFF10-\\uFF19";//unicode符号
        String chinese="\\u4E00-\\u9FFF";//中文
        String html ="";
        try {
            html =Utils.getHtml(url,encode);
//			System.out.println(html);
			if(html==null){
			    return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //自定义范围
        html=getDelHtml(GlobalValue.config.getDheader(),GlobalValue.config.getDtail(),html);
        switch (rule){
            case "1":
                Pattern compile = Pattern.compile("[pvri\\-/\"]>([^字<*][\\pP\\w\\pN\\pL\\pM"
                        +unicode_azAZ09+chinese+ch_punctuation
                        + "]{3,}[^字\\w>]{0,2})(<br|</p|</d|<p|<!|<d|</li)",Pattern.CASE_INSENSITIVE);
                Matcher m=compile.matcher(html);
                while(m.find()) {
                    String c="";
                    if(((c=m.group(1).replaceAll("&[#\\w]{3,6};", " ").trim()).length())>0)
                          content.append(c+"\r\n");
                }
                break;
            case "2":
                Pattern compile2 = Pattern.compile("([^/][\\s\\S]*?>)([\\s\\S]*?)(<)",Pattern.CASE_INSENSITIVE);
                Matcher m2=compile2.matcher(html);
                while(m2.find()) {
//                    System.out.println(m2.group(2));
                    if((Pattern.matches("[\\s\\S]*?[^字\\w<*]["+chinese +"]{1,}[\\s\\S]*?",m2.group(2))||Pattern.matches("[\\s\\S]*?&#\\d{4,5}[\\s\\S]*?",m2.group(2)))
                            &&(m2.group(1).endsWith("br />")||m2.group(1).endsWith("br/>")||m2.group(1).endsWith("br>")||m2.group(1).endsWith("abc\">")||m2.group(1).endsWith("p>")||m2.group(1).endsWith("v>")||m2.group(1).endsWith("->"))
                             &&m2.group(2).replaceAll("&[#\\w]{3,6};"," ").trim().length()>0)
                            content.append(m2.group(2).replaceAll("&[#\\w]{3,6};"," ").trim()+"\r\n");
                }
                break;
        }
        return content.toString();
    }
    public static Map<String,String> getConfig(){
        Map<String,String> conf=new HashMap<>();
        conf.put("charset",defcharset);
        conf.put("title",noveltitle);
        return conf;
    }

    /**
     * 获取添加范围后的html
     * @param header 范围头
     * @param tail 范围尾部
     * @param src 源
     * @return 删减后的html
     */
    private static String getDelHtml(String header,String tail,String src){
        int end=tail!=null&&tail.length()>1 ?src.indexOf(tail):src.length();
        int st=header!=null&&header.length()>1 ? src.indexOf(header):0;
        if(st==-1)
            st=0;
        if(end==-1)
            end=src.length();
        if(st!=0)
            st-=5;
        if(end!=src.length())
            end+=5;
        System.out.println(st+"--"+end+"--"+tail+"--"+header);
        return src.substring(st,end);
    }
}