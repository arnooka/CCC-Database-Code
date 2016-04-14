package com.cinco;
import java.io.FileNotFoundException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

/* Van Stanek, Avinash Nooka
 * CCC Database in java
 * 2/08/2016
 */


public class CCCDatabase {
	
	private static org.apache.log4j.Logger log = Logger.getLogger(CCCDatabase.class);
	
	public static void main(String args[]) throws FileNotFoundException{
		
		//Old methods which output invoice data from .dat files
		//JsonXmlParser.toXMLandJSON();
		//InvoiceParser.Invoices();
		
		BasicConfigurator.configure();
		try{
			//Query and output invoice information
			DBObjectsParser.DBObjects();
			DBInvoiceParser.Invoices();
		}catch(Exception e){	
 			log.error("Something Goofed HARD...", e);
			throw new RuntimeException(e);
	 	}
	}
}
