package com.example.webhooksqlsolver.service;

import org.springframework.stereotype.Service;

@Service
public class SqlProblemSolver {

    public String solveProblem(String regNo) {
        // Extract last two digits from registration number
        String lastTwoDigits = regNo.substring(regNo.length() - 2);
        int lastTwoDigitsInt = Integer.parseInt(lastTwoDigits);
        
        if (lastTwoDigitsInt % 2 == 1) {
            // Odd - Question 1
            return solveQuestion1();
        } else {
            // Even - Question 2
            return solveQuestion2();
        }
    }

    private String solveQuestion1() {
        // SQL solution for Question 1 (Odd registration numbers)
        return "SELECT p.AMOUNT AS SALARY, " +
               "CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME, " +
               "TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE, " +
               "d.DEPARTMENT_NAME " +
               "FROM PAYMENTS p " +
               "JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID " +
               "JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID " +
               "WHERE DAY(p.PAYMENT_TIME) <> 1 " +
               "ORDER BY p.AMOUNT DESC LIMIT 1;";
    }

    private String solveQuestion2() {
        // SQL solution for Question 2 (Even registration numbers) - Placeholder
        return "SELECT COUNT(*) as total_orders, customer_id " +
               "FROM orders " +
               "GROUP BY customer_id " +
               "HAVING COUNT(*) > 5;";
    }
}
