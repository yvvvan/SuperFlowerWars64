package app.gameboard;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
    * <h1>Punkt</h1>
    * Die Klasse enthält die Information zum Aufzeichnen eines Punktes
    * @author Viktoriya Pak
*/

public class Dots {
    /**
        * x-Koordinate von einem Punkt
    */
    private int x;
    /**
        * y-Koordinate von einem Punkt
    */
    private int y;

    /**
        * Konstruktor der Klasse
        * @param x x-Koordinate von einem Punkt zum Aufzeichnen
        * @param y y-Koordinate von einem Punkt zum Aufzeichnen
    */
    public Dots(int x, int y) {
      this.x = x;
      this.y = y;
    }
    /**
        * Überschriebene toString-Methode. Liefert die Position eines Punktes in Form (x,y)
    */
    @Override
    public String toString() {
      return "(" + x + "," + y + ")";
    }
    /**
        * Getter-Methode für x-Koordinate
    */
    public int getXCoord() {
      return x;
    }
    /**
        * Getter-Methode für y-Koordinate
    */
    public int getYCoord() {
      return y;
    }
}
