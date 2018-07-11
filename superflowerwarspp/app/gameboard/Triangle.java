package app.gameboard;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Triangle{

    private int[] xpoints;
    private int[] ypoints;
    private int npoints;
    private int x_coordinate;
    private int y_coordinate;
	private boolean isClicked = false;
    private Polygon poly;
    private Color playerColor;

    public Triangle(int[] xpoints, int[] ypoints, int npoints) {
        this.xpoints = new int[npoints];
        this.ypoints = new int[npoints];
           for(int i = 0; i < npoints; i++) {
               this.xpoints[i] = xpoints[i];
               this.ypoints[i] = ypoints[i];
           }
        this.npoints = npoints;
        this.poly = new Polygon(xpoints, ypoints, npoints);
    }

    public Polygon getPolygon() {
        return poly;
    }

    public int[] getXpoints() {
        return xpoints;
    }

    public int[] getYpoints() {
        return ypoints;
    }

    public void setTriangleClicked(boolean isClicked) {
        this.isClicked = isClicked;
    }

    public boolean isTriangleClicked() {
        return isClicked;
    }

    public void setTriangleColor(Color col) {
        this.playerColor = col;
    }

    public Color getTriangleColor() {
        return playerColor;
    }

    public String toString() {
        return "[(" + xpoints[0] + "," + ypoints[0] + ")," + "(" + xpoints[1] + "," + ypoints[1] + ")," + "(" + xpoints[2] + "," + ypoints[2] + ")]";
    }
}
