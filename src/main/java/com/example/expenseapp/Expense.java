package com.example.expenseapp;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "expenses")  // ✅ Defines table name in MySQL
public class Expense implements Serializable { // ✅ Allows serialization

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // ✅ Auto-generates ID
    private Long id;

    @Column(nullable = false) // ✅ Ensures category is not null in DB
    private String category;

    @Column(nullable = false) // ✅ Ensures amount is stored properly
    private double amount;

    @Column(nullable = false) // ✅ Ensures date is stored properly
    private LocalDate date;

    // ✅ Default constructor (required by JPA)
    public Expense() {}

    // ✅ Constructor with fields
    public Expense(String category, double amount, LocalDate date) {
        this.category = category;
        this.amount = amount;
        this.date = date;
    }

    // ✅ Getter & Setter methods
    public Long getId() { return id; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    // ✅ Convert Expense to CSV format for file saving
    public String toCSV() {
        return category + "," + String.format("%.2f", amount) + "," + date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    // ✅ Override toString() for better readability
    @Override
    public String toString() {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " | " + category + " | ₹" + String.format("%.2f", amount);
    }
}