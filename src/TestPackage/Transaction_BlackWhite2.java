/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TestPackage;

import budgets.Transaction;

/**
 *
 * @author Romisha Thapa
 */
public class Transaction_BlackWhite2 {

    public static void main(String[] args) {
        Transaction tr = new Transaction("Romisha20170110-153607-421", "Romisha",
                "2016/05/10", "Salary", 66000.0, "Income", false);
        System.out.println("ID: " + tr.getTransactionID());
        System.out.println("Username: " + tr.getLoggedInUser());
        System.out.println("Date: " + tr.getDate());
        System.out.println("Description: " + tr.getDescription());
        System.out.println("Amount: " + tr.getAmount());
    }
}




