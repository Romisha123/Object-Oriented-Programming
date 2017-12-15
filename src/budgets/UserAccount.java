/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgets;

/**
 *
 * @author Romisha Thapa
 */
public class UserAccount {

    public String name;
    public String userName;
    public String password;
    public String accountType;

    public String sharedWith;
    public String homeName;

    public UserAccount(String name, String userName, String password, String accountType) {
        this.name = name;
        this.userName = userName;
        this.password = cipher_Password(password);
        this.accountType = accountType;
    }

    //Second constructor (overloading) used while reading from CSV file
    public UserAccount(String name, String userName, String password, String accountType, boolean encrypt) {
        this.name = name;
        this.userName = userName;
        this.password = password;
        this.accountType = accountType;
    }

    private String cipher_Password(String password) {
        String cipher = "";
        char ch;
        for (int i = 0; i < password.length(); i++) {
            ch = password.charAt(i);
            ch += 1;
            cipher += ch;
        }
        return cipher;
    }

    public String decipher_Password() {
        String plainText = "";
        char ch;
        for (int i = 0; i < password.length(); i++) {
            ch = password.charAt(i);
            ch -= 1;
            plainText += ch;
        }
        return plainText;
    }
}
