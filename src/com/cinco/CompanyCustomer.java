package com.cinco;
import java.util.HashMap;

public class CompanyCustomer extends Customer {

	public CompanyCustomer(String customerCode, Persons primaryContact, String name, Address customerAddress, String type) {
		super(customerCode, primaryContact, name, customerAddress, type);
	}
	
	public String toString(){
		String a = null;
		
		//Statement checks if a person exists for the company
		if(this.getPrimaryContact() == null){
			a = " "+this.getName()+" "+"("+this.getCustomerCode()+")\n"+" null\n"+
				" "+this.getAddress().getStreet()+"\n"+" "+this.getAddress().getCity()+
				" "+this.getAddress().getState()+" "+this.getAddress().getZip()+
				" "+this.getAddress().getCountry()+"\n";
		}else{
			a = " "+this.getName()+" "+"("+this.getCustomerCode()+")\n"+
				" "+this.getPrimaryContact().getLastName()+", "+this.getPrimaryContact().getFirstName()+"\n"+
				" "+this.getAddress().getStreet()+"\n"+" "+this.getAddress().getCity()+
				" "+this.getAddress().getState()+" "+this.getAddress().getZip()+
				" "+this.getAddress().getCountry()+"\n";
		}
		
		return a;
	}
	
	//Calculate taxes for a Company Customer
	public static Double taxes(HashMap<String, Customer> customer, HashMap<String, Product> product,
							   String customerCode, String productCode, double total){
		double taxes = 0;
		
		if(customer.get(customerCode).getType().equals("C") && product.get(productCode).getType().equals("C")){
			taxes += total*0.0425;
		}else if(customer.get(customerCode).getType().equals("C") && product.get(productCode).getType().equals("L")){
			taxes += total*0.0425;
		}else if(customer.get(customerCode).getType().equals("C") && product.get(productCode).getType().equals("E")){
			taxes += total*0.07;
		}
		
		return taxes;
	}
}
