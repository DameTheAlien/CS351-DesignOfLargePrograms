// Feel free to change the package
// The package is simply the folder path to this file
// below the src folder.
package jhaugh.timestables;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 * This class represents all of the state necessary to generate
 * the times table visualization.
 */
public class Visualization {
    private double r = 0;
    private boolean increment = true;

    private double timesTableNum;
    private double radius;

    public Visualization(double timesTableNum,
                         double radius) {
        this.timesTableNum = timesTableNum;
        this.radius = radius;
    }

    public double getR() {
        return r;
    }

    public double getRadius() {
        return radius;
    }

    public double getTimesTableNum() {
        return timesTableNum;
    }

    public void setTimesTableNum(double timesTableNum) {
        this.timesTableNum = timesTableNum;
    }

    public void incrementTimesTableNum(double stepNum) {
        // TODO: Increment the timesTableNumber by the given
        //  stepNum.
    }

    /**
     * Increment the "r" instance variable from 0 up to 1
     * then decrement back down to 0 then repeat.
     * The direction is determined by the "increment"
     * instance variable.
     * In order to properly compare doubles you need to
     * use Double.compare(d1, d2).
     * TODO: Implement the above logic
     */
    private void incrementRed() {

    }

    /**
     * Generates all of the lines within the circle.
     * The color of each line is determined by the "r" instance variable
     * and the "g" and "b" arguments are kept constant.
     * @param numPoints Number of points around the circle
     * @return
     */
    public Group generateLines(double numPoints) {
        incrementRed();
        // TODO: Make a new color using r for the red value and whichever
        //  value you want for green and blue.
        Color color; // fill this in by instantiating a new Color
        // Make a new group to store the lines in
        Group lines = new Group();
        PointOnCircle[] points = PointOnCircle.generatePoints(radius, numPoints);
        /**
         * Loop over each point and draw a line from it to the result
         * of the equation discussed in the video.
         */
        for (PointOnCircle poc : points) {
            // TODO: Generate the id of the point to draw a line to from
            //  the current point you are at. Remember that the PointOnCircle
            //  has the "id" instance variable which represents the number that
            //  point represents. With this information and by watching the video
            //  you should be able to figure out this equation.
            double correspondingPointId = 0; // TODO: replace 0 with the correct equation
            PointOnCircle pointTo   = points[(int)correspondingPointId];
            PointOnCircle pointFrom = points[poc.getId()];

            // TODO: Construct a line from one point to the other
            Line line = new Line(); // TODO: fill this constructor in
            // TODO: Change the color of the line by utilizing lines
            //  setStroke method. This method accepts a Color as an argument
            //  which should be the Color you constructed previously.
            // Add the line to the group
            lines.getChildren().add(line);
        }
        return lines;
    }
}
