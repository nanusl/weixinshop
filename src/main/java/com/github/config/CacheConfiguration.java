package com.github.config;

import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author nanusl
 * @version V1.0
 * @Description
 * @date 2021-02-03 20:09
 */
@Slf4j
@EnableCaching
@Configuration
public class CacheConfiguration extends CachingConfigurerSupport {

    private final StringRedisSerializer keySerializer;

    private final RedisConnectionFactory redisConnectionFactory;

    private final RedisSerializer<?> valueSerializer;

    CacheConfiguration(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
        keySerializer = new StringRedisSerializer();
        valueSerializer = new JdkSerializationRedisSerializer();
    }

    @Bean
    public KeyGenerator wiselyKeyGenerator() {
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName());
            sb.append("-");
            sb.append(method.getName());
            sb.append("-");
            for (Object obj : params) {
                sb.append(obj.toString());
            }
            return sb.toString();
        };
    }

    @Bean
    @Override
    public CacheManager cacheManager() {
        // 设置默认过期时间：30 分钟
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(30))
                .disableCachingNullValues()
                // 使用注解时的序列化、反序列化
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer));

        Set<String> cacheNames = Sets.newHashSet("Test_");

        Map<String, RedisCacheConfiguration> configMap = cacheNames
                .parallelStream()
                .collect(Collectors.toMap(e -> e, e -> defaultCacheConfig));

        return RedisCacheManager.builder(redisConnectionFactory)
                .initialCacheNames(cacheNames)
                .withInitialCacheConfigurations(configMap)
                .build();
    }

    @Bean
    @Primary
    public RedisTemplate<String, Object> getRedisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();

        template.setKeySerializer(keySerializer);
        template.setHashKeySerializer(keySerializer);

        template.setValueSerializer(valueSerializer);
        template.setHashValueSerializer(valueSerializer);
        template.setDefaultSerializer(valueSerializer);

        log.info("redis: {}", redisConnectionFactory);
        LettuceConnectionFactory factory = (LettuceConnectionFactory) redisConnectionFactory;
        log.info("spring.redis.database: {}", factory.getDatabase());
        log.info("spring.redis.host: {}", factory.getHostName());
        log.info("spring.redis.port: {}", factory.getPort());

        template.setConnectionFactory(redisConnectionFactory);
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public StringRedisTemplate getStringRedisTemplate() {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
}
