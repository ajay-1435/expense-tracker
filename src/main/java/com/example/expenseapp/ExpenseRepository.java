package com.example.expenseapp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    // ✅ Calculate total expenses safely
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e")
    Double sumTotalExpenses();

    // ✅ Calculate monthly expenses grouped by month (Ensure date format correctness)
    @Query("SELECT FUNCTION('DATE_FORMAT', e.date, '%Y-%m') AS month, SUM(e.amount) FROM Expense e GROUP BY FUNCTION('DATE_FORMAT', e.date, '%Y-%m')")
    List<Object[]> sumExpensesByMonth();

    // ✅ Calculate category-wise expenses
    @Query("SELECT e.category, SUM(e.amount) FROM Expense e GROUP BY e.category")
    List<Object[]> sumExpensesByCategory();
}