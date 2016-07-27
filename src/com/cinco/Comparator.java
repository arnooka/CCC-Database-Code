package com.cinco;

public class Comparator {
	
	// Makes all compare methods able to be called through the dot operator
	public interface CompareInterface {
		
		public int compare(InvoiceListNode<?> current, InvoiceListNode<?> input);
		
	}
	
	// Compares the current node's customer name to the input node's customer name
	public static class CompareByName implements CompareInterface {

		@Override
		public int compare(InvoiceListNode<?> current, InvoiceListNode<?> input) {
			int name = current.getInvoice().getCustomer().getName().compareTo(input.getInvoice().getCustomer().getName());
			if(name < 0){
				return 1;
			}else if(name > 0){
				return -1;
			}else{
				return 1;
			}
		}
		
	}
	
	// Compares the current node's total to the input node's total
	public static class CompareByTotal implements CompareInterface {

		@Override
		public int compare(InvoiceListNode<?> current, InvoiceListNode<?> input) {
			if(current.getInvoice().getEndTotal() < input.getInvoice().getEndTotal()){
				return -1;
			}else if(current.getInvoice().getEndTotal() > input.getInvoice().getEndTotal()){
				return 1;
			}else{
				return 1;
			}
		}
		
	}
	
	// Compares the current node's type to the input node's type
	// Compares by sales person otherwise if type is the same
	public static class CompareTypeToName implements CompareInterface {

		@Override
		public int compare(InvoiceListNode<?> current, InvoiceListNode<?> input) {
			int type = current.getInvoice().getCustomer().getType().compareTo(input.getInvoice().getCustomer().getType());
			if(type < 0){ // Type checks
				return 1;
			}else if(type > 0){
				return -1;
			}else if(type == 0){
				
				// Checks if sales person is null: Adds the 
				// node to the end of the 'C' or 'G' types if so
				if(input.getInvoice().getSalesPerson() == null){
					return 1;
				}
				
				int lastName = current.getInvoice().getSalesPerson().getLastName().compareTo(input.getInvoice().getSalesPerson().getLastName());
				int firstName = current.getInvoice().getSalesPerson().getFirstName().compareTo(input.getInvoice().getSalesPerson().getFirstName()); 
				if(lastName < 0){ // Last Name checks
					return 1;
				}else if(lastName > 0){
					return -1;
				}else if(lastName == 0){
					
					if(firstName < 0){ // First Name checks
						return 1;
					}else if(firstName > 0){
						return -1;
					}else{
						return 1;
					}
				}
			}
			return 0;
		}
	}

	
}
