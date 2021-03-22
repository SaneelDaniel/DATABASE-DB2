import java.util.*;


public class BankingUI {

	static Scanner scnr = new Scanner(System.in);
	public static int custId;
	static String prev;
	private static String custName = null;
	private static Scanner scnr2 = new Scanner(System.in);
	//public static boolean done = false;


	/*
	 * the main menu
	 * Title – Welcome to the Self Services Banking System! – Main Menu
	 */
	public static void mainMenu() {
		prev = "main";
		boolean done = false;
		System.out.println("\n\n\n::     Welcome to the Self Service Banking System! - Main Menu     ::\n");
		System.out.println("1. New Customer\n");
		System.out.println("2. Customer Login\n");
		System.out.println("3. Exit\n");
		int input = scnr.nextInt();

		//New Customer
		if(input == 1) {
			String name = "";
			String lname = "";
			String gender = "";
			int age = 0;
			String pin = "";

			System.out.println("\nPlease Enter your name: ");			
			while(!done) {
				name = scnr.nextLine();
				if(name.length()>0) {
					done = true;
				}
			}

			System.out.println("\nPlease Enter your Gender ('M' For Male And 'F' For Female): ");
			while(!(gender.equals("M") || gender.equals("F"))) {
				gender = scnr.next();
				if(!(gender.equals("M") || gender.equals("F")))
					System.out.println("Please Enter A Valid Gender: ");
			}

			System.out.println("\nPlease Enter your Age: ");
			while(!(age>0)) {
				age = scnr.nextInt();
				if(!(age>0))
					System.out.println("\nPlease Enter a Valid Age: ");
			}

			System.out.println("\nPlease Enter a Secure Pin: ");
			pin = scnr.next();
			System.out.println();
			BankingSystem.newCustomer(name, gender, Integer.toString(age),pin);
			//formatting page 
			System.out.println();
			System.out.println();
			System.out.println();
			mainMenu();
		}
		else if(input == 2) { //Customer Login
			String ID;
			String pin;
			System.out.println("\nPlease Enter Your Customer ID: ");
			ID = scnr.next();
			System.out.println("\nPlease Enter Your Pin: ");
			pin = scnr.next();
			if(ID.equals("0") && pin.equals("0")) {//admin login 
				//formatting page
				System.out.println();
				System.out.println();
				System.out.println();
				administratorMenu();
			}

			if(BankingSystem.login(ID, pin)) {
				custId = Integer.parseInt(ID);
				custName = BankingSystem.getCustName(ID);
				//formatting page 
				System.out.println();
				System.out.println();
				System.out.println();
				customerMenu();
			}
			else {
				System.out.println("You Entered an Invalid combination of ID & PIN, PLease Try Again!\n");
				System.out.println();
				System.out.println(":: RETURNING TO PREVIOUS PAGE ::\n");
				//formatting page 
				System.out.println();
				System.out.println();
				System.out.println();
				mainMenu();
			}
		}
		else if(input == 3) {//exit
			System.out.println("\nSee You Next Time, Have a Good Day!");
			//BankingSystem.dropTable();
			BankingSystem.closeConnection();
			scnr.close();
			scnr2.close();
			System.exit(0);

		}
		else {//invalid entry
			System.out.println();
			System.out.println(":: INVALID ENTRY - RETURNING TO PREVIOUS PAGE ::\n\n\n");
			//formatting page 
			System.out.println();
			System.out.println();
			System.out.println();
			mainMenu();
		}
	}


