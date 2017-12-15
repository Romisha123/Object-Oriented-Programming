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
public class UserAccount_BlackWhite3 {

    public static void main(String[] args) {
        UserAccount ac = new UserAccount("Romisha", "Romisha", "romisha123", "Home");
        System.out.println(ac.name);
        System.out.println(ac.userName);
        System.out.println(ac.password);
        System.out.println(ac.accountType);
    }
}









