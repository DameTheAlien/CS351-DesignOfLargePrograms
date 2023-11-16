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

public class Item {
    /* Name of the item */
    private String name;
    /* ID number of the item */
    private String ID;
    /* Description of the item */
    private String desc;
    /* Inital price of the item */
    private double price;
    
    /*
     * Default constructor to make these account
     * objects without assigning intial values to
     * it.
     */
    public Item() {
        // Default constructor
    }
    
    /*
     * Setter for the item name.
     * 
     * @param item name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /*
     * Getter for the item name.
     * 
     * @return item name
     */
    public String getName() {
        return name;
    }
    
    /*
     * Setter for the item ID.
     * 
     * @param item ID to set
     */
    public void setID(String ID) {
        this.ID = ID;
    }
    
    /*
     * Getter for the item ID.
     * 
     * @return item ID
     */
    public String getID() {
        return ID;
    }
    
   /*
    * Setter for the item description.
    * 
    * @param description string
    */
    public void setDescription(String desc) {
        this.desc = desc;
    }
    
    /*
     * Getter for the item description.
     * 
     * @return description string
     */
    public String getDescription() {
        return desc;
    }
    
    /*
     * Setter for item price.
     * 
     * @param price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }
    
    /*
     * Getter for item price
     * 
     * @return price to get
     */
    public double getPrice() {
        return price;
    }
    
    /*
     * Simple to string for all of the items
     * parameters.
     * 
     * @return toString representation
     */
    public String toString() {
        String rep = "";
        rep = " - Item name: " + name + "\n" +
              " - " +  desc + "\n" +
              " - Starting price: " + price + "\n";
        return rep;
    }
}
