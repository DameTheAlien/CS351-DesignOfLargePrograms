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
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class ServerThread extends Thread {
    /* Socket for the server*/
    private Socket s = null;
    /* Data output stream to write data */
    private DataOutputStream os = null;
    /* Data input stream to read data */
    private DataInputStream is;
    /* Account object to hold current account info */
    private Account currAccount = null;
    /* List of accounts currently on the server */
    private ArrayList<Account> accounts = new ArrayList<Account>();
    /* List of auction houses on the server */
    private ArrayList<ItemList> itemList = new ArrayList<ItemList>();
    /* Index at which the auction houses need to added at in the list */
    private int houseIndex = 0;
    /* List to hold the account info and bet user input strings */
    private String actInfo, betString;
    /* Double to keep track of the current bet */
    private Double currBet;
    /* Boolean to see if the auction was won by this thread */
    private boolean auctionWon;
    
    /*
     * Server thread constructor that holds the
     * socket of the server.
     * 
     * @param socket of the server
     */
    public ServerThread(Socket s) {
        this.s = s;
    }
    
    /*
     * Gets the current account and returns it
     * to add to overall list and keep track of
     * it.
     * 
     * @param index of the account to add at
     * @return account to get
     */
    public Account getAccount(int index) {
        return accounts.get(index);
    }
    
    /*
     * Adds the current account to the list
     * of accounts on the server.
     * 
     * @param account to add
     */
    public void addAccount(Account a) {
        accounts.add(a);
    }
    
    /*
     * Gets the current action house and
     * returns it.
     * 
     * @param index to retrieve the auction from
     * @return auction house in which to return
     */
    public ItemList getAuctionHouse(int index) {
        return itemList.get(index);
    }
    
    /*
     * Setter method for the number of auction houses
     * in the current server.
     * 
     * @param list of auction houses
     */
    public void setItemList(ArrayList<ItemList> itemList) {
        this.itemList = itemList;
    }
    
    /*
     * Adds an auction house to the list of
     * auction houses in the server.
     * 
     * @param auction house to add
     */
    public void addHouse(ItemList h) {
        h.setHouseNum(itemList.size());
        itemList.add(h);
    }
    
    /*
     * Set up of string to show to the client when they
     * are in the menu of options.
     * 
     * @return string of main menu
     */
    public String showChoices() {
        String choices =  "What would you like to do?\n" +
                          " [b] Check balance from the bank\n" +
                          " [c] Contact auction house to bid\n" + 
                          " [q] Exit the program";  
        return choices;
    }
    
    /*
     * Handles the register of the current
     * account from the current clients 
     * input. Sets all the params of the
     * current account object.
     */
    public void handleRegister() {
        currAccount = new Account();
        Scanner sc = new Scanner(actInfo);
        String args = "";
        
        args = sc.next();
        currAccount.setName(args);
        args = sc.next();
        Double balance = Double.parseDouble(args);
        currAccount.setBalance(balance);
        currAccount.setAvailableFunds(balance);
    }
    
    /*
     * This handles the checking of the current
     * balance in the account and sets it up and
     * prints it out to the client.
     */
    public void handleCheckBal() {
        String out = "";
        if(currAccount == null) {
            out = "You are not signed in! Please register with the bank first..\n" +
                  "Press ENTER to continue..."; 
            try {
                os.writeUTF(out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            out = "  Current Balance: $" + currAccount.getBalance() + "\n" +
                  "  Available Funds: $" + currAccount.getAvailableFunds() + "\n" +
                  "Press ENTER to continue..."; 
                try {
                    os.writeUTF(out);
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
    
    /*
     * Shows the available auction houses by scanning 
     * through the current list and then layouting them
     * accordingly. It will print out that there are no
     * current auction houses if there are none open. The
     * output stream will handle to sending of data.
     */
    public void showAvailable() {
        String avail = "Available Auction Houses: \n";
        for(int i = 0; i < itemList.size(); i++) {
            avail += " " + itemList.get(i).getHouseNum() + ": Auction House " +
                     itemList.get(i).getHouseNum() + " IS OPEN!!!";
        }
        avail += "\nPlease enter the ID number to "
                + "contact it and view and bid on items";
        if(itemList.size() == 0) {
            avail = "No available Auction Houses currently available!";
        }
        try {
            os.writeUTF(avail);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /*
     * This shows the auction house at the given index
     * by the user and its list of current items. It
     * will then output it to the client.
     * 
     * @param index of the auction house
     */
    public void showAuctionHouse(int index) {
        String auction = "Here is Auction House " + index + "'s Current Items\n";
        for(int j = 0; j < itemList.get(index).getItemList().size(); j++) {
            auction += itemList.get(index).getItemList().get(j).getID() + ": \n" 
                    + itemList.get(index).getItemList().get(j).toString();
        }
        auction += "\nPlease enter the ID number to bet on an auction to contact it:\n"
                + "Or press ENTER to return to the menu:";
        try {
            os.writeUTF(auction);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /*
     * Handles the choice based of the clients
     * input. This handles the output of many 
     * prompts for the user to follow based off
     * of the menu inputs.
     * 
     * @param string of the users choice
     */
    public void handleChoices(String choice) {
        switch(choice) {
            // Handles main menu actions
            case "r":
                if(currAccount == null) {
                    String out = "";
                    out = "Please enter your name";
                    
                    out += " & How much money do you want to deposit below:";
                    try {
                        os.writeUTF(out);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    String reg = "AYE " + currAccount.getName() + "! YOU ALREADY REGISTERED!\n" +
                                 "Press ENTER to continue...";
                    try {
                        os.writeUTF(reg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case "b":
                handleCheckBal();
                break;
            case "c":
                showAvailable();
                break;
            // Handles auction house showing
            case "0":
                showAuctionHouse(0);
                break;
            case "1":
                showAuctionHouse(1);
                break;
            case "2":
                showAuctionHouse(2);
                break;
            case "3":
                showAuctionHouse(3);
                break;
            case "4":
                showAuctionHouse(4);
                break;
            case "5":
                showAuctionHouse(5);
                break;
            default:
                System.out.println("Agent entered: Not a correct choice\n");
                break;
        }
    }
    
    /*
     * The set up of the auction house according to the 
     * specifications of the string that was sent from
     * the auction house object. This scans through a 
     * string and makes a list out of it and sets up
     * the auction house object and adds it to the list
     * of available auction houses.
     * 
     * @param string of auction house specifications
     */
    public void setupAuctionHouse(String auctString) {
        if(auctString.length() > 2) {
            Scanner sc = new Scanner(auctString);
            String scanStr = "";
            
            // Set up first item
            Item i1 = new Item();
            scanStr = sc.nextLine();
            i1.setName(scanStr);
            scanStr = sc.nextLine();
            i1.setID(scanStr);
            scanStr = sc.nextLine();
            i1.setDescription(scanStr);
            scanStr = sc.nextLine();
            i1.setPrice(Double.parseDouble(scanStr));
            
            // Set up second item
            Item i2 = new Item();
            scanStr = sc.nextLine();
            i2.setName(scanStr);
            scanStr = sc.nextLine();
            i2.setID(scanStr);
            scanStr = sc.nextLine();
            i2.setDescription(scanStr);
            scanStr = sc.nextLine();
            i2.setPrice(Double.parseDouble(scanStr));
            
            // Set up third item
            Item i3 = new Item();
            scanStr = sc.nextLine();
            i3.setName(scanStr);
            scanStr = sc.nextLine();
            i3.setID(scanStr);
            scanStr = sc.nextLine();
            i3.setDescription(scanStr);
            scanStr = sc.nextLine();
            i3.setPrice(Double.parseDouble(scanStr));
            
            // Set up the new list of items
            ArrayList<Item> newList = new ArrayList<Item>();
            newList.add(i1);
            newList.add(i2);
            newList.add(i3);
            
            // Set up the auction house
            ItemList i = new ItemList();
            i.setItemList(newList);
            i.setHouseNum(houseIndex);
            houseIndex++;
            itemList.add(i);
        }
        else {
           //Do nothing, needs to be handled
        }
      
    }

    /*
     * This handles the bet option. It will take in
     * that string of how much the client chooses to
     * bet on and which object they choose to bet on.
     * It will then get that object from the auction
     * house and then it will freeze the amount in the
     * account of the available balances. Then if the 
     * auction is won after 10 seconds, the auction will
     * end and will be won. 
     */
    public void handleBet() {
        Scanner sc = new Scanner(betString);
        String id = sc.next();
        Double amt = Double.parseDouble(sc.next());
        
        for(int i = 0; i < itemList.get(0).getItemList().size(); i++) {
            if(itemList.get(0).getItemList().get(i).getName().equals(id)) {
                currBet = amt;
                Double oldAmt = currAccount.getBalance();
                currAccount.deductAvaliable(currBet);
                try {
                    Thread.sleep(10000);
                    auctionWon = true;
                    if(auctionWon) {
                        String out = "";
                        out = "AUCTION WON! Your item has been added to your account";
                        try {
                            os.writeUTF(out);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        currAccount.withdraw(currBet);
                        currAccount.addWonItems(itemList.get(0).getItemList().get(i));
                    }
                    else {
                        currAccount.setAvailableFunds(oldAmt);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        
    }
    
    /*
     * Runs the current thread and constantly reads 
     * the user input from the client until the 'q'
     * character has been entered. It will then close
     * everything accordingly when the q is entered.
     */
    public void run() {
        try {
            is = new DataInputStream(s.getInputStream());
            os = new DataOutputStream(s.getOutputStream());
        }
        catch(IOException e){
            e.printStackTrace();
        }
        
        String line = "";
        while(!line.equals("q")) {
            try {
                os.flush();
                os.writeUTF(showChoices());
                line = is.readUTF();
                
                // Auction house input check
                if(line.length() > 50 && line.contains("\n")) {
                    setupAuctionHouse(line);
                }
                else {
                    // Do nothing
                }
                
                // Client bet and regristration input check
                if(line.contains("d ") && !line.contains("\n")) {
                    betString = line;
                    handleBet();
                }
                else if(line.contains("e ") && !line.contains("\n")) {
                    betString = line;
                    handleBet();
                }
                else if(line.contains("f ") && !line.contains("\n")) {
                    betString = line;
                    handleBet();
                }
                else if(line.contains("g ") && !line.contains("\n")) {
                    betString = line;
                    handleBet();
                }
                else if(line.contains("h ") && !line.contains("\n")) {
                    betString = line;
                    handleBet();
                }
                else if(line.contains("i ") && !line.contains("\n")) {
                    betString = line;
                    handleBet();
                }
                else if(line.contains(" ") && !line.contains("\n")) {
                    actInfo = line;
                    handleRegister();
                }
                // Handles all responses from the client
                handleChoices(line);
                System.out.println("Client response (Agent " + this.getId() +  ") : "  + line);
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        }
        
        System.out.print("Agent " + this.getId() + " Closed");
        try {
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
