import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
//import java.util.*;


/**
 * Manage connection to database and perform SQL statements.
 */
public class BankingSystem {

	// Connection properties
	private static String driver;
	private static String url;
	private static String username;
	private static String password;
	//private static boolean connected = false;

	// JDBC Objects
	private static Connection con;
	private static Statement stmt;
	private static ResultSet rs;

	//personal variables
	public static int mainAcctNo = 1000;
	public static int mainCustId = 100;
	private static boolean transfer = false;

	/**
	 * Initialize database connection given properties file.
	 * @param filename name of properties file
	 */
	public static void init(String filename) {
		try {
			Properties props = new Properties();						// Create a new Properties object
			FileInputStream input = new FileInputStream(filename);	// Create a new FileInputStream object using our filename parameter
			props.load(input);										// Load the file contents into the Properties object
			driver = props.getProperty("jdbc.driver");				// Load the driver
			url = props.getProperty("jdbc.url");						// Load the url
			System.out.println("url: " + url);	//test
			username = props.getProperty("jdbc.username");			// Load the username
			System.out.println("username: " + username);	//test
			password = props.getProperty("jdbc.password");			// Load the password
			System.out.println("pwd: " + password);	//test
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * Test database connection.
	 */
	public static void testConnection() {
		System.out.println(":: TEST - CONNECTING TO DATABASE");
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, username, password);
			con.close();
			System.out.println(":: TEST - SUCCESSFULLY CONNECTED TO DATABASE");
		} catch (Exception e) {
			System.out.println(":: TEST - FAILED CONNECTED TO DATABASE");
			e.printStackTrace();
		}
	}


	/*
	 * Method to get the customer id 
	 * @par
	 */
	private static int getCustId(String name, String gender, String age, String pin) {
		try {
			stmt = con.createStatement();
			String query = String.format("SELECT ID FROM P1.CUSTOMER WHERE name = '%s' AND gender = '%s' AND age = %s AND pin = %s;", name, gender, age, pin);
			rs = stmt.executeQuery(query);
			int id = 0;
			while(rs.next()) {
				id = rs.getInt(1);
			}
			return id;
		}
		catch(Exception e) {
			System.out.println("Error Generating Customer Id");
			e.printStackTrace();
		}

		return 0;
	}


	/*
	 * Method to get customer Account Number
	 */
	private static int getAcctnum(String id, String type, String amount) {
		try {
			stmt = con.createStatement();
			String query = String.format("SELECT NUMBER FROM P1.ACCOUNT WHERE id = %s AND type = '%s' AND BALANCE = %s;", id, type, amount);
			rs = stmt.executeQuery(query);
			int acctNo = 0;
			while(rs.next()) {
				acctNo = rs.getInt(1);
			}
			return acctNo;
		}
		catch(Exception e) {
			System.out.println(":: ERROR - COULD NOT GENERATE ACCOUNT NUMBER ::");
			e.printStackTrace();
		}
		return 0;
	}

	/*
	 * Method to get Customer Name After Login
	 * @param String custId
	 * @return String customer name
	 */
	public static String getCustName(String custId) {
		try {
			stmt = con.createStatement();
			String query = String.format("Select Name FROM P1.Customer WHERE Id = %s;", custId);
			rs = stmt.executeQuery(query);
			String name = "";
			while(rs.next()) {
				name = name + rs.getString(1);
			}
			return name;
		}
		catch(Exception e) {
			System.out.println(":: Error getting Customer Name ::");
			e.printStackTrace();
		}
		return null;
	}


	/*
	 * Method for customer login and authentication
	 * @param String customerId, String PinCode
	 * @return true if pin & id combination exists
	 */
	public static boolean login(String id, String pin) {
		try {
			System.out.println(":: LOGGING IN... Please Wait!\n");
			String query = String.format("Select id, pin from p1.Customer where id = %s and pin = %s", id, pin);         
			Statement stmt = con.createStatement();                                                 
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {                                                                      
				int thisid = rs.getInt(1);
				if(thisid == Integer.parseInt(id)){
					System.out.println(":: LOG IN COMPLETE ::\n");
					return true;
				} 
			}         
			System.out.println(":: PLEASE INPUT VALID ID & PIN ::\n");
			rs.close();                                                                             
			stmt.close();     
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;	
		}	
	}


