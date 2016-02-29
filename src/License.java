public class License extends Product{

		private Double fee;
		private Double annualCost;
		private Double years;

		public License(String productCode, String productName, Double fee, Double annualCost, String type){
			super(productName, productCode, type);
			this.fee = fee;
			this.annualCost = annualCost;
		}
		//public License(String productCode, String productName, Double fee, Double annualCost, String type){
		//	super(productName, productCode);
		//	this.fee = fee;
		//	this.annualCost = annualCost;
		//	this.years = years;
		//}

		public Double getFee() {
			return fee;
		}

		public Double getAnnualCost() {
			return annualCost;
		}

		public void setFee(Double fee) {
			this.fee = fee;
		}

		public void setAnnualCost(Double annualCost) {
			this.annualCost = annualCost;
		}
		public Double getYears() {
			return years;
		}
		public void setYears(Double years) {
			this.years = years;
		}
		
}
