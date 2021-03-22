Hey. so apparently i had some validation issues in the previous file due to which it did not run the Test properly, but i talked with the professor about it and he asked me to submit the new fixed files, 

I noticed that the main issue was that. my account numbers were starting from 10000 instead of 1000, So i am attaching a new set of files, BankingSystem.java, Create.sql, P1.java, db.properties, BankingUI.java, and this readme file, alongwith the batchInputProcessor.java and the jdbc driver.

I am attaching everything in a zip file, simply unzip it and run it in the same directory and it should be working:

Kindly consider, and also i would mention the steps below to run it, just in case:

Please Note that i have commented out the call BankingUI.mainMenu() in my p1.java, which would run the the UI, and left the BatchInputProcessor.run(argv[0]); call uncommented
			- this would only run the Batch Input processor, using the commands below; 
						- javac p1.java
						- java -cp ":./db2jcc4.jar" P1 db.properties

If you wish to run the UI, simply uncomment the call BankingUI.mainMenu() from the p1.java file and comment the BatchInputProcessor.run(argv[0]); and run the same commands.


Thank You and i really appreciate the effort. 