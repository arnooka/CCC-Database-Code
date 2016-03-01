import java.util.ArrayList;

public class Invoice {
	
	private String invoiceID;
	private String customerName;
	private String salesPerson;
	private ArrayList<String> invoiceProducts;
	
	public Invoice(String invoiceID, String customerName, String salesPerson, ArrayList<String> invoiceProducts) {
		this.invoiceID = invoiceID;
		this.customerName = customerName;
		this.salesPerson = salesPerson;
		this.invoiceProducts = invoiceProducts;
	}

	public String getInvoiceID() {
		return invoiceID;
	}

	public String getCustomerName() {
		return customerName;
	}

	public String getSalesPerson() {
		return salesPerson;
	}

	public ArrayList<String> getInvoiceProducts() {
		return invoiceProducts;
	}
	
}
