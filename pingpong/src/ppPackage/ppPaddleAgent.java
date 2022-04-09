package ppPackage;

import java.awt.Color;
import acm.graphics.GPoint;
import acm.program.GraphicsProgram;
import static ppPackage.ppSimParams.*;

//Important! This code is a modified version of the ppBall code provided, by Professor Ferrie, in the assignment handout as well as the code provided by Katrina Poulin in the tutorial. 

public class ppPaddleAgent extends ppPaddle {

	ppBall myBall;    // myBall instance

	public ppPaddleAgent(double X, double Y, Color myColor, ppTable myTable, GraphicsProgram GProgram) {
		super(X, Y, myColor, myTable, GProgram);

	}

	public void run() {
		int ballSkip = 0;
		int AgentLag = 0;
		double lastX = X;
		double lastY = Y;
		while(true) {
			Vx = (X-lastX)/TICK;
			Vy = (Y - lastY)/TICK;
			lastX = X;
			lastY = Y;
			if (ballSkip++ >= AgentLag) {
				// get the ball Y position
				double Y = myBall.getP().getY();
				// set paddle position to that Y
				this.setP(new GPoint(this.getP().getX(), Y));
				ballSkip=0;
			}
			this.GProgram.pause(TICK*TSCALE);
		}
	}
/**
 * This method allows the reference to ppBall to be set externally
 * @param myBall
 */
	public void attachBall (ppBall myBall) {
		this.myBall = myBall;

	}
}
