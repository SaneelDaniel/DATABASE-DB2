-- Project 3 DDLs: drop.clp
--
connect to sample;
--
-- drop previous definition first
drop view p3.total_balance;
drop table p3.account;
drop table p3.customer;
--
-- 
commit;
terminate;