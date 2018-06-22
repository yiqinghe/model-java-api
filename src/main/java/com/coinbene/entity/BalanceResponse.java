package com.coinbene.entity;

import java.util.List;

/**
 * Created by caigaonian870 on 18/6/21.
 */
public class BalanceResponse {

    private  String status;
    private  String account;
    private  long timestamp;
    private List<Balance> balance;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public List<Balance> getBalance() {
        return balance;
    }

    public void setBalance(List<Balance> balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "BalanceResponse{" +
                "status='" + status + '\'' +
                ", account='" + account + '\'' +
                ", timestamp=" + timestamp +
                ", balance=" + balance +
                '}';
    }

    public class Balance{
        private  String asset;
        private  String available;
        private  String reserved;
        private  String total;

        public String getAsset() {
            return asset;
        }

        public void setAsset(String asset) {
            this.asset = asset;
        }

        public String getAvailable() {
            return available;
        }

        public void setAvailable(String available) {
            this.available = available;
        }

        public String getReserved() {
            return reserved;
        }

        public void setReserved(String reserved) {
            this.reserved = reserved;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        @Override
        public String toString() {
            return "Balance{" +
                    "asset='" + asset + '\'' +
                    ", available='" + available + '\'' +
                    ", reserved='" + reserved + '\'' +
                    ", total='" + total + '\'' +
                    '}';
        }
    }
}
