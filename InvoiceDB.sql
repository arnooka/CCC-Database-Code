## Van Stanek & Avinash Nooka
## SQL Homework 4
drop table if exists invoiceProducts;
drop table if exists invoices; 
drop table if exists emails;
drop table if exists customers;  
drop table if exists products;
drop table if exists persons;
drop table if exists addresses;
 
##Delete foreign key first 

create table addresses(
	addressID int not null auto_increment primary key,
	street varchar(250),
    city varchar(250),
    state varchar(250),
    zip varchar(250),
    country varchar(250)
);

create table persons(
	personID int auto_increment primary key, 
	personCode varchar(240),
	personFirstName varchar(240),
	personLastName varchar(240),
	addressID int,
    ##constraint uniquepersonCode unique index(personCode),
	foreign key fk_persons_to_address(addressID) references addresses(addressID) on delete set null
);

create table products(
	productID int not null auto_increment primary key, 
	prodCode varchar(250),
	prodType varchar(250),
	prodName varchar(250),
	ppu double,
	fee double,
	hourly double, 
	yearly double,
	personID int,
    ##constraint uniqueproductCode unique index(prodCode),
	foreign key fk_persons_to_products(personID) references persons(personID) on delete set null
);	

create table emails(
	personID int,
	email varchar(250),
	foreign key fk_emails_to_persons(personID) references persons(personID) on delete set null
);

create table customers(
	customerID int not null auto_increment primary key, 
	customerCode varchar(250),
	customerName varchar(250),
	customerType varchar(250),
	addressID int,
	personID int,
    ##constraint uniquecustomerCode unique index(customerCode),
	foreign key fk_customers_to_address(addressID) references addresses(addressID) on delete set null,
	foreign key fk_customers_to_persons(personID) references persons(personID) on delete set null
);

create table invoices(
	invoiceID int not null auto_increment primary key,
	InvoiceCode varchar(250),
	customerCode varchar(250),
	personCode varchar(250),
	customerID int,
	personID int,
    constraint uniqueinvoiceCode unique index(InvoiceCode),
	foreign key fk_invoices_to_customers(customerID) references customers(customerID) on delete set null,
	foreign key fk_invoices_to_persons(personID) references persons(personID) on delete set null
);

create table invoiceProducts(
	invoiceProductID int not null auto_increment primary key,
	productID int,
	invoiceID int,
	invoiceCode varchar(250),
	productCode varchar(250),
	hours double, 
	units int, 
	startDate varchar(250),
    endDate varchar(250),
    constraint uniqueinvoiceIDproductID unique index(invoiceID, productID),
	foreign key fk_persons_to_products(productID) references products(productID) on delete set null,
	foreign key fk_persons_to_invoices(invoiceID) references invoices(invoiceID) on delete set null
);

##select * from invoiceProducts;
insert into addresses(street, city, state, zip, country) values 
	('222 Dailey news', 'New York', 'NY', 'zipcode', 'United States'),
	('tempel city','Star Destroyer Base', 'FL','', 'Mexico'),	
	('888 Tardis Dr', 'London','', '60611','England'),
	('1234 Yello Dr.', 'Springfield', 'AN','', 'Canada'),
	('Steven; 555 Robo St.', 'Seattle','', '7987987','Mexico'),
	('2438 N 32nd St.', 'Buffalo', 'NY', '78643', 'United States'),
	('42 Wallaby Way', 'Sydney', 'New South Wales', 'In a land down unda', 'Australia'),
	('600 W E St', 'Lincoln', 'NE', '68522','USA'),
	('6852 Looney Toons Lane','Los Angeles','CA','92345','USA'),
	('99999 Rocky Way','The Rock Central','The Rock','88888','Mars');
##select * from  addresses;

##Person Inserts
insert into persons(personCode, personFirstName, personLastName, addressID) values 
	('000'	,'person','no'		, 1),
	('123'	,'Super' , 'Man'	, 2),
	('122'	,'Darth' ,'Vador'	, 3),
	('111'	,'David' ,'Tennant' , 4),
	('222'	,'H'	 ,'Simpson' , 5),
	('333'	,'Steven','Haskings', 6),
	('S001'	,'Lauren','Lauften' , 7),
	('S002'	,'P'	 ,'Sherman' , 8);
    
##Product Inserts
insert into products (prodCode, prodType, prodName, personID, ppu, fee, hourly, yearly) values 
	('f2187', 'C', 'Storm Trooper', 3, 	  0, 150, 30, 0),
	('bb8'	, 'E', 'Ball droid'	  , 1, 2500,   0,  0, 0),
	('c3p0'	, 'L', 'Awkward AI'	  , 1,    0, 350,  0, 1200);

##Email Inserts
insert into emails(personID, email) values 
	(2, 'TotesRealSprMn@savedaworld.com'), 
	(2, 'Clarkkent@whatever.com'),
	(3, 'notAnikan@force.com'),
	(4, 'TheDoc@real.com'),
	(5, 'blablabla@bla.bla'),
	(6, 'SteevHawks@Physics4eva.edu'),
	(6, 'roboRock@nerd.com'),
	(7, 'llauften@Yahoo.com'),
	(8,'Australia;pbshiredme@pls.com');
    
##Customer Inserts
insert into customers (customerCode, customerName, customerType, addressID, personID) values 
	('C001', 'Lincoln Industries', 'C',  '8', '2'),
	('C002', 'ACME Corperation'  , 'G',  '9', '8'),
	('C003', 'Smurfs Inc.'		 , 'G', '10', '7');
   
## Invoice Inserts
insert into invoices (InvoiceCode, customerCode, personCode, customerID, personID) values 
	('INV001', 'C001', 'S002',(select customerID from customers where customerCode = 'C001'),(select personID from persons where personCode = 'S002')),
	('INV002', 'C002', 'S002',(select customerID from customers where customerCode = 'C002'),(select personID from persons where personCode = '122')),
	('INV003', 'C003', 'S001',(select customerID from customers where customerCode = 'C003'),(select personID from persons where personCode = 'S001'));
	
##Invoice Product Inserts
insert into invoiceProducts (productID, invoiceID, invoiceCode, productCode, hours, units, startDate, endDate) values 
	((select productID From products where prodCode = 'f2187'),(select invoiceID from invoices where invoiceCode = 'INV001'),'INV001','f2187', 17, 0, null, null),
	((select productID From products where prodCode = 'bb8'),(select invoiceID from invoices where invoiceCode = 'INV002'),'INV002','bb8', 0, 2, null, null),
	((select productID From products where prodCode = 'c3p0'),(select invoiceID from invoices where invoiceCode = 'INV003'), 'INV003', 'c3p0', 0, 0, '1999-12-31', '2003-12-31');
    