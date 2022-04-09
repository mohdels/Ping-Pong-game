package ppPackage;

import acm.graphics.GPoint;

import acm.graphics.GRect;
import acm.program.GraphicsProgram;
import static ppPackage.ppSimParams.*;

//Important! This code is a modified version of the ppBall code provided, by Professor Ferrie, in the assignment handout as well as the code provided by Katrina Poulin in the tutorial. 

import java.awt.Color;

public class ppPaddle extends Thread {

	double X;
	double Y;
	double Vx;
	double Vy;
	GRect myPaddle;
	GraphicsProgram GProgram;
	ppTable myTable;
	private Color myColor;

	public ppPaddle (double X, double Y, Color myColor, ppTable myTable, GraphicsProgram GProgram) {

		this.X = X;
		this.Y = Y;
		this.myTable = myTable;
		this.GProgram = GProgram;
		this.myColor= myColor;

		// world coordinates ppPaddleW

		double upperLeftX = X - ppPaddleW/2;
		double upperLeftY = Y + ppPaddleH/2;

		// p in screen coordinates 

		GPoint p = myTable.W2S(new GPoint(upperLeftX, upperLeftY));

		// screen coordinates

		double ScrX = p.getX();
		double ScrY = p.getY();

		this.myPaddle = new GRect(ScrX, ScrY, ppPaddleW*Xs, ppPaddleH*Ys);

		myPaddle.setFilled(true);
		myPaddle.setColor(myColor);
		GProgram.add(myPaddle);


	}
	public void run() {
		double lastX = X;
		double lastY = Y;
		while (true) {
			Vx=(X-lastX)/TICK;
			Vy=(Y-lastY)/TICK;
			lastX=X;
			lastY=Y;
			GProgram.pause(TICK*TSCALE); // Time to mS
		}

	}

	public GPoint getP() {
		GPoint p = new GPoint(X,Y);
		return p;  // return new GPoint(X,Y)
	}

	// Sets and moves the paddle to (X,Y)
	public void setP(GPoint P) {
		// update instance variables

		this.X = P.getX();    // in world coordinates
		this.Y = P.getY();    // in world coordinates

		// world coordinates

		double upperLeftX = X - ppPaddleW/2;
		double upperLeftY = Y + ppPaddleH/2;

		// p in screen coordinates 

		GPoint p = myTable.W2S(new GPoint(upperLeftX, upperLeftY));

		// screen coordinates

		double ScrX = p.getX();
		double ScrY = p.getY();

		// move the GRect instance

		this.myPaddle.setLocation(ScrX, ScrY);

	}

	// Returns paddle velocity (Vx,Vy) 
	public GPoint getV() {
		return new GPoint(Vx,Vy);

	}

	// Returns the sign of the Y velocity of the paddle
	public double getSgnVy() {
		// return 1 when Vy >= 0
		if (Vy >= 0) return 1;
		// return -1 when Vy <= 0
		else return -1;

	}


	// Returns true if a surface at position (Sx,Sy) is deemed to be in contact with the paddle
	public boolean contact(double Sx, double Sy) {
		return ((Sy >= Y-ppPaddleH/2) && (Sy <= Y + ppPaddleH/2));  // called whenever X + X0 >= ppPaddle.getP().getX()

	}





}


