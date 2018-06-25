package com.ocx.entity;


public class OrderResponse {
    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    private Data data;

    @Override
    public String toString() {
        return "OrderQueryResponse{" +
                "data=" + data +
                '}';
    }

    public static class Data {
        private String id;
        private String side;
        private String ord_type;
        private String price;
        private String avg_price;
        private String state;
        private String state_i18n;
        private String volume;
        private String remaining_volume;
        private String executed_volume;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSide() {
            return side;
        }

        public void setSide(String side) {
            this.side = side;
        }

        public String getOrd_type() {
            return ord_type;
        }

        public void setOrd_type(String ord_type) {
            this.ord_type = ord_type;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getState_i18n() {
            return state_i18n;
        }

        public void setState_i18n(String state_i18n) {
            this.state_i18n = state_i18n;
        }

        public String getAvg_price() {
            return avg_price;
        }

        public void setAvg_price(String avg_price) {
            this.avg_price = avg_price;
        }

        public String getVolume() {
            return volume;
        }

        public void setVolume(String volume) {
            this.volume = volume;
        }

        public String getRemaining_volume() {
            return remaining_volume;
        }

        public void setRemaining_volume(String remaining_volume) {
            this.remaining_volume = remaining_volume;
        }

        public String getExecuted_volume() {
            return executed_volume;
        }

        public void setExecuted_volume(String executed_volume) {
            this.executed_volume = executed_volume;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "id='" + id + '\'' +
                    ", side='" + side + '\'' +
                    ", ord_type='" + ord_type + '\'' +
                    ", price='" + price + '\'' +
                    ", state='" + state + '\'' +
                    ", state_i18n='" + state_i18n + '\'' +
                    ", avg_price='" + avg_price + '\'' +
                    ", volume='" + volume + '\'' +
                    ", remaining_volume='" + remaining_volume + '\'' +
                    ", executed_volume='" + executed_volume + '\'' +
                    '}';
        }
    }
}

