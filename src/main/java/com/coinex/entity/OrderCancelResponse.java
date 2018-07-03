package com.coinex.entity;

public class OrderCancelResponse {
    private  String status;
    private  String msg;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "OrderCancelResponse{" +
                "status='" + status + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}