	/*
	 * Method to check Account Status
	 * @param account number
	 * @return true if Active
	 */
	public static boolean checkAcctStatus(String acctNo) {
		try {
			stmt = con.createStatement();
			String query = String.format("Select Status FROM P1.Account WHERE Number = %s;", acctNo);
			rs = stmt.executeQuery(query);
			String stat = "";
			while(rs.next()) {
				stat = stat + rs.getString(1);
			}
			//System.out.println("Status:" + stat);
			if(stat.equals("A")) {
				return true;
			}
			else {
				System.out.println("\n:: Account Is Inactive: " + acctNo + " ::");
				return false;
			}
		}
		catch(Exception e) {
			System.out.println(":: Account Inactive ::");
		}
		return false;
	}


	/*
	 * validate customer action on the given acctNo
	 * @param String account number
	 * @return true if the param - accountNo belongs to the currently logged in Customer
	 */
	public static boolean validated(String acctNo) {
		try {

			stmt = con.createStatement();
			String query = String.format("SELECT ID FROM P1.Account WHERE NUMBER = %s;", acctNo);
			rs = stmt.executeQuery(query);
			int id=0;
			while(rs.next()) {
				id = rs.getInt(1);
			}
			if(id == BankingUI.custId) {
				return true;
			}
			stmt.close();
			rs.close();
			con.commit();
		} catch(Exception e) {
			System.out.println("::  COULD NOT VALIDATE - PLEASE ENTER A VALID ACCOUNT NUMBER  ::");
			e.printStackTrace();
		}
		return false;
	}


