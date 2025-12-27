package miniproject1;

import java.io.*;
import java.util.Scanner;

abstract class Bank {
    abstract void deposit(double amount);
    abstract void withdraw(double amount);
    abstract void displayDetails();
}


class BankAccount extends Bank implements Serializable {
    private int accountNumber;
    private String accountHolder;
    protected double balance;

    public BankAccount(int accountNumber, String accountHolder, double balance) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = balance;
    }


    public int getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public double getBalance() {
        return balance;
    }


    void deposit(int amount) {
        deposit((double) amount);
    }


    @Override
    void deposit(double amount) {
        balance += amount;
        System.out.println("Amount deposited successfully.");
    }

    @Override
    void withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            System.out.println("Withdrawal successful.");
        } else {
            System.out.println("Insufficient balance!");
        }
    }

    @Override
    void displayDetails() {
        System.out.println("\n--- Account Details ---");
        System.out.println("Account Number : " + accountNumber);
        System.out.println("Account Holder : " + accountHolder);
        System.out.println("Balance        : ₹" + balance);
    }
}

class SavingsAccount extends BankAccount {
    public SavingsAccount(int accNo, String name, double balance) {
        super(accNo, name, balance);
    }


    @Override
    void withdraw(double amount) {
        if (balance - amount >= 500) {
            balance -= amount;
            System.out.println("Withdrawal successful (Savings Account).");
        } else {
            System.out.println("Minimum balance of ₹500 required.");
        }
    }
}

class CurrentAccount extends BankAccount {
    public CurrentAccount(int accNo, String name, double balance) {
        super(accNo, name, balance);
    }

    @Override
    void withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            System.out.println("Withdrawal successful (Current Account).");
        } else {
            System.out.println("Overdraft not allowed.");
        }
    }
}


public class BankApp {
    static final String FILE_NAME = "bankdata.text";

    public static void saveAccount(BankAccount account) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(account);
        } catch (IOException e) {
            System.out.println("Error saving account.");
        }
    }

    public static BankAccount loadAccount() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (BankAccount) ois.readObject();
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BankAccount account = loadAccount();

        if (account == null) {
            System.out.println("Create New Account");
            System.out.print("Enter Account Number: ");
            int accNo = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter Account Holder Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Initial Balance: ");
            double bal = sc.nextDouble();

            System.out.print("Account Type (1-Savings, 2-Current): ");
            int type = sc.nextInt();

            if (type == 1)
                account = new SavingsAccount(accNo, name, bal);
            else
                account = new CurrentAccount(accNo, name, bal);

            saveAccount(account);
        }

        int choice;
        do {
            System.out.println("\n--- Bank Menu ---");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Balance Enquiry");
            System.out.println("4. Display Account Details");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter amount: ");
                    account.deposit(sc.nextDouble());
                    saveAccount(account);
                    break;
                case 2:
                    System.out.print("Enter amount: ");
                    account.withdraw(sc.nextDouble());
                    saveAccount(account);
                    break;
                case 3:
                    System.out.println("Balance: ₹" + account.getBalance());
                    break;
                case 4:
                    account.displayDetails();
                    break;
                case 5:
                    System.out.println("Thank you for using Bank System.");
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        } while (choice != 5);

        sc.close();
    }
}
