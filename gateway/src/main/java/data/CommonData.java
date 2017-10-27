package data;

import java.io.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Administrator on 2017/10/24.
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

}
