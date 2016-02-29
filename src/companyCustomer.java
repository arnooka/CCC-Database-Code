public class companyCustomer extends Customer {

	public companyCustomer(String customerCode, Persons primaryContact, String name, Address customerAddress, String type) {
		super(customerCode, primaryContact, name, customerAddress, type);
		// TODO Auto-generated constructor stub
	}
	
	public String toString(){
		return " "+this.getName()+" "+"("+this.getCustomerCode()+")\n"+" "+this.getPrimaryContact().getLastName()+", "+this.getPrimaryContact().getFirstName()+
				"\n"+" "+this.getAddress().getStreet()+"\n"+" "+this.getAddress().getCity()+" "+this.getAddress().getState()+" "+this.getAddress().getZip()+
				" "+this.getAddress().getCountry()+"\n";
	}
}
