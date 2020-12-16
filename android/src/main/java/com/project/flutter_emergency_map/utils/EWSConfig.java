package com.project.flutter_emergency_map.utils;

/**
 * @author : youtian
 * @date : 2019/7/8 16:30
 */
public class EWSConfig {
    public static final String TOKEN = "Bearer d277145";

    /**
     * 授权类型，分为Basic和Token两种
     */
    public static final String AUTH_TYPE = "Token";
    /**
     * 授权用户id（请用户自己填写）
     */
    public static final String AUTH_CLIENT_ID = "";
    /**
     * 授权密码（请用户自己填写）
     */
    public static final String CLIENT_SECRET = "";

    /**
     * 授权服务端地址
     */
    public static final String TOKEN_URL = "http://10.84.1.101:9090/oauth/token";

    /**
     * 服务端地址
     */
    public static final String SERVICE_URL = "http://120.52.31.31:590/service/api/egis/base/v1";

    public static final String WRAS_URL = "http://172.18.24.144:18103/egis/business/v1";

    public static final String WRMS_URL = "http://172.18.2.74:18102/egis/business/v1";

    public static final String WRCS_URL = "http://172.18.24.144:18101/egis/business/v1";
    public static final String WRDS_URL = "http://172.18.24.144:18105/egis/business/v1";
    public static final String WCPS_URL = "http://172.18.24.144:18104/egis/business/v1";
    public static final String WCPS_WS_URL = "ws://172.18.24.144:18108/egis/business/v1/wcps/websocket";


}
