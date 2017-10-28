package data;

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
            BufferedReader br =  new BufferedReader(new InputStreamReader(new FileInputStream("/home/iot/IoT/persist")) );
            String line;
            while ((line=br.readLine())!=null) {
                String data[] = line.split(" ");
                if(data.length!=2) continue;
                devicesTokens.put(data[0],data[1]);
            }
            br.close();
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

    private boolean tjl = false ;

    protected void init() {
        if (!tjl) return ;

        // 初始化时，从持久化中加载信息
        HashMap<String, Device> allDevices = RedisUtil.getAllDevices();
        for(Device device : allDevices.values()) {
            devicesTokens.put(device.getuId(), device.getDeviceAccess()) ;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        if (!tjl) return ;

        // 对内存数据进行持久化操作
        //RedisUtil.setAllDevices();
        HashMap<String, Device> map = new HashMap() ;

        for(Map.Entry<String, String> et : devicesTokens.entrySet()) {
            String uid = et.getKey() ;
            String token = et.getValue() ;
            map.put(uid, new Device(uid, token)) ;
        }

        RedisUtil.setAllDevices(map);
    }
}