	/*
	 * getting the connection ready
	 */
	public static void getConnected() {
		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, username, password);
			con.setAutoCommit(false);
			stmt = con.createStatement();
			//connected = true;
			System.out.println(":: CONNECTING TO DATABASE ::\n\n\n\n");
		} catch (Exception e) {
			System.out.println(":: FAILED CONNECTED TO DATABASE ::");
			e.printStackTrace();
		}
	}


	/*
	 * Close the current connection to the database
	 */
	public static void closeConnection() {
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, username, password);
			con.close();
			//connected = false;
			System.out.println(":: SUCCESSFULLY DISCONNECTED FROM DATABASE ::");
		} catch (Exception e) {
			System.out.println(":: FAILED TO DISCONNECT FROM DATABASE ::");
			e.printStackTrace();
		}
	}


	/*
	 * create tables
	 */
	public static void createTables() {
		try {
			stmt = con.createStatement();
			String query1 = "create table P1.Customer(ID INT NOT NULL Primary Key GENERATED BY DEFAULT AS IDENTITY (START WITH 100, INCREMENT BY 1), Name varchar(15) not null, Gender char not null CHECK (Gender in ('M','F', 'm', 'f')), Age int not null CHECK (Age >= 0), Pin int not null CHECK (Pin >= 0));";
			//System.out.println(query1);
			stmt.executeUpdate(query1);
			String query2 = "create table P1.Account(NUMBER INT NOT NULL Primary Key GENERATED ALWAYS AS IDENTITY (START WITH 1000, INCREMENT BY 1),ID int not null, Balance int not null CHECK (Balance >= 0), Type char not null CHECK (Type in ('C', 'S', 'c', 's')), Status char not null CHECK (Status in ('A','I')), constraint my_constraint foreign key(ID) REFERENCES P1.Customer(ID) ON DELETE CASCADE);";
			stmt.executeUpdate(query2);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(":: FAILED TO CREATE NEW CUSTOMER ::");
			e.printStackTrace();
		}
	}


	/**
	 * Create a new customer.
	 * @param name customer name
	 * @param gender customer gender
	 * @param age customer age
	 * @param pin customer pin
	 */
	public static void newCustomer(String name, String gender, String age, String pin) 
	{
		try {
			System.out.println("\n:: CREATE NEW CUSTOMER - STARTING ::");
			stmt = con.createStatement(); 
			String new_customer = String.format("Insert into P1.CUSTOMER(Name, Gender, Age, Pin) values ('%s', '%s', %s, %s)", name, gender, age, pin); 
			//System.out.println(new_customer);
			stmt.executeUpdate(new_customer);
			stmt.close();
			con.commit();
			int custId = BankingSystem.getCustId(name, gender, age, pin);
			System.out.println("\n:: CREATE NEW CUSTOMER - SUCCESS ::");
			System.out.printf("Welcome %s\n\nThank You For Joining Us,\nWe Are Here To Provide You With The Best Services Possible\n\nPLEASE NOTE DOWN YOUR CUSTOMER ID:%s\n\n[NOTE: Also Please Remember Your PIN!]\n",name, custId);
		}
		catch(Exception e){
			System.out.println(":: FAILED TO CREATE NEW CUSTOMER ::");
			e.printStackTrace();
		}
	}


	/*
	 * Drop tables
	 */
	public static void dropTable()
	{
		try {
			System.out.println(":: DROP TABLE - STARTING");
			Class.forName("com.ibm.db2.jcc.DB2Driver");                             
			con = DriverManager.getConnection (url, username, password);                 
			stmt = con.createStatement(); 
			String drop_table_customer = String.format("Drop Table P1.CUSTOMER if exists"); 
			stmt.executeUpdate(drop_table_customer);
			String drop_table_account = String.format("Drop Table P1.ACCOUNT if exists"); 
			stmt.executeUpdate(drop_table_account);
			stmt.close();
			con.commit();
			con.close();
			System.out.println(":: DROP TABLE - SUCCESS ::");

		}
		catch(Exception e){
			System.out.println(":: DROP TABLE - FAILED ::");
		}
	}


	/**
	 * Open a new account.
	 * @param id customer id
	 * @param type type of account
	 * @param amount initial deposit amount
	 */
	public static void openAccount(String id, String type, String amount) 
	{
		try{
			System.out.println("\n:: OPEN ACCOUNT - RUNNING ::");
			stmt = con.createStatement(); 
			String query = String.format("Insert into P1.Account(ID, Balance, Type, Status) values (%s, %s, '%s', 'A')", id, amount, type);
			stmt.executeUpdate(query);
			stmt.close();
			con.commit();
			System.out.println(":: OPEN ACCOUNT - SUCCESS ::");
			int acctNum = BankingSystem.getAcctnum(id, type, amount);
			System.out.println("[Please Note Down Your Account Number: " + acctNum + "]");
			System.out.println("\n[NOTE PLEASE REMEMBER YOUR PIN]\n");

		}
		catch(Exception e){
			System.out.println(":: ERROR IN OPENING ACCOUNT ::\n\n");
			e.printStackTrace();
		}
	}


	/**
	 * Close an account.
	 * @param accNum account number
	 */
	public static void closeAccount(String accNum) 
	{

		try {
			System.out.println("\n:: RUNNING - CLOSE ACCOUNT: " + accNum + " ::");
			Class.forName(driver);
			con = DriverManager.getConnection(url, username, password);
			stmt = con.createStatement(); 
			String query = String.format("Update P1.Account set balance = 0, status = 'I' where Number = %s", accNum); //, and ID = %s BankingUI.custId);
			//System.out.println(query);
			stmt.executeUpdate(query);
			stmt.close();
			con.commit();
			System.out.println(":: CLOSE ACCOUNT - SUCCESS: " + accNum + " ::\n");
		} catch (Exception e) {
			System.out.println("Exception in Deleting the Account: " + accNum + "\n\n");
			e.printStackTrace();
		}
	}


	/**
	 * Deposit into an account. 
	 * @param accNum account number
	 * @param amount deposit amount
	 * @throws SQLException 
	 */
	public static void deposit(String accNum, String amount)
	{
		try {
			if(!transfer) {System.out.println("\n:: RUNNING - DEPOSIT AMOUNT ::");}
			stmt = con.createStatement(); 
			/*
			if(!transfer) {
				int balance = BankingSystem.getBalance(accNum);
				if(BankingUI.custId == BankingSystem.getCustId(accNum)){
					System.out.println("Balance before Deposit: " + balance + "\n\n");
				}
			}

			con.commit();
			rs.close();
			 */
			stmt = con.createStatement(); 
			String query = String.format("Update P1.Account set Balance = Balance + %s where Number = %s;", amount, accNum);// and status = 'A' after where
			//System.out.println(query);
			stmt.executeUpdate(query);
			stmt = con.createStatement(); 
			int balance2 = BankingSystem.getBalance(accNum);
			
			if(!transfer) {
				System.out.println(":: DEPOSIT AMOUNT - SUCCESS ::");
				if(BankingUI.custId == BankingSystem.getCustId(accNum)) {
					System.out.println("Balance After Deposit: " + balance2 + "\n\n");
				}
			}
			else {
				transfer = false;
			}			
			 
			con.commit();
			rs.close();
		} catch (Exception e) {
			System.out.println("Exception in Depositing the Amount: " + amount + "\n\n");
			e.printStackTrace();
		}
	}


	/*
	 * Helper method to get customer if from the given account number
	 * a variation of the get customer id 
	 * @param acct number
	 * @return Customer ID
	 */
	private static int getCustId(String accNum) {
		try {
			stmt = con.createStatement();
			String query = String.format("SELECT ID FROM p1.ACCOUNT WHERE NUMBER + %s;", accNum);
			rs = stmt.executeQuery(query);
			int custId = 0;
			while(rs.next()) {
				custId = rs.getInt(1);
			}
			return custId;
		}
		catch(Exception e) {
			System.out.println("ERROR IN GETTING CUSTOMER ID");
		}
		return 0;
	}


	/*
	 * returns the current balance in the account
	 * @param String account number to get the balance for
	 * @return int balance
	 */
	private static int getBalance(String acctNo) {
		try {
			stmt = con.createStatement();
			String query = String.format("SELECT BALANCE FROM P1.Account WHERE NUMBER = %s;", acctNo);
			rs = stmt.executeQuery(query);
			int balance = 0;
			while(rs.next()) {
				balance = rs.getInt(1);
			}
			return balance;
		}
		catch(Exception e) {
			System.out.println(":: Error in Getting the Balance from the Account Number ::" + acctNo);
			e.printStackTrace();
		}
		return 0;
	}


	/**
	 * Withdraw from an account.
	 * @param accNum account number
	 * @param amount withdraw amount
	 */
	public static void withdraw(String accNum, String amount) 
	{
		try{
			if(!transfer) {
				System.out.println("\n:: WITHDRAW - RUNNING ::");
			}
			stmt = con.createStatement(); 

			int balance = BankingSystem.getBalance(accNum);

			int amt = Integer.parseInt(amount);
			if(balance < amt) {
				System.out.println("Not Enough Funds to Withdraw, Current Balance = " + balance);
			}
			else {

				if(!transfer) {
					System.out.println("Your Current Balance is: " + balance);
				}

				String query = String.format("Update P1.Account set Balance = Balance - %s where Number = %s and status = 'A'", amount, accNum);
				stmt.executeUpdate(query);
				balance = BankingSystem.getBalance(accNum);
				if(!transfer) {System.out.println("\n:: WITHDRAW - SUCCESS :: \nBALANCE AFTER WITHDRAW:"  + balance +  "\n\n");}
				//}
				stmt.close();
				con.commit();

			}
		}
		catch(Exception e){
			System.out.println("::  Withdraw Error  ::\n\n");
			e.printStackTrace();
		}

	}

	/**
	 * Transfer amount from source account to destination account. 
	 * @param srcAccNum source account number
	 * @param destAccNum destination account number
	 * @param amount transfer amount
	 */
	public static void transfer(String srcAccNum, String destAccNum, String amount) 
	{
		try {
			long balance = BankingSystem.getBalance(srcAccNum);
			long amt = Integer.parseInt(amount);
			if(balance < amt) {
				System.out.println("\n:: NOT ENOUGH BALANCE TO TRANSFER ::\n");
				System.out.printf("Current Balance: %s \n\n", balance);
			}
			else {

				System.out.println("\n:: TRANSFER RUNNING ::");
				transfer = true;
				BankingSystem.withdraw(srcAccNum, amount);
				transfer = true;
				BankingSystem.deposit(destAccNum, amount);
				balance = BankingSystem.getBalance(srcAccNum);
				System.out.println(":: TRANSFER SUCCESS ::");
				System.out.println("[ Your Balance After Transfer: " + balance + " ]\n\n");
			}
		}
		catch(Exception e) {
			System.out.println(":: ERROR IN TRANSFERING MONEY ::");
			e.printStackTrace();
		}
	}


	/**
	 * Display account summary.
	 * @param cusID customer ID
	 */
	public static void accountSummary(String cusID) 
	{
		try {
			Class.forName("com.ibm.db2.jcc.DB2Driver");
			con = DriverManager.getConnection(url, username, password);
			System.out.println(":: ACCOUNT SUMMARY - STARTING");
			stmt = con.createStatement(); 
			String summary_account = String.format("SELECT Number, Balance From P1.ACCOUNT WHERE P1.ACCOUNT.ID = '%s' AND STATUS = 'A';", cusID); 
			rs = stmt.executeQuery(summary_account);
			int total = 0;
			System.out.println("NUMBER      BALANCE");
			System.out.println("----------- -----------");

			while (rs.next()){
				System.out.printf("%11s %11s\n",rs.getString(1),rs.getString(2));
				total = total + rs.getInt(2);
			}
			System.out.println("-----------------------");
			System.out.printf("TOTAL       %11s\n", total);
			stmt.close();
			con.commit();
			System.out.println(":: ACCOUNT SUMMARY - SUCCESS ::\n\n");
		}
		catch(Exception e)
		{
			System.out.println(":: ACCOUNT SUMMARY - FAILED ::\n\n");
			e.printStackTrace();
		}
	}


	/**
	 * Display Report A - Customer Information with Total Balance in Decreasing Order.
	 */
	public static void reportA() 
	{
		try {
			System.out.println(":: REPORT A - RUNNING ::");       
			Class.forName(driver);                             
			con = DriverManager.getConnection (url, username, password);                 
			con.setAutoCommit(false);
			stmt = con.createStatement();                                              
			String query = "Select ID, Name, Gender, Age, TOTAL from p1.customer Join (select ID as accountID,sum(balance) as TOTAL from p1.account group by id) on accountID = p1.customer.id order by Total Desc";
			System.out.println("ID          NAME            GENDER   AGE          TOTAL");
			System.out.println("----------- --------------- ------   -----------  ------------");	
			rs = stmt.executeQuery(query);    
			while(rs.next()) {
				String Id = rs.getString(1);
				String name = rs.getString(2);
				String gender = rs.getString(3);
				String age = rs.getString(4);
				//String pin = rs.getString(5);
				String tot = rs.getString(5);
				System.out.printf("%11s %-15s %-6s   %11s  %12s\n", Id, name, gender, age, tot); 
			}

			rs.close();                                                                             
			stmt.close();                                                                           
			System.out.println(":: REPORT A - SUCCESS\n");
		} catch (Exception e) {
			System.out.println(":: ERROR - REPORT COULDNOT BE GENERATED AT THIS TIME");
			e.printStackTrace();
		}
	}


	/**
	 * Display Report B - Customer Information with Total Balance in Decreasing Order.
	 * @param min minimum age
	 * @param max maximum age
	 */
	public static void reportB(String min, String max) 
	{
		try 
		{
			System.out.println("\n:: REPORT B - RUNNING ::\n");
			String query = String.format("Select AVG(balance) from p1.account JOIN p1.customer ON p1.customer.id = p1.account.id where p1.customer.age >=%s AND p1.customer.age <= %s and p1.account.status = 'A'", min, max);
			Statement stmt = con.createStatement();                                                 			
			ResultSet rs = stmt.executeQuery(query);                                                
			while(rs.next()) {
				int balance = rs.getInt(1);
				System.out.println();
				System.out.println("AVERAGE    ");
				System.out.println("-----------");
				System.out.printf("%11s", balance);
			}
			rs.close();                                                                             
			stmt.close();                                                                           
			System.out.println("\n:: REPORT B - SUCCESS\n");
		} 
		catch (Exception e) {
			System.out.println("::  ERROR - COULD NOT GENERATE REPORT B ::");

		}
	}
}

