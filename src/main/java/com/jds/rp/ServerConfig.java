package com.jds.rp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("file:${user.dir}/server.properties")
public class ServerConfig {

	@Value("${server_port}")
	private int serverPort;
	@Value("${appid}")
	private String appid;
	@Value("${secret}")
	private String secret;
	@Value("${grant_type}")
	private String grantType;
	
	public int serverPort() {
		return this.serverPort;
	}

	public int getServerPort() {
		return serverPort;
	}

	public String getAppid() {
		return appid;
	}

	public String getSecret() {
		return secret;
	}

	public String getGrantType() {
		return grantType;
	}
	
}
