package com.cinco;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

public class DBInvoiceQueries {
	
	private static org.apache.log4j.Logger log = Logger.getLogger(DBInvoiceQueries.class);
	//Create an ArrayList of invoice objects
	private static ArrayList<Invoice> invoices = new ArrayList<Invoice>();
	
	public static void Invoices() throws SQLException {
		
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// NOT NEEDED FOR ASSIGNMENT 7
		//Print precursor information for executive invoices
		System.out.printf("Executive Summary Report\n=========================\n");
		System.out.printf("%-9s %-49s %-32s %-15s %-10s %-11s %s\n", 
						  "Invoice", "Customer", "Salesperson", "Subtotal", "Fees", "Taxes", "Total");
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		// Establish Database Connection
		Connection conn = DatabaseConnection.getConnection();
		// ArrayList of Detailed Individual Invoices
		ArrayList<String> individualInvoice = new ArrayList<String>();
		// ArrayList holds the orderd product for an invoice
		ArrayList<String> invoiceProducts = new ArrayList<String>();
		
		// Get HashMaps that map to Customer, Product, and Person objects
		HashMap<String, Customer> customers = DBObjectQueries.getCustomersMap();
		HashMap<String, Product> products = DBObjectQueries.getProductsMap();
		HashMap<String, Person> persons = DBObjectQueries.getPersonsMap();
		
		HashMap<String, Integer> invoiceProductCount = new HashMap<String, Integer>();
		try{
			// This query gets the number of products per invoice and 
			// saves the values into the invoiceProductCount HashMap
			String query = "SELECT invoiceCode, count(productCode) from invoiceProducts group by invoiceCode";
			
			PreparedStatement ps = null;
 			ResultSet rs = null;
 			ps = conn.prepareStatement(query);
 			rs = ps.executeQuery();
 			while(rs.next()){
 				String invoiceCode = rs.getString("invoiceCode");
 				int productCount   = rs.getInt("count(productCode)");
 				invoiceProductCount.put(invoiceCode, productCount);
 			}
		}catch(SQLException e){	
			log.error("SQLException: ", e);
			throw new RuntimeException(e);
		}
		
		// Invoice Query
		double grandSubtotal = 0, grandFees = 0, grandTaxes = 0, grandTotal = 0;
		try {
			// HashMap that maps a product to its corresponding price multiplier value
			HashMap<String, Double> productMultiplyer = new HashMap<String, Double>();
			String query = "SELECT  i.invoiceID AS invoiceID, "
 					+ "i.invoiceCode AS Code, "
 					+ "c.customerName AS customerName, "
 					+ "i.customerCode AS customerCode, "
 					+ "i.personCode AS personCode, "
 					+ "ip.productCode AS productCode, "
 					+ "ip.hours AS hours, "
 					+ "ip.units AS units, "
 					+ "p.prodType AS type, "
 					+ "ip.startDate AS start, "
 					+ "ip.endDate AS end "
 					+ "FROM invoices AS i "
 					+ "JOIN invoiceProducts AS ip ON i.invoiceID = ip.invoiceID "
 					+ "JOIN products AS p ON ip.productID = p.productID "
 					+ "JOIN customers AS c ON i.customerID = c.customerID "
 					+ "ORDER BY i.invoiceCode";
					// Uncomment to order invoices alphabetically by a customer's name
// 					+ "ORDER BY c.customerName, i.invoiceCode DESC";
					
 			PreparedStatement ps = null;
 			ResultSet rs = null;
 			ps = conn.prepareStatement(query);
 			rs = ps.executeQuery();
 			
 			int counter = 1;
			double endTotal = 0, totalTax = 0, subTotal = 0, totalFee = 0;
 			while(rs.next()){
 				String invoiceCode 	= rs.getString("Code");
 				String customerCode = rs.getString("customerCode");
 				String personCode 	= rs.getString("personCode");
 				String productCode  = rs.getString("productCode");
 				String type 		= rs.getString("type");
 				String start 		= rs.getString("start");
 				String end 			= rs.getString("end");
 				double hours 		= rs.getDouble("hours");
 				double units 		= rs.getDouble("units");
 				
 				// Get a customer/product/person from the HashMap
 				Person salesPerson = persons.get(personCode);
 				Customer customer  = customers.get(customerCode);
 				Product product    = products.get(productCode);
 				
 				double hourly = 0, ppu = 0, annual = 0, total = 0, 
 						taxes = 0, compFee = 0, fee = 0, days = 0;
 				
 				// Gets the count
 				int productCount = invoiceProductCount.get(invoiceCode);
 				
 				// Add productCode if not in invoiceProducts
 				if(!invoiceProducts.contains(productCode)){
 					invoiceProducts.add(productCode);
 				}
 				
 				// Equipment and Consultations multiplyer
 				if(type.equals("C")){
 					productMultiplyer.put(productCode, hours);
 				}else if(type.equals("E")){
					productMultiplyer.put(productCode, units);
				// Licenses rate multiplyer
				}else if(type.equals("L")){
					 days = License.daysBetween(start, end);
					productMultiplyer.put(productCode, days);
				}
 				
 				// Get respective ammounts for a product
 				String amounts 	= Product.getCosts(product);
				String cost[] 	= amounts.split(",");
				hourly 			= Double.parseDouble(cost[0]);
				ppu 			= Double.parseDouble(cost[1]);
				annual 			= Double.parseDouble(cost[2]);
				fee 		   += Double.parseDouble(cost[3]);
				
				// Get the Compliance fee
				if(customer != null){
					if(customer.getType().equals("G")){
						compFee = customer.getComplianceFee();
					}else{
						compFee = 0;
					}
				}
				
 				// Get consultation fee
				fee 	 += product.getConsultationFee();
				// Calculate the invoice Subtotals and ending Totals
				total 	  = Product.getTotal(product, productMultiplyer, hourly, ppu, annual);
				subTotal += total;
				// Calculate tax percentage for a Company Customer
				taxes 	  = Math.round(CompanyCustomer.taxes(customer, product, total)*100);
				taxes 	  = taxes/100;
				totalTax += taxes;
				
				// totalFee holds all fees for one invoice including compliance fee
				totalFee 	   += fee;
				grandSubtotal  += total;
				grandFees 	   += fee;
				grandTaxes 	   += taxes;
				
				// Statement occurs when all products are found for an invoice
				if(counter == productCount){
					double totalFees = totalFee+compFee; 
					grandFees  += compFee;
					endTotal 	= Math.round((subTotal+totalFee+compFee+totalTax)*100);
					endTotal 	= endTotal/100;
					grandTotal += endTotal;
					
					// Create an invoice object
					Invoice invoice2 = new Invoice(invoiceCode, customer, salesPerson, invoiceProducts, subTotal, 
							totalFees, totalTax, endTotal);
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			// NOT NEEDED FOR ASSIGNMETN 7
					@SuppressWarnings("unused")
					Invoice invoice = new Invoice(invoiceCode, customer, salesPerson, invoiceProducts);
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					// Add invoice to arraylist
					invoices.add(invoice2);
					//Creates an individual invoice as a string and adds to an ArrayList
					individualInvoice.add(Invoice.toString(invoiceCode, customerCode, productMultiplyer, customer,
							products, salesPerson, invoiceProducts));
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			// NOT NEEDED FOR ASSIGNMENT 7
					// Print out each Executive Summary invoice
					if(salesPerson == null){
						System.out.printf("%-9s %-49s %-29s $%10.2f $%10.2f $%10.2f $%10.2f\n",
								invoiceCode, customer.getName(), "null",
								subTotal, totalFee+compFee, totalTax, endTotal);
					}else{
						System.out.printf("%-9s %-49s %-29s $%10.2f $%10.2f $%10.2f $%10.2f\n",
								invoiceCode, customer.getName(), salesPerson.getLastName()+", "+
								salesPerson.getFirstName(), subTotal, totalFee+compFee, totalTax, endTotal);
					}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					
					
					// Reset to store new products for new invoice
					invoiceProducts.clear();
					// Reset values
					counter = 1; endTotal = 0; totalFee = 0; subTotal = 0; totalTax = 0;
				}else{
					counter++;
				}
			}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 		// NOT NEEDED FOR ASSIGNMENT 7
 			// Print the remaining details for the Executive Report
 			System.out.println("===================================================================="
 					 + "=====================================================================");
 			System.out.printf("%-89s $%10.2f $%10.2f $%10.2f $%10.2f\n\n\n\n\n", 
 							  "TOTALS", grandSubtotal, grandFees, grandTaxes, grandTotal);
 			
 			System.out.printf("Individual Invoice Detail Reports\n==================================================\n");
 			
 			// Print the Detailed Invoice Report
 			for(int i = 0; i<individualInvoice.size(); i++){
 				System.out.println(individualInvoice.get(i));
 			}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 			ps.close();
 			rs.close();
	 	}catch(SQLException e){	
	 		log.error("SQLException: ", e);
			throw new RuntimeException(e);
	 	}
		
		conn.close();
	}
	
	public static ArrayList<Invoice> getInvoiceArrayList(){
		return invoices;
	}
	
}
