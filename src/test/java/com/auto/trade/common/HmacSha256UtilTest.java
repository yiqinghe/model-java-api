package com.auto.trade.common;

import com.auto.model.entity.TradeStatus;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by gof on 18/6/24.
 */
public class HmacSha256UtilTest {
    @Test
    public void sha256_HMAC() throws Exception {
        String msg = "GET|/api/v2/markets|access_key=xxx&foo=bar&tonce=123456789";
        String secret="abc";
        String sign = HmacSha256Util.sha256_HMAC(msg,secret);
        Assert.assertTrue(sign.equals("704f773b6b26772fd82bd3a8115079fb4f71d7baa1aad6b2922e99b17ed95cdc"));

    }

}