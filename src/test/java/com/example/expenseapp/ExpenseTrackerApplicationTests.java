package com.example.expenseapp;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = ExpenseTrackerApplication.class)
public class ExpenseTrackerApplicationTests {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Test
    public void contextLoads() {
        assertThat(expenseRepository).isNotNull();  // ✅ Ensures repository loads
    }
}