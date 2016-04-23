package com.cinco;

public class InvoiceListNode<T> {
		
	private InvoiceListNode<T> next;
	private Invoice invoice;
	
	// Node constructors
	public InvoiceListNode(Invoice invoice){
		this.next = null;
		this.invoice = invoice;
	}
	
	// Returns an invoice in a node
	public Invoice getInvoice(){
		return invoice;
	}
	
	// Returns the next node in the list
	public InvoiceListNode<T> getNext(){
		return next;
	}
	
	// Sets the next node in the list
	public void setNext(InvoiceListNode<T> next){
		this.next = next;
	}
}

