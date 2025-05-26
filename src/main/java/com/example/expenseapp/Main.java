package com.example.expenseapp; // Updated package name

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

class ExpenseTracker {
    private ArrayList<Expense> expenses = new ArrayList<>();
    private static final String FILE_PATH = "src/main/resources/data.csv";

    public void loadExpensesFromCSV() {
        if (!Files.exists(Paths.get(FILE_PATH))) {
            System.out.println("No previous expenses found.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String category = parts[0];
                    double amount = Double.parseDouble(parts[1]);
                    LocalDate date = LocalDate.parse(parts[2], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    expenses.add(new Expense(category, amount, date));
                }
            }
            System.out.println("✅ Loaded previous expenses from CSV!");
        } catch (Exception e) {
            System.out.println("Error loading expenses: " + e.getMessage());
        }
    }

    public void addExpense(String category, double amount, LocalDate date) {
        Expense exp = new Expense(category, amount, date);
        expenses.add(exp);
        saveToCSV(exp);
    }

    public void showExpenses() {
        System.out.println("\nExpense List:");
        for (Expense exp : expenses) {
            System.out.println(exp);
        }
    }

    private void saveToCSV(Expense exp) {
        try (FileWriter writer = new FileWriter(FILE_PATH, true)) {
            writer.write(exp.toCSV() + "\n");
        } catch (IOException e) {
            System.out.println("Error saving data to CSV: " + e.getMessage());
        }
    }

    public void showTotalSpending() {
        double total = 0.0;
        for (Expense exp : expenses) {
            total += exp.getAmount();
        }
        System.out.println("\nTotal Expenses: ₹" + String.format("%.2f", total));
    }

    public void showSpendingByCategory() {
        HashMap<String, Double> categoryTotals = new HashMap<>();
        for (Expense exp : expenses) {
            categoryTotals.put(exp.getCategory(), categoryTotals.getOrDefault(exp.getCategory(), 0.0) + exp.getAmount());
        }

        System.out.println("\nExpense Breakdown by Category:");
        for (String category : categoryTotals.keySet()) {
            System.out.println(category + ": ₹" + String.format("%.2f", categoryTotals.get(category)));
        }
    }

    public void showSpendingByMonth() {
        HashMap<String, Double> monthTotals = new HashMap<>();
        for (Expense exp : expenses) {
            String monthYear = exp.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM"));
            monthTotals.put(monthYear, monthTotals.getOrDefault(monthYear, 0.0) + exp.getAmount());
        }

        System.out.println("\nMonthly Expense Summary:");
        for (String month : monthTotals.keySet()) {
            System.out.println(month + ": ₹" + String.format("%.2f", monthTotals.get(month)));
        }
    }
}

public class Main {
    public static void main(String[] args) {
        ExpenseTracker tracker = new ExpenseTracker();
        tracker.loadExpensesFromCSV(); // ✅ Load saved expenses before entering the menu
        
        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        while (true) {
            System.out.println("\n1. Add Expenses\n2. Show Expenses\n3. Show Total Spending\n4. Show Spending by Category\n5. Show Spending by Month\n6. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                System.out.println("How many expenses would you like to add?");
                int count = scanner.nextInt();
                scanner.nextLine();

                for (int i = 0; i < count; i++) {
                    System.out.println("Expense " + (i + 1) + ":");
                    System.out.print("Enter category (Food/Travel/etc.): ");
                    String category = scanner.nextLine();
                    System.out.print("Enter amount: ");
                    double amount = scanner.nextDouble();
                    scanner.nextLine();

                    LocalDate date = null;
                    while (date == null) {
                        System.out.print("Enter date (YYYY-MM-DD): ");
                        String dateInput = scanner.nextLine();
                        try {
                            date = LocalDate.parse(dateInput, formatter);
                        } catch (DateTimeParseException e) {
                            System.out.println("Invalid date format! Please enter a valid date (YYYY-MM-DD).");
                        }
                    }

                    tracker.addExpense(category, amount, date);
                    System.out.println("Expense added and saved to CSV!\n");
                }
            } else if (choice == 2) {
                tracker.showExpenses();
            } else if (choice == 3) {
                tracker.showTotalSpending();
            } else if (choice == 4) {
                tracker.showSpendingByCategory();
            } else if (choice == 5) {
                tracker.showSpendingByMonth();
            } else {
                System.out.println("Exiting...");
                break;
            }
        }
        scanner.close();
    }
}