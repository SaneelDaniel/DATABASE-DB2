-- Project 2 DDLs: test.clp
--
connect to cs157a;
--
-- Quick test
--
insert into p2.customer(Name,Gender,Age,Pin) values('Michael','M',21,p2.encrypt(100));
insert into p2.account(ID, Balance, Type, Status) values(100, 100, 'C', 'A');
insert into p2.account(ID, Balance, Type, Status) values(100, 200, 'S', 'A');
--
insert into p2.customer(Name,Gender,Age,Pin) values('Diana','F',24,p2.encrypt(101));
insert into p2.account(ID, Balance, Type, Status) values(101, 300, 'C', 'A');
insert into p2.account(ID, Balance, Type, Status) values(101, 400, 'S', 'A');
--
insert into p2.customer(Name,Gender,Age,Pin) values('Amy','F',24,p2.encrypt(-99));
insert into p2.account(ID, Balance, Type, Status) values(101, 300, 'C', 'A');
insert into p2.account(ID, Balance, Type, Status) values(101, 400, 'S', 'A');
select * from p2.customer;
select p2.decrypt(pin) from p2.customer;
select * from p2.account;
--
commit;
terminate;