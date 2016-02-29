public class Equipment extends Product {

	
	private Double pricePerUnit;
	private Double numUnits;
	
	public Equipment(String productCode, String productName, Double pricePerUnit, String type){
		super(productCode, productName, type);
		this.pricePerUnit = pricePerUnit;
	
	}
	//public Equipment(String productCode, String productName, Double pricePerUnit, String type){
	//	super(productCode, productName);
	//	this.pricePerUnit = pricePerUnit;
	//	this.numUnits = numUnits;
	//}

	public Double getPricePerUnit() {
		return pricePerUnit;
	}

	public void setPricePerUnit(Double pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}
	public Double getNumUnits() {
		return numUnits;
	}
	public void setNumUnits(Double numUnits) {
		this.numUnits = numUnits;
	}
	
	

}
