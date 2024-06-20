package com.kyle.utils;


import com.alibaba.fastjson.JSONObject;
import com.kyle.utils.listener.CallInfo;
import com.kyle.utils.listener.HttpClientHolder;
import com.kyle.utils.listener.HttpClientListener;
import com.kyle.utils.listener.HttpClientListenerRegister;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

/**
 *
 * @author carroll
 * @Date 2017-07-25 18:06
 **/
public class OkHttpUtil {
    private static final Logger logger = LoggerFactory.getLogger(OkHttpUtil.class);

    private static int CONNECT_TIMEOUT = 10;
    private static int READ_TIMEOUT = 10;
    private static int WRITE_TIMEOUT = 10;

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static OkHttpClient client;
    private MyTrustManager mMyTrustManager;

    private OkHttpUtil() {
    }

    private OkHttpUtil(int CONNECT_TIMEOUT,int READ_TIMEOUT,int WRITE_TIMEOUT) {
        this.CONNECT_TIMEOUT = CONNECT_TIMEOUT;
        this.READ_TIMEOUT = READ_TIMEOUT;
        this.WRITE_TIMEOUT = WRITE_TIMEOUT;
    }
    /**
     * 参数初始化
     */
    @PostConstruct
    private void init() {
        logger.info("\n init OkHttpUtil start");
//        logger.info("\n params: " + StringUtil.objToJsonString(config));
//        if (config.isEnable()) {
//            CONNECT_TIMEOUT = config.getConnectTimeout();
//            READ_TIMEOUT = config.getReadTimeout();
//            WRITE_TIMEOUT = config.getWriteTimeout();
//        }
        client = new OkHttpClient().newBuilder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .sslSocketFactory(createSSLSocketFactory(), mMyTrustManager)
                .hostnameVerifier(new TrustAllHostnameVerifier())
                .build();
    }

    /**
     * post请求
     *
     * @param url
     * @param content
     * @return
     * @throws IOException
     */
    public static OkResponse doPost(String url, String content) throws IOException {
        return doPost(url, content, null, null, null, null);
    }

    /**
     * post请求
     *
     * @param url
     * @param content
     * @return
     * @throws IOException
     */
    public static OkResponse doPost(String url, String content, Map<String, String> headers,
                                    Long connectTimeout, Long readTimeout, Long writeTimeout) throws IOException {
        HttpClientHolder.setRequestBody(content);
        RequestBody body = RequestBody.create(JSON, content);
        Request.Builder builder = new Request.Builder().url(url);
        if (headers != null) {
            headers.forEach(builder::addHeader);
        }
        OkResponse response;
        try {
            response = invoke(builder.post(body).build(), connectTimeout, readTimeout, writeTimeout);
            logger.info("调用OkHttpUtil post方法 URL： {}, 参数： {}, headers： {}, \n结果： {}",
                    url, content, JSONObject.toJSONString(headers), JSONObject.toJSONString(response));
        } catch (IOException e) {
            logger.error("异常！调用OkHttpUtil post方法 URL： {}, 参数： {}, headers： {}",
                    url, content, JSONObject.toJSONString(headers));
            throw e;
        }

        return response;
    }

