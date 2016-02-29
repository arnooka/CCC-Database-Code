import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;

public class JsonXmlFactory {
	
	//Maps personCode to person
	private static HashMap<String, Persons> personCode1 = new HashMap<String, Persons>();
	//customer code to customer
	private static HashMap<String, Customer> customerCode1 = new HashMap<String, Customer>();
	//productCode to product
	private static HashMap<String, Product> productCode1 = new HashMap<String, Product>();
	
	@SuppressWarnings("resource")
	public static void toXMLandJSON() throws FileNotFoundException {
			//Open files and create Json and XML builders
			Scanner person = new Scanner(new File("data/Persons.dat"));
			Scanner customer = new Scanner(new File("data/Customers.dat"));
			Scanner product = new Scanner(new File("data/Products.dat"));
			
			XStream xstream = new XStream();
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			
			////////////////////////////////////////////////////////////////////////////
			//Create elements from persons.dat and send to the person class constructor
			try {
				PrintWriter js = new PrintWriter("data/Persons.json");
				PrintWriter xm = new PrintWriter("data/Persons.xml");
				//Gets the first line which holds the line count of the file
				int l = Integer.parseInt(person.nextLine());
				xm.printf("<persons>\n");
				js.printf("{\n\"persons\": [\n");
				xstream.alias("person", Persons.class);
				int count = 1;
				//Tokenize the line and send to the persons constructor
				for(int i = 0; i<l; i++){
					String line = person.nextLine().trim();
					String tokens[] = line.split(";");
					
					String personCode = tokens[0].trim();
					String personName = tokens[1].trim();
					String Address = tokens[2].trim();
					
					String fullAddress[] = Address.split(",");
					String street = fullAddress[0].trim();
					String city = fullAddress[1].trim();
					String state = fullAddress[2].trim();
					String zip = fullAddress[3].trim();
					String country = fullAddress[4].trim();
					
					String emailTokens[] = null;
					if(tokens.length == 4){
						String email = tokens[3].trim();
						emailTokens = email.split(",");
					}else{
						emailTokens = new String[0];
					}
				
					String fullName[] = personName.split(",");
					String lastName = fullName[0].trim();
					String firstName = fullName[1].trim();
					//Generate a new instance for a person with parsed information
					Persons person1 = new Persons(personCode, firstName, lastName,
					new Address(street, city, state, zip, country), emailTokens);
					
					//personCode maps to an instance of a person
					personCode1.put(personCode, person1);
					
					String xml = xstream.toXML(person1);
					String json = gson.toJson(person1);
					
					if(count < l){
						js.printf("	"+json+",\n");
						count++;
					}else if(count == l){
						js.printf("	"+json+"\n");
					}
					xm.printf(xml+"\n");
				}
				xm.printf("</persons>\n");
				js.printf("]}\n");
				js.close();
				xm.close();
			}catch (FileNotFoundException e) {
			throw new RuntimeException(e);
			}
			
			////////////////////////////////////////////////////////////////////////////////////
			//Create elements from Customers.dat and send to the customer subclass constructors.
			try {
				PrintWriter js = new PrintWriter("data/Customers.json");
				PrintWriter xm = new PrintWriter("data/Customers.xml");
				//Gets the first line which holds the line count of the file
				int m = Integer.parseInt(customer.nextLine());
				xm.printf("<customers>\n");
				js.printf("{\n\"customers\": [\n");
				int count = 1;
				//Tokenize the line and send to the customer constructor
				for(int i = 0; i<m; i++){
					
					///////////////////////////////////////////////////////////////////////////////////////////////////////
					String line = customer.nextLine().trim();
					String tokens[] = line.split(";");
					String customerCode = tokens[0].trim();
					String customerType = tokens[1].trim();
					String personCode = tokens[2].trim();
					String customerName = tokens[3].trim();
					
					String Address = tokens[4].trim();
					String fullAddress[] = Address.split(",");
					String street = fullAddress[0].trim();
					String city = fullAddress[1].trim();
					String state = fullAddress[2].trim();
					String zip = fullAddress[3].trim();
					String country = fullAddress[4].trim();
					
					//Get the related person from the personCode in Customers.dat
					Persons primaryContact = (Persons) personCode1.get(personCode);
					
					//Statement that determins the type of customer
					if(customerType.equals("C")){
						xstream.alias("companyCustomer", companyCustomer.class);
						//Generate an instance of a company customer
						companyCustomer customer1 = new companyCustomer(customerCode, primaryContact, customerName,
									new Address(street, city, state, zip, country), customerType);
						//Maps a customerCode to a customer
						customerCode1.put(customerCode, customer1);
						String xml = xstream.toXML(customer1);
						String json = gson.toJson(customer1);
						if(count < m){
						js.printf("	"+json+",\n");
							count++;
						}else if(count == m){
							js.printf("	"+json+"\n");
						}
						xm.printf(xml+"\n");
					}else if(customerType.equals("G")){
						xstream.alias("governmentCustomer", governmentCustomer.class);
						//Generate an instance of a government customer
						governmentCustomer government1 = new governmentCustomer(customerCode, primaryContact, customerName,
											new Address(street, city, state, zip, country), customerType);
						//Maps a customerCode to a customers full name
						customerCode1.put(customerCode, government1);
						String xml = xstream.toXML(government1);
						String json = gson.toJson(government1);
						if(count < m){
							js.printf("	"+json+",\n");
							count++;
						}else if(count == m){
							js.printf("	"+json+"\n");
						}
						xm.printf(xml+"\n");
					}
				}
				xm.printf("</customers>\n");
				js.printf("]}\n");
				js.close();
				xm.close();
			}catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			}
			
