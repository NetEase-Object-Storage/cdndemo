package com.netease.nos.http;

/**
 * Created by future on 2018/9/30
 */


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class HttpHelper {
    private static final Logger logger = Logger.getLogger(HttpHelper.class);

    private HttpClient httpClient;
    private String host;

    public HttpHelper(String host) {
        this.host = host;
        httpClient = HttpClients.createDefault();
    }

    public HttpResponse post(String uri, Map<String, String> header, Map<String, String> urlParam, String body) throws IOException {
        HttpPost httpPost = new HttpPost(getCompleteURL(uri, urlParam));
        if (body != null) {
            httpPost.setEntity(new StringEntity(body, "UTF-8"));
        }
        header.forEach((key, value) -> {
            httpPost.setHeader(key, value);
        });
        HttpResponse httpResponse = httpClient.execute(httpPost);
        return httpResponse;
    }


    private String getCompleteURL(String uri, Map<String, String> urlParam) throws UnsupportedEncodingException {
        StringBuilder sb;
        sb = new StringBuilder("http://" + host);
        if (uri != null) {
            sb.append(uri);
        }
        if (urlParam != null) {
            String[] parameterNames = urlParam.keySet().toArray(new String[urlParam.size()]);
            int i = 0;
            for (String parameterName : parameterNames) {
                String parameterValue = urlParam.get(parameterName);
                if (parameterValue != null) {
                    if (i == 0) {
                        sb.append("?").append(parameterName + "=").append(HttpHelper.urlEncode(parameterValue));

                    } else {
                        sb.append("&").append(parameterName + "=").append(HttpHelper.urlEncode(parameterValue));
                    }
                    i++;
                } else {
                    if (i == 0) {
                        sb.append("?").append(parameterName);
                    } else {
                        sb.append("&").append(parameterName);
                    }
                    i++;
                }
            }
        }
        logger.debug("url = " + sb.toString());
        return sb.toString();
    }


    //引用自s3的编码
    public static String urlEncode(String s) throws UnsupportedEncodingException {
        if (s == null) return null;

        String encodedString = URLEncoder.encode(s, HttpHeaders.DEFAULT_ENCODING);
        return encodedString.replaceAll("\\+", "%20");
    }
}
