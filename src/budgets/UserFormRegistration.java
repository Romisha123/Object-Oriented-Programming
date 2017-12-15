/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgets;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Stream;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Romisha Thapa
 */
public class UserFormRegistration extends JFrame implements ActionListener {

    JLabel lblName, lblUsername, lblPassword, lblAccountType;
    JTextField txtName, txtUsername;
    JPasswordField txtPassword;
    JComboBox<String> cboAccountType;
    JButton btnSubmit, btnReset, btnRegister;

    JTable tblRegistration;
    TableModel tblModel;
    JScrollPane scrollPane;
    Container c;

    JPanel myPanel;
    LinkedList<UserAccount> stackAccount;
    boolean allowRegister = false;
    String ACType;

    Font font1;

    public UserFormRegistration() {

        setTitle("Sign Up");
        setLayout(new FlowLayout());
        setSize(420, 360);

        font1 = new Font("Lucida Fax", Font.PLAIN, 17);

        Container c = getContentPane();
        c.setBackground(Color.decode("#759de1"));

        myPanel = new JPanel(new GridLayout(7, 2));
        lblName = new JLabel("Name");
        lblName.setFont(font1);

        lblUsername = new JLabel("Username");
        lblUsername.setFont(font1);

        lblPassword = new JLabel("Password");
        lblPassword.setFont(font1);

        lblAccountType = new JLabel("Account Type");
        lblAccountType.setFont(font1);

        myPanel.setBackground(Color.decode("#759de1"));

        txtName = new JTextField(10);
        txtName.setFont(font1);
        //txtName.setBackground(Color.decode("#759de1"));

        txtUsername = new JTextField(10);
        txtUsername.setFont(font1);
        //txtUsername.setBackground(Color.decode("#759de1"));

        txtPassword = new JPasswordField(10);
        txtPassword.setFont(font1);
        //txtPassword.setBackground(Color.decode("#759de1"));

        btnSubmit = new JButton("Submit");
        btnSubmit.setBackground(Color.decode("#466082"));
        btnSubmit.setForeground(Color.white);

        btnReset = new JButton("Reset");
        btnReset.setBackground(Color.decode("#466082"));
        btnReset.setForeground(Color.white);

        btnRegister = new JButton("Register");
        btnRegister.setBackground(Color.decode("#466082"));
        btnRegister.setForeground(Color.white);

        btnSubmit.addActionListener(this);
        btnReset.addActionListener(this);
        btnRegister.addActionListener(this);

        //Instantiating (creating object) & populating (inserting opetions) combobox
        cboAccountType = new JComboBox<String>();
        String[] accountTypes = {"Home", "Shared", "Discretionary"};
        for (String eachAccountType : accountTypes) {
            cboAccountType.addItem(eachAccountType);
        }
        cboAccountType.setBackground(Color.white);
        cboAccountType.setFont(font1);

        //Blank table instantiation
        String[] columnHeadings = {"NAME", "USERNAME", "PWD (ENCRYPT)", "AC TYPE"};
        tblModel = new DefaultTableModel(columnHeadings, 0);
        tblRegistration = new JTable(tblModel) {//Creating table object & making table anonymous class to make cell editable false.
            @Override
            public boolean isCellEditable(int data, int column) {
                return false;
            }
        };

        //Inserting vertical scrollbar to table
        tblRegistration.setAutoCreateRowSorter(true);
        tblRegistration.setPreferredScrollableViewportSize(new Dimension(400, 50));
        tblRegistration.setFillsViewportHeight(true);
        scrollPane = new JScrollPane(tblRegistration);

        myPanel.add(lblName);
        myPanel.add(txtName);
        myPanel.add(lblUsername);
        myPanel.add(txtUsername);
        myPanel.add(lblPassword);
        myPanel.add(txtPassword);
        myPanel.add(lblAccountType);
        myPanel.add(cboAccountType);
        myPanel.add(new JLabel(""));
        myPanel.add(new JLabel(""));
        myPanel.add(btnSubmit);
        myPanel.add(btnReset);
        c.add(myPanel);
        c.add(scrollPane);
        c.add(btnRegister);

        stackAccount = new LinkedList<UserAccount>();
//        pack();
//        setVisible(true);
        setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton btnSource = (JButton) e.getSource();
        if (btnSource.equals(btnSubmit)) {
            String[] invalidChars = new String[]{"|", ";", "."};

            //Null validation
            if (txtName.getText().equals(null) || txtName.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Name cannot be blank!");
                txtName.setText("");
                txtName.requestFocus();
                return;
            }
            if (txtUsername.getText().equals(null) || txtName.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Username cannot be blank!");
                txtUsername.setText("");
                txtUsername.requestFocus();
                return;
            }
            if (new String(txtPassword.getPassword()).equals(null) || new String(txtPassword.getPassword()).equals("")) {
                JOptionPane.showMessageDialog(null, "Password cannot be blank!");
                txtPassword.setText("");
                txtPassword.requestFocus();
                return;
            }
            //Invalid character validation
            for (int i = 0; i < invalidChars.length; i++) {
                if (txtUsername.getText().contains(invalidChars[i])) {
                    JOptionPane.showMessageDialog(null, "'|' & ',' & ';' are reserved characters. User name cannot contain them");
                    txtUsername.setText("");
                    return;
                }
                if (new String(txtPassword.getPassword()).contains(invalidChars[i])) {
                    JOptionPane.showMessageDialog(null, "'|' & ',' & ';' are reserved characters. Password cannot contain them");
                    txtPassword.setText("");
                    return;
                }
            }

            //Duplicate username validation
            if (!validateUniqueUsername(txtUsername.getText())) {
                JOptionPane.showMessageDialog(null, "Username already taken. Enter unique username.");
                return;
            }
            switch (cboAccountType.getSelectedItem().toString()) {
                case "Shared":
                    ACType = "Shared";
                    if (tblRegistration.getRowCount() < 2) {
                        UserAccount sharedAC = new UserAccount(txtName.getText(), txtUsername.getText(),new String(txtPassword.getPassword()), "Shared");
                        stackAccount.add(sharedAC);
                        populateTable();
                        allowRegister = (tblRegistration.getRowCount() == 2) ? true : false;//result = testCondition ? value1 : value2              
                        cboAccountType.setEnabled(false);
                    } else {
                        JOptionPane.showMessageDialog(null, "Shared account can have only 2 users!");
                    }
                    break;

                case "Home":
                    ACType = "Home";
                    UserAccount homeAC = new UserAccount(txtName.getText(), txtUsername.getText(),new String(txtPassword.getPassword()), "Home");
                    stackAccount.add(homeAC);
                    populateTable();
                    allowRegister = true; //result = testCondition ? value1 : value2
                    cboAccountType.setEnabled(false);
                    break;
                case "Discretionary":
                    ACType = "Discretionary";
                    if (tblRegistration.getRowCount() < 1) {
                        UserAccount discretionaryAC = new UserAccount(txtName.getText(), txtUsername.getText(),new String(txtPassword.getPassword()), "Discretionary");
                        stackAccount.add(discretionaryAC);
                        populateTable();
                        allowRegister = (tblRegistration.getRowCount() == 1) ? true : false; //result = testCondition ? value1 : value2          
                        cboAccountType.setEnabled(false);
                    } else {
                        JOptionPane.showMessageDialog(null, "Discretionary account can have only 1 user!");
                    }
            }

        } else if (btnSource.equals(btnReset)) {
            ((DefaultTableModel) tblRegistration.getModel()).setRowCount(0); //clear table
            stackAccount.clear(); //clear accounts stack
            txtName.setText("");
            txtPassword.setText("");
            txtUsername.setText("");
            txtName.requestFocus();
            cboAccountType.setEnabled(true);
        } else if (btnSource.equals(btnRegister)) {
            if (allowRegister == true) {
                writeLoginDetailsToFile();
                writeConnections(ACType);

                JOptionPane.showMessageDialog(null, "Successfully Registered :-)");
                btnReset.doClick();
            } else {
                JOptionPane.showMessageDialog(null, "Please provide your full details");
            }
        }
    }

