package com.company;

import java.io.IOException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Main {

    // New ATM instance
    private static ATM atm = new ATMImpl();

    public static void main(String[] args) throws IOException {

        Scanner in = new Scanner(System.in);

        // Have to create an account before being able to access it
        System.out.println("Welcome! Enter credentials to open an account...");
        boolean accountCreated = false;
        while (!accountCreated) {
            try {
                System.out.print("AccountID: ");
                int accountId = Integer.parseInt(in.next());
                System.out.print("AccountPin: ");
                int accountPin = Integer.parseInt(in.next());
                if (accountId < 0 || accountPin < 0) {
                    throw new NumberFormatException("Inputs have to be positive");
                }
                atm.openAccount(accountId, accountPin);
                accountCreated = true;
            } catch (Exception e) {
                System.out.println("Inputs have to be valid positive integers");
            }
        }

        // Timer for 2 min timeout
        Timer timer = new Timer();
        timer.schedule(createTask(), 120000);

        while (in.hasNextLine()) {
            timer.cancel(); // Cancel the timer once the input is registered
            String[] input = in.nextLine().split(" "); // Split by whitespace for command amount inputs
            String command = input[0]; // Get the command

            switch (command) {
                case "authorize":
                    // Validate the input
                    try {
                        int accountIdReauth = Integer.parseInt(input[1]);
                        int accountPinReauth = Integer.parseInt(input[2]);
                        if (accountIdReauth < 0 || accountPinReauth < 0) {
                            System.out.println("Inputs have to be positive");
                            throw new NumberFormatException("Inputs have to be positive");
                        }
                        // Call the authorize method
                        atm.authorize(accountIdReauth, accountPinReauth);
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                        System.out.println("Invalid input! Please try again...\nFormat is: authorize <accountId> <accountPin>");
                    }
                    break;
                case "open":
                    // Validate the input
                    try {
                        int accountIdReauth = Integer.parseInt(input[1]);
                        int accountPinReauth = Integer.parseInt(input[2]);
                        if (accountIdReauth < 0 || accountPinReauth < 0) {
                            System.out.println("Inputs have to be positive");
                            throw new NumberFormatException("Inputs have to be positive");
                        }
                        // Call the openAccount method
                        atm.openAccount(accountIdReauth, accountPinReauth);
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                        System.out.println("Invalid input! Please try again...\nFormat is: open <accountId> <accountPin>");
                    }
                    break;
                case "withdraw":
                    // Validate the input
                    try {
                        int amount = Integer.parseInt(input[1]);
                        if (amount < 0) {
                            System.out.println("Inputs have to be positive");
                            throw new NumberFormatException("Inputs have to be positive");
                        }
                        // Call the withdraw method
                        atm.withdraw(amount);
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                        System.out.println("Invalid input! Please try again...\nFormat is: withdraw <amount>");
                    }
                    break;
                case "deposit":
                    // Validate the input
                    try {
                        int amount = Integer.parseInt(input[1]);
                        if (amount < 0) {
                            System.out.println("Inputs have to be positive");
                            throw new NumberFormatException("Inputs have to be positive");
                        }
                        // Call the deposit method
                        atm.deposit(amount);
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                        System.out.println("Invalid input! Please try again...\nFormat is: deposit <amount>");
                    }
                    break;
                case "balance":
                    // Call the balance method
                    atm.balance();
                    break;
                case "history":
                    // Call the history method
                    atm.history();
                    break;
                case "logout":
                    // Call the logout method
                    atm.logout();
                    break;
                case "end":
                    // End the program
                    System.out.println("Ending program!");
                    return;
                case "":
                    break;
                default:
                    // If nothing matches the input default to this
                    System.out.println("Invalid input! Please try again... Available commands {authorize, open, withdraw, deposit, balance, history, logout, end}");
                    break;
            }
            // Reset the timer
            timer = new Timer();
            timer.schedule(createTask(), 120000);
            System.out.print("Please enter a command: ");
        }
    }

    // Timeout task to call logout after session expires
    public static TimerTask createTask() {
        return new TimerTask() {
            @Override
            public void run() {
                atm.logout();
            }
        };
    }
}
