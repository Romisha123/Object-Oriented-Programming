/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TestPackage;

import budgets.Trigger;

/**
 *
 * @author Romisha Thapa
 */
public class Trigger_BlackWhite1 {

    public static void main(String[] args) {
        Trigger tr = new Trigger("Romisha", "2016/05/10", "Salary", 66000.0, "Income");
        System.out.println("ID: " + tr.getTriggerID());
        System.out.println("Username: " + tr.getLoggedInUser());
        System.out.println("Date: " + tr.getDate());
        System.out.println("Description: " + tr.getDescription());
        System.out.println("Amount: " + tr.getAmount());
    }
}



