package com.cinco;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

public class DBObjectQueries {
	
	private static org.apache.log4j.Logger log = Logger.getLogger(DBObjectQueries.class);

	//Maps personCode to person
	private static HashMap<String, Person> persons = new HashMap<String, Person>();
	//customer code to customer
	private static HashMap<String, Customer> customers = new HashMap<String, Customer>();
	//productCode to product
	private static HashMap<String, Product> products = new HashMap<String, Product>();
	
	public static void DBObjects() throws SQLException {
		
		Connection conn = DatabaseConnection.getConnection();
		
		// This query gets the number of emails for a person
		HashMap<String, Integer> emailCount = new HashMap<String, Integer>();
		try{
			String query = "Select personCode, count(e.email) from persons as p "
					+ "LEFT JOIN emails as e on e.personID = p.personID "
					+ "Group by personCode";
			PreparedStatement ps = null;
			ResultSet rs = null;
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while(rs.next()){
				String personCode = rs.getString("personCode");
				int count 		   = rs.getInt("count(e.email)");
				emailCount.put(personCode, count);
			}
		}catch(SQLException e){	
				log.error("SQLException: ", e);
			throw new RuntimeException(e);
		}
		
		// Person Query
 		try {
 			String query = "SELECT  p.personCode AS personCode, p.personLastName AS lastName,"
					+ " p.personFirstName AS firstName, "
					+ "a.street  AS street, "
					+ "a.city  AS city, "
					+ "a.state AS state, "
					+ "a.zip AS zip, "
					+ "a.country AS country, "
					+ "e.email AS email "
					+ "FROM persons AS p "
					+ "LEFT JOIN addresses AS a ON p.addressID = a.addressID " 
					+ "LEFT JOIN emails AS e ON e.personID = p.personID "
					+ "ORDER BY personCode ASC";
 		
 			PreparedStatement ps = null;
 			ResultSet rs = null;
 
 			ps = conn.prepareStatement(query);
 			rs = ps.executeQuery();
 			int counter = 1;
 			ArrayList<String> emails = new ArrayList<String>();
 			while(rs.next()){
 				String personCode = rs.getString("personCode");
 				String lastName   = rs.getString("lastName");
 				String firstName  = rs.getString("firstName");
 				String street     = rs.getString("street");
 				String city       = rs.getString("city");
 				String state      = rs.getString("state");
 				String zip        = rs.getString("zip");
 				String country    = rs.getString("country");
 				String email      = rs.getString("email");
 				int emailCounter = emailCount.get(personCode);
 				if(!emails.contains(email)){
 					emails.add(email);
 				}
 				// Statement that checks when all emails are found for a person
 				if(counter >= emailCounter){
 					//Generate a new instance for a person with queried information
 					Person person1 = new Person(personCode, firstName, lastName,
 	 						new Address(street, city, state, zip, country), emails);
 					// Map personCode to an instance of a person
 	 				persons.put(personCode, person1);
 	 				emails.clear();
 	 				counter = 1;
 				}else{
 					counter++;
 				}
 				
 			}
 			ps.close();
 			rs.close();
	 	}catch(SQLException e){	
	 		log.error("SQLException: ", e);
			throw new RuntimeException(e);
	 	}
		
 		// Customer Query
 		try{
 			String query = "SELECT c.customerCode AS customerCode, "
 						+ "c.customerName AS Name, "
 						+ "c.customerType AS type, "
 						+ "a.street  AS street, " 
 						+ "a.city  AS city, "
 						+ "a.state AS state, "
 						+ "a.zip AS zip, "
 						+ "a.country AS country,"
 						+ "p.personCode AS personCode, "
 						+ "p.personFirstName AS FirstName, "
 						+ "p.personLastName AS LastName "
 						+ "FROM customers AS c "
 						+ "LEFT JOIN addresses AS a ON c.addressID = a.addressID "
 						+ "LEFT JOIN persons AS p ON p.personID = c.personID";
		
			PreparedStatement ps = null;
			ResultSet rs = null;
        
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while(rs.next()){
				String customerCode = rs.getString("customerCode");
				String customerType = rs.getString("type");
				String personCode   = rs.getString("personCode");
				String customerName = rs.getString("Name");
				String street       = rs.getString("street");
				String city         = rs.getString("city");
				String state        = rs.getString("state");
				String zip          = rs.getString("zip");
				String country      = rs.getString("country");
				
				Person primaryContact = persons.get(personCode);
				//Generate a new instance for a person with queried information
				if(customerType.equals("C")){
					CompanyCustomer customer1 = new CompanyCustomer(customerCode, primaryContact, customerName, 
							new Address(street, city, state, zip, country), customerType);
					//Map customerCode to an instance of a company customer
					customers.put(customerCode, customer1);
				}else if(customerType.equals("G")){
					GovernmentCustomer government1 = new GovernmentCustomer(customerCode, primaryContact, customerName,
							new Address(street, city, state, zip, country), customerType);
					//Map customerCode to an instance of a government customer
					customers.put(customerCode, government1);
				}
			}
			ps.close();
 			rs.close();
 		}catch(SQLException e){	
 			log.error("SQLException: ", e);
			throw new RuntimeException(e);
	 	}
 		
 		
/* 		.----.______
 		|rare pepe's|
 		|    ___________
 		|   /          /
 		|  /          /
 		| /          /
 		|/__________/
 */		
 		
 		// Product Query
 		try{
 			String query = "SELECT  p.prodCode AS Code, " 
 						+ "p.prodType AS type, "
 						+ "p.prodName  AS name, "
 						+ "p.ppu  AS ppu," 
 						+ "p.fee AS fee," 
 						+ "p.hourly AS hourly," 
 						+ "p.yearly AS yearly "
 						+ "FROM products AS p";
		
			PreparedStatement ps = null;
			ResultSet rs = null;
        
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while(rs.next()){
				String productCode = rs.getString("Code");
				String type 	   = rs.getString("type");
				String productName = rs.getString("name");
				double ppu 		   = rs.getDouble("ppu");
				double fee 		   = rs.getDouble("fee");
				double hourly 	   = rs.getDouble("hourly");
				double yearly 	   = rs.getDouble("yearly");
				
				// Generate a new instance for a product with queried information
				if(type.equals("C")){
					// Get an instance of a person throught the consultCode
					Person a = (Person) persons.get(productCode);
					//Generate an instance of a consultation
					Consultations consultation1 = new Consultations(productCode, productName, a, hourly, type);
					// Map a product code to an instance of a consultation
					products.put(productCode, consultation1);
					
				}else if(type.equals("E")){
					// Generate an instance of an equipment
					Equipment equipment1 = new Equipment(productCode, productName, ppu, type);
					// Map a product code to an instance of an equipment
					products.put(productCode, equipment1);
					
				}else if(type.equals("L")){
					// Generate an instance of a license
					License license1 = new License( productName, productCode, fee, yearly, type);
					// Map a product code to an instance of a license
					products.put(productCode, license1);
				}
			}
			ps.close();
 			rs.close();
 		}catch(SQLException e){	
 			log.error("SQLException: ", e);
			throw new RuntimeException(e);
	 	}
 		conn.close();
	}
	
	public static HashMap<String, Person> getPersonsMap() {
		return persons;
	}

	public static HashMap<String, Customer> getCustomersMap() {
		return customers;
	}
	
	public static HashMap<String, Product> getProductsMap() {
		return products;
	}

}