	/*
	 * customer menu after login or get any information
	 * Title – Customer Main Menu
	 */
	public static void customerMenu() {
		//prev = "cust";
		System.out.printf("::     Welcome %s -- What Would You Like To Do Today?     ::\n\n", custName);
		System.out.println("1. Open Account\n");
		System.out.println("2. Close Account\n");
		System.out.println("3. Deposit\n");
		System.out.println("4. Withdraw\n");
		System.out.println("5. Transfer\n");
		System.out.println("6. Account Summary\n");
		System.out.println("7. Exit\n");
		int input = scnr.nextInt();

		if(input == 1) {//open accounnt
			System.out.println("\nPlease Enter A Customer ID (Customer Id's Can Only Be Numbers Starting from 100): ");
			String id = scnr.next();
			System.out.println("\nPlease Enter The Type of Account to Create (S For Savings And C For Checking): ");
			String type = scnr.next();
			System.out.println("\nPlease Enter the Amount for Initial Deposit: ");
			String amount = scnr.next();
			BankingSystem.openAccount(id, type, amount);
			//formatting page shifts
			System.out.println();
			System.out.println();
			System.out.println();
			customerMenu();
		}
		else if(input == 2) {//close Account
			System.out.println("\nPlease Enter The Account Number to Close: ");
			String acctNo = scnr.next();

			if(BankingSystem.validated(acctNo)) {
				BankingSystem.closeAccount(acctNo);
			}
			else {
				System.out.println("\n::  Invalid Account Number OR The Account Does Not Belong To You ::\n\n:: RETURNING TO PREVIOUS MENU  ::\n");
			}
			//formatting page shifts
			System.out.println();
			System.out.println();
			System.out.println();
			customerMenu();
		}
		else if(input == 3) {//deposit
			System.out.println("\nPlease Enter The Account Number to Deposit into: ");
			String acctNo = scnr.next();

			if(BankingSystem.checkAcctStatus(acctNo)){
				System.out.println("\nPlease Enter The Amount to Deposit into Account Number: " + acctNo);
				String amount = scnr.next();
				BankingSystem.deposit(acctNo, amount);
			}
			//formatting page shifts
			System.out.println();
			System.out.println();
			System.out.println();
			customerMenu();
		}
		else if(input == 4) {//withdraw
			System.out.println("\nPlease Enter Your Account Number: ");
			String acctNo = scnr.next();
			String amount = "";
			if(BankingSystem.validated(acctNo)) {
				System.out.println("\nPlease Enter The Amount to Withdraw From Account Number: " + acctNo + "\n");
				amount = scnr.next();
			}

			if(BankingSystem.validated(acctNo) && BankingSystem.checkAcctStatus(acctNo)) {
				BankingSystem.withdraw(acctNo, amount);
			}else {
				if(!BankingSystem.validated(acctNo)) {				
					System.out.println("The Account Does Not Belong To You!\nPlease Enter A Valid Account Number\n");
				}
				else {
					System.out.println(":: RETURNING TO PREVIOUS PAGE ::");
				}
			}
			//formatting page shifts
			System.out.println();
			System.out.println();
			System.out.println();
			customerMenu();
		}
		else if(input == 5) {//transfer
			System.out.println("\nPlease Enter Your Source Account Number: ");
			String srcacctNo = scnr.next();
			System.out.println("\nPlease Enter Your Destination Account Number: ");
			String destacctNo = scnr.next();
			System.out.println("\nPlease Enter The Amount to Transfer From Account Number: " + srcacctNo + " into Account Number: " + destacctNo);
			String amount = scnr.next();

			if(BankingSystem.validated(srcacctNo) && BankingSystem.checkAcctStatus(srcacctNo) &&  BankingSystem.checkAcctStatus(destacctNo)) {
				BankingSystem.transfer(srcacctNo, destacctNo, amount);
			}
			else {
				System.out.println("\nPlease Enter A Valid Account Number");
				System.out.println("\n:: RETURNING TO PREVIOUS PAGE ::");
			}
			//formatting page shifts
			System.out.println();
			System.out.println();
			System.out.println();
			customerMenu();
		}
		else if(input == 6) {//accountSymmary
			BankingSystem.accountSummary(Integer.toString(custId));
			//formatting page shifts
			System.out.println();
			System.out.println();
			System.out.println();

			customerMenu();
		}
		else if(input == 7) {
			mainMenu();
		}
		else {//Invalid Entry
			System.out.println();
			System.out.println(":: INVALID ENTRY - RETURNING TO PREVIOUS PAGE ::");
			//formatting page shifts
			System.out.println();
			System.out.println();
			System.out.println();
			customerMenu();
		}
	}

	/*
	 * Administrator Menu
	 * Title – Administrator Main Menu
	 */
	public static void administratorMenu() {
		prev = "admin";
		System.out.println("::     Administrator Main Menu     ::\n");
		System.out.println("1. Account Summary for a Customer\n");
		System.out.println("2. Report A :: Customer INformation with Total Balance in Decreasing Order\n");
		System.out.println("3. Report B :: Find the Average Total Balance Between Age Groups\n");
		System.out.println("4. Exit");
		int input = scnr.nextInt();

		if(input == 1) {//acct summary
			System.out.println("\nPlease Enter the Customer ID to get the Summary for: ");
			String custId = scnr.next();
			BankingSystem.accountSummary(custId);
			System.out.println();
			System.out.println();
			System.out.println();
			administratorMenu();
		}
		else if(input == 2) {//Report A
			BankingSystem.reportA();
			System.out.println();
			System.out.println();
			System.out.println();
			administratorMenu();
		}
		else if(input == 3) {//Report B
			System.out.println("\nPlease Enter the Min and Max Age for the Report: ");
			String min = scnr.next();
			String max = scnr.next();
			BankingSystem.reportB(min, max);
			//formatting page shifts
			System.out.println();
			System.out.println();
			System.out.println();

			administratorMenu();
		}
		else if(input == 4) {//Exit
			if(prev.equals("main")) {
				//formatting page shifts
				System.out.println();
				System.out.println();
				System.out.println();
				mainMenu();
			}
			else {
				//formatting page shifts
				System.out.println();
				System.out.println();
				System.out.println();
				customerMenu();
			}
		}
		else {//Invalid Entry
			System.out.println();
			System.out.println("::  INVALID ENTRY - RETURNING TO PREVIOUS PAGE ::");
			System.out.println();
			administratorMenu();
		}
	}

}
