package ppPackage;

import static ppPackage.ppSimParams.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JToggleButton;

import acm.graphics.GPoint;
import acm.program.GraphicsProgram;
import acm.util.RandomGenerator;

//Important! This code is a modified version of the ppBall code provided, by Professor Ferrie, in the assignment handout as well as the code provided by Katrina Poulin in the tutorial. 

/**
 * This is the main entry point for the program. It implements the run() method, generates the table walls, prompts the user for input (Vo, theta, loss), 
 * creates an instance of a ball with these parameters, and starts the simulation.
 * @author mohammedelsayed
 *
 */

public class ppSim extends GraphicsProgram {

	ppTable myTable;
	ppPaddle RPaddle;
	ppPaddleAgent LPaddle;
	ppBall myBall;
	RandomGenerator rgen;


	public static void main(String[] args) {
		new ppSim().start(args);
	}

	public void init() {
		this.setSize(ppSimParams.WIDTH, ppSimParams.HEIGHT+OFFSET);

		// create the buttons for the three menu items
		JButton newServeButton = new JButton ("New Serve");
		JButton quitButton = new JButton ("Quit");
		traceButton = new JToggleButton("Trace", false);

		// add buttons to the canvas
		add(newServeButton, SOUTH);
		add(quitButton, SOUTH);
		add(traceButton, SOUTH);

		addMouseListeners();
		addActionListeners();

		rgen = RandomGenerator.getInstance();
		rgen.setSeed(RSEED);

		myTable = new ppTable(this);
		myBall = newBall();
		newGame();

	}

	/**
	 * This method simply encapsulates generating random parameters and creating a ppBall instance,
       so that this process can be repeated each time a new serve is requested.
	 * @return
	 */
	ppBall newBall() {
		// generate paramaters for simulation
		Color iColor = Color.RED;
		double iYinit = rgen.nextDouble(YinitMIN,YinitMAX);
		double iLoss = rgen.nextDouble(EMIN,EMAX);
		double iVel = rgen.nextDouble(VoMIN,VoMAX);
		double iTheta = rgen.nextDouble(ThetaMIN,ThetaMAX);

		//create ball

		return new ppBall(Xinit, iYinit, iVel, iTheta, iLoss, iColor, this, myTable);
	}

	public void newGame() {
		if (myBall != null) myBall.kill();     // stop current game in play
		myTable.newScreen();
		myBall = newBall();
		RPaddle = new ppPaddle(ppPaddleXinit,ppPaddleYinit, Color.GREEN, myTable, this);
		LPaddle = new ppPaddleAgent(LPaddleXinit, LPaddleYinit, Color.BLUE, myTable, this);
		LPaddle.attachBall(myBall);
		myBall.setRightPaddle(RPaddle);
		myBall.setLeftPaddle(LPaddle);
		pause(STARTDELAY);
		myBall.start();
		LPaddle.start();
		RPaddle.start();
	}


	/**
	 * Mouse Handler - a moved event moves the paddle up and down in Y
	 */

	public void mouseMoved(MouseEvent e) {

		// convert mouse position to a point in screen coordinates

		if (myTable==null || RPaddle==null) return;
		GPoint Pm = myTable.S2W(new GPoint(e.getX(),e.getY()));
		double PaddleX = RPaddle.getP().getX();
		double PaddleY = Pm.getY();
		RPaddle.setP(new GPoint(PaddleX,PaddleY));
	}

	/**
	 * Button Handler
	 */
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals("New Serve")) {
			newGame();
		} else if (command.equals("Quit")) {
			System.exit(0);
		}
	}

}

