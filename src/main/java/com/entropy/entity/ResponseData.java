package com.entropy.entity;

public class ResponseData {
    private Boolean success; //响应是否成功
    private String msg; //响应信息

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "ResponseData{" +
                "success=" + success +
                ", msg='" + msg + '\'' +
                '}';
    }

    //响应成功
    public static ResponseData OK() {
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(true);
        responseData.setMsg("响应成功");
        return responseData;
    }

    //响应失败
    public static ResponseData FAILED() {
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(false);
        responseData.setMsg("响应失败");
        return responseData;
    }
}
