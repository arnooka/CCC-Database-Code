package com.cinco;

public class Equipment extends Product {

	private Double pricePerUnit;
	private Double numUnits;
	
	public Equipment(String productCode, String productName, Double pricePerUnit, String type){
		super(productCode, productName, type);
		this.pricePerUnit = pricePerUnit;
	
	}

	public Double getPricePerUnit() {
		return pricePerUnit;
	}
	
	public Double getNumUnits() {
		return numUnits;
	}
	
}
