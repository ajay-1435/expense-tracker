// ✅ Use your actual Render backend URL
const BASE_URL = "https://expense-tracker-1-r83a.onrender.com/api/expenses";

// ✅ Fetch and display all expenses
async function fetchExpenses() {
    try {
        const response = await fetch(`${BASE_URL}`);
        if (!response.ok) throw new Error(`Server Error: ${response.status}`);

        const expenses = await response.json();
        console.log("Fetched Expenses:", expenses);

        const list = document.getElementById("expenseList");
        list.innerHTML = ""; // ✅ Clear old list

        expenses.forEach(exp => {
            const item = document.createElement("li");
            item.textContent = `${exp.date} - ${exp.category}: ₹${exp.amount}`;
            list.appendChild(item);
        });

    } catch (error) {
        console.error("Error fetching expenses:", error);
        alert(`Failed to fetch expenses: ${error.message}`);
    }
}

// ✅ Ensure expenses are fetched when page loads
document.addEventListener("DOMContentLoaded", fetchExpenses);

// ✅ Handle expense submission
document.getElementById("expenseForm").addEventListener("submit", async function(event) {
    event.preventDefault();

    const category = document.getElementById("category").value.trim();
    const amount = parseFloat(document.getElementById("amount").value);
    const date = document.getElementById("date").value;

    if (!category || isNaN(amount) || amount <= 0 || !date) {
        alert("Please enter valid expense details.");
        return;
    }

    const expense = { category, amount, date };

    try {
        const response = await fetch(`${BASE_URL}`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(expense)
        });

        if (!response.ok) throw new Error(`Server Error: ${response.status}`);

        const result = await response.json();
        console.log("Expense Added:", result);

        // ✅ Immediately update UI
        const list = document.getElementById("expenseList");
        const item = document.createElement("li");
        item.textContent = `${result.date} - ${result.category}: ₹${result.amount}`;
        list.appendChild(item);

        document.getElementById("expenseForm").reset(); // ✅ Clear inputs

        fetchTotalExpense();  // ✅ Refresh total expenses
        fetchMonthlyExpenses();  // ✅ Refresh monthly breakdown

    } catch (error) {
        console.error("Error adding expense:", error);
        alert(`Failed to add expense: ${error.message}`);
    }
});

// ✅ Fetch and display total expenses
async function fetchTotalExpense() {
    try {
        const response = await fetch(`${BASE_URL}/total`);
        if (!response.ok) throw new Error(`Server Error: ${response.status}`);

        const totalExpense = await response.json();
        console.log("Total Expense Data:", totalExpense);

        document.getElementById("totalExpense").textContent = `₹${totalExpense.toFixed(2)}`;

    } catch (error) {
        console.error("Error fetching total expense:", error);
    }
}

// ✅ Fetch and display monthly expenses
async function fetchMonthlyExpenses() {
    try {
        const response = await fetch(`${BASE_URL}/monthly`);
        if (!response.ok) throw new Error(`Server Error: ${response.status}`);

        const monthlyExpenses = await response.json();
        console.log("Monthly Expenses:", monthlyExpenses);

        const container = document.getElementById("monthlyExpenses");
        container.innerHTML = ""; // ✅ Clear previous data

        Object.keys(monthlyExpenses).forEach(month => {
            const monthData = document.createElement("p");
            monthData.textContent = `${month}: ₹${monthlyExpenses[month].toFixed(2)}`;
            container.appendChild(monthData);
        });

    } catch (error) {
        console.error("Error fetching monthly expenses:", error);
    }
}

// ✅ Ensure functions run when the page loads
document.addEventListener("DOMContentLoaded", () => {
    fetchTotalExpense();
    fetchMonthlyExpenses();
});

// ✅ Show monthly expenses on button click
document.getElementById("toggleMonthlyView").addEventListener("click", fetchMonthlyExpenses);