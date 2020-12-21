package com.learncamel.alert;


import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MailProcessor implements Processor{
	
	@Autowired
	JavaMailSender emailSender;
	
	@Autowired
	Environment environment;

	@Override
	public void process(Exchange exchange) throws Exception {
		
		Exception e = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
		//log.info("Exception caught in mail processor");
		
		String messageBody = "Exception happened in the camel route: " + e.getMessage();
		
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(environment.getProperty("mailFrom"));
		message.setTo(environment.getProperty("mailTo"));
		message.setSubject("Exception in Camel Route");
		message.setText(messageBody);
		
		emailSender.send(message);

	}
	
	
 
}
