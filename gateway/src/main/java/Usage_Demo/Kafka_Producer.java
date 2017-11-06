package Usage_Demo;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.Random;

/**
 * Created by tangjialiang on 2017/11/6.
 */

public class Kafka_Producer {
    /**
     * @param args
     */
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "10.108.218.58:9092");
        props.put("metadata.broker.list","10.108.218.58:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        Producer<String, String> producer = new KafkaProducer(props);
        // 发送业务消息
        // 读取文件 读取内存数据库 读socket端口
        int i = 0 ;
        while (true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 温度随机数
            int max=20;
            int min=10;
            Random random = new Random();
            int temperature = random.nextInt(max)%(max-min+1) + min;

            int type = (random.nextInt(max)%(max-min+1) + min)%2;
            String dataType = (type==0) ? ("telemetry") : ("attributions") ;

            String info = "{\"uId\":\"uid1231231231\", \"dataType\":\""+dataType+"\", \"info\":{\"temperature\":\""+temperature+"\"}, \"deviceName\":\"tjl's Demo VDevice\"}" ; // "":""
            ProducerRecord<String, String> mesg = new ProducerRecord<String, String>("test",
                    info) ;

            System.out.format("topic: %s info: %s\n", "test", mesg.value()) ;
            producer.send(mesg);

            i++ ;
        }
    }
}