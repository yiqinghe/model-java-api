package com.auto.model.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by gof on 18/6/30.
 */
public class Util {
    private static final Logger log = LoggerFactory.getLogger(Util.class);

    public static void threadSleep(long millSecs){
        try {
            Thread.sleep(millSecs);
        } catch (InterruptedException e) {
            log.error("threadSleep ,e:{}",e);
        }
    }
}
