/*
 * Name: Damian Franco
 *       dfranco24@unm.edu
 *       101789677
 *       CS 351 - 004
 * 
 * Project: Distributed Auction (Lab 4)
 * 
 */
package DistAuct;

import java.io.*;
import java.net.*;
import java.util.*;

public class Agent {
    /* Socket to get connection to the server */
    private Socket sock = null;
    /* Data output stream to output to server */
    private DataOutputStream os = null;
    /* Data input stream to send data to the server */
    private DataInputStream is = null;
    /* Input stream to take in data from the server */
    private DataInputStream sis = null;
    
    /*
     * Starts the agent by welcoming the user and taking
     * in the user input until the char 'q' is input by
     * the user. This will send whatever data that the
     * user inputs to the server and handle it accordingly
     * on the server side. This is just a data sender with 
     * no logic in this class.
     * 
     * @param address of the server
     * @param port number
     */
    public void startAgent(String address, int port) throws IOException {
        try {
            sock = new Socket(address, port);
            os = new DataOutputStream(sock.getOutputStream());
            is = new DataInputStream(System.in);
            sis = new DataInputStream(sock.getInputStream());
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        
        System.out.println("Client Address: " + address);
        System.out.println("Client Port: " + port);
        System.out.println("\nWelcome to my scuffed auction lab!\n");
        System.out.println("My name is Watson, I am your agent for today. ");
        System.out.println("Please press ENTER to begin the program: ");
        System.out.println("IMPORTANT!!\nPlease enter the character "
                            + "'r' to register with the bank first\n");
        String line = "";
        String serverLine = "";
        while(!line.equals("q")) {
            try {
                os.flush();
                line = is.readLine();
                os.writeUTF(line);
                serverLine = sis.readUTF();
                System.out.println("Server Said: \n" + serverLine);
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        }
        
        try {
            is.close();
            os.close();
            sock.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    /*
     * Main function to run this part of the program, it
     * asks for the address of the server and a port number
     * and then it fires up the agent client.
     */
    public static void main(String[] args) {
        System.out.println("Please enter the address you wish to connect to:");
        Scanner sc = new Scanner(System.in);
        String add = sc.nextLine();
        System.out.println("Now please enter the the port number:");
        int port = sc.nextInt();
        
        Agent a = new Agent();
        try {
            a.startAgent(add, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
