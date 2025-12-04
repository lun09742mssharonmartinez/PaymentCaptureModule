package com.paymentcapture.module.models;

/**
 * 支付数据模型
 */
public class PaymentData {
    
    private String type;        // 类型（扫码支付、链接支付等）
    private String url;         // 支付链接
    private String amount;      // 金额
    private String orderId;     // 订单号
    private long timestamp;     // 时间戳

    public PaymentData() {
        this.timestamp = System.currentTimeMillis();
    }

    public PaymentData(String type, String url) {
        this.type = type;
        this.url = url;
        this.timestamp = System.currentTimeMillis();
        parseUrl();
    }

    /**
     * 从 URL 中解析金额和订单号
     */
    private void parseUrl() {
        if (url == null) return;

        // 解析金额
        String[] amountKeys = {"money=", "amount=", "totalAmount="};
        for (String key : amountKeys) {
            int index = url.indexOf(key);
            if (index != -1) {
                int start = index + key.length();
                int end = url.indexOf("&", start);
                if (end == -1) end = url.length();
                amount = url.substring(start, end);
                break;
            }
        }

        // 解析订单号
        String[] orderKeys = {"orderId=", "orderNo=", "tradeNo="};
        for (String key : orderKeys) {
            int index = url.indexOf(key);
            if (index != -1) {
                int start = index + key.length();
                int end = url.indexOf("&", start);
                if (end == -1) end = url.length();
                orderId = url.substring(start, end);
                break;
            }
        }

        if (amount == null) amount = "未知";
        if (orderId == null) orderId = "未知";
    }

    // Getters and Setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        parseUrl();
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
