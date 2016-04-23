package com.cinco;

public class Consultations extends Product{

		private Person consultant;
		private Double pricePerHour;
		private Double hours;
		
		public Consultations (String productCode, String productName, Person consultant, Double pricePerHour, String type){
			super(productCode, productName, type);
			this.consultant = consultant;
			this.pricePerHour = pricePerHour;
		}

		public double getConsultationFee(){
			double fees = 150.0;
			return fees;
		}
		
		public Person getConsultant() {
			return consultant;
		}

		public Double getPricePerHour() {
			return pricePerHour;
		}

		public void setConsultant(Person consultant) {
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
