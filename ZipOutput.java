/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.seven.bii;

import java.io.File;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.EncryptionMethod;



/**
 *
 * @author 7 Bii
 */

 

public class ZipOutput {
    
    //encrypts user folder
    public static void Encrypt(String userFolder, String userPassword) throws ZipException 
    {
        //Parametes to zip to
        ZipParameters zipParameters = new ZipParameters();
        zipParameters.setEncryptFiles(true);
        zipParameters.setCompressionLevel(CompressionLevel.MAXIMUM);
        zipParameters.setEncryptionMethod(EncryptionMethod.AES);
        
        //tries zipping
        try{
            ZipFile zipFile = new ZipFile("/home/ntu-user/Users/"+userFolder+".zip", userPassword.toCharArray());
            zipFile.addFolder(new File("/home/ntu-user/Users/" + userFolder), zipParameters);
            File index = new File("/home/ntu-user/Users/" + userFolder);
            if (index.exists()) {   //checks if folder exists
                index.delete();
            }
            //deletes weird guest zip
            File myObj = new File("/home/ntu-user/Users/guest.zip"); 
            myObj.delete();
        }catch(Exception e) 
        {
            e.printStackTrace();
        }
        
    }
        //decrypts user folder
        public static void Decrypt(String userFolder, String userPassword) throws ZipException{
            try{
            ZipFile zipFile = new ZipFile("/home/ntu-user/Users/"+userFolder+".zip", userPassword.toCharArray());
            zipFile.extractAll("/home/ntu-user/Users/");
            //deletes weird user subfolder
            File myObj = new File("/home/ntu-user/Users/" + userFolder + ".zip"); 
            myObj.delete();
            File index = new File("/home/ntu-user/Users");
            if (index.exists()) {
                index.delete();
            }
        }
        catch(ZipException e) 
        {
            System.out.println("Failed to decrypt");
            e.printStackTrace();
            
        }
        }
        
            
    
}