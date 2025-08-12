package com.kyle.utils;

import java.math.BigDecimal;

/**
 * 转换工具类
 *
 * @author: carroll.he
 * @date 2021/9/18 
 */
public class ParserUtil {
    public static final BigDecimal HUNDRED = new BigDecimal("100");

    public static Integer parseInteger(String data) {
        if (StringUtil.isEmpty(data)) {
            return null;
        }
        try {
            return Integer.parseInt(data);
        } catch (Exception e) {
            return null;
        }
    }

    public static Long parseLong(String data) {
        if (StringUtil.isEmpty(data)) {
            return null;
        }
        try {
            return Long.parseLong(data);
        } catch (Exception e) {
            return null;
        }
    }

    public static BigDecimal parseBigdecimal(String data) {
        if (StringUtil.isEmpty(data)) {
            return null;
        }
        try {
            return new BigDecimal(data);
        } catch (Exception e) {
            return null;
        }
    }

    public static Long yuanToFen(String data) {
        BigDecimal yuan = parseBigdecimal(data);
        if (yuan == null) {
            return null;
        }
        return yuan.multiply(HUNDRED).longValue();
    }

    public static Long yuanToFen(BigDecimal yuan) {
        if (yuan == null) {
            return null;
        }
        return yuan.multiply(HUNDRED).longValue();
    }

    public static BigDecimal fenToYuan(Long fen) {
        if (fen == null) {
            return null;
        }
        return BigDecimal.valueOf(fen).divide(HUNDRED, 2, BigDecimal.ROUND_HALF_UP);
    }

    public static String ifNull(String v1, String v2) {
        return StringUtil.isEmpty(v1) ? v2 : v1;
    }

    public static Long ifNull(Long v1, Long v2) {
        return v1 == null ? v2 : v1;
    }
}
