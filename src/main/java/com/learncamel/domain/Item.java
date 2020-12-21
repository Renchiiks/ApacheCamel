package com.learncamel.domain;

import java.math.BigDecimal;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

@CsvRecord(separator = ",", skipFirstLine = true)
public class Item {
	
	@DataField(pos = 1)
	private String trasactionType;
	
	@DataField(pos = 2)
	private String sku;
	
	@DataField(pos = 3)
	private String itemDescription;
	
	@DataField(pos = 4, precision = 2)
	private BigDecimal price;

	public String getTrasactionType() {
		return trasactionType;
	}

	public void setTrasactionType(String trasactionType) {
		this.trasactionType = trasactionType;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "Item [trasactionType=" + trasactionType + ", sku=" + sku + ", itemDescription=" + itemDescription
				+ ", price=" + price + "]";
	}
	
	
}
