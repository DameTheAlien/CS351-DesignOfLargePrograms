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

import java.net.*;
import java.util.*;
import java.io.*;

public class Bank {  
    /* A list of all the auction houses */
    private ArrayList<ItemList> houses = new ArrayList<ItemList>();
    
    /*
     * This function adds a house to the server so that
     * any client can access the auction houses.
     * 
     * @param AuctionHouse object (ItemList)
     */
    public void addHouse(ItemList h) {
        h.setHouseNum(houses.size());
        houses.add(h);
    }
    
    /*
     * Set the whole auction list to the current list of houses
     * in and update every time an action is entered.
     * 
     * @param list of auction houses
     */
    public void setItemList(ArrayList<ItemList> houses) {
        this.houses = houses;
    }
    
    /*
     * Starts the whole server and prints out if the server has
     * started. Then it starts an object of the server thread so
     * that each client has an individual thread that concurrently
     * runs. 
     * 
     * @param port number
     */
    public void startServer(int port) {
        Socket s = null;
        ServerSocket ss2 = null;
        System.out.println("SERVER HAS BEEN CREATED!!!");
        System.out.println("SERVER IS WAITING...");
        try {
            ss2 = new ServerSocket(port);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        while(true) {
            try {
                s = ss2.accept();
                System.out.println("CLIENT CONNECTED");
                ServerThread st = new ServerThread(s);
                //Update Auction houses
                setItemList(houses);
                st.setItemList(houses);
                //Start the server thread
                st.start();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * Main function to run this part of the program, it
     * asks for the port number and starts the server after
     * they are entered.
     */
    public static void main(String[] args) {
        System.out.println("Please enter a port number of the server to start:");
        Scanner sc = new Scanner(System.in);
        int portNum = sc.nextInt();
        Bank b = new Bank();
        b.startServer(portNum);
    }
   
}
