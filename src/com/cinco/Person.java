package com.cinco;

import java.util.ArrayList;

public class Person{

		private String personCode;
		private String firstName;
		private String lastName;
		private Address address;
		private ArrayList<String> emails;
		private String[] emailTokens;
		
		// DBObjectsParser Person Constructor
		public Person(String personCode, String firstName, String lastName, Address address, ArrayList<String> emails){
			this.personCode = personCode;
			this.firstName = firstName;
			this.lastName = lastName;
			this.emails = emails;
			this.address = address;
		}
		// JsonXmlParser Person Constructor
		public Person(String personCode, String firstName, String lastName, Address address,
				String[] emailTokens) {
			this.personCode = personCode;
			this.firstName = firstName;
			this.lastName = lastName;
			this.emailTokens = emailTokens;
			this.address = address;
		}
		
		public String getPersonCode() {
			return personCode;
		}

		public ArrayList<String> getEmailList() {
			return emails;
		}
		
		public String[] getEmailTokens(){
			return emailTokens;
		}

		public String getFirstName() {
			return firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public Address getAddress() {
			return address;
		}

}

