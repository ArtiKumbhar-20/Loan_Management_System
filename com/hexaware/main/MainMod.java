package com.hexaware.main;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;
import com.hexaware.dao.LoanRepositoryImpl;
import com.hexaware.dto.Customer;
import com.hexaware.dto.Loan;
import com.hexaware.myexceptions.InvalidLoanException;
import com.hexaware.util.DBConnection;

public class MainMod {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Connection connection = DBConnection.getConnection(); 

        LoanRepositoryImpl loanRepository = new LoanRepositoryImpl(connection);

        while (true) {
            System.out.println("\nLoan Management System");
            System.out.println("----------------------");
            System.out.println("1. Apply Loan");
            System.out.println("2. Get All Loans");
            System.out.println("3. Get Loan by ID");
            System.out.println("4. Loan Repayment");
            System.out.println("5. Exit");
            System.out.println("Enter your choice:");

            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    // Apply Loan
                    try {
                        System.out.println("Do you want to apply for the loan? (Yes/No)");
                        String applyChoice = scanner.nextLine();
                        if (applyChoice.equalsIgnoreCase("yes")) {
                            Loan loan = new Loan();

                            System.out.println("Enter Customer ID:");
                            int customerId = scanner.nextInt();
                            scanner.nextLine(); 

                            System.out.println("Enter Principal Amount:");
                            double principalAmount = scanner.nextDouble();
                            scanner.nextLine(); 

                            System.out.println("Enter Interest Rate:");
                            double interestRate = scanner.nextDouble();
                            scanner.nextLine(); 
                            System.out.println("Enter Loan Term (in months):");
                            int loanTerm = scanner.nextInt();
                            scanner.nextLine(); 

                            System.out.println("Enter Loan Type (CarLoan/HomeLoan):");
                            String loanType = scanner.nextLine();

                            // Set loan details
                            loan.setCustomer(new Customer(customerId));
                            loan.setPrincipalAmount(principalAmount);
                            loan.setInterestRate(interestRate);
                            loan.setLoanTerm(loanTerm);
                            loan.setLoanType(loanType);
                            loan.setLoanStatus("Pending");

                            // Confirmation
                            System.out.println("Confirm loan application? (Yes/No)");
                            String confirmChoice = scanner.nextLine();
                            if (confirmChoice.equalsIgnoreCase("yes")) {
                                loanRepository.applyLoan(loan);
                                System.out.println("Loan application successful.");
                            } else {
                                System.out.println("Loan application cancelled.");
                            }
                        } else {
                            System.out.println("Loan application cancelled.");
                        }
                    } catch (SQLException | InvalidLoanException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case 2:
                    // Get All Loans
                    try {
                        loanRepository.getAllLoan();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    // Get Loan by ID
                    System.out.println("Enter Loan ID:");
                    int loanId = scanner.nextInt();
                    scanner.nextLine(); 
                    try {
                        loanRepository.getLoanById(loanId);
                    } catch (SQLException | InvalidLoanException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case 4:
                    // Loan Repayment
                    System.out.println("Enter Loan ID:");
                    int repaymentLoanId = scanner.nextInt();
                    scanner.nextLine(); 
                    System.out.println("Enter Repayment Amount:");
                    double repaymentAmount = scanner.nextDouble();
                    scanner.nextLine(); 
                    try {
                        loanRepository.loanRepayment(repaymentLoanId, repaymentAmount);
                    } catch (SQLException | InvalidLoanException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case 5:
                    // Exit
                    System.out.println("Exiting Loan Management System.");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
                    break;
            }
        }
    }
}
