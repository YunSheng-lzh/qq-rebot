package com.jack.qqrebot.utils;

import com.jack.qqrebot.jst.xjf.CustomUrlEncodedFormEntity;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.*;
import java.util.*;

public class HttpUtils {
    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */

    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = null;
            if(StringUtils.isEmpty(param)){
                 urlNameString = url ;
            }else{
                 urlNameString = url + "?" + param;
            }

            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();


            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setRequestProperty("Content-Type"," application/json;charset=utf-8");
            // 建立实际的连接


                connection.connect();

            // 获取所有响应头字段
            //Map<String, List<String>> map = connection.getHeaderFields();
//            map.entrySet().forEach(stringListEntry -> {
//                System.out.println(stringListEntry.getKey() +"-->"+stringListEntry.getValue());
//            });
            // 定义 BufferedReader输入流来读取URL的响应

            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(),"utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }


    public static String sendGetUseGBK(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = null;
            if(StringUtils.isEmpty(param)){
                urlNameString = url ;
            }else{
                urlNameString = url + "?" + param;
            }

            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();

            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setRequestProperty("Content-Type"," application/json;charset=utf-8");
            // 建立实际的连接


            connection.connect();

            // 获取所有响应头字段
            //Map<String, List<String>> map = connection.getHeaderFields();
//            map.entrySet().forEach(stringListEntry -> {
//                System.out.println(stringListEntry.getKey() +"-->"+stringListEntry.getValue());
//            });
            // 定义 BufferedReader输入流来读取URL的响应

            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(),"gbk"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }


    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */

    public static String sendPost(String url, String param) {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();

            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new OutputStreamWriter(conn.getOutputStream(),"utf-8");
            // 发送请求参数
            out.write(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(),"utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }


    public static String getWav(String url,String param)  {
        OutputStreamWriter out = null;
        InputStream iStream = null;
        FileOutputStream outputStream = null;
        String fileName = UUID.randomUUID().toString().replace("-","")+".wav";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();;
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);

            // 获取URLConnection对象对应的输出流
            out = new OutputStreamWriter(conn.getOutputStream(),"utf-8");
            // 发送请求参数
            out.write(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应

            File file = new File("C:\\CQPro\\data\\record\\"+fileName);//本地生成的文件
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                byte[] Buffer = new byte[4096 * 5];
                 outputStream = new FileOutputStream(file);
                 iStream = conn.getInputStream();//去字段用getBinaryStream()
                int size = 0;
                while ((size = iStream.read(Buffer)) != -1) {
                    outputStream.write(Buffer, 0, size);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(outputStream != null){
                    outputStream.close();
                }
                if(iStream!=null){
                    iStream.close();
                }
                if(out != null){
                    out.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return fileName;
    }


    public static String getPic(String url, String param) {
        InputStream iStream = null;
        FileOutputStream outputStream = null;
        String fileName = UUID.randomUUID().toString().replace("-","")+".jpg";
        try {
            String urlNameString = null;
            if(StringUtils.isEmpty(param)){
                urlNameString = url ;
            }else{
                urlNameString = url + "?" + param;
            }

            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            HttpURLConnection connection = null;
            connection=  (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setRequestProperty("Content-Type"," application/json;charset=utf-8");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
//            map.entrySet().forEach(stringListEntry -> {
//                //System.out.println(stringListEntry.getKey() +"-->"+stringListEntry.getValue());
//            });
            // 定义 BufferedReader输入流来读取URL的响应


            File file = new File("C:\\CQPro\\data\\image\\"+fileName);//本地生成的文件
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                byte[] Buffer = new byte[4096 * 5];
                outputStream = new FileOutputStream(file);
                iStream = connection.getInputStream();//去字段用getBinaryStream()
                int size = 0;
                while ((size = iStream.read(Buffer)) != -1) {
                    outputStream.write(Buffer, 0, size);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (iStream != null) {
                    iStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return fileName;
    }


    public static String requestOCRForHttp(String url, Map<String, String> requestParams,Map<String,String> headers) throws Exception {
        String result = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        /** HttpPost */
        HttpPost httpPost = new HttpPost(url);

        Set<Map.Entry<String, String>> entries = headers.entrySet();
        for (Map.Entry<String, String> header : entries) {
            httpPost.setHeader(header.getKey(),header.getValue());
        }

        if(requestParams != null){
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            Iterator<Map.Entry<String, String>> it = requestParams.entrySet().iterator();
//		System.out.println(params.toString());
            while (it.hasNext()) {
                Map.Entry<String, String> en = it.next();
                String key = en.getKey();
                String value = en.getValue();
                if (value != null) {
                    params.add(new BasicNameValuePair(key, value));
                }
            }
            CustomUrlEncodedFormEntity urlEncodedFormEntity = new CustomUrlEncodedFormEntity(params, "UTF-8");
            httpPost.setEntity(urlEncodedFormEntity);
        }
        /** HttpResponse */
        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
        try {
            HttpEntity httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity, "utf-8");
            EntityUtils.consume(httpEntity);
        } finally {
            try {
                if (httpResponse != null) {
                    httpResponse.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    public static byte[] inputStream2byte(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while ((rc = inputStream.read(buff, 0, 100)) > 0) {
            byteArrayOutputStream.write(buff, 0, rc);
        }
        return byteArrayOutputStream.toByteArray();
    }


    public static InputStream byte2InputStream(byte[] bytes) {
        return new ByteArrayInputStream(bytes);
    }
}