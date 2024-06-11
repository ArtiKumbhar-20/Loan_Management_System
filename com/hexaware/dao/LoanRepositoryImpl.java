package com.hexaware.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import com.hexaware.dto.Customer;
import com.hexaware.dto.Loan;
import com.hexaware.myexceptions.InvalidLoanException;

public class LoanRepositoryImpl implements ILoanRepository{
	private Connection myConnectionObject;

    public LoanRepositoryImpl(Connection myConnectionObject) {
        this.myConnectionObject = myConnectionObject;
    }
    
    @Override
    public boolean applyLoan(Loan loan) throws InvalidLoanException, SQLException {
        String userConfirmation = "Yes"; 

        if (userConfirmation.equalsIgnoreCase("Yes")) {
           
            PreparedStatement myStatement = myConnectionObject.prepareStatement(
                    "INSERT INTO Loan (customerId, principalAmount, interestRate, loanTerm, loanType, loanStatus) VALUES (?, ?, ?, ?, ?, ?)");
            myStatement.setInt(1, loan.getCustomer().getCustomerId());
            myStatement.setDouble(2, loan.getPrincipalAmount());
            myStatement.setDouble(3, loan.getInterestRate());
            myStatement.setInt(4, loan.getLoanTerm());
            myStatement.setString(5, loan.getLoanType());
            myStatement.setString(6, "Pending");

            boolean addStatus = !myStatement.execute();
            myStatement.close(); 

            return addStatus;
        } else {
            System.out.println("Loan application canceled.");
            return false;
        }
    }

    @Override
    public ArrayList<Loan> getAllLoan() throws SQLException {
        ArrayList<Loan> loanList = new ArrayList<>();
        PreparedStatement myStatement = this.myConnectionObject.prepareStatement("SELECT * FROM Loan L JOIN Customer C ON L.customerId = C.customerId");
        ResultSet myResult = myStatement.executeQuery();

        System.out.println("All Loans:");
        System.out.printf("%-10s | %-20s | %-30s | %-15s | %-20s | %-12s | %-12s | %-15s | %-10s\n",
                "Loan ID", "Customer Name", "Email Address", "Phone", "Address", "Credit Score", "Principal", "Interest", "Loan Type");
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

        while (myResult.next()) {
            int loanId = myResult.getInt("loanId");
            String customerName = myResult.getString("name");
            String emailAddress = myResult.getString("emailAddress");
            String phoneNumber = myResult.getString("phoneNumber");
            String address = myResult.getString("address");
            int creditScore = myResult.getInt("creditScore");
            double principalAmount = myResult.getDouble("principalAmount");
            double interestRate = myResult.getDouble("interestRate");
            String loanType = myResult.getString("loanType");
            String loanStatus = myResult.getString("loanStatus");

            System.out.printf("%-10d | %-20s | %-30s | %-15s | %-20s | %-12d | $%-10.2f | %-12.2f%% | %-15s | %-10s\n",
                    loanId, customerName, emailAddress, phoneNumber, address, creditScore, principalAmount, interestRate, loanType, loanStatus);

            // Create Customer object and set attributes
            Customer customer = new Customer();
            customer.setName(customerName);
            customer.setEmailAddress(emailAddress);
            customer.setPhoneNumber(phoneNumber);
            customer.setAddress(address);
            customer.setCreditScore(creditScore);

            // Create Loan object and set attributes
            Loan loan = new Loan();
            loan.setLoanId(loanId);
            loan.setCustomer(customer);
            loan.setPrincipalAmount(principalAmount);
            loan.setInterestRate(interestRate);
            loan.setLoanType(loanType);
            loan.setLoanStatus(loanStatus);

            loanList.add(loan);
        }

        myStatement.close();

        return loanList;
    }

