package com.cinco;
import java.util.ArrayList;
import java.util.HashMap;

public class Invoice {
	
	private String invoiceCode;
	private Customer customer;
	private Person salesPerson;
	private ArrayList<String> invoiceProducts;
	private double subTotal;
	private double totalFees;
	private double totalTax;
	private double endTotal;
	
	// Constructor for database connection and output
	public Invoice(String invoiceCode, Customer customer, Person salesPerson, ArrayList<String> invoiceProducts) {
		this.invoiceCode = invoiceCode;
		this.customer = customer;
		this.salesPerson = salesPerson;
		this.invoiceProducts = invoiceProducts;
	}
	
	// Constructor for linked lists and output (Just executive summary)
	public Invoice(String invoiceCode, Customer customer, Person salesPerson, ArrayList<String> invoiceProducts,
			double subTotal, double totalFees, double totalTax, double endTotal) {
		this.invoiceCode = invoiceCode;
		this.customer = customer;
		this.salesPerson = salesPerson;
		this.invoiceProducts = invoiceProducts;
		this.subTotal = subTotal;
		this.totalFees = totalFees;
		this.totalTax = totalTax;
		this.endTotal = endTotal;
	}

	// String builder for individual invoices
	public static String toString(String invoiceID, String customerCode, HashMap<String, Double> productMultiplyer,
			Customer cus, HashMap<String, Product> product, Person person, ArrayList<String> queriedProducts){
		
		double totalFee = 0, subtotal = 0, compFee = 0, taxes = 0, hourly = 0, ppu = 0, annual = 0, total = 0, fee;
		
		// Stores a list of products with their fees, rates and total cost as a string
		ArrayList<String> invoiceProducts = new ArrayList<String>();
		// Determine if there is a customer with the passed in customerCode
		if(cus != null){
			// Determines if there is a compliance fee
			if(cus.getType().equals("G")){
				compFee = cus.getComplianceFee(); 
			}
		}
		
		// Creates strings of each product for an invoice and calculates fees, taxes and subtotals
		for(int i = 0; i<queriedProducts.size(); i++){
			String s = null;
			fee = 0;
			String productCode = queriedProducts.get(i);
			Product prod = product.get(productCode);
			
			// Get the costs of each item
			String costs = Product.getCosts(prod);
			String tokens[] = costs.split(",");
			hourly = Double.parseDouble(tokens[0]);
			ppu = Double.parseDouble(tokens[1]);
			annual = Double.parseDouble(tokens[2]);
			fee = Double.parseDouble(tokens[3]);
			
			// String a holds the formatted product's name and its rates
			if(prod.getType().equals("C")){
				// Add consultation service fee
				fee+=150;
				total 	  = Product.getTotal(prod, productMultiplyer, hourly, ppu, annual);
				subtotal += total;
				String string = String.format("%s (%.2f hrs @ $%.2f/hr)", prod.getName(),
											 productMultiplyer.get(productCode), hourly);
				s = String.format("%-11s %-71s $%10.2f $%10.2f\n", productCode, string, fee, total);
				invoiceProducts.add(s);

			}else if(prod.getType().equals("E")){
				total 	  = Product.getTotal(prod, productMultiplyer, hourly, ppu, annual);
				subtotal += total;
				String string = String.format("%s (%.0f units @ $%.2f/unit)", prod.getName(),
											 productMultiplyer.get(productCode), ppu);
				s = String.format("%-11s %-71s $%10.2f $%10.2f\n", productCode, string, fee, total);
				invoiceProducts.add(s);
			}else if(prod.getType().equals("L")){
				total 	  = Product.getTotal(prod, productMultiplyer, hourly, ppu, annual);
				subtotal += total;
				String string = String.format("%s (%.0f days @ $%.2f/yr)", prod.getName(),
											 productMultiplyer.get(productCode), annual);
				s = String.format("%-11s %-71s $%10.2f $%10.2f\n", productCode, string, fee, total);
				invoiceProducts.add(s);
			}
			totalFee += fee;
			
			// Calculates tax percentage for a customer type
			taxes += Math.round(CompanyCustomer.taxes(cus, prod, total)*100);
		}
		double grandTotal = Math.round((subtotal+totalFee+compFee+(taxes/100))*100);
		
		// Create strings segments and concatinate
		String a = "Invoice " + invoiceID+"\n";
		String b = "========================\n";
		// Check if a Sales person exists
		String c = "Salesperson: null\n";
		if(person == null){
		}else{
			c = "Salesperson: " +person.getLastName()+", "+
					person.getFirstName()+"\n";
		}
		String d = "Customer Info:\n";
		String e = "null";
		if(cus == null){
		}else{
			// toString() method gets formatted customer info
			e = cus.toString();
		}
		String f = "-------------------------------------------\n";
		String g = String.format("%-11s %-78s %-10s %s\n", "Code", "Item", "Fees", "Total");
		// Concatenate all requested products from the products ArrayList
		String h = "";
		for(int j = 0; j<invoiceProducts.size(); j++){
			h = h+invoiceProducts.get(j);
		}
		String i = String.format("%108s\n", "========================");
		String j = String.format("%-83s $%10.2f $%10.2f\n", "SUB-TOTALS", totalFee, subtotal);
		String k = String.format("%-95s $%10.2f\n", "COMPLIANCE FEE", compFee);
		String l = String.format("%-95s $%10.2f\n", "TAXES", taxes/100);
		String m = String.format("%-95s $%10.2f\n", "TOTAL", grandTotal/100);
		
		// Return all strings as one giant string
		String invoice = a+b+c+d+e+f+g+h+i+j+k+l+m;
		return invoice;
	}

	public String getInvoiceCode() {
		return invoiceCode;
	}
	
	public Customer getCustomer() {
		return customer;
	}

	public Person getSalesPerson() {
		return salesPerson;
	}

	public ArrayList<String> getInvoiceProducts() {
		return invoiceProducts;
	}
	
	public double getSubTotal(){
		return subTotal;
	}
	
	public double getTotalFees(){
		return totalFees;
	}
	
	public double getTotalTax(){
		return totalTax;
	}
	
	public double getEndTotal(){
		return endTotal;
	}
	
}
