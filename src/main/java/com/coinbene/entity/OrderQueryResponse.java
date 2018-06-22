package com.coinbene.entity;

/**
 * Created by caigaonian870 on 18/6/21.
 */
public class OrderQueryResponse {

    private  String symbol;

    private  String status;
    private  long timestamp;

    private  Order order;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "OrderQueryResponse{" +
                "symbol='" + symbol + '\'' +
                ", status='" + status + '\'' +
                ", timestamp=" + timestamp +
                ", order=" + order +
                '}';
    }

    public class Order{
        private  String orderid;
        private  String orderstatus;
        private  String symbol;
        private  String type;
        private  String price;
        private  String orderquantity;
        private  String filledquantity;
        private  String filledamount;
        private  String averageprice;
        private  String fees;
        private  String createtime;
        private  String lastmodified;

        public String getOrderid() {
            return orderid;
        }

        public void setOrderid(String orderid) {
            this.orderid = orderid;
        }

        public String getOrderstatus() {
            return orderstatus;
        }

        public void setOrderstatus(String orderstatus) {
            this.orderstatus = orderstatus;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getOrderquantity() {
            return orderquantity;
        }

        public void setOrderquantity(String orderquantity) {
            this.orderquantity = orderquantity;
        }

        public String getFilledquantity() {
            return filledquantity;
        }

        public void setFilledquantity(String filledquantity) {
            this.filledquantity = filledquantity;
        }

        public String getFilledamount() {
            return filledamount;
        }

        public void setFilledamount(String filledamount) {
            this.filledamount = filledamount;
        }

        public String getAverageprice() {
            return averageprice;
        }

        public void setAverageprice(String averageprice) {
            this.averageprice = averageprice;
        }

        public String getFees() {
            return fees;
        }

        public void setFees(String fees) {
            this.fees = fees;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public String getLastmodified() {
            return lastmodified;
        }

        public void setLastmodified(String lastmodified) {
            this.lastmodified = lastmodified;
        }

        @Override
        public String toString() {
            return "Order{" +
                    "orderid='" + orderid + '\'' +
                    ", orderstatus='" + orderstatus + '\'' +
                    ", symbol='" + symbol + '\'' +
                    ", type='" + type + '\'' +
                    ", price='" + price + '\'' +
                    ", orderquantity='" + orderquantity + '\'' +
                    ", filledquantity='" + filledquantity + '\'' +
                    ", filledamount='" + filledamount + '\'' +
                    ", averageprice='" + averageprice + '\'' +
                    ", fees='" + fees + '\'' +
                    ", createtime='" + createtime + '\'' +
                    ", lastmodified='" + lastmodified + '\'' +
                    '}';
        }
    }

}
