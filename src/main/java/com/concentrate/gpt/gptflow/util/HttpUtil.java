package com.concentrate.gpt.gptflow.util;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 16070360 on 2018/9/12.
 */
public class HttpUtil {
    //log
    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    private static final String PROTOCOL_HTTP = "http";
    private static final String PROTOCOL_HTTPS = "https";

    //连接管理器
    private static PoolingHttpClientConnectionManager cm;
    //空字符串
    private static String EMPTY_STR = "";

    private HttpUtil() {
    }


    /**
     * 初始化
     */
    private static void init() {
        if (cm == null) {
            cm = new PoolingHttpClientConnectionManager();
            cm.setMaxTotal(50);// 整个连接池最大连接数
            cm.setDefaultMaxPerRoute(5);// 每路由最大连接数，默认值是2
        }
    }

    /**
     * 通过连接池获取HttpClient
     *
     * @return
     */
    private static CloseableHttpClient getHttpClient() {
        init();
        return HttpClients.custom().setConnectionManager(cm).build();
    }

    /**
     * post请求发送表单数据
     *
     * @param url    请求地址
     * @param params 键值对表单参数
     * @return 返回结果
     */
    public static String post(String url, Map<String, Object> params) {
        List<NameValuePair> formData = new ArrayList<NameValuePair>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
//            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            formData.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
        }
        //获取HTTP连接
        CloseableHttpClient httpClient = getHttpClient();
        HttpPost post = new HttpPost(url);
        try {
            UrlEncodedFormEntity requestEntity = new UrlEncodedFormEntity(formData, "utf-8");
            post.setEntity(requestEntity);
            HttpResponse response = httpClient.execute(post);
            logger.info("HttpResponse,code:{}"+response.getStatusLine().getStatusCode());
            HttpEntity entity = response.getEntity();
            //如果请求成功
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String responseStr = EntityUtils.toString(entity, "utf-8");
                EntityUtils.consume(entity);
                return responseStr;
            } else { //如果请求失败
                // 调用HttpGet的abort，这样就会直接中止这次连接，
                post.abort();
                logger.error("execute httpClient post request failed! response code is：" + response.getStatusLine().getStatusCode());
                return EMPTY_STR;
            }
        } catch (Exception e) {
            post.abort();
            logger.error("execute httpClient post request failed!" + e.getMessage(), e);
            return EMPTY_STR;
        }
    }

    /**
     * post请求发送表单数据
     *
     * @param url    请求地址
     * @param str 参数
     * @return 返回结果
     */
    public static String postString(String url,String str) {
        StringEntity reqEntity = new StringEntity(str, "UTF-8");

        //获取HTTP连接
        CloseableHttpClient httpClient = getHttpClient();
        HttpPost post = new HttpPost(url);
        try {
            post.setEntity(reqEntity);
            HttpResponse response = httpClient.execute(post);
            logger.info("HttpResponse,code:{}"+response.getStatusLine().getStatusCode());
            HttpEntity entity = response.getEntity();
            //如果请求成功
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String responseStr = EntityUtils.toString(entity, "utf-8");
                EntityUtils.consume(entity);
                return responseStr;
            } else { //如果请求失败
                // 调用HttpGet的abort，这样就会直接中止这次连接，
                post.abort();
                logger.error("execute httpClient post request failed! response code is：" + response.getStatusLine().getStatusCode());
                return EMPTY_STR;
            }
        } catch (Exception e) {
            post.abort();
            logger.error("execute httpClient post request failed!" + e.getMessage(), e);
            return EMPTY_STR;
        }
    }

    /**
     * Http接口通知-Get请求
     *
     * @param url url地址
     * @return
     */
    public static String doGet(String url) {
        //获取HTTP连接
        CloseableHttpClient httpClient = getHttpClient();
        HttpGet httpGet = new HttpGet(url);
        try {
            // 为httpGet实例设置配置
            /*RequestConfig requestConfig = RequestConfig.custom().
                setConnectTimeout(180 * 1000) //设置连接超时时间
                .setConnectionRequestTimeout(180 * 1000) // 设置请求超时时间
                .setSocketTimeout(180 * 1000)
                .setRedirectsEnabled(true) //默认允许自动重定向
                .build();
            httpPost.setConfig(requestConfig);*/
            //为httpGet设置请求头
            httpGet.setHeader("Accept", "application/json;charset=UTF-8");
            httpGet.setHeader("Accept-Encoding", "gzip, deflate, sdch");
            httpGet.setHeader("Connection", "Close");
            // 执行get请求得到返回对象
            HttpResponse response = httpClient.execute(httpGet);
            // 通过返回对象获取返回数据
            HttpEntity entity = response.getEntity();
            int stateCode = response.getStatusLine().getStatusCode();
            if (stateCode == HttpStatus.SC_OK && entity != null) {
                String responseStr = EntityUtils.toString(entity, "utf-8");
                EntityUtils.consume(entity);
                return responseStr;
            } else {
                // 调用HttpPost的abort，这样就会直接中止这次连接，
                httpGet.abort();
                logger.error("execute httpClient get request failed! response code is：{}", stateCode);
                return EMPTY_STR;
            }
        } catch (Exception e) {
            httpGet.abort();
            logger.error("execute httpClient get request failed!" + e.getMessage(), e);
            return EMPTY_STR;
        }
    }

    /**
     * post请求发送Json数据
     *
     * @param url        请求地址
     * @param jsonParams json字符串
     * @return 返回结果
     */
    public static String postJsonData(String url, String jsonParams) {
        //获取HTTP连接
        CloseableHttpClient httpClient = getHttpClient();
        HttpPost httpPost = new HttpPost(url);
        /*RequestConfig requestConfig = RequestConfig.custom().
                setConnectTimeout(180 * 1000) //设置连接超时时间
                .setConnectionRequestTimeout(180 * 1000) // 设置请求超时时间
                .setSocketTimeout(180 * 1000)
                .setRedirectsEnabled(true) //默认允许自动重定向
                .build();
        httpPost.setConfig(requestConfig);*/
        //httpPost.setHeader("Content-Type", ContentType.APPLICATION_JSON.toString());
        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
        try {
            StringEntity stringEntity = new StringEntity(jsonParams, Charset.forName("UTF-8"));
            stringEntity.setContentEncoding("UTF-8");
            // 发送Json格式的数据请求
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);
//            System.out.println("request parameters" + EntityUtils.toString(httpPost.getEntity()));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity entity = httpResponse.getEntity();
            //如果请求成功
//            System.out.println("status code:"+response.getStatusLine().getStatusCode());
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String responseStr = EntityUtils.toString(entity, "utf-8");
                EntityUtils.consume(entity);
                return responseStr;
            } else {
                // 调用HttpPost的abort，这样就会直接中止这次连接，
                httpPost.abort();
                logger.error("execute httpClient post request failed! response code is：" + httpResponse.getStatusLine().getStatusCode());
                return EMPTY_STR;
            }
        } catch (Exception e) {
            httpPost.abort();
            logger.error("execute httpClient post request failed!" + e.getMessage(), e);
            return EMPTY_STR;
        }
        /*finally {
            if(null != httpClient){
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }*/
    }

    public static void main(String[] args) {
        String url = "http://localhost:8080/jsadm/noauth/compareGoodsJson.do";
        Map <String,Object > param = new HashMap<String,Object>();
        param.put("gds_id","12386622630_0071203769");
        System.out.println(postJsonData(url, JSON.toJSONString(param)));
    }

}
