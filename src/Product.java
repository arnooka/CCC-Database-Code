import java.util.HashMap;

public class Product{

	private String productCode;
	private String name;
	private String type;

	protected Product ( String productCode, String name, String type) {
		this.productCode = productCode;
		this.name = name;
		this.type = type;
	}

	public static Double getTotal(HashMap<String, Product> product, HashMap<String, Double> productMultiplyer,
						   String productCode, double h, double ppu, double annual){
		double total = 0;
		if(product.get(productCode).getType().equals("C")){
			total = productMultiplyer.get(productCode)*h;
		}else if(product.get(productCode).getType().equals("E")){
			total = productMultiplyer.get(productCode)*ppu;
		}else if(product.get(productCode).getType().equals("L")){
			total = (productMultiplyer.get(productCode)/365)*annual;
		}
		return total;
	}
	
	public static String getCosts(String productCode, HashMap<String, Product> product){
		double h  = 0, ppu = 0, annual = 0, fee = 0;
		
		if(product.get(productCode).getType().equals("C")){
			h = ((Consultations) product.get(productCode)).getPricePerHour();
		}else if(product.get(productCode).getType().equals("E")){
			ppu = ((Equipment) product.get(productCode)).getPricePerUnit();
		}else if(product.get(productCode).getType().equals("L")){
			annual = ((License) product.get(productCode)).getAnnualCost();
			fee = ((License) product.get(productCode)).getFee();
		}
		String a = h+","+ppu+","+annual+","+fee;
		return a;
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
