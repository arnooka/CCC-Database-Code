import java.util.HashMap;

public class Consultations extends Product{

		private Persons consultant;
		private Double pricePerHour;
		private Double hours;
		
		public Consultations (String productCode, String productName, Persons consultant, Double pricePerHour, String type){
			super(productCode, productName, type);
			this.consultant = consultant;
			this.pricePerHour = pricePerHour;
		}

		public static double consultationFee(HashMap<String, Product> product, String productCode){
			double fees = 0;
			if(product.get(productCode).getType().equals("C")){
				fees+=150;
			}
			return fees;
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
