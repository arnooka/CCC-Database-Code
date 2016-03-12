## Van Stanek & Avinash Nooka
## SQL Homework 4
drop table if exists invoiceProducts;
drop table if exists invoices; 
drop table if exists emails;
drop table if exists customers;  
drop table if exists products;
drop table if exists persons;
drop table if exists states;
drop table if exists addresses;
 
##delete foreign key first 

create table addresses(
	addressID int not null auto_increment primary key,
    address varchar(250)
);

create table states(
	stateName varchar(250),
    addressID int not null,
    foreign key fk_states_to_addresses(addressID) references addresses(addressID)
);

create table persons(
	personID int auto_increment primary key, 
	personCode varchar(240),
	personFirstName varchar(240),
	personLastName varchar(240),
    addressID int not null,
    foreign key fk_persons_to_address(addressID) references addresses(addressID)
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
    foreign key fk_persons_to_products(personID) references persons(personID)
);	

create table emails(
	personID int not null,
	email varchar(250),
    foreign key fk_emails_to_persons(personID) references persons(personID)
);

create table customers(
	customerID int not null auto_increment primary key, 
	customerCode varchar(250),
	customerName varchar(250),
	customerType varchar(250),
    addressID int not null,
    personID int,
    foreign key fk_customers_to_address(addressID) references addresses(addressID),
    foreign key fk_customers_to_persons(personID) references persons(personID)
);

create table invoices(
	invoiceID int not null auto_increment primary key,
	InvoiceCode varchar(250),
    customerCode varchar(250),
    personCode varchar(250),
    customerID int not null,
    personID int,
    foreign key fk_invoices_to_customers(customerID) references customers(customerID),
    foreign key fk_invoices_to_persons(personID) references persons(personID)
);

create table invoiceProducts(
     invoiceProductID int not null auto_increment primary key,
	 productID int not null,
     invoiceID int not null,
     invoiceCode varchar(250),
     productCode varchar(250),
	 hours double, 
     units double, 
	 daysBetween double,
     foreign key fk_persons_to_products(productID) references products(productID),
	 foreign key fk_persons_to_invoices(invoiceID) references invoices(invoiceID)
);



insert into addresses(address) values 
	('222 Dailey news, New York, NY, zipcode, United States'),
	('tempel city,Star Destroyer Base, FL,, Mexico'),	
	('888 Tardis Dr, London, , 60611,England'),
	('1234 Yello Dr., Springfield, AN,, Canada'),
	('Steven; 555 Robo St., Seattle,, 7987987,Mexico'),
	('2438 N 32nd St., Buffalo, NY, 78643, United States'),
	('42 Wallaby Way, Sydney, New South Wales, In a land down unda, Australia'),
    ('600 W E St, Lincoln, NE, 68522,USA'),
    ('6852 Looney Toons Lane,Los Angeles,CA,92345,USA'),
    ('99999 Rocky Way,The Rock Central,The Rock,88888,Mars');
select * from  addresses;

insert into states(stateName, addressID) values 
	('NY', 1),('FL', 2),('', 3),('AN', 4),('', 5) ,('NY', 6),('New south wales', 7),('NE', 8),('CA', 9),('The Rock', 10);

insert into persons(personCode, personFirstName, personLastName, addressID) values 
	('000','person','no', 1),
	('123','Super', 'Man', 2),
	('122','Darth','Vador', 3),
	('111','David','Tennant', 4),
	('222','H','Simpson', 5),
	('333','Steven','Haskings', 6),
	('s001','Lauren','Lauften', 7),
	('s002','P','sherman', 8);
    
    ##Product Inserts
insert into products (prodCode, prodType, prodName, personID, ppu, fee, hourly, yearly) values 
	('f2187', 'C', 'Storm Trooper', 3, 0, 150, 30, 0),
	('bb8', 'E', 'Ball droid', 1, 2500, 0, 0, 0),
	('C3p0', 'L', 'Awkward AI', 1, 0, 0, 350, 1200);
    
insert into emails(personID, email) values 
	(1, 'TotesRealSprMn@savedaworld.com'), 
    (1, 'Clarkkent@whatever.com'),
	(2, 'notAnikan@force.com'),
    (3, 'TheDoc@real.com'),
    (4, 'blablabla@bla.bla'),
    (5, 'SteevHawks@Physics4eva.edu'),
	(5, 'roboRock@nerd.com'),
    (6, 'llauften@Yahoo.com'),
    (7,'Australia;pbshiredme@pls.com');
    
##Customer Insert
insert into customers (customerCode, customerName, customerType, addressID, personID) values 
	('C001', 'Lincoln Industries', 'C', '8', '2'),
	('C002', 'ACME Corperation', 'G', '9', '3'),
	('C003', 'Smurfs Inc.', 'G', '10', '6');
   
   ## Invoice Inserts
insert into invoices (InvoiceCode, customerCode, personCode, customerID, personID) values 
	('INV001', 'C001', 'S002',(select customerID from customers where customerCode = 'C001'),(select personID from persons where personCode = 'S002')),
	('INV002', 'C002', '122',(select customerID from customers where customerCode = 'C002'),(select personID from persons where personCode = '122')),
	('INV003', 'C003', 'S001',(select customerID from customers where customerCode = 'C003'),(select personID from persons where personCode = 'S001'));
	
##Invoice Product Inserts
insert into invoiceProducts (productID, invoiceID, invoiceCode, productCode, hours, units, daysBetween) values 
	((select productID From products where prodCode = 'f2187'),(select invoiceID from invoices where invoiceCode = 'INV001'),'INV001','f2187', 17, 0, 0),
	((select productID From products where prodCode = 'bb8'),(select invoiceID from invoices where invoiceCode = 'INV002'),'INV002','bb8', 0, 2, 0),
	((select productID From products where prodCode = 'c3p0'),(select invoiceID from invoices where invoiceCode = 'INV003'), 'INV003', 'c3p0', 0, 0, '1461');
    
 

 