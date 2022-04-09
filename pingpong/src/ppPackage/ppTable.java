package ppPackage;

import acm.graphics.GPoint;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;
import static ppPackage.ppSimParams.*;

import java.awt.Color;



public class ppTable {

	GraphicsProgram GProgram;

	/**
	 * This method creates the ground plane, left wall, and right wall.
	 * @param GProgram
	 */
	public ppTable(GraphicsProgram GProgram) {
		this.GProgram = GProgram;

		// Create the ground plane
		drawGroundLine();
		// Create the upper plane
		

	}
	/**
	 * Method to convert from world to screen coordinates.
	 * @param P a point object in world coordinates
	 * @return p the corresponding point object in screen coordinates
	 */

	public GPoint W2S (GPoint P) {

		double X = P.getX();
		double Y = P.getY();
		double x = (X-Xmin)*(xmax-xmin)/(Xmax-Xmin);
		double y = ymax-(Y-Ymin)*(ymax-ymin)/(Ymax-Ymin);
		return new GPoint(x,y);


	}
	/**
	 * Method to convert from screen to world coordinates.
	 * @param p a point object in screen coordinates
	 * @return P the corresponding point object in screen coordinates
	 */

	public GPoint S2W (GPoint p) {
		double x = p.getX();
		double y = p.getY();
		double X = (x*(Xmax-Xmin)/xmax-xmin)+Xmin;
		double Y= ((-(y-ymax)/ymax-ymin)*(Ymax-Ymin))+Ymin;
		return new GPoint(X,Y);

	}

	/**
	 * Erase all objects on the display (except buttons) and draws a new ground plane
	 */
	public void newScreen() {
		GProgram.removeAll();
		drawGroundLine();
	}
	/**
	 * Draws the ground line
	 */
	public void drawGroundLine() {
		GRect gPlane = new GRect(0,HEIGHT,WIDTH+OFFSET,3);   // A thick line HEIGHT pixels down from the top
		gPlane.setColor(Color.BLACK);
		gPlane.setFilled(true);
		GProgram.add (gPlane);
	}
	
	



}









