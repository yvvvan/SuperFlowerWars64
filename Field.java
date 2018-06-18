package superflowerwars64;

import flowerwarspp.preset.*;

/**
*
* Klasse, die einzelne Felder auf dem Spielbrett darstellt.
*
* @author Maximilian Schlensog
*/

public class Field {

  /**Linker Nachbar*/
  private Field left = null;
  /**Rechter Nachbar*/
  private Field right = null;
  /**Unterer Nachbar*/
  private Field down = null;
  /**Position auf dem Brett*/
  private Position [] pos = new Position[3];
  /**Ist es leer oder hat es ein Blumenbeet?*/
  private Flower flower = null;
  /**Wenn es ein Blumenbeet hat, welcher Farbe ist es?*/
  private PlayerColor color = null;

  public Field (Position eins, Position zwei, Position drei) {

    pos[0] = eins;
    pos[1] = zwei;
    pos[2] = drei;

  }//END CONSTRUCTOR

}//END CLASS