    @Override
    public Loan getLoanById(int loanId) throws SQLException, InvalidLoanException {
        PreparedStatement myStatement = this.myConnectionObject.prepareStatement(
                "SELECT * FROM Loan L JOIN Customer C ON L.customerId = C.customerId WHERE loanId = ?");
        myStatement.setInt(1, loanId);
        ResultSet myResult = myStatement.executeQuery();

        if (myResult.next()) {
            int fetchedLoanId = myResult.getInt("loanId");
            int customerId = myResult.getInt("customerId");
            String customerName = myResult.getString("name");
            String emailAddress = myResult.getString("emailAddress");
            String phoneNumber = myResult.getString("phoneNumber");
            String address = myResult.getString("address");
            int creditScore = myResult.getInt("creditScore");
            double principalAmount = myResult.getDouble("principalAmount");
            double interestRate = myResult.getDouble("interestRate");
            int loanTerm = myResult.getInt("loanTerm");
            String loanType = myResult.getString("loanType");
            String loanStatus = myResult.getString("loanStatus");

            Customer customer = new Customer(customerId, customerName, emailAddress, phoneNumber, address, creditScore);
            Loan loan = new Loan(fetchedLoanId, customer, principalAmount, interestRate, loanTerm, loanType, loanStatus);

            System.out.println("Loan Details:");
            System.out.printf("%-10s | %-20s | %-30s | %-15s | %-20s | %-12s | %-12s | %-15s | %-10s | %-10s\n",
                    "Loan ID", "Customer Name", "Email Address", "Phone", "Address", "Credit Score", "Principal", "Interest", "Loan Type", "Status");
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("%-10d | %-20s | %-30s | %-15s | %-20s | %-12d | $%-10.2f | %-12.2f%% | %-15s | %-10s\n",
                    fetchedLoanId, customerName, emailAddress, phoneNumber, address, creditScore, principalAmount, interestRate, loanType, loanStatus);

            return loan;
        } else {
            throw new InvalidLoanException("Loan not found with ID: " + loanId);
        }
    }

    
    @Override
    public double calculateInterest(int loanId) throws SQLException, InvalidLoanException {
        PreparedStatement myStatement = this.myConnectionObject.prepareStatement(
                "SELECT principalAmount, interestRate, loanTerm FROM Loan WHERE loanId = ?");
        myStatement.setInt(1, loanId);
        ResultSet myResult = myStatement.executeQuery();

        if (myResult.next()) {
            double principalAmount = myResult.getDouble("principalAmount");
            double interestRate = myResult.getDouble("interestRate");
            int loanTerm = myResult.getInt("loanTerm");

            double interest = (principalAmount * interestRate * loanTerm) / 12.0;
            return interest;
        } else {
            throw new InvalidLoanException("Loan not found with ID: " + loanId);
        }
    }
    
    
    @Override
    public boolean loanStatus(int loanId) throws SQLException, InvalidLoanException {
        Loan loan = getLoanById(loanId);
        if (loan == null) {
            throw new InvalidLoanException("Loan with ID " + loanId + " not found.");
        }

        // Check the credit score of the customer associated with the loan
        int creditScore = loan.getCustomer().getCreditScore();
        String newStatus = (creditScore > 650) ? "Approved" : "Rejected";

        PreparedStatement myStatement = myConnectionObject.prepareStatement("UPDATE Loan SET loanStatus = ? WHERE loanId = ?");
        myStatement.setString(1, newStatus);
        myStatement.setInt(2, loanId);
        boolean updateStatus = !myStatement.execute();

        System.out.println("Loan ID: " + loanId + " has been " + newStatus.toLowerCase() + ".");
        return updateStatus;
    }

    @Override
    public double calculateEMI(int loanId) throws SQLException, InvalidLoanException {
        PreparedStatement myStatement = this.myConnectionObject.prepareStatement(
                "SELECT principalAmount, interestRate, loanTerm FROM Loan WHERE loanId = ?");
        myStatement.setInt(1, loanId);
        ResultSet myResult = myStatement.executeQuery();

        if (myResult.next()) {
            double principalAmount = myResult.getDouble("principalAmount");
            double annualInterestRate = myResult.getDouble("interestRate");
            int loanTerm = myResult.getInt("loanTerm");

            return calculateEMI(principalAmount, annualInterestRate, loanTerm);
        } else {
            throw new InvalidLoanException("Loan not found with ID: " + loanId);
        }
    }

    public double calculateEMI(double principalAmount, double annualInterestRate, int loanTerm) {
        double monthlyInterestRate = annualInterestRate / 12 / 100;
        return (principalAmount * monthlyInterestRate * Math.pow(1 + monthlyInterestRate, loanTerm)) / 
               (Math.pow(1 + monthlyInterestRate, loanTerm) - 1);
    }

    @Override
    public void loanRepayment(int loanId, double amount) throws SQLException, InvalidLoanException {
        Loan loan = getLoanById(loanId);
        double emiAmount = calculateEMI(loanId);

        if (amount < emiAmount) {
            System.out.println("Repayment amount is less than the EMI amount. Payment rejected.");
            return;
        }

        int numberOfEmis = (int)(amount / emiAmount);
        double totalRepayment = numberOfEmis * emiAmount;
        double remainingPrincipal = loan.getPrincipalAmount() - totalRepayment;

        if (remainingPrincipal < 0) {
            remainingPrincipal = 0;
        }

        PreparedStatement myStatement = this.myConnectionObject.prepareStatement(
                "UPDATE Loan SET principalAmount = ? WHERE loanId = ?");
        myStatement.setDouble(1, remainingPrincipal);
        myStatement.setInt(2, loanId);
        boolean updateStatus = !myStatement.execute();

        if (updateStatus) {
            System.out.println("Loan repayment successful.");
        } else {
            System.out.println("Loan repayment update failed.");
        }
    }
}
