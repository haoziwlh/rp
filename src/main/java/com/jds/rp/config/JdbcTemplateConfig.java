package com.jds.rp.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;

@Configuration
@PropertySource("file:${user.dir}/druid.properties")
public class JdbcTemplateConfig {

	@Value("${driverClassName}")
	private String driverClassName;
	@Value("${jdbcUrl}")
	private String jdbcUrl;
	@Value("${username}")
	private String username;
	@Value("${passwd}")
	private String passwd;
	@Value("${initialSize}")
	private int initialSize;
	@Value("${maxActive}")
	private int maxActive;
	@Value("${minIdle}")
	private int minIdle;
	@Value("${maxWait}")
	private int maxWait;
	@Value("${connectionProperties}")
	private String connectionProperties;
	@Value("${defaultAutoCommit}")
	private boolean defaultAutoCommit;
	@Value("${logAbandoned}")
	private boolean logAbandoned;
	@Value("${removeAbandoned}")
	private boolean removeAbandoned;
	@Value("${removeAbandonedTimeout}")
	private int removeAbandonedTimeout;
	@Value("${timeBetweenEvictionRunsMillis}")
	private int timeBetweenEvictionRunsMillis;
	@Value("${numTestsPerEvictionRun}")
	private int numTestsPerEvictionRun;
	@Value("${minEvictableIdleTimeMillis}")
	private int minEvictableIdleTimeMillis;
	@Value("${poolPreparedStatements}")
	private boolean poolPreparedStatements;
	@Value("${maxOpenPreparedStatements}")
	private int maxOpenPreparedStatements;
	@Value("${validationQuery}")
	private String validationQuery;
	@Value("${testOnBorrow}")
	private boolean testOnBorrow;
	@Value("${testOnReturn}")
	private boolean testOnReturn;
	@Value("${testWhileIdle}")
	private boolean testWhileIdle;
	
	@Bean
    public JdbcTemplate jdbcTemplate(ClientHttpRequestFactory factory){
		return new JdbcTemplate(ds());
    }
	
	@Bean
	public DataSource ds() {
		DruidDataSource ds = new DruidDataSource();
		configureDs(ds);
		return ds;
	}
	
	private void configureDs(DruidDataSource ds) {
		ds.setDriverClassName(driverClassName);
		ds.setUrl(jdbcUrl);
		ds.setUsername(username);
		ds.setPassword(passwd);
		ds.setInitialSize(initialSize);
		ds.setMaxActive(maxActive);
		ds.setMinIdle(minIdle);
		ds.setMaxWait(maxWait);
		ds.setConnectionProperties(connectionProperties);
		ds.setDefaultAutoCommit(defaultAutoCommit);
		ds.setLogAbandoned(logAbandoned);
		ds.setRemoveAbandoned(removeAbandoned);
		ds.setRemoveAbandonedTimeout(removeAbandonedTimeout);
		ds.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
		ds.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
		ds.setPoolPreparedStatements(poolPreparedStatements);
		ds.setMaxOpenPreparedStatements(maxOpenPreparedStatements);
		ds.setValidationQuery(validationQuery);
		ds.setTestOnBorrow(testOnBorrow);
		ds.setTestOnReturn(testOnReturn);
		ds.setTestWhileIdle(testWhileIdle);
	}
	
	@Bean
    public PlatformTransactionManager mysqlTransactionManager(DataSource myqlDataSource){
        return new DataSourceTransactionManager(myqlDataSource);
    }
}
