package com.jds.rp.config;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import java.nio.charset.StandardCharsets;

import javax.net.ssl.SSLException;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.Netty4ClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
	public static final int MAX_WAIT = 8;
	
	@Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory){
		RestTemplate restTemplate = new RestTemplateBuilder()
		.requestFactory(factory)
		.build();
		
		restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
		
		return restTemplate;
    }
	
	@Bean
    public AsyncRestTemplate restAsyncTemplate(Netty4ClientHttpRequestFactory factory){
		AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate(factory);
		asyncRestTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
		return asyncRestTemplate;
    }
    
    @Bean
    public ClientHttpRequestFactory httpRequestFactory() throws SSLException{
    	Netty4ClientHttpRequestFactory factory = new Netty4ClientHttpRequestFactory();
        factory.setReadTimeout(5000);
        factory.setConnectTimeout(15000);
        SslContext sslContext = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        factory.setSslContext(sslContext);
        return factory;
    }
}
