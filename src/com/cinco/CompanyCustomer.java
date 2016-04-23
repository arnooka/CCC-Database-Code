package com.cinco;

public class CompanyCustomer extends Customer {

	public CompanyCustomer(String customerCode, Person primaryContact, String name, Address customerAddress, String type) {
		super(customerCode, primaryContact, name, customerAddress, type);
	}
	
	public String toString(){
		String a = null;
		
		// Statement checks if a person exists for the company
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
	
	// Calculate taxes for a Company Customer
	public static Double taxes(Customer cus, Product prod, double total){
		double taxes = 0;
		if(cus == null){
		}else{
			if(cus.getType().equals("C") && prod.getType().equals("C")){
				taxes += total*0.0425;
			}else if(cus.getType().equals("C") && prod.getType().equals("L")){
				taxes += total*0.0425;
			}else if(cus.getType().equals("C") && prod.getType().equals("E")){
				taxes += total*0.07;
			}
		}
		
		
		return taxes;
	}
}