			///////////////////////////////////////////////////////////////////////////////////
			//Create elements from Products.dat and send to the products subclass constructors.
			try {
				PrintWriter js = new PrintWriter("data/Products.json");
				PrintWriter xm = new PrintWriter("data/Products.xml");
				//Gets the first line which holds the line count of the file
				int n = Integer.parseInt(product.nextLine());
				xm.printf("<products>\n");
				js.printf("{\n\"products\": [\n");
				int count = 1;
				//Tokenize the line and send to the product constructor
				for(int i = 0; i<n; i++){
				xstream.alias("Product", Product.class);
				String line = product.nextLine().trim();
				String tokens[] = line.split(";");
				
				String productCode = tokens[0].trim();
				String type = tokens[1].trim();
				String productName = tokens[2].trim();
				
				//Statement that determins the type of product
				if(type.equals("C")){ //Consultantion
					String consultCode = tokens[3].trim();
					Double hourlyRate = Double.parseDouble(tokens[4]);
					
					//String invItems = type+";"+productName+";"+hourlyRate;
					
					//Get an instance of a person throught the consultCode
					Persons a = (Persons) personCode1.get(consultCode);
					//Generate an instance of a consultation
					Consultations consultation1 = new Consultations(consultCode, productName, a, hourlyRate, type);
					
					//Map the required items for invoice to the Consultation product code
					productCode1.put(productCode, consultation1);
					xstream.alias("consultation", Consultations.class);
					String xml = xstream.toXML(consultation1);
					String json = gson.toJson(consultation1);
					if(count < n){
						js.printf("	"+json+",\n");
						count++;
					}else if(count == n){
						js.printf("	"+json+"\n");
					}
					xm.printf(xml+"\n");
					
				}else if(type.equals("E")){ //Equipment
					Double pricePerUnit = Double.parseDouble(tokens[3]);
					//Generate an instance of an equipment
					
					//String invItems = type+";"+productName+";"+pricePerUnit;
					
					Equipment equipment1 = new Equipment(productCode, productName, pricePerUnit, type);
					
					//Map the required items for invoice to the Equipment product code
					productCode1.put(productCode, equipment1);
					xstream.alias("equipment", Equipment.class);
					String xml = xstream.toXML(equipment1);
					String json = gson.toJson(equipment1);
					if(count < n){
						js.printf("	"+json+",\n");
						count++;
					}else if(count == n){
						js.printf("	"+json+"\n");
					}
					xm.printf(xml+"\n");
				
				}else if(type.equals("L")){ //License
					Double fee = Double.parseDouble(tokens[3]);
					Double annualFee = Double.parseDouble(tokens[4]);
					
					//String invItems = type+";"+productName+";"+fee+";"+annualFee;
					
					xstream.alias("license", License.class);
					//Generate an instance of a license
					License license1 = new License( productName, productCode, fee, annualFee, type);
					
					//Map the required items for invoice to the Equipment product code
					productCode1.put(productCode, license1);
					String xml = xstream.toXML(license1);
					String json = gson.toJson(license1);
					if(count < n){
						js.printf("	"+json+",\n");
						count++;
					}else if(count == n){
						js.printf("	"+json+"\n");
					}
					xm.printf(xml+"\n");
				}
			}
			xm.printf("</products>");
			js.printf("]}\n");
			js.close();
			xm.close();
		}catch (FileNotFoundException e){
		throw new RuntimeException(e);
		}
			
	}
	
	public static HashMap<String, Persons> getPersonCode1() {
		return personCode1;
	}

	public static HashMap<String, Customer> getCustomerCode1() {
		return customerCode1;
	}
	

	public static HashMap<String, Product> getProductCode1() {
		return productCode1;
	}
	
}
