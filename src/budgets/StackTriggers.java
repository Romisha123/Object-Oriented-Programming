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
public class StackTriggers extends LinkedList<Trigger> {

    void populateTable(PanelTriggerDetails panelTriggers) {

        JTable table = panelTriggers.getTableTrigger();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        String[] tuple = new String[6];

        for (Trigger eachTrigger : this) { //Loop through all indices/rooms in this stack
            tuple[0] = eachTrigger.getTriggerID(); //Using getters of each trigger to access their state/field.
            tuple[1] = eachTrigger.getLoggedInUser();
            tuple[2] = eachTrigger.getDate();
            tuple[3] = eachTrigger.getDescription();
            tuple[4] = Double.toString(eachTrigger.getAmount());
            tuple[5] = eachTrigger.getTriggerType();

            model.addRow(tuple);
            Arrays.fill(tuple, null); //Clearing all elements from tuple for next trigger data

        }
    }

    void updateStack(String triggerID, TriggerEntryForm frmTrigger) {
        for (Trigger eachTrigger : this) {//Loop through each triggers in stack
            if (eachTrigger.getTriggerID().equals(triggerID)) { //If triggerID matches, then edit that trigger
                eachTrigger.setDate(frmTrigger.txtDate.getText());
                eachTrigger.setDescription(frmTrigger.txtDescription.getText());
                eachTrigger.setAmount(Double.parseDouble(frmTrigger.txtAmount.getText()));//Convert string to double & update
                eachTrigger.setTriggerType(frmTrigger.getSelectedTiggerType());
            }
        }

    }

    void deleteFromStack(String triggerID) {
        ListIterator<Trigger> itr = this.listIterator();
        while (itr.hasNext()) {
            Trigger eachTrigger = itr.next();
            if (eachTrigger.getTriggerID().equals(triggerID)) {
//                itr.remove();
            }
        }
    }

}
