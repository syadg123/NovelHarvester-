# 一、前言
这个东西是大二开始做的，不过当时花了几天做了一个1.0版本 然后发布到了吾爱破解论坛，发现下载的人还挺多的，后面还有人提了意见加阅读器之类，然后就开始了不归路。。
但是我自己想做的就是一个能长期使用并且可以无Server的PC软件，所以我一直坚持着这个理念。为什么不做安卓，因为安卓这类软件太多了。
1.0 到 4.0已经快两年了。完全是个兴趣作品，兴致来了就更新一下。到现在大三快结束了，碰上了2019-nCoV，所以又多了点时间更新了一下，也就是4.0了。
先上图看看效果
![](https://imgconvert.csdnimg.cn/aHR0cDovL3VuY2xlLWRldi51bmNsZXpzLmNvbS9ibG9nLzIwMjAwNTI3MTMxOTUxLnBuZw?x-oss-process=image/format,png)
# 二、用到的东西
Java码农用到的东西。
1. JavaFX2
2. [Mybatis](https://mybatis.org/mybatis-3/) and [plus](https://github.com/baomidou/mybatis-plus) and sqlite
3. UI组件 [Jfoenix](https://github.com/jfoenixadmin/JFoenix)、[ControlsFX](https://github.com/controlsfx/controlsfx)
4. 著名工具包 [Hutool](https://github.com/looly/hutool)
5. 自动更新 [FXLauncher](https://github.com/edvin/fxlauncher)
6. 全局热键 [Jnativehook](https://github.com/kwhat/jnativehook)

# 三、功能
目录解析小说下载(支持TXT，MOBI，EPUB)、在线阅读、文本转语音(windows引擎)、有声小说下载，在线听
## 1.文本小说
软件支持文本小说的搜索、下载、解析目录、合成语音、在线阅读等功能
### 1 搜索
内置几个书源搜索、可以自行添加。搜索也只是根据规则找到目录地址，然后在通过目录解析进行下载的
![](https://imgconvert.csdnimg.cn/aHR0cDovL3VuY2xlLWRldi51bmNsZXpzLmNvbS9ibG9nLzIwMjAwNTI3MTM1MDI4LnBuZw?x-oss-process=image/format,png)
### 2 解析目录
任意一个网站的小说目录地址，填入如果检测到剪贴板有会自动导入，点击解析目录则会解析出来章节目录，如果解析成功之后可以加入书架在线阅读，也可以直接下载

双击目录章节列表地方查看章节内容，如果发现有广告则再解析配置中添加广告字符串即可去除。如果没有匹配到章节内容可以换个浏览器打开，如果还是没有，则开启模拟浏览器，再进行上诉操作
![](https://imgconvert.csdnimg.cn/aHR0cDovL3VuY2xlLWRldi51bmNsZXpzLmNvbS9ibG9nLzIwMjAwNTI3MTM1MjA5LnBuZw?x-oss-process=image/format,png)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200527135239960.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQyMDA2MTIw,size_16,color_FFFFFF,t_70)
### 3.在线阅读
#### 书架 
按照自己想法解析完成之后，添加到书架即可在线阅读，可以导入本地TXT，可以拖入。
点击进行阅读，也可以右键拉取最新章节列表。缓存式阅读，阅读后自动缓存到本地，移除书架时，清理缓存文件。
![](https://imgconvert.csdnimg.cn/aHR0cDovL3VuY2xlLWRldi51bmNsZXpzLmNvbS9ibG9nLzIwMjAwNTI3MTMxOTUxLnBuZw?x-oss-process=image/format,png)
#### 阅读器
滚动方式阅读，上下章节拼接，沉浸状态栏模式，让阅读体验更佳，支持常规的阅读器设置。使用windows语音引擎朗读
![](https://imgconvert.csdnimg.cn/aHR0cDovL3VuY2xlLWRldi51bmNsZXpzLmNvbS9ibG9nLzIwMjAwNTI3MTQwMDUzLnBuZw?x-oss-process=image/format,png)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200527140118645.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQyMDA2MTIw,size_16,color_FFFFFF,t_70)
## 2.有声小说
### 1.搜索
搜索有声小说然后获取到目录，**首先检测音频是否失效** 然后再进行后续操作，加入书架，添加下载。

![](https://imgconvert.csdnimg.cn/aHR0cDovL3VuY2xlLWRldi51bmNsZXpzLmNvbS9ibG9nLzIwMjAwNTI3MTQwNjExLnBuZw?x-oss-process=image/format,png)
![](https://imgconvert.csdnimg.cn/aHR0cDovL3VuY2xlLWRldi51bmNsZXpzLmNvbS9ibG9nLzIwMjAwNTI3MTQwNzE5LnBuZw?x-oss-process=image/format,png)
### 2.加入书架在线收听
可以加入书架后听，方便快捷，也能记录位置。
![](https://imgconvert.csdnimg.cn/aHR0cDovL3VuY2xlLWRldi51bmNsZXpzLmNvbS9ibG9nLzIwMjAwNTI3MTQwODI3LnBuZw?x-oss-process=image/format,png)
## 3.下载及配置
再下载时候可以配置下载的线程数量，线程数量越多当然下载会快，但是可能会导致别人服务器受不了，所以不推荐太多线程。而且线程多了之后很可能会出现封IP导致下载失败的情况出现。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200527141000132.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQyMDA2MTIw,size_16,color_FFFFFF,t_70)
![](https://imgconvert.csdnimg.cn/aHR0cDovL3VuY2xlLWRldi51bmNsZXpzLmNvbS9ibG9nLzIwMjAwNTI3MTQxMDI3LnBuZw?x-oss-process=image/format,png)

## 其他
增加了软件主题配置和国际化语言支持

# 最后
github地址：欢迎star ***https://github.com/unclezs/NovelHarvester***
下载地址: https://uncle.lanzous.com/b0ibxcpe
欢迎关注公众号【书虫无书荒】获取最新资讯及使用教程

# 如果你觉得软件不错 欢迎star
![](http://uncle-dev.unclezs.com/blog/1590252920.jpg?blog)
