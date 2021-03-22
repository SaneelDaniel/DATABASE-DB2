--
drop procedure p2.test4@
drop procedure p2.test3@
--
--
CREATE PROCEDURE p2.test3 
(IN jobName CHAR(8), OUT jobCount INTEGER)
LANGUAGE SQL
  BEGIN
    DECLARE SQLSTATE CHAR(5);

    declare c1 cursor for
      SELECT count(jobName) 
        from employee 
      where job = jobName;
    
    open c1;
    fetch c1 into jobCount;
    close c1;

END @

CREATE PROCEDURE p2.test4 
(IN jobName CHAR(18), OUT jobCount INTEGER, OUT msg VARCHAR(30))
LANGUAGE SQL
  BEGIN
    DECLARE err integer;
    DECLARE v_count integer;
    DECLARE error_cond CONDITION FOR SQLSTATE '22001';
    DECLARE CONTINUE HANDLER FOR error_cond
      SET msg = 'Input string too long'; 

    SET v_count = -1;

    CALL p2.test3(jobName, v_count);
    set jobCount = v_count;

END @
 