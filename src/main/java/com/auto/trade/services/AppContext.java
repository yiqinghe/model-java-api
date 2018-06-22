package com.auto.trade.services;

/**
 * Created by gof on 18/6/18.
 */
public class AppContext {
    public static DataService dataService;

    public static DataService getDataService() {
        return dataService;
    }

    public static void setDataService(DataService dataService) {
        AppContext.dataService = dataService;
    }
}
