/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.seven.bii;

/**
 *
 * @author 7 Bii
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.Arrays;


public class ExternalCmd {
    static String outline = "";
    public static String externalCmdSort(String input) throws IOException { 
        //Checks list of commands
        while (!false){
            String [] parsedCommand = input.split(" ");

            String[] listOfCommands = {"ls", "cp", "mv", "mkdir", "rmdir", "ps", "which"};
            boolean valid = Arrays.stream(listOfCommands).anyMatch(Array.get(parsedCommand, 0)::equals);
            //if not a command returns false
            if ( !valid ){
                    return "false";
            }else {
                //if command found uses process
                ProcessBuilder pb = new ProcessBuilder();
                pb.command(parsedCommand);
                pb.directory(new File(System.getProperty("user.dir"))); //sets process builder directory to current directory
                Process process = pb.start();
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()))) {
                    String line;
                    outline="";
                    while ((line = reader.readLine()) != null) {
                        //prints output
                        outline +=" "+line;
                        System.out.println(outline);
                        
                    }
                }
            }return outline;
        }
    }
}
    