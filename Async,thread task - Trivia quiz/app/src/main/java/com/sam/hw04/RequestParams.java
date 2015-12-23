package com.sam.hw04;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * HW04
 * Sam Painter and Praveen Suenani
 * RequestParams.java
 */
public class RequestParams {
    String baseUrl;
    HashMap<String, String> params = new HashMap<String, String>();

    public RequestParams(String method, String baseUrl) {
        super();
        this.baseUrl = baseUrl;
    }

    public void addParam(String key, String value) {
        params.put(key, value);
    }

    public String getEncodedParams() {
        StringBuilder s = new StringBuilder();
        for(String key: params.keySet()) {
            try {
                String value = URLEncoder.encode(params.get(key), "UTF-8");
                if(s.length() > 0){
                    s.append("&");
                }
                s.append(key).append("=").append(value);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return s.toString();
    }

    public String getEncodedUrl() {
        return this.baseUrl + "?" + getEncodedParams();
    }

}
