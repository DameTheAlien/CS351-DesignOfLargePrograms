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

public class AuctionHouse {
    /* List of the current auction house objects */
    private ArrayList<Item> itemList = new ArrayList<Item>();
    /* Object of the current auction house */
    private ItemList currItems = new ItemList();
    /* Socket to the server conncetion */
    private Socket sock = null;
    /* Data output stream to write to the server */
    private DataOutputStream os = null;
    /* Data input stream to read from the server */
    private DataInputStream is = null;
    
    /*
     * This method is just makes a bunch of new item
     * objects and them set the paramters to each of 
     * them. Just a bunch of random items being set up
     * to be sent to the server.
     */
    public void setupItems() {
        Item car = new Item();
        Item trip = new Item();
        Item air = new Item();
        Item basket = new Item();
        Item pizza = new Item();
        Item art = new Item();
        
        // Car set up
        car.setName("Exotic Sports Car (Model)");
        car.setID("d");
        car.setDescription("It's a brand new 2021 Bugatti Chiron! Usually goes for "
                + "$3.6 Million Dollars! This is just a model of that car though.");
        car.setPrice(250.0);
        
        // Trip set up
        trip.setName("A Beautiful Vacation");
        trip.setID("e");
        trip.setDescription("A marvelous vacation to the Bahamas! All expenses paid!");
        trip.setPrice(10.0);
        
        // Kanye air set up
        air.setName("Air from Kayne West's concert");
        air.setID("f");
        air.setDescription("It's air! It's in a plastic bag! And it went for $60k "
                + "(this is real) previously!");
        air.setPrice(1000.0);
        
        // Basket set up
        basket.setName("Handmade Gift Basket");
        basket.setID("g");
        basket.setDescription("A homemade gift basket with all sort of treats and goodies!");
        basket.setPrice(20.0);
        
        // Pizza set up
        pizza.setName("A Slice or Pizza");
        pizza.setID("h");
        pizza.setDescription("Delicious slice of pizza everyday for a year from "
                + "any pizza place of your choosing!");
        pizza.setPrice(100.0);
        
        // Art set up
        art.setName("Classical Art Piece");
        art.setDescription("A classical art piece painted by Bob Ross!");
        art.setID("i");
        art.setPrice(50.0);
        
        // Add all to the list of items
        itemList.add(car);
        itemList.add(trip);
        itemList.add(air);
        itemList.add(basket);
        itemList.add(pizza);
        itemList.add(art);
    }
    
    /*
     * This will get a random item from the item
     * list and return it, nothing too special.
     * 
     * @return random item
     */
    public Item getItem() {
        Item curr = null;
        curr = itemList.get(ThreadLocalRandom.current().nextInt(0, itemList.size() - 1));
        return curr;
    }
    
    /*
     * Set the current items in the auction house and remove
     * them to not have any duplicates. This will get 
     * random items and add them all to a item list 
     * object.
     */
    public void setCurrentItems() {
        ArrayList<Item> curr = new ArrayList<Item>();
        curr.add(getItem());
        itemList.remove(curr.get(0));
        curr.add(getItem());
        itemList.remove(curr.get(1));
        curr.add(getItem());
        itemList.remove(curr.get(2));
        ItemList it = new ItemList(curr, 0);
        currItems.setItemList(curr);
        currItems.setHouseNum(0);
    }
    
    /*
     * This will send a single string of the auction
     * house information to the server. I orginally 
     * tried doing a object output stream but it was
     * ruining my whole program so I just did this.
     */
    public String oneStringHouse() {
        String str = "";
        for(int i = 0; i < currItems.getItemList().size(); i++) {
            str += "" + currItems.getItemList().get(i).getName() + "\n";
            str += "" + currItems.getItemList().get(i).getID() + "\n";
            str += "" + currItems.getItemList().get(i).getDescription() + "\n";
            str += "" + currItems.getItemList().get(i).getPrice() + "\n";
        }
        
        return str;
    }
    
    /*
     * Start of the auction house client with the 
     * address and port of the server and it will
     * read in the user input until the char 'q'
     * is inputted by the user. There is also some
     * welcoming and informative information to the
     * user that gets printed to the console.
     */
    public void startAuctionHouse(String address, int port) throws IOException {
        try {
            sock = new Socket(address, port);
            is = new DataInputStream(System.in);
            os = new DataOutputStream(sock.getOutputStream());
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        
        // User welcome to this house
        System.out.println("Client Address: " + address);
        System.out.println("Client Port: " + port);
        System.out.println("\nAuction house opened and started!\n");
        System.out.println("Can send three auctions to the bank right now!");
        System.out.println("Here are the auctions in this house:\n");
        for(int i = 0; i < currItems.getItemList().size(); i++) {
            System.out.println(i +": " + currItems.getItemList().get(i).getName());
        }
        System.out.println("\nPlease press enter to send items over to bank\n");
        
        // Handle user input
        String line = "";
        while(!line.equals("q")) {
            try {
                String a = oneStringHouse();
                line = is.readLine();
                os.writeUTF(a);
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
     * and then it fires up the auction house client.
     * It will also set up the items and set the current
     * items to send over to the server.
     */
    public static void main(String[] args) {
        AuctionHouse ah = new AuctionHouse();
        System.out.println("Please enter the address you wish to connect to:");
        Scanner sc = new Scanner(System.in);
        String add = sc.nextLine();
        System.out.println("Now please enter the the port number:");
        int port = sc.nextInt();
        
        ah.setupItems();
        ah.setCurrentItems();
        try {
            ah.startAuctionHouse(add, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
