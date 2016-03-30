## Van Stanek & Avinash Nooka
## SQL Homework 4

##1.
select * from persons;

##2.
select email from emails  where personID = (select personID from persons where personCode = '111'); 

##3. A query to add an email to a specific person
insert into emails (personID, email)
	values ((select personID from persons where personCode = '111'),('ThecoolestDoc@tardis.g'));

##4. A query to change the email address of a given email record
set sql_safe_updates = 0;
update emails set email = 'Somethingelse@gmail.com' where email = 'ThecoolestDoc@tardis.g';

##5. A query (or series of queries) to remove a given person record
delete from emails where personID = (select personID from persons where personCode = 123);
update customers set personID = null where personID = (select personID from persons where personCode = 123);
delete from persons where personCode = 123;
set sql_safe_updates = 1;

##6. A query to create a person record
insert into persons (personCode, personFirstName, personLastName, addressID) 
	values ("blah5","Steve","Jobbers","7");    

##7. A query to get all the products in a particular invoice
select i.invoiceCode, p.productCode from invoices as i
	join invoiceProducts as p on i.invoiceID = p.invoiceID where i.invoiceCode = 'INV001';

##8. A query to get all the invoices of a particular customer
select invoiceCode, c.customerName from invoices as i 
	join customers as c on i.customerID = c.customerID where c.customerCode = 'C001';
select * from products;

##9. A query to create a new product record
insert into products(prodCode, prodType, prodName, ppu, fee, hourly, yearly, personID) 
	values ('Han','C','shipment', 0, 150, 15,0, null); 

##10. A query to create a new invoice record
insert into invoices(InvoiceCode, customerCode, personCode, customerID, personID) 
	values('INV004','C003','111', 3, 4);
    
##11. A query to associate a particular product with a particular invoice
insert into invoiceProducts(productID, invoiceID, invoiceCode, productCode, hours, units, daysBetween) values 
	((select productID from products where prodCode = 'c3p0'), (select invoiceID from invoices where invoiceCode = 'INV001'), 'INV001', 'c3p0', 0,0,100);
    
insert into invoiceProducts(productID, invoiceID, invoiceCode, productCode, hours, units, daysBetween) values 
	((select productID from products where prodCode = 'bb8'), (select invoiceID from invoices where invoiceCode = 'INV002'), 'INV002', 'bb8', 0, 10, 0);
select * from products;

##12. A query to find the total number of invoices for each (and every) customer record
select c.customerName, count(*) from invoices as i join customers as c on i.customerCode = c.customerCode 
	group by c.customerCode having count(*) >= 1;

##13. A query to find the total number of invoices for each salesperson
select p.personFirstName, p.personLastName, count(*) from invoices as i
	join persons as p on i.personID = p.personID group by i.personID;

##14. A query to find the total number of invoices that include a particular product
select p.productCode, count(*) as 'invoices containing product' from invoices as i
	join invoiceProducts as p on i.invoiceID = p.invoiceID where p.productCode = 'c3p0'; 

##15. A query to find the total cost (excluding fees and taxes) of all equipment in each invoice
select i.invoiceCode, sum(ip.units * p.ppu) as 'invoice equipment price' from invoices as i 
	join invoiceProducts as ip on i.invoiceID = ip.invoiceID 
	join products as p on ip.productID = p.productID;

##16. A query to detect invalid data in invoices as follows. In a single invoice, a particular
	##equipment product should only appear once (since any number of units can be consolidated
	##to a single record). Write a query to find any invoice that includes multiple
	##instances of the same equipment product.
select i.invoiceCode as 'invoice with conflicts', ip.productCode, count(*) as 'Times product appears' from invoices as i 
	join invoiceProducts as ip on i.invoiceID = ip.invoiceID 
	join products as p on ip.productID = p.productID
	where p.prodType = 'E' group by ip.invoiceID, p.prodCode having (count(*)>1);  
  
## This query will find any duplicates     
##select i.invoiceCode as 'invoice with conflicts', ip.productCode, count(*) as 'Times product appears' from invoices as i
	##join invoiceProducts as ip on i.invoiceID = ip.invoiceID 
	##group by i.invoiceCode, ip.productCode having count(*)>1;
    
##17. Write a query to detect a possible conflict of interest as follows. No distinction is
	##made in this system between a person who is the primary contact of a client and a
	##person who is also a sales person. Write a query to find any and all invoices where the
	##salesperson is the same as the primary contact of the invoice’s customer.
select invoiceCode as 'conflicting invoices' from invoices as i 
	join customers as c on c.customerCode = i.customerCode 
	join persons as p on i.personID = p.personID
	where i.personID = c.personID;
    
