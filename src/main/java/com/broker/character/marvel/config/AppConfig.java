package com.broker.character.marvel.config;



import com.character.marvel.model.MarvelCharacter;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


/**
 * @author prabhath amarasinghe
 */

@Configuration
@ComponentScan("com.broker.character.marvel")
@PropertySource("classpath:/application.properties")
public class AppConfig {

    public final static String MARVEL_COMMON_ENDPOINT = "https://gateway.marvel.com:443/v1/public";
    public final static String MARVEL_CHARACTER_ENDPOINT = "/characters";

    public final static String MARVEL_API_KEY_PARAM = "apikey=";
    public final static String MARVEL_TIMESTAMP_PARAM = "ts=";
    public final static String MARVEL_HASH_PARAM = "hash=";
    public final static String MARVEL_OFFSET_PARAM = "offset=";

    public final static String BOOTSTRAP_SERVERS = "localhost:9092";
    public final static String SCHEMA_REGISTRY_URL = "http://localhost:8081";

    @Bean
    public RestTemplate getRestTemplate(RestTemplateBuilder restTemplateBuilder){
        return restTemplateBuilder.build();
    }

    @Bean
    public ProducerFactory<Long, MarvelCharacter> producerFactory() {
        Map<String, Object> configMap = new HashMap<>();
        configMap.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        configMap.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        configMap.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        configMap.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, SCHEMA_REGISTRY_URL);
        configMap.put(ProducerConfig.ACKS_CONFIG, "1");


        return new DefaultKafkaProducerFactory<Long, MarvelCharacter>(configMap);
    }

    @Bean
    public KafkaTemplate<Long, MarvelCharacter> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

}
