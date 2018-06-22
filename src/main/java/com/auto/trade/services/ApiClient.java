package com.auto.trade.services;

import com.auto.trade.common.Constants;
import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import org.springframework.stereotype.Service;

/**
 * Created by gof on 18/1/28.
 */
@Service
public class ApiClient {

    private static BinanceApiClientFactory factory;
    public static BinanceApiRestClient client;

    public static BinanceApiRestClient getBinanceClientInstant(){
        if(client == null){
            synchronized (ApiClient.class){
                if(client==null){
                    BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(Constants.APIKEY, Constants.SECRET);
                    client = factory.newRestClient();
                }
            }

        }
        return client;
    }

}
