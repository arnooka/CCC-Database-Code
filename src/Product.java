public class Product{

	private String productCode;
	private String name;
	private String type;

	protected Product ( String productCode, String name, String type) {
		this.productCode = productCode;
		this.name = name;
		this.type = type;
	}

	public String getProductCode() {
		return productCode;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

}
