package atm;

import java.io.Console;
import java.util.Dictionary;
import java.util.Hashtable;

public class MyATM {

	public static void main(String[] args) {
		System.out.println("Welcome to ATM Simulator");
		System.out.println();
		showCommand();

		Console console = System.console();
		boolean isLogin = false;
		String userLogin = "";
		int userDeposit = 0;		
		
		Dictionary<String, Integer> userDeposits = new Hashtable<>();
		Dictionary<String, String> owedTo = new Hashtable<>();
		Dictionary<String, String> owedFrom = new Hashtable<>();

		while (1 == 1) {
			String command = console.readLine();

			String[] commandArr = command.split("\s+");

			if (commandArr.length == 1) {
				if (command.equals("logout")) {
					if (isLogin == true) {
						System.out.println("Goodbye, " + userLogin + "!");
						isLogin = false;
						userLogin = "";
						userDeposit = 0;
					} else {
						System.out.println("You are not login");
					}
				} else {
					commandIsWrong();
				}
			} else if (commandArr.length == 2) {

				switch (commandArr[0]) {
				case "login":
					if (isLogin == true) {
						System.out.println("You already login as " + userLogin);
					} else {
						isLogin = true;
						userLogin = commandArr[1];
						if (userDeposits.get(userLogin) != null) {
							userDeposit = userDeposits.get(userLogin);
						} else {
							userDeposits.put(userLogin, 0); // new user
						}
						System.out.println("Hello " + userLogin + "!");
						System.out.println("Your balance is $" + userDeposit);
						
						if(owedTo.get(userLogin) != null) {
							String[] owedToArr = owedTo.get(userLogin).split("\s+");
							System.out.println("Owed $" + owedToArr[1] + " to " + owedToArr[0]);
						}
						
						if(owedFrom.get(userLogin) != null) {
							String[] owedFromArr = owedFrom.get(userLogin).split("\s+");
							System.out.println("Owed $" + owedFromArr[1] + " from " + owedFromArr[0]);
						}
						
					}
					break;
				case "deposit":
					if (checkValidInteger(commandArr[1])) {
						if (isLogin == true) {
							userDeposit += Integer.parseInt(commandArr[1]);
							userDeposits.put(userLogin, userDeposit);
							System.out.println("Your balance is $" + userDeposit);
						} else {
							System.out.println("Please login before deposit");
						}
					} else {
						commandIsWrong();
					}

					break;
				case "withdraw":
					if (checkValidInteger(commandArr[1])) {
						if (isLogin == true) {
							userDeposit -= Integer.parseInt(commandArr[1]);
							userDeposits.put(userLogin, userDeposit);
							System.out.println("Your balance is $" + userDeposit);
						} else {
							System.out.println("Please login before deposit");
						}
					} else {
						commandIsWrong();
					}
					break;
				default:
					commandIsWrong();
				}

			} else if (commandArr.length == 3) {
				if (commandArr[0].equals("transfer")) {
					if (userDeposits.get(commandArr[1]) != null) {
						if (checkValidInteger(commandArr[2])) {
							String userTransf = commandArr[1];
							int userTransfBalance = userDeposits.get(commandArr[1]);
							if (Integer.parseInt(commandArr[2]) > userDeposit) {
								System.out.println("Transferred $" + userDeposit + "to " + userTransf);
								int diff = Integer.parseInt(commandArr[2]) - userDeposit;
								userTransfBalance += userDeposit;
								userDeposits.put(userTransf, userTransfBalance);
								userDeposit = 0;
								userDeposits.put(userLogin, 0);
								System.out.println("Your balance is $0");
								owedTo.put(userLogin, userTransf+" "+diff);
								owedFrom.put(userTransf, userLogin+" "+diff);
								System.out.println("Owed $" + diff + " to " + userTransf);
							} else {
								System.out.println("Transferred $" + commandArr[2] + " to " + userTransf);
								userTransfBalance += Integer.parseInt(commandArr[2]);
								userDeposits.put(userTransf, userTransfBalance);
								userDeposit -= Integer.parseInt(commandArr[2]);
								userDeposits.put(userLogin, userDeposit);
								System.out.println("Your balance is $" + userDeposit);
							}
						} else {
							commandIsWrong();
						}

					} else {
						System.out.println("User you want to transfer not exist");
					}
				} else {
					commandIsWrong();
				}
			} else {
				commandIsWrong();
			}

		}
	}

	static void showCommand() {
		System.out.println("Command : ");
		System.out.println(" 	`login [name]` - Logs in as this customer and creates the customer if not exist");
		System.out.println();
		System.out.println("	`deposit [amount]` - Deposits this amount to the logged in customer");
		System.out.println();
		System.out.println(" 	`withdraw [amount]` - Withdraws this amount from the logged in customer");
		System.out.println();
		System.out.println(
				" 	`transfer [target] [amount]` - Transfers this amount from the logged in customer to the target customer");
		System.out.println();
		System.out.println(" 	`logout` - Logs out of the current customer");
		System.out.println();
	}

	static void commandIsWrong() {
		System.out.println("Wrong Command!!");
		showCommand();
	}

	static boolean checkValidInteger(String input) {
		Boolean flag = true;
		for (int a = 0; a < input.length(); a++) {
			if (a == 0 && input.charAt(a) == '-')
				continue;
			if (!Character.isDigit(input.charAt(a)))
				flag = false;
		}

		return flag;

	}
}
