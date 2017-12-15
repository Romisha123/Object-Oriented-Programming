/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgets;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Romisha Thapa
 */
public class Transaction {

//    public static void setRandom(AtomicLong aRandom) {
//        random = aRandom;
//    }
    private String transactionID;
    private String loggedInUser;
    private String date;
    private String description;
    private double amount;
    private String transactionType;

//    private static AtomicLong random = new AtomicLong();
//class level variable. For making every transaction object's ID unique. Check out quick summary.
    public Transaction(String loggedInUser, String date, String description, double amount, String transactionType) {
        this.transactionID = loggedInUser + getUniqueID();
        this.loggedInUser = loggedInUser;
        this.date = date;
        this.description = description;
        this.amount = amount;
        this.transactionType = transactionType;
    }

    //constructor overloaded for reading from file
    public Transaction(String transactionID, String loggedInUser, String date, String description, 
            double amount, String transactionType, boolean x) {
        this.transactionID = transactionID;
        this.loggedInUser = loggedInUser;
        this.date = date;
        this.description = description;
        this.amount = amount;
        this.transactionType = transactionType;
    }

    private String getUniqueID() {
//        return String.valueOf(random.getAndIncrement());//Get previous valye of "random" & increment by 1.
//Thus generating unique ID for each transactions.
        LocalDateTime localDateAndTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-SSS");
        return localDateAndTime.format(formatter);
    }

    public String getTransactionID() {
        return transactionID;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getLoggedInUser() {
        return loggedInUser;
    }

}
