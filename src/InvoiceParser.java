import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class InvoiceParser {

	public static void Invoices() throws FileNotFoundException {
		
		//Get HashMaps that map to Customer, Product, and Person objects
		HashMap<String, Customer> customer = JsonXmlParser.getCustomerCode1();
		HashMap<String, Product> product = JsonXmlParser.getProductCode1();
		HashMap<String, Persons> person = JsonXmlParser.getPersonCode1();
		
		Scanner invoice = new Scanner(new File("data/Invoices.dat"));
		
		//Print precursor information for executive invoices
		System.out.printf("Executive Summary Report\n=========================\n");
		System.out.printf("%-9s %-49s %-32s %-15s %-10s %-11s %s\n", 
						  "Invoice", "Customer", "Salesperson", "Subtotal", "Fees", "Taxes", "Total");
		
		//ArrayList of Detailed Individual Invoices
		ArrayList<String> individualInvoice = new ArrayList<String>();
		
		//ArrayList holds the orderd product for an invoice and is later cleared for the next invoice
		ArrayList<String> invoiceProducts = new ArrayList<String>();
		
		//Stores the number of invoices
		int o = Integer.parseInt(invoice.nextLine());
		
		double grandSubtotal = 0, grandFees = 0, grandTaxes = 0, grandTotal = 0;
		for(int i = 0; i<o; i++){
			double subtotal =0, total = 0, endTotal = 0, fees = 0, taxes = 0;
			int productCount = 0;
			
			String line = invoice.nextLine().trim();
			String tokens[] = line.split(";");
			String invoiceID = tokens[0].trim();
			String customerCode = tokens[1].trim();
			String personCode = tokens[2].trim();
			String productInfo = tokens[3].trim();
			
			//HashMap that maps a product to its corresponding price multiplier value
			HashMap<String, Double> productMultiplyer = new HashMap<String, Double>();
			
			//Boolean used to add a compliance fee only once if needed
			boolean bool = true;
			String individualProducts[] = productInfo.split(",");
			
			productCount = individualProducts.length;
			invoiceProducts.clear();
			for(int j = 0; j<individualProducts.length; j++){
				String p[] = individualProducts[j].trim().split(":");
				
				//Equipment and Consultations rate multiplyer
				if(p.length == 2){
					String code = p[0];
					double num = Double.parseDouble(p[1]);
					productMultiplyer.put(code, num);
				//Licenses rate multiplyer
				}else if(p.length == 3){
					String code = p[0];
					String date1 = p[1];
					String date2 = p[2];
					double days = License.daysBetween(date1, date2);
					productMultiplyer.put(code, days);
				}
				for(int k = 0; k<p.length; k++){
					String code = p[k];
					Product product1 = product.get(code);
					if(product1 != null){
						double fee = 0, compFee = 0, hourly = 0, ppu = 0, annual = 0;
						
						//code is the productCode
						invoiceProducts.add(code);
						
						//Get the costs of each items respective type
						String amounts = Product.getCosts(code, product);
						String cost[] = amounts.split(",");
						hourly = Double.parseDouble(cost[0]);
						ppu = Double.parseDouble(cost[1]);
						annual = Double.parseDouble(cost[2]);
						fee = Double.parseDouble(cost[3]);
						
						//Add compliance fee for Government Customers once
						if(bool == true){
							compFee += governmentCustomer.getComplianceFee(customer, customerCode);
							bool = false;
						}
						//Consultation fee
						fees += Consultations.consultationFee(product, code);
						fees += fee+compFee;
						//Calculate the invoice Subtotals and ending Totals
						total = Product.getTotal(product, productMultiplyer, code, hourly, ppu, annual);
						subtotal += total;
						//Calculate tax percentage for a customer type
						taxes = Math.round(companyCustomer.taxes(customer, product, customerCode, code, total)*100);
						taxes = taxes/100;
						//Creates an individual invoice as a string and adds to an ArrayList
						if(invoiceProducts.size() == productCount){
							individualInvoice.add(Invoice.toString(invoiceID, customerCode, productMultiplyer, customer,
									product, person, personCode, invoiceProducts));
							//Check for if the sales person exists
							if(person.get(personCode) == null){
								@SuppressWarnings("unused")
								Invoice invoice1 = new Invoice(invoiceID, customer.get(customerCode).getName(),
										"null", invoiceProducts);
							}else{
								@SuppressWarnings("unused")
								Invoice invoice1 = new Invoice(invoiceID, customer.get(customerCode).getName(),
										person.get(personCode).getLastName()+", "+person.get(personCode).getFirstName(),
										invoiceProducts);
							}
						}
					}
				}
			}
			endTotal = Math.round((subtotal+fees+taxes)*100);
			endTotal = endTotal/100;
			grandSubtotal += subtotal;
			grandFees += fees;
			grandTaxes += taxes;
			grandTotal += endTotal;
			//Print out each Executive Summary invoice
			if(person.get(personCode) == null){
				System.out.printf("%-9s %-49s %-29s $%10.2f $%10.2f $%10.2f $%10.2f\n",
						invoiceID, customer.get(customerCode).getName(), "null", subtotal, fees, taxes, endTotal);
			}else{
				System.out.printf("%-9s %-49s %-29s $%10.2f $%10.2f $%10.2f $%10.2f\n",
						invoiceID, customer.get(customerCode).getName(), person.get(personCode).getLastName()+", "+
						person.get(personCode).getFirstName(), subtotal, fees, taxes, endTotal);
			}
		}
		//Print the remaining details for the Executive Report
		System.out.println("===================================================================="
				 + "=====================================================================");
		System.out.printf("%-89s $%10.2f $%10.2f $%10.2f $%10.2f\n\n\n\n\n", 
						  "TOTALS", grandSubtotal, grandFees, grandTaxes, grandTotal);
		System.out.printf("Individual Invoice Detail Reports\n==================================================\n");
		//Print the Detailed Invoice Report
		for(int i = 0; i<individualInvoice.size(); i++){
			System.out.println(individualInvoice.get(i));
		}
		invoice.close();
	}
}