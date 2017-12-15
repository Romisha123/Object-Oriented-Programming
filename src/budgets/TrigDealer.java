/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgets;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 *
 * @author Romisha Thapa
 */
public class TrigDealer implements ActionListener {
    
    StackTriggers stackTriggers;
    PanelTriggerDetails panelTriggerDetails;
    
    Timer myTimer;
    
    TrigDealer(StackTriggers stackTriggers, PanelTriggerDetails panelTriggers) {
        this.stackTriggers = stackTriggers;
        this.panelTriggerDetails = panelTriggers;
        
        myTimer = new Timer(5000, this);
        myTimer.start();
    }
    
    void addToFile(String toWritefileName, String toEditfileName, String action) {
        String selectedFileName = null;
        //Declare all writers (We need 3 writers to write to File)
        FileWriter fWriter = null;
        BufferedWriter bWriter = null;// Uses RAM to increase write performance. Less hard disk activity is good.
        PrintWriter pWriter = null;//We can use bufferwriter but PrintWriter lets us print in new line easily
        File CSVFile = null;
        try {
            if (action.equals("write")) {
                CSVFile = new File("src/txtfiles/" + toWritefileName + "_Trigger.txt"); //Instantiate a file object (blank)
                if (!CSVFile.exists()) {
                    CSVFile.createNewFile(); //Create a blank notepad file (.txt format)if file doesn't exist.
                }
                selectedFileName = toWritefileName;
            } else if (action.equals("update")) {
                CSVFile = new File("src/txtfiles/" + toEditfileName + "_Trigger.txt"); //Instantiate a file object (blank)
                if (!CSVFile.exists()) {
                    CSVFile.createNewFile(); //Create a blank notepad file (.txt format)if file doesn't exist.
                }
                selectedFileName = toEditfileName;
            }
            //Initialize all writers declared above
            fWriter = new FileWriter(CSVFile);
            bWriter = new BufferedWriter(fWriter);
            pWriter = new PrintWriter(bWriter);
            
            for (Trigger eachTrigger : stackTriggers) { //Loop through each indices/rooms in the stack.
                if (eachTrigger.getLoggedInUser().equals(selectedFileName)) {
                pWriter.println("|" + eachTrigger.getTriggerID() + "|" + "," + "|" + eachTrigger.getLoggedInUser() + "|" + 
                         "," + "|" + eachTrigger.getDate() + "|" + "," + "|" + eachTrigger.getDescription() + "|" + "," + 
                            "|" + eachTrigger.getAmount() + "|" + "," + "|" + eachTrigger.getTriggerType() + "|");
                }
            }
            pWriter.close();
        } catch (IOException iOException) {
            System.out.println("Error: " + iOException.getMessage());
        } finally { //We need to release system resources that writers have locked into.
            try {
                fWriter.close();
            } catch (IOException ex) {//Catch IO Exception
                ex.printStackTrace(); //Print error messages
//            }
            }
        }
    }
    
    void getFromFile(boolean clearStack) {                                                                                                     
        if (clearStack == true) {
            stackTriggers.clear();
        }
        for (String eachStackConnection : panelTriggerDetails.main_Dashboard.stackConnections) {
            String readLine;

            //Declare all readers (We need 2 readers to read from File)
            FileReader fr = null;
            BufferedReader br = null;
            try {
                File CSVFile = new File("src/txtfiles/" + eachStackConnection + "_Trigger.txt"); //Load file (CSV notepad file) in memory

//Initialize all readers decalred above
                fr = new FileReader(CSVFile);
                br = new BufferedReader(fr);
                
                while ((readLine = br.readLine()) != null) {
                    
                    String[] eachLine = readLine.split(","); //Reads each line and splits by comma

                    Trigger eachTrigger = new Trigger(eachLine[0].replace("|", ""), eachLine[1].replace("|", ""),
                            eachLine[2].replace("|", ""), eachLine[3].replace("|", ""), Double.parseDouble(eachLine[4].replace("|", "")),           
                            eachLine[5].replace("|", ""), true);
                    stackTriggers.add(eachTrigger);
                }
                stackTriggers.populateTable(panelTriggerDetails);
                
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
        }

        //Check Trigger date matches current date
        isMatchTriggerDate();
    }
    
    void isMatchTriggerDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        LocalDate localDate = LocalDate.now();
        String currentDate = (DateTimeFormatter.ofPattern("yyyy/MM/dd").format(localDate));
        Calendar cTrigger = Calendar.getInstance();
        Calendar cCurrentDate = Calendar.getInstance();
        
        for (Trigger eachTrigger : stackTriggers) {//Loop through each triggers in stack

            try {
                cTrigger.setTime(sdf.parse(eachTrigger.getDate())); //Convert Trigger date String to Date object
                cCurrentDate.setTime(sdf.parse(currentDate)); //Convert Current date String to Date object
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
            
            if (cTrigger.getTime().compareTo(cCurrentDate.getTime()) < 0 | cTrigger.getTime().compareTo(cCurrentDate.getTime()) == 0) { 
//If triggerID matches current date (==0) or Trigger date is before Current date (<0)
                JOptionPane.showMessageDialog(null, eachTrigger.getDescription() + "'s Transaction will be added automatically.");
                
                Transaction triggerTransaction = new Transaction(eachTrigger.getLoggedInUser(), eachTrigger.getDate(),
                        eachTrigger.getDescription(), (Double) eachTrigger.getAmount(), eachTrigger.getTriggerType()); 
//Trigger bata Transaction banaune    

                panelTriggerDetails.main_Dashboard.panelTransactionDetails.stackTransactions.add(triggerTransaction); 
//Add trigger transaction to TransactionStack
                panelTriggerDetails.main_Dashboard.panelTransactionDetails.transacDealer.inputToFile
        (triggerTransaction.getLoggedInUser(), null, "write");
                panelTriggerDetails.main_Dashboard.panelTransactionDetails.stackTransactions.populateTable                                                
        (panelTriggerDetails.main_Dashboard.panelTransactionDetails);
                panelTriggerDetails.main_Dashboard.panelTransactionDetails.updatePanelNetSummary();
                        //Accessing Panel Transaction to update Panel NEt Summary data

                cTrigger.add(Calendar.MONTH, 1); //Increase date by 1 month
                eachTrigger.setDate(sdf.format(cTrigger.getTime())); //Change trigger date to new(increased by 1 month) date
                stackTriggers.populateTable(panelTriggerDetails);
                addToFile(eachTrigger.getLoggedInUser(), null, "write");
                
            }
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        isMatchTriggerDate();
    }
}
