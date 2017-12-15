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
public class Trigger {

    private String triggerID;
    private String loggedInUser;
    private String date;
    private String description;
    private double amount;
    private String triggerType;

    public Trigger(String loggedInUser, String date, String description, double amount, String triggerType) {
        this.triggerID = loggedInUser + getUniqueID();
        this.loggedInUser = loggedInUser;
        this.date = date;
        this.description = description;
        this.amount = amount;
        this.triggerType = triggerType;
    }

    //constructor overloaded for reading from file
    public Trigger(String triggerID, String loggedInUser, String date, String description,
            double amount, String triggerType, boolean x) {                                                                          
        this.triggerID = triggerID;
        this.loggedInUser = loggedInUser;
        this.date = date;
        this.description = description;
        this.amount = amount;
        this.triggerType = triggerType;
    }

    private String getUniqueID() {
        LocalDateTime localDateAndTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-SSS");
        return localDateAndTime.format(formatter);
    }

    public String getTriggerID() {
        return triggerID;
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

    public String getTriggerType() {
        return triggerType;
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

    public void setTriggerType(String triggerType) {
        this.triggerType = triggerType;
    }

    public String getLoggedInUser() {
        return loggedInUser;
    }

}
