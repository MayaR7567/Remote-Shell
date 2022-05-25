/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.seven.bii;

import static com.mycompany.seven.bii.BuiltIns.builtInSort;
import static com.mycompany.seven.bii.ExternalCmd.externalCmdSort;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 7 Bii
 */
public class EchoHTTPServer extends Thread {
    
    private final int PORT = 2224;
    //set home directory 
    String promptMessage = "Prompt$";
    String homeDir = System.setProperty("user.dir", "/home/ntu-user/Users/");
    String output;
    int numRows = 2;
    
    
    //Main class
    public static void main(String[] args) {
        //Starting new server
        EchoHTTPServer gtp = new EchoHTTPServer();
        gtp.start();
        
    }

    //Runtime code
    @Override
    public void run() {
        //try opening server socket
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("Echo Server listening port: " + PORT);
            boolean shudown = true;
            String currentDir = System.getProperty("user.dir"); //get current directory
            //while its not shutdown keep running
            while (shudown) {
                //update prompt message with directory
                //try socket connection
                try (Socket socket = server.accept()) {
                    //Get input and output streams
                    InputStream is = socket.getInputStream();
                    try (PrintWriter out = new PrintWriter(socket.getOutputStream())) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(is));
                        //Read from server
                        String line;
                        line = in.readLine();
                        String auxLine = line;
                        line = "";
                        // looks for post data
                        int postDataI = -1;
                        while ((line = in.readLine()) != null && (line.length() != 0)) {

                            System.out.println(line);
                            if (line.contains("Content-Length:")) {
                                postDataI = Integer.parseInt(line
                                        .substring(
                                                line.indexOf("Content-Length:") + 16,
                                                line.length()));
                            }
                        }
                        //Assigns post data
                        String postData = "";
                        for (int i = 0; i < postDataI; i++) {
                            int intParser = in.read();
                            postData += (char) intParser;
                        }
                        // replace + by " "
                        int index=postData.indexOf('+');
                        while(index>-1){
                            postData = postData.substring(0, index) + ' ' + postData.substring(index + 1);
                            index=postData.indexOf('+');
                        }

                        //Split postdate from user= to get pure input
                        String post = postData.substring(postData.indexOf('=') + 1);
                        
                        //HTML for http server
                        out.println("HTTP/1.0 200 OK");
                        out.println("Content-Type: text/html; charset=utf-8");
                        out.println("Server: MINISERVER");
                        // this blank line signals the end of the headers
                        out.println("");
                        // Design Page
                        out.println(line);
                        out.println("<H1>Group 7Bii - Remote Shell</H1>");
                        //out.println("<p style=\"margin-left:5px\">GET->" + auxLine + "</H1>");
                        currentDir = System.getProperty("user.dir");
                        //Check if valid command
                        promptMessage = currentDir+"$ ";
                        out.println("<p style=\"margin-left:5px\" >" + "<p style=\"color:blue\">"+ promptMessage + " " + post + "</p>");
                        if("false".equals(externalCmdSort(post))){
                            if("false".equals(builtInSort(post))){
                                output = "Error: Command " + post + "was not found";
                            }else{
                                currentDir = System.getProperty("user.dir");    //get current directory
                                promptMessage = currentDir+" $ ";
                                output = builtInSort(post);
                            }
                        }else{
                            output = externalCmdSort(post);
                        }
                        //display form
                        out.println("<p style=\"margin-left:5px\">" +"<p style=\"color:green\"> Output " + output + "</H1>");
                        out.println("<form id=\"form\" name=\"input\" action=\"imback\" method=\"post\">");
                        out.println("<p style=\"color:blue\">"+ promptMessage + "<input type=\"text\" name=\"input\"><input id=\"submitButton\"type=\"submit\" value=\"Submit\">");
                        out.println("</form>");
                        //display help
                        out.println("<button onclick=\"toggleText()\"> Toggle Help</button>");
                        out.println("<p id=\"Myid\">"
                                + "Name : Format : Description<br>"
                                + "<br>"
                                + "ls : ls : List of current directory<br>"
                                + "<br>"
                                + "cp : cp src target : Copy Files from src to target<br>"
                                + "<br>"
                                + "mv : mv src target : Move file from src to target<br>"
                                + "<br>"
                                + "mkdir : mkdir <name> : Creates new directory<br>"
                                + "<br>"
                                + "rmdir : rmdir <name> : Removes directory<br>"
                                + "<br>"
                                + "ps : ps : Reports a snapshot ofthe current processes<br>"
                                + "<br>"
                                + "which : which : Shows the location of a command<br>"
                                + "<br>"
                                + "login : login user pass : Login into a user account, default username and password are 'root'<br>"
                                + "<br>"
                                + "logoff : logoff : Logoff from the current account<br>"
                                + "<br>"
                                + "cd : cd src target : Change current directory<br>"
                                + "<br>"
                                + "showDir : showDir : Displays current directory<br>"
                                + "<br>"
                                + "help : help : Toggle help button displays the command help</p>");
                        out.println("<script>\n" +
                            "function toggleText(){\n" +
                            "  var x = document.getElementById(\"Myid\");\n" +
                            "  if (x.style.display == \"none\") {\n" +
                            "    x.style.display = \"block\";\n" +
                            "  } else {\n" +
                            "    x.style.display = \"none\";\n" +
                            "  }\n" +
                            "}\n" +
                            "</script>");
                        //if your get parameter contains shutdown it will shutdown

                        if (auxLine.contains("?shutdown")) {
                            shudown = false;
                        }
                    } 

                }

            }
            } catch (IOException ex) {
            Logger.getLogger(EchoHTTPServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