    /**
     * post请求
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static OkResponse doPost(String url, String content, Map<String, String> headers) throws IOException {
        if (content == null) {
            content = "{}";
        }
        HttpClientHolder.setRequestBody(content);
        RequestBody body = RequestBody.create(JSON, content);

        Request.Builder builder = new Request.Builder().url(url);
        if (headers != null) {
            headers.forEach(builder::addHeader);
        }

        builder.url(url)
                .post(body)
                .build();

        OkResponse response;
        try {
            response = invoke(builder.build());
            logger.info("调用OkHttpUtil post方法 URL： {}, 参数： {}, headers： {}, \n结果： {}",
                    url, content, JSONObject.toJSONString(headers), JSONObject.toJSONString(response));
        } catch (IOException e) {
            logger.error("异常！调用OkHttpUtil post方法 URL： {}, 参数： {}, headers： {}",
                    url, content, JSONObject.toJSONString(headers));
            throw e;
        }

        return response;
    }

    /**
     * get请求
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static OkResponse doGet(String url) throws IOException {
        return doGet(url, null, null, null, null, null);
    }

    /**
     * get请求
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static OkResponse doGet(String url, String requestStr, Map<String, String> headers) throws IOException {
        return doGet(url, requestStr, headers, null, null, null);
    }

    /**
     * get请求
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static OkResponse doGet(String url, String requestStr, Map<String, String> headers,
                                   Long connectTimeout, Long readTimeout, Long writeTimeout) throws IOException {
        HttpClientHolder.setRequestBody(null);
        if (!StringUtil.isEmpty(requestStr)) {
            url = getUrl(url, requestStr);
        }

        Request.Builder builder = new Request.Builder().url(url);
        if (headers != null) {
            headers.forEach(builder::addHeader);
        }
        OkResponse response;
        try {
            response = invoke(builder.build(), connectTimeout, readTimeout, writeTimeout);
            logger.info("调用OkHttpUtil get方法 URL： {}, 参数： {}, headers： {}, \n结果： {}",
                    url, requestStr, JSONObject.toJSONString(headers), JSONObject.toJSONString(response));
        } catch (IOException e) {
            logger.error("异常！调用OkHttpUtil get方法 URL： {}, 参数： {}, headers： {}",
                    url, requestStr, JSONObject.toJSONString(headers));
            throw e;
        }
        return response;
    }

    /**
     * delete请求
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static OkResponse doDelete(String url) throws IOException {
        HttpClientHolder.setRequestBody(null);
        Request request = new Request.Builder().url(url).delete().build();
        OkResponse response;
        try {
            response = invoke(request);
            logger.info("调用OkHttpUtil delete方法 URL： {}, \n结果： {}", url, JSONObject.toJSONString(response));
        } catch (IOException e) {
            logger.error("异常！调用OkHttpUtil delete方法 URL： {}", url);
            throw e;
        }
        return response;
    }

    /**
     * put请求
     *
     * @param url
     * @param content
     * @return
     * @throws IOException
     */
    public static OkResponse doPut(String url, String content) throws IOException {
        if (content == null) {
            content = "{}";
        }
        HttpClientHolder.setRequestBody(content);
        RequestBody body = RequestBody.create(JSON, content);
        Request request = new Request.Builder().url(url).put(body).build();

        OkResponse response;
        try {
            response = invoke(request);
            logger.info("调用OkHttpUtil put方法 URL： {}, 参数： {}, \n结果： {}",
                    url, content, JSONObject.toJSONString(response));
        } catch (IOException e) {
            logger.error("异常！调用OkHttpUtil put方法 URL： {}, 参数： {}", url, content);
            throw e;
        }
        return response;
    }

    private static OkResponse invoke(Request request) throws IOException {
        return invoke(request, null, null, null);
    }

    private static OkResponse invoke(Request request, Long connectTimeout, Long readTimeout, Long writeTimeout) throws IOException {
        HttpClientHolder.setResponseCode(null);
        HttpClientHolder.setResponseBody(null);
        HttpClientHolder.setUrl(request.url().toString());
        OkHttpClient tempclient = client;
        if (tempclient == null) {
            client = new OkHttpClient().newBuilder()
                    .connectTimeout(connectTimeout == null ? 10 : connectTimeout, TimeUnit.SECONDS)
                    .readTimeout(readTimeout == null ? 10 : readTimeout, TimeUnit.SECONDS)
                    .writeTimeout(writeTimeout == null ? 10 : writeTimeout, TimeUnit.SECONDS)
                    .build();
            tempclient = client;
        }

        for (HttpClientListener listener : HttpClientListenerRegister.listeners()) {
            try {
                request = listener.pre(request);
            } catch (Exception e) {
                logger.warn("第三方接口调用pre监听器执行错误", e);
            }
        }

        CallInfo info = new CallInfo();
        info.setRequestTime(System.currentTimeMillis());
        try (Response response = tempclient.newCall(request).execute()) {
            info.setResponseTime(System.currentTimeMillis());
            String resStr = null;
            byte[] bytes = null;
            Integer code = response.code();
            HttpClientHolder.setResponseCode(code);
            ResponseBody body = response.body();

            if (body != null) {
                String disposition = response.header("Content-Disposition");
                if (!StringUtil.isEmpty(disposition) && disposition.endsWith(".gz")) {
                    resStr = decompressGZIP(body.byteStream());
                } else {
                    bytes = body.bytes();
                    resStr = new String(bytes);
                }
                HttpClientHolder.setResponseBody(resStr);
            }

            for (HttpClientListener listener : HttpClientListenerRegister.listeners()) {
                try {
                    listener.execute(request, HttpClientHolder.getResponseBody(), info);
                } catch (Exception e) {
                    logger.warn("第三方接口调用监听器执行错误", e);
                }
            }

            if (response.isSuccessful()) {
                logger.info("返回结果：{}:{}", code, resStr);
                return new OkResponse(code, resStr, bytes);
            } else {
                if (body != null) {
                    logger.error("请求失败, 返回结果：" + resStr);
                }
                throw new IOException("Unexpected code " + response);
            }
        }
    }

