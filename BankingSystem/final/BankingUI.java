import java.util.*;


public class BankingUI {

	static Scanner scnr = new Scanner(System.in);
	static String custId;
	static String prev;


	/*
	 * the main menu
	 * Title – Welcome to the Self Services Banking System! – Main Menu
	 */
	public static void mainMenu() {
		prev = "main";
		System.out.println("::     Welcome to the Self Service Banking System! - Main Menu     ::");
		System.out.println("1. New Customer");
		System.out.println("2. Customer Login");
		System.out.println("3. Exit");
		int input = scnr.nextInt();

		//New Customer
		if(input == 1) {
			String name;
			String gender;
			String age;
			String pin;
			System.out.println("Please Enter your name: ");
			name = scnr.next();
			System.out.println("Please Enter your Gender: ");
			gender = scnr.next();
			System.out.println("Please Enter your Age: ");
			age = scnr.next();
			System.out.println("Please Enter a Secure Pin: ");
			pin = scnr.next();

			BankingSystem.newCustomer(name, gender, age, pin);
			
			//formatting page shifts
			System.out.println();
			System.out.println();
			System.out.println();
			mainMenu();
		}
		else if(input == 2) { //Customer Login
			String ID;
			String pin;
			System.out.println("Please Enter Your Customer ID: ");
			ID = scnr.next();
			System.out.println("Please Enter Your Pin: ");
			pin = scnr.next();

			if(ID.equals("0") && pin.equals("0")) {//admin login 
				//formatting page shifts
				System.out.println();
				System.out.println();
				System.out.println();
				administratorMenu();
			}

			if(BankingSystem.login(ID, pin)) {
				custId = ID;
				//formatting page shifts
				System.out.println();
				System.out.println();
				System.out.println();
				customerMenu();
			}
			else {
				System.out.println("You Entered an Invalid combination of ID & PIN, PLease Try Again!");
				System.out.println();
				System.out.println(":: RETURNING TO PREVIOUS PAGE ::");
				//formatting page shifts
				System.out.println();
				System.out.println();
				System.out.println();
				mainMenu();
			}
		}
		else if(input == 3) {//exit
			System.out.println("See You Next Time, Have a Good Day!");
			BankingSystem.closeConnection();
			System.exit(0);
		}
		else {//invalid entry
			System.out.println();
			System.out.println(":: INVALID ENTRY - RETURNING TO PREVIOUS PAGE ::");
			//formatting page shifts
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
		prev = "cust";
		System.out.println("::     Customer Main Menu     ::");
		System.out.println("1. Open Account");
		System.out.println("2. Close Account");
		System.out.println("3. Deposit");
		System.out.println("4. Withdraw");
		System.out.println("5. Transfer");
		System.out.println("6. Account Summary");
		System.out.println("7. Exit");
		int input = scnr.nextInt();

		if(input == 1) {//open accounnt
			System.out.println("Please Enter A Customer ID: ");
			String id = scnr.next();
			System.out.println("Please Enter The Type of Account to Create: ");
			String type = scnr.next();
			System.out.println("Please Enter the Amount for Initial Deposit: ");
			String amount = scnr.next();
			BankingSystem.openAccount(id, type, amount);

			//formatting page shifts
			System.out.println();
			System.out.println();
			System.out.println();

			customerMenu();
		}
		else if(input == 2) {//close Account
			System.out.println("Please Enter The Account Number to Close: ");
			String acctNo = scnr.next();

			if(BankingSystem.validated(acctNo)) {
				BankingSystem.closeAccount(acctNo);
			}

			//formatting page shifts
			System.out.println();
			System.out.println();
			System.out.println();

			customerMenu();
		}
		else if(input == 3) {//deposit
			System.out.println("Please Enter The Account Number to Deposit into: ");
			String acctNo = scnr.next();
			System.out.println("Please Enter The Amount to Deposit into Account Number: " + acctNo);
			String amount = scnr.next();
			BankingSystem.deposit(acctNo, amount);

			//formatting page shifts
			System.out.println();
			System.out.println();
			System.out.println();

			customerMenu();
		}
		else if(input == 4) {//withdraw
			System.out.println("Please Enter Your Account Number: ");
			String acctNo = scnr.next();
			System.out.println("Please Enter The Amount to Withdraw From Account Number: " + acctNo);
			String amount = scnr.next();

			if(BankingSystem.validated(acctNo)) {
				BankingSystem.withdraw(acctNo, amount);
			}
			//formatting page shifts
			System.out.println();
			System.out.println();
			System.out.println();

			customerMenu();
		}
		else if(input == 5) {//transfer
			System.out.println("Please Enter Your Source Account Number: ");
			String srcacctNo = scnr.next();
			System.out.println("Please Enter Your Destination Account Number: ");
			String destacctNo = scnr.next();
			System.out.println("Please Enter The Amount to Transfer From Account Number: " + srcacctNo + " into Account Number: " + destacctNo);
			String amount = scnr.next();

			if(BankingSystem.validated(srcacctNo)) {
				BankingSystem.transfer(srcacctNo, destacctNo, amount);
			}

			//formatting page shifts
			System.out.println();
			System.out.println();
			System.out.println();

			customerMenu();
		}
		else if(input == 6) {//accountSymmary
			BankingSystem.accountSummary(custId);
		}
		else if(input == 7) {//previous screen
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
				administratorMenu();
			}
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
		System.out.println("::     Administrator Main Menu     ::");
		System.out.println("1. Account Summary for a Customer");
		System.out.println("2. Report A :: Customer INformation with Total Balance in Decreasing Order");
		System.out.println("3. Report B :: Find the Average Total Balance Between Age Groups");
		System.out.println("4. Exit");
		int input = scnr.nextInt();

		if(input == 1) {//acct summary
			System.out.println("Please Enter the Customer ID to get the Summary for: ");
			custId = scnr.next();
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
			System.out.println("Please Enter the Min and Max Age for the Report: ");
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
