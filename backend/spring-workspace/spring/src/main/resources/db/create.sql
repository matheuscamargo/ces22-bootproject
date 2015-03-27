-- Create table Hyperlink
create table Hyperlink (id int AUTO_INCREMENT primary key NOT NULL, link varchar(255), created DATETIME NOT NULL, lastEdited DATETIME NOT NULL);

create table Comment ( id int AUTO_INCREMENT primary key NOT NULL, comment varchar(200) DEFAULT NULL, hyperlinkid int, FOREIGN KEY (hyperlinkid) REFERENCES Hyperlink(id));

create table MetaTag ( id int AUTO_INCREMENT primary key NOT NULL, tag varchar(20) DEFAULT NULL, hyperlinkid int, FOREIGN KEY (hyperlinkid) REFERENCES Hyperlink(id));
