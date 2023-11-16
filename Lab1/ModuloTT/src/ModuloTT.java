import java.text.DecimalFormat;
import java.util.concurrent.ThreadLocalRandom;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
/**
 * CS 351 - 004
 * 
 * NAME: Damian Franco 
 *       dfranco24@unm.edu
 *       101789677
 * 
 * LAB: Modulo Times Tables
 */
public class ModuloTT extends Application {
    /* Global double multiplier variable to hold the multiplier value */
    public double mult = 2.0;
    /* Global integer that holds our variable to cycle through the favorite tour animation */
    public int favIncrement = 0;
    /* Global color array of colors to pick from at random */
    public Color[] colorList = {
            Color.GREEN,
            Color.BLUE,
            Color.RED,
            Color.ORANGE,
            Color.DEEPPINK,
            Color.CRIMSON,
            Color.DARKVIOLET,
            Color.BLACK,
            Color.FUCHSIA,
            Color.GOLD
            };
  
    /*
     * The setPoints method will take in our Circle and assign points along
     * the parameter of the circle based off the number of points we want to
     * assign. We achieve this by making a new array of points with the size 
     * equal to the number of points. We then will assign a double variable to
     * our radius of our circle. We also assign two other double variables based
     * off of our angle and our seperation between points. In this method, we will
     * parse through our Point array and assign each Point within the array according
     * to how many points we have and the angle of the next point. In order to get our
     * x and y variable correctly placed, the Math class would have to be called.
     * Since x corresponds to the COS of a graph, we must take the COS of our angle
     * and multiply it by our radius. That will give us our x positon on the parameter
     * of the Circle. We do the same with y, but use SIN instead of COS. Then we add
     * the new points to our Point array. We will then increment our i and our angle 
     * according to our next next point.
     * 
     * @param mainCircle Circle object to get radius from to see
     * @param NOP Number of Points on the circle
     * @return Array of the Point object placed around the circle
     */
    public Point[] setPoints(Circle mainCircle, double NOP) {
        //Empty Point array sized to the NOP double
        Point[] p = new Point[(int) NOP];
        //Double variable assigned to the size of our circles radius
        double radius = mainCircle.getRadius();
        //Sepeartion between our points
        double nextPointSep = 360 / NOP;
        double angle = 180;
        for(int i = 0; i < p.length; i++) {
            double x = radius * Math.cos(Math.toRadians(angle)) ;
            double y = radius * Math.sin(Math.toRadians(angle));
            //if statement the breaks the loop when i = to the length of
            //our point array. This allows the program to configure to any
            //number of point that we want to setup on the circle.
            if(i == p.length) {
                break;
            }
            p[i] = new Point(i, x, y);
            angle += nextPointSep;
        }
        return p;
    }
    
    /*
     * The drawLines will get take our points that we setup alongside our circle and
     * parse through the point array and get our points on the line values from the 
     * setPoints. We first will parse through the point array. We will then set a double
     * variable that will get our value and multply it by our current value with our 
     * multiplier. Then we will mod it by the number of point on our circle. This will give
     * the next value of our point that we want to draw to. Then we will set a point up where
     * of our current point. We will then set a new point that is our point we want to draw to.
     * Our origin point we will draw from, our nextPoint point we will draw to. Then the program 
     * will setup a new line object with our origin point and next points x and y values. It will
     * then add that new line to a group and repeat for every single line until it is broken out
     * of our loop. 
     * 
     * 
     * @param double m multplier we use to draw our lines
     * @param double NOP number of points on the circle
     * @param Circle mainCircle circle object the we base our points on
     * @param Color c color of our visualization
     * 
     * @return Group of lines generated from our for loop
     */
    public Group drawLines(double m, double NOP, Circle mainCircle, Color c) {
        Group lines = new Group();
        Point[] p = setPoints(mainCircle, NOP);
        for(int i = 0; i < p.length; i++) {
            double nextValue = (p[i].getValue() * m) % NOP;
            Point origin = p[p[i].getValue()];
            //if statement the breaks the loop when i = to the length of
            //our point array. This allows the program to configure to any
            //number of point that we want to setup on the circle.
            if(nextValue >= p.length) {
                break;
            }
            Point nextPoint = p[(int) nextValue];
            //The + 300, + 265, are there to  accomidate to our GUI 
            //x and y coordinates. 
            Line line = new Line(origin.x + 300, origin.y + 265, 
                    nextPoint.x + 300, nextPoint.y + 265); 
            line.setStroke(c);
            lines.getChildren().add(line);
        }
        return lines;
    }
    
