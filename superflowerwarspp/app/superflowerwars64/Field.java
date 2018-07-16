package app.superflowerwars64;

import flowerwarspp.preset.*;

/**
*
* Klasse, die einzelne Felder auf dem Spielbrett darstellt.
*
* @author Maximilian Schlensog
*/

public class Field extends Flower{

  /**Linker Nachbar*/
  private Field left = null;
  /**Rechter Nachbar*/
  private Field right = null;
  /**Vertikaler Nachbar*/
  private Field vertical = null;
  /*/**Position auf dem Brett
  private Position [] pos = new Position[3];*/
  /**Wenn es ein Blumenbeet hat, welcher Farbe ist es?*/
  private PlayerColor color = null;

  /**Markierung für die legal/illegal Strategie aus der 'Hilfestellung für Implementation'
  * -2 = wenn ditch feld unfruchtbar gemacht hat
  * -1 = für einen Blumenzug ungültiger Zug
  * 0 = unmarkiert
  * 1 = 'grau',
  * alle anderen positiven Zahlen sind andere Farben
  */
  private int clustermark = 0;
  /**Anzahl der Blumenbeete im eigenen Cluster*/
  private int clusteramount = 0;
  /**Markierung für die getPoints-Methoden*/
  private boolean hasbeenchecked = false;
  /** Serialisierungskonstante */
  private static final long serialVersionUID = 1L;


  public Field (Position eins, Position zwei, Position drei) {
    super(eins, zwei, drei);

  }//END CONSTRUCTOR
  //============================================================================

  public int getNeighborAmount () {
    int amount = 0;
    if(left != null) {
      amount++;
    }
    if(right != null) {
      amount++;
    }
    if(vertical != null) {
      amount++;
    }
    return amount;
  }
  //============================================================================
  public void setCheck(boolean a) {
    hasbeenchecked = a;
  }
  //============================================================================

  public boolean getCheck() {
    return hasbeenchecked;
  }
  //============================================================================

  public void setRight(Field r) {
    right = r;
  }
  //============================================================================

  public Field getRight() {
    return right;
  }
  //============================================================================

  public void setLeft(Field l) {
    left = l;
  }
  //============================================================================

  public Field getLeft() {
    return left;
  }
  //============================================================================

  public void setVertical(Field v) {
    vertical = v;
  }
  //============================================================================

  public Field getVertical() {
    return vertical;
  }
  //============================================================================

  public void setClusteramount(int a) {
    clusteramount = a;
  }
  //============================================================================

  public int getClusteramount() {
    return clusteramount;
  }
  //============================================================================

  @Override
  public String toString() {
    return "Field " + getFirst() + ", " + getSecond() + ", " + getThird() + getClusteramount() + " " + getMark() + "\n";
  }//TESTING!!!!!!!!!!!!!!
  //============================================================================

  public void setColor(PlayerColor c) {
    color = c;
  }
  //============================================================================

  public PlayerColor getColor() {
    return color;
  }
  //============================================================================

  public void setMark(int mark) {
    clustermark = mark;
  }
  //============================================================================

  public int getMark() {
    return clustermark;
  }
  //============================================================================

}//END CLASS
