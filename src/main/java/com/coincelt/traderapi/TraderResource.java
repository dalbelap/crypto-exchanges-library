package com.coincelt.traderapi;

import com.coincelt.traderapi.api.APIPoloniex;
import com.coincelt.traderapi.api.ApiBitfinex;
import com.coincelt.traderapi.api.ApiKraken;
import okhttp3.OkHttpClient;

/**
 * Static connection to an Exchange API
 */
public class TraderResource {

    private OkHttpClient client;
    private APIPoloniex apiPoloniex;
    private ApiBitfinex apiBitfinex;
    private ApiKraken apiKraken;

    public TraderResource(){
        client = new OkHttpClient();
    }

    /**
     * Returns an object for access to the API Poloniex
     * @return APIPoloniex object
     */
    public APIPoloniex getAPIPoloniex(){
        if(apiPoloniex == null){
            apiPoloniex = new APIPoloniex(client);
        }

        return apiPoloniex;
    }

    public ApiBitfinex getApiBitfinex(){
        if(apiBitfinex == null){
            apiBitfinex = new ApiBitfinex(client);
        }

        return apiBitfinex;
    }

    public ApiKraken getApiKraken(){
        if(apiKraken == null){
            apiKraken = new ApiKraken(client);
        }

        return apiKraken;
    }
}
