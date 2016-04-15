package com.cinco;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
/**
	 * This is a collection of utility methods that define a general API for
	 * interacting with the database supporting this application.
	 *
	 */

public class InvoiceData {
	
	private static org.apache.log4j.Logger log = Logger.getLogger(DBObjectsParser.class);

		/**
		 * Method that removes every person record from the database
		 */
		public static void removeAllPersons() {
			Connection conn = DatabaseConnection.getConnection();
			PreparedStatement ps = null;
			try {
	 			String s  = "DELETE FROM persons;";
	 			ps = conn.prepareStatement(s);
	 			ps.executeUpdate();
	 			ps.close();
	 			conn.close();
		 	}catch(SQLException e){	
		 		log.error("SQLException: ", e);
				throw new RuntimeException(e);
		 	}
		}

		/**
		 * Removes the person record from the database corresponding to the
		 * provided <code>personCode</code>
		 * @param personCode
		 */
		public static void removePerson(String personCode) {
			Connection conn = DatabaseConnection.getConnection();
			PreparedStatement ps = null;
			try{
				String s = "DELETE FROM persons WHERE personCode = ? ;";
				ps = conn.prepareStatement(s);
				ps.setString(1, personCode);
				ps.executeUpdate();
				
				ps.close();
				conn.close();
			}catch(SQLException e){	
		 		log.error("SQLException: ", e);
				throw new RuntimeException(e);
		 	}
		}
		
		/**
 * INSERT INTO table_name (column1,column2,column3,...)
		 * VALUES (value1,value2,value3,...);
		 * 
		 * Method to add a person record to the database with the provided data. 
		 * @param personCode
		 * @param firstName
		 * @param lastName
		 * @param street
		 * @param city
		 * @param state
		 * @param zip
		 * @param country
		 */
		public static void addPerson(String personCode, String firstName, String lastName, 
				String street, String city, String state, String zip, String country) {
			Connection conn = DatabaseConnection.getConnection();
			PreparedStatement ps = null;
			ResultSet rs = null;
			try{
				//Add address to databases
				String s = "INSERT INTO addresses(street, city, state, zip, country) VALUES "
						+ "(?, ?, ?, ?, ?);";
				ps = conn.prepareStatement(s);
				ps.setString(1, street);
				ps.setString(2, city);
				ps.setString(3, state);
				ps.setString(4, zip);
				ps.setString(5, country);
				ps.executeUpdate();
				
				//Query for newly added addressID
				String s2 = "SELECT addressID FROM addresses WHERE street = ? AND city = ?;";
				ps = conn.prepareStatement(s2);
				ps.setString(1, street);
				ps.setString(2, city);
				rs = ps.executeQuery();
				rs.next();
				int addressID = 0;
				if(!rs.next()){
					//rs.next() = null;
				}else{
					addressID = rs.getInt("addressID");
				}
				
				
				//Add person to database
				String s3 = "INSERT INTO persons(personCode, personFirstName, personLastName, addressID) VALUES "
						+ "( ?, ?, ?, ?);";
				ps = conn.prepareStatement(s3);
				ps.setString(1, personCode);
				ps.setString(2, firstName);
				ps.setString(3, lastName);
				ps.setInt(4, addressID);
				ps.executeUpdate();
				
				ps.close();
				rs.close();
				conn.close();
			}catch(SQLException e){	
		 		log.error("SQLException: ", e);
				throw new RuntimeException(e);
		 	}
		}
		
		/**
		 * Adds an email record corresponding person record corresponding to the
		 * provided <code>personCode</code>
		 * @param personCode
		 * @param email
		 */
		public static void addEmail(String personCode, String email) {
			Connection conn = DatabaseConnection.getConnection();
			PreparedStatement ps = null;
			ResultSet rs = null;
			try{
				String s = "SELECT personID FROM persons WHERE personCode = ?;";
				ps = conn.prepareStatement(s);
				ps.setString(1, personCode);
				rs = ps.executeQuery();
				rs.next();
				int personID = rs.getInt("personID");
				
				String s2 = "INSERT INTO emails(personID, email) VALUES "
						+ "(?, ?);";
				ps = conn.prepareStatement(s2);
				ps.setInt(1, personID);
				ps.setString(2, email);
				ps.executeUpdate();
				
				ps.close();
				rs.close();
				conn.close();
			}catch(SQLException e){	
		 		log.error("SQLException: ", e);
				throw new RuntimeException(e);
		 	}
		}
		
