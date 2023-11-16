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

import java.util.*;

public class ItemList {
    /* A list of items to hold the contents of the house */
    private ArrayList<Item> itemList;
    /* Index of the auction house */
    private int houseNumber;
    
    /*
     * Default constructor to make these account
     * objects without assigning intial values to
     * it.
     */
    public ItemList() {
        // Default constructor
    }
    
    /*
     * Constructor that takes in the item list
     * and the index of the house number.
     * 
     * @param list of items
     * @param index of current house
     */
    public ItemList(ArrayList<Item> itemList, int houseNumber) {
        this.itemList = itemList;
        this.houseNumber = houseNumber;
    }
    
    /*
     * Getter for the item list.
     * 
     * @return list of items
     */
    public ArrayList<Item> getItemList() {
        return itemList;
    }
    
    /*
     * Setter for the item list.
     * 
     * @param list of items to set
     */
    public void setItemList(ArrayList<Item> itemList) {
        this.itemList = itemList;
    }
    
    /*
     * Getter for the index of the house.
     * 
     * @return index of the house
     */
    public int getHouseNum() {
        return houseNumber;
    }
    
    /*
     * Setter for the house index number.
     * 
     * @param index number to set
     */
    public void setHouseNum(int houseNumber) {
        this.houseNumber = houseNumber;
    }
}
