package com.auto.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auto.model.entity.*;
import com.auto.model.entity.Currency;
import com.auto.trade.entity.DepthData;
import com.auto.trade.entity.OrderPrice;
import com.coinbene.Util;
import com.ocx.entity.DepthResponse;
import com.ocx.entity.TickerResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

public class ApiOcx implements Api<Object> {
    private static final Logger log = LoggerFactory.getLogger(ApiOcx.class);

    private String access_key = "";
    private String secret_key = "";
    private String endpoint = "";
    private int timeout = 60;

    public ApiOcx(String access_key, String secret_key) {
        this.access_key = access_key;
        this.secret_key = secret_key;
        this.endpoint = "https://openapi.ocx.com";
        this.timeout = 60;
    }


    /**
     * 获取余额
     *
     * @param currency
     * @return
     */
    @Override
    public Balance getBalance(Currency currency) {
        return null;
    }

    /**
     * 获取交易深度
     * # Request
     * GET https://openapi.ocx.com/api/v2/depth?market_code=ethbtc
     * # Response
     * {
     * "data" : {
     * "timestamp" : 1529275554,
     * "asks" : [],
     * "bids" : []
     * }
     * }
     *
     * @param symbol
     * @return
     */
    @Override
    public DepthData getDepthData(TradeSymbol symbol) {
        DepthData depthData = new DepthData();

        long start = System.currentTimeMillis();
        String response = Util.doGetRequest(this.endpoint + "/api/v2/depth?market_code=" + getSymbol(symbol));
        long end = System.currentTimeMillis();
        log.info("getOrderPrice cost:{},getOrderPrice:{}", end - start, response);
        if (response != null) {
            JSONObject t_jo = JSON.parseObject(response);
            DepthResponse depthResponse = JSON.parseObject(t_jo.getString("data"), DepthResponse.class);
            System.out.println(depthResponse);
            if (depthResponse.getData() != null) {
                DepthResponse.Data td = depthResponse.getData();
                depthData.eventTime = td.getTimestamp();
//                depthData.bid_1_price = tickerPrice.getLast();
//                depthData.ask_1_price = tickerPrice.getLast();
            } else {
                log.error("ticketResponse.asks bids null");
            }
        }
        return depthData;
    }

    /**
     * 获取实时价格
     * # Request
     * GET https://openapi.ocx.com/api/v2/tickers
     * <p>
     * # Response
     * {
     * "data": [{
     * "low": "0.051",
     * "high": "0.0537",
     * "last" : "0.053",
     * "market_code" : "ethbtc",
     * "open" : "0.0517",
     * "volume" : "454.3",
     * "timestamp" : 1529275425,
     * "sell": "0.07914108",
     * "buy": "0.07902933",
     * }]
     * }
     *
     * @param symbol
     * @return
     */
    @Override
    public OrderPrice getOrderPrice(TradeSymbol symbol) {
        OrderPrice orderPrice = new OrderPrice();

        long start = System.currentTimeMillis();
        String response = Util.doGetRequest(this.endpoint + "/api/v2/tickers" + getSymbol(symbol));
        long end = System.currentTimeMillis();
        log.info("getOrderPrice cost:{},getOrderPrice:{}", end - start, response);

        if (response != null) {

            TickerResponse tickerResponse = JSON.parseObject(response, TickerResponse.class);
            if (tickerResponse.getData() != null) {
                TickerResponse.Data t_d = tickerResponse.getData().get(0);
                orderPrice.price = t_d.getSell();
                orderPrice.eventTime = t_d.getTimestamp();
                orderPrice.symbol = t_d.getMarket_code();
                orderPrice.qty = t_d.getVolume();
            } else {
                log.error("getOrderPrice null");
            }
        }
        return orderPrice;
    }

    /**
     * 发起交易，限价单
     *
     * @param order
     * @return
     */
    @Override
    public Order buy(Order order) {
        return null;
    }

    /**
     * 发起交易，限价单
     *
     * @param order
     * @return
     */
    @Override
    public Order sell(Order order) {
        return null;
    }

    /**
     * 发起交易，市场单GTC
     *
     * @param order
     * @return
     */
    @Override
    public Order buyMarket(Order order) {
        return null;
    }

    /**
     * 发起交易，市场单
     *
     * @param order
     * @return
     */
    @Override
    public Order sellMarket(Order order) {
        return null;
    }

    /**
     * 取消订单
     *
     * @param order
     * @return
     */
    @Override
    public Order cancel(Order order) {
        return null;
    }

    /**
     * 查询订单
     * # Request
     * GET https://openapi.ocx.com/api/v2/orders
     * # Response
     * {
     * "data": [{
     * "id": 3,
     * "side": "sell",
     * "ord_type": "limit",
     * "price": "0.0",
     * "avg_price": "0.0",
     * "state": "wait",
     * "state_i18n": "WAIT",
     * "market_code": "ethbtc",
     * "market_name": "ETH/BTC",
     * "market_base_unit": "eth",
     * "market_quote_unit": "btc",
     * "created_at": "2018-06-17T22:57:00Z",
     * "volume": "0.1",
     * "remaining_volume": "0.1",
     * "executed_volume": "0.0"
     * }]
     * }
     *
     * @param order
     * @return
     */
    @Override
    public Order queryOrder(Order order) {
        long start = System.currentTimeMillis();
        String this_path = "/api/v2/orders";

        Map<String, String> params = new HashMap<>();
        String this_params_url = this.signed_params("GET", this_path, params, this.secret_key);
        String response = Util.doGetRequest(this.endpoint + this_path + "?" + this_params_url);
        long end = System.currentTimeMillis();
        log.info("query cost:{},response:{}", end - start, response);

        if (response != null) {
            System.out.println(response);
        }
        return null;
    }

