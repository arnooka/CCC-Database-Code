import java.util.ArrayList;
import java.util.HashMap;

public class Invoice {
	
	private String invoiceID;
	private String customerName;
	private String salesPerson;
	private ArrayList<String> invoiceProducts;
	
	public Invoice(String invoiceID, String customerName, String salesPerson, ArrayList<String> invoiceProducts) {
		this.invoiceID = invoiceID;
		this.customerName = customerName;
		this.salesPerson = salesPerson;
		this.invoiceProducts = invoiceProducts;
	}
	
	//String builder for individual invoices
	public static String toString(String invoiceID, String customerCode, HashMap<String, Double> productMultiplyer,
			HashMap<String, Customer> customer, HashMap<String, Product> product, HashMap<String, Persons> person,
			String personCode, ArrayList<String> invoiceProducts){
		double totalFee = 0, subtotal = 0, compFee = 0, taxes = 0, hourly = 0, ppu = 0, annual = 0, total = 0, fee;
		//Stores a list of products with their fees, rates and total cost as a string
		ArrayList<String> products = new ArrayList<String>();
		//Determines if there is a compliance fee 
		compFee = governmentCustomer.getComplianceFee (customer, customerCode);
		
		//Creates strings of each product for an invoice and calculates fees, taxes and subtotals
		for(int i = 0; i<invoiceProducts.size(); i++){
			String s = null;
			String code = invoiceProducts.get(i);
			fee = 0;
			
			//Get the costs of each item
			String costs = Product.getCosts(code, product);
			String tokens[] = costs.split(",");
			hourly = Double.parseDouble(tokens[0]);
			ppu = Double.parseDouble(tokens[1]);
			annual = Double.parseDouble(tokens[2]);
			fee = Double.parseDouble(tokens[3]);
			
			//String a holds the formatted product's name and its rates
			if(product.get(code).getType().equals("C")){
				total = Product.getTotal(product, productMultiplyer, code, hourly, ppu, annual);
				subtotal += total;
				//Add consultation service fee
				fee+=150;
				String string = String.format("%s (%.2f hrs @ $%.2f/hr)", product.get(code).getName(),
											 productMultiplyer.get(code), hourly);
				s = String.format("%-11s %-71s $%10.2f $%10.2f\n", code, string, fee, total);
				products.add(s);

			}else if(product.get(code).getType().equals("E")){
				total = Product.getTotal(product, productMultiplyer, code, hourly, ppu, annual);
				subtotal += total;
				String string = String.format("%s (%.0f units @ $%.2f/unit)", product.get(code).getName(),
											 productMultiplyer.get(code), ppu);
				s = String.format("%-11s %-71s $%10.2f $%10.2f\n", code, string, fee, total);
				products.add(s);
			}else if(product.get(code).getType().equals("L")){
				total = Product.getTotal(product, productMultiplyer, code, hourly, ppu, annual);
				subtotal += total;
				String string = String.format("%s (%.0f days @ $%.2f/yr)", product.get(code).getName(),
											 productMultiplyer.get(code), annual);
				s = String.format("%-11s %-71s $%10.2f $%10.2f\n", code, string, fee, total);
				products.add(s);
			}
			totalFee += fee;
			
			//Calculates tax percentage for a customer type
			taxes += Math.round(companyCustomer.taxes(customer, product, customerCode, code, total)*100);
		}
		double grandTotal = Math.round((subtotal+totalFee+compFee+(taxes/100))*100);
		
		//Create strings segments and concatinate
		String a = "Invoice " + invoiceID+"\n";
		String b = "========================\n";
		//Check if a Sales person exists
		String c = null;
		if(person.get(personCode) == null){
			c = "Salesperson: null\n";
		}else{
			c = "Salesperson: " +person.get(personCode).getLastName()+", "+
					person.get(personCode).getFirstName()+"\n";
		}
		String d = "Customer Info:\n";
		String e = customer.get(customerCode).toString();
		String f = "-------------------------------------------\n";
		String g = String.format("%-11s %-78s %-10s %s\n", "Code", "Item", "Fees", "Total");
		//Create the string of all requested products from the products ArrayList
		String h = "";
		for(int j = 0; j<products.size(); j++){
			h = h+products.get(j);
		}
		String i = String.format("%108s\n", "========================");
		String j = String.format("%-83s $%10.2f $%10.2f\n", "SUB-TOTALS", totalFee+compFee, subtotal);
		String k = String.format("%-95s $%10.2f\n", "COMPLIANCE FEE", compFee);
		String l = String.format("%-95s $%10.2f\n", "TAXES", taxes/100);
		String m = String.format("%-95s $%10.2f\n", "TOTAL", grandTotal/100);
		
		//customer.get(customerCode).toString() holds the formatted customer information
		String invoice = a+b+c+d+e+f+g+h+i+j+k+l+m;
		return invoice;
	}

	public String getInvoiceID() {
		return invoiceID;
	}

	public String getCustomerName() {
		return customerName;
	}

	public String getSalesPerson() {
		return salesPerson;
	}

	public ArrayList<String> getInvoiceProducts() {
		return invoiceProducts;
	}
	
}
