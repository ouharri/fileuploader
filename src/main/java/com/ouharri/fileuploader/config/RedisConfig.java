package com.ouharri.fileuploader.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

/**
 * Configuration class for Redis.
 */
@Configuration
public class RedisConfig {

    @Value("${redis.host}")
    private String redisHost;

    @Value("${redis.port}")
    private int redisPort;

    /**
     * Bean definition for the LettuceConnectionFactory, which is responsible for connecting to the Redis server.
     *
     * @return LettuceConnectionFactory bean
     */
    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisHost, redisPort);

        return new LettuceConnectionFactory(configuration);
    }

    /**
     * Bean definition for the RedisCacheManager, which manages caching configurations.
     *
     * @return RedisCacheManager bean
     */
    @Bean
    public RedisCacheManager cacheManager() {
        // Set the default cache configuration with a TTL of 10 minutes and disable caching of null values
        RedisCacheConfiguration cacheConfig = myDefaultCacheConfig(Duration.ofMinutes(10)).disableCachingNullValues();

        // Build the RedisCacheManager with additional cache configurations for specific caches
        return RedisCacheManager.builder(redisConnectionFactory())
                .cacheDefaults(cacheConfig)
                .withCacheConfiguration("tutorials", myDefaultCacheConfig(Duration.ofMinutes(5)))
                .withCacheConfiguration("tutorial", myDefaultCacheConfig(Duration.ofMinutes(1)))
                .build();
    }

    /**
     * Helper method to create a default RedisCacheConfiguration with a specified time-to-live (TTL).
     *
     * @param duration Time-to-live duration for cache entries
     * @return RedisCacheConfiguration with the specified TTL
     */
    private RedisCacheConfiguration myDefaultCacheConfig(Duration duration) {
        return RedisCacheConfiguration
                .defaultCacheConfig()
                .entryTtl(duration)
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }
}