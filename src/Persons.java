public class Persons{

		private String personCode;
		private String firstName;
		private String lastName;
		private Address address;
		private String[] emails;
		
		public Persons(String personCode, String firstName, String lastName, Address address, String[] emails){
			this.personCode = personCode;
			this.firstName = firstName;
			this.lastName = lastName;
			this.emails = emails;
			this.address = address;
		}

		public String getPersonCode() {
			return personCode;
		}

		public String[] getEmail() {
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

		public void setPersonCode(String personCode) {
			this.personCode = personCode;
		}

		public void setEmail(String[] emails) {
			this.emails = emails;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		public void setAddress(Address address) {
			this.address = address;
		}

}

