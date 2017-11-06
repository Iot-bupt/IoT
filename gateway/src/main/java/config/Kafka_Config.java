package config;

/**
 * Created by tangjialiang on 2017/11/6.
 */
public class Kafka_Config {
    public static String bootstrap_servers = "10.108.218.58:9092";
    public static String metadata_broker_list = "10.108.218.58:9092" ;
    public static String key_serializer = "org.apache.kafka.common.serialization.StringSerializer" ;
    public static String value_serializer = "org.apache.kafka.common.serialization.StringSerializer" ;
    public static String recorder_topic = "data" ;
}
