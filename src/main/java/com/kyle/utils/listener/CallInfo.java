package com.kyle.utils.listener;

/**
 * 其他调用信息
 *
 * @author: hehongbo
 * @date 2018/7/2
 */
public class CallInfo {
    /**
     * 请求时间
     */
    private Long requestTime;

    /**
     * 响应时间
     */
    private Long responseTime;

    /**
     * 状态码
     */
    private String statusCode;

    public Long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Long responseTime) {
        this.responseTime = responseTime;
    }

    public Long getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Long requestTime) {
        this.requestTime = requestTime;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
}