    /*
     * The favSetup will setup all of my favorite visualization stops.
     * These are generated from putting specific settings to the multiplier
     * and points on the circle right into our drawLines method. Then it will 
     * add all of the favorite graphs into a Group array full of the my 
     * favorite settings. 
     * 
     * @param Circle mainCircle circle we base our visualization off of
     * 
     * @return Group array that will return the group of favorite graphs
     * 
     * @Override
     */
    public Group[] favSetup(Circle mainCircle) {
        Color favColor = colorList[ThreadLocalRandom.current().nextInt(0, colorList.length)];
        Group fav1 = drawLines(180.9, 360, mainCircle, favColor);
        Group fav2 = drawLines(2.95, 360, mainCircle, favColor);
        Group fav3 = drawLines(49, 100, mainCircle, favColor);
        Group fav4 = drawLines(287, 360, mainCircle, favColor);
        Group fav5 = drawLines(28.7, 360, mainCircle, favColor);
        Group fav6 = drawLines(259, 260, mainCircle, favColor);
        Group fav7 = drawLines(182, 360, mainCircle, favColor);
        Group fav8 = drawLines(217, 360, mainCircle, favColor);
        Group fav9 = drawLines(42, 165, mainCircle, favColor);
        Group fav10 = drawLines(272, 360, mainCircle, favColor);
        Group favList[] = {fav1, fav2, fav3, fav4, fav5, fav6, fav7, fav8, fav9, fav10};
        return favList;
    }
    