    public static OkResponse post(String url, byte[] bytes, Map<String, String> headers) throws IOException {
        Request.Builder builder = new Request.Builder().url(url);
        if (headers != null) {
            headers.forEach(builder::addHeader);
        }
        builder.post(RequestBody.create(MediaType.parse("application/octet-stream"), bytes)).build();
        OkResponse response;
        try {
            response = invoke(builder.build(), null, null, null);
            logger.info("调用OkHttpUtil post方法 URL： {}, headers： {}, \n结果： {}",
                    url, JSONObject.toJSONString(headers), JSONObject.toJSONString(response));
        } catch (IOException e) {
            logger.error("异常！调用OkHttpUtil get方法 URL： {}, headers： {}",
                    url, JSONObject.toJSONString(headers));
            throw e;
        }
        return response;
    }


    /**
     * 解压gz
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static String decompressGZIP(InputStream inputStream) throws IOException {
        InputStream bodyStream = new GZIPInputStream(inputStream);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int length;
        while ((length = bodyStream.read(buffer)) > 0) {
            outStream.write(buffer, 0, length);
        }

        return new String(outStream.toByteArray());
    }

    /**
     * 基于application/x-www-form-urlencoded的post请求
     *
     * @param url
     * @param content
     * @return
     * @throws IOException
     */
    public static OkResponse doPostFormUrlEncoded(String url, Map<String, String> content) throws IOException {
        if (content != null && !content.isEmpty()) {
            HttpClientHolder.setRequestBody(JSONObject.toJSONString(content));
        }

        FormBody.Builder builder = new FormBody.Builder();
        if (content != null) {
            content.forEach(builder::add);
        }
        Request request = new Request.Builder().url(url).post(builder.build()).build();
        OkResponse response;
        try {
            response = invoke(request);
            logger.info("调用OkHttpUtil postForm方法 URL： {}, 参数： {}, \n结果： {}",
                    url, content, JSONObject.toJSONString(response));
        } catch (IOException e) {
            logger.error("异常！调用OkHttpUtil postForm方法 URL： {}, 参数： {}", url, content);
            throw e;
        }
        return response;
    }

    /**
     * 基于application/x-www-form-urlencoded的post请求
     *
     * @param url
     * @param content
     * @param header
     * @return
     * @throws IOException
     */
    public static OkResponse doPostFormUrlEncoded(String url, Map<String, String> content, Map<String, String> header) throws IOException {
        if (content != null && !content.isEmpty()) {
            HttpClientHolder.setRequestBody(JSONObject.toJSONString(content));
        }

        FormBody.Builder builder = new FormBody.Builder();
        if (content != null) {
            content.forEach(builder::add);
        }

        Request.Builder requestBuilder = new Request.Builder().url(url);
        if (header != null) {
            header.forEach(requestBuilder::addHeader);
        }

        Request request = requestBuilder.post(builder.build()).build();
        OkResponse response;
        try {
            response = invoke(request);
            logger.info("调用OkHttpUtil postForm方法 URL： {}, 参数： {}, header:{} \n结果： {}",
                    url, content, JSONObject.toJSONString(header), JSONObject.toJSONString(response));
        } catch (IOException e) {
            logger.error("异常！调用OkHttpUtil postForm方法 URL： {}, 参数： {}", url, content);
            throw e;
        }
        return response;
    }

