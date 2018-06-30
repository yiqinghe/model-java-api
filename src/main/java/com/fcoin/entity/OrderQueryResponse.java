package com.fcoin.entity;

import java.util.Arrays;

public class OrderQueryResponse {
    private  String status;
    private  Data data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data{
        private  String id;
        private  String symbol;
        private  String type;
        private  String side;
        private  String price;
        private  String amount;
        private  String state;
        private  String executed_value;
        private  String fill_fees;
        private  String filled_amount;
        private  String created_at;
        private  String source;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getSide() {
            return side;
        }

        public void setSide(String side) {
            this.side = side;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getExecuted_value() {
            return executed_value;
        }

        public void setExecuted_value(String executed_value) {
            this.executed_value = executed_value;
        }

        public String getFill_fees() {
            return fill_fees;
        }

        public void setFill_fees(String fill_fees) {
            this.fill_fees = fill_fees;
        }

        public String getFilled_amount() {
            return filled_amount;
        }

        public void setFilled_amount(String filled_amount) {
            this.filled_amount = filled_amount;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "id='" + id + '\'' +
                    ", symbol='" + symbol + '\'' +
                    ", type='" + type + '\'' +
                    ", side='" + side + '\'' +
                    ", price='" + price + '\'' +
                    ", amount='" + amount + '\'' +
                    ", state='" + state + '\'' +
                    ", executed_value='" + executed_value + '\'' +
                    ", fill_fees='" + fill_fees + '\'' +
                    ", filled_amount='" + filled_amount + '\'' +
                    ", created_at='" + created_at + '\'' +
                    ", source='" + source + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "OrderQueryResponse{" +
                "status='" + status + '\'' +
                ", data=" + data +
                '}';
    }
}

