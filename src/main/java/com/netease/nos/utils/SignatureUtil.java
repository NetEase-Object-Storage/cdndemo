package com.netease.nos.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by future on 2018/9/30
 */
public class SignatureUtil {


    /**
     * Created by future on 2018/9/30
     */
    public static String getString2Sign(String httpMethod, String contentMD5, String contentType, String date, String resoucePath) {
        StringBuffer sb = new StringBuffer();
        sb.append(httpMethod).append("\n").append(contentMD5).append("\n").append(contentType).append("\n").append(date).append("\n");
        sb.append(resoucePath);
        return sb.toString();
    }

    private static final String HMAC_SHA256 = "HmacSHA256";


    public static String signString(String secretKey, String string2Sign) throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(), HMAC_SHA256);
        Mac mac = Mac.getInstance(HMAC_SHA256);
        mac.init(signingKey);
        return new String(Base64.encodeBase64(mac.doFinal(string2Sign.getBytes())));
    }

    public static String getAuthInfo(String accessKey, String signature) {
        return "NCDN " + accessKey + ":" + signature;
    }

    private static final List<String> SIGNED_PARAMETERS = Arrays.asList(new String[]{"enable", "disable", "pageSize",
            "pageNum", "dateFrom", "dateTo", "versioning", "type", "orderType", "sortField", "serviceType",
            "status", "isEnabled"});


    public static String getResoucePath(String uri, Map<String, String> urlparam) {
        String resourcePath = ((uri != null) ? uri : "");
        StringBuilder sb = new StringBuilder(resourcePath);

        if (urlparam != null) {
            String[] parameterNames = urlparam.keySet().toArray(new String[urlparam.size()]);
            Arrays.sort(parameterNames);
            int i = 0;
            for (String parameterName : parameterNames) {
                if (SIGNED_PARAMETERS.contains(parameterName)) {
                    String parameterValue = urlparam.get(parameterName);
                    //请求参数同时带键名、键值
                    if (parameterValue != null) {
                        if (i == 0) {
                            sb.append("?").append(parameterName + "=").append(parameterValue);
                        } else {
                            sb.append("&").append(parameterName + "=").append(parameterValue);
                        }
                        //请求参数只有键名，没有键值
                    } else {
                        if (i == 0) {
                            sb.append("?").append(parameterName);
                        } else {
                            sb.append("&").append(parameterName);
                        }
                    }
                    i++;
                }
            }
        }
        return sb.toString();
    }


}
