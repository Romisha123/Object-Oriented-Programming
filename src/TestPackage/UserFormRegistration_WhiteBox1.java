/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TestPackage;

import budgets.UserFormRegistration;

/**
 *
 * @author Romisha Thapa
 */
public class UserFormRegistration_WhiteBox1 {

    public static void main(String[] args) {
        UserFormRegistration fr = new UserFormRegistration();
        //Test case 1 
        fr.writeConnections("Home");
        //Test case 2 
        fr.writeConnections("Discretionary");
    }
}