    public boolean validateUniqueUsername(String username) {
        for (UserAccount eachAccount : stackAccount) {
            if (eachAccount.userName.equals(username)) {
                return false;
            }
        }

        for (String eachUserName : readAllUserNames()) {
            if ((eachUserName.equals(username))) {
                return false;
            }
        }
        return true;
    }

    public void populateTable() {
        DefaultTableModel model = (DefaultTableModel) tblRegistration.getModel();
        String[] tuple = new String[4];
        model.setRowCount(0); //Clear table

        for (UserAccount eachAccount : stackAccount) { //Loop through all indices/rooms in this stack
            tuple[0] = eachAccount.name;
            tuple[1] = eachAccount.userName;
            tuple[2] = eachAccount.password;
            tuple[3] = eachAccount.accountType;

            model.addRow(tuple);
            Arrays.fill(tuple, null); //Clearing all elements from tuple for next transaction data

        }
    }

    public void writeLoginDetailsToFile() {
        //Declare all writers (We need 3 writers to write to File)
        FileWriter fWriter = null;
        BufferedWriter bWriter = null;// Uses RAM to increase write performance. Less hard disk activity is good.
        PrintWriter pWriter = null;//We can use bufferwriter but PrintWriter lets us print in new line easily
        File TransactionFile = null;
        File TriggerFile = null;
        try {
            //Creating blank CSV file for future transaction for each accounts
            for (UserAccount eachAccount : stackAccount) {
                TransactionFile = new File("src/txtfiles/" + eachAccount.userName + "_Transaction" + ".txt"); //Instantiate a file object (blank)
                TriggerFile = new File("src/txtfiles/" + eachAccount.userName + "_Trigger" + ".txt"); //Instantiate a file object (blank)
                TransactionFile.createNewFile(); //Create a blank notepad file (.txt format) 
                TriggerFile.createNewFile(); //Create a blank notepad file (.txt format) 
            }
            //Write to file - Login details
            fWriter = new FileWriter("src/txtfiles/" + "LoginDetails.txt", true);
            bWriter = new BufferedWriter(fWriter);
            pWriter = new PrintWriter(bWriter);
            for (UserAccount eachAccount : stackAccount) { //Loop through each indices/rooms in the stack.
                pWriter.println("|" + eachAccount.name + "|" + "," + "|" + eachAccount.userName + "|" + "," + "|" + eachAccount.password + "|" + "," + "|" + eachAccount.accountType + "|");
            }
            pWriter.close();
        } catch (IOException iOException) {
            System.out.println("Error: " + iOException.getMessage());
        } finally { //We need to release system resources that writers have locked into.
            try {
                fWriter.close();
                bWriter.close();
                pWriter.close();

            } catch (IOException ex) {//Catch IO Exception
                ex.printStackTrace(); //Print error messages
//            }
            }
        }
    }

