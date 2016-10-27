package com.coincelt.traderapi.api;

import okhttp3.OkHttpClient;

/**
 * General Public API
 */
public class GeneralApi {

    protected OkHttpClient client;

    public GeneralApi(OkHttpClient client){
        this.client = client;
    }

}
