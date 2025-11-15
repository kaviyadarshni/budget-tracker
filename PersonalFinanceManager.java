import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

class Transaction {
    private String date;
    private String description;
    private double amount;
    private String type; // "Income" or "Expense"
    private String category;

    public Transaction(String description, double amount, String type, String category) {
        this.date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        this.description = description;
        this.amount = amount;
        this.type = type;
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return String.format("%s | %s | %.2f | %s | %s", date, description, amount, type, category);
    }
}

class FinanceManager {
    private ArrayList<Transaction> transactions;
    private final String fileName = "transactions.txt";

    public FinanceManager() {
        transactions = new ArrayList<>();
        loadTransactions();
    }

    public void addTransaction(String description, double amount, String type, String category) {
        Transaction transaction = new Transaction(description, amount, type, category);
        transactions.add(transaction);
        saveTransactions();
    }

    public void viewTransactions() {
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }
        System.out.println("Date       | Description       | Amount | Type   | Category");
        System.out.println("-------------------------------------------------------");
        for (Transaction t : transactions) {
            System.out.println(t);
        }
    }

    public void viewSummary() {
        double totalIncome = 0;
        double totalExpense = 0;

        for (Transaction t : transactions) {
            if (t.getType().equalsIgnoreCase("Income")) {
                totalIncome += t.getAmount();
            } else {
                totalExpense += t.getAmount();
            }
        }

        System.out.printf("Total Income: $%.2f\n", totalIncome);
        System.out.printf("Total Expense: $%.2f\n", totalExpense);
        System.out.printf("Balance: $%.2f\n", totalIncome - totalExpense);
    }

    private void saveTransactions() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (Transaction t : transactions) {
                writer.println(t.getType() + "," + t.getAmount() + "," + t.getCategory() + "," + t.toString().split(" \\| ")[0] + "," + t.toString().split(" \\| ")[1]);
            }
        } catch (IOException e) {
            System.out.println("Error saving transactions: " + e.getMessage());
        }
    }

    private void loadTransactions() {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    Transaction t = new Transaction(parts[4], Double.parseDouble(parts[1]), parts[0], parts[2]);
                    transactions.add(t);
                }
            }
        } catch (IOException e) {
            System.out.println("No previous transactions found.");
        }
    }
}

public class PersonalFinanceManager {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        FinanceManager manager = new FinanceManager();

        while (true) {
            System.out.println("\nPersonal Finance Manager");
            System.out.println("1. Add Transaction");
            System.out.println("2. View Transactions");
            System.out.println("3. View Summary");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("Enter description: ");
                    String desc = scanner.nextLine();
                    System.out.print("Enter amount: ");
                    double amount;
                    try {
                        amount = Double.parseDouble(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid amount. Try again.");
                        continue;
                    }
                    System.out.print("Enter type (Income/Expense): ");
                    String type = scanner.nextLine();
                    if (!type.equalsIgnoreCase("Income") && !type.equalsIgnoreCase("Expense")) {
                        System.out.println("Invalid type. Must be 'Income' or 'Expense'.");
                        continue;
                    }
                    System.out.print("Enter category (e.g., Food, Salary, Rent): ");
                    String category = scanner.nextLine();
                    manager.addTransaction(desc, amount, type, category);
                    System.out.println("Transaction added successfully!");
                    break;

                case 2:
                    manager.viewTransactions();
                    break;

                case 3:
                    manager.viewSummary();
                    break;

                case 4:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
}