package com.coinbene;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Created by caigaonian870 on 18/6/21.
 */
public class Util {
    private static final Logger log = LoggerFactory.getLogger(Util.class);


    public static String buildPostJson(Map<String,Object> paras){
        paras.put("sign",string2MD5(buildMd5Sign(paras)));
        paras.remove("secret");
        return JSON.toJSONString(paras);

    }
    public static String doPostForJson(String url, String jsonParams){
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom().
                    setConnectTimeout(180 * 1000).setConnectionRequestTimeout(180 * 1000)
                    .setSocketTimeout(180 * 1000).setRedirectsEnabled(true).build();

            httpPost.setConfig(requestConfig);
            httpPost.setHeader("Content-Type","application/json;charset=utf-8");
            httpPost.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; Trident/7.0; rv:11.0) like Gecko");
            httpPost.setHeader("Connection","keep-alive");
            try {
                httpPost.setEntity(new StringEntity(jsonParams, ContentType.create("application/json", "utf-8")));
                System.out.println("request parameters" + EntityUtils.toString(httpPost.getEntity()));
                HttpResponse response = httpClient.execute(httpPost);
                System.out.println(" code:"+response.getStatusLine().getStatusCode());
                System.out.println("doPostForInfobipUnsub response"+response.getStatusLine().toString());
                String result = EntityUtils.toString(response.getEntity());
                System.out.println("doPostForInfobipUnsub response"+result);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                //return "post failure :caused by-->" + e.getMessage().toString();
            }finally {
                if(null != httpClient){
                    try {
                        httpClient.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        return null;
    }
    public static String doGetRequest(String url){
        CloseableHttpClient httpCilent2 = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000)   //设置连接超时时间
                .setConnectionRequestTimeout(5000) // 设置请求超时时间
                .setSocketTimeout(5000)
                //.setRedirectsEnabled(true)//默认允许自动重定向
                .build();
        HttpGet httpGet2 = new HttpGet(url);
        httpGet2.setConfig(requestConfig);
        httpGet2.setHeader("Content-Type","application/json;charset=utf-8");
        httpGet2.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; Trident/7.0; rv:11.0) like Gecko");
        httpGet2.setHeader("Connection","keep-alive");
        String srtResult;
        try {
            HttpResponse httpResponse = httpCilent2.execute(httpGet2);
            if(httpResponse.getStatusLine().getStatusCode() == 200){
                srtResult = EntityUtils.toString(httpResponse.getEntity());//获得返回的结果

                System.out.println(srtResult);
                return srtResult;
            }else if(httpResponse.getStatusLine().getStatusCode() == 400){
                //..........
            }else if(httpResponse.getStatusLine().getStatusCode() == 500){
                //.............
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                httpCilent2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static  String buildMd5Sign(Map<String, Object> paras){

        Set<String> keys = paras.keySet();
        List<String> keyList = new ArrayList<>();
        for(String key:keys){
            keyList.add(key);
        }
        Collections.sort(keyList);
        StringBuffer sb = new StringBuffer();
        for(String key:keyList){
            sb.append(key);
            sb.append("=");
            sb.append(paras.get(key));
            sb.append("&");
        }
        String signStr =sb.toString();
        if(signStr.length() > 0){
            signStr=signStr.substring(0,signStr.length()-1);
        }
        log.info("signStr:{}",signStr);
        signStr = signStr.toUpperCase();
        return signStr;

    }

    /***
     * MD5加码 生成32位md5??
     */
    public static String string2MD5(String plainText){
        String re_md5 = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            re_md5 = buf.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return re_md5;
    }

}
