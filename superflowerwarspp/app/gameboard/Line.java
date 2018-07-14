package app.gameboard;
import flowerwarspp.preset.*;
import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;
import java.awt.image.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Float;
import java.awt.geom.Point2D;
/**
    * Die Klasse Line enthält die Information zum Zeichnen einer Linie, die einen Graben repräsentiert.
    * @author Viktoriya Pak
*/
class Line {
    /**
        * x-Koordinate des Start-Punktes einer Linie
    */
    private int x1;
    /**
        * y-Koordinate des Start-Punktes einer Linie
    */
    private int y1;
    /**
        * x-Koordinate des End-Punktes einer Linie
    */
    private int x2;
    /**
        * y-Koordinate des End-Punktes einer Linie
    */
    private int y2;
    //private Color playerColor;
    /**
        * Linie selbst
    */
    private Line2D line;
    /**
        * Mitteilt, ob diese Linie schon angeklickt wird oder nicht
    */
    private boolean isClicked = false;

    /**
        * Konstruktor der Klasse initialisert die x,y - Koordinate von Start- und End-Punkten und erzeugt
        * abhängig von diesen Werten eine {@link Line2D}-Linie
        * @param x1 x-Koordinate des Start-Punktes
        * @param y1 y-Koordinate des Start-Punktes
        * @param x2 x-Koordinate des End-Punktes
        * @param y2 y-Koordinate des End-Punktes
    */

    public Line(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        line = new Line2D.Float(x1, y1, x2, y2);
    }

    /**
        * Überprüft, ob diese Linie schon angeklickt wurde oder nicht
        * @return false wenn nicht, true - sonst
    */
    public boolean isLineClicked() {
        return isClicked;
    }

    /**
        * Setter-Methode für {@link #isClicked}
    */

    public void setLineClicked() {
        isClicked = true;
    }

    /**
        * Getter-Methode für x-Koordinate des Start-Punktes
    */

    public int getX1() {
        return x1;
    }

    /**
        * Getter-Methode für y-Koordinate des Start-Punktes
    */

    public int getY1() {
        return y1;
    }

    /**
        * Getter-Methode für x-Koordinate des End-Punktes
    */

    public int getX2() {
        return x2;
    }

    /**
        * Getter-Methode für y-Koordinate des End-Punktes
    */

    public int getY2() {
        return y2;
    }

    /**
        * Getter-Methode für {@link #line}
    */

    public Line2D getLine() {
        return line;
    }

   /**
        * Überschriebene toString()-Methode. Liefert eine Textuelle Darstelung
        * einer Linie der Gestalt (x1, y1) --- (x2, y2)
   */
   @Override
   public String toString() {
       return ("(" + x1 + "," + y1 + ") --- (" + x2 + "," + y2 + ")");
   }
}
