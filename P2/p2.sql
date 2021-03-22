CONNECT TO CS157A @
DROP PROCEDURE p2.CUST_CRT @
DROP PROCEDURE p2.CUST_LOGIN @
DROP PROCEDURE p2.ACCT_OPN @
DROP PROCEDURE p2.ACCT_CLS @
DROP PROCEDURE p2.ACCT_DEP @
DROP PROCEDURE p2.ACCT_WTH @
DROP PROCEDURE p2.ACCT_TRX @
DROP PROCEDURE p2.ADD_INTEREST @
--
--
--
CREATE PROCEDURE p2.CUST_CRT
(
IN NameIN VARCHAR(15), 
IN GenderIN CHAR(1), 
IN AgeIN integer, 
IN PinIN integer,
OUT Cust_ID Integer, 
OUT SQL_CODE CHAR(5), 
OUT err_msg VARCHAR(30)
)
LANGUAGE SQL

BEGIN
DECLARE error_cond CONDITION FOR SQLSTATE '02000';
    DECLARE CONTINUE HANDLER FOR error_cond
      SET SQL_CODE = '02000';

INSERT INTO p2.customer(Name,Gender,Age,Pin) VALUES (NameIN,GenderIN,AgeIN,p2.encrypt(PinIN));
SET SQL_CODE = '00000';
SET err_msg = 'NO ERROR';
SELECT p2.customer.ID INTO Cust_ID FROM p2.customer WHERE p2.customer.Name = NameIN and p2.decrypt(p2.customer.Pin) = PinIN;
IF SQL_CODE = '02000' THEN SET err_msg = 'ERROR WHILE CREATING CUSTOMER';
END IF;
END @
--
--
--
CREATE PROCEDURE p2.CUST_LOGIN
(
IN IDIN Integer, 
IN PinIN Integer,
OUT Valid CHAR(1), 
OUT SQL_CODE CHAR(5), 
OUT err_msg VARCHAR(30)
)
LANGUAGE SQL

BEGIN
DECLARE temp_id Integer;
DECLARE error_cond CONDITION FOR SQLSTATE '02000';
    DECLARE CONTINUE HANDLER FOR error_cond
      SET SQL_CODE = '02000';
SET Valid = '1';
SET SQL_CODE = '00000';
SET err_msg = 'NO ERROR';

SELECT p2.customer.ID INTO temp_id FROM p2.customer WHERE p2.customer.ID = IDIN and p2.decrypt(p2.customer.Pin) = PinIN;
IF SQL_CODE = '02000' THEN SET err_msg = 'LOGIN FAILED';
END IF;
IF SQL_CODE = '02000' THEN SET Valid = '0';
END IF;
END @
--
--
--
CREATE PROCEDURE p2.ACCT_OPN
(
IN IDIN Integer, 
IN BalanceIN Integer, 
IN TypeIN CHAR(1),
OUT Account_Id Integer, 
OUT SQL_CODE CHAR(5), 
OUT err_msg VARCHAR(30)
)
LANGUAGE SQL

BEGIN
DECLARE error_cond CONDITION FOR SQLSTATE '02000';
    DECLARE CONTINUE HANDLER FOR error_cond
      SET SQL_CODE = '02000';

IF EXISTS(SELECT ID FROM p2.customer WHERE p2.customer.ID = IDIN)
THEN IF BalanceIN>0
THEN INSERT INTO p2.account(ID,Balance,Type,Status) VALUES (IDIN,BalanceIN,TypeIN,'A');
END IF;
END IF;
SET SQL_CODE = '00000';
SET err_msg = 'NO ERROR';

SELECT p2.account.Number INTO Account_Id FROM p2.account WHERE p2.account.ID = IDIN and p2.account.Balance = BalanceIN;
IF SQL_CODE = '02000' THEN SET err_msg = 'ERROR IN OPENING ACCOUNT';
END IF;
END @
--
--
--
CREATE PROCEDURE p2.ACCT_CLS
(
IN NumberIn Integer,
OUT SQL_CODE CHAR(5), 
OUT err_msg CHAR(30)
)
LANGUAGE SQL

