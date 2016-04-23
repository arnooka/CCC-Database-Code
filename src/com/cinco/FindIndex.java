package com.cinco;

import com.cinco.Comparator.CompareInterface;

public class FindIndex {
	
	// Finds the place where the new node needs to be added
	public static <T> int Index(InvoiceListNode<T> head, InvoiceListNode<T> input, CompareInterface comparator){
		int index = 1;
		InvoiceListNode<T> current = head;
		if(current == null){
			return index;
		}
		
		while(current.getNext() != null){
			if(comparator.compare(current, input) > 0){
				current = current.getNext();
				index++;
			}else if(comparator.compare(current, input) <= 0){
				return index;
			}
		}
		
		// Checks the last node is reached and 
		// compares it to the input
		if(current.getNext() == null){
			if(comparator.compare(current, input) <= 0){
				return index;
			}else{
				index++;
			}
		}
		
		return index;
	}
	
}
