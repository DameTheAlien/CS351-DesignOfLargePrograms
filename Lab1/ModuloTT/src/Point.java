/**
 * CS 351 - 004
 * 
 * NAME: Damian Franco 
 *       dfranco24@unm.edu
 *       101789677
 * 
 * LAB: Modulo Times Tables
 */
public class Point {
    /* Holds the value of our Point */
    public int value;
    /* Holds the X coordinate of our Point */
    public double x;
    /* Holds the Y coordinate of our Point */
    public double y;

    /*
     * Simple constructor for the Point object.
     * 
     * @param int value Value of our Point
     * @param double x x-coordinate of our Point
     * @param double y y-coordinate of our Point
     */
    public Point(int value, double x, double y) {
        this.value = value;
        this.x = x;
        this.y = y;
    }
    
    /*
     * Getter that return the value of the point.
     * 
     * @return int of the value of the point
     */
    public int getValue() {
        return value;
    }
    
    /*
     * Getter that return the y coordinate of the point.
     * 
     * @return double of the x coordinate of the point
     */
    public double getX() {
        return x;
    }
    
    /*
     * Getter that return the y coordinate of the point.
     * 
     * @return double of the x coordinate of the point
     */
    public double getY() {
        return y;
    }
    
    /*
     * toString method used for mostly testing reasons to 
     * see if I was on the right track setting up my 
     * Point objects value, x and y coordinates.
     * 
     * @return simpt toString of the point
     */
    public String toString() {
        return "Value = " + value + "\n" + 
                "(" + x + ", " + y + ")";
    }
}
