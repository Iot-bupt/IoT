package consumer;

import com.alibaba.fastjson.JSONObject;
import data.CommonData;
import data.Device;
import handler.DeviceMsgReceiver;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Administrator on 2017/10/24.
 */
public class LocalConsumer implements  Consumer{
    private final DeviceMsgReceiver receiver;
    int n;
    private final Thread[] threads;

    public LocalConsumer(int n){
        receiver = new DeviceMsgReceiver();
        this.n = n;
        threads = new Thread[n];
    }

    public void init(){


    }
    public void startConsume(){
        for(int i=0;i<n;i++){
            if(threads[i]!=null)threads[i].stop();
            threads[i] =  new Thread(new Runnable() {
                public void run() {
                    LinkedBlockingQueue<String> cache =  CommonData.getInstance().rocketMQMsgCache;
                    while(true){
                        try{
                            String msg = cache.take();
                            JSONObject json = JSONObject.parseObject(msg);
                            String uId = json.getString("uId");
                            String dataType = json.getString("dataType");
                            String info = json.getString("info");
                            String deviceName = (String)json.get("deviceName") ;

                            if(uId==null||dataType==null||info==null||deviceName==null){
                                System.err.println("missing paras because of null");
                                continue;
                            }

                            Device device = new Device();
                            device.setuId(uId);
                            device.setInfo(info);
                            device.setDeviceName(deviceName);
                            receiver.receive(device,dataType);

                        }catch(Exception e){
                            System.err.println("something wrong because of "+ e.getMessage());
                        }
                    }
                }
            });
            threads[i].start();;
        }
    }
    public void stopConsume(){
        for(int i=0;i<n;i++){
            threads[i].stop();
            threads[i] = null;
        }
    }
}
