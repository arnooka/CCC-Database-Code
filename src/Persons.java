public class Persons{

		private String personCode;
		private String firstName;
		private String lastName;
		private Address address;
		private String emails;
		
		public Persons(String personCode, String firstName, String lastName, Address address, String email){
			this.personCode = personCode;
			this.firstName = firstName;
			this.lastName = lastName;
			this.emails = email;
			this.address = address;
		}

		public String getPersonCode() {
			return personCode;
		}

		public String getEmail() {
			return emails;
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

