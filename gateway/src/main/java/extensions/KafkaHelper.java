package extensions;

import com.alibaba.fastjson.JSONObject;
import config.Kafka_Config;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * Created by tangjialiang on 2017/11/6.
 * tools
 */
public class KafkaHelper {

    private Producer<String, String> producer = null ;

    public KafkaHelper() {
        init() ;
    }

    public void init() {
        Properties props = new Properties();
        props.put("bootstrap.servers", Kafka_Config.bootstrap_servers);
        props.put("metadata.broker.list",Kafka_Config.metadata_broker_list);
        props.put("key.serializer", Kafka_Config.key_serializer);
        props.put("value.serializer", Kafka_Config.value_serializer);
        producer = new KafkaProducer(props);
    }

    public void sengMsg(String msgStr) {
        ProducerRecord<String, String> pr = new ProducerRecord<String, String>(Kafka_Config.recorder_topic, msgStr) ;
        producer.send(pr);
    }

    public void sendMsg(JSONObject json) {
        String jsonStr = json.toJSONString() ;
        sengMsg(jsonStr);
    }
}