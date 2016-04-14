package com.cinco;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

public class DBInvoiceParser {
	
	private static org.apache.log4j.Logger log = Logger.getLogger(DBInvoiceParser.class);
	
	public static void Invoices() throws FileNotFoundException {
		//Print precursor information for executive invoices
		System.out.printf("Executive Summary Report\n=========================\n");
		System.out.printf("%-9s %-49s %-32s %-15s %-10s %-11s %s\n", 
								  "Invoice", "Customer", "Salesperson", "Subtotal", "Fees", "Taxes", "Total");
		
		//Get HashMaps that map to Customer, Product, and Person objects
		HashMap<String, Customer> customer = DBObjectsParser.getCustomerCode();
		HashMap<String, Product> product = DBObjectsParser.getProductCode();
		HashMap<String, Persons> person = DBObjectsParser.getPersonCode();
		
		//Establish Database Connection
		Connection conn = DatabaseConnection.getConnection();
				
		//ArrayList of Detailed Individual Invoices
		ArrayList<String> individualInvoice = new ArrayList<String>();
		
		//ArrayList holds the orderd product for an invoice and is later cleared for the next invoice
		ArrayList<String> invoiceProducts = new ArrayList<String>();
		
		double grandSubtotal = 0, grandFees = 0, grandTaxes = 0, grandTotal = 0;
		
		//This query gets the number of products per invoice and stores the values
		//in a HashMap
		HashMap<String, Integer> count = new HashMap<String, Integer>();
		try{
			String query = "SELECT invoiceCode, count(productCode) from invoiceProducts group by invoiceCode";
			
			PreparedStatement ps = null;
 			ResultSet rs = null;
 			ps = conn.prepareStatement(query);
 			rs = ps.executeQuery();
 			while(rs.next()){
 				String invoiceCode = rs.getString("invoiceCode");
 				int c 			   = rs.getInt("count(productCode)");
 				count.put(invoiceCode, c);
 			}
		}catch(SQLException e){	
			log.error("SQLException: ", e);
			throw new RuntimeException(e);
		}
		
		//Invoice Query
		try {
			//HashMap that maps a product to its corresponding price multiplier value
			HashMap<String, Double> productMultiplyer = new HashMap<String, Double>();
 			
			String query = "SELECT  i.invoiceID AS invoiceID, "
 					+ "i.invoiceCode AS Code, "
 					+ "i.customerCode AS customerCode, "
 					+ "i.personCode AS personCode, "
 					+ "ip.productCode AS productCode, "
 					+ "ip.hours AS hours, "
 					+ "ip.units AS units, "
 					+ "ip.startDate AS start, "
 					+ "ip.endDate AS end "
 					+ "FROM invoices AS i "
 					+ "JOIN invoiceProducts AS ip ON i.invoiceID = ip.invoiceID "
 					+ "JOIN products AS p ON ip.productID = p.productID "
 					+ "ORDER BY i.invoiceCode";
 		
 			PreparedStatement ps = null;
 			ResultSet rs = null;

 			ps = conn.prepareStatement(query);
 			rs = ps.executeQuery();
 			int c = 1;
			double endTotal = 0, fee = 0, totalTax = 0, subTotal = 0;
 			while(rs.next()){
 				String invoiceID 	= rs.getString("Code");
 				String customerCode = rs.getString("customerCode");
 				String personCode 	= rs.getString("personCode");
 				String productCode  = rs.getString("productCode");
 				String start 		= rs.getString("start");
 				String end 			= rs.getString("end");
 				double h 			= rs.getDouble("hours");
 				double u 			= rs.getDouble("units");
 				String type 		= product.get(productCode).getType();
 				double  hourly = 0, ppu = 0, annual = 0, total = 0, taxes = 0;
 				//Gets the count
 				int a = count.get(invoiceID);
 				
 				//Add productCode if not in invoiceProducts
 				if(!invoiceProducts.contains(productCode)){
 					invoiceProducts.add(productCode);
 				}
 				
 				double d = 0;
 				
 				//Equipment and Consultations multiplyer
 				if(type.equals("C")){
 					productMultiplyer.put(productCode, h);
 				}else if(type.equals("E")){
					productMultiplyer.put(productCode, u);
				//Licenses rate multiplyer
				}else if(type.equals("L")){
					 d = License.daysBetween(start, end);
					productMultiplyer.put(productCode, d);
				}
 				
 				String amounts 	= Product.getCosts(productCode, product);
				String cost[] 	= amounts.split(",");
				hourly 			= Double.parseDouble(cost[0]);
				ppu 			= Double.parseDouble(cost[1]);
				annual 			= Double.parseDouble(cost[2]);
				fee 		   += Double.parseDouble(cost[3]);
				
				//Government fee	
				fee += GovernmentCustomer.getComplianceFee(customer, customerCode);
 				//Consultation fee
				fee += Consultations.consultationFee(product, productCode);
				//Calculate the invoice Subtotals and ending Totals
				total = Product.getTotal(product, productMultiplyer, productCode, hourly, ppu, annual);
				subTotal += total;
				//Calculate tax percentage for a customer type
				taxes = Math.round(CompanyCustomer.taxes(customer, product, customerCode, productCode, total)*100);
				taxes = taxes/100;
				totalTax += taxes;
				
				grandSubtotal  += total;
				grandFees 	   += fee;
				grandTaxes 	   += taxes;
				
				//Statement occurs when all products are found for an invoice
				if(c == a){
					endTotal = Math.round((subTotal+fee+totalTax)*100);
					endTotal = endTotal/100;
					grandTotal 	   += endTotal;
					//Creates an individual invoice as a string and adds to an ArrayList
					individualInvoice.add(Invoice.toString(invoiceID, customerCode, productMultiplyer, customer,
							product, person, personCode, invoiceProducts));
					//Print out each Executive Summary invoice
					if(person.get(personCode) == null){
						System.out.printf("%-9s %-49s %-29s $%10.2f $%10.2f $%10.2f $%10.2f\n",
								invoiceID, customer.get(customerCode).getName(), "null", subTotal, fee, totalTax, endTotal);
					}else{
						System.out.printf("%-9s %-49s %-29s $%10.2f $%10.2f $%10.2f $%10.2f\n",
								invoiceID, customer.get(customerCode).getName(), person.get(personCode).getLastName()+", "+
								person.get(personCode).getFirstName(), subTotal, fee, totalTax, endTotal);
					}
					invoiceProducts.clear();
					//Reset values
					c = 1;
					endTotal = 0;
					fee = 0;
					hourly = 0;
					ppu = 0;
					annual = 0;
					subTotal = 0;
					totalTax = 0;
				}else{
					//Switch coding language cuz itz not wurth it
					c++;
				}
				//Check for if the sales person exists
				if(person.get(personCode) == null){
					@SuppressWarnings("unused")
					Invoice invoice1 = new Invoice(invoiceID, customer.get(customerCode).getName(),
							"NULL", invoiceProducts);
				}else{
					@SuppressWarnings("unused")
					Invoice invoice1 = new Invoice(invoiceID, customer.get(customerCode).getName(),
							person.get(personCode).getLastName()+", "+person.get(personCode).getFirstName(),
							invoiceProducts);
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
	 	}catch(SQLException e){	
	 		log.error("SQLException: ", e);
			throw new RuntimeException(e);
	 	}
	}
}

