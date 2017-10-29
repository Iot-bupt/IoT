package data;

import config.Config;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Administrator on 2017/10/24.
 *
 */
public class CommonData {
    public LinkedBlockingQueue<String> rocketMQMsgCache;
    public ConcurrentHashMap<String,String> devicesTokens;
    public volatile static   CommonData  instance;
    private  CommonData(){
        try{
            rocketMQMsgCache  = new LinkedBlockingQueue();
            devicesTokens = new ConcurrentHashMap<String, String>();
            init();
        }catch(Exception e){

        }
    }

    public  static  CommonData getInstance(){
        if(instance==null){
            synchronized (CommonData.class){
                if(instance==null) instance = new CommonData();
            }
        }
        return instance;
    }



    protected void init() {
        // 初始化时，从持久化中加载信息
        Map<String, String> allDevices = RedisUtil.hmgetAll(Config.REDISDEVICESKEY);
        for(Map.Entry<String,String>  dev : allDevices.entrySet()) {
            devicesTokens.put(dev.getKey(), dev.getValue());
        }
    }

    @Override
    protected void finalize() throws Throwable {
        // 对内存数据进行持久化操作
        //RedisUtil.setAllDevices();
//        HashMap<String, String> map = new HashMap() ;
//
//        for(Map.Entry<String, String> et : devicesTokens.entrySet()) {
//            String uid = et.getKey() ;
//            String token = et.getValue() ;
//            map.put(uid, new Device(uid, token)) ;
//        }
//
//        RedisUtil.setAllDevices(map);
   }
}