		/**
		 * Method that removes every customer record from the database
		 */
		public static void removeAllCustomers() {
			Connection conn = DatabaseConnection.getConnection();
			PreparedStatement ps = null;
			try {
	 			String s  = "DELETE FROM customers;";
	 			ps = conn.prepareStatement(s);
	 			ps.executeUpdate();
	 			
	 			ps.close();
				conn.close();
		 	}catch(SQLException e){	
		 		log.error("SQLException: ", e);
				throw new RuntimeException(e);
		 	}
		}

		public static void addCustomer(String customerCode, String type, String primaryContactPersonCode, String name, 
				String street, String city, String state, String zip, String country) {
			Connection conn = DatabaseConnection.getConnection();
			PreparedStatement ps = null;
			ResultSet rs = null;
			try{
				//Add address to databases
				String s = "INSERT INTO addresses(street, city, state, zip, country) VALUES "
						+ "(?, ?, ?, ?, ?);";
				ps = conn.prepareStatement(s);
				ps.setString(1, street);
				ps.setString(2, city);
				ps.setString(3, state);
				ps.setString(4, zip);
				ps.setString(5, country);
				ps.executeUpdate();
				
				//Query for newly added addressID
				String s2 = "SELECT addressID FROM addresses WHERE street = ? AND city = ?;";
				ps = conn.prepareStatement(s2);
				ps.setString(1, street);
				ps.setString(2, city);
				rs = ps.executeQuery();
				rs.next();
				int addressID = rs.getInt("addressID");
				
				
				String s3 = "SELECT personID FROM persons WHERE personCode = ?;";
				ps = conn.prepareStatement(s3);
				ps.setString(1, primaryContactPersonCode);
				rs = ps.executeQuery();
				rs.next();
				int personID = rs.getInt("personID");
				
				String s4 = "insert into customers (customerCode, customerName, customerType, addressID, personID) values "
						+ "(?, ?, ?, ?, ?);";
				ps = conn.prepareStatement(s4);
				ps.setString(1, customerCode);
				ps.setString(2, name);
				ps.setString(3, type);
				ps.setInt(4, addressID);
				ps.setInt(5, personID);
				ps.executeUpdate();
				
				ps.close();
				rs.close();
				conn.close();
			}catch(SQLException e){	
		 		log.error("SQLException: ", e);
				throw new RuntimeException(e);
		 	}
		}

		/**
		 * Removes all product records from the database
		 */
		public static void removeAllProducts() {
			Connection conn = DatabaseConnection.getConnection();
			PreparedStatement ps = null;
			try {
	 			String s  = "DELETE FROM products;";
	 			ps = conn.prepareStatement(s);
	 			ps.executeUpdate();
	 			
	 			ps.close();
				conn.close();
		 	}catch(SQLException e){	
		 		log.error("SQLException: ", e);
				throw new RuntimeException(e);
		 	}
		}
	
		/**
		 * Removes a particular product record from the database corresponding to the
		 * provided <code>productCode</code>
		 * @param assetCode
		 */
		public static void removeProduct(String productCode) {
			Connection conn = DatabaseConnection.getConnection();
			PreparedStatement ps = null;
			try{
				String s = "DELETE FROM products WHERE prodCode = ?;";
				ps = conn.prepareStatement(s);
				ps.setString(1, productCode);
				ps.executeUpdate();
				
				ps.close();
				conn.close();
			}catch(SQLException e){	
		 		log.error("SQLException: ", e);
				throw new RuntimeException(e);
		 	}
		}

