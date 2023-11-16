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

import java.util.ArrayList;

public class Account {
    /* Account ID integer number */
    private int ID;
    /* Double for the accounts overall balance */
    private double balance;
    /* Double for accounts available funds */
    private double avaFunds;
    /* Holds the name of the account owner  */
    private String name;
    /* Holds the items won in auctions */
    private ArrayList<Item> wonItems;
    
    /*
     * Default constructor to make these account
     * objects without assigning intial values to
     * it.
     */
    public Account() {
        // Default constructor
    }
    
    /*
     * Constructor with multiple parameters that must
     * be met in order to make a new object.
     * 
     * @param account ID
     * @param account balance
     * @param account name
     */
    public Account(int ID, double balance, String name) {
        this.ID = ID;
        this.balance = balance;
        this.name = name;
    }
    
    /*
     * Getter for the account ID.
     * 
     * @return ID number to get
     */
    public int getID() {
        return ID;
    }
    
    /*
     * Setter for the account ID.
     * 
     * @param ID number to set
     */
    public void setID(int ID) {
        this.ID = ID;
    }
    
    /*
     * Getter for the account balance.
     * 
     * @return account balance
     */
    public double getBalance() {
        return balance;
    }
    
    /*
     * Setter for the account balance.
     * 
     * @param balance to set
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }
    
    /*
     * Getter for available funds.
     * 
     * @return available funds
     */
    public Double getAvailableFunds() {
        return avaFunds;
    }
    
    /*
     * Setter for available funds.
     * 
     * @param funds to set
     */
    public void setAvailableFunds(double avaFunds) {
        this.avaFunds = avaFunds;
    }
    
    /*
     * Deduct from available funds with a given amount.
     * This handles insufficent funds as well.
     * 
     * @param balance to deduct
     */
    public void deductAvaliable(double duductAmt) {
        if(avaFunds > 0 || avaFunds - duductAmt > 0) {
            avaFunds -= duductAmt;
        }
        else {
            System.out.println("INSUFFICENT FUNDS");
        }
    }
    
    /*
     * Withdrawal from full balance of the account.
     * This handles insufficent funds too.
     * 
     * @param balance to withdraw
     */
    public void withdraw(double withdrawAmt) {
        if(balance > 0 || balance - withdrawAmt > 0) {
            balance -= withdrawAmt;
        }
        else {
            System.out.println("INSUFFICENT FUNDS");
        }
    }
    
    /*
     * Deposit funds in the account balance.
     * 
     * @param amount to deposit
     */
    public void deposit(Double depositAmt) {
        balance += depositAmt;
    }
    
    /*
     * Getter for the account holders name.
     * 
     * @return account holders name
     */
    public String getName() {
        return name;
    }
    
    /*
     * Setter for the account holders name.
     * 
     * @param name to set to account
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /*
     * Add to the overall list of won auction items
     */
    public void addWonItems(Item e) {
        wonItems.add(e);
    }
    
    /*
     * Simple toString to print out the whole accounts
     * details.
     */
    public String toString() {
        String s = "";
        s = "Name: " + name + "\n" +
            "ID: " + ID + "\n" +
            "Total Balance: " + balance + "\n" +
            "Available Funds: " + getAvailableFunds() + "\n" +
            "Won Items: \n";
        for(int i = 0; i < wonItems.size(); i++) {
            s += "" + wonItems.get(i) + "\n";
        }
        return s;
    }
}
