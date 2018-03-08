package com.jds.rp.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
/**
 * 
 * @author ee
 * 2018年1月5日 上午10:26:04
 */
import org.springframework.data.redis.core.RedisTemplate;

import redis.clients.jedis.JedisPoolConfig;

@Configuration
@PropertySource("file:${user.dir}/redis.properties")
public class RedisConfig {
	@Value("${max_total}")
	private int maxTotal;
	@Value("${test_on_borrow}")
	private boolean testOnBorrow;
	@Value("${test_on_return}")
	private boolean testOnReturn;
	@Value("${hostname}")
	private String hostName;
	@Value("${port}")
	private int port;
	@Value("${password}")
	private String password;
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	JedisConnectionFactory jedisConnectionFactory() {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(maxTotal);
		poolConfig.setTestOnBorrow(testOnBorrow);
		poolConfig.setTestOnReturn(testOnReturn);

		JedisConnectionFactory connectionFactory = new JedisConnectionFactory(poolConfig);
		connectionFactory.setHostName(hostName);
		connectionFactory.setPort(port);
		connectionFactory.setUsePool(true);
		if(StringUtils.isNotEmpty(password)) {
			connectionFactory.setPassword(password);
		}
		return connectionFactory;
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		return redisTemplate;
	}
}