    /**
     * 基于application/x-www-form-urlencoded的put请求
     *
     * @param url
     * @param content
     * @return
     * @throws IOException
     */
    public static OkResponse doPutFormUrlEncoded(String url, Map<String, String> content) throws IOException {
        if (content != null && !content.isEmpty()) {
            HttpClientHolder.setRequestBody(JSONObject.toJSONString(content));
        }
        FormBody.Builder builder = new FormBody.Builder();
        if (content != null) {
            content.forEach(builder::add);
        }
        Request request = new Request.Builder().url(url).put(builder.build()).build();

        OkResponse response;
        try {
            response = invoke(request);
            logger.info("调用OkHttpUtil putForm方法 URL： {}, 参数： {}, \n结果： {}",
                    url, content, JSONObject.toJSONString(response));
        } catch (IOException e) {
            logger.error("异常！调用OkHttpUtil putForm方法 URL： {}, 参数： {}", url, content);
            throw e;
        }
        return response;
    }

    /**
     * 基于application/x-www-form-urlencoded的put请求
     *
     * @param url
     * @param content
     * @param header
     * @return
     * @throws IOException
     */
    public static OkResponse doPutFormUrlEncoded(String url, Map<String, String> content, Map<String, String> header) throws IOException {
        if (content != null && !content.isEmpty()) {
            HttpClientHolder.setRequestBody(JSONObject.toJSONString(content));
        }

        FormBody.Builder builder = new FormBody.Builder();
        if (content != null) {
            content.forEach(builder::add);
        }

        Request.Builder requestBuilder = new Request.Builder().url(url);
        if (header != null) {
            header.forEach(requestBuilder::addHeader);
        }

        Request request = requestBuilder.put(builder.build()).build();
        OkResponse response;
        try {
            response = invoke(request);
            logger.info("调用OkHttpUtil postForm方法 URL： {}, 参数： {}, header:{} \n结果： {}",
                    url, content, JSONObject.toJSONString(header), JSONObject.toJSONString(response));
        } catch (IOException e) {
            logger.error("异常！调用OkHttpUtil postForm方法 URL： {}, 参数： {}", url, content);
            throw e;
        }
        return response;
    }

    /**
     * 基于multipart/form-data的post请求
     *
     * @param url
     * @param content
     * @return
     * @throws IOException
     */
    public static OkResponse doPostFormData(String url, Map<String, String> headers, Map<String, Object> content) throws IOException {
        if (content != null && !content.isEmpty()) {
            HttpClientHolder.setRequestBody(JSONObject.toJSONString(content));
        }
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        if (content != null) {
            content.forEach((key, value) -> {
                if (value instanceof RequestBody) {
                    builder.addFormDataPart(key, (String) content.get("filename"), (RequestBody) value);
                } else {
                    builder.addFormDataPart(key, String.valueOf(value));
                }
            });
        }
        Request.Builder requestBuilder = new Request.Builder().url(url);
        if (headers != null) {
            headers.forEach(requestBuilder::addHeader);
        }
        Request request = requestBuilder.post(builder.build()).build();

        OkResponse response;
        try {
            response = invoke(request);
            logger.info("调用OkHttpUtil postForm方法 URL： {}, 参数： {}, headers： {}, \n结果： {}",
                    url, content, JSONObject.toJSONString(request.headers().toMultimap()), JSONObject.toJSONString(response));
        } catch (IOException e) {
            logger.error("异常！调用OkHttpUtil postForm方法 URL： {}, 参数： {}, headers： {}",
                    url, content, JSONObject.toJSONString(request.headers().toMultimap()));
            throw e;
        }
        return response;
    }

    /**
     * 基于multipart/form-data的put请求
     *
     * @param url
     * @param content
     * @return
     * @throws IOException
     */
    public static OkResponse doPutFormData(String url, Map<String, String> content) throws IOException {
        if (content != null && !content.isEmpty()) {
            HttpClientHolder.setRequestBody(JSONObject.toJSONString(content));
        }
        FormBody.Builder builder = new FormBody.Builder();
        if (content != null) {
            content.forEach(builder::add);
        }
        Request request = new Request.Builder().url(url).put(builder.build()).build();
        logger.info("调用OkHttpUtil putForm方法 URL： " + url + ", 参数： " + content);

        OkResponse response;
        try {
            response = invoke(request);
            logger.info("调用OkHttpUtil putForm方法 URL： {}, 参数： {}, \n结果： {}",
                    url, content, JSONObject.toJSONString(response));
        } catch (IOException e) {
            logger.error("异常！调用OkHttpUtil putForm方法 URL： {}, 参数： {}", url, content);
            throw e;
        }
        return response;
    }

