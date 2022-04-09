package ppPackage;

import java.awt.Color;
import static ppPackage.ppSimParams.*;
import acm.graphics.GOval;
import acm.graphics.GPoint;
import acm.program.GraphicsProgram;


/**
 * This is the heart of the program. It embeds the simulation loop that was created in assignment 1 and extends the Thread class- which
 * means that its run() method executes concurrently with all other methods in the program.
 * @author mohammedelsayed
 *
 */
public class ppBall extends Thread {

	// Instance variables

	private double Xinit;       // Initial position of ball - X
	private double Yinit;       // Initial position of ball - Y
	private double Vo;          // Initial velocity (Magnitude)
	private double theta;       // Initial direction
	private double loss;        // Energy loss on collision
	private Color color;        // Color of ball
	private GraphicsProgram GProgram;       // Instance of ppSim class (this)
	GOval myBall;                           // Graphics object representing ball
	ppTable myTable;                        // ppTable instance
	ppPaddle RPaddle;                       // Right ppPaddle instance
	ppPaddle LPaddle;                       // Left ppPaddle instance
	double X, Xo, Y, Yo;
	double Vx, Vy;
	boolean falling;


	/**
	 * The constructor for the ppBall class copies parameters to instance variables, creates an
	 * instance of a GOval to represent the ping-pong ball, and adds it to the display.
	 *
	 * @param Xinit - starting position of the ball X (meters)
	 * @param Yinit - starting position of the ball Y (meters)
	 * @param Vo - initial velocity (meters/second)
	 * @param theta - initial angle to the horizontal (degrees)
	 * @param loss - loss on collision ([0,1])
	 * @param color - ball color (Color)
	 * @param GProgram - a reference to the ppSim class used to manage the display
	 */

	public ppBall(double Xinit, double Yinit, double Vo, double theta, double loss, Color color, GraphicsProgram GProgram, ppTable myTable) {

		this.Xinit=Xinit; // Copy constructor parameters to instance variables
		this.Yinit=Yinit;
		this.Vo=Vo;
		this.theta=theta;
		this.loss=loss;
		this.color=color;
		this.GProgram=GProgram;
		this.myTable = myTable;

		// Create the ball

		GPoint p = myTable.W2S(new GPoint(Xinit,Yinit));    // Convert simulation to screen coordinates
		double ScrX = p.getX();                    
		double ScrY = p.getY();
		this. myBall = new GOval(ScrX,ScrY,2*bSize*Xs,2*bSize*Ys); 
		myBall.setColor(color);                    // set the color of the ball
		myBall.setFilled(true);                    // Fill the ball with the chosen color
		GProgram.add(myBall);                      // Add the ball
		GProgram.pause(1000);                               // So we can see the starting point of the ball




	}

