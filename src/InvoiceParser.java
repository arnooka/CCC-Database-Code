import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.joda.time.DateTime;
import org.joda.time.Days;

public class InvoiceParser {

	public static void Invoices() throws FileNotFoundException {
		
		Scanner invoice = new Scanner(new File("data/Invoices.dat"));
		
		///////////////////////////////////////////////////////////////////////////////////
		//Create elements from Invoicess.dat and compare the info to the .dat files
		System.out.printf("Executive Summary Report\n=========================\n");
		System.out.printf("%-9s %-49s %-32s %-15s %-10s %-11s %s\n", "Invoice", "Customer", "Salesperson",
																 "Subtotal", "Fees", "Taxes", "Total");
		//ArrayList of Detailed Individual Invoices
		ArrayList<String> individualInvoice = new ArrayList<String>();
		//ArrayList holds the orderd product for an invoice and is later cleared for the next invoice
		ArrayList<String> invoiceProducts = new ArrayList<String>();
		
		double grandSubtotal = 0, grandFees = 0, grandTaxes = 0, grandTotal = 0;
		
		//Get the number of invoices from the first line in Invoices.dat as an integer
		int o = Integer.parseInt(invoice.nextLine());
		for(int i = 0; i<o; i++){
			double subtotal =0, total = 0, endTotal = 0, fees = 0, taxes = 0;
			int productCount = 0;
			
			String line = invoice.nextLine().trim();
			String tokens[] = line.split(";");
			String invoiceID = tokens[0].trim();
			String customerCode = tokens[1].trim();
			String personCode = tokens[2].trim();
			String productInfo = tokens[3].trim();
			
			//Get HashMaps that map to Customer, Product, and Person objects
			HashMap<String, Customer> customer = JsonXmlFactory.getCustomerCode1();
			HashMap<String, Product> product = JsonXmlFactory.getProductCode1();
			HashMap<String, Persons> person = JsonXmlFactory.getPersonCode1();
			
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
					
					String start[] = date1.split("-");
					String Year1 = start[0].trim();
					String Month1 = start[1].trim();
					String Day1 = start[2].trim();
					int startYear = Integer.parseInt(Year1);
					int startMonth = Integer.parseInt(Month1);
					int startDay = Integer.parseInt(Day1);
			
					String end[] = date2.split("-");
					String Year2 = end[0].trim();
					String Month2 = end[1].trim();
					String Day2 = end[2].trim();
					int endYear = Integer.parseInt(Year2);
					int endMonth = Integer.parseInt(Month2);
					int endDay = Integer.parseInt(Day2);
			
					DateTime startDate = new DateTime(startYear, startMonth, startDay, 0, 0);
					DateTime endDate = new DateTime(endYear, endMonth, endDay, 0, 0);
					double days = Days.daysBetween(startDate, endDate).getDays();
			
					productMultiplyer.put(code, days);
				}
				for(int k = 0; k<p.length; k++){
					Product product1 = product.get(p[k]);
					if(product1 != null){

						double fee = 0, h = 0, ppu = 0, annual = 0;
						
						//p[k] is the productCode
						invoiceProducts.add(p[k]);
						
						//Get the costs of each items respective type
						if(product.get(p[k]).getType().equals("C")){
							h = ((Consultations) product.get(p[k])).getPricePerHour();
						}else if(product.get(p[k]).getType().equals("E")){
							ppu = ((Equipment) product.get(p[k])).getPricePerUnit();
						}else if(product.get(p[k]).getType().equals("L")){
							annual = ((License) product.get(p[k])).getAnnualCost();
							fee = ((License) product.get(p[k])).getFee();
						}
						
						//Add the compliance fee to the fee
						if(bool == true){
							if(customer.get(customerCode).getType().equals("G")){
								fee += 125;
								bool = false;
							}
						}
						fees += fee;
						
						//Calculate the invoice Subtotals and ending Totals
						if(product.get(p[k]).getType().equals("C")){
							total = productMultiplyer.get(p[k])*h;
							subtotal += total;
						}else if(product.get(p[k]).getType().equals("E")){
							total = productMultiplyer.get(p[k])*ppu;
							subtotal += total;
						}else if(product.get(p[k]).getType().equals("L")){
							total = (productMultiplyer.get(p[k])/365)*annual;
							subtotal += total;
						}
						
						//Calculate tax percentage for a customer type
						if(customer.get(customerCode).getType().equals("C") && product.get(p[k]).getType().equals("C")){
							taxes += total*0.0425;
						}else if(customer.get(customerCode).getType().equals("C") && product.get(p[k]).getType().equals("L")){
							taxes += total*0.0425;
						}else if(customer.get(customerCode).getType().equals("C") && product.get(p[k]).getType().equals("E")){
							taxes += total*0.07;
						}
						
						//Creates an individual invoice as a string and adds to an ArrayList
						if(invoiceProducts.size() == productCount){
							individualInvoice.add(invoiceString(invoiceID, customerCode, productMultiplyer, customer, product,
									person, p[k], personCode, invoiceProducts));
							
							//Check for what to send to the constructor
							Invoice invoice1 = new Invoice(invoiceID, customer.get(customerCode).getName(),
									person.get(personCode).getLastName()+", "+person.get(personCode).getFirstName(), invoiceProducts);
						}
					}
				}
			}
			endTotal += subtotal+fees+taxes;
			grandSubtotal += subtotal;
			grandFees += fees;
			grandTaxes += taxes;
			grandTotal += endTotal;
			//Print out each Executive Summary invoice
			System.out.printf("%-9s %-49s %-29s $%10.2f $%10.2f $%10.2f $%10.2f\n",
					 invoiceID, customer.get(customerCode).getName(), person.get(personCode).getLastName()+", "+
					person.get(personCode).getFirstName(), subtotal, fees, taxes, endTotal);
		}
		//Print the remaining details for the Executive Report
		System.out.println("===================================================================="
				 + "=====================================================================");
		System.out.printf("%-89s $%10.2f $%10.2f $%10.2f $%10.2f\n\n\n\n\n", "TOTALS", grandSubtotal, grandFees, grandTaxes, grandTotal);
		System.out.printf("Individual Invoice Detail Reports\n==================================================\n");
		//Print the Detailed Invoice Report
		for(int i = 0; i<individualInvoice.size(); i++){
			System.out.println(individualInvoice.get(i));
		}
		invoice.close();
	}
	
	//String builder for each individual invoice
	private static String invoiceString(String invoiceID, String customerCode, HashMap<String, Double> productMultiplyer, HashMap<String, Customer> customer,
			HashMap<String, Product> product, HashMap<String, Persons> person, String productCode, String personCode, ArrayList<String> invoiceProducts){
		double totalFee = 0, subtotal = 0, compFee = 0, total = 0, taxes = 0, h = 0, ppu = 0, annual = 0;
		
		//Stores a list of products with their fees, rates and total cost as a string
		ArrayList<String> products = new ArrayList<String>();
		
		//Determines if there is a compliance fee 
		if(customer.get(customerCode).getType().equals("G")){
			compFee = 125.00;
			totalFee += 125;
		}
		
		//Creates strings of each product for an invoice and calculates fees, taxes and subtotals 
		for(int i = 0; i<invoiceProducts.size(); i++){
			String s = null;
			double fee = 0;
			total = 0;
			
			//Get the costs of each items respective type
			if(product.get(invoiceProducts.get(i)).getType().equals("C")){
				h = ((Consultations) product.get(invoiceProducts.get(i))).getPricePerHour();
			}else if(product.get(invoiceProducts.get(i)).getType().equals("E")){
				ppu = ((Equipment) product.get(invoiceProducts.get(i))).getPricePerUnit();
			}else if(product.get(invoiceProducts.get(i)).getType().equals("L")){
				annual = ((License) product.get(invoiceProducts.get(i))).getAnnualCost();
				fee = ((License) product.get(invoiceProducts.get(i))).getFee();
			}
			totalFee += fee;
			
			//String b formats the product's name and its rates
			if(product.get(invoiceProducts.get(i)).getType().equals("C")){
				
				total = productMultiplyer.get(invoiceProducts.get(i))*h;
				subtotal += total;
				String b = String.format("%s (%.2f hrs @ $%.2f/hr)", product.get(invoiceProducts.get(i)).getName(),
						productMultiplyer.get(invoiceProducts.get(i)), h);
				s = String.format("%-11s %-71s $%10.2f $%10.2f\n", invoiceProducts.get(i), b, fee, total);
				products.add(s);
			}else if(product.get(invoiceProducts.get(i)).getType().equals("E")){
				
				total = productMultiplyer.get(invoiceProducts.get(i))*ppu;
				subtotal += total;
				String b = String.format("%s (%.0f units @ $%.2f/unit)", product.get(invoiceProducts.get(i)).getName(),
						productMultiplyer.get(invoiceProducts.get(i)), ppu);
				s = String.format("%-11s %-71s $%10.2f $%10.2f\n", invoiceProducts.get(i), b, fee, total);
				products.add(s);
			}else if(product.get(invoiceProducts.get(i)).getType().equals("L")){
				
				total = (productMultiplyer.get(invoiceProducts.get(i))/365)*annual;
				subtotal += total;
				String b = String.format("%s (%.0f days @ $%.2f/yr)", product.get(invoiceProducts.get(i)).getName(),
						productMultiplyer.get(invoiceProducts.get(i)), annual);
				s = String.format("%-11s %-71s $%10.2f $%10.2f\n", invoiceProducts.get(i), b, fee, total);
				products.add(s);
			}
			//Calculates tax percentage for a customer type
			if(customer.get(customerCode).getType().equals("C") && 
					(product.get(invoiceProducts.get(i)).getType().equals("C") || product.get(invoiceProducts.get(i)).getType().equals("L")) ){
				taxes += total*0.0425;
			}else if(customer.get(customerCode).getType().equals("C") && product.get(invoiceProducts.get(i)).getType().equals("E")){
				taxes += total*0.07;
			}
			
		}
		
		//Create the string of the requested products from the products ArrayList
		String a = "";
		for(int j = 0; j<products.size(); j++){
			a = a+products.get(j);
		}
		
		double grandTotal = subtotal+totalFee+taxes;
		
		String one = "Invoice " + invoiceID+"\n";
		String two = "========================\n";
		String three = "Salesperson: " +person.get(personCode).getLastName()+", "+
						person.get(personCode).getFirstName()+"\n";
		String four = "Customer Info:\n";
		String five = "-------------------------------------------\n";
		String six = String.format("%-11s %-78s %-10s %s\n", "Code", "Item", "Fees", "Total");
		String seven = "                                                                                    ========================\n";
		String eight = String.format("%-83s $%10.2f $%10.2f\n", "SUB-TOTALS", totalFee, subtotal);
		String nine = String.format("%-95s $%10.2f\n", "COMPLIANCE FEE", compFee);
		String ten = String.format("%-95s $%10.2f\n", "TAXES", taxes);
		String eleven = String.format("%-95s $%10.2f\n", "TOTAL", grandTotal);
		
		//customer.get(customerCode).toString() hodls the formatted customer information
		String invoice = one+two+three+four+customer.get(customerCode).toString()+five+six+a+seven+eight+nine+ten+eleven;
		return invoice;
	}

}
