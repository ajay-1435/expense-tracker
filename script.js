// ✅ Fetch and display all expenses
async function fetchExpenses() {
    try {
        const response = await fetch("http://localhost:8080/api/expenses");

        if (!response.ok) {
            throw new Error(`Server responded with ${response.status}`);
        }// ✅ Fetch and display all expenses
async function fetchExpenses() {
    try {
        const response = await fetch("http://localhost:8080/api/expenses");

        if (!response.ok) {
            throw new Error(`Server responded with ${response.status}`);
        }

        const expenses = await response.json();
        console.log("Fetched Expenses:", expenses);  // ✅ Debugging

        const list = document.getElementById("expenseList");
        list.innerHTML = "";  // ✅ Clear old list

        expenses.forEach(exp => {
            const item = document.createElement("li");
            item.textContent = `${exp.date} - ${exp.category}: ₹${exp.amount}`;
            list.appendChild(item);
        });

    } catch (error) {
        console.error("Error fetching expenses:", error);
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

    if (!category || isNaN(amount) || !date) {
        alert("Please enter valid expense details.");
        return;
    }

    const expense = { category, amount, date };

    try {
        const response = await fetch("http://localhost:8080/api/expenses", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(expense)
        });

        if (!response.ok) {
            throw new Error(`Server responded with ${response.status}`);
        }

        const result = await response.json();
        console.log("Expense Added Response:", result);  // ✅ Debugging

        // ✅ Immediately update UI
        const list = document.getElementById("expenseList");
        const item = document.createElement("li");
        item.textContent = `${result.date} - ${result.category}: ₹${result.amount}`;
        list.appendChild(item);

        document.getElementById("expenseForm").reset();  // ✅ Clear inputs

        fetchTotalExpense();  // ✅ Refresh total expenses
        fetchMonthlyExpenses();  // ✅ Refresh monthly breakdown

    } catch (error) {
        console.error("Error adding expense:", error);
        alert(`Failed to add expense. Server responded with: ${error.message}`);
    }
});

// ✅ Handle clearing all expenses
document.getElementById("clearExpenses").addEventListener("click", async function() {
    if (!confirm("Are you sure you want to delete all expenses? This action cannot be undone!")) return;

    try {
        const response = await fetch("http://localhost:8080/api/expenses/clear", {
            method: "DELETE",
            headers: { "Content-Type": "application/json" }
        });

        if (!response.ok) {
            throw new Error(`Server responded with ${response.status}`);
        }

        alert("All expenses cleared!");
        document.getElementById("expenseList").innerHTML = "";  // ✅ Clears expense list UI
        document.getElementById("totalExpense").textContent = "₹0.00";  // ✅ Resets total expenses

    } catch (error) {
        console.error("Error clearing expenses:", error);
        alert("Failed to clear expenses!");
    }
});

// ✅ Fetch and display total expenses
async function fetchTotalExpense() {
    try {
        const response = await fetch("http://localhost:8080/api/expenses/total");

        if (!response.ok) {
            throw new Error(`Server responded with ${response.status}`);
        }

        const totalExpense = await response.json();
        console.log("Total Expense Data:", totalExpense);  // ✅ Debugging

        document.getElementById("totalExpense").textContent = `₹${totalExpense.toFixed(2)}`;

    } catch (error) {
        console.error("Error fetching total expense:", error);
    }
}

// ✅ Fetch and display monthly expenses
async function fetchMonthlyExpenses() {
    try {
        const response = await fetch("http://localhost:8080/api/expenses/monthly");

        if (!response.ok) {
            throw new Error(`Server responded with ${response.status}`);
        }

        const monthlyExpenses = await response.json();
        console.log("Monthly Expenses Data:", monthlyExpenses);  // ✅ Debugging

        const container = document.getElementById("monthlyExpenses");
        container.innerHTML = "";  // ✅ Clear previous data

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

        const expenses = await response.json();
        console.log("Fetched Expenses:", expenses);  // ✅ Debugging

        const list = document.getElementById("expenseList");
        list.innerHTML = "";  // ✅ Clear old list

        expenses.forEach(exp => {
            const item = document.createElement("li");
            item.textContent = `${exp.date} - ${exp.category}: ₹${exp.amount}`;
            list.appendChild(item);
        });

    } catch (error) {
        console.error("Error fetching expenses:", error);
    }
}

// ✅ Ensure expenses are fetched when page loads
document.addEventListener("DOMContentLoaded", fetchExpenses);


// ✅ Show expenses grouped by category
document.getElementById("toggleCategoryView").addEventListener("click", showExpensesByCategory);

document.getElementById("clearExpenses").addEventListener("click", async function() {
    if (!confirm("Are you sure you want to delete all expenses? This action cannot be undone!")) return;

    try {
        const response = await fetch("http://localhost:8080/api/expenses/clear", { method: "DELETE" });

        if (!response.ok) {
            throw new Error(`Server responded with ${response.status}`);
        }

        alert("All expenses cleared!");
        document.getElementById("expenseList").innerHTML = "";  // ✅ Clears expense list UI
        document.getElementById("totalExpense").textContent = "₹0.00";  // ✅ Resets total expenses

    } catch (error) {
        console.error("Error clearing expenses:", error);
        alert("Failed to clear expenses!");
    }
});

async function showExpensesByCategory() {
    try {
        const response = await fetch("http://localhost:8080/api/expenses");

        if (!response.ok) {
            throw new Error(`Server responded with ${response.status}`);
        }

        const expenses = await response.json();
        console.log("Fetched Expenses for Category View:", expenses);  // ✅ Debugging

        const list = document.getElementById("expenseList");
        list.innerHTML = "";  // ✅ Clear list

        const groupedExpenses = {};

        expenses.forEach(exp => {
            if (!groupedExpenses[exp.category]) {
                groupedExpenses[exp.category] = [];
            }
            groupedExpenses[exp.category].push(exp);
        });

        Object.keys(groupedExpenses).forEach(category => {
            const categoryHeader = document.createElement("h3");
            categoryHeader.textContent = category;
            list.appendChild(categoryHeader);

            groupedExpenses[category].forEach(exp => {
                const item = document.createElement("li");
                item.textContent = `${exp.date}: ₹${exp.amount}`;
                list.appendChild(item);
            });
        });

    } catch (error) {
        console.error("Error fetching expenses by category:", error);
    }
}

// ✅ Fetch and display total expenses
async function fetchTotalExpense() {
    try {
        const response = await fetch("http://localhost:8080/api/expenses/total");

        if (!response.ok) {
            throw new Error(`Server responded with ${response.status}`);
        }

        const totalExpense = await response.json();
        console.log("Total Expense Data:", totalExpense);  // ✅ Debugging

        document.getElementById("totalExpense").textContent = `₹${totalExpense.toFixed(2)}`;

    } catch (error) {
        console.error("Error fetching total expense:", error);
    }
}

// ✅ Fetch and display monthly expenses
async function fetchMonthlyExpenses() {
    try {
        const response = await fetch("http://localhost:8080/api/expenses/monthly");

        if (!response.ok) {
            throw new Error(`Server responded with ${response.status}`);
        }

        const monthlyExpenses = await response.json();
        console.log("Monthly Expenses Data:", monthlyExpenses);  // ✅ Debugging

        const container = document.getElementById("monthlyExpenses");
        container.innerHTML = "";  // ✅ Clear previous data

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