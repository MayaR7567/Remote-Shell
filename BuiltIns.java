/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.seven.bii;

import java.io.File;
import java.lang.reflect.Array;
import java.util.Arrays;
import static com.mycompany.seven.bii.ZipOutput.Encrypt;
import static com.mycompany.seven.bii.ZipOutput.Decrypt;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.lingala.zip4j.exception.ZipException;
/**
 *
 * @author 7 Bii
 */
public class BuiltIns {
    //User Details
    private static String Username;
    private static String Password;
    public static String[][] twoDStringArray = {{"root","root","super"},{"maya","maya123","super"},{"shruti","shruti123", "user"}};

   //Validate if builtin command
   public static String builtInSort(String input) {
    
    while (true){
        //take input and check against list of commands
        String [] parsedCommand = input.split(" ");
        String[] listOfCommands = {"cd", "showDir", "login", "whoAmI", "logoff","chPass"};
        boolean valid = Arrays.stream(listOfCommands).anyMatch(Array.get(parsedCommand, 0)::equals);
        if ( !valid ){
                        return "false"; //if no command matches
                }else{
            //Match the found command
            switch(parsedCommand[0]) {
            case "cd":
                return changeDirectory(input);

            case "showDir":
                String dir = System.getProperty("user.dir");
                return dir;
            case "login":
                //splits login details
                String loginResult = "";
                String loginDetails = input.substring(input.indexOf(' ') + 1);
                String details = loginDetails + " ";
                String [] userPass = details.split(" ");
                String user = userPass[0];
                String pass = userPass[1];
                System.out.println(login(user,pass));   //login method
                //validate login status
                if(login(user,pass) == true){
                try {
                    //if true decrypt
                    Decrypt(Username, Password);
                    loginResult = "Login Successful. Hello " + Username;
                } catch (ZipException ex) {
                    Logger.getLogger(BuiltIns.class.getName()).log(Level.SEVERE, null, ex);
                }
                }else{
                    loginResult = "Login Failed";
                }
                return loginResult;

            case "whoAmI":
                return Username;
            case "logoff":
                {
                    try {
                        //encrypt user folder
                        Encrypt(Username, Password);
                    } catch (ZipException ex) {
                        Logger.getLogger(BuiltIns.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    String validDir;
                    System.setProperty("user.dir", "/home/ntu-user/Users/"); //to go back to home directory
                    validDir = System.setProperty("user.dir", "/home/ntu-user/Users/");
                }
                Username = "guest";
                return "Logged out. Guest Signed in";

          }

        }   
    }
   }
   
   //Validate user credentials
   public static boolean login(String username, String password){
       //Booleans
       boolean loggedin = false;
       boolean userbool = false;
       boolean passbool = false;
       //tmp passwords
       String tmpuser = "";
       String tmppass = "";
       int row;
       //Loop to check username
       for(row = 0;row<3; row++){
           tmpuser = twoDStringArray[row][0];
           //If username found make userbool true
           if(username.equals(tmpuser)){
               userbool = true;
               break;
           }
        }
        //checks password to username
        tmppass = twoDStringArray[row][1];
         if(password.equals(tmppass)){
             passbool = true;
         }
        //if username and password match 
       if(userbool == true && passbool == true){
           Username = username;
           Password = password;
           loggedin = true;
       }
       return loggedin;
   }
   
   //change directory
    private static String changeDirectory(String input) {
        String validDir = null;
        String dirInput = input.substring(input.indexOf(' ') + 1);
        System.out.println(System.getProperty("user.dir"));
        //goes to home directory if ..
        if("..".equals(dirInput)){
            System.setProperty("user.dir", "/home/ntu-user/Users/");
            validDir = System.setProperty("user.dir", "/home/ntu-user/Users/");
        }else{
            //checks if valid directory
            File tmpDir = new File("/home/ntu-user/Users/" + dirInput);
            boolean exists = tmpDir.exists();
            if(exists){
                //if valid sets to working directory
                validDir = System.setProperty("user.dir", "/home/ntu-user/Users/" + dirInput);
            }else{
                validDir = ("There is no directory called /home/ntu-user/Users/" + dirInput);
            }
        }
        return validDir;
        
    }


}
 



