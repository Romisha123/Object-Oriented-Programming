/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgets;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author Romisha Thapa
 */
public class TransacDealer {

    StackTransactions stackTransactions;
    PanelTransactionDetails panelTransactionDetails;

    TransacDealer(StackTransactions stackTransactions, PanelTransactionDetails panelTransactions) {
        this.stackTransactions = stackTransactions;
        this.panelTransactionDetails = panelTransactions;
    }

    void inputToFile(String toWritefileName, String toEditfileName, String action) {
        String selectedFileName = null;
        //Declare all writers (We need 3 writers to write to File)
        FileWriter fWriter = null;
        BufferedWriter bWriter = null;// Uses RAM to increase write performance. Less hard disk activity is good.
        PrintWriter pWriter = null;//We can use bufferwriter but PrintWriter lets us print in new line easily
        File CSVFile = null;
        try {
            if (action.equals("write")) {
                CSVFile = new File("src/txtfiles/" +
                        toWritefileName + "_Transaction.txt"); //Instantiate a file object (blank)
                if (!CSVFile.exists()) {
                    CSVFile.createNewFile(); //Create a blank notepad file (.txt format)if file doesn't exist.
                }
                selectedFileName = toWritefileName;
            } else if (action.equals("update")) {
                CSVFile = new File("src/txtfiles/" + 
                        toEditfileName + "_Transaction.txt"); //Instantiate a file object (blank)
                if (!CSVFile.exists()) {
                    CSVFile.createNewFile(); //Create a blank notepad file (.txt format)if file doesn't exist.
                }
                selectedFileName = toEditfileName;
            }
            //Initialize all writers declared above
            fWriter = new FileWriter(CSVFile);
            bWriter = new BufferedWriter(fWriter);
            pWriter = new PrintWriter(bWriter);

            for (Transaction eachTransaction : stackTransactions) { //Loop through each indices/rooms in the stack.
                if (eachTransaction.getLoggedInUser().equals(selectedFileName)) {
                    pWriter.println("|" + eachTransaction.getTransactionID() + "|" + "," + "|" + eachTransaction.getLoggedInUser() +
                    "|" + "," + "|" + eachTransaction.getDate() + "|" + "," + "|" + eachTransaction.getDescription() + "|" + "," + "|" 
                            + eachTransaction.getAmount() + "|" + "," + "|" + eachTransaction.getTransactionType() + "|");
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

    void outputFromFile(boolean clearStack) {
        if (clearStack == true) {
            stackTransactions.clear();
        }
        for (String eachStackConnection : panelTransactionDetails.main_Dashboard.stackConnections) {
            String readLine;

            //Declare all readers (We need 2 readers to read from File)
            FileReader fr = null;
            BufferedReader br = null;
            try {
                File CSVFile = new File("src/txtfiles/" + eachStackConnection + "_Transaction.txt"); //Load file (CSV notepad file) in memory

//Initialize all readers decalred above
                fr = new FileReader(CSVFile);
                br = new BufferedReader(fr);

                while ((readLine = br.readLine()) != null) {

                    String[] eachLine = readLine.split(","); //Reads each line and splits by comma

                    Transaction eachTransaction = new Transaction(eachLine[0].replace("|", ""), eachLine[1].replace("|", ""),
                            eachLine[2].replace("|", ""), eachLine[3].replace("|", ""), Double.parseDouble(eachLine[4].replace("|", "")), 
                                eachLine[5].replace("|", ""), true);
                    stackTransactions.add(eachTransaction);
                }
                stackTransactions.populateTable(panelTransactionDetails);

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
    }

//    }
}


