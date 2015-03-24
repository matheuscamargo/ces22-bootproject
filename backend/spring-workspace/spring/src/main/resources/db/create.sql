-- Create table Hyperlink
create table Hyperlink (id int AUTO_INCREMENT primary key NOT NULL, link varchar(255));

create table Comment ( id int AUTO_INCREMENT primary key NOT NULL, comment varchar(1000) DEFAULT NULL, hyperLinkid int );

create table MetaTag ( id int AUTO_INCREMENT primary key NOT NULL, tag varchar(1000) DEFAULT NULL, hyperLinkid int );
