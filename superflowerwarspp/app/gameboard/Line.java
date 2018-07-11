package app.gameboard;
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

class Line {
    private int x1;
    private int y1;
    private int x2;
    private int y2;
    private Color playerColor;
    private Line2D line;
    private boolean isClicked = false;

    public Line(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        line = new Line2D.Float(x1, y1, x2, y2);
    }
    public boolean isLineClicked() {
        return isClicked;
    }

    public void setLineClicked() {
        isClicked = true;
    }

    public int getX1() {
        return x1;
    }

    public int getY1() {
        return y1;
    }

    public int getX2() {
        return x2;
    }

    public int getY2() {
        return y2;
    }

    public Line2D getLine() {
      return line;
    }

    public String toString() {
        return ("(" + x1 + "," + y1 + ") --- (" + x2 + "," + y2 + ")");
    }
}
