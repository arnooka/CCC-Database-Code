package com.cinco;

public class GovernmentCustomer extends Customer {

	public GovernmentCustomer(String customerCode, Person primaryContact, String name, Address customerAddress, String type) {
		super(customerCode, primaryContact, name, customerAddress, type);
	}
	
	public String toString(){
		String a = null;
		
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
	
	public Double getComplianceFee() {
		return 125.0;
	}
	
}
