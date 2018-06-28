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

      /*for(Field x : fieldset) {
        System.out.println(x.toString());
      }//USED FOR TESTING*/

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
    else {  /*HIER WIRD GESCHAUT SOBALD DAS ENTSTEHENDE FELD BEREITS EXISTIERT.
      WENN ES DAS TUT, DANN WIRD ES NICHT INS SET GEADDET SONDERN ES WERDEN EINFACH
      NUR DIE NACHBARN AKTUALISIERT, DAMIT ZWEI VERSCH FELDER NICHT EIN UND DASSELBE
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
    else {  /*HIER WIRD GESCHAUT SOBALD DAS ENTSTEHENDE FELD BEREITS EXISTIERT.
      WENN ES DAS TUT, DANN WIRD ES NICHT INS SET GEADDET SONDERN ES WERDEN EINFACH
      NUR DIE NACHBARN AKTUALISIERT, DAMIT ZWEI VERSCH FELDER NICHT EIN UND DASSELBE
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
        if(current == PlayerColor.Red) {
          ditchMove(move, redditchset, redflowerset);
        }
        else {
          ditchMove(move, blueditchset, blueflowerset);
        }

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
        throw new MoveFormatException("Move hatte keinen gültigen Typ.");
        //^^ ANDERER EXCEPTION TYP VLLT^^ ?!?!?!?
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
      //bzw ne kopie des spielbretts
      boolean empty = playerflowerset.isEmpty();
      //^^wird hier init., damit sobald die flowers geaddet werden, das nicht sowieso nicht leer ist!^^

        playerflowerset.add(first); //Schritt Eins aus 'erkennen von gültigen Zügen'
        playerflowerset.add(second);
        if(!empty) {
          if(isFlowerMoveLegal(playerflowerset, first, second)) {
              status = Status.Ok;
              return true;
            }
          else {
            playerflowerset.remove(first);
            playerflowerset.remove(second);
            status = Status.Illegal;
            return false;
          }
        }
    return true;
  }//END FLOWERMOVE
  //============================================================================

  private boolean isFlowerMoveLegal(HashSet<Flower> playerflowerset, Flower first, Flower second) {

    Field [] fieldsetcopy = new Field[fieldset.size()];
    /*WENN ICH DAS ZU ARRAY MACHE, WERDEN INSTANZEN KOPIERT?
    IST NÄMLICH NICHT SO GUT, WENN ICH MARKIERUNGEN SETZE DIE DANN VON isFlowerMoveLegal
    GELESEN WERDEN, WENN DIE PLAYERCOLOR ANDERS IST!!!*/
    fieldset.toArray(fieldsetcopy);
    int mark = 1; //1 = grau, andere ints sind andere farben für die felder
    int amount = 0; // für schritt 7
    //-vv Alle flowers kriegen eine graue Färbung vv----------------------------
    for(Flower flower : playerflowerset) {
      for(Field field : fieldsetcopy) {
        if(field.equals(flower)) {
          if(field.getMark() != -2) {// ->wenn ditch da ist
            field.setMark(mark);
          }
          else {
            return false;
          }
        }
      }//IRGENDWAS BESSERES ALS 2 FOREACHs !?!?!?
    }
    //-vv Alle felder mit einer grauen Färbung färben sich und ihre grauen Nachbarn rekursiv
    for(Field f : fieldsetcopy) {
      if(f.getMark() == 1) {
        additionalColoring(f, ++mark);
      }//Schritte 3-6
    }//OB DAS GEHT!?!?!??!?!?!!??!!!!!!!!!!!!??????

    //vv schritte 7-11 vv
    for(int i = 2; i < mark; i++) {//schritt 7
      amount = 0;
      for(Field f : fieldsetcopy) {
        if(f.getMark() == i) {
          amount++;
        }
      }
      if(amount > 4) { //hier schritt 9
        System.out.println("FlowerMove ist nicht legal");
        //^^HIER WAHRSCHL LIEBER NE EXCEPTION DIE HOCHGEWORFEN WIRD, ODER?
        return false;
      }
      for(Field f : fieldsetcopy) {//rückspeichern der clustermenge in die felder
        if(f.getMark() == i) {
          f.setClusteramount(amount);
        }
      }
      // vv Schritt 10-1 (färben)
      if(amount == 4) {
        for(Field f : fieldsetcopy) {
          if(f.getMark() == i) {
            illegalColoring(f);
          }
        }
      }
      // vv Schritt 10-2 (überprüfen)
      for(Field f : fieldsetcopy) {
        if(f.equals(first) || f.equals(second)) {
          if(f.getMark() == -1) {
            return false;
          }
        }
      }

    }/*NE ECHT BESCHISSENE LÖSUNG, ÜBERLEGE OB ICH DAS IWIE IN ADDITIONALCOLORING
    BESSER LÖSEN KANN, BISHER KEINE IDEE*/



    return true;
  }//ENDE ISFLOWERMOVELEGAL
  //============================================================================

  private void additionalColoring (Field field, int mark) {

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

  private void illegalColoring(Field field) {

    int mark = field.getMark();
    /*Field right = null;
    if(field.getRight() != null) {
      right = field.getRight();
    }WIE WÄRS HIERMIT? ODER AUCH WIEDER STRESS WEGEN NULL SPÄTER?*/

    //vv wenn ein rechtes feld existiert und nicht dieselbe farbe hat wie es selbst vv
    if(field.getRight() != null) {
      if(field.getRight().getMark() != mark) {
        /*^^ in separaten klauseln, damit wenns in einer ist,
        nicht rumgeheult wird, wenn right = null ist*/
        field.getRight().setMark(-1);
        if(field.getRight().getVertical() != null) {
          if(field.getRight().getVertical().getMark() != mark) {
            field.getRight().getVertical().setMark(-1);
          }
        }
        if(field.getRight().getRight() != null) {
          if(field.getRight().getRight().getMark() != mark) {
            field.getRight().getRight().setMark(-1);
          }
          if(field.getRight().getRight().getVertical() != null) {
            if(field.getRight().getRight().getVertical().getMark() != mark) {
              field.getRight().getRight().getVertical().setMark(-1);
            }
          }
        }
      }
    }
    if(field.getLeft() != null) {
      if(field.getLeft().getMark() != mark) {
        /*^^ in separaten klauseln, damit wenns in einer ist,
        nicht rumgeheult wird, wenn right = null ist*/
        field.getLeft().setMark(-1);
        if(field.getLeft().getLeft() != null) {
          if(field.getLeft().getLeft().getMark() != mark) {
            field.getLeft().getLeft().setMark(-1);
          }
        }
        if(field.getLeft().getVertical() != null) {
          if(field.getLeft().getVertical().getMark() != mark) {
            field.getLeft().getVertical().setMark(-1);
          }
          if(field.getLeft().getVertical().getRight() != null) {
            if(field.getLeft().getVertical().getRight().getMark() != mark) {
              field.getLeft().getVertical().getRight().setMark(-1);
            }
          }
        }
      }
    }
    if(field.getVertical() != null) {
      if(field.getVertical().getMark() != mark) {
        /*^^ in separaten klauseln, damit wenns in einer ist,
        nicht rumgeheult wird, wenn right = null ist*/
        field.getVertical().setMark(-1);
        if(field.getVertical().getRight() != null) {
          if(field.getVertical().getRight().getMark() != mark) {
            field.getVertical().getRight().setMark(-1);
          }
        }
        if(field.getVertical().getLeft() != null) {
          if(field.getVertical().getLeft().getMark() != mark) {
            field.getVertical().getLeft().setMark(-1);
          }
          if(field.getVertical().getLeft().getLeft() != null) {
            if(field.getVertical().getLeft().getLeft().getMark() != mark) {
              field.getVertical().getLeft().getLeft().setMark(-1);
            }
          }
        }
      }
    }

  }//END ILLEGALCOLORING
  //============================================================================

  private boolean ditchMove (Move move, HashSet<Ditch> playerditchset, HashSet<Flower> playerflowerset) {

    Ditch ditch = move.getDitch();


    if(checkDitchPositions(ditch)) {//länge gecheckt!
      for(Flower flower : playerflowerset) {
        if(isDitchConnectedToFlower(ditch, flower)){//zw. 2 Blumen gecheckt!
          //for breaken oder so weiter machen?!
        }
      }

      playerditchset.add(ditch);
      return true;
    }
    else {
      return false;
    }

  }//END DITCHMOVE
  //============================================================================

  public boolean isDitchConnectedToFlower (Ditch ditch, Flower flower) {

    Position depos1 = ditch.getFirst();
    Position depos2 = ditch.getSecond();

    Position flpos1 = flower.getFirst();
    Position flpos2 = flower.getSecond();
    Position flpos3 = flower.getThird();

    if(depos1.equals(flpos1) || depos1.equals(flpos2) || depos1.equals(flpos3)) {
      return true;
    }//in zwei klauseln weil sonst zu unleserlich >.>
    if(depos2.equals(flpos1) || depos2.equals(flpos2) || depos2.equals(flpos3)) {
      return true;
    }
    return false;
  }//END ISDITCHCONNECTEDTOFLOWER
  //============================================================================

  private boolean checkDitchPositions (Ditch ditch) {

    int row1 = ditch.getFirst().getRow();
    int column1 = ditch.getFirst().getColumn();
    int row2 = ditch.getSecond().getRow();
    int column2 = ditch.getSecond().getColumn();

    int differenzRow = row1 - row2;

    int differenzColumn = column1 - column2;



    int summeCR = differenzRow + differenzColumn;
    if(summeCR < 0) {
      summeCR *= -1;
    }
    if(differenzRow < 0) { //in betrag machen,damit kein umständl. zeug später
      differenzRow *= -1;
    }
    if(differenzColumn < 0) {
      differenzColumn *= -1;
    }
    //^^KANN ICH HIER AUCH EINFACH UNSIGNED CASTEN!??!!?


    if((differenzRow > 1) || (differenzColumn > 1) || (summeCR > 1)) {
      return false;
    }
    return true;

  }//END CHECKDITCHPOSTIONS
  //============================================================================

  public int getPoints(final PlayerColor color) {
      System.out.println("getPoints got called, but it's not yet implemented :(");
      //kann hier mit clusteramount arbeiten, da das immer wenn move bneutzt wird, aufgerufen wird
       return 0;
  }//END GETPOINTS
  //============================================================================

  public static void main (String[] args) {
    SWRBoard b = new SWRBoard(3);
    //int x = b.getPoints(b.getCurrentPlayer());
    System.out.println(b.status);

    Flower flower = new Flower(new Position(1,1), new Position(1,2), new Position(2,1));
    b.redflowerset.add(flower);

    for(Field f : b.fieldset) {
      if(f.equals(flower)) {
        b.additionalColoring(f, 2);
        System.out.println(f.toString());
      }
    }

    //checkin checkDitchPositions
    /*Ditch di = Ditch.parseDitch("{(1,2),(2,1)}");
    Ditch du = Ditch.parseDitch("{(1,1),(2,2)}");

    System.out.println(b.checkDitchPositions(di));
    System.out.println(b.checkDitchPositions(du));*/

    Move m = new Move(MoveType.Surrender);
    b.make(m);
    System.out.println(b.status);




  }//END MAIN
  //============================================================================


}//END CLASS
