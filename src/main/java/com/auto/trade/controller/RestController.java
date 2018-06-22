package com.auto.trade.controller;

import com.auto.trade.response.AccountResponse;
import com.auto.trade.services.ApiClient;
import com.auto.trade.services.DataService;
import com.binance.api.client.domain.account.Account;
import com.binance.api.client.domain.account.NewOrderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.binance.api.client.domain.account.NewOrder.marketBuy;
import static com.binance.api.client.domain.account.NewOrder.marketSell;

@org.springframework.web.bind.annotation.RestController
public class RestController {
    private static final Logger log = LoggerFactory.getLogger(RestController.class);
    @Autowired
    private DataService dataService;


    //@RequestMapping("/buy")
    public String buy(@RequestParam(value="symbol", defaultValue="symbol") String symbol,
                      @RequestParam(value="quantity", defaultValue="0.01") String quantity) {

        NewOrderResponse newOrderResponse = ApiClient.getBinanceClientInstant().newOrder(marketBuy("ETHUSDT", quantity));
        log.info(newOrderResponse.getClientOrderId());
        return newOrderResponse.getClientOrderId();
    }
    //@RequestMapping("/sell")
    public String sell(@RequestParam(value="symbol", defaultValue="symbol") String symbol,
                       @RequestParam(value="quantity", defaultValue="0.01") String quantity) {
        NewOrderResponse newOrderResponse = ApiClient.getBinanceClientInstant().newOrder(marketSell("ETHUSDT", quantity));
        log.info(newOrderResponse.getClientOrderId());
        return newOrderResponse.getClientOrderId();
    }
    @RequestMapping("/getAccountBalance")
    public AccountResponse getAccountBalance() throws InterruptedException {

        Account account = ApiClient.getBinanceClientInstant().getAccount();
        //log.info("account balances {}",account.getBalances());
        log.info("account ETH balance {}",account.getAssetBalance("ETH").getFree());
        log.info("account USDT balance {}",account.getAssetBalance("USDT").getFree());
        log.info("account BNB balance {}",account.getAssetBalance("BNB").getFree());
        AccountResponse accountResponse  = new AccountResponse();
        accountResponse.setBtc(account.getAssetBalance("BTC").getFree());
        accountResponse.setBnb(account.getAssetBalance("BNB").getFree());
        accountResponse.setEth(account.getAssetBalance("ETH").getFree());
        accountResponse.setUsdt(account.getAssetBalance("USDT").getFree());
        log.info("accountResponse {}",accountResponse);

        return accountResponse;
    }

}
