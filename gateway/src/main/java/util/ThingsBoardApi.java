package util;

import com.alibaba.fastjson.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tangjialiang on 2017/10/18.
 *
 * thingsboard的相关api接口
 */
public class ThingsBoardApi {

    private String host = "127.0.0.1" ;
    private int port = 123 ;
    private HttpClientHelper thingsboardHelper = new HttpClientHelper() ;

    public volatile  static ThingsBoardApi instance;

    private ThingsBoardApi(String host, int port) {
        this.host = host ;
        this.port = port ;
    }

    public static ThingsBoardApi getInstance(String host, int port){
        if(instance==null){
            synchronized (ThingsBoardApi.class){
                if(instance==null) instance = new ThingsBoardApi(host,port);
            }
        }
        instance.host = host;
        instance.port = port;
        return instance;
    }


    //  --------------- thingsboard提供的相关接口 ---------------

    /**
     * POST /api/v1/{deviceToken}/attributes
     * 发送设备的属性数据
     * @param token
     * @param deviceToken
     * @param msg
     * @return
     * @throws Exception
     */
    public void api_attributes(String token, String deviceToken, String msg) throws Exception{
        String address = "http://" + host + ":"+port+"/api/v1/"+deviceToken+"/attributes" ;

        Map<String, String> request_headers = new HashMap<String, String>() ;
        request_headers.put("Content-Type", "application/json") ;
        request_headers.put("Accept", "application/json") ;
        request_headers.put("X-Authorization", "Bearer "+token) ;

        // 消息体（为post请求）
        JSONObject msgjson = JSONObject.parseObject(msg) ;

        // 发送这个请求
        String response = thingsboardHelper.sendPost(address, request_headers, msgjson);
    }

    /**
     * POST /api/v1/{deviceToken}/telemetry
     * 发送设备的实时数据
     * @param deviceToken
     * @param msg
     * @return
     * @throws Exception
     */
    public void api_telemetry(String token, String deviceToken, String msg) throws Exception {
        String address = "http://" + host + ":"+port+"/api/v1/"+deviceToken+"/telemetry" ;

        Map<String, String> request_headers = new HashMap<String, String>() ;
        request_headers.put("Content-Type", "application/json") ;
        request_headers.put("Accept", "application/json") ;
        request_headers.put("X-Authorization", "Bearer "+token) ;

        // 消息体（为post请求）
        JSONObject msgjson = JSONObject.parseObject(msg) ;

        // 发送这个请求
        String response = thingsboardHelper.sendPost(address, request_headers, msgjson);

        // 解析返回json
        // nothing

        return  ;
    }

    /**
     * POST /api/device
     * 创建一个设备
     * @param token
     * @param deviceName
     * @return
     * @throws Exception 存在相同名字出现400
     */
    public String api_device(String token, String deviceName, String deviceType) throws Exception{
        // 构建访问地址
        String address = "http://" + host + ":"+port+"/api/device" ;

        // 构建http请求
        Map<String, String> request_headers = new HashMap<String, String>() ;
        request_headers.put("Content-Type", "application/json") ;
        request_headers.put("Accept", "application/json") ;
        request_headers.put("X-Authorization", "Bearer "+token) ;

        // 消息体（为post请求）
        JSONObject msgjson = new JSONObject();
        String name  = URLEncoder.encode(deviceName,"utf-8");
        msgjson.put("name", name);
        msgjson.put("type", deviceType) ;

        // 发送这个请求
        String response = thingsboardHelper.sendPost(address, request_headers, msgjson);

        // 解析返回json
        JSONObject jsonObject = JSONObject.parseObject(response);
        JSONObject id = (JSONObject)jsonObject.get("id");
        String deviceId = (String)id.get("id") ;

        System.out.println("get deviceId: " + deviceId) ;
        return deviceId ;
    }

    /**
     * POST /api/auth/login
     * 获取账号的token
     * @param username
     * @param password
     * @return
     * @throws Exception
     */
    public String api_token(String username, String password) throws Exception {
        // 构建访问地址
        String address = "http://" + host + ":"+port+"/api/auth/login" ;

        // 构建http请求
        Map<String, String> request_headers = new HashMap<String, String>() ;
        request_headers.put("Content-Type", "application/json") ;
        request_headers.put("Accept", "application/json") ;

        // 消息体（为post请求）
        JSONObject msgjson = new JSONObject();
        msgjson.put("username", "tenant@thingsboard.org");
        msgjson.put("password", "tenant");

        // 发送这个请求
        String response = thingsboardHelper.sendPost(address, request_headers, msgjson);

        // 解析返回json
        JSONObject jsonObject = JSONObject.parseObject(response);
        String token = (String)jsonObject.get("token") ;
        return token ;
    }

    /**
     * GET /api/device/{deviceId}/credentials"
     * 获取设备的accessToken
     * @param token
     * @param deviceId
     * @return
     */
    public String api_accessToken(String token, String deviceId) {
        // 构建访问地址
        String address = "http://" + host + ":"+port+"/api/device/"+deviceId+"/credentials" ;

        // 构建http请求
        Map<String, String> request_headers = new HashMap<String, String>() ;
        request_headers.put("Content-Type", "application/json") ;
        request_headers.put("Accept", "application/json") ;
        request_headers.put("X-Authorization", "Bearer "+token) ;

        // 消息体（为post请求）
        // nothing

        // 发送这个请求
        String response = thingsboardHelper.sendGet(address, request_headers);

        // 解析返回json
        JSONObject jsonObjec  = JSONObject.parseObject(response);
        String access_token = (String)jsonObjec.get("credentialsId") ;

        return access_token ;
    }
}