BEGIN
DECLARE temp Integer;
DECLARE error_cond CONDITION FOR SQLSTATE '02000';
    DECLARE CONTINUE HANDLER FOR error_cond
      SET SQL_CODE = '02000';

SET SQL_CODE = '00000';
SET err_msg = 'no error';
SELECT p2.account.Number INTO temp FROM p2.account WHERE p2.account.Number = NumberIn;

IF SQL_CODE = '00000' THEN UPDATE p2.account SET Balance = 0, Status = 'I' WHERE p2.account.Number = NumberIn;
END IF;
IF SQL_CODE = '02000' THEN SET err_msg = 'ERROR IN CLOSING ACCT';
END IF;
END @
--
--
--
CREATE PROCEDURE p2.ACCT_DEP
(
IN NumberIn Integer, 
IN AmtIn Integer,
OUT SQL_CODE CHAR(5), 
OUT err_msg CHAR(30)
)
LANGUAGE SQL

BEGIN
DECLARE temp Integer;
DECLARE error_cond CONDITION FOR SQLSTATE '02000';
    DECLARE CONTINUE HANDLER FOR error_cond
      SET SQL_CODE = '02000';
SET SQL_CODE = '00000';
SET err_msg = 'no error';

SELECT p2.account.Number INTO temp FROM p2.account WHERE p2.account.Number = NumberIn;
IF SQL_CODE = '00000' THEN UPDATE p2.account SET Balance = Balance + AmtIn WHERE p2.account.Number = NumberIn and p2.account.status = 'A' and AmtIn>0;
END IF;
IF SQL_CODE = '02000' THEN SET err_msg = 'ERROR IN DEPOSITING';
END IF;
END @
--
--
--
CREATE PROCEDURE p2.ACCT_WTH
(
IN NumberIn Integer, 
IN AmtIn Integer,
OUT SQL_CODE CHAR(5), 
OUT err_msg CHAR(30)
)
LANGUAGE SQL

BEGIN
DECLARE temp Integer;
DECLARE error_cond CONDITION FOR SQLSTATE '02000';
    DECLARE CONTINUE HANDLER FOR error_cond
      SET SQL_CODE = '02000';
SET SQL_CODE = '00000';
SET err_msg = 'no error';

SELECT p2.account.Number INTO temp FROM p2.account WHERE p2.account.Number = NumberIn;
IF SQL_CODE = '00000' THEN UPDATE p2.account SET Balance = Balance - AmtIn WHERE p2.account.Number = NumberIn and Balance>=AmtIn and AmtIn>0 and p2.account.status = 'A';
END IF;
IF SQL_CODE = '02000' THEN SET err_msg = 'ERROR WHILE WITHDRAWING';
END IF;
END @
--
--
--
CREATE PROCEDURE p2.ACCT_TRX
(
IN Src_Acct Integer, 
IN Dest_Acct Integer, 
IN Amt Integer,
OUT SQL_CODE CHAR(5), 
OUT err_msg CHAR(30)
)
LANGUAGE SQL

BEGIN
CALL p2.ACCT_DEP(Dest_Acct,Amt,SQL_CODE,err_msg);
CALL p2.ACCT_WTH(Src_Acct,Amt,SQL_CODE,err_msg);
END @
--
--
--
CREATE PROCEDURE p2.ADD_INTEREST
(
IN Savings_Rate float, 
IN Checking_Rate float,
OUT SQL_CODE CHAR(5), 
OUT err_msg VARCHAR(30)
)

BEGIN
SET SQL_CODE = '00000';
SET err_msg = 'NO ERROR';
UPDATE p2.account SET Balance = Balance + (Balance*Savings_Rate) WHERE p2.account.status = 'A' and p2.account.Type = 'S';
UPDATE p2.account SET Balance = Balance + (Balance*Savings_Rate) WHERE p2.account.status = 'A' and p2.account.Type = 'C';
END @
--
