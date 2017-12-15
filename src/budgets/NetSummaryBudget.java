/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgets;

import java.awt.Color;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Romisha Thapa
 */
public class NetSummaryBudget extends JPanel {

    double[] aggrNet;

    JLabel blank, lblHomeAggregated, lblPersonal; //Grid's top horizontal column headings
    JLabel lblTotalIncome, lblTotalOutcome, lblNetIncome; //Grid's left vertical column headings

    JLabel lblTotalIncomePersonal, lblTotalOutcomePersonal, lblNetIncomePersonal;
    JLabel lblTotalIncomeHome, lblTotalOutcomeHome, lblNetIncomeHome;

    PanelTransactionDetails panelTransactionDetails;

    double totalIncomeHome, totalOutcomeHome;
    double totalIncomePersonal, totalOutcomePersonal;

    NetSummaryBudget(PanelTransactionDetails pnlTransaction) {
        aggrNet = new double[2];
        setBackground(Color.decode("#759de1"));
        
        this.panelTransactionDetails = pnlTransaction;

        setLayout(new GridLayout(4, 3));

        blank = new JLabel("");
        lblHomeAggregated = new JLabel("Home (Aggregated for Home AC Only)");
        lblPersonal = new JLabel("Personal");

        lblTotalIncome = new JLabel("Total Income (A)");
        lblTotalOutcome = new JLabel("Total Outcome (B)");
        lblNetIncome = new JLabel("Net Income(A-B)");

        lblTotalIncomePersonal = new JLabel();
        lblTotalOutcomePersonal = new JLabel();
        lblNetIncomePersonal = new JLabel();

        lblTotalIncomeHome = new JLabel();
        lblTotalOutcomeHome = new JLabel();
        lblNetIncomeHome = new JLabel();

        add(blank);
        add(lblHomeAggregated);
        add(lblPersonal);

        add(lblTotalIncome);
        add(lblTotalIncomeHome);
        add(lblTotalIncomePersonal);

        add(lblTotalOutcome);
        add(lblTotalOutcomeHome);
        add(lblTotalOutcomePersonal);

        add(lblNetIncome);
        add(lblNetIncomeHome);
        add(lblNetIncomePersonal);

        setLblTotalAmountPersonal("Income");
        setLblTotalAmountPersonal("Outcome");

    }

    void setLblTotalAmountPersonal(String type) {
        double sum = 0;
        for (int i = 0; i < panelTransactionDetails.getTableTransaction().getRowCount(); i++) {
            if (panelTransactionDetails.getTableTransaction().getValueAt(i, 5).equals((type))) {
                sum = sum + Double.parseDouble((String) panelTransactionDetails.getTableTransaction().getValueAt(i, 4));
            }
        }
        if (type.equals("Income")) {
            totalIncomePersonal = sum;
            lblTotalIncomePersonal.setText(Double.toString(sum));
            lblTotalIncomePersonal.setForeground(Color.blue);

        } else if (type.equals("Outcome")) {
            totalOutcomePersonal = sum;
            lblTotalOutcomePersonal.setText(Double.toString(sum));
            lblTotalOutcomePersonal.setForeground(Color.blue);
        }

    }

    void setLblNetAmount() {
        lblNetIncomePersonal.setText(Double.toString(totalIncomePersonal - totalOutcomePersonal));
        lblNetIncomePersonal.setForeground(Color.blue);

        lblTotalIncomeHome.setText(Double.toString(aggrNet[0]));
        lblTotalOutcomeHome.setText(Double.toString(aggrNet[1]));
        lblNetIncomeHome.setText(Double.toString(totalIncomeHome - totalOutcomeHome));

        lblNetIncomeHome.setForeground(Color.red);
        lblTotalIncomeHome.setForeground(Color.red);
        lblTotalOutcomeHome.setForeground(Color.red);

        writeNetAmountToFile(null, false);
    }

    void writeNetAmountToFile(String useraccount, boolean surplusAddition) {
        String userName;
        if (useraccount != null) {
            userName = useraccount;
        } else {
            userName = panelTransactionDetails.main_Dashboard.loggedInUser;
        }

        File tmpFile = new File("src/txtfiles/NetIncomeAll.tmp"); //Construct the new file that will later be renamed to the original filename. 
        File oldFile = new File("src/txtfiles/NetIncomeAll.txt");

        FileWriter fWriter = null;
        BufferedWriter bWriter = null;
        PrintWriter pWriter = null;

        BufferedReader bReader = null;
        try {
            //Initialize all writers readers declared above
            fWriter = new FileWriter(tmpFile, true);
            bWriter = new BufferedWriter(fWriter);
            pWriter = new PrintWriter(bWriter, true);
            bReader = new BufferedReader(new FileReader(oldFile));

            String line;
            // Read from the original file and write to the new 
            //unless content matches data to be removed.
            double oldNetIncome = 0.0;
            while ((line = bReader.readLine()) != null) {
                String[] whole = line.split(",");
                if (!whole[0].equals(userName)) {
                    pWriter.println(line);
                    pWriter.flush();
                } else {
                    oldNetIncome = Double.parseDouble(whole[1]);
                }
            }
            if (surplusAddition == false) {
                pWriter.println(userName + "," + lblNetIncomePersonal.getText() + "," + lblTotalOutcomePersonal.getText());//write updated net income
            } else {
                double netNetIncome = Double.parseDouble(lblNetIncomePersonal.getText()) + oldNetIncome;
                pWriter.println(userName + "," + Double.toString(netNetIncome) + "," + lblTotalOutcomePersonal.getText());//write updated net income

            }
            pWriter.close();
            bReader.close();
            oldFile.delete();
            tmpFile.renameTo(oldFile);

        } catch (IOException iOException) {
            System.out.println("Error: " + iOException.getMessage());
        } finally {
            try {
                fWriter.close();
                bReader.close();

            } catch (IOException ex) {
                ex.printStackTrace();
//            }
            }
        }
    }

    public void setAggrNet(double[] aggrNet) {
        this.aggrNet = aggrNet;
        totalIncomeHome = aggrNet[0];
        totalOutcomeHome = aggrNet[1];
        setLblNetAmount();

    }
}
