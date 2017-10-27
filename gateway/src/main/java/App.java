import config.Config;
import consumer.LocalConsumer;
import consumer.MqttConsumer;
import data.CommonData;
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
//        RocketConsumer rocketConsumer = new RocketConsumer("iotConsumerGroup");
//        MessageListenerConcurrently listener = new MessageListenerConcurrently(){
//            LinkedBlockingQueue<String> rocketMQMsgCache = CommonData.getInstance().rocketMQMsgCache;
//            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
//                try{
//                    for (Message msg :list){
//                        String content = new String(msg.getBody()) ;
//                        rocketMQMsgCache.put(content);
//                    }
//                }catch(Exception e){
//                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
//                }
//                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
//            }
//        };
//        rocketConsumer.init(Config.ROCKETMQURL,"simpleInstance",Config.TOPIC,listener);

        MqttConsumer mqttConsumer = new MqttConsumer();
        MqttCallback callback = new MqttCallback() {
            LinkedBlockingQueue<String> rocketMQMsgCache = CommonData.getInstance().rocketMQMsgCache;
            public void connectionLost(Throwable throwable) {

            }

            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                String data = new String (mqttMessage.getPayload());
                rocketMQMsgCache.put(data);
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
