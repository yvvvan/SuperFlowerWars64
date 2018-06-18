package superflowerwars64;

import flowerwarspp.preset.*;
import java.util.*;

/**
* Klasse, die das Spielbrett intern realisiert
*
* @author Maximilian Schlensog
*/
public class SWRBoard implements flowerwarspp.preset.Board {

  /**Die Groeße (in Dreieckskanten) des Spielbretts*/
  private int size;
  /**Status des Brettes*/
  private Status status;
  /**Aktueller Spieler*/
  private PlayerColor current = PlayerColor.Red;

//  private HashSet<Int> set;

  /**Konstruktor, der die Größe des Feldes (in Dreieckskanten) übergeben bekommt*/
  public SWRBoard(int size) {
    if(size >= 3 && size <= 30) {
      this.size = size;
      //set = new HashSet<Int>(size*size);
    }
    else {
      System.out.println("Spielbrett kann nicht initialisiert werden.\nSpielbrett Größe muss größergleich 3 und kleinergleich 30 sein.");
    }
  }//END SWRBOARD-CONSTRUCTOR

  public PlayerColor getCurrentPlayer() {// TESTING METHOD!!!!!!!!
    return current;
  }

  @Override
  public Viewer viewer() {

    return new MyViewer(this, current, status);

  }//END VIEWER

  public int getSize () {
    return size;
  }//END GETSIZE

  @Override
  public void make(final Move move) throws IllegalStateException {
    MoveType type = move.getType();

    switch (type) {
      case Flower:
        //TO DO
        break;
      case Ditch:
        //TO DO
        break;
      case Surrender:
        //TO DO
        break;
      case End:
        //TO DO
        break;
      default:
          System.out.println("Make-methode hat keinen Movetype...?");
    }

  }//END MAKE

  public Collection<Move> getPossibleMoves() {
        System.out.println("getPossibleMoves got called, but it's not yet implemented :(");
       return null;
  }//END GETPOSSIBLEMOVES

  public int getPoints(final PlayerColor color) {
      System.out.println("getPoints got called, but it's not yet implemented :(");
       return 0;
  }//END GETPOINTS

  public static void main (String[] args) {
    SWRBoard b = new SWRBoard(3);
    int x = b.getPoints(b.getCurrentPlayer());

  }//END MAIN


}//END CLASS