		/**
		 * Adds an equipment record to the database with the
		 * provided data.  
		 */
		public static void addEquipment(String productCode, String name, Double pricePerUnit) {
			Connection conn = DatabaseConnection.getConnection();
			PreparedStatement ps = null;
			try{
				String s = "INSERT INTO products (prodCode, prodType, prodName, personID, ppu, fee, hourly, yearly) VALUES "
						+ "( ?, 'E', ?, null, ?, 0, 0, 0);";
				ps = conn.prepareStatement(s);
				ps.setString(1, productCode);
				ps.setString(2, name);
				ps.setDouble(3, pricePerUnit);
				ps.executeUpdate();
				
				ps.close();
				conn.close();
			}catch(SQLException e){	
		 		log.error("SQLException: ", e);
				throw new RuntimeException(e);
		 	}
		}
		
		/**
		 * Adds an license record to the database with the
		 * provided data.  
		 */
		public static void addLicense(String productCode, String name, double serviceFee, double annualFee) {
			Connection conn = DatabaseConnection.getConnection();
			PreparedStatement ps = null;
			try{
				String s = "INSERT INTO products (prodCode, prodType, prodName, personID, ppu, fee, hourly, yearly) VALUES "
//						+"('"+productCode+"', 'L', '"+name+"', 1, 0, "+serviceFee+", 0, "+annualFee+");";
						+ "(? , 'L', ?, null, 0, ?, 0, ?);";
				ps = conn.prepareStatement(s);
				ps.setString(1, productCode);
				ps.setString(2, name);
				ps.setDouble(3, serviceFee);
				ps.setDouble(4, annualFee);
				ps.executeUpdate();
				
				ps.close();
				conn.close();
			}catch(SQLException e){	
		 		log.error("SQLException: ", e);
				throw new RuntimeException(e);
		 	}
		}
		/**
		 * Adds an consultation record to the database with the
		 * provided data.  
		 */
		public static void addConsultation(String productCode, String name, String consultantPersonCode, Double hourlyFee) {
			Connection conn = DatabaseConnection.getConnection();
			PreparedStatement ps = null;
			ResultSet rs = null;
			try{
				String s = "SELECT personID FROM persons WHERE personCode = ?;";
				ps = conn.prepareStatement(s);
				ps.setString(1, consultantPersonCode);
				rs = ps.executeQuery();
				rs.next();
				int personID = rs.getInt("personID");
				
				
				String s2 = "INSERT INTO products (prodCode, prodType, prodName, personID, ppu, fee, hourly, yearly) VALUES "
						+ "( ?, 'C', ?, ?, 0, 0, ?, 0);";
				ps = conn.prepareStatement(s2);
				ps.setString(1, productCode);
				ps.setString(2, name);
				ps.setInt(3,personID);
				ps.setDouble(4, hourlyFee);
				ps.executeUpdate();
				
				ps.close();
				rs.close();
				conn.close();
			}catch(SQLException e){	
		 		log.error("SQLException: ", e);
				throw new RuntimeException(e);
		 	}
		}
		
		/**
		 * Removes all invoice records from the database
		 */
		public static void removeAllInvoices() {
			Connection conn = DatabaseConnection.getConnection();
			PreparedStatement ps = null;
			try {
	 			String s  = "DELETE FROM invoices;";
	 			ps = conn.prepareStatement(s);
	 			ps.executeUpdate();
	 			
	 			ps.close();
				conn.close();
		 	}catch(SQLException e){	
		 		log.error("SQLException: ", e);
				throw new RuntimeException(e);
		 	}
		}
		
		/**
		 * Removes the invoice record from the database corresponding to the
		 * provided <code>invoiceCode</code>
		 * @param invoiceCode
		 */
		public static void removeInvoice(String invoiceCode) {
			Connection conn = DatabaseConnection.getConnection();
			PreparedStatement ps = null;
			try{
				String s = "DELETE FROM invoices WHERE InvoiceCode = ?;";
				ps = conn.prepareStatement(s);
				ps.setString(1, invoiceCode);
				ps.executeUpdate();
				
				ps.close();
				conn.close();
			}catch(SQLException e){	
		 		log.error("SQLException: ", e);
				throw new RuntimeException(e);
		 	}
		}
		
