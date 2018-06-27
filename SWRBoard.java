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
  /**Interner Speicher für rote Graeben-menge*/
  private HashSet<Ditch> redditchset;
  /**Interner Speicher für blaue Graeben-menge*/
  private HashSet<Ditch> blueditchset;

  /**Konstruktor, der die Größe des Feldes (in Dreieckskanten) übergeben bekommt*/
  public SWRBoard(int size) {
    if(size >= 3 && size <= 30) {
      this.size = size;
      redflowerset = new HashSet<Flower>(size * size);
      blueflowerset = new HashSet<Flower>(size * size);
      fieldset = new HashSet<Field>(size * size);


      //Initialisierung---------------------------------------------------------

      fieldset.add(fieldconstructor(1, 1, fieldset));
      status = Status.Ok;

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

  public int getNeighborAmount (Flower flower) {

    for(Field field : fieldset) {
      if(field.equals(flower)) {
        return field.getNeighborAmount();
      }
    }
    return 0;
  }//END GETNEIGHBORAMOUNT
  //============================================================================

  private Field fieldconstructor(int i, int j, Collection<Field> coll) {

    Field f = new Field(new Position(i, j), new Position(i, j+1), new Position(i+1, j));

    if((i+j) < (size + 1)) {//WENN I+J == SIZE, IST MAN AN DER RECHTEN KANTE ANGEKOMMEN!
      Field ff = invertedfieldconstructor(i, j+1, coll);
      f.setRight(ff);
      ff.setLeft(ff);
      coll.add(ff);

    }
    return f;

  }//END FIELDCONSTRUCTOR
  //============================================================================

  private Field invertedfieldconstructor(int i, int j, Collection<Field> coll) {

    Field f = new Field(new Position(i, j), new Position(i+1, j-1), new Position(i+1, j));

    Field ff = fieldconstructor(i, j, coll); //DIES IST DER OBERE NACHBAR
    //EIN UMGEKEHRTES FELD HAT IMMER EINEN OBEREN NACHBARN
    if(!coll.contains(ff)) {
      f.setVertical(ff);
      ff.setVertical(f);
      coll.add(ff);
    }
    else {  /*HIER WIRD GESCHAUT SOBALD DAS ENTSTEHENDE FELD BEREITS EXISTIERT
      WENN ES DAS TUT, DANN WIRD ES NICHT INS SET GEADDET SONDERN EINFACH NUR
      DIE NACHBARN AKTUALISIERT, DAMIT ZWEI VERSCH FELDER NICHT EIN UND DASSELBE
      FELD KONSTRUIEREN MIT FOLGENDER KOLLISION, DIE NUR EIN FELD MIT ZU WENIG
      NACHBARN IM SET LASSEN WÜRDE*/
      for(Field x : coll) {
        if(x.equals(ff)) {
          x.setVertical(f);
          f.setVertical(x);
        }
      }
    }
    //--------------------------------------------------------------------------
    Field fff = fieldconstructor(i+1, j-1, coll); //DIES IST DER RECHTE NACHBAR
    //EIN UMGEKEHRTES FELD HAT AUCH IMMER EINEN RECHTEN NACHBARN
    if(!coll.contains(fff)) {
      f.setRight(fff);
      fff.setLeft(f);
      coll.add(fff);
    }
    else {  /*HIER WIRD GESCHAUT SOBALD DAS ENTSTEHENDE FELD BEREITS EXISTIERT
      WENN ES DAS TUT, DANN WIRD ES NICHT INS SET GEADDET SONDERN EINFACH NUR
      DIE NACHBARN AKTUALISIERT, DAMIT ZWEI VERSCH FELDER NICHT EIN UND DASSELBE
      FELD KONSTRUIEREN MIT FOLGENDER KOLLISION, DIE NUR EIN FELD MIT ZU WENIG
      NACHBARN IM SET LASSEN WÜRDE*/
      for(Field x : coll) {
        if(x.equals(fff)) {
          x.setVertical(f);
          fff.setVertical(x);
        }
      }
    }
    return f;

  }//END INVERTEDFIELDCONSTRUCTOR
  //============================================================================

  @Override
  public void make(final Move move) throws IllegalStateException {
    MoveType type = move.getType();

    if(status == Status.RedWin || status == Status.BlueWin || status == Status.Draw) {
      //^^FUNKTIONIERT DAS HIER!??!?!?!!^^ oft kommt bad operand fehler!
      System.out.println("Spiel vorbei. Es dürfen keine weiteren Züge gemacht werden.");
      return;
    }

    switch (type) {
      case Flower:
        if(current == PlayerColor.Red) {
          flowerMove(move, redflowerset);
          }
        else {
          flowerMove(move, blueflowerset);
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
        //END CASE END----------------------------------------------------------

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

  public Collection<Flower> getFlowers(final PlayerColor color) {
    if(color == PlayerColor.Red) {
      return redflowerset;
    }
    else {
      return blueflowerset;
    }
  }//END GETFLOWERS
  //============================================================================

  public Collection<Ditch> getDitches(final PlayerColor color) {
    if(color == PlayerColor.Red) {
      return redditchset;
    }
    else {
      return blueditchset;
    }
  }//END GETDITCHES
  //============================================================================

  private boolean flowerMove (Move move, HashSet<Flower> playerflowerset) {

      Flower first = move.getFirstFlower();
      Flower second = move.getSecondFlower();
      HashSet<Field> otherset = new HashSet<Field>();  //set der zu den Blumen korrelierenden Felder
      boolean empty = playerflowerset.isEmpty();

        playerflowerset.add(first); //Schritt Eins aus 'erkennen von gültigen Zügen'
        playerflowerset.add(second);
        if(!empty) {
          if(isFlowerMoveLegal(playerflowerset)) {
              return true;
            }
          else {
            playerflowerset.remove(first);
            playerflowerset.remove(second);
            //HIER MUSS NOCH IWIE DER SPIELER NE NEUE CHANCE FÜR ZUG KRIEGEN!!!
            //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            return false;
          }
        }
    return true;
  }//END FLOWERMOVE
  //============================================================================

  private boolean isFlowerMoveLegal(HashSet<Flower> playerflowerset) {

    Field [] fieldsetcopy = new Field[fieldset.size()];
    fieldset.toArray(fieldsetcopy);
    int mark = 1; //1 = grau, andere ints sind andere farben für die felder
    int amount = 0; // für schritt 7

    for(Flower flower : playerflowerset) {
      for(Field field : fieldsetcopy) {
        if(field.equals(flower)) {
          field.setMark(mark);
        }
      }//IRGENDWAS BESSERES ALS 2 FOREACHs !?!?!?
    } //schritt zwei aus 'erkennen von gültigen zügen'

    for(Field f : fieldsetcopy) {
      if(f.getMark() == 1) {
        additionalColoring(f, ++mark);
      }//Schritte 3-6
    }//OB DAS GEHT!?!?!??!?!?!!??!!!!!!!!!!!!??????

    for(int i = 2; i < mark; i++) {
      amount = 0;
      for(field f : fieldsetcopy) {
        if(f.getMark() == i) {
          amount++;
        }
      }
      for(field f : fieldsetcopy) {
        if(f.getMark() == i) {
          f.setClusteramount(amount);
        }
      }
    }/*NE ECHT BESCHISSENE LÖSUNG, ÜBERLEGE OB ICH DAS IWIE IN ADDITIONALCOLORING
    BESSER LÖSEN KANN, BISHER KEINE IDEE*/

    //hier schritt 8

    return false;
  }//ENDE ISFLOWERMOVELEGAL
  //============================================================================

  public void additionalColoring (Field field, int mark) {

    field.setMark(mark);

    if(field.getRight() != null)  {
      if(field.getRight().getMark() == 1) {
        additionalColoring(field.getRight(), mark);
      }
    }
    if(field.getLeft() != null)  {
      if(field.getLeft().getMark() == 1) {
        additionalColoring(field.getLeft(), mark);
      }
    }
    if(field.getVertical() != null)  {
      if(field.getVertical().getMark() == 1) {
        additionalColoring(field.getVertical(), mark);
      }
    }
    //OB DAS GEHT!?!??!?!?!??!?!?!?!?!?

  }//END ADDITIONALCOLORING
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
    SWRBoard b = new SWRBoard(4);
    int x = b.getPoints(b.getCurrentPlayer());
    System.out.println(b.status);
    Move m = new Move(MoveType.Surrender);
    b.make(m);
    System.out.println(b.status);


  }//END MAIN
  //============================================================================


}//END CLASS
