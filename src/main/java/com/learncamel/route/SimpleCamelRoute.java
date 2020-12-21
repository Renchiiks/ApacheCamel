package com.learncamel.route;

import javax.sql.DataSource;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.spi.DataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.learncamel.alert.MailProcessor;
import com.learncamel.domain.Item;
import com.learncamel.exception.DataException;
import com.learncamel.processor.BuildSqlProcessor;
import com.learncamel.processor.SuccessProcessor;
import com.mysql.jdbc.exceptions.MySQLDataException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SimpleCamelRoute extends RouteBuilder {

	@Autowired
	Environment environment;
	
	@Qualifier("dataSource")
	@Autowired
	DataSource dataSource;
	
	@Autowired
	BuildSqlProcessor processor;
	
	@Autowired
	SuccessProcessor successProcessor;
	
	@Autowired(required = true)
	MailProcessor mailProcessor;

	@Override
	public void configure() throws Exception {

		log.info("Starting the Camel route");
		
		DataFormat bindy = new BindyCsvDataFormat(Item.class);
		
//		errorHandler(deadLetterChannel("log.errorInRoute?level=ERROR&showProperties=true")
//		.maximumRedeliveries(3)
//		.redeliveryDelay(3000)
//		.backOffMultiplier(2)
//		.retryAttemptedLogLevel(LoggingLevel.ERROR));
		
		onException(MySQLDataException.class).log(LoggingLevel.ERROR, "MySQLDataException in the route ${body}")
		.maximumRedeliveries(3).backOffMultiplier(2).retryAttemptedLogLevel(LoggingLevel.ERROR);
		
		onException(DataException.class).log(LoggingLevel.ERROR, "DataException in the route ${body}");
		//.process(mailProcessor);
		
		from("{{startRoute}}")
		.log("Timer Invoked and the body " + environment.getProperty("message"))
		.choice()
			.when(header("env")
				.isNotEqualTo("mock"))
				.pollEnrich("{{fromRoute}}")
			.otherwise()
				.log("mock env flow and the body is ${body}")
		.end()
		.to("{{toRoute1}}")
		.unmarshal(bindy)
		.log("The unmarshaled object is ${body}")
		.split(body()).log("Record is ${body}")
			.process(new BuildSqlProcessor())
			.to("{{toRoute2}}")
		.end()
		.process(new SuccessProcessor())
		.to("{{toRoute3}}");

		log.info("Ending the Camel route");
		
		
	}
}
