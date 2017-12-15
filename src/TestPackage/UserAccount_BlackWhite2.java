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
public class UserAccount_BlackWhite2 {

    public static void main(String[] args) {
        UserAccount ac = new UserAccount("", "", "Romisha", "");//dummy ac
        //Constructor calls encryptPassword method
        System.out.println("Encrypted Password: " + (ac.password));
        System.out.println("Decrypted Password: " + ac.decipher_Password());
    }
}




