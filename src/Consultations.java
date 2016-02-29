public class Consultations extends Product{

		private Persons consultant;
		private Double pricePerHour;
		private Double hours;
		
		public Consultations (String productCode, String productName, Persons consultant, Double pricePerHour, String type){
			super(productCode, productName, type);
			this.consultant = consultant;
			this.pricePerHour = pricePerHour;
		}

		public Persons getConsultant() {
			return consultant;
		}

		public Double getPricePerHour() {
			return pricePerHour;
		}

		public void setConsultant(Persons consultant) {
			this.consultant = consultant;
		}

		public void setPricePerHour(Double pricePerHour) {
			this.pricePerHour = pricePerHour;
		}
		public Double getHours() {
			return hours;
		}
		public void setHours(Double hours) {
			this.hours = hours;
		}
		
}
