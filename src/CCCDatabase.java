import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

/* Van Stanek, Avinash Nooka
 * CCC Database in java
 * 2/08/2016
 */


public class CCCDatabase {
	
	private static org.apache.log4j.Logger log = Logger.getLogger(CCCDatabase.class);
	
	public static void main(String args[]){
		
		BasicConfigurator.configure();
		
		try{
			DBObjectsParser.DBObjects();
		
			DBInvoiceParser.Invoices();
			
			//Removes All Persons from the Database
			//InvoiceData.removeAllPersons();
			
			//Removes a Single Person from the Database
			String personCode = "000";
			//InvoiceData.removePerson(personCode);
		}catch(Exception e){	
 			log.error("Something Goofed HARD...", e);
			throw new RuntimeException(e);
	 	}
	}
}
