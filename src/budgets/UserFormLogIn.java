/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgets;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author Romisha Thapa
 */
public class UserFormLogIn extends JFrame implements ActionListener {

    JLabel lblUsername, lblPassword;
    JTextField txtUsername;
    JPasswordField txtPassword;
    JButton btnLogin, btnOpenRegistrationForm;

    LinkedList<UserAccount> stackAccounts;
    LinkedList<String> stackConnections;
    UserFormRegistration userFormRegistration;
    LinkedList<String> connectedUsersNet;
    
    Font font1,font2;

    public UserFormLogIn() {
        Container c = getContentPane();
        c.setLayout(null);

        setTitle("Login (Budgets Application by Romisha Thapa");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setSize(600, 300);
        c.setBackground(Color.decode("#759de1"));
        
        font1 =  new Font("Lucida Fax", Font.BOLD, 17);
        font2 =  new Font("Lucida Fax", Font.PLAIN, 20);
        

        lblUsername = new JLabel("   Username   ");
        lblUsername.setFont(font2);
        lblUsername.setForeground(Color.white);
        
        
        
        lblPassword = new JLabel("   Password   ");
        lblPassword.setFont(font2);
        lblPassword.setForeground(Color.white);
        
        txtUsername = new JTextField(20);
        txtUsername.setBackground(Color.decode("#acd0e5"));
//        txtUsername.setForeground(Color.white);
        txtUsername.setBorder(null);
        
        
        txtPassword = new JPasswordField(20);
        txtPassword.setBackground(Color.decode("#acd0e5"));
        txtPassword.setBorder(null);
                
        btnLogin = new JButton("Login");
        btnLogin.setBackground(Color.decode("#466082"));
        btnLogin.setBorder(null);
        btnLogin.setForeground(Color.white);
        
        btnOpenRegistrationForm = new JButton("SignUp");
        btnOpenRegistrationForm.setBackground(Color.decode("#466082"));
        btnOpenRegistrationForm.setBorder(null);
        btnOpenRegistrationForm.setForeground(Color.white);

        btnLogin.addActionListener(this);
        btnOpenRegistrationForm.addActionListener(this);

        
        JLabel heading2 = new JLabel("BUDGETS APPLICATION");
        heading2.setFont(new Font("Lucida Fax", Font.PLAIN, 30));
        heading2.setForeground(Color.white);
        
        c.add(heading2);
        heading2.setBounds(120, 10, 400, 30);
//        JLabel img = new JLabel("", new ImageIcon("src/budgets/Romi.jpg"), JLabel.CENTER);
//        c.add(img);


        c.add(lblUsername);
        lblUsername.setBounds(100, 80, 150, 20);
        
        c.add(txtUsername);
        txtUsername.setBounds(250, 80, 250, 20);
        
        c.add(lblPassword);
        lblPassword.setBounds(100, 120, 150, 20);
        
        c.add(txtPassword);
        txtPassword.setBounds(250, 120, 250, 20);
        
        c.add(btnLogin);
        btnLogin.setBounds(150, 180, 150, 30);
        
        c.add(btnOpenRegistrationForm);
        btnOpenRegistrationForm.setBounds(310, 180, 150, 30);
        
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);

        stackAccounts = new LinkedList<UserAccount>();
        addToStackAccounts();
        stackConnections = new LinkedList<>();

