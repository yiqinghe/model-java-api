package com.auto.model.common;

import com.auto.model.entity.QuantitativeResult;

/**
 * Created by caigaonian870 on 18/7/2.
 */
public interface ModelInterface {
    boolean init();
    QuantitativeResult periodStart() throws InterruptedException ;
}
