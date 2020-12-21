package com.learncamel.processor;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.learncamel.domain.Item;
import com.learncamel.exception.DataException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BuildSqlProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {

		Item item = exchange.getIn().getBody(Item.class);
		// log.info("Item in Processor is: " + item);

		String tableName = "items";
		StringBuilder query = new StringBuilder();
		
		if(ObjectUtils.isEmpty(item.getSku())) {
			throw new DataException("Sku is null for " + item.getItemDescription());
		}
		
		if (item.getTrasactionType().equals("ADD")) {
			query.append("INSERT INTO" + tableName + "(sku, item_description, price) VALUES ('");
			query.append(item.getSku() + "','" + item.getItemDescription() + "'," + item.getPrice() + ")");
		} else if (item.getTrasactionType().equals("UPDATE")) {

			query.append("UPDATE " + tableName + " SET price = ");
			query.append(item.getPrice()+ " WHERE sku = '"+ item.getSku()+"'");

		} else if (item.getTrasactionType().equals("DELETE")) {
			query.append("DELETE FROM " + tableName + " WHERE sku = '"+item.getSku()+ "'");

		}
		// log.info("Final Query is: " + query);

		exchange.getIn().setBody(query.toString());

	}

}
