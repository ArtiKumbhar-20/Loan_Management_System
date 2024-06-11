package com.hexaware.dao;

import java.sql.SQLException;
import java.util.ArrayList;

import com.hexaware.dto.Loan;
import com.hexaware.myexceptions.InvalidLoanException;

public interface ILoanRepository {

	boolean applyLoan(Loan loan) throws InvalidLoanException, SQLException, SQLException;

	ArrayList<Loan> getAllLoan() throws SQLException;

	Loan getLoanById(int loanId) throws SQLException, InvalidLoanException;

	double calculateInterest(int loanId) throws SQLException, InvalidLoanException;

	boolean loanStatus(int loanId) throws SQLException, InvalidLoanException;

	double calculateEMI(int loanId) throws SQLException, InvalidLoanException;

	void loanRepayment(int loanId, double amount) throws SQLException, InvalidLoanException;
	
}
