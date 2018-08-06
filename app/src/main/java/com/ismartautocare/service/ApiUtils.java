package com.ismartautocare.service;

/**
 * Created by programmer on 1/10/18.
 */

public class ApiUtils {
    public static final String BASE_URL = "http://43.229.78.108:3011/";
//        public static final String BASE_URL = "http://192.168.2.36:3011/";

    public static SOService getSOService() {

        return RetrofitClient.getClient(BASE_URL).create(SOService.class);
    }
}