    /**
     * 组装是否能发起交易的上下文，例如能深度数据中，买一卖一价差很大，并且返回发起交易的价格
     *
     * @param depthData
     * @return
     */
    @Override
    public TradeContext buildTradeContext(DepthData depthData) {
        return null;
    }

    /**
     * 获取每次买单的目标币的交易数量
     *
     * @param originalTargetAmount
     * @return
     */
    @Override
    public BigDecimal getTargetAmountForBuy(BigDecimal originalTargetAmount) {
        return null;
    }

    /**
     * 获取每次卖单的目标币的交易数量
     *
     * @param originalTargetAmount
     * @return
     */
    @Override
    public BigDecimal getTargetAmountForSell(BigDecimal originalTargetAmount) {
        return null;
    }

    class HMACSHA256 {

        /**
         * 将加密后的字节数组转换成字符串
         *
         * @param b 字节数组
         * @return 字符串
         */
        private String byteArrayToHexString(byte[] b) {
            StringBuilder hs = new StringBuilder();
            String stmp;
            for (int n = 0; b != null && n < b.length; n++) {
                stmp = Integer.toHexString(b[n] & 0XFF);
                if (stmp.length() == 1)
                    hs.append('0');
                hs.append(stmp);
            }
            return hs.toString().toLowerCase();
        }

        /**
         * sha256_HMAC加密
         *
         * @param message 消息
         * @param secret  秘钥
         * @return 加密后字符串
         */
        public String sha256_HMAC(String message, String secret) {
            String hash = "";
            try {
                Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
                SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
                sha256_HMAC.init(secret_key);
                byte[] bytes = sha256_HMAC.doFinal(message.getBytes());
                hash = byteArrayToHexString(bytes);
                System.out.println(hash);
            } catch (Exception e) {
                System.out.println("Error HmacSHA256 ===========" + e.getMessage());
            }
            return hash;
        }
    }

    public String signed_params(String http_head, String path, Map<String, String> params, String secretKey) {
        Map<String, String> _t_p = params;
        _t_p = this._format_params(_t_p);
        String url_params = ApiOcx.formatUrlMap(_t_p, false, false);
        String payload = String.format("%s|%s|%s", http_head, path, url_params);
        String signature = signed_challenge(payload, secretKey);

//        _t_p.put("signature",signature);
        String signed_url_params = url_params + "&signature=" + signature;
        return signed_url_params;
    }

    public String signed_challenge(String challenge, String secret) {
        String signature = new HMACSHA256().sha256_HMAC(challenge, secret);
        return signature;
    }


    private Map<String, String> _format_params(Map<String, String> params) {
        if (params.getOrDefault("access_key", "") != this.access_key) {
            params.put("access_key", this.access_key);
        }
        if (params.getOrDefault("tonce", "0") == "0") {
            params.put("tonce", System.currentTimeMillis() + "");
        }
        return params;
    }

    public static String getSymbol(TradeSymbol symbol) {
        String target = StringUtils.lowerCase(symbol.targetCurrency.getCurrency());
        String base = StringUtils.lowerCase(symbol.baseCurrency.getCurrency());
        return target + base;
    }

    /**
     * 方法用途: 对所有传入参数按照字段名的Unicode码从小到大排序（字典序），并且生成url参数串<br>
     * 实现步骤: <br>
     *
     * @param paraMap    要排序的Map对象
     * @param urlEncode  是否需要URLENCODE
     * @param keyToLower 是否需要将Key转换为全小写
     *                   true:key转化成小写，false:不转化
     * @return
     */
    public static String formatUrlMap(Map<String, String> paraMap, boolean urlEncode, boolean keyToLower) {
        String buff = "";
        Map<String, String> tmpMap = paraMap;
        try {
            List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(tmpMap.entrySet());
            // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
            Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {

                @Override
                public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                    return (o1.getKey()).toString().compareTo(o2.getKey());
                }
            });
            // 构造URL 键值对的格式
            StringBuilder buf = new StringBuilder();
            for (Map.Entry<String, String> item : infoIds) {
                if (StringUtils.isNotBlank(item.getKey())) {
                    String key = item.getKey();
                    String val = item.getValue();
                    if (urlEncode) {
                        val = URLEncoder.encode(val, "utf-8");
                    }
                    if (keyToLower) {
                        buf.append(key.toLowerCase() + "=" + val);
                    } else {
                        buf.append(key + "=" + val);
                    }
                    buf.append("&");
                }

            }
            buff = buf.toString();
            if (buff.isEmpty() == false) {
                buff = buff.substring(0, buff.length() - 1);
            }
        } catch (Exception e) {
            return null;
        }
        return buff;
    }


    public static void main(String[] args) {
        ApiOcx o = new ApiOcx("SdD4k9lx86mAITOEUf4CExZHcUI9lYZiSBBZfWLg", "3CSutxsHQ31btYI4bM1xfoNo7RNo9Lr8F5ShFLv8");
//        o.getDepthData(new TradeSymbol(Currency.eth, Currency.usdt));
//        o.getOrderPrice(new TradeSymbol(Currency.eth, Currency.usdt));
//        Map<String, String> t = new HashMap<>();
//        t.put("access_key", "xxx");
//        t.put("tonce", "123456789");
//        t.put("foo", "bar");
//        ApiOcx oo = new ApiOcx("xxx", "abc");
//        String xx = oo.signed_params("GET", "/api/v2/markets", t, oo.secret_key);
//        System.out.println(xx);
        o.queryOrder(new Order(new TradeSymbol(Currency.bnb,Currency.bnb), TradeType.buy,"3.2","22"));
    }
}
