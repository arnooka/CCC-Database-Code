package com.cinco;

public class Consultations extends Product{

	private Person consultant;
	private Double pricePerHour;
	
	public Consultations (String productCode, String productName, Person consultant, Double pricePerHour, String type){
		super(productCode, productName, type);
		this.consultant = consultant;
		this.pricePerHour = pricePerHour;
	}
	
	public double getConsultationFee(){
		double fees = 150.0;
		return fees;
	}
	
	public Person getConsultant() {
		return consultant;
	}
	
	public Double getPricePerHour() {
		return pricePerHour;
	}
	
}
