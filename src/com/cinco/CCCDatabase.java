/* Van Stanek, Avinash Nooka
 * CCC Database in java
 * 2/08/2016
 */

package com.cinco;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class CCCDatabase {
	
	private static org.apache.log4j.Logger log = Logger.getLogger(CCCDatabase.class);
	
	// Switches for printing various information
	private static boolean parserSwitch = false;
	private static boolean querySwitch = false;
	private static boolean listSwitch = true;
	
	public static <T> void main(String args[]) throws FileNotFoundException{
		
		// Switch check
		if(!parserSwitch && !querySwitch && !listSwitch){
			System.out.println("Check switches to print information!");
			System.exit(1);
		}
		
		// Old methods which output invoice data from .dat files
		if(parserSwitch){
			System.out.println("\nParsing through \".dat\" files\n");
			JsonXmlParser.toXMLandJSON();
			InvoiceParser.Invoices();
		}
		
		BasicConfigurator.configure();
		try{
			if(querySwitch){
				System.out.println("\nQuerying database\n");
				//Querys and Outputs Invoice Information
				DBObjectQueries.DBObjects();
				DBInvoiceQueries.Invoices();
			}
			
			// Anything after this is for phase 6
			if(listSwitch){
				DBObjectQueries.DBObjects();
				DBInvoiceQueries.Invoices();
				
				System.out.println("\nLinked List Sorting");
				// Gets All Queried Invoice Objects in a List
				ArrayList<Invoice> invoices = DBInvoiceQueries.getInvoiceArrayList();
				// Create new Invoice Linked List
				InvoiceList<T> il = new InvoiceList<T>();
				
				// Sorted by Customer Sales Person Name
				System.out.println("\nSORTED BY CUSTOMER NAME");
				for(int i = 0; i<invoices.size(); i++){
					InvoiceListNode<T> input = new InvoiceListNode<T>(invoices.get(i));
					
					int index = InvoiceList.Index(il.getHead(), input, new Comparator.CompareByName());
					il.add(input, index);
				}
				il.toString();
				il.clear();
				
				// Sorted By Invoice Total Amount
				System.out.println("\nSORTED BY INVOICE TOTAL");
				for(int i = 0; i<invoices.size(); i++){
					InvoiceListNode<T> input = new InvoiceListNode<T>(invoices.get(i));
					
					int index = InvoiceList.Index(il.getHead(), input, new Comparator.CompareByTotal());
					il.add(input, index);
				}
				il.toString();
				il.clear();
				
				// Sort By Customer Type then SalesPerson
				System.out.println("\nSORTED BY CUSTOMER TYPE - SALESPERSON");
				for(int i = 0; i<invoices.size(); i++){
					InvoiceListNode<T> input = new InvoiceListNode<T>(invoices.get(i));
					
					int index = InvoiceList.Index(il.getHead(), input, new Comparator.CompareTypeToName());
					il.add(input, index);
				}
				il.toString();
				il.clear();
			}
			
		}catch(Exception e){	
 			log.error("Something Goofed HARD...", e);
			throw new RuntimeException(e);
	 	}
	}

	public static boolean getQuerySwitch() {
		return querySwitch;
	}
	
}
