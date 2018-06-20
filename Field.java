package superflowerwars64;

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
  /**Unterer Nachbar*/
  private Field vertical = null;
  /*/**Position auf dem Brett
  private Position [] pos = new Position[3];*/
  /**Wenn es ein Blumenbeet hat, welcher Farbe ist es?*/
  private PlayerColor color = null;
  /**Markierung für die legal/illegal Strategie aus der 'Hilfestellung für Implementation'*/
  private int clustermark = 0;


  public Field (Position eins, Position zwei, Position drei) {
    super(eins, zwei, drei);

  }//END CONSTRUCTOR


  @Override
  public String toString() {
    return "Field " + getFirst() + ", " + getSecond() + ", " + getThird() + color + "\n";
  }//TESTING!!!!!!!!!!!!!!


  public void setColor(PlayerColor c) {
    color = c;
  }

}//END CLASS