	public void run() {

		// Initialize simulation parameters

		Xo = Xinit;                          // Set initial X position
		Yo = Yinit;                          // Set initial Y position
		double time = 0;                            // Time starts at 0 and counts up
		double Vt = bMass*g / (4*Pi*bSize*bSize*k); // Terminal velocity
		double Vox=Vo*Math.cos(theta*Pi/180);       // X component of velocity
		double Voy=Vo*Math.sin(theta*Pi/180);       // Y component of velocity

		double KEx = ETHR;
		double KEy = ETHR;
		double PE = ETHR;

		// Main simulation loop

		falling = true;      // Initial state = falling.


		// Important - X and Y are ***relative*** to the initial starting position Xo, Yo.
		// So the absolute position is Xabs = X+Xo and Yabs = Y+Yo.
		// Also - print out a header line for the displayed values.

		System.out.printf("\t\t\t Ball Position and Velocity\n");

		while (falling) {
			X = Vox*Vt/g*(1-Math.exp(-g*time/Vt));             // Update relative position
			Y = Vt/g*(Voy+Vt)*(1-Math.exp(-g*time/Vt))-Vt*time;
			Vx = Vox*Math.exp(-g*time/Vt);                     // Update velocity
			Vy = (Voy+Vt)*Math.exp(-g*time/Vt)-Vt;

			// Display current values (1 time/second)

			System.out.printf("t: %.2f\t\t X: %.2f\t Y: %.2f/t Vx: %.2f\t Vy: %.2f\n", time,X+Xo, Y+Yo, Vx, Vy);

			GProgram.pause(SLEEP);   // Pause program for SLEEP mS

			if (Y + Yo > Ymax) {          // checks for hitting the ceiling
				falling = false;
			}

			// Check to see if we hit the ground yet. when the ball hits the ground, the height of the center is the radius of the ball.

			if (Vy<0 && Y+Yo <= bSize) {
				// ground collision
				// calculate energy
				// set Vox, Voy
				// Reset X, Y, Time, Xo, Yo


				KEx = 0.5*bMass*Vx*Vx*(1-loss);     // horizontal kinetic energy
				KEy = 0.5*bMass*Vy*Vy*(1-loss);     // vertical kinetic energy
				PE = 0;

				Vox=Math.sqrt(2*KEx/bMass);             // X component of velocity
				Voy=Math.sqrt(2*KEy/bMass);             // Y component of velocity


				Xo = Xo + X;                           // need to accumulate distance between each collision
				Yo=bSize;                              // the absolute position of the ball on the ground
				time=0;                                // time is reset at every collision
				Vt=bMass*g / (4*Pi*bSize*bSize*k);     // Terminal velocity
				X=0;                                   // (X,Y) is the instantaneous position along an arc,
				Y=0;                                   // Absolute position is (Xo+X,Yo+Y)


				if (Vx<0) Vox=-Vox;                        // Check to see if X component of velocity becomes negative (opposite direction), X component of inital velocity becomes negative (opposite direction) as well



			}
			
      



			if (Vx>0 && Xo+X >= RPaddle.getP().getX()- ppPaddleW/2 - bSize) {

				// possible collision with right paddle

				if (RPaddle.contact(X+Xo, Y+Yo)) {
					KEx = 0.5*bMass*Vx*Vx*(1-loss);           // horizontal kinetic energy
					KEy = 0.5*bMass*Vy*Vy*(1-loss);           // vertical kinetic energy
					PE = bMass*g*(Y);                         // potential energy

					Vox=-Math.sqrt(2*KEx/bMass);              // X component of velocity
					Voy=Math.sqrt(2*KEy/bMass);               // Y component of velocity

					Vox=Vox*ppPaddleXgain;                       // Scale X component of velocity

					if (Vox > VoxMax) Vox = VoxMax;   // this limits horizontal from passing a certain maximum horizontal velocity 

					Voy=Voy*ppPaddleYgain*RPaddle.getSgnVy();    // Scale Y + same direction as paddle 

					Xo = RPaddle.getP().getX()- ppPaddleW/2 - bSize;                              
					Yo = Yo + Y;                              // Accumulate distance
					time = 0;                                 // Time is reset at every condition
					X = 0;                                    // (X,Y) is the instantaneous position along an arc
					Y = 0;                                    // Absolute position is (Xo+x, Yo+Y)
				} else {
					falling = false;
				}
			}

			if (Vx<0 && Xo+X <= LPaddle.getP().getX()+ ppPaddleW/2 + bSize) {

				// possible collision with left paddle

				if (LPaddle.contact(X+Xo, Y+Yo)) {
					KEx = 0.5*bMass*Vx*Vx*(1-loss);        // horizontal kinetic energy
					KEy = 0.5*bMass*Vy*Vy*(1-loss);        // vertical kinetic energy
					PE = bMass*g*(Y);                      // potential energy

					Vox=Math.sqrt(2*KEx/bMass);            // X component of velocity
					Voy=Math.sqrt(2*KEy/bMass);            // Y component of velocity

					Vox=Vox*ppPaddleXgain;                       // Scale X component of velocity

					if (Vox > VoxMax) Vox = VoxMax;            // this limits horizontal from passing a certain maximum horizontal velocity 

					Voy=Voy*ppPaddleYgain*LPaddle.getSgnVy();    // Scale Y + same direction as paddle 

					Xo = LPaddle.getP().getX() + ppPaddleW/2 + bSize;                           // Reinitialize Xo, Yo, time, X, and Y
					Yo = Yo + Y;                              // Accumulate distance
					time = 0;                                 // Time is reset at every condition
					X = 0;                                    // (X,Y) is the instantaneous position along an arc
					Y = 0;                                    // Absolute position is (Xo+x, Yo+Y)
				} else {
					falling = false;
				}

			}



			if ((KEx+KEy+PE<ETHR)) {

				falling=false;
			}

			// Update the position of the ball. Plot a tick mark at current location.

			GPoint p1 = myTable.W2S(new GPoint(Xo+X-bSize, Yo+Y+bSize));   // Get current position in screen coordinates
			double ScrX = p1.getX();
			double ScrY = p1.getY();
			myBall.setLocation(ScrX,ScrY);

			if (traceButton.isSelected())  trace(ScrX,ScrY); //trace(Xo+X, Yo+Y);

			time+= TICK;

			if (TEST) System.out.printf("t: %.2f X: %.2f Y: %.2f Vx: %.2f Vy: %.2f\n", time,X+Xo, Y+Yo, Vx, Vy);

			// Pause display

			GProgram.pause(TICK*TSCALE);
		}
	}








	/**
	 * Method to convert from world to screen coordinates.
	 * @param P a point object in world coordinates
	 * @return P the corresponding point object in screen coordinates
	 */

	GPoint W2S (GPoint P) {

		double X = P.getX();
		double Y = P.getY();
		double x = (X-Xmin)*(xmax-xmin)/(Xmax-Xmin);
		double y = ymax-(Y-Ymin)*(ymax-ymin)/(Ymax-Ymin);
		return new GPoint(x,y);
	}
	/***
	 * A simple method to plot a point on the screen 
	 * @param X - X location of point (world coordinates)
	 * @param Y - Y location of point
	 */
	private void trace(double ScrX, double ScrY) {
		GOval pt = new GOval(ScrX,ScrY,PD,PD);
		pt.setColor(Color.BLACK);
		pt.setFilled(true);
		GProgram.add(pt);
	}

	public void setRightPaddle(ppPaddle myPaddle) {
		this.RPaddle = myPaddle;
	}

	/*
	 * Sets the value of the reference to the Agent paddle
	 */
	public void setLeftPaddle(ppPaddle myPaddle) {
		this.LPaddle = myPaddle;
	}

	/*
	 * Getter for ball velocity
	 */
	public GPoint getV() {
		return new GPoint(Vx, Vy);
	}

	/*
	 * Getter for ball position
	 */
	public GPoint getP() {
		return new GPoint(X + Xo, Y + Yo);
	}

	/*
	 * Terminates simulation
	 */
	void kill() {
		falling = false;
	}



}