        userFormRegistration = new UserFormRegistration();
        userFormRegistration.setVisible(false);
        //Anonymous class to override close operation. When form is closed it is hidden. 
        userFormRegistration.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                userFormRegistration.dispose();
                addToStackAccounts();
                getConnections();
            }
        });
        connectedUsersNet = new LinkedList();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton btnSource = (JButton) e.getSource();

        if (btnSource.equals(btnLogin)) {
            //Null validation
            if (txtUsername.getText().equals(null) || txtUsername.getText().equals("")) {
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
            if (new String(txtPassword.getPassword()).equals(null) || new String(txtPassword.getPassword()).equals("")) {
                JOptionPane.showMessageDialog(null, "Password cannot be blank!");
                txtPassword.setText("");
                txtPassword.requestFocus();
                return;
            }
            boolean verified = false;

            for (UserAccount eachAccount : stackAccounts) {
                if ((txtUsername.getText().equals(eachAccount.userName))) {
                    if (((new String(txtPassword.getPassword())).equals(eachAccount.decipher_Password()))) {
                        JOptionPane.showMessageDialog(null, "Welcome!");
                        verified = true;
                        Main_Dashboard mainGUI = new Main_Dashboard(getConnections(), txtUsername.getText(), eachAccount.accountType);
                        mainGUI.netSummaryBudget.setAggrNet(mainGUI.getAggregatedNetIncome(connectedUsersNet));
                        dispose();
                        break;
                    }
                }
            }
            if (verified == false) {
                JOptionPane.showMessageDialog(null, "Incorrect Username or Password!");
                txtUsername.selectAll();
                txtUsername.requestFocus();
            }
        } else if (btnSource.equals(btnOpenRegistrationForm)) {
            userFormRegistration.setVisible(true);
            userFormRegistration.btnReset.doClick(); //Reset all fields
        }
    }

    public static void main(String[] args) {
        UserFormLogIn frmLogIn = new UserFormLogIn();
    }

    void addToStackAccounts() {
        stackAccounts.clear();
        String readLine;

        FileReader fr = null;
        BufferedReader br = null;

        try {

            File CSVFile = new File("src/txtfiles/LoginDetails.txt"); //Load file (CSV notepad file) in memory

            //Initialize all readers decalred above
            fr = new FileReader(CSVFile);
            br = new BufferedReader(fr);
            stackAccounts.clear();

            while ((readLine = br.readLine()) != null) {

                String[] eachLine = readLine.split(","); //Reads each line and splits by comma

                UserAccount eachAccount = new UserAccount(eachLine[0].replace("|", ""), eachLine[1].replace("|", ""),eachLine[2].replace("|", ""), eachLine[3].replace("|", ""), false);
                stackAccounts.add(eachAccount);
            }

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

    }

    LinkedList<String> getConnections() {
        String readLine;

        FileReader fr = null;
        BufferedReader br = null;

        try {

            File CSVFile = new File("src/txtfiles/Connections.txt"); //Load file (CSV notepad file) in memory

            //Initialize all readers decalred above
            fr = new FileReader(CSVFile);
            br = new BufferedReader(fr);
            stackConnections.clear();
            String[] eachLine = null;

            boolean stopReadLine = false;
            while ((readLine = br.readLine()) != null) {
                eachLine = readLine.split(","); //Reads each line and splits by comma 
                if (eachLine[0].equals("|Shared|")) {
                    eachLine[1] = eachLine[1].replace("|", "");
                    String[] userNames = eachLine[1].split(";");
                    for (String userName : userNames) {
                        if (userName.equals(txtUsername.getText())) {
                            stackConnections.addAll(Arrays.asList(userNames));
                            connectedUsersNet = stackConnections;
                            stopReadLine = true;
                        }
                    }

                } else if (eachLine[0].equals("|Home|")) {
                    eachLine[1] = eachLine[1].replace("|", "");
                    String[] userNames = eachLine[1].split(";");
                    for (String userName : userNames) {
                        if (userName.equals(txtUsername.getText())) {
                            connectedUsersNet.addAll(Arrays.asList(userNames));
                            stopReadLine = true;
                        }
                    }

                    stackConnections.add(txtUsername.getText());
                    stopReadLine = true;
                } else if (eachLine[0].equals("|Discretionary|")) {
                    stackConnections.add(txtUsername.getText());
                    connectedUsersNet = stackConnections;
                    stopReadLine = true;
                }

                if (stopReadLine == true) {
                    break;
                }
            }

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
        return stackConnections;

    }

}
