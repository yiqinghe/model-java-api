package com.coinbene;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by caigaonian870 on 18/6/21.
 */
public class UtilTest {
    @Test
    public void buildMd5Sign() throws Exception {
        Map<String,Object> paras =new HashMap<>();
        paras.put("apiid","ecc31028073bfdd44a75cd42d12e8201");
        paras.put("secret","783b66ca8b774e4d8ca59f4f3aadbc93");
        paras.put("timestamp",11223112231L);
        paras.put("account","exchange");
        String signStr = Util.buildMd5Sign(paras);
        System.out.println(signStr);
        Assert.assertTrue(signStr.equals("ACCOUNT=EXCHANGE&APIID=ECC31028073BFDD44A75CD42D12E8201&SECRET=783B66CA8B774E4D8CA59F4F3AADBC93&TIMESTAMP=11223112231"));

    }

    @Test
    public void string2MD5() throws Exception {
        Map<String,Object> paras =new HashMap<>();
        paras.put("apiid","ecc31028073bfdd44a75cd42d12e8201");
        paras.put("secret","783b66ca8b774e4d8ca59f4f3aadbc93");
        paras.put("timestamp",11223112231L);
        paras.put("account","exchange");
        String signStr = Util.buildMd5Sign(paras);
        System.out.println(signStr);

        String sign = Util.string2MD5(signStr);
        System.out.println("sign:{}"+sign);
        Assert.assertTrue(sign.equals("4ef3198c37a9702c1dd847dbbcdd764a"));

    }

    @Test
    public void doGetRequest(){
        Util.doGetRequest(Api.market_url+"ticker?symbol=btcusdt");
    }

    @Test
    public void buildPostJson(){
        Map<String,Object> paras =new HashMap<>();
        paras.put("apiid","ecc31028073bfdd44a75cd42d12e8201");
        paras.put("secret","783b66ca8b774e4d8ca59f4f3aadbc93");
        paras.put("timestamp",11223112231L);
        paras.put("account","exchange");
        String json = Util.buildPostJson(paras);
        System.out.println("json:{}"+json);

    }

}