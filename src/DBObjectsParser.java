import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

public class DBObjectsParser {
	
	private static org.apache.log4j.Logger log = Logger.getLogger(DBObjectsParser.class);

	//Maps personCode to person
	private static HashMap<String, Persons> personCode1 = new HashMap<String, Persons>();
	//customer code to customer
	private static HashMap<String, Customer> customerCode1 = new HashMap<String, Customer>();
	//productCode to product
	private static HashMap<String, Product> productCode1 = new HashMap<String, Product>();
	
	public static void DBObjects() {
		
		Connection conn = DatabaseConnection.getConnection();
		//Person Query
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
					+ "JOIN addresses AS a ON p.addressID = a.addressID " 
					+ "JOIN emails AS e ON e.personID = p.personID ORDER BY personCode ASC";
 		
 			PreparedStatement ps = null;
 			ResultSet rs = null;
 
 			ps = conn.prepareStatement(query);
 			rs = ps.executeQuery();
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
 				
 				//Generate a new instance for a person with queried information
				Persons person1 = new Persons(personCode, firstName, lastName,
 						new Address(street, city, state, zip, country), email);
				//Map personCode to an instance of a person
 				personCode1.put(personCode, person1);
 			}
	 	}catch(SQLException e){	
	 		log.error("SQLException: ", e);
			throw new RuntimeException(e);
	 	}
		
 		//Customer Query
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
 						+ "FROM customers AS c JOIN addresses AS a ON c.addressID = a.addressID "
 						+ "JOIN persons AS p ON p.personID = c.personID";
		
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
				
				Persons primaryContact = personCode1.get(personCode);
				
				//Generate a new instance for a person with queried information
				if(customerType.equals("C")){
					companyCustomer customer1 = new companyCustomer(customerCode, primaryContact, customerName, 
							new Address(street, city, state, zip, country), customerType);
					//Map customerCode to an instance of a company customer
					customerCode1.put(customerCode, customer1);
				}else if(customerType.equals("G")){
					governmentCustomer government1 = new governmentCustomer(customerCode, primaryContact, customerName,
							new Address(street, city, state, zip, country), customerType);
					//Map customerCode to an instance of a government customer
					customerCode1.put(customerCode, government1);
				}
			}
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
 		
 		//Product Query
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
				
				//Generate a new instance for a product with queried information
				if(type.equals("C")){
					//Get an instance of a person throught the consultCode
					Persons a = (Persons) personCode1.get(productCode);
					//Generate an instance of a consultation
					Consultations consultation1 = new Consultations(productCode, productName, a, hourly, type);
					//Map a product code to an instance of a consultation
					productCode1.put(productCode, consultation1);
					
				}else if(type.equals("E")){
					//Generate an instance of an equipment
					Equipment equipment1 = new Equipment(productCode, productName, ppu, type);
					//Map a product code to an instance of an equipment
					productCode1.put(productCode, equipment1);
					
				}else if(type.equals("L")){
					//Generate an instance of a license
					License license1 = new License( productName, productCode, fee, yearly, type);
					//Map a product code to an instance of a license
					productCode1.put(productCode, license1);
				}
			}
 		}catch(SQLException e){	
 			log.error("SQLException: ", e);
			throw new RuntimeException(e);
	 	}
	}
	
	public static HashMap<String, Persons> getPersonCode() {
		return personCode1;
	}

	public static HashMap<String, Customer> getCustomerCode() {
		return customerCode1;
	}
	
	public static HashMap<String, Product> getProductCode() {
		return productCode1;
	}

}
