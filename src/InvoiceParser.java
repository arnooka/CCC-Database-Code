import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
public class InvoiceParser {

	public static void Invoices() throws FileNotFoundException {
		//Print precursor information for executive invoices
		System.out.printf("Executive Summary Report\n=========================\n");
		System.out.printf("%-9s %-49s %-32s %-15s %-10s %-11s %s\n", 
								  "Invoice", "Customer", "Salesperson", "Subtotal", "Fees", "Taxes", "Total");
		
		//Get HashMaps that map to Customer, Product, and Person objects
		HashMap<String, Customer> customer = JsonXmlParser.getCustomerCode();
		HashMap<String, Product> product = JsonXmlParser.getProductCode();
		HashMap<String, Persons> person = JsonXmlParser.getPersonCode();
		
		//Establish Database Connection
		Connection conn = DatabaseConnection.getConnection();
				
		//ArrayList of Detailed Individual Invoices
		ArrayList<String> individualInvoice = new ArrayList<String>();
		
		//ArrayList holds the orderd product for an invoice and is later cleared for the next invoice
		ArrayList<String> invoiceProducts = new ArrayList<String>();
		
		double grandSubtotal = 0, grandFees = 0, grandTaxes = 0, grandTotal = 0;
		
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
	 		System.out.println("SQL Exception: ");
	 		e.printStackTrace();
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
 					+ "ip.hours as hours, "
 					+ "ip.units as units, "
 					+ "ip.daysBetween as days "
 					+ "FROM invoices as i "
 					+ "JOIN invoiceProducts as ip on i.invoiceID = ip.invoiceID "
 					+ "JOIN products as p on ip.productID = p.productID";
 		
 			PreparedStatement ps = null;
 			ResultSet rs = null;

 			ps = conn.prepareStatement(query);
 			rs = ps.executeQuery();
 			int c = 1;
 			while(rs.next()){
 				String invoiceID 	= rs.getString("Code");
 				String customerCode = rs.getString("customerCode");
 				String personCode 	= rs.getString("personCode");
 				String productCode  = rs.getString("productCode");
 				double h 			= rs.getDouble("hours");
 				double u 			= rs.getDouble("units");
 				double d 			= rs.getDouble("days");
 				String type 		= product.get(productCode).getType();
 				double total = 0, taxes = 0, endTotal = 0, fee = 0, hourly = 0, ppu = 0, annual = 0;
 				
 				int a = count.get(invoiceID);
 				
 				//Add productCode if not in invoiceProducts
 				if(!invoiceProducts.contains(productCode)){
 					invoiceProducts.add(productCode);
 				}
 				
 				//Equipment and Consultations multiplyer
 				if(type.equals("C")){
 					productMultiplyer.put(productCode, h);
 				}else if(type.equals("E")){
					productMultiplyer.put(productCode, u);
				//Licenses rate multiplyer
				}else if(type.equals("L")){
					productMultiplyer.put(productCode, d);
				}
 				
 				String amounts 	= Product.getCosts(productCode, product);
				String cost[] 	= amounts.split(",");
				hourly 			= Double.parseDouble(cost[0]);
				ppu 			= Double.parseDouble(cost[1]);
				annual 			= Double.parseDouble(cost[2]);
				fee 		   += Double.parseDouble(cost[3]);
				
				//Government fee	
				fee += governmentCustomer.getComplianceFee(customer, customerCode);
 				//Consultation fee
				fee += Consultations.consultationFee(product, productCode);
				//Calculate the invoice Subtotals and ending Totals
				total = Product.getTotal(product, productMultiplyer, productCode, hourly, ppu, annual);
				//Calculate tax percentage for a customer type
				taxes = Math.round(companyCustomer.taxes(customer, product, customerCode, productCode, total)*100);
				taxes = taxes/100;
				if(c == a){
					//Creates an individual invoice as a string and adds to an ArrayList
					individualInvoice.add(Invoice.toString(invoiceID, customerCode, productMultiplyer, customer,
							product, person, personCode, invoiceProducts));
					invoiceProducts.clear();
					c = 1;
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
				endTotal 		= Math.round((total+fee+taxes)*100);
				endTotal 		= endTotal/100;
				grandSubtotal  += total;
				grandFees 	   += fee;
				grandTaxes 	   += taxes;
				grandTotal 	   += endTotal;
				//Print out each Executive Summary invoice
				if(person.get(personCode) == null){
					System.out.printf("%-9s %-49s %-29s $%10.2f $%10.2f $%10.2f $%10.2f\n",
							invoiceID, customer.get(customerCode).getName(), "null", total, fee, taxes, endTotal);
				}else{
					System.out.printf("%-9s %-49s %-29s $%10.2f $%10.2f $%10.2f $%10.2f\n",
							invoiceID, customer.get(customerCode).getName(), person.get(personCode).getLastName()+", "+
							person.get(personCode).getFirstName(), total, fee, taxes, endTotal);
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
	 		System.out.println("SQL Exception: ");
	 		e.printStackTrace();
	 	}
	}
}

