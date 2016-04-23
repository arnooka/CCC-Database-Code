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
	
	public static <T> void main(String args[]) throws FileNotFoundException{
		
		// Old methods which output invoice data from .dat files
		//JsonXmlParser.toXMLandJSON();
		//InvoiceParser.Invoices();
		
		BasicConfigurator.configure();
		try{
			//Querys and Outputs Invoice Information
			DBObjectQueries.DBObjects();
			DBInvoiceQueries.Invoices();
			
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Anything between is for assignment 7
			// Gets All Queried Invoice Objects in a List
			ArrayList<Invoice> invoices = DBInvoiceQueries.getInvoiceArrayList();
			// Create new Invoice Linked List
			InvoiceList<T> il = new InvoiceList<T>();
			
			// Sorted by Customer Sales Person Name
			System.out.println("\n\n\n\nSORTED BY CUSTOMER NAME");
			for(int i = 0; i<invoices.size(); i++){
				InvoiceListNode<T> input = new InvoiceListNode<T>(invoices.get(i));
				
				int index = FindIndex.Index(il.getHead(), input, new Comparator.CompareByName());
				il.add(input, index);
			}
			il.toString();
			il.clear();
			
			// Sorted By Invoice Total Amount
			System.out.println("\nSORTED BY INVOICE TOTAL");
			for(int i = 0; i<invoices.size(); i++){
				InvoiceListNode<T> input = new InvoiceListNode<T>(invoices.get(i));
				
				int index = FindIndex.Index(il.getHead(), input, new Comparator.CompareByTotal());
				il.add(input, index);
			}
			il.toString();
			il.clear();
			
			// Sort By Customer Type then SalesPerson
			System.out.println("\nSORTED BY CUSTOMER TYPE - SALESPERSON");
			for(int i = 0; i<invoices.size(); i++){
				InvoiceListNode<T> input = new InvoiceListNode<T>(invoices.get(i));
				
				int index = FindIndex.Index(il.getHead(), input, new Comparator.CompareTypeToName());
				il.add(input, index);
			}
			il.toString();
			il.clear();
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		}catch(Exception e){	
 			log.error("Something Goofed HARD...", e);
			throw new RuntimeException(e);
	 	}
	}
}
