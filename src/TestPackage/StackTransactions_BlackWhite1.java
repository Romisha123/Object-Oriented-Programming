/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TestPackage;

import budgets.Main_Dashboard;
import budgets.PanelTransactionDetails;
import budgets.StackTransactions;
import budgets.Transaction;

/**
 *
 * @author Romisha Thapa
 */
public class StackTransactions_BlackWhite1 {

    public static void main(String[] args) {
        StackTransactions st = new StackTransactions();
        Transaction t1 = new Transaction("PaulCopan", "2016/01/01", "Salary", 6000.0, "Income");
        st.add(t1);
    }
}
