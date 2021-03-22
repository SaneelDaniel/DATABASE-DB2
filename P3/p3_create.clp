-- Project 3 DDLs: create.clp
--
connect to cs157a;
--
-- drop previous definition first
drop view p3.total_balance;
drop table p3.account;
drop table p3.customer;
--
-- Without column constraint on Age & Pin, need logic in application to handle
--
create table p3.customer
(
  ID		integer generated always as identity (start with 100, increment by 1),
  Name		varchar(15) not null,
  Gender	char check (Gender in ('M','F')),
  Age		integer not null,
  Pin		integer not null,
  primary key (ID)
);
--
-- Without column constraint on Balance, Type, Status, need logic in application to handle
--
create table p3.account
(
  Number	integer generated always as identity (start with 1000, increment by 1),
  ID		integer not null references p3.customer (ID),
  Balance	integer not null,
  Type		char not null,
  Status	char not null,
  primary key (Number)
);
--
-- Views can reduce application logic. For example, you can join this view with p3.customer
-- to search total balance based on Age and/or Gender.
--
create view p3.total_balance as
  select a.ID, SUM(a.Balance) Total 
    from p3.account a 
  group by a.ID;
-- 

commit;
terminate;