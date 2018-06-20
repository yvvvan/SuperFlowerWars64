package superflowerwars64;

import flowerwarspp.preset.*;
import java.util.*;

/**
* Klasse, die das Spielbrett intern realisiert
*
* @author Maximilian Schlensog
*/
public class SWRBoard implements Board, Viewable {

  /**Die Groeße (in Dreieckskanten) des Spielbretts*/
  private int size;
  /**Status des Brettes*/
  private Status status;
  /**Aktueller Spieler*/
  private PlayerColor current = PlayerColor.Red;
  /**Interner Speicher für Flower-menge*/
  private HashSet<Flower> flowerset;
  /**Interner Speicher für einzelne Felder auf dem Brett*/
  private HashSet<Field> fieldset;
  /**Interner Speicher für moegliche Zuege*/
  private HashSet<Move> moveset; //SIZE?!?!?!?!?
  /**Interner Speicher für Graeben-menge*/
  private HashSet<Ditch> ditchset; //SIZE?!?!?!?!??!

  /**Konstruktor, der die Größe des Feldes (in Dreieckskanten) übergeben bekommt*/
  public SWRBoard(int size) {
    if(size >= 3 && size <= 30) {
      this.size = size;
      flowerset = new HashSet(size * size);
      fieldset = new HashSet(size * size);

      //Initialisierung---------------------------------------------------------
      for(int i = 1; i <= (size); i++) {
        int j = 1;
        for(; j <= (size); j++) {
          //If you wanna make sense of this, look in early notes: "this exists!"
          if((i+j) <= (size+1)) {
            fieldset.add(new Field(new Position(i, j), new Position(i, j+1), new Position(i+1, j)));
            //STILL GOTTA DO NEIGHBORINOS HERE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            if((j-1) != 0) {
              fieldset.add(new Field(new Position(i, j), new Position(i+1, j), new Position(i+1, j-1)));
            }
          }
        }
      }
      //ende Init---------------------------------------------------------------

      /*for(Field x : fieldset) {
        System.out.println(x.toString());
      }USED FOR TESTING*/
      //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
      Flower f = new Flower(new Position(1,1), new Position(1,2), new Position(2,1));
      for(Field x : fieldset) {
        if(x.equals(f)) {
          x.setColor(current);
          System.out.println(x.toString());
        }
      }//FRIGGIN TESTING OVER HERE!!!!!!!!!!


    }
    else {
      System.out.println("Spielbrett kann nicht initialisiert werden.\nSpielbrettgröße muss größer als oder gleich 3 und kleiner als oder gleich 30 sein.\n");
    }
  }//END SWRBOARD-CONSTRUCTOR
  //============================================================================

  public PlayerColor getCurrentPlayer() {// TESTING METHOD!!!!!!!!
    return current;
  }

  public void setCurrentPlayer(PlayerColor color) {
    current = color;
  }//END SETCURRENTPLAYER
  //============================================================================

  @Override
  public Viewer viewer() {

    return new MyViewer(this, current, status);

  }//END VIEWER
  //============================================================================

  public int getSize () {
    return size;
  }//END GETSIZE
  //============================================================================

  @Override
  public void make(final Move move) throws IllegalStateException {
    MoveType type = move.getType();

    switch (type) {
      case Flower:
        //TO DO
        break;
        //END CASE FLOWER-------------------------------------------------------------------------

      case Ditch:
        //TO DO
        break;
        //END CASE DITCH-------------------------------------------------------------------------

      case Surrender:
        if(current == PlayerColor.Red) {
          status = Status.BlueWin;
        }
        else {
          status = Status.RedWin;
        }
        break;
        //END CASE SURRENDER-------------------------------------------------------------------------

      case End:
        if(getPoints(PlayerColor.Red) > getPoints(PlayerColor.Blue)) {
          status = Status.RedWin;
        }
        else if(getPoints(PlayerColor.Red) < getPoints(PlayerColor.Blue)) {
          status = Status.BlueWin;
        }
        else {
          status = Status.Draw;
        }
        break;
        //END CASE END-------------------------------------------------------------------------

      default:
        System.out.println("Make-methode hat keinen Movetype...?");
        break;//DO I NEED IT? I MEAN IT ENDS RIGHT THERE RIGHT?
        //END DEFAULT CASE------------------------------------------------------

    }

  }//END MAKE
  //============================================================================

  public Collection<Move> getPossibleMoves() {
        System.out.println("getPossibleMoves got called, but it's not yet implemented :(");
       return null;
  }//END GETPOSSIBLEMOVES
  //============================================================================

  public int getPoints(final PlayerColor color) {
      System.out.println("getPoints got called, but it's not yet implemented :(");
       return 0;
  }//END GETPOINTS
  //============================================================================

  public static void main (String[] args) {
    SWRBoard b = new SWRBoard(3);
    int x = b.getPoints(b.getCurrentPlayer());
    System.out.println(b.status);
    Move m = new Move(MoveType.Surrender);
    b.make(m);
    System.out.println(b.status);


  }//END MAIN
  //============================================================================


}//END CLASS
