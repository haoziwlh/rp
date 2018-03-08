package com.jds.rp.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
/**
 * 
 * @author ee
 * 2018年1月4日 下午12:31:31
 */
@Service
public class HttpService {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private AsyncRestTemplate asyncRestTemplate;
	
	public String get(String url, Map<String, Object> params) {
		return restTemplate.getForObject(url, String.class, params);
	}
	
	public String get(String url) {
		return restTemplate.getForObject(url, String.class);
	}
	
	public String getWithParams(String url, Map<String, Object> params) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
		for(Map.Entry<String, Object> me : params.entrySet()) {
			builder.queryParam(me.getKey(), me.getValue());
		}
		return restTemplate.getForObject(builder.build().encode().toUri(), String.class);
	}
	
	public ResponseEntity<String> getAsync(String url, Map<String, Object> params) {
		return restTemplate.getForEntity(url, String.class, params);
	}
	
	public ListenableFuture<ResponseEntity<String>> getAsync(String url) {
		return asyncRestTemplate.getForEntity(url, String.class);
	}
	
	public ListenableFuture<ResponseEntity<String>> postAsyncJson(String url, String json) {
         HttpHeaders headers = new HttpHeaders();
         MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
         headers.setContentType(type);
         headers.add("Accept", MediaType.APPLICATION_JSON.toString());
         
         HttpEntity<String> formEntity = new HttpEntity<>(json, headers);
         return asyncRestTemplate.postForEntity(url, formEntity, String.class);
	}
}
