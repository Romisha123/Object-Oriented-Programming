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
public class PanelTriggerDetails extends JPanel implements ActionListener {

    JButton btnNew, btnEdit, btnDelete, btnTrigger;
    JTable tableTrigger;
    TableModel tableModel;
    JScrollPane scrollPane;
    StackTriggers stackTriggers;
    TrigDealer trigDealer;
    TriggerEntryForm triggerEntryForm;
    Main_Dashboard main_Dashboard; //TO access net summary panel
    boolean isFormOpen;
    String toEditFilename;
    
    Font font;

    PanelTriggerDetails(Main_Dashboard mainGUI) { // Accepts MainGUI because we want the form to send 
        //the command to MainGUI's panelNetSummary that it needs to update.                                                  
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
        stackTriggers = new StackTriggers();
        trigDealer = new TrigDealer(stackTriggers, this);

        //Blank table instantiation
        String[] columnHeadings = {"TRIGGER ID", "USER", "DATE", "DESCRIPTION", "AMOUNT", "TRIGGER TYPE"};
        tableModel = new DefaultTableModel(columnHeadings, 0);
        tableTrigger = new JTable(tableModel) {//Creating table object & making table anonymous class to make cell editable false.
            @Override
            public boolean isCellEditable(int data, int column) {
                return false;
            }
        };

        //Inserting vertical scrollbar to table
        tableTrigger.setAutoCreateRowSorter(true);
        tableTrigger.setPreferredScrollableViewportSize(new Dimension(890, 400));
        tableTrigger.setFillsViewportHeight(true);
        scrollPane = new JScrollPane(tableTrigger);

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
        triggerEntryForm = new TriggerEntryForm(stackTriggers, this);
        isFormOpen = false;

        //Anonymous class to override close operation. When form is closed it is hidden. 
        triggerEntryForm.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                triggerEntryForm.setVisible(false);
                triggerEntryForm.dispose();
                isFormOpen = false;
            }
        });

        //Load file's data & load to table
        trigDealer.getFromFile(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton btnSource = (JButton) e.getSource();

        if (btnSource.equals(btnNew) & ((isFormOpen == false))) {
            triggerEntryForm.btnReset.doClick(); //Reset all fields
            triggerEntryForm.setVisible(true);
            isFormOpen = true;
            triggerEntryForm.setFormCommand(TriggerEntryForm.DO_ADD, trigDealer, null);
        } else if (btnSource.equals(btnEdit) & ((isFormOpen == false))) {
            int selectedRowNo = tableTrigger.getSelectedRow();
            if (selectedRowNo != -1) {//If row is selected (selected row no. is greater than -1)
                triggerEntryForm.setVisible(true);
                isFormOpen = true;

                //Loading form with selected table data so that it can be edited
                toEditFilename = tableTrigger.getValueAt(selectedRowNo, 1).toString();
                String triggerID = tableTrigger.getValueAt(selectedRowNo, 0).toString();
                triggerEntryForm.txtDate.setText(tableTrigger.getValueAt(selectedRowNo, 2).toString());
//Bringing date in form, from selected table row
                triggerEntryForm.txtDescription.setText(tableTrigger.getValueAt(selectedRowNo, 3).toString());
//Bringing description in form, from selected table row
                triggerEntryForm.txtAmount.setText(tableTrigger.getValueAt(selectedRowNo, 4).toString());
//Bringing amount in form, from selected table row

                ////Bringing trigger type in form, from selected table row
                String triggerType = tableTrigger.getValueAt(selectedRowNo, 5).toString();
                if (triggerType.equals("Income")) {
                    triggerEntryForm.rdoIncome.doClick();
                } else if (triggerType.equals("Outcome")) {
                    triggerEntryForm.rdoExpense.doClick();
                }
                triggerEntryForm.setFormCommand(TriggerEntryForm.DO_EDIT, trigDealer, triggerID);
            } else if (selectedRowNo == -1) {//If row is not selected (selected row no. is equal to -1)
                JOptionPane.showMessageDialog(null, "Select a Row to edit!");
            }
        } else if (btnSource.equals(btnDelete) & ((isFormOpen == false))) {
            int selectedRowNo = tableTrigger.getSelectedRow();

            if (selectedRowNo != -1) {//If row is selected (selected row no. is greater than -1)
                stackTriggers.deleteFromStack(tableTrigger.getValueAt(selectedRowNo, 0).toString());
                trigDealer.addToFile(main_Dashboard.loggedInUser, null, "write");
                JOptionPane.showMessageDialog(null, "Trigger deleted!");
                ((DefaultTableModel) tableTrigger.getModel()).removeRow(selectedRowNo);
                updatePanelNetSummary();
            } else if (selectedRowNo == -1) {//If row is not selected (selected row no. is equal to -1)
                JOptionPane.showMessageDialog(null, "Select a Row to delete!");
            }
        }
    }

    public JTable getTableTrigger() {
        return tableTrigger;
    }

    public void updatePanelNetSummary() {
        main_Dashboard.netSummaryBudget.setLblTotalAmountPersonal("Income");
        main_Dashboard.netSummaryBudget.setLblTotalAmountPersonal("Outcome");
        main_Dashboard.netSummaryBudget.setLblNetAmount();
    }
}
