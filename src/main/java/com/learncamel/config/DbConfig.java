package com.learncamel.config;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DbConfig {

//	@Bean(name = "dataSource")
//	@ConfigurationProperties(prefix = "spring.datasource")
//	public DataSource dataSource() {
//		DataSource dSource = DataSourceBuilder.create().build();
//		return dSource;
//	}

}
