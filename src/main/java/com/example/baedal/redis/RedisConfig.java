package com.example.baedal.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
public class RedisConfig {

    @Value("${spring.redis.port}")
    public int port;

    @Value("${spring.redis.host}")
    public String host;

    /*Class <-> JSON간 변환 담당
    *
    * 1) json -> object 변환 시
    * readValue(File file, T.class) => json File을 읽어 T 클래스로 변환 readValue(Url url,
    * T.class) => url로 접속하여 데이터를 읽어와 T 클래스로 변환 readValue(String string, T.class) => string 형식의
    * json데이터를 T 클래스로 변환
    * 2) object -> json 변환시
    * writeValue(File file, T object) => object를 json file로 변환하여 저장
    * writeValueAsBytes(T object) => byte[] 형태로 object를 저장 writeValueAsString(T object) => string 형태로
    * object를 json형태로 저장
    *
    * RedisCacheConfiguration의 valueSerializer인 Jackson2는
    * LocalDate type을 인식하지 못함. 이에 대한 에러를 방지하기 위해
    * 관련 모듈을 추가한 objectMapper를 Serializer에 전달하기 위한 메서드*/
    @Bean
    public ObjectMapper objectMapper() {
        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
                .allowIfSubType(Object.class)
                .build();

        return JsonMapper.builder()
                .polymorphicTypeValidator(ptv)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .addModule(new JavaTimeModule())
                .activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL)
                .build();
    }

    /*Lettuce
    1) Netty 기반 redis client library
    2) async로 요청하기 때문에 높은 성능을 갖고 있음*/
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host,port);
    }

    /*1) redisTemplate을 json 형식으로 데이터를 받을 때 값이 일정하도록 직렬화
     *2) 저장할 클래스가 여러 개일 경우 범용적으로 저장할 수 있게 GenericJackson2JsonRedisSerializer을 이용*/
    @Bean
    public RedisTemplate<String, Object> redisTemplate(ObjectMapper objectMapper) {
        GenericJackson2JsonRedisSerializer serializer =
                new GenericJackson2JsonRedisSerializer(objectMapper);

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(serializer);
        redisTemplate.setEnableTransactionSupport(true); // transaction 허용

        return redisTemplate;
    }

    /*RedisCacheManager을 Bean으로 등록하면 spring에서는 caching을 할 떄
    * local cache에 저장하지 않고 redis에 저장한다.
    * default value serializer 옵션이 JdkSerialization인데,
    * 사람이 읽을 수 있는 구조로 저장하기 위해 json 형식으로 formatting*/
    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory,
                                               ObjectMapper objectMapper) {

        /*custom 설정을 적용하기 위해 RedisCacheConfiguration 생성
        * 이후 RedisCacheManager를 생성할 때 cacheDefaults의 인자로 configuration을 주면 해당 설정 적용*/
        RedisCacheConfiguration configuration = RedisCacheConfiguration
                .defaultCacheConfig()
                /*1) null값 caching될 수 없도록 설정
                * 2) cache의 ttl(time to live)를 설정*/
                //.disableCachingNullValues()
                //.entryTtl(Duration.ofSeconds(defaultExpireSecond))

                /*1) serializeKeysWith: cache key를 직렬화-역직렬화 하는데 사용하는 Pair 지정
                * 2) serializeValuesWith: cache value를 직렬화-역직렬화 하는데 사용하는 Pair를 지정
                * 3) Value는 다양한 자료구조가 올 수 있기 때문에 */
                .serializeKeysWith(RedisSerializationContext
                        .SerializationPair
                        .fromSerializer(new StringRedisSerializer())).serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper)));

        return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory)
                .cacheDefaults(configuration).build();
    }

}