    /**
     * url是否连通
     *
     * @param httpUrl url
     * @return boolean
     */
    public static boolean canGetHtmlCode(String httpUrl) {
        boolean result = false;
        CallInfo info = new CallInfo();
        info.setRequestTime(System.currentTimeMillis());
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/4.0");
            connection.setConnectTimeout(1000);
            connection.connect();
            connection.disconnect();
            result = true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            result = false;
        }
        info.setResponseTime(System.currentTimeMillis());
        HttpClientListenerRegister.listeners().forEach(listener -> {
            try {
                listener.execute(new Request.Builder().url(httpUrl).build(), null, info);
            } catch (Exception e) {
                logger.warn("第三方接口调用监听器执行错误", e);
            }
        });
        return result;
    }

    /**
     * 上传文件。
     *
     * @param formFields 表单信息
     * @param files      文件信息
     */
    public static OkResponse postMultipart(String url, Map<String, String> formFields, File[] files) throws Exception {

        return postMultipart(url, formFields, null, files, null, "files");
    }

    /**
     * 上传文件。
     *
     * @param formFields 表单信息
     * @param files      文件信息
     */
    public static OkResponse postMultipart(String url, Map<String, String> formFields, Map<String, String> headers, File[] files, String[] mediaTypes, String fileFieldName) throws Exception {

        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        for (Map.Entry<String, String> field : formFields.entrySet()) { // 添加表单信息
            bodyBuilder.addFormDataPart(field.getKey(), field.getValue());
        }
        MediaType mediaType = MediaType.parse("text/plain");
        File file = null;
        for (int i = 0; i < files.length; i++) { // 添加文件
            file = files[i];
            if (mediaTypes != null) {
                mediaType = MediaType.parse(mediaTypes[i]);
            }
            bodyBuilder.addFormDataPart(fileFieldName, file.getName(),
                    RequestBody.create(mediaType, file));
        }

        Request.Builder requestBuilder = new Request.Builder().url(url);
        if (headers != null) {
            headers.forEach(requestBuilder::addHeader);
        }

        Request request = requestBuilder
                .url(url)
                .post(bodyBuilder.build())
                .build();
        OkResponse response;
        String content = JSONObject.toJSONString(formFields);
        try {
            response = invoke(request);
            logger.info("调用OkHttpUtil postMultipart方法 URL： {}, 参数： {}, \n结果： {}",
                    url, content, JSONObject.toJSONString(response));
        } catch (IOException e) {
            logger.error("异常！调用OkHttpUtil postMultipart方法 URL： {}, 参数： {}", url, content);
            throw e;
        }
        return response;
    }

    /**
     * 拼接url，特殊符号进行url编码
     *
     * @param url
     * @param params
     * @return
     */
    public static String getUrl(String url, String params) {
        Map mapParams = JSONObject.parseObject(params);

        if (null != mapParams && mapParams.size() > 0) {
            mapParams.forEach((k, v) -> {
                try {
                    mapParams.put(k, URLEncoder.encode(v.toString(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    logger.error("UnsupportedEncodingException", e);
                }
            });
        }
        return StringUtil.getUrl(url, mapParams);
    }

    public static class OkResponse {
        private Integer code;
        private String result;
        private byte[] bytes;

        public OkResponse() {
        }

        public OkResponse(Integer code, String result, byte[] bytes) {
            this.code = code;
            this.result = result;
            this.bytes = bytes;
        }

        public byte[] getBytes() {
            return bytes;
        }

        public void setBytes(byte[] bytes) {
            this.bytes = bytes;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public boolean isSuccessful() {
            return code != null && code >= 200 && code < 300;
        }
    }


    private SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;
        try {
            mMyTrustManager = new MyTrustManager();
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{mMyTrustManager}, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }

        return ssfFactory;
    }

    //实现X509TrustManager接口
    public class MyTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    //实现HostnameVerifier接口
    private class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
}
