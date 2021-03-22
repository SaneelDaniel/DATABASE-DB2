CONNECT to CS157a;

-- create customer
CALL p2.CUST_CRT('CUS1', 'M', 20, 1111, ?,?,?);
CALL p2.CUST_CRT('CUS2', 'F', 25, 2222, ?,?,?);
CALL p2.CUST_CRT('CUS3', 'M', 40, 3333, ?,?,?);


-- customer login
CALL p2.CUST_LOGIN(100, 1111, ?,?,?);
--customer login error cases
CALL p2.CUST_LOGIN(102, 1111, ?,?,?);
CALL p2.CUST_LOGIN(999, 9999, ?,?,?);

-- open account
CALL p2.ACCT_OPN(100, 100, 'C',?,?,?);
CALL p2.ACCT_OPN(100, 200, 'S',?,?,?);
CALL p2.ACCT_OPN(101, 300, 'C',?,?,?);
CALL p2.ACCT_OPN(101, 400, 'S',?,?,?);
CALL p2.ACCT_OPN(102, 500, 'C',?,?,?);
CALL p2.ACCT_OPN(102, 600, 'S',?,?,?);

-- open with invalid id
CALL p2.ACCT_OPN(999, 500, 'C',?,?,?);
-- invalid balance
CALL p2.ACCT_OPN(100, -100, 'C',?,?,?);

-- close account
CALL p2.ACCT_CLS(1004,?,?);
SELECT NUMBER, BALANCE, STATUS FROM p2.ACCOUNT WHERE NUMBER = 1004;
-- close invalid account
CALL p2.ACCT_CLS(9999,?,?);

--deposit into account 
CALL p2.ACCT_DEP(1000, 33, ?,?);
-- deposit into invalid account
CALL p2.ACCT_DEP(9999, 44, ?,?);
--deposit with negative balance
CALL p2.ACCT_DEP(1001, -44, ?,?);
CALL p2.ACCT_DEP(1004, 99, ?,?);
SELECT NUMBER, BALANCE FROM p2.account where NUMBER IN(1000, 1001, 1004);

-- withdraw from account
CALL p2.ACCT_WTH(1000, 22, ?, ?);
-- over drawn
CALL p2.ACCT_WTH(1002, 2000, ?, ?);
CALL p2.ACCT_WTH(1003, -88, ?, ?);
SELECT NUMBER, BALANCE FROM p2.account where NUMBER IN(1000, 1002);

UPDATE p2.account set Balance = 100 where number = 1000;
UPDATE p2.account set Balance = 200 where number = 1001;
UPDATE p2.account set Balance = 300 where number = 1002;
UPDATE p2.account set Balance = 400 where number = 1003;

--transfer to another account
CALL p2.ACCT_TRX(1003, 1002, 66, ?,?);
--different customer
CALL p2.ACCT_TRX(1005, 1000, 99, ?,?);
SELECT NUMBER, BALANCE FROM p2.account where NUMBER IN(1000, 1002, 1003, 1005);

UPDATE p2.account set Balance = 100 where number = 1000;
UPDATE p2.account set Balance = 200 where number = 1001;
UPDATE p2.account set Balance = 300 where number = 1002;
UPDATE p2.account set Balance = 400 where number = 1003;
UPDATE p2.account set Balance = 500 where number = 1004;
UPDATE p2.account set Balance = 600 where number = 1005;

--interest
SELECT NUMBER, BALANCE FROM p2.account;
CALL p2.ADD_INTEREST (0.5, 0.1,?,?);
SELECT NUMBER, BALANCE FROM p2.account;
