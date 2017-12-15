/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Romisha Thapa
 */
public class PanelTransactionDetails extends JPanel implements ActionListener {

    JButton btnNew, btnEdit, btnDelete, btnTrigger;
    JTable tableTransaction;
    TableModel tableModel;
    JScrollPane scrollPane;
    StackTransactions stackTransactions;
    TransacDealer transacDealer;
    TransactionEntryForm transactionEntryForm;
    Main_Dashboard main_Dashboard; //TO access net summary panel
    boolean isFormOpen;
    String toEditFilename;
    Font font;

    public PanelTransactionDetails(Main_Dashboard mainGUI) { // Accepts MainGUI because we want the form to 
        //send the command to MainGUI's panelNetSummary that it needs to update.
        this.main_Dashboard = mainGUI;
        setLayout(new FlowLayout());
        setBackground(Color.decode("#759de1"));
        
        font = new Font("Lucida Fax", Font.PLAIN, 17);

        btnNew = new JButton("New");
        btnNew.setBackground(Color.decode("#466082"));
        btnNew.setFont(font);
        btnNew.setForeground(Color.white);
        
        btnEdit = new JButton("Edit");
        btnEdit.setBackground(Color.decode("#466082"));
        btnEdit.setFont(font);
        btnEdit.setForeground(Color.white);
        
        btnDelete = new JButton("Delete");                                                                                                   
        btnDelete.setBackground(Color.decode("#466082"));
        btnDelete.setFont(font);
        btnDelete.setForeground(Color.white);
        
        btnTrigger = new JButton("New Trigger");
        stackTransactions = new StackTransactions();
        transacDealer = new TransacDealer(stackTransactions, this);

        //Blank table instantiation
        String[] columnHeadings = {"TRANSACTION ID", "USER", "DATE", "DESCRIPTION", "AMOUNT", "TRANSACTION TYPE"};
        tableModel = new DefaultTableModel(columnHeadings, 0);
        tableTransaction = new JTable(tableModel) {//Creating table object & making table anonymous class to make cell editable false.
            @Override
            public boolean isCellEditable(int data, int column) {
                return false;
            }
        };

        //Inserting vertical scrollbar to table
        tableTransaction.setAutoCreateRowSorter(true);
        tableTransaction.setPreferredScrollableViewportSize(new Dimension(890, 400));
        tableTransaction.setFillsViewportHeight(true);
        scrollPane = new JScrollPane(tableTransaction);

        btnNew.addActionListener(this);
        btnEdit.addActionListener(this);
        btnDelete.addActionListener(this);
        btnTrigger.addActionListener(this);

        add(btnNew);
        add(btnEdit);
        add(btnDelete);
        add(scrollPane);
        setVisible(true);

        //Make form's object but dont open it yet
        transactionEntryForm = new TransactionEntryForm(stackTransactions, this);
        isFormOpen = false;

        //Anonymous class to override close operation. When form is closed it is hidden. 
        transactionEntryForm.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                transactionEntryForm.setVisible(false);
                transactionEntryForm.dispose();
                isFormOpen = false;
            }
        });

        //Load file's data & load to table
        transacDealer.outputFromFile(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton btnSource = (JButton) e.getSource();

        if (btnSource.equals(btnNew) & ((isFormOpen == false))) {
            transactionEntryForm.btnReset.doClick(); //Reset all fields
            transactionEntryForm.setVisible(true);
            isFormOpen = true;
            transactionEntryForm.setFormCommand(TransactionEntryForm.DO_ADD, transacDealer, null);
        } else if (btnSource.equals(btnEdit) & ((isFormOpen == false))) {
            int selectedRowNo = tableTransaction.getSelectedRow();
            if (selectedRowNo != -1) {//If row is selected (selected row no. is greater than -1)
                transactionEntryForm.setVisible(true);
                isFormOpen = true;

                //Loading form with selected table data so that it can be edited
                toEditFilename = tableTransaction.getValueAt(selectedRowNo, 1).toString();
                String transactionID = tableTransaction.getValueAt(selectedRowNo, 0).toString();
                transactionEntryForm.txtDate.setText(tableTransaction.getValueAt(selectedRowNo, 2).toString());
//Bringing date in form, from selected table row
                transactionEntryForm.txtDescription.setText(tableTransaction.getValueAt(selectedRowNo, 3).toString());
//Bringing description in form, from selected table row
                transactionEntryForm.txtAmount.setText(tableTransaction.getValueAt(selectedRowNo, 4).toString());
//Bringing amount in form, from selected table row

                ////Bringing transaction type in form, from selected table row
                String transactionType = tableTransaction.getValueAt(selectedRowNo, 5).toString();
                if (transactionType.equals("Income")) {
                    transactionEntryForm.rdoIncome.doClick();
                } else if (transactionType.equals("Outcome")) {
                    transactionEntryForm.rdoExpense.doClick();
                }
                transactionEntryForm.setFormCommand(TransactionEntryForm.DO_EDIT, transacDealer, transactionID);
            } else if (selectedRowNo == -1) {//If row is not selected (selected row no. is equal to -1)
                JOptionPane.showMessageDialog(null, "Select a Row to edit!");
            }
        } else if (btnSource.equals(btnDelete) & ((isFormOpen == false))) {
            int selectedRowNo = tableTransaction.getSelectedRow();

            if (selectedRowNo != -1) {//If row is selected (selected row no. is greater than -1)
                stackTransactions.deleteFromStack(tableTransaction.getValueAt(selectedRowNo, 0).toString());
                transacDealer.inputToFile(main_Dashboard.loggedInUser, null, "write");
                updatePanelNetSummary();
                ((DefaultTableModel) tableTransaction.getModel()).removeRow(selectedRowNo);
                JOptionPane.showMessageDialog(null, "Transaction deleted!");
            } else if (selectedRowNo == -1) {//If row is not selected (selected row no. is equal to -1)
                JOptionPane.showMessageDialog(null, "Select a Row to delete!");
            }
        }
    }

    public JTable getTableTransaction() {
        return tableTransaction;
    }

    public void updatePanelNetSummary() {
        main_Dashboard.netSummaryBudget.setLblTotalAmountPersonal("Income");
        main_Dashboard.netSummaryBudget.setLblTotalAmountPersonal("Outcome");
        main_Dashboard.netSummaryBudget.setLblNetAmount();
    }
}
