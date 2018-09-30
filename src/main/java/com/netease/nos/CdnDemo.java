package com.netease.nos;

import com.netease.nos.http.HttpHeaders;
import com.netease.nos.http.HttpHelper;
import com.netease.nos.module.PurgeUrlBody;
import com.netease.nos.utils.DateUtil;
import com.netease.nos.utils.JsonUtil;
import com.netease.nos.utils.Md5Util;
import com.netease.nos.utils.SignatureUtil;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by future on 2018/9/30
 */
public class CdnDemo {
    // 用户的ak
    private static final String ak = "09af6e2b6d584072bfeea4a3cf265c0c";
    // 用户的sk
    private static final String sk = "45955382fc4b4092864db49704eb29d2";
    // 需要Purge的域名
    private static final String domainName = "redirect.nosdn.127.net";

    // cdn endpoint
    private static final String endPoint = "ncdn-eastchina1.126.net";

    public static void main(String[] args) throws Exception {


        String date = DateUtil.getRFCDateFromLong(System.currentTimeMillis());

        // 构建purge body
        PurgeUrlBody purgeUrlBody = new PurgeUrlBody();
        List<String> purgeFileUrls = new LinkedList<>();
        purgeFileUrls.add("http://" + domainName + "/aa");// 需要加入http scheme
        purgeUrlBody.setFileUrl(purgeFileUrls);

        String body = JsonUtil.toJsonStr(purgeUrlBody);
        String md5 = Md5Util.getMD5Str(body);


        String purgeUri = "/domain/" + domainName + "/purge";
        /**
         * 计算StringToSign，如果这里的md5传参了，那么header中也需要加入，否者会报签名出错
         */
        String s2s = SignatureUtil.getString2Sign("POST", md5, HttpHeaders.DEFAULT_CONTENT_TYPE, date, SignatureUtil.getResoucePath(purgeUri, null));
        /**
         * 通过sk, s2s 计算签名
         */
        String signature = SignatureUtil.signString(sk, s2s);
        /**
         * 认证信息header拼接, NCDN ak:signature
         */
        String authInfo = SignatureUtil.getAuthInfo(ak, signature);

        /**
         * 设置header值,如果md5需要check，那么这里也需要传入
         */
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put(HttpHeaders.HEADER_AUTHORIZATION, authInfo);
        headerMap.put(HttpHeaders.HEADER_DATE, date);
        headerMap.put(HttpHeaders.HEADER_CONTENT_TYPE, HttpHeaders.DEFAULT_CONTENT_TYPE);
        headerMap.put(HttpHeaders.HEADER_CONTENT_MD5, md5);


        HttpHelper httpHelper = new HttpHelper(endPoint);


        // 发送http请求
        HttpResponse httpResponse = httpHelper.post(purgeUri, headerMap, null, body);
        System.out.println(httpResponse.getStatusLine().getStatusCode());// 202,即为正确的请求

        // response的流要记得关闭
        EntityUtils.consume(httpResponse.getEntity());

    }
}
