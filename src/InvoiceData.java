import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.mysql.jdbc.Statement;
	
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
			Statement st =  null;
	 		try {
	 			st = (Statement) conn.createStatement();
	 			String s  = "ALTER TABLE customers DROP FOREIGN KEY customers_ibfk_2;";
	 			String s2 = "ALTER TABLE products  DROP FOREIGN KEY products_ibfk_1;";
	 			String s3 = "ALTER TABLE invoices  DROP FOREIGN KEY invoices_ibfk_2;";
	 			String s4 = "ALTER TABLE persons   DROP FOREIGN KEY persons_ibfk_1;";
	 			String s5 = "ALTER TABLE emails    DROP FOREIGN KEY emails_ibfk_1;";
	 			String s6 = "SET SQL_SAFE_UPDATES = 0;";
	 			String s7 = "DELETE FROM persons;";
	 			String s8 = "DELETE FROM emails;";
	 			String s9 = "SET SQL_SAFE_UPDATES = 1;";
	 			
	 			st.executeUpdate(s);
	 			st.executeUpdate(s2);
	 			st.executeUpdate(s3);
	 			st.executeUpdate(s4);
	 			st.executeUpdate(s5);
	 			st.executeUpdate(s6);
	 			st.executeUpdate(s7);
	 			st.executeUpdate(s8);
	 			st.executeUpdate(s9);
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
			Statement st =  null;
			
			try{
				st = (Statement) conn.createStatement();
				/* TODO Add statements for removing
				 * a single person and execute the
				 * database update
				 * */
				String s = "TODO";
				st.executeUpdate(s/*ALTER THE STRING*/);
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
			
		}
		
		/**
		 * Adds an email record corresponding person record corresponding to the
		 * provided <code>personCode</code>
		 * @param personCode
		 * @param email
		 */
		public static void addEmail(String personCode, String email) {
			
		}
		
		/**
		 * Method that removes every customer record from the database
		 */
		public static void removeAllCustomers() {
			
		}

		public static void addCustomer(String customerCode, String type, String primaryContactPersonCode, String name, 
				String street, String city, String state, String zip, String country) {
			
		}

		/**
		 * Removes all product records from the database
		 */
		public static void removeAllProducts() {
			
		}

		/**
		 * Removes a particular product record from the database corresponding to the
		 * provided <code>productCode</code>
		 * @param assetCode
		 */
		public static void removeProduct(String productCode) {
			
		}

		/**
		 * Adds an equipment record to the database with the
		 * provided data.  
		 */
		public static void addEquipment(String productCode, String name, Double pricePerUnit) {
			
		}
		
		/**
		 * Adds an license record to the database with the
		 * provided data.  
		 */
		public static void addLicense(String productCode, String name, double serviceFee, double annualFee) {
			
		}

		/**
		 * Adds an consultation record to the database with the
		 * provided data.  
		 */
		public static void addConsultation(String productCode, String name, String consultantPersonCode, Double hourlyFee) {
			
		}
		
		/**
		 * Removes all invoice records from the database
		 */
		public static void removeAllInvoices() {
			
		}
		
		/**
		 * Removes the invoice record from the database corresponding to the
		 * provided <code>invoiceCode</code>
		 * @param invoiceCode
		 */
		public static void removeInvoice(String invoiceCode) {
			
		}
		
		/**
		 * Adds an invoice record to the database with the given data.  
		 */
		public static void addInvoice(String invoiceCode, String customerCode, String salesPersonCode) {
			
		}
		
		/**
		 * Adds a particular equipment (corresponding to <code>productCode</code> to an 
		 * invoice corresponding to the provided <code>invoiceCode</code> with the given
		 * number of units
		 */
		public static void addEquipmentToInvoice(String invoiceCode, String productCode, int numUnits) {
			
		}
		
		/**
		 * Adds a particular equipment (corresponding to <code>productCode</code> to an 
		 * invoice corresponding to the provided <code>invoiceCode</code> with the given
		 * begin/end dates
		 */
		public static void addLicenseToInvoice(String invoiceCode, String productCode, String startDate, String endDate) {
			
		}

		/**
		 * Adds a particular equipment (corresponding to <code>productCode</code> to an 
		 * invoice corresponding to the provided <code>invoiceCode</code> with the given
		 * number of billable hours.
		 */
		public static void addConsultationToInvoice(String invoiceCode, String productCode, double numHours) {
			
		}
	}
