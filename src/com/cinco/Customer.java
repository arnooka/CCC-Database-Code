package com.cinco;
public class Customer{
	
		
		private String customerCode;
		private String name;
		private Address address;
		private Person primaryContact;
		private String type;
		
		public Customer(String customerCode, Person primaryContact, String name, Address address, String type){
			this.customerCode = customerCode;
			this.primaryContact = primaryContact;
			this.name = name;
			this.address = address;
			this.type = type;
		}

		public String getCustomerCode() {
			return customerCode;
		}

		public Person getPrimaryContact() {
			return primaryContact;
		}

		public String getName() {
			return name;
		}

		public Address getAddress() {
			return address;
		}

		public String getType() {
			return type;
		}
		
		public Double getComplianceFee() {
			return 0.0;
		}
		

}