    /*
     * The start method will handle everything in our program on the GUI
     * side of things. It will also setup other arithmetic that helps us 
     * handle our button events, animation for our visualization, and event
     * handling in general on our GUI.
     * 
     * @param Stage primaryStage main GUI stage
     * 
     * @Override
     */
    public void start(Stage primaryStage) throws Exception {
        /* Width of the GUI window */
        double width  = 1225.0;
        /* Height of the GUI window */
        double height = 580.0;
        primaryStage.setTitle("Modulo Times Table");
        /* Main Canvas creation */
        Canvas canvas = new Canvas(width, height);
        
        /* Circle setup */
        Circle mainCircle = new Circle(300.0f, 265.0f, 250.f);
        mainCircle.setStroke(Color.BLACK);
        mainCircle.setFill(null);
        Group g = new Group(mainCircle);
        
        /* Top visualization label setup */
        Label topLabel = new Label("Start the Visualization by setting the Multiplier\n"
                + "and Update Time on the Slider below:");
        Font tFont = Font.font("TimesRoman", FontWeight.EXTRA_BOLD, 15);
        topLabel.setFont(tFont);
        topLabel.setLayoutX(650);
        topLabel.setLayoutY(10);
        
        /* Top times table multiplier increment label setup */
        Label timesTableMult = new Label("Times Table Multiplier:  " + 2.0);
        Font timesTableFont = Font.font("TimesRoman", FontWeight.EXTRA_BOLD, 15);
        timesTableMult.setFont(timesTableFont);
        timesTableMult.setLayoutX(1000);
        timesTableMult.setLayoutY(225);

        /* Top multiplier slider label setup */
        Label topMultLabel = new Label();
        topMultLabel.setLayoutX(865);
        topMultLabel.setLayoutY(65);

        /* Top multiplier slider setup */
        Slider topMultSlider = new Slider(0, 5, 1);
        topMultSlider.setBlockIncrement(1.0);
        topMultSlider.setPrefWidth(550);
        topMultSlider.setMajorTickUnit(0.25);
        topMultSlider.setShowTickLabels(true);
        topMultSlider.setShowTickMarks(true);
        topMultSlider.setLayoutX(650);
        topMultSlider.setLayoutY(85);
        
        /* Top time delay slider label setup */
        Label topTimeLabel = new Label();
        topTimeLabel.setLayoutX(865);
        topTimeLabel.setLayoutY(145);
        
        /* Top time delay slider setup */
        Slider timerSlider = new Slider(0, 5, 1);
        timerSlider.setBlockIncrement(1.0);
        timerSlider.setPrefWidth(550);
        timerSlider.setMajorTickUnit(0.25);
        timerSlider.setShowTickLabels(true);
        timerSlider.setShowTickMarks(true);
        timerSlider.setLayoutX(650);
        timerSlider.setLayoutY(165);
        
        /* Bindings to top two slider labels to update user at they slide the slider  */
        topMultLabel.textProperty().bind(
                Bindings.format("Slide to Increment by: %.1f", 
                        topMultSlider.valueProperty())
                );
        topTimeLabel.textProperty().bind(
                Bindings.format("Slide to set time delay: %.1f", 
                        timerSlider.valueProperty())
                );
        
        /* Start button setup */
        Button startBtn = new Button();
        startBtn.setText("Start");
        startBtn.setPrefHeight(35);
        startBtn.setPrefWidth(125);
        startBtn.setLayoutX(865);
        startBtn.setLayoutY(220);
        
        /* Pause button setup */
        Button pauseBtn = new Button();
        pauseBtn.setText("Pause");
        pauseBtn.setPrefHeight(35);
        pauseBtn.setPrefWidth(125);
        pauseBtn.setLayoutX(785);
        pauseBtn.setLayoutY(270);
        
        /* Restart button setup */
        Button restartBtn = new Button();
        restartBtn.setText("Restart");
        restartBtn.setPrefHeight(35);
        restartBtn.setPrefWidth(125);
        restartBtn.setLayoutX(945);
        restartBtn.setLayoutY(270);

        /* Bottom title labe setup */
        Label bottomLabel = new Label("Slide the two sliders below to jump to a specific "
                + "number\nof Points on the Circle and a Multiplier:");
        Font bFont = Font.font("TimesRoman", FontWeight.EXTRA_BOLD, 15);
        bottomLabel.setFont(bFont);
        bottomLabel.setLayoutX(650);
        bottomLabel.setLayoutY(320);

        /* Points on the circle select slider label setup */
        Label pointLabel = new Label();
        pointLabel.setLayoutX(855);
        pointLabel.setLayoutY(375);
        
        /* Points on the circle select slider setup */
        Slider pointsSlider = new Slider(0, 360, 360);
        pointsSlider.setBlockIncrement(1);
        pointsSlider.setPrefWidth(550);
        pointsSlider.setMajorTickUnit(5);
        pointsSlider.setShowTickLabels(true);
        pointsSlider.setShowTickMarks(true);
        pointsSlider.setLayoutX(650);
        pointsSlider.setLayoutY(395);
        
        /* Jump to multiplier label slider setup */
        Label multLabel = new Label();
        multLabel.setLayoutX(865);
        multLabel.setLayoutY(445);
        
        /* Jump to multiplier select slider setup */
        Slider multSlider = new Slider(0, 360, 2);
        multSlider.setBlockIncrement(1);
        multSlider.setPrefWidth(550);
        multSlider.setMajorTickUnit(5);
        multSlider.setShowTickLabels(true);
        multSlider.setShowTickMarks(true);
        multSlider.setLayoutX(650);
        multSlider.setLayoutY(465);
        
        /* Bindings to bottom two slider labels to update user at they slide the slider */
        multLabel.textProperty().bind(
                Bindings.format("Slide to set Multiplier: %.0f", 
                        multSlider.valueProperty())
                );
        pointLabel.textProperty().bind(
                Bindings.format("Slide to set # of Points: %.0f", 
                        pointsSlider.valueProperty())
                );
        
        /* Jump to button setup */
        Button jumpBtn = new Button();
        jumpBtn.setText("Jump To");
        jumpBtn.setPrefHeight(35);
        jumpBtn.setPrefWidth(125);
        jumpBtn.setLayoutX(865);
        jumpBtn.setLayoutY(510);
       
        /* Color select label setup*/
        Label colorLabel = new Label ("Select Jump To Color:");
        colorLabel.setLayoutX(450);
        colorLabel.setLayoutY(485);
        
        /* Color select combo box setup */
        ComboBox<String> colorSelect = new ComboBox<String>();
        colorSelect.getItems().addAll(
                "GREEN",
                "BLUE",
                "RED",
                "ORANGE",
                "SIENNA",
                "CRIMSON",
                "DARKVIOLET",
                "CHOCOLATE",
                "FUCHSIA",
                "LAWNGREEN"
                );
        colorSelect.setValue("GREEN");
        colorSelect.setLayoutX(450);
        colorSelect.setLayoutY(505);
        
        /* Favorite tour label setup */
        Label favLabel = new Label ("View my favorites:");
        favLabel.setLayoutX(50);
        favLabel.setLayoutY(490);
        
        /* Favorite tour button setup */
        Button favBtn = new Button();
        favBtn.setText("Take Tour!");
        favBtn.setLayoutX(50);
        favBtn.setLayoutY(510);
        
        /* Main pane decleration */
        Pane root = new Pane(canvas);
        
        /* Adding all the jump to elements to the GUI */
        root.getChildren().add(g);
        root.getChildren().add(bottomLabel);
        root.getChildren().add(pointLabel);
        root.getChildren().add(multLabel);
        root.getChildren().add(pointsSlider);
        root.getChildren().add(multSlider);
        root.getChildren().add(jumpBtn);
        
        /* Adding all the visualization elements to the GUI */
        root.getChildren().add(timesTableMult);
        root.getChildren().add(topLabel);
        root.getChildren().add(topMultLabel);
        root.getChildren().add(topTimeLabel);
        root.getChildren().add(topMultSlider);
        root.getChildren().add(timerSlider);
        root.getChildren().add(startBtn);
        root.getChildren().add(pauseBtn);
        root.getChildren().add(restartBtn);
        
        /* Adding the color select options to the GUI */
        root.getChildren().add(colorLabel);
        root.getChildren().add(colorSelect);
        
        /* Adding the favorite tour elements to the GUI */
        root.getChildren().add(favLabel);
        root.getChildren().add(favBtn);
        
        /*
         * The new AnimationTimer object created here will handle all of our main 
         * visuals when the visualization is ran. In the timer class, we will set 
         * up a long variable that will handle our time. This will also give us 
         * the ability to set up how quickly our animation refresh rate from
         * our slider. We will then remove our last animation and add a new group
         * of lines to display with the new multiplier incremented. Next we will 
         * select a new color to swap to and set that to the color of our next
         * graph. We will increment our multiplier by whatever the user selects 
         * on the slider. The animation will keep running until it hits hits 
         * the number of points which is set at 360 for our main visualization. 
         * The if statement her will handle our time delay according to the in
         * class demonstartion by Professor Haugh. 
         */
        DecimalFormat df = new DecimalFormat("#.0");
        AnimationTimer mainVisuals = new AnimationTimer() {
            public long lastUpdate = 0;
            @Override
            public void handle(long now) {
                if (now - lastUpdate >= timerSlider.getValue() * 1_000_000_000) {
                    root.getChildren().removeIf(node -> node instanceof Group);
                    double p = 360;
                    Color c = colorList[ThreadLocalRandom.current().nextInt(0, colorList.length)];
                    Group lines = drawLines(mult, p, mainCircle, c);
                    root.getChildren().add(lines);
                    timesTableMult.setText("Times Table Multiplier: " + df.format(mult));
                    mult += topMultSlider.getValue();
                    lastUpdate = now;
                    if(mult > 360) {
                        stop();
                    }
              }
            }
        };
            
        /*
         * Handling our Jump To button by getting our values from our sliders and
         * then setting assigning them to a new group of lines that we make with those
         * values from our sliders. That will then give the user the visualization of
         * their selected multiplier and number of circles. 
         */
        jumpBtn.setOnAction(event -> {
            root.getChildren().removeIf(node -> node instanceof Group);
            double m = multSlider.getValue();
            double p = pointsSlider.getValue(); 
            Color c = Color.web(colorSelect.getValue());
            Group lines = drawLines(m, p, mainCircle, c);
            root.getChildren().add(lines);
        });
       
        /*
         * Handles the start buttons events. It will stop the previous animations, if started
         * and then start the main visuals AnimationTimer class. The start button will also
         * be able to start the animation at the same spot, rather than restart it. 
         */
        startBtn.setOnAction(event -> {
            mainVisuals.stop();
            mainVisuals.start();
        });
        
        /*
         * Handles the stop button event by just stopping the main visuals. It will then stop it
         * and not restart it.
         */
        pauseBtn.setOnAction(event -> {
            mainVisuals.stop();
        });
        
        /*
         * Handles the restart button by stopping our main visuals, setting
         */
        restartBtn.setOnAction(event -> {
            mainVisuals.stop();
            mult = 2.0;
            mainVisuals.handle(0);
            mainVisuals.start();
        });
        
        /*
         * Sets up our animations for the favorite list to cycle through
         * every frame. First it will remove each visualization before 
         * the last and then show the next visualization on the list and 
         * increment by one. It will then stop after all of the favorites
         * have been shown.
         */
        Group[] favList = favSetup(mainCircle);
        AnimationTimer favTour = new AnimationTimer() {
            public long lastUpdate = 0;
            @Override
            public void handle(long now) {
                if (now - lastUpdate >= timerSlider.getValue() * 1_000_000_000) {
                    root.getChildren().removeIf(node -> node instanceof Group);
                    root.getChildren().add(favList[favIncrement]);
                    favIncrement++;
                    lastUpdate = now;
                    if(favIncrement > 9) {
                        stop();
                    }
              }
            }
        };
        
        /*
         * Favorite button event handling. This stops our tour and restarts it from
         * the beginning by setting our increment back to 0;
         */
        favBtn.setOnAction(event -> {
            favTour.stop();
            favIncrement = 0;
            favTour.start();
        });
        
        /* Main scene decleration */
        Scene scene = new Scene(root, width, height);
        /* Setting the scene and showing it when the application is launched */
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    /*
     * Main method the starts the application when ran.
     */
    public static void main(String[] args) {
        launch(args);
    }
    
   
}
