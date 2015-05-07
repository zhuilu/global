package com.global.http;


import org.json.JSONObject;

public abstract class HJResponse {

    public abstract void response(HJResponse response);

    public abstract void onError(String errRes);

    public JSONObject head;
    public String     result       = "";
    public String     errorCode    = "";
    public String     errorMessage = "";
    public String     describe     = "";
    public long       callTime;
    public JSONObject body;

    public JSONObject getHead() {
        return head;
    }

    public void setHead(JSONObject head) {
        this.head = head;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public long getCallTime() {
        return callTime;
    }

    public void setCallTime(long callTime) {
        this.callTime = callTime;
    }

    public JSONObject getBody() {
        return body;
    }

    public void setBody(JSONObject body) {
        this.body = body;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String sign;

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
