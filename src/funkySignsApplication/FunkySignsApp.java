package funkySignsApplication;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.awt.geom.*;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import funkySignsGui.FunkySignsView;
import funkySignsApplication.FunkySignsApp;
import funkySignsGui.FunkySignsDisplayFrame;
import funkySignsModel.AnimatedSign;
import funkySignsModel.CompoundSign;
import funkySignsModel.MovingSign;
import funkySignsModel.PlainSign;
import funkySignsModel.Sign;
import funkySignsModel.SpinningSign;

/**
 * The master application.
 * The main() method lives here.  It constructs an instance of FunkySignsApp,
 * which constructs the GUI and a Timer to kick off tick() events.
 * The command line arguments dictate which animations to set up.
 */

public class FunkySignsApp {

	/** Number of milliseconds between ticks. */
	private static final int TICKTIME = 100;

	/** Timer that will call tick() repeatedly in the GUI thread. */
    protected javax.swing.Timer timer;
	
	/** Collection of Signs to be animated. */
    protected Set<Sign> signs;
    
    /** The main Frame of the application. */
	private FunkySignsDisplayFrame gui;

	/** Construct the SignsApp.
	 *  The gui is set up, but it will still need to be made visible.
	 *  The timer is set up too, but will still need to be started.
	 */
	public FunkySignsApp() {
		signs = new HashSet<Sign>();	
    	gui = new FunkySignsDisplayFrame();
    	
        timer = new javax.swing.Timer(TICKTIME, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tick(); // Call this every time timer fires.
			}
		});
     }

	/** Entry point for the application.
	 *  @param args Names of animations to run.
	 *  @see handleArgs()
	 **/
    public static void main(String[] args) {
        FunkySignsApp app = new FunkySignsApp();      
        app.handleArgs(args);
        app.go();
    }
    
	/** Read the argument list to decide which animations to run. **/
    private void handleArgs(String[] args) {
    	
    	// !!! Task 2 a): Ensure unique args.
    	Set <String> arguments = new HashSet<String>();
    	
    	for (String arg: args)
    		handleArg(arg);
    }
    
    /** Handle a single command line argument. */
    private void handleArg(String arg) {
    	
    	switch (arg.toUpperCase()) {
    	case "PLAINSIGN":
    		makePlainSign();
    		break;
    	case "COWBOYSIGN":
    		makeCowboySign();
    		break;
    	case "VEGASSIGN":
    		makeVegasSign();
    		break;
    	case "OUTDOORSIGN":
    		makeOutdoorSign();
    		break;
    	default:
    		System.err.println("Unknown argument: " + arg);
    	}
    }

	/** Start the animation.  Makes the gui appear & starts the timer. **/
    private void go() {
    	timer.start();
    	gui.setVisible(true);
    }
    
	/** Increment the animation. **/
    private void tick() {
    	
    	// !!! Task 2 b): Eliminate randomness of iteration.

    	for (Sign sign: signs)
    		sign.tick();
    }
    
    private Rectangle getGuiBounds() {
    	return gui.getSignsDisplayPanel().getBounds();
    }
    
	/** Construct a simple sign using one of the gifs. 
	 *  @param imagePath full path to the image.
	 * */
    
	private void makePlainSign() {
		Icon myImageIcon = new ImageIcon("images/saloon-sign.jpg");
		Sign plainSign = new PlainSign(myImageIcon);
        new FunkySignsView(gui.getSignsDisplayPanel(), plainSign);
		plainSign.setLocation(new Point(650,30));
        signs.add(plainSign);
     }
	
	/**
	 * Utility method for assembling an AnimatedSign.
	 * This method loads a set of image files with the given root name 
	 * and extension.  A number (0-numIcons) is inserted into the filenames.
	 * @param basePath Root of the filenames containing images.
	 * @param numIcons Number of image files to load.
	 * @param extension Remainder of the filename after the generated number.
	 */
	private AnimatedSign makeAnimation(String basePath, int numIcons, String extension) {
		
		// Generate filenames & make ImageIcons for each of the images.
		Icon[] icons = new Icon[numIcons];
		for (int i = 1; i <= numIcons; i++){
			icons[i-1] = new ImageIcon(basePath + "-" + i + extension);
		}

		// Create the basic plainSign
		Sign plainSign = new PlainSign(icons[0]);
		new FunkySignsView(gui.getSignsDisplayPanel(),plainSign);
		
		// Create an animated sign by giving it a plain sign and all the icons.
		AnimatedSign animatedSign = new AnimatedSign(plainSign, icons);		
        return animatedSign;
    }
	
	/** Utility method to wrap a Sign so it moves.
	 *  @param base The Sign to wrap.
	 *  @param deltaXY The amount to increment the Sign's XY coordinates.
	 **/
	private MovingSign makeMovingSign(Sign base, Point deltaXY) {
		return new MovingSign(base, deltaXY, getGuiBounds());
	}
	
	/** Utility method to wrap a Sign so it spins.
	 *  @param base The Sign to wrap.
	 *  @param deltaDegrees The amount to increment the Sign's rotation.
	 **/
	private SpinningSign makeSpinner(Sign base, int deltaDegrees) {
		return new SpinningSign(base, deltaDegrees);
	}
	
	private void makeVegasSign(){
		
		// Create the flashing lights
		Sign flashingLights = makeAnimation("images/surround-lights",2,".png");
		// Create the Vegas sign. Animate it.
		Sign vegasSignAnimated = makeAnimation("images/vegas-sign", 3, ".png");

		// Line up both the signs to each other
		vegasSignAnimated.setLocation(new Point(0, 0)); 
		flashingLights.setLocation(new Point (175,275));
		
		// Create the Compound sign by adding both the animated sign and the flashing lights
        Sign vegasSign = new CompoundSign();
        vegasSign.addSign(flashingLights);
        vegasSign.addSign(vegasSignAnimated);
        
        // Choose the new location for the completed sign
        vegasSign.setLocation(new Point(400,330));
 
        // Add it to our collection of signs    
        signs.add(vegasSign);	
		
	}
	
	private void makeOutdoorSign(){
		
		// Create the individual signs
        Icon seng301ImageIcon = new ImageIcon("images/seng301-sign.png");
		Sign seng301Sign = new PlainSign(seng301ImageIcon);	
        new FunkySignsView(gui.getSignsDisplayPanel(), seng301Sign);

        
    	Icon metalSignIcon = new ImageIcon("images/metal-background.png");
        Sign metalSign = new PlainSign(metalSignIcon);
        new FunkySignsView(gui.getSignsDisplayPanel(), metalSign);
        
        // Align the signs to each other
        metalSign.setLocation(new Point(0, 0)); 
		seng301Sign.setLocation(new Point(0,0));

        
		CompoundSign outdoorSign = new CompoundSign();
		outdoorSign.addSign(seng301Sign);
		outdoorSign.addSign(metalSign);				

		// Set the location of the OutdoorSign
		
		outdoorSign.setLocation(new Point(10,40));
		signs.add(outdoorSign);
		
	}
	/* Construct a cowboy sign using the cowboy and basic signs */
	private void makeCowboySign(){
		
		//Create the plain sign
	   	Icon baseIcon = new ImageIcon("images/plain-sign.png");
        Sign baseSign = new PlainSign(baseIcon);
        new FunkySignsView(gui.getSignsDisplayPanel(), baseSign);

        // Create the two cowboys (left and right)
		Icon cowboyIcon = new ImageIcon("images/cowboy.png");
        Sign stillCowboyLeft= new PlainSign(cowboyIcon);
        new FunkySignsView(gui.getSignsDisplayPanel(), stillCowboyLeft);
        Sign stillCowboyRight = new PlainSign(cowboyIcon);
        new FunkySignsView(gui.getSignsDisplayPanel(), stillCowboyRight);
        
        // Line up all cowboys with the base sign
        stillCowboyLeft.setLocation(new Point(0, 0)); 
        baseSign.setLocation(new Point(225, 110)); 
        stillCowboyRight.setLocation(new Point(400, 0));
        
    	// !!! Task 1 a): Spin each cowboy. You can choose the speed and direction of the spin.
        Sign spinningCowboyLeft = makeSpinner(stillCowboyLeft, 10);
        Sign spinningCowboyRight = makeSpinner(stillCowboyRight, -10);
        
		// Create the Compound sign by adding both the animated sign and the flashing lights
        CompoundSign cowboySign = new CompoundSign();
        cowboySign.addSign(baseSign);
        cowboySign.addSign(spinningCowboyLeft);
        cowboySign.addSign(spinningCowboyRight);

        // !!! Task 1 b): Place the movingCowboySign somewhere appropriate on the screen
        cowboySign.setLocation(new Point(25, 50));
        
    	// !!! Task 1 c): Move the movingCowboySign so that it moves from one side of the screen to the other
        Sign movingCowboySign = makeMovingSign(cowboySign, new Point(10, 0));
        
        // !!! Add the movingCowboySign to our collection of signs
        signs.add(movingCowboySign);
	}
}
