# somecodes


> 1. 校验身份证号码
```
public static boolean isIDNumber(String IDNumber) {
        if (IDNumber == null || "".equals(IDNumber)) {
            return false;
        }
        // 定义判别用户身份证号的正则表达式（15位或者18位，最后一位可以为字母）
        String regularExpression = "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|" +
                "(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)";
        //假设18位身份证号码:41000119910101123X  410001 19910101 123X
        //^开头
        //[1-9] 第一位1-9中的一个      4
        //\\d{5} 五位数字           10001（前六位省市县地区）
        //(18|19|20)                19（现阶段可能取值范围18xx-20xx年）
        //\\d{2}                    91（年份）
        //((0[1-9])|(10|11|12))     01（月份）
        //(([0-2][1-9])|10|20|30|31)01（日期）
        //\\d{3} 三位数字            123（第十七位奇数代表男，偶数代表女）
        //[0-9Xx] 0123456789Xx其中的一个 X（第十八位为校验值）
        //$结尾

        //假设15位身份证号码:410001910101123  410001 910101 123
        //^开头
        //[1-9] 第一位1-9中的一个      4
        //\\d{5} 五位数字           10001（前六位省市县地区）
        //\\d{2}                    91（年份）
        //((0[1-9])|(10|11|12))     01（月份）
        //(([0-2][1-9])|10|20|30|31)01（日期）
        //\\d{3} 三位数字            123（第十五位奇数代表男，偶数代表女），15位身份证不含X
        //$结尾


        boolean matches = IDNumber.matches(regularExpression);

        //判断第18位校验值
        if (matches) {

            if (IDNumber.length() == 18) {
                try {
                    char[] charArray = IDNumber.toCharArray();
                    //前十七位加权因子
                    int[] idCardWi = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
                    //这是除以11后，可能产生的11位余数对应的验证码
                    String[] idCardY = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
                    int sum = 0;
                    for (int i = 0; i < idCardWi.length; i++) {
                        int current = Integer.parseInt(String.valueOf(charArray[i]));
                        int count = current * idCardWi[i];
                        sum += count;
                    }
                    char idCardLast = charArray[17];
                    int idCardMod = sum % 11;
                    if (idCardY[idCardMod].toUpperCase().equals(String.valueOf(idCardLast).toUpperCase())) {
                        return true;
                    } else {
                        System.out.println("身份证最后一位:" + String.valueOf(idCardLast).toUpperCase() +
                                "错误,正确的应该是:" + idCardY[idCardMod].toUpperCase());
                        return false;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("异常:" + IDNumber);
                    return false;
                }
            }

        }
        return matches;
    } 
```
> 2. 爬虫、下载图片
```
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cayley on 2019/5/28.
 */
public class DownloadImg {

    public static void main(String[] args) throws Exception {

//        downloadPicture("http://ppbc.iplant.cn/image/236/4559711.jpg");
        parse360();

    }
    //链接url下载图片
    private static void downloadPicture(String urlList) throws Exception {
        URL url = null;
        int imageNumber = 0;
        try {
            url = new URL(urlList);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("referer", "http://ppbc.iplant.cn");
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();//通过输入流获取图片数据
            readInputStream(inStream, "/Users/cayley/Downloads/image/111.png");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readInputStream(InputStream inStream, String path) throws Exception{
        FileOutputStream fos = new FileOutputStream(new File(path));
        byte[] buffer = new byte[102400];
        int len = 0;
        while( (len=inStream.read(buffer)) != -1 ){
            fos.write(buffer, 0, len);
        }
        inStream.close();
        fos.flush();
        fos.close();
    }

    public static void parseSomething(String key){
        Map<String,String> map = new HashMap<>();
        try{
            Document doc = Jsoup.connect("http://ppbc.iplant.cn/list?keyword="+key).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31").get();
            System.out.println(doc);
            if(doc!=null){
                // dom解析获得指定元素
                Element count = doc.getElementById("form1");
                String id = count.baseUri().substring(count.baseUri().lastIndexOf("/")+1,count.baseUri().length());
                int i=1;
                while (true){
                    Document listDoc = Jsoup.connect("http://ppbc.iplant.cn/ashx/getphotopage.ashx?page="+i+"&n=2&group=sp&cid="+id).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31").get();

                    i++;
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void parse360(){
        Map<String,String> map = new HashMap<>();
        int c = 0;
        try{
            Document doc = Jsoup.connect("http://www.huacaoshumu.net/fenlei/shumu.php").userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31").get();
            if(doc!=null){
                // dom解析获得指定元素
                Elements divs = doc.getElementsByClass("bao");

                for(Element e:divs){
                    c++;
                    Node node = e.child(0).childNodes().get(1);
                    String url = e.child(0).attr("href");
                    url = url.replaceAll("\\r","");
                    url = url.replaceAll("\\n","");
                    Document oneDoc = Jsoup.connect("http://www.huacaoshumu.net"+url).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31").get();
                    int i=0;
                    if(oneDoc!=null){
                        Element tu = oneDoc.getElementById("tu");
                        String imageUrl = "http://www.huacaoshumu.net"+tu.attr("src");
                        String imgName = node.toString()+i+".jpg";
                        download222(imageUrl,imgName);

                        Elements imgHidden = oneDoc.getElementsByClass("image_hidden");
                        for(Element img:imgHidden.get(0).children()){
                            i++;
                            String imgUrl = "http://www.huacaoshumu.net"+img.attr("src");
                            String hname = node.toString()+i+".jpg";
                            download222(imgUrl,hname);
                            System.out.println("正在下载"+c+"树的第"+i+"个图片");
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void download222(String urlList,String imgName) throws Exception {
        URL url = null;
        int imageNumber = 0;
        try {
            url = new URL(urlList);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();//通过输入流获取图片数据
            readInputStream(inStream, "/Users/cayley/Downloads/image/"+imgName);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


<dependency>
    <groupId>org.jsoup</groupId>
    <artifactId>jsoup</artifactId>
    <version>1.8.3</version>
</dependency>

```

