import java.io.FileNotFoundException;

/* Van Stanek, Avinash Nooka
 * CCC Database in java
 * 2/08/2016
 */


public class CCCDatabase {

	public static void main(String args[]) throws FileNotFoundException{
		
		JsonXmlParser.toXMLandJSON();
		
		InvoiceParser.Invoices();
	}
}
