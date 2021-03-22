
/**
 * Main entry to program.
 */
public class ProgramLauncher {

	public static String ID;
	public static int Pin;
	
	
	public static void main(String argv[]) {
		System.out.println(":: PROGRAM START");
		
		if (argv.length < 1) {
			System.out.println("Need database properties filename");
		} else {
			BankingSystem.init(argv[0]);
			BankingSystem.testConnection();
			BatchInputProcessor.run(argv[0]);
			//BankingSystem.getConnected();
			//BankingUI.mainMenu();
		}
	}
}