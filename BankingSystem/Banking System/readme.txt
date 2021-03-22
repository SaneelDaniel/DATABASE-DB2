Read Me Please Before Running the Program, 

1.I have attached my create.sql file to create the Tables in my canvas submission, but i also have the create.sql in my database container, which you would be able to access if you log into it. 

2. My UI Class is accessed from P1.java that i am submitting along and also i am uploading all the .java files needed, kindly keep them all in the same directory to run it without any errors.

2. I have 3 .java file -> BankingSystem.java, ProgramLauncher.java and BankingUI.java
   Please find them all!
   And in the directory, run:

   	- javac P1.java
   	- java -cp ":./db2jcc4.jar" P1 mydb.properties

3. Also the only main() method is in p1.java so you would have to run that along with the db.properties file. 
	- I have Commented out the statement in P1.java, that runs the batchinput from the properties file but if you want to run them you can simply uncomment it.

4. I have create table and drop table methods written in the code, if you wish to use them, just un-comment those sections in the P1 and this would create tables when you start the app, and drop them at the end (if you are using both). 
	- if kept commented, it should not be a problem.

5. I know this would be extra work for you but i did not know until a day before submission, that we were to be checked by the batch input processor, we were told to develop a UI based system, so i put my validation checks accordingly

Kindly Consider The Points Below:

My program is structured to have a UI and run along with the UI
	
Running the Batch Processor directly may render unwanted results, but the reason is that i have multiple validation checks that have to be passed to access some functions Like:
	
- I validate a user before letting him/her Withdraw, Transfer, Login, Close Account, etc. 

- The test cases in the sample-db.properties file would have different results after the Withdraw function is completed as:
               Batch Input has the last new customer Misty so she can the program thinks that she is logged in as the last user, so it wont let her close other accounts and transfer from Pikachu's account.

- But i assure you if you run it with the UI, everything would work flawlessly

- Finally: I tried to make last minute changes to make it work, but i would have had to chaneg a lot of my validation logic, so left it to work with the UI.

THANK YOU VERY MUCH
