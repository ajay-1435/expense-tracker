package com.example.expenseapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.io.File;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin(origins = "*")  // ✅ Allow frontend requests
public class ExpenseController {

    @Autowired
    private ExpenseRepository repo;

    // ✅ Get all expenses
    @GetMapping
    public ResponseEntity<List<Expense>> getAll() {
        List<Expense> expenses = repo.findAll();
        return ResponseEntity.ok(expenses);
    }

    // ✅ Get total expenses
    @GetMapping("/total")
    public ResponseEntity<Double> getTotalExpenses() {
        Double total = repo.sumTotalExpenses();
        System.out.println("Total Expenses Retrieved: " + total);
        return ResponseEntity.ok(total);
    }

    // ✅ Get expenses grouped by month
    @GetMapping("/monthly")
    public ResponseEntity<Map<String, Double>> getMonthlyExpenses() {
        List<Object[]> results = repo.sumExpensesByMonth();

        Map<String, Double> monthlyExpenses = new HashMap<>();
        for (Object[] row : results) {
            monthlyExpenses.put(String.valueOf(row[0]), ((Number) row[1]).doubleValue());
        }

        System.out.println("Monthly Expenses Retrieved: " + monthlyExpenses);
        return ResponseEntity.ok(monthlyExpenses);
    }

    // ✅ Get category-wise expenses
    @GetMapping("/category")
    public ResponseEntity<Map<String, Double>> getExpensesByCategory() {
        List<Object[]> results = repo.sumExpensesByCategory();

        Map<String, Double> categoryExpenses = new HashMap<>();
        for (Object[] row : results) {
            categoryExpenses.put(String.valueOf(row[0]), ((Number) row[1]).doubleValue());
        }

        System.out.println("Category-wise Expenses Retrieved: " + categoryExpenses);
        return ResponseEntity.ok(categoryExpenses);
    }

    // ✅ Clear all expenses & reset CSV
    @DeleteMapping("/clear")
    public ResponseEntity<String> clearExpenses() {
        try {
            repo.deleteAll();  // ✅ Clears all records from the database

            try (FileWriter writer = new FileWriter("expenses.csv")) {
                writer.write("Date,Category,Amount\n");  // ✅ Resets CSV file
                writer.flush();
                System.out.println("All expenses cleared!");

            } catch (IOException e) {
                System.err.println("Failed to reset CSV file: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to clear expenses.");
            }

            return ResponseEntity.ok("All expenses cleared successfully.");

        } catch (Exception e) {
            System.err.println("Error clearing expenses: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error clearing expenses.");
        }
    }

    // ✅ Add a new expense & save to CSV
    @PostMapping
    public ResponseEntity<Expense> addExpense(@RequestBody Expense expense) {
        try {
            Expense savedExpense = repo.save(expense);

            try (FileWriter writer = new FileWriter("expenses.csv", true)) {
                File file = new File("expenses.csv");

                // ✅ If file is empty, add column headers
                if (file.length() == 0) {
                    writer.append("Date,Category,Amount\n");
                }

                writer.append(String.format("%s,%s,%.2f\n",
                        savedExpense.getDate(), savedExpense.getCategory(), savedExpense.getAmount()));
                writer.flush();

            } catch (IOException e) {
                System.err.println("Failed to write to CSV file: " + e.getMessage());
            }

            return ResponseEntity.ok(savedExpense);

        } catch (Exception e) {
            System.err.println("Error saving expense: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}