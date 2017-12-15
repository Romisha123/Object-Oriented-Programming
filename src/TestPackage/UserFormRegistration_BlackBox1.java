/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TestPackage;

import budgets.UserFormRegistration;
import java.util.LinkedList;
import javax.swing.JFrame;

/**
 *
 * @author Romisha Thapa
 */
public class UserFormRegistration_BlackBox1 extends JFrame {

    public static void main(String[] args) {

        UserFormRegistration fr = new UserFormRegistration();
        LinkedList<String> userNames = new LinkedList();
        userNames = fr.readAllUserNames();

        for (String userName : userNames) {
            System.out.println(userName);
        }
    }

}
