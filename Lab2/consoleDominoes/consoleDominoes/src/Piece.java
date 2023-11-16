/*
 * Name: Damian Franco
 *       101789677
 *       CS 351 - 004
 * 
 * Project: Mexican Train Dominoes
 * Version: Console V4
 */
public class Piece {
    /* Left value integer */
    public int leftVal;
    /* Right value integer */
    public int rightVal;
    
    /* Constructor for the Piece object */
    public Piece(int leftVal, int rightVal) {
        this.leftVal = leftVal;
        this.rightVal = rightVal;
    }
    
    /* Getter for the left value of the dominoe */
    public int getLeftVal() {
        return leftVal;
    }
    
    /* Getter for the right value of the dominoe */
    public int getRightVal() {
        return rightVal;
    }
    
    /* Setter for the left value */
    public void setLeftVal(int leftVal) {
        this.leftVal = leftVal;
    }
    
    /* Setter for the right value */
    public void setRightVal(int rightVal) {
        this.rightVal = rightVal;
    }
    
    /* String representation of the dominoe */
    public String toString() {
        return "[" + leftVal +", " + rightVal + "]";
    }
}
