/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgets;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.ListIterator;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author Romisha
 */
public class Main_Dashboard extends JFrame implements ActionListener {

    JTabbedPane tabbedPane;
    PanelTransactionDetails panelTransactionDetails;
    PanelTriggerDetails panelTriggerDetails;

    NetSummaryBudget netSummaryBudget;
    LinkedList<String> stackConnections;

    String loggedInUser;
    String accountType;

    JPanel pnlSettings;
    JButton btnEstimate, deleteAC;

    //Declaring objects for Delete AC button
    boolean isOpen = false;
    JFrame frame = null;
    LinkedList<String> connectedUsers;
    JComboBox<String> users;
    
    
    Font font1;

    public Main_Dashboard(LinkedList<String> stackConnections, String loggedInUser, String accountType) {
        this.stackConnections = stackConnections;
        this.loggedInUser = loggedInUser;
        this.accountType = accountType;

        connectedUsers = new LinkedList();
        
        font1 = new Font("Lucida Fax", Font.PLAIN, 15);

        tabbedPane = new JTabbedPane();
        panelTransactionDetails = new PanelTransactionDetails(this);
        netSummaryBudget = new NetSummaryBudget(panelTransactionDetails);
        panelTriggerDetails = new PanelTriggerDetails(this);
        pnlSettings = new JPanel(new FlowLayout());
        Container c = getContentPane();

        setTitle("Home Budgetting - LOGGED IN AS: " + loggedInUser + "    " + "ACCOUNT TYPE: " + accountType);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(920, 600);
        setLocationRelativeTo(null);
        c.setLayout(new BorderLayout());
        c.setBackground(Color.decode("#759de1"));

        btnEstimate = new JButton("Balance Estimation (Monthly)");
        btnEstimate.setFont(font1);
        btnEstimate.setBackground(Color.decode("#466082"));
        btnEstimate.setForeground(Color.white);
        
        
        deleteAC = new JButton("Remove Account & Transfer Surplus");
        deleteAC.setFont(font1);
        deleteAC.setBackground(Color.decode("#466082"));
        deleteAC.setForeground(Color.white);

        btnEstimate.addActionListener(this);
        deleteAC.addActionListener(this);
        deleteAC.addActionListener(this);
        pnlSettings.add(btnEstimate);
        pnlSettings.add(deleteAC);
        pnlSettings.setBackground(Color.decode("#759de1"));

        tabbedPane.addTab("Transactions", panelTransactionDetails);
        tabbedPane.addTab("Trigger", (JPanel) panelTriggerDetails);

        c.add(pnlSettings, BorderLayout.NORTH);
        c.add(tabbedPane, BorderLayout.CENTER);
        c.add(netSummaryBudget, BorderLayout.PAGE_END);

        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                new UserFormLogIn();
            }
        });

    }

    double[] getAggregatedNetIncome(LinkedList<String> stackConnections) {
        this.connectedUsers = stackConnections;
        String readLine;
        double aggrIncome = 0;
        double aggrOutcome = 0;
        double[] aggrNet = new double[2];

        //Declare all readers (We need 2 readers to read from File)
        FileReader fr = null;
        BufferedReader br = null;

        try {

            File CSVFile = new File("src/txtfiles/NetIncomeAll.txt"); //Load file (CSV notepad file) in memory

            //Initialize all readers decalred above
            fr = new FileReader(CSVFile);
            br = new BufferedReader(fr);

            String[] eachLine = null;
            while ((readLine = br.readLine()) != null) {
                eachLine = readLine.split(","); //Reads each line and splits by comma 
                for (String eachUser : stackConnections) {
                    if (eachLine[0].equals(eachUser)) {
                        aggrIncome = aggrIncome + Double.parseDouble(eachLine[1]);
                        aggrOutcome = aggrOutcome + Double.parseDouble(eachLine[2]);

                    }
                }

            }
            aggrNet[0] = aggrIncome;
            aggrNet[1] = aggrOutcome;

        } catch (FileNotFoundException fnfe) {
            System.out.println("Error: File not present in disk!" + fnfe.getMessage());
        } catch (SecurityException se) {
            System.out.println("Cannot access file. Access is denied");
        } catch (IOException ioe) {    //Any exception not caught by FileNotFoundException will be caught by this
            System.out.println("Cannot read from file. Error: " + ioe.getMessage());
        } finally {//We need to release system resources that writers have locked into.
            try {
                br.close();
                fr.close();
            } catch (IOException ex) { //Catch IO Exception
                ex.printStackTrace(); //Print error messages
            }
        }

        return aggrNet;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = (Object) e.getSource();
        if (src.equals(btnEstimate)) {

            double sum = 0;
            int count = 0;
            for (int i = 0; i < panelTransactionDetails.getTableTransaction().getRowCount(); i++) {
                if (panelTransactionDetails.getTableTransaction().getValueAt(i, 5).equals("Outcome")) {
                    sum = sum + Double.parseDouble((String) panelTransactionDetails.getTableTransaction().getValueAt(i, 4));
                    count++;
                }
            }
            double avgSpendingPerMonth = sum / count;
            double currentBalance = (netSummaryBudget.totalIncomePersonal) - (netSummaryBudget.totalOutcomePersonal);
            String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"};

            int currentMonth;
            GregorianCalendar date = new GregorianCalendar();
            currentMonth = date.get(Calendar.MONTH);
            currentMonth = currentMonth + 1;

            String msg = "Average Spending Per Month: " + avgSpendingPerMonth + "\n" + "Current balance: " + currentBalance + "\n";
            for (int i = months.length - (months.length - currentMonth); i < months.length; i++) {
                msg = msg + "\n\n" + ("Estimated balance in " + months[i] + ": " + (currentBalance - (avgSpendingPerMonth * (i - currentMonth))));
            }
            JOptionPane.showMessageDialog(null, msg, "Estimated Monthly Balannce", JOptionPane.INFORMATION_MESSAGE);

        } else if (src.equals(deleteAC)) {

            if (isOpen == false) {
                JOptionPane.showMessageDialog(null, "Your account will be deleted. First, Select account/username to transfer surplus to");
                frame = new JFrame();
                users = new JComboBox();
                users.addActionListener(this);
                connectedUsers.remove(loggedInUser);
                for (String eachUser : connectedUsers) {
                    users.addItem(eachUser);
                }
                frame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        frame.setVisible(false);
                        isOpen = true;
                    }
                });
                isOpen = true;
                frame.add(users);
                frame.setTitle("Select Account(username)");
                frame.setSize(200, 100);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

                if (connectedUsers.size() < 1) {
                    frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                    JOptionPane.showMessageDialog(null, "No connected users in discretionary account. Surplus cannot be transferred.");
                    File toDeleteTransaction = new File("src/txtfiles/" + loggedInUser + "_Transaction.txt");
                    File toDeleteTrigger = new File("src/txtfiles/" + loggedInUser + "_Trigger.txt");
                    toDeleteTransaction.delete();
                    toDeleteTrigger.delete();
                    cleanConnectionsFile();
                    cleanLoginDetailsFile();
                    cleanNetIncomeFile(loggedInUser, true);
                }

            } else {
                frame.setVisible(true);
            }

        } else if (src.equals(users)) {
            String selectedAC = users.getSelectedItem().toString();
            int response = -1;
            if (isOpen == true) {
                response = JOptionPane.showConfirmDialog(null, "Confirm selection?");
            }

            if (response == JOptionPane.YES_OPTION) {
                double surplus = Double.parseDouble(netSummaryBudget.lblNetIncomePersonal.getText());
                LocalDate localDate = LocalDate.now();
                String currentDate = (DateTimeFormatter.ofPattern("yyyy/MM/dd").format(localDate));

                if (surplus > 0) {
                    Transaction surplusTran = new Transaction(selectedAC, currentDate, 
                    "Transferred Surplus from Account of username - " + loggedInUser, surplus, "Income");
                    deleteAllRecords(surplusTran, selectedAC);
                    cleanConnectionsFile();
                    cleanLoginDetailsFile();
                    cleanNetIncomeFile(selectedAC, false);

                    frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                    this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
                } else {
                    JOptionPane.showMessageDialog(null, "Insufficient funds. Surplus cannot be transferred.");
                    File toDeleteTransaction = new File("src/txtfiles/" + loggedInUser + "_Transaction.txt");
                    File toDeleteTrigger = new File("src/txtfiles/" + loggedInUser + "_Trigger.txt");
                    toDeleteTransaction.delete();
                    toDeleteTrigger.delete();
                    cleanConnectionsFile();
                    cleanLoginDetailsFile();
                    cleanNetIncomeFile(selectedAC, true);

                    frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                    this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
                }
            }

        }
    }

    void cleanConnectionsFile() {
        File tmpFile = new File("src/txtfiles/Connections.tmp"); 
//Construct the new file that will later be renamed to the original filename. 
        File oldFile = new File("src/txtfiles/Connections.txt");

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
            String toPrint = "";

            // Read from the original file and write to the new 
            //unless content matches data to be removed.
            while ((line = bReader.readLine()) != null) {
                if (!line.contains(loggedInUser + ";")) {
                    pWriter.println(line);
                } else {
                    String[] whole = line.split(",");
                    int count = 0;
                    for (char ch : whole[1].toCharArray()) {
                        if (ch == ';') {
                            count++;
                        }
                    }
                    if (count != 1) {
                        String users[] = whole[1].split(";");
                        for (int i = 0; i < users.length; i++) {
                            users[i] = users[i].replace("|", "");
                            if (users[i].equals(loggedInUser)) {
                                users[i] = "";
                            }
                        }
                        System.out.println("---------------------");
                        LinkedList Users = new LinkedList<String>();
                        Users.addAll(Arrays.asList(users));

                        ListIterator<String> itr = Users.listIterator();
                        while (itr.hasNext()) {
                            String eachElement = itr.next();
                            if (eachElement.equals(" ") || eachElement.equals("")) {
                                itr.remove();
                            }
                        }

                        if (Users.size() == 1 && whole[0].equals("|Shared|")) {
                            whole[0] = "|Discretionary|";
                        }

                        pWriter.print(whole[0]);
                        pWriter.print(",|");
                        for (int j = 0; j < Users.size(); j++) {
                            toPrint = toPrint + Users.get(j).toString() + ";";
                        }
                        pWriter.print(toPrint + "|");
                        pWriter.println();

                    }
                }
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
            }
        }
    }

    void cleanNetIncomeFile(String seletedAC, boolean noSurplusTransfer) {
        File tmpFile = new File("src/txtfiles/NetIncomeAll.tmp"); 
// file tmpfile Constructs the new file that will later be renamed to the original filename. 
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
            while ((line = bReader.readLine()) != null) {
                String[] whole = line.split(",");
                if (!whole[0].equals(loggedInUser)) {
                    pWriter.println(line);
                    pWriter.flush();
                }
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
            }
        }
        if (noSurplusTransfer == false) {
            netSummaryBudget.writeNetAmountToFile(seletedAC, true);
        } else {
            netSummaryBudget.writeNetAmountToFile(seletedAC, false);

        }

    }

    void cleanLoginDetailsFile() {
        File tmpFile = new File("src/txtfiles/LoginDetails.tmp"); 
//file tmpfile Constructs the new file that will later be renamed to the original filename. 
        File oldFile = new File("src/txtfiles/LoginDetails.txt");

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
            while ((line = bReader.readLine()) != null) {
                if (!line.contains("|" + loggedInUser + "|")) {
                    pWriter.println(line);
                    pWriter.flush();
                }
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
            }
        }
    }

    void deleteAllRecords(Transaction surplusTran, String selectedAC) {
        FileWriter fWriter = null;
        BufferedWriter bWriter = null;
        PrintWriter pWriter = null;
        File CSVFile = null;
        try {
            CSVFile = new File("src/txtfiles/" + selectedAC + "_Transaction.txt");
            //csv file Instantiate a file object (blank)


            //Initialize all writers declared above
            fWriter = new FileWriter(CSVFile, true);
            bWriter = new BufferedWriter(fWriter);
            pWriter = new PrintWriter(bWriter);

            pWriter.println("|" + surplusTran.getTransactionID() + "|" + "," + "|"
                    + surplusTran.getLoggedInUser() + "|" + "," + "|" + surplusTran.getDate() + 
                    "|" + "," + "|" + surplusTran.getDescription() + "|" + "," + "|" + surplusTran.getAmount() + 
                    "|" + "," + "|" + surplusTran.getTransactionType() + "|");
            pWriter.close();
        } catch (IOException iOException) {
            System.out.println("Error: " + iOException.getMessage());
        } finally {
            try {
                fWriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        //Delete transaction & trigger records.
        File toDeleteTransaction = new File("src/txtfiles/" + loggedInUser + "_Transaction.txt");
        File toDeleteTrigger = new File("src/txtfiles/" + loggedInUser + "_Trigger.txt");
        toDeleteTransaction.delete();
        toDeleteTrigger.delete();
    }
}
