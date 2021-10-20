package com.carroll.utils.listener;

/**
 * @author: hehongbo
 * @date 2018/7/31
 */
public class HttpClientHolder {

    private static final ThreadLocal<String> requestBody = new ThreadLocal<String>() {
        @Override
        protected String initialValue() {
            return null;
        }
    };

    private static final ThreadLocal<String> responseBody = new ThreadLocal<String>() {
        @Override
        protected String initialValue() {
            return null;
        }
    };

    private static final ThreadLocal<Integer> responseCode = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return null;
        }
    };
    private static final ThreadLocal<String> url = new ThreadLocal<String>() {
        @Override
        protected String initialValue() {
            return null;
        }
    };

    public static void setRequestBody(String request) {
        requestBody.set(request);
    }

    public static void setResponseBody(String response) {
        responseBody.set(response);
    }

    public static String getRequestBody() {
        return requestBody.get();
    }

    public static String getResponseBody() {
        return responseBody.get();
    }

    public static Integer getResponseCode() {
        return responseCode.get();
    }

    public static void setResponseCode(Integer code) {
        responseCode.set(code);
    }

    public static void clean() {
        requestBody.remove();
        responseBody.remove();
        responseCode.remove();
    }

    public static String getUrl() {
        return url.get();
    }

    public static void setUrl(String value) {
        url.set(value);
    }
}
