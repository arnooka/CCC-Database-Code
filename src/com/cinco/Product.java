package com.cinco;
import java.util.HashMap;

public class Product{

	private String productCode;
	private String name;
	private String type;

	protected Product ( String productCode, String name, String type) {
		this.productCode = productCode;
		this.name = name;
		this.type = type;
	}

	public static Double getTotal(Product prod, HashMap<String, Double> productMultiplyer,
						   double h, double ppu, double annual){
		double total = 0;
		if(prod.getType().equals("C")){
			total = productMultiplyer.get(prod.getProductCode())*h;
		}else if(prod.getType().equals("E")){
			total = productMultiplyer.get(prod.getProductCode())*ppu;
		}else if(prod.getType().equals("L")){
			total = (productMultiplyer.get(prod.getProductCode())/365)*annual;
		}
		return total;
	}
	
	public static String getCosts(Product prod){
		double hourly  = 0, ppu = 0, annual = 0, fee = 0;
		
		if(prod.getType().equals("C")){
			hourly = ((Consultations) prod).getPricePerHour();
		}else if(prod.getType().equals("E")){
			ppu = ((Equipment) prod).getPricePerUnit();
		}else if(prod.getType().equals("L")){
			annual = ((License) prod).getAnnualCost();
			fee = ((License) prod).getFee();
		}
		String a = hourly+","+ppu+","+annual+","+fee;
		return a;
	}
	
	public String getProductCode() {
		return productCode;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}
	
	public double getConsultationFee(){
		return 0.0;
	}
}
