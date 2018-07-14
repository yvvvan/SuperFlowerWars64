package app.gameboard;
import flowerwarspp.preset.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
    * Die Klasse Triangle ist für die Repräsentation eines Feldes in der Dreicksgestalt.
    * @author Viktoriya Pak
*/
public class Triangle{
    /**
        * Enthält die x-Koordinaten der 3 Punkten eines Dreickes
    */
    private int[] xpoints;
    /**
        * Enthält die y-Koordinaten der 3 Punkten eines Dreickes
    */
    private int[] ypoints;
    /**
        * Die Anzahl von Punkten in einem Dreieck
    */
    private int npoints;
    /**
        * Mitteilt, ob diese Linie schon angeklickt wird oder nicht
    */
    private boolean isClicked = false;
    /**
        * Ein entsprechndes Polygon zu jedem Dreieck
    */
    private Polygon poly;
    private Color playerColor;
    /**
        * Konstruktor der Klasse initialisert die {@link #xpoints} und {@link #ypoints}
        * und erzeugt ein Dreieck-Polygon
    */
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
    /**
        * Getter-Methode für {@link #poly}
    */
    public Polygon getPolygon() {
        return poly;
    }

    /**
        * Getter-Methode für {@link #xpoints}
    */
    public int[] getXpoints() {
        return xpoints;
    }

    /**
        * Getter-Methode für {@link #ypoints}
    */
    public int[] getYpoints() {
        return ypoints;
    }

    /**
        * Setter-Methode für {@link #isClicked}
    */
    public void setTriangleClicked(boolean isClicked) {
        this.isClicked = isClicked;
    }

    /**
        * Überprüft, ob dieser Dreieck schon angeklickt wurde oder nicht
        * @return false wenn nicht, true - sonst
    */
    public boolean isTriangleClicked() {
        return isClicked;
    }

    /**
        * Setter-Methode für die Farbe von dem Dreieck
        * @param col Die neue Farbe des Dreiecks
    */

    public void setTriangleColor(Color col) {
        this.playerColor = col;
    }

    /**
        * Getter-Methode für {@link #playerColor}
    */
    public Color getTriangleColor() {
        return playerColor;
    }

    /**
        * Überschriebene toString()-Methode.. Liefert eine Textuelle Darstelung
        * eines Dreiecks der Gestalt [(x1,y1),(x2,y2),(x3,y3)]
    */

    @Override
    public String toString() {
        return "[(" + xpoints[0] + "," + ypoints[0] + ")," + "(" + xpoints[1] + "," + ypoints[1] + ")," + "(" + xpoints[2] + "," + ypoints[2] + ")]";
    }
}
