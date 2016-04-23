package com.cinco;

public class InvoiceList<T> {

	private InvoiceListNode<T> head = null;
	private static int size = 0;
	
	// Clear everything in the linked list
    public void clear() {
		InvoiceListNode<T> next;
    	while (head != null){
    		next = head.getNext();
    		head.setNext(null);
    		head = null;
    		head = next;
    	}
    	size = 0;
    }
    
    // Add to the start of the linked list
    public void addToStart(InvoiceListNode<T> input) {
    	if(head == null){
    		head = input;
    	}else{
    		input.setNext(head);
        	head = input;
    	}
    	size++;
    }
    
    // Add to the end of the linked list
    public void addToEnd(InvoiceListNode<T> input) {
    	InvoiceListNode<T> temp = head;
    	if (head == null){
    		head = input;
    	}else{
    		while (temp.getNext() != null){
    			temp = temp.getNext();
    		}
    		temp.setNext(input);
    	}
    }
    
    // Add a node to a certain 
    public void add(InvoiceListNode<T> input, int index){
    	if(head == null || index == 1 || index < 0){
    		// Checks if node needs to be the new head
    		addToStart(input);
    	}else if(index == size()+1){
    		// Checks if node needs to be the new tail
    		addToEnd(input);
    	}else{
    		// Adds the node to a specific index otherwise
    		InvoiceListNode<T> prev = head;
    		InvoiceListNode<T> temp = null;
    		int i = 1;
    		while(prev.getNext() != null && i < index-1){
    			prev = prev.getNext();
    			i++;
    		}
    		if(prev.getNext() != null){
    			// Link the new node between the previous
    			// node and the one after the prevoius node
    			temp = prev.getNext();
    			prev.setNext(input);
    			input.setNext(temp);
    		}else{
    			// Add to end otherwise
    			addToEnd(input);
    		}
    	}
    	size++;
    }
    
    // Remove a node at a certain position
    public void remove(int position) {
    	InvoiceListNode<T> current = head;
    	if (position == 0){
    		head = current.getNext();  
    		return;
    	}

    	for (int i=0; current !=null && i<position-1; i++){
    		current = current.getNext();  
    		if (current == null || current.getNext() == null){
    			return;
    		}
    	}
    	InvoiceListNode<T> next = current.getNext().getNext();
    	current.setNext(next);  
    	size--;
    }
    
    // Get the size of the linked list
  	public static int size() { 
      	return size;
  	}
  	
  	public boolean isEmpty(){
  		return size == 0;
  	}
    
    // Print the list as a Sting
	public String toString(){
		InvoiceListNode<T> current = head;
		String output = "";
		if(current == null){
			output += "null";
			return output;
		}
		
		String header = String.format("Executive Summary Report\n=========================\n"
							+ "%-9s %-49s %-32s %-15s %-10s %-11s %s", 
							"Invoice", "Customer", "Salesperson", "Subtotal", "Fees", "Taxes", "Total");
		System.out.println(header);
		
		double grandSubTotal = 0;
		double grandFees = 0;
		double grandTaxes = 0;
		double grandTotal = 0;
		
		while(current != null){
			String invoiceCode 	= current.getInvoice().getInvoiceCode();
			String customerName = current.getInvoice().getCustomer().getName();
			String lastName 	= null;
			String firstName 	= null;
			double subTotal 	= current.getInvoice().getSubTotal();
			double totalFees 	= current.getInvoice().getTotalFees();
			double totalTax 	= current.getInvoice().getTotalTax();
			double endTotal 	= current.getInvoice().getEndTotal();
			String s = null;
			if(current.getInvoice().getSalesPerson() == null){
				grandSubTotal += subTotal;
				grandFees 	  += totalFees;
				grandTaxes 	  += totalTax;
				grandTotal 	  += endTotal;
				
				s = String.format("%-9s %-49s %-29s $%10.2f $%10.2f $%10.2f $%10.2f",
				invoiceCode, customerName, "null", subTotal, totalFees, totalTax, endTotal);
			}else{
				lastName 	= current.getInvoice().getSalesPerson().getLastName();
				firstName 	= current.getInvoice().getSalesPerson().getFirstName();
				
				grandSubTotal += subTotal;
				grandFees 	  += totalFees;
				grandTaxes 	  += totalTax;
				grandTotal 	  += endTotal;
				
				s = String.format("%-9s %-49s %-29s $%10.2f $%10.2f $%10.2f $%10.2f",
				invoiceCode, customerName, lastName+", "+firstName, subTotal, totalFees, totalTax, endTotal);
			}
			
			System.out.println(s);
			output += s;
			current = current.getNext();
		}
		String ending = String.format("===================================================================="
				 + "=====================================================================\n"
				 + "%-89s $%10.2f $%10.2f $%10.2f $%10.2f", 
				 "TOTALS", grandSubTotal, grandFees, grandTaxes, grandTotal);
		System.out.println(ending);
		
		return output;
	}
	
	public InvoiceListNode<T> getHead(){
		return head;
	}
	
}