		/**
		 * Adds an invoice record to the database with the given data.  
		 */
		public static void addInvoice(String invoiceCode, String customerCode, String salesPersonCode) {
			Connection conn = DatabaseConnection.getConnection();
			PreparedStatement ps = null;
			try{
				String s = "insert into invoices (InvoiceCode, customerCode, personCode, customerID, personID) values "
						+ "(?, ?, ?,"
						+ "(select customerID from customers where customerCode = ?),"
						+ "(select personID from persons where personCode = ?));";
				ps = conn.prepareStatement(s);
				ps.setString(1, invoiceCode);
				ps.setString(2, customerCode);
				ps.setString(3, salesPersonCode);
				ps.setString(4, customerCode);
				ps.setString(5, salesPersonCode);
				ps.executeUpdate();
				
				ps.close();
				conn.close();
			}catch(SQLException e){	
		 		log.error("SQLException: ", e);
				throw new RuntimeException(e);
		 	}
		}
		
		/**
		 * Adds a particular equipment (corresponding to <code>productCode</code> to an 
		 * invoice corresponding to the provided <code>invoiceCode</code> with the given
		 * number of units
		 */
		public static void addEquipmentToInvoice(String invoiceCode, String productCode, int numUnits) {
			Connection conn = DatabaseConnection.getConnection();
			PreparedStatement ps = null;
			try{
				String s = "insert into invoiceProducts (productID, invoiceID, invoiceCode, productCode, hours, units, startDate, endDate) values "
						+ "((select productID From products where prodCode = ?),"
						+ "(select invoiceID from invoices where invoiceCode = ?),"
						+ "?, ?, 0, ?, null, null);";
				ps = conn.prepareStatement(s);
				ps.setString(1, productCode);
				ps.setString(2, invoiceCode);
				ps.setString(3, invoiceCode);
				ps.setString(4, productCode);
				ps.setInt(5, numUnits);
				ps.executeUpdate();
				
				ps.close();
				conn.close();
			}catch(SQLException e){	
		 		log.error("SQLException: ", e);
				throw new RuntimeException(e);
		 	}
		}
		
		/**
		 * Adds a particular equipment (corresponding to <code>productCode</code> to an 
		 * invoice corresponding to the provided <code>invoiceCode</code> with the given
		 * begin/end dates
		 */
		public static void addLicenseToInvoice(String invoiceCode, String productCode, String startDate, String endDate) {
			Connection conn = DatabaseConnection.getConnection();
			PreparedStatement ps = null;
			try{
				String s = "insert into invoiceProducts (productID, invoiceID, invoiceCode, productCode, hours, units, startDate, endDate) values "
						+ "((select productID From products where prodCode = ?),"
						+ "(select invoiceID from invoices where invoiceCode = ?),"
						+ "?, ?, 0, 0, ?, ?);";
				ps = conn.prepareStatement(s);
				ps.setString(1, productCode);
				ps.setString(2, invoiceCode);
				ps.setString(3, invoiceCode);
				ps.setString(4, productCode);
				ps.setString(5, startDate);
				ps.setString(6, endDate);
				ps.executeUpdate();
				
				ps.close();
				conn.close();
			}catch(SQLException e){
		 		log.error("SQLException: ", e);
				throw new RuntimeException(e);
		 	}
		}
		
		/**
		 * Adds a particular equipment (corresponding to <code>productCode</code> to an 
		 * invoice corresponding to the provided <code>invoiceCode</code> with the given
		 * number of billable hours.
		 */
		public static void addConsultationToInvoice(String invoiceCode, String productCode, double numHours) {
			Connection conn = DatabaseConnection.getConnection();
			PreparedStatement ps = null;
			try{
				String s = "insert into invoiceProducts (productID, invoiceID, invoiceCode, productCode, hours, units, startDate, endDate) values "
						+ "((select productID From products where prodCode = ?),"
						+ "(select invoiceID from invoices where invoiceCode = ?),"
						+ "?, ?, ?, 0, null, null);";
				ps = conn.prepareStatement(s);
				ps.setString(1, productCode);
				ps.setString(2, invoiceCode);
				ps.setString(3, invoiceCode);
				ps.setString(4, productCode);
				ps.setDouble(5, numHours);
				ps.executeUpdate();
				
				ps.close();
				conn.close();
			}catch(SQLException e){	
		 		log.error("SQLException: ", e);
				throw new RuntimeException(e);
		 	}
		}
	}
