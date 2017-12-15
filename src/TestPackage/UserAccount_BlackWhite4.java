/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TestPackage;

import budgets.UserAccount;

/**
 *
 * @author Romisha Thapa
 */
public class UserAccount_BlackWhite4 {

    public static void main(String[] args) {
        //Overloaded constructor where encrypt = false
        UserAccount ac = new UserAccount("Romisha", "Romisha", "Romisha123", "Home", false);
        System.out.println(ac.name);
        System.out.println(ac.userName);
        System.out.println(ac.password);
        System.out.println(ac.accountType);
    }
}