    public void writeConnections(String ACType) {
        FileWriter fWriter = null;
        BufferedWriter bWriter = null;
        PrintWriter pWriter = null;
        BufferedReader bReader = null;
        File oldFile = new File("src/txtfiles/Connections.txt");
        File tmpFile = new File("src/txtfiles/Connections.tmp");

        try {
            //Initialize all writers readers declared above
            fWriter = new FileWriter(tmpFile, true);
            bWriter = new BufferedWriter(fWriter);
            pWriter = new PrintWriter(bWriter, true);
            bReader = new BufferedReader(new FileReader(oldFile));

            String line;
            boolean writeOnceOnly = false;
            // Read from the original file and write to the new 
            //unless content matches data to be removed.
            if (ACType.equals("Home")) {
                while ((line = bReader.readLine()) != null) {
                    if (!line.contains("Home")) {
                        pWriter.println(line);
                    } else if (line.contains("Home")) {
                        String[] whole = line.split(",");
                        String[] oldUsers = whole[1].split(";");
                        for (int i = 0; i < oldUsers.length; i++) {
                            oldUsers[i] = oldUsers[i].replace("|", "");
                        }
                        String[] newUsers = new String[stackAccount.size()];
                        int index = 0;
                        for (UserAccount eachAccount : stackAccount) { //Loop through each indices/rooms in the stack.
                            newUsers[index] = eachAccount.userName;
                            index++;
                        }
                        String[] mergedUsers = Stream.concat(Arrays.stream(oldUsers), Arrays.stream(newUsers)).toArray(String[]::new);//Join 2 arrays
                        line = "";
                        line = line + ("|" + ACType + "|" + "," + "|");
                        for (int i = 0; i < mergedUsers.length; i++) {
                            if (i != mergedUsers.length) {
                                line = line + (mergedUsers[i] + ";");
                            } else {
                                line = line + (mergedUsers[i]);
                            }
                        }
                        line = line.replace(";;", ";");
                        line = line + "|";
                        pWriter.println(line);
                    } else if (writeOnceOnly == true) {
                        String[] userNames = new String[stackAccount.size()];
                        int index = 0;
                        pWriter.print("|" + ACType + "|" + "," + "|");

                        for (UserAccount eachAccount : stackAccount) { //Loop through each indices/rooms in the stack.
                            userNames[index] = eachAccount.userName;
                            index++;
                        }

                        for (int i = 0; i < userNames.length; i++) {
                            if (i != userNames.length) {
                                pWriter.print(userNames[i] + ";");
                            } else {
                                pWriter.print(userNames[i]);
                            }
                        }
                        writeOnceOnly = false;
                        pWriter.print("|");
                        pWriter.println();
                        pWriter.flush();
                    }
                }
            } else {
                while ((line = bReader.readLine()) != null) {
                    pWriter.println(line);
                }
                String[] userNames = new String[stackAccount.size()];
                int index = 0;
                pWriter.print("|" + ACType + "|" + "," + "|");

                for (UserAccount eachAccount : stackAccount) { //Loop through each indices/rooms in the stack.
                    userNames[index] = eachAccount.userName;
                    index++;
                }

                for (int i = 0; i < userNames.length; i++) {
                    if (i != userNames.length) {
                        pWriter.print(userNames[i] + ";");
                    } else {
                        pWriter.print(userNames[i]);
                    }
                }
                writeOnceOnly = false;
                pWriter.print("|");
                pWriter.println();
                pWriter.flush();
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

    public LinkedList<String> readAllUserNames() {
        LinkedList<String> allUserNames = new LinkedList();
        String readLine;
        FileReader fr = null;
        BufferedReader br = null;
        try {
            File CSVFile = new File("src/txtfiles/LoginDetails.txt"); //Load file (CSV notepad file) in memory
            fr = new FileReader(CSVFile);
            br = new BufferedReader(fr);

            while ((readLine = br.readLine()) != null) {
                String[] eachLine = readLine.split(","); //Reads each line and splits by comma
                allUserNames.add(eachLine[1].replace("|", ""));
            }

        } catch (FileNotFoundException fnfe) {
            System.out.println("Error: File not present in disk!" + fnfe.getMessage());
        } catch (SecurityException se) {
            System.out.println("Cannot access file. Access is denied");
        } catch (IOException ioe) {    //Any exception not caught by FileNotFoundException will be caught by this
            System.out.println("Cannot read from file. Error: " + ioe.getMessage());
        } finally {//We need to release system resources that writers have locked into.
            try {
                fr.close();
            } catch (IOException ex) { //Catch IO Exception
                ex.printStackTrace(); //Print error messages
            }
        }
        return allUserNames;
    }

}
