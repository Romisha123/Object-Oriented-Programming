/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgets;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 *
 * @author Romisha Thapa
 */
public class TransactionEntryForm extends JFrame implements ActionListener {

    JTextField txtDate, txtDescription, txtAmount;
    ButtonGroup transactionType;
    JRadioButton rdoIncome, rdoExpense;
    JButton btnSubmit, btnReset;
    StackTransactions stackTransactions;
    PanelTransactionDetails panelTransactionDetails;

    TransacDealer transacDealer;
    String transactionID;

    //1 form for both adding transaction and editing transaction. Therefire commands are defined.
    int formCommand = 0;
    final static int DO_ADD = 1;
    final static int DO_EDIT = 2;

    TransactionEntryForm(StackTransactions stackTransactions, PanelTransactionDetails panelTransactions) {
        this.stackTransactions = stackTransactions;
        this.panelTransactionDetails = panelTransactions;

        setTitle("Add/Edit Transaction");
//        setDefaultCloseOperation(this.EXIT_ON_CLOSE);
//        setAlwaysOnTop(true);
        setSize(330, 240);
        setLayout(new GridLayout(6, 2));
        setBackground(Color.decode("#759de1"));

        txtDate = new JTextField(10);
        txtDate.setText(getCurrentDate());

        txtDescription = new JTextField(10);
        txtAmount = new JTextField(10);
        
        rdoIncome = new JRadioButton("Income");
        rdoIncome.setBackground(Color.decode("#759de1"));
        rdoExpense = new JRadioButton("Outcome");
        rdoExpense.setBackground(Color.decode("#759de1"));
        
        transactionType = new ButtonGroup();
        transactionType.add(rdoIncome);
        transactionType.add(rdoExpense);

        btnSubmit = new JButton("Submit");
        btnSubmit.setBackground(Color.decode("#466082"));
//        btnSubmit.setBorder(null);
        btnSubmit.setForeground(Color.white);
        
        btnReset = new JButton("Reset");
        btnReset.setBackground(Color.decode("#466082"));
//        btnSubmit.setBorder(null);
        btnReset.setForeground(Color.white);
        
        btnSubmit.addActionListener(this);
        btnReset.addActionListener(this);

        Container c = getContentPane();
        c.setBackground(Color.decode("#759de1"));
        c.add(new JLabel("Date (YYYY/MM/DD)"));
        c.add(txtDate);
        c.add(new JLabel("Description"));
        c.add(txtDescription);
        c.add(new JLabel("Amount"));
        c.add(txtAmount);
        c.add(new JLabel("Transaction Type"));
        c.add(rdoIncome);
        c.add(new JLabel(""));
        c.add(rdoExpense);
        c.add(btnSubmit);
        c.add(btnReset);

        setLocationRelativeTo(null);
    }

    static String getCurrentDate() {// Current date tanera form ma haldine so use does not have to type manually
        LocalDate localDate = LocalDate.now();
        return (DateTimeFormatter.ofPattern("yyyy/MM/dd").format(localDate));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton btnSource = (JButton) e.getSource();

        if (btnSource.equals(btnSubmit)) {
            String[] invalidChars = new String[]{"|", ";", "."};

            //Null validation
            if (txtDate.getText().equals(null) || txtDate.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Date cannot be blank!");
                txtDate.setText("");
                txtDate.requestFocus();
                return;
            }
            if (txtDescription.getText().equals(null) || txtDescription.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Description cannot be blank!");
                txtDescription.setText("");
                txtDescription.requestFocus();
                return;
            }
            if (txtAmount.getText().equals(null) || txtAmount.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Amount cannot be blank!");
                txtAmount.setText("");
                txtAmount.requestFocus();
                return;
            }

            //Invalid character validation
            for (int i = 0; i < invalidChars.length; i++) {
                if (txtDescription.getText().contains(invalidChars[i])) {
                    JOptionPane.showMessageDialog(null, "'|' & ',' & ';' are reserved characters. User name cannot contain them");
                    txtDescription.setText("");
                    txtAmount.requestFocus();
                    return;
                }

                //Data type validation
                if (!isNumeric(txtAmount.getText())) {
                    JOptionPane.showMessageDialog(null, "Amount must be numeric!");
                    txtAmount.setText("");
                    txtAmount.requestFocus();
                    return;
                }

                if (!isThisDateValid(txtDate.getText(), "yyyy/MM/dd")) {
                    JOptionPane.showMessageDialog(null, "Invalid date! Enter valid date.");
                    txtDate.setText("");
                    txtDate.requestFocus();
                    return;
                }
            }

            switch (formCommand) {
                case DO_ADD:
                    Double amount = Double.parseDouble(txtAmount.getText());
                    Transaction transactionRecord = new Transaction(panelTransactionDetails.main_Dashboard.loggedInUser,
                            txtDate.getText(), txtDescription.getText(), amount, getSelectedTransactionType());

                    stackTransactions.add(transactionRecord);
                    stackTransactions.populateTable(panelTransactionDetails);
                    transacDealer.inputToFile(panelTransactionDetails.main_Dashboard.loggedInUser, null, "write");
                    JOptionPane.showMessageDialog(null, "New transaction added successfully!");
                    panelTransactionDetails.updatePanelNetSummary();
                    break;
                case DO_EDIT:
                    stackTransactions.updateStack(transactionID, this);
                    transacDealer.inputToFile(null, panelTransactionDetails.toEditFilename, "update");
//                    stackTransactions.populateTable(panelTransactions, true);
                    transacDealer.outputFromFile(true);
                    panelTransactionDetails.updatePanelNetSummary();
                    JOptionPane.showMessageDialog(null, "Selected transaction updated successfully!");
                    break;
            }
        } else if (btnSource.equals(btnReset)) {
            txtDate.setText(getCurrentDate());
            txtDescription.setText("");
            txtAmount.setText("");
        }
    }

    String getSelectedTransactionType() { //Gets which radio button was selected in form
        for (Enumeration<AbstractButton> buttons = transactionType.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) {
                return button.getText();
            }
        }
        return null;
    }

    public void setFormCommand(int formCommand, TransacDealer myFileHandler, String transactionID) {
        this.transacDealer = myFileHandler;
        this.transactionID = transactionID;

        if (formCommand == DO_ADD) {
            this.formCommand = DO_ADD;
        } else if (formCommand == DO_EDIT) {
            this.formCommand = DO_EDIT;
        }
    }

    public boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public boolean isThisDateValid(String dateToValidate, String dateFromat) {

        if (dateToValidate == null) {
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
        sdf.setLenient(false);

        try {

            //if not valid, it will throw ParseException
            Date date = sdf.parse(dateToValidate);

        } catch (ParseException e) {

            return false;
        }

        return true;
    }
}
