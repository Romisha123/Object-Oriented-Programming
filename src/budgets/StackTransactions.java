/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgets;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.ListIterator;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Romisha Thapa
 */
public class StackTransactions extends LinkedList<Transaction> {

    public void populateTable(PanelTransactionDetails panelTransactions) {

        JTable table = panelTransactions.getTableTransaction();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        String[] tuple = new String[6];

        for (Transaction eachTransaction : this) { //Loop through all indices/rooms in this stack
            tuple[0] = eachTransaction.getTransactionID(); //Using getters of each transaction to access their state/field.
            tuple[1] = eachTransaction.getLoggedInUser();
            tuple[2] = eachTransaction.getDate();
            tuple[3] = eachTransaction.getDescription();
            tuple[4] = Double.toString(eachTransaction.getAmount());
            tuple[5] = eachTransaction.getTransactionType();

            model.addRow(tuple);
            Arrays.fill(tuple, null); //Clearing all elements from tuple for next transaction data

        }
    }

    public void updateStack(String transactionID, TransactionEntryForm frmTransaction) {
        for (Transaction eachTransaction : this) {//Loop through each transactions in stack
            if (eachTransaction.getTransactionID().equals(transactionID)) { //If transactionID matches, then edit that transaction
                eachTransaction.setDate(frmTransaction.txtDate.getText());
                eachTransaction.setDescription(frmTransaction.txtDescription.getText());
                eachTransaction.setAmount(Double.parseDouble(frmTransaction.txtAmount.getText()));//Convert string to double & update
                eachTransaction.setTransactionType(frmTransaction.getSelectedTransactionType());
            }
        }

    }

    public void deleteFromStack(String transactionID) {
        ListIterator<Transaction> itr = this.listIterator();
        while (itr.hasNext()) {
            Transaction eachTransaction = itr.next();
            if (eachTransaction.getTransactionID().equals(transactionID)) {
                itr.remove();
            }
        }
    }

}
