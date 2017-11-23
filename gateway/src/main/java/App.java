import com.alibaba.fastjson.JSONObject;
import config.Config;
import consumer.LocalConsumer;
import consumer.MqttConsumer;
import data.CommonData;
import extensions.KafkaHelper;
import extensions.MessgeRecorder;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Administrator on 2017/10/25.
 */
public class App {
    public static void main(String[] args){

        MqttConsumer mqttConsumer = new MqttConsumer();
        MqttCallback callback = new MqttCallback() {
            LinkedBlockingQueue<String> rocketMQMsgCache = CommonData.getInstance().rocketMQMsgCache;
            public void connectionLost(Throwable throwable) {

            }

            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                String data = new String (mqttMessage.getPayload());
                rocketMQMsgCache.put(data);

                // todo send msg[json] to kafka
                try {
                    JSONObject json = JSONObject.parseObject(data);
                    String uId = json.getString("uId");
                    String dataType = json.getString("dataType");
                    String info = json.getString("info");

                    if (!dataType.equals("telemetry")) return;
                    MessgeRecorder mr = new MessgeRecorder(info);
                    mr.addCurTime().addUid(uId);

                    KafkaHelper kh = KafkaHelper.getInstance() ;
                    kh.sengMsg(mr.toJson().toString());
                } catch (Exception e) {
                    System.err.println("fail to send to kafka!") ;
                }
            }

            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
            }
        };
        mqttConsumer.init(callback,Config.TOPIC);


        LocalConsumer localConsumer = new LocalConsumer(1);
        localConsumer.init();

        localConsumer.startConsume();
        mqttConsumer.startConsume();
    }
}
