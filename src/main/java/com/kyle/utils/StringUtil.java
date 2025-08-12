package com.kyle.utils;

import com.alibaba.fastjson.JSONObject;

import java.util.*;

/**
 * @author carroll
 * @Date 2017-07-25 18:06
 **/
public class StringUtil {
    private static final String CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int RETIRES = 3;

    /**
     * 生成密码
     *
     * @return
     */
    public static String generatePwd() {
        Random r = new Random(System.currentTimeMillis());
        int[] tag = {0, 0, 0};

        String pwd = new String();

        while (pwd.length() < RETIRES) {
            pwd += CHARS.charAt(r.nextInt(52));
        }

        int temp;
        while (pwd.length() < RETIRES + RETIRES) {
            temp = r.nextInt(3);
            if (tag[temp] == 0) {
                int randomNum = r.nextInt(10);
                pwd = pwd + randomNum;
                tag[temp] = 1;
            }
        }

        return pwd;
    }

    /**
     * 组装请求参数
     *
     * @param url
     * @param params
     * @return
     */
    public static String getUrl(String url, String params) {
        Map mapParams = JSONObject.parseObject(params);
        return getUrl(url, mapParams);
    }

    /**
     * 组装请求参数
     *
     * @param url
     * @param params
     * @return
     */
    public static String getUrl(String url, Object params) {
        String strParams = StringUtil.objToJsonString(params);
        Map mapParams = JSONObject.parseObject(strParams);
        return getUrl(url, mapParams);
    }

    /**
     * 组装请求参数
     *
     * @param url
     * @param params
     * @return
     */
    public static String getUrl(String url, Map params) {
        // 添加url参数
        if (params != null) {
            Iterator<Map.Entry> iterator = params.entrySet().iterator();
            StringBuffer sb = null;
            while (iterator.hasNext()) {
                Map.Entry map = iterator.next();
                String key = map.getKey().toString();
                Object value = map.getValue();
                if (sb == null) {
                    sb = new StringBuffer();
                    if (url != null && url.indexOf("?") > -1) {
                        sb.append("&");
                    } else {
                        sb.append("?");
                    }
                } else {
                    sb.append("&");
                }
                sb.append(key);
                sb.append("=");
                sb.append(value);
            }
            if (sb != null) {
                url += sb.toString();
            }
        }
        return url;
    }

    public static String objToJsonString(Object object) {
        return JSONObject.toJSONString(object);
    }

    public static Map jsonStringToMap(String str) {
        return JSONObject.parseObject(str, Map.class);
    }

    public static JSONObject jsonStringToJsonObj(String str) {
        return JSONObject.parseObject(str);
    }

    public static <T> T jsonStringToClazz(String str, Class<T> clazz) {
        return JSONObject.parseObject(str, clazz);
    }

    /**
     * 解析查询字符串
     *
     * @param queryString
     * @return
     */
    public static Map<String, String> parseQueryString(String queryString) {
        if (isEmpty(queryString)) {
            return null;
        }
        Map<String, String> paramMap = new HashMap<>();
        StringTokenizer st = new StringTokenizer(queryString, "&");
        int eqFlagIndex = -1;
        while (st.hasMoreTokens()) {
            String pairs = st.nextToken();
            eqFlagIndex = pairs.indexOf('=');
            if (eqFlagIndex < 0) {
                continue;
            }
            String key = pairs.substring(0, pairs.indexOf('='));
            String value = pairs.substring(pairs.indexOf('=') + 1);
            paramMap.put(key, value);
        }
        return paramMap;
    }

    /**
     * 生成指定长度的包含数字和字母的随机字符串
     *
     * @param length
     * @return
     */
    public static String getRandomString(int length) {
        String baseStr = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        int baseLength = baseStr.length();

        for (int i = 0; i < length; ++i) {
            str.append(baseStr.charAt(random.nextInt(baseLength - 1)));
        }

        return str.toString();
    }

    public static boolean isEmpty(Object str) {
        return str == null || "".equals(str);
    }

    public static String shortUUID() {
        UUID uuid = UUID.randomUUID();
        byte[] bytes = new byte[16];
        System.arraycopy(longToBytes(uuid.getMostSignificantBits()), 0, bytes, 0, 8);
        System.arraycopy(longToBytes(uuid.getLeastSignificantBits()), 0, bytes, 8, 8);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
                .substring(0, 11); // 截取前11字符（如 "QkL3hYt7TdM"）
    }

    private static byte[] longToBytes(long value) {
        byte[] result = new byte[8];
        for (int i = 7; i >= 0; i--) {
            result[i] = (byte)(value & 0xFF);
            value >>= 8;
        }
        return result;
    }
}
