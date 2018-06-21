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
  /**Interner Speicher für rote Flower-menge*/
  private HashSet<Flower> redflowerset;
  /**Interner Speicher für blaue Flower-menge*/
  private HashSet<Flower> blueflowerset;
  /**Interner Speicher für einzelne Felder auf dem Brett*/
  private HashSet<Field> fieldset;
  /**Interner Speicher für moegliche Zuege*/
  private HashSet<Move> moveset;
  /**Interner Speicher für Graeben-menge*/
  private HashSet<Ditch> ditchset;

  /**Konstruktor, der die Größe des Feldes (in Dreieckskanten) übergeben bekommt*/
  public SWRBoard(int size) {
    if(size >= 3 && size <= 30) {
      this.size = size;
      redflowerset = new HashSet(size * size);
      blueflowerset = new HashSet(size * size);
      fieldset = new HashSet(size * size);


      //Initialisierung---------------------------------------------------------

      int i = 1;
      int j = 1;
      //Laufvariablen
      fieldset.add(fieldconstructor(i, j, fieldset));
      //Keine duplikate, daher dass geaddet wird. aber nötig, da fieldconstr ein field returnt

      //ENDE Initialisierung----------------------------------------------------

      for(Field x : fieldset) {
        System.out.println(x.toString());
      }//USED FOR TESTING

    /*  Flower f = new Flower(new Position(1,1), new Position(1,2), new Position(2,1));
      for(Field x : fieldset) {
        if(x.equals(f)) {
          x.setColor(current);
          System.out.println(x.toString());
        }
      }//FRIGGIN TESTING OVER HERE!!!!!!!!!!*/


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

  public Field fieldconstructor(int i, int j, Collection<Field> coll) {

    Field f = new Field(new Position(i, j), new Position(i, j+1), new Position(i+1, j));

    if((i+j) < (size + 1)) {//WENN I+J == SIZE, IST MAN AN DER RECHTEN KANTE ANGEKOMMEN!
      Field ff = invertedfieldconstructor(i, j+1, coll);
      f.setRight(ff);
      ff.setLeft(f);
    }
    coll.add(f);
    return f;

  }//END FIELDCONSTRUCTOR
  //============================================================================

  public Field invertedfieldconstructor(int i, int j, Collection<Field> coll) {

    Field f = new Field(new Position(i, j), new Position(i+1, j-1), new Position(i+1, j));

    Field ff = fieldconstructor(i, j, coll); //DIES IST DER OBERE NACHBAR
    //EIN UMGEKEHRTES FELD HAT IMMER EINEN OBEREN NACHBARN
    f.setVertical(ff);
    ff.setVertical(f);

    Field fff = fieldconstructor(i+1, j-1, coll); //DIES IST DER RECHTE NACHBAR
    //EIN UMGEKEHRTES FELD HAT AUCH IMMER EINEN RECHTEN NACHBARN
    f.setRight(fff);
    fff.setLeft(f);
    coll.add(f);
    return f;

  }//END INVERTEDFIELDCONSTRUCTOR
  //============================================================================

  @Override
  public void make(final Move move) throws IllegalStateException {
    MoveType type = move.getType();

    switch (type) {
      case Flower:
        if(isFlowerMoveLegal(move)) {
          if(current == PlayerColor.Red) {
            redflowerset.add(move.getFirstFlower());
            redflowerset.add(move.getSecondFlower());
          }
          else {
            blueflowerset.add(move.getFirstFlower());
            blueflowerset.add(move.getSecondFlower());
          }
        }
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

    }//END SWITCH

  }//END MAKE
  //============================================================================

  public Collection<Move> getPossibleMoves() {
      System.out.println("getPossibleMoves got called, but it's not yet implemented :(");

      return null;
  }//END GETPOSSIBLEMOVES
  //============================================================================

  public boolean isFlowerMoveLegal (Move move) {

    if(move.getType() == MoveType.Flower) {
      //RLY CHECK EVERYTHING ACCORDING TO GIVEN GARTENBAU RULES?

      return true;
    }

    //THROW EXCEPTION OR PRINT SMTHG?

    return false;
  }//END ISFLOWERMOVELEGAL
  //============================================================================

  public boolean isDitchMoveLegal (Move move) {

    if(move.getType() == MoveType.Flower) {
      //TO DO!!!
      return true;
    }

    return false;
  }//END ISDITCHMOVELEGAL
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
