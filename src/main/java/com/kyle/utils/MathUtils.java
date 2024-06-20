package com.kyle.utils;

import java.math.BigDecimal;

/**
 * @author carroll
 * @Date 2017-07-25 18:06
 **/
public class MathUtils {
    private static final int HUNDRED = 100;

    /**
     * 计算百分比（四舍五入）
     * @param dividend 被除数
     * @param divisor 除数
     * @param scale 小数位数
     * @return
     */
    public static BigDecimal percent(Long dividend, Long divisor, int scale){
        return percent(dividend, divisor, scale, BigDecimal.ROUND_HALF_DOWN);
    }

    /**
     * 计算百分比
     * @param dividend 被除数
     * @param divisor 除数
     * @param scale 小数位数
     * @param roundingMode 进位操作
     * @return
     */
    public static BigDecimal percent(Long dividend, Long divisor, int scale, int roundingMode){
        if(dividend != 0 && divisor != 0){
            return BigDecimal.valueOf(dividend).multiply(BigDecimal.valueOf(HUNDRED)).divide(BigDecimal.valueOf(divisor), scale, roundingMode);
        } else {
            return BigDecimal.valueOf(0);
        }
    }
}
