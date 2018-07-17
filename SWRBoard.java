package superflowerwars64;

import flowerwarspp.preset.*;
import java.util.*;

/**
* Klasse, die das Spielbrett intern realisiert.
* SWRBoard ueberprueft die Legalitaet eines Zuges, uebernimmt ihn ggf.
* Regeln und Vorraussetzungen basierend auf dem Projekt "Flowerwars" des
* allgemeinen Programmier Praktikums, Sommersemester 2018
* @author Maximilian Schlensog
*/
public class SWRBoard implements Board, Viewable {

  /**Die Groesse (in Dreieckskanten) des Spielbretts*/
  private int size;
  /**Status des Brettes*/
  private Status status;
  /**Aktueller Spieler*/
  private PlayerColor current = PlayerColor.Red;
  /**Interner Speicher fuer rote Flower-menge*/
  private HashSet<Flower> redflowerset;
  /**Interner Speicher fuer blaue Flower-menge*/
  private HashSet<Flower> blueflowerset;
  /**Interner Speicher fuer einzelne Felder auf dem Brett*/
  private HashSet<Field> fieldset;
  /**Interner Speicher fuer moegliche Zuege*/
  private HashSet<Move> moveset;
  /**Interner Speicher fuer rote Graeben-menge*/
  private HashSet<Ditch> redditchset;
  /**Interner Speicher fuer blaue Graeben-menge*/
  private HashSet<Ditch> blueditchset;

  /**Konstruktor, der die Groesse des Feldes (in Dreieckskanten) uebergeben bekommt*/
  public SWRBoard(int size) {
    if(size >= 3 && size <= 30) {
      this.size = size;
      redflowerset = new HashSet<Flower>(size * size);
      blueflowerset = new HashSet<Flower>(size * size);
      redditchset = new HashSet<Ditch>(size * size);
      blueditchset = new HashSet<Ditch>(size * size);
      fieldset = new HashSet<Field>(size * size);

      //Initialisierung---------------------------------------------------------
      fieldset.add(fieldconstructor(1, 1, fieldset));
      status = Status.Ok;
    }
    else {
      System.out.println("Spielbrett kann nicht initialisiert werden.\nSpielbrettgroesse muss groesser als oder gleich 3 und kleiner als oder gleich 30 sein.\n");
    }
  }//END SWRBOARD-CONSTRUCTOR
  //============================================================================
  /**Methode, die den aktuellen Spieler zurueckgibt*/
  public PlayerColor getCurrentPlayer() {
    return current;
  }//END GETCURRENTPLAYER
  //============================================================================
  /**Methode, die den aktuellen Status zurueckgibt*/
  public Status getStatus() {
    return status;
  }//END GETSTATUS
  //============================================================================
  /**Methode, die die aktuellen Spielerfarbe veraendert*/
  public void setCurrentPlayer(PlayerColor color) {
    current = color;
  }//END SETCURRENTPLAYER
  //============================================================================
  /**Konstruktor fuer den Viewer auf dem Brett*/
  @Override
  public Viewer viewer() {

    return new MyViewer(this, current, status);

  }//END VIEWER
  //============================================================================
  /**Methode, die die Groesse des Bretts zurueckgibt*/
  public int getSize () {
    return size;
  }//END GETSIZE
  //============================================================================
  /**Methode, die die Anzahl der Nachbarn einer Blume oder eines Feldes zurueckgibt*/
  public int getNeighborAmount (Flower flower) {

    for(Field field : fieldset) {
      if(field.equals(flower)) {
        return field.getNeighborAmount();
      }
    }
    return 0;
  }//END GETNEIGHBORAMOUNT
  //============================================================================
  /**Hilfsmethode fuer die Initialisierung des Spielbretts mit Spielfeldern.
   * Diese Methode konstruiert Felder die aufrecht stehen.
   */
  private Field fieldconstructor(int i, int j, Collection<Field> coll) {

    Field f = new Field(new Position(i, j), new Position(i, j+1), new Position(i+1, j));
    Field ff = new Field(new Position(i, j+1), new Position(i+1, j), new Position(i+1, j+1));//Vertikaler nachbar


    if((i+j) < (size + 1)) {//WENN I+J == SIZE, IST MAN AN DER RECHTEN KANTE ANGEKOMMEN!
      ff = invertedfieldconstructor(i, j+1, coll);
      f.setRight(ff);
      ff.setLeft(f);
      coll.add(ff);

    }
    return f;

  }//END FIELDCONSTRUCTOR
  //============================================================================
  /**Hilfsmethode fuer die Initialisierung des Spielbretts mit Spielfeldern.
   * Diese Methode konstruiert Felder die umgekehrt stehen.
   */
  private Field invertedfieldconstructor(int i, int j, Collection<Field> coll) {

    Field f = new Field(new Position(i, j), new Position(i+1, j-1), new Position(i+1, j));
    Field ff = new Field(new Position(i, j), new Position(i, j+1), new Position(i+1, j));//Vertikaler nachbar
    Field fff = new Field(new Position(i+1, j-1), new Position(i+1, j+1), new Position(i+1, j)); //DIES IST DER RECHTE NACHBAR

    Field vert = null;
    Field right = null;

    for(Field x : coll) {
      if(x.equals(ff)) {
        vert = x;
      }
      if(x.equals(fff)) {
        right = x;
      }
    }
    if(vert == null) {
      ff = fieldconstructor(i, j, coll);
      ff.setVertical(f);
      f.setVertical(ff);
      coll.add(ff);
    }
    else{
      vert.setVertical(f);
      f.setVertical(vert);
    }
    if(right == null) {
      fff = fieldconstructor(i+1, j-1, coll);
      fff.setLeft(f);
      f.setRight(fff);
      coll.add(fff);
    }
    else{
      right.setLeft(f);
      f.setRight(right);
    }
    return f;

  }//END INVERTEDFIELDCONSTRUCTOR
  //============================================================================
  /**Methode, die die Verarbeitung von Zuegen uebernimmt und unter Umstaenden
   * an weitere Methoden weitergibt.
   */
  @Override
  public void make(final Move move) throws IllegalStateException {
    MoveType type = move.getType();

    HashSet<Flower> playerflowers;
    HashSet<Ditch> playerditches;

    if(current == PlayerColor.Red) {
      playerflowers = redflowerset;
      playerditches = redditchset;
    }
    else {
      playerflowers = blueflowerset;
      playerditches = blueditchset;
    }

    if(status == Status.RedWin || status == Status.BlueWin || status == Status.Draw) {
      throw new IllegalStateException("Illegal");
      /*System.out.println("Spiel vorbei. Es duerfen keine weiteren Zuege gemacht werden.");
      return;*/
    }

    switch (type) {
      case Flower:
          if(flowerMove(move, playerflowers)) {
            nextPlayer();
            status = Status.Ok;
          }
          else {
            status = Status.Illegal;
          }
          break;
        //END CASE FLOWER-------------------------------------------------------------------------

      case Ditch:
        if(ditchMove(move, playerditches, playerflowers)) {
          nextPlayer();
          status = Status.Ok;
        }
        else {
          status = Status.Illegal;
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
        Collection<Move> possiblemoves = getPossibleMoves();
        for(Move m : possiblemoves) {
          if(m.getType() == MoveType.Flower) {
            status = Status.Illegal;
            return;
          }
        }
        if(getPoints(PlayerColor.Red) > getPoints(PlayerColor.Blue)) {
          status = Status.RedWin;
        }
        else if(getPoints(PlayerColor.Red) < getPoints(PlayerColor.Blue)) {
          status = Status.BlueWin;
        }
        else if(getPoints(PlayerColor.Red) == getPoints(PlayerColor.Blue)){
          status = Status.Draw;
        }
        else {
          status = Status.Illegal;
        }
        break;
        //END CASE END----------------------------------------------------------

      default:
        throw new MoveFormatException("Move hatte keinen gueltigen Typ.");
    }//END SWITCH

  }//END MAKE
  //============================================================================
  /**Methode um schnell und einfach den naechsten Spieler zu setzen*/
  private void nextPlayer () {
    if(current == PlayerColor.Red) {
      current = PlayerColor.Blue;
    }
    else {
      current = PlayerColor.Red;
    }
  }//END NEXTPLAYER
  //============================================================================
  /*Methode, die die Menge aller gültigen Züge zurückliefert
   * author: Yufan Dong
   */
     public Collection<Move> getPossibleMoves() {

           HashSet<Ditch> ditchset = new HashSet<Ditch>();
            ditchset.addAll(redditchset);
            ditchset.addAll(blueditchset);


           HashSet<Flower> flowerset = new HashSet<Flower>();
           flowerset = (current == PlayerColor.Red)?redflowerset:blueflowerset;

           HashSet<Flower> allflowerset = new HashSet<Flower>();
           allflowerset.addAll(redflowerset);
           allflowerset.addAll(blueflowerset);

           HashSet<Flower> gardenset = new HashSet<Flower>();
           for(Flower flower : flowerset){
             for(Field field : fieldset){
               if(field.equals(flower)){
                 if(field.getClusteramount() == 4){
                   gardenset.add(field);
                 }
               }
             }
           }

           HashSet<Field> beet3set = new HashSet<Field>();
           for(Flower flower : flowerset){
             for(Field field : fieldset){
               if(field.equals(flower)){
                 if(field.getClusteramount() == 3){
                   beet3set.add(field);
                 }
               }
             }
           }

           HashSet<Field> beetNachbarnset = new HashSet<Field>();
           for(Field flower : beet3set){
               beetNachbarnset.add(flower.getLeft());
               beetNachbarnset.add(flower.getRight());
               beetNachbarnset.add(flower.getVertical());
           }
           beetNachbarnset.removeAll(beet3set);

         //FLOWER
           HashSet<Flower> unpossibleF = new HashSet<Flower>();

           //alle besizte Field (red+blue)
           unpossibleF.addAll(redflowerset);
           unpossibleF.addAll(blueflowerset);

           //alle nachbarn und ecke von Garten (eigne)
           for(Flower flower : gardenset){
             Position[] p =new Position[]{flower.getFirst(),flower.getSecond(),flower.getThird()};
             for(int i = 0; i<3 ; i++){
               int a = p[i].getColumn();
               int b = p[i].getRow();
               if(a-1>0){
                 ckadFlower(unpossibleF, p[i], new Position(a-1,b),   new Position(a-1,b+1) );
                 ckadFlower(unpossibleF, p[i], new Position(a,b+1),   new Position(a-1,b+1) );}
                 ckadFlower(unpossibleF, p[i], new Position(a,b+1),   new Position(a+1,b) );
               if(b-1>0){
                 ckadFlower(unpossibleF, p[i], new Position(a+1,b-1), new Position(a+1,b) );
                 ckadFlower(unpossibleF, p[i], new Position(a+1,b-1), new Position(a,b-1) );
                if(a-1>0)
                 ckadFlower(unpossibleF, p[i], new Position(a-1,b),   new Position(a,b-1) );}
             }
           }

           //alle Nachbar von ditch (red+blue)
           for(Ditch d : ditchset){
             //(a,b) (c,x)
             int a = d.getFirst().getColumn();
             int b = d.getFirst().getRow();
             int c = d.getSecond().getColumn();
             int x = d.getSecond().getRow();

             int diff1 = a - c;
             int diff2 = b - x;

             if      (diff1 == 0){ //a = c, in one column: like "/"
               if(a-1>0)
               ckadFlower(unpossibleF, d.getFirst(), d.getSecond(), new Position(a-1,Math.max(b,x)));
               ckadFlower(unpossibleF, d.getFirst(), d.getSecond(), new Position(a+1,Math.min(b,x)));
             }
             else if (diff2 == 0){ //b = d, in one row: like "-"
               ckadFlower(unpossibleF, d.getFirst(), d.getSecond(), new Position(Math.min(a,c),b+1));
               if(b-1>0)
               ckadFlower(unpossibleF, d.getFirst(), d.getSecond(), new Position(Math.max(a,c),b-1));
             }
             else {  //diff1 !=0 && diff2 != 0      // like "\"
               ckadFlower(unpossibleF, d.getFirst(), d.getSecond(), new Position(a,x));
               ckadFlower(unpossibleF, d.getFirst(), d.getSecond(), new Position(c,b));
             }
           }

           HashSet<Move> possibleM = new HashSet<Move>();
           HashSet<Flower> possibleF = new HashSet<Flower>();
           possibleF.addAll(fieldset);
           possibleF.removeAll(unpossibleF);
           for(Flower f1 : possibleF){
             for(Flower f2 : possibleF){
               if (!f1.equals(f2) && (!beetNachbarnset.contains(f1)||!beetNachbarnset.contains(f2))){
                 Move m = new Move(f1,f2);
                 possibleM.add(m);
               }
             }
           }

         //DITCH
           HashSet<Ditch> possibleD = new HashSet<Ditch>();

           HashSet<Position> points = new HashSet<Position>();
           for(Flower flower : flowerset){
             points.add(flower.getFirst());
             points.add(flower.getSecond());
             points.add(flower.getThird());
           }

           HashSet<Position> unPossibleP = new HashSet<Position>();
           for(Ditch d : ditchset){
             unPossibleP.add(d.getFirst());
             unPossibleP.add(d.getSecond());
           }

           points.removeAll(unPossibleP);

           for(Position p1 : points){
             for(Position p2 : points){
               if(!p1.equals(p2)){
                 int a = p1.getColumn();
                 int b = p1.getRow();
                 int c = p2.getColumn();
                 int d = p2.getRow();

                 int diff1 = Math.abs(a - c);
                 int diff2 = Math.abs(b - d);

               if((diff1 == 0 && diff2 == 1)||(diff1 == 1 && diff2 == 0)||(a-c==d-b)){
                 if (diff1 == 0 && diff2 == 1){ //a = c, in one column: like "/"
                   Flower f1 = (a-1>0)?(new Flower(p1,p2, new Position(a-1,Math.max(b,d)))):null;
                   Flower f2 = new Flower(p1,p2, new Position(a+1,Math.min(b,d)));
                   ckadDitch(possibleD,allflowerset,f1,f2,p1,p2);
                 }
                 else if (diff2 == 0 && diff1 == 1){ //b = d, in one row: like "-"
                   Flower f1 = new Flower(p1,p2, new Position(Math.min(a,c),b+1));
                   Flower f2 = (b-1>0)?(new Flower(p1,p2, new Position(Math.max(a,c),b-1))):null;
                   ckadDitch(possibleD,allflowerset,f1,f2,p1,p2);
                 }
                 else{ // (diff1 == 1 && diff2 == 1)                // like "\"
                   Flower f1 = new Flower(p1,p2, new Position(a,d));
                   Flower f2 = new Flower(p1,p2, new Position(c,b));
                   ckadDitch(possibleD,allflowerset,f1,f2,p1,p2);
                 }

                 // if ((f1==null||!allflowerset.contains(f1))&&(f2==null||!allflowerset.contains(f2)))
                 //   possibleD.add(new Ditch(p1,p2));
               }
               }
             }
           }

             for(Ditch d : possibleD){
               Move m = new Move(d);
               possibleM.add(m);
             }

           //Surrender
           possibleM.add(new Move(MoveType.Surrender));

           return possibleM;
 }//END GETPOSSIBLEMOVES
 //============================================================================
 private void ckadDitch(Collection<Ditch> possibleD ,Collection<Flower> allflowerset ,Flower f1,Flower f2,Position p1,Position p2){

   if ((f1==null||!allflowerset.contains(f1))&&(f2==null||!allflowerset.contains(f2)))
     possibleD.add(new Ditch(p1,p2));

 }//END CKADDITCH
 //======================================================================

 private void ckadFlower(Collection<Flower> list ,Position p1,Position p2,Position p3){
   if((p1!=p2)&&(p2!=p3)&&(p3!=p1)){
       Position[] p = new Position[]{p1,p2,p3};
       boolean onBoard = true;
       for(int i = 0; i<3; i++){
           int a = p[i].getColumn();
           int b = p[i].getRow();
           if(a<0 || b<0 ||(a+b)>size+1){
               onBoard = false;
           }
       }
       if(onBoard){
           Flower f = new Flower(p1,p2,p3);
           if(fieldset.contains(f))
           list.add(f);
       }
   }
 }//END CKADFLOWER

  //============================================================================
  /**Methode, die die Menge aller Blumen eines Spielers als Hashset zurueckgibt*/
  public Collection<Flower> getFlowers(final PlayerColor color) {
    if(color == PlayerColor.Red) {
      return redflowerset;
    }
    else {
      return blueflowerset;
    }
  }//END GETFLOWERS
  //============================================================================
  /**Methode, die die Menge aller Graeben eines Spielers als Hashset zurueckgibt*/
  public Collection<Ditch> getDitches(final PlayerColor color) {
    if(color == PlayerColor.Red) {
      return redditchset;
    }
    else {
      return blueditchset;
    }
  }//END GETDITCHES
  //============================================================================
  /**Hilfsmethode für make, die Zuege abhandelt, die Blumen setzen*/
  private boolean flowerMove (Move move, HashSet<Flower> playerflowerset) {//UNCHECKED

    Flower first = move.getFirstFlower();
    Flower second = move.getSecondFlower();

    if(redflowerset.contains(first) || redflowerset.contains(second)) {
      return false;
    }
    if(blueflowerset.contains(first) || blueflowerset.contains(second)) {
      return false;
    }

    if(first.equals(second)) {
      return false;
    }

    //vv Hier wird die Korrektheit der uebergebenen Blumen ueberprueft vv
    if(!checkFlowerPositions(first) || !checkFlowerPositions(second)) {
      return false;
    }

    playerflowerset.add(first); //Schritt Eins aus 'erkennen von gueltigen Zuegen'
    playerflowerset.add(second);
      if(isFlowerMoveLegal(playerflowerset)) {
        for(Field field : fieldset) {//Speichern der farbe in ein feld
          if(field.equals(first) || field.equals(second)) {
            field.setColor(current);
          }
        }
        return true;
      }
      else {
        playerflowerset.remove(first);
        playerflowerset.remove(second);
        if(isFlowerMoveLegal(playerflowerset)) {
        }/*dieser aufruf von isFlowerMoveLegal ist nur hier, um die korrekten...
        ...clusteramounts in die felder zu speichern, da diese benötigt werden*/
        return false;
      }
    //return true;
  }//END FLOWERMOVE
  //============================================================================
  /**Hilfsmethode fuer flowerMove, die die korrekte Position ueberprueft*/
  private boolean checkFlowerPositions (Flower flower) {

    Position pos1 = flower.getFirst();
    Position pos2 = flower.getSecond();
    Position pos3 = flower.getThird();

    //Summe einer Position darf nie size+2 ueberschreiten!
    if(!checkPosition(pos1, size) || !checkPosition(pos2, size) || !checkPosition(pos3, size)) {
      return false;
    }

    if((pos1.equals(pos2)) || (pos2.equals(pos3)) || (pos3.equals(pos1))) {
      return false;
    }

    /*vv Wenn einer dieser Graeben ungueltig sein sollte, ist somit auch
    die Blume ungueltig!*/
    Ditch one = new Ditch(pos1, pos2);
    Ditch two = new Ditch(pos2, pos3);
    Ditch three = new Ditch(pos3, pos1);

    if(checkDitchPositions(one) && checkDitchPositions(two) && checkDitchPositions(three)) {
      return true;
    }
    else {
      return false;
    }

  }//END CHECKFLOWERPOSITIONS
  //============================================================================
  /**Hilfsmethode fuer checkFlowerPositions, die ueberprueft, ob eine Position
   * auch wirklich auf dem Brett liegt
   */
  private boolean checkPosition(Position pos, int size) {
    //Summe einer Position darf nie size+2 ueberschreiten!
    int border = size + 2;

    int row = pos.getRow();
    int column = pos.getColumn();
    int sum = row + column;

    if(sum > border) {
      return false;
    }
    else {
      return true;
    }

  }//END CHECKPOSITION
  //============================================================================
  /**Hilfsmethode fuer flowerMove. Überprueft teilw. ueber andere Methoden
   * die Gueltigkeit eines Zuges, der Blumen beinhaltet.
   */
  private boolean isFlowerMoveLegal(HashSet<Flower> playerflowerset) {
    //UNCHECKED

    HashSet<Field> gardenset = new HashSet<Field>();
    HashSet<Field> saveableguys = new HashSet<Field>();
    int mark = 1; //1 = grau, andere ints sind andere farben fuer die felder
    int amount = 0; // fuer schritt 7
    //-vv Alle flowers kriegen eine graue Faerbung vv---------------------------
    for(Flower flower : playerflowerset) {
      for(Field field : fieldset) {
        if(field.equals(flower)) {
          if(field.getMark() != -2) {//-2 ->wenn ditch da ist
            field.setMark(mark);
          }
          else {
            cleanUpMarks();
            return false;
          }
        }
      }
    }//^^ --------------------------------------------------------------------^^
    //-vv Alle felder mit einer grauen Faerbung faerben sich und ihre grauen Nachbarn rekursiv
    for(Field f : fieldset) {
      if(f.getMark() == 1) {
        additionalColoring(f, ++mark);
      }//Schritte 3-6
    }//^^---------------------------------------------------------------------^^

    //vv schritte 7-10 vv
    for(int i = 2; i <= mark; i++) {//schritt 7
      amount = 0;
      for(Field f : fieldset) {
        if(f.getMark() == i) {
          saveableguys.add(f);  //saveableguys ist ein set, in dem felder vorgemerkt...
          amount++;             //...werden, damit ihre beet/garten menge an feldern rueckgespeichert werden kann
          if(amount > 4) { //hier schritt 9
            cleanUpMarks();
            return false;
          }
        }
      }//ENDE FOREACH
      for(Field f : saveableguys) {//hier werden saveableguys' mengen zurueckgespeichert
        f.setClusteramount(amount);
        if(amount == 4) {          //gleichzeitig werden sie in ein set in dem alle gartenfelder sind,...
          gardenset.add(f);        //...gespeichert, sobald die menge 4 ist
        }
      }//ENDE FOREACH
      saveableguys.clear();/* hier wird das set wieder geleert, damit nicht ...
                           die falschen amount-werte gespeichert werden*/
    }//ENDE FOR

    for(Field f : gardenset) {    //hier wird dann über alle felder die in einem...
      illegalColoring(f);         //...Garten sind, iteriert, damit diese die Felder um sich herum...
    }                             //nach der Gartenabstandsregel als ungültig markieren
    for(Field f : fieldset) {
      for(Flower flower: playerflowerset) {
        if(f.equals(flower) && (f.getMark() == -1)) { //hier wird das ganz ueberprueft:...
          cleanUpMarks();                             //wenn ein feld gefunden wird, was eine blume auf...
          return false;                               //sich hat UND die gartenabstandregel-verletzung...
        }                                             //als markierung hat, wird false zurueckgegeben
      }
    }

    cleanUpMarks();
    return true; //schritt 11
  }//ENDE ISFLOWERMOVELEGAL
  //============================================================================
  /**Methode die von isFlowerMoveLegal aufgerufen wird, um die Markierungen die
   * es setzt, wieder zu entfernen
   */
  private void cleanUpMarks () {

    for(Field f : fieldset) {
      if(f.getMark() != -2) {//alle Markierungen ausser die Ditch-markierung, da diese global für rot & blau gilt
        f.setMark(0);
      }
    }

  }//END CLEANUPMARKS
  //============================================================================
  /**Hilfsmethode fuer isFlowerMoveLegal, die dafuer sorgt, dass Beete/Gaerten sich selbst
   * und ihre Nachbarn in den zusaetzlichen Farben, basierend auf den Regeln
   * unter "Hilfestellungen zur Implementierung", markieren.
   */
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

  }//END ADDITIONALCOLORING
  //============================================================================
  /**Hilfsmethode fuer isFlowerMoveLegal, die Nachbarfelder von Gaerten markiert,
   * sodass diese nicht bebaut werden duerfen.
   */
  private void illegalColoring(Field field) {

    int mark = field.getMark();

    //vv wenn ein rechtes feld existiert und nicht dieselbe farbe hat wie es selbst vv
    if(field.getRight() != null) {
      if(field.getRight().getMark() != mark) {
        /*^^ in separaten klauseln, damit wenns in einer ist,
        nicht rumgeheult wird, wenn right = null ist*/
        field.getRight().setMark(-1);
      }
      if(field.getRight().getVertical() != null) {
        if(field.getRight().getVertical().getMark() != mark) {
          field.getRight().getVertical().setMark(-1);
        }//dann bekommt es die extra markierung -1, die aussagt, dass es nicht...
      }//...bebaut werden darf, da hier ein Garten ist
        //vv hier nochmal fuer rechten und oberen Nachbarn vv
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
    if(field.getLeft() != null) {
      if(field.getLeft().getMark() != mark) {
        /*^^ in separaten klauseln, damit wenns in einer ist,
        nicht rumgeheult wird, wenn right = null ist*/
        field.getLeft().setMark(-1);
      }
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
    if(field.getVertical() != null) {
      if(field.getVertical().getMark() != mark) {
        /*^^ in separaten klauseln, damit wenns in einer ist,
        nicht rumgeheult wird, wenn right = null ist*/
        field.getVertical().setMark(-1);
      }
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

  }//END ILLEGALCOLORING
  //============================================================================
  /**Methode, die die Verarbeitung von Zuegen uebernimmt, die einen Graben
   * setzen
   */
  private boolean ditchMove (Move move, HashSet<Ditch> playerditchset, HashSet<Flower> playerflowerset) {

    Ditch ditch = move.getDitch();

    int flowerconnectioncamount = 0; //um herauszufinden, ob auch wirklich 2 blumen verbunden sind, und nicht leere felder an einem Ende sind
    Position pos1 = ditch.getFirst();
    Position pos2 = ditch.getSecond();


    if(checkDitchPositions(ditch)) {//Legalitaet gecheckt!
      if(isNeighborEmpty(ditch)) {//FELDER WERDEN GLEICHZEITIG UNFRUCHTBAR GEMACHT!
        for(Ditch d : redditchset) {//checken ob ein anderer ditch dieselbe pos hat
          if(d.getFirst().equals(pos1) || d.getSecond().equals(pos1) || d.getFirst().equals(pos2) || d.getSecond().equals(pos2)) {
            return false;
          }
        }
        for(Ditch d : blueditchset) {//checken ob ein anderer ditch dieselbe pos hat
          if(d.getFirst().equals(pos1) || d.getSecond().equals(pos1) || d.getFirst().equals(pos2) || d.getSecond().equals(pos2)) {
            return false;
          }
        }
        for(Flower flower : playerflowerset) {
          if(isDitchConnectedToFlower(ditch, flower)){//zw. 2 Blumen gecheckt!
            flowerconnectioncamount++;
          }
        }//ENDE FOREACH
        if(flowerconnectioncamount > 1) { // da flowerconnectioncamount auch mehr als nur 2 blumen haben kann
          playerditchset.add(ditch);
          return true;
        }
      }
    }
    return false;

  }//END DITCHMOVE
  //============================================================================
  /**Hilfsmethode fuer ditchMove. Überprueft, ob eine Blume eine Position
   * mit dem Graben gemeinsam hat, und somit mit ihm verbunden ist.
   */
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
  /**Hilfsmethode fuer ditchMove, die die Gueltigkeit der Position eines ueber-
   * gebenen Graben ueberprueft.
   */
  private boolean checkDitchPositions (Ditch ditch) {

    int row1 = ditch.getFirst().getRow();
    int column1 = ditch.getFirst().getColumn();
    int row2 = ditch.getSecond().getRow();
    int column2 = ditch.getSecond().getColumn();

    int differenzRow = row1 - row2;

    int differenzColumn = column1 - column2;

    /*Die differenz zwischen 2 columns, 2 rows und deren summe duerfen nie mehr
    als |1| sein*/

    int summeCR = differenzRow + differenzColumn;
    if(summeCR < 0) {
      summeCR *= -1;
    }
    if(differenzRow < 0) { //in betrag machen,damit kein umstaendl. zeug spaeter
      differenzRow *= -1;
    }
    if(differenzColumn < 0) {
      differenzColumn *= -1;
    }

    if((differenzRow > 1) || (differenzColumn > 1) || (summeCR > 1)) {
      return false;
    }
    return true;

  }//END CHECKDITCHPOSTIONS
  //============================================================================
  /**Hilfsmethode fuer ditchMove. Überprueft, ob die angrenzenden Felder
   * unbebaut sind.
   */
  public boolean isNeighborEmpty (Ditch ditch) {

    HashSet<Field> markierte = new HashSet<Field>();
    HashSet<Field> foundfields = findFieldsOfDitch(ditch);

    for(Field f : foundfields) {
      if(f.getColor() == null) {
        markierte.add(f);
      }
      else {
        return false;
      }
    }
    for(Field f : markierte) {
      f.setMark(-2);//ditch unfruchtbarkeitsmarkierung
    }
    return true;


  }//END ISNEIGHBOREMPTY
  //============================================================================
  /**Hilfsmethode für isNeighborEmpty. Gibt die an einen Graben angrenzenden
   * Felder zurueck.
   */
  private HashSet<Field> findFieldsOfDitch (Ditch ditch) {

    HashSet<Field> foundfields = new HashSet<Field>();
    Position pos1 = ditch.getFirst();
    Position pos2 = ditch.getSecond();

    for(Field f : fieldset) {
      if((pos1.equals(f.getFirst()) || pos1.equals(f.getSecond()) || pos1.equals(f.getThird())) && (pos2.equals(f.getFirst()) || pos2.equals(f.getSecond()) || pos2.equals(f.getThird()))) {
        foundfields.add(f);
      }
    }

    return foundfields;

  }//END FINDFIELDSOFDITCH
  //============================================================================
  /**Methode, die die Punktzahl des uebergebenen Spielers zurueckgibt*/
  public int getPoints(final PlayerColor color) {
    HashSet<Flower> playerflowerset;
    HashSet<Ditch> playerditchset;
    HashSet<Field> gardenset = new HashSet<Field>(); //hier field damit ich mit cluster-x variablen arbeiten kann
    if(color == PlayerColor.Red) {
      playerflowerset = redflowerset;
      playerditchset = redditchset;
    }
    else {
      playerflowerset = blueflowerset;
      playerditchset = blueditchset;
    }
    int points = 0;
    int mark = 1; //1 = grau, andere ints siehe Field-Klasse unter clustermark
    //-vv Alle flowers kriegen eine graue Faerbung vv----------------------------
    for(Flower flower : playerflowerset) {
      for(Field field : fieldset) {
        if(field.equals(flower)) {
          field.setMark(mark);
          if(field.getClusteramount() == 4) {
            gardenset.add(field);
          }
        }
      }
    }
    //-vv Alle felder mit einer grauen Faerbung faerben sich und ihre grauen Nachbarn rekursiv vv
    for(Field f : fieldset) {
      if(f.getMark() == 1) {
        additionalColoring(f, ++mark);
      }
    }

    //bisher alle flowercluster in versch. farben makiert
    for(Field f : gardenset) {
      points += p(countingN(f, playerditchset));
    }

    cleanUpMarks();
    return points;
  }//END GETPOINTS
  //============================================================================
  /**Hilfsmethode fuer getPoints.*/
  private int countingN (Field field, HashSet<Ditch> playerditchset) {
    //NUR MIT GARDENSET SACHEN AUFRUFEN!
    int anzahl = 0;

    if(!field.getCheck()) {//damit keine dopplungen entstehen
      HashSet<Ditch> tobecheckedset = new HashSet<Ditch>();
      HashSet<Ditch> checkedoffset = new HashSet<Ditch>();
      HashSet<Field> tobecheckedfields = new HashSet<Field>();

      checkYourGarden(field, tobecheckedset, playerditchset);

      for(Ditch ditch : tobecheckedset) {
        if(!checkedoffset.contains(ditch)) {
          findFields(ditch, tobecheckedfields);
        }
      }
      for(Field f : tobecheckedfields) {
        anzahl += countingN(field, playerditchset);
      }

      if(field.getClusteramount() == 4) {
        anzahl++;
      }
    }

    return anzahl;
  }//END COUNTINGN
  //============================================================================
  /**Hilfsmethode fuer countingN. Alle Felder in demselben Garten wie das auf-
  rufende Feld 'sammeln' alle Graeben ein und checken sich selbst ab*/
  private void checkYourGarden (Field field, HashSet<Ditch> tobecheckedset, HashSet<Ditch> playerditchset) {
    //UNCHECKED
    field.setCheck(true);
      //vv hierfuer brauch ich die Faerbung aus getPoints! vv
    if(field.getRight() != null) {
      if(field.getRight().getMark() == field.getMark()) {
        if(!field.getRight().getCheck()) {
          checkYourGarden(field.getRight(), tobecheckedset, playerditchset);
        }
      }
    }
    if(field.getLeft() != null) {
      if(field.getLeft().getMark() == field.getMark()) {
        if(!field.getLeft().getCheck()) {
          checkYourGarden(field.getLeft(), tobecheckedset, playerditchset);
        }
      }
    }
    if(field.getVertical() != null) {
      if(field.getVertical().getMark() == field.getMark()) {
        if(!field.getVertical().getCheck()) {
          checkYourGarden(field.getVertical(), tobecheckedset, playerditchset);
        }
      }
    }
    for(Ditch d : playerditchset) {
      if(isDitchConnectedToFlower(d, field)) {
        tobecheckedset.add(d);
      }
    }
  }//END CHECKYOURGARDEN
  //============================================================================
  /**Hilfsmethode fuer getPoints. Berechnet die Punkte eines Garten-
  konglomerats auf Basis der in den Regeln festgelegten Weise*/
  private int p (int n) {//CHECKED

    if(n == 1) {
      return 1;
    }
    else return (p(n-1) + n);

  }//END P
  //============================================================================
  /**Hilfsmethode fuer countingN. addiert alle Felder an dem
   * uebergebenen Graben ins tobecheckedfields-set, wenn diese noch
   * nicht checkYourGarden aufriefen.
   */
  private void findFields (Ditch ditch, HashSet<Field> tobecheckedfields) {//UNCHECKED

    /*Hier kommen alle Felder rein, die auf der anderen Seite liegen.
      Denn Felder , die auf der anderen Seite liegen, muessen keine Nachbarn sein.
      Es koennen genauso gut Blumenbeete sein, die ueber Eck zsmliegen.
      Wenn also der countingN aufruf auf NUR EIN Feld ginge, wuerden nicht
      alle verbindungen ueberprueft. Trotzdem verhindert countingN mehrfache
      Aufrufe auf denselben Ditch!*/

      for(Field f : fieldset) {
        if(isDitchConnectedToFlower(ditch, f)) {
          tobecheckedfields.add(f);
        }
      }

  }//END FINDFIELDS
  //============================================================================

  public static void main (String[] args) {

    int size = 6;

    SWRBoard b = new SWRBoard(size);

    /*System.out.println(b.getCurrentPlayer());
    b.nextPlayer();
    System.out.println(b.getCurrentPlayer());*/

    //int testamount = 0;

    //Ditch doa = Ditch.parseDitch("{(5,5),(4,5)}");

    //Ditch doe = Ditch.parseDitch("{(4,5),(5,5)}");

    Ditch doi = Ditch.parseDitch("{(3,3),(3,2)}");
    Flower fa = Flower.parseFlower("{(3,2),(3,3),(4,2)}");
    Flower fi = Flower.parseFlower("{(1,4),(2,4),(2,3)}");
    Flower fu = Flower.parseFlower("{(2,3),(3,3),(2,4)}");
    Flower fe = Flower.parseFlower("{(4,2),(4,1),(3,2)}");



    Move mf = new Move(fa, fe);
    b.make(mf);
    b.nextPlayer();
    Move mv = new Move(fi, fu);
    b.make(mv);
    Move m = new Move(doi);
    b.make(m);
    System.out.println(b.getStatus());

    for(Field f : b.fieldset) {
      System.out.println(f);
    }


    //System.out.println(doa.equals(doe));

    /*Flower fo = Flower.parseFlower("{(3,6),(3,5),(4,5)}");
    Flower fa = Flower.parseFlower("{(5,5),(5,4),(6,4)}");

    b.redflowerset.add(fo);
    b.redflowerset.add(fa);

    Move m = new Move(doa);

    b.make(m);

    System.out.println(b.getStatus());*/

    /*HashSet<Flower> testset = new HashSet<Flower>();

    testset.add(fo);
    testset.add(fa);*/

  /*  for(Flower f : testset) {
      if(b.isDitchConnectedToFlower(doa, f)) {
        testamount++;
      }
    }

    System.out.println(testamount);*/

  /*  HashSet<Ditch> testset = new HashSet<Ditch>();

    testset.add(doa);

    for(Ditch d : testset) {
      if(d.equals(doe)) {
        System.out.println("true");
      }
    }*/

    //System.out.println(b.checkDitchPositions(doe));


    //checking isNeighborEmpty on empty board

    /*Ditch da = Ditch.parseDitch("{(2,1),(2,2)}");
    Ditch doe = Ditch.parseDitch("{(1,1),(2,2)}");
    Ditch dae = Ditch.parseDitch("{(1,1),(1,2)}");

    System.out.println(b.isNeighborEmpty(da));
    System.out.println(b.isNeighborEmpty(doe));
    System.out.println(b.isNeighborEmpty(dae));*/

    /*//checking isNeighborEmpty on filled Board

    Ditch da = Ditch.parseDitch("{(1,2),(2,2)}");
//CHECKING LATER
    Flower af = Flower.parseFlower("{(1,3),(2,2),(2,3)}");
    Flower bf = Flower.parseFlower("{(1,2),(2,2),(2,1)}");
    for(Ditch d : redditchset) {
      System.out.println(d);
    }
    b.redflowerset.add(af);
    b.isNeighborEmpty(da);
    b.redflowerset.add(bf);
    b.isNeighborEmpty(da);

*/

    //System.out.println(b.p(4));

  /*  HashSet<Integer> test1 = new HashSet<Integer>();
    for(int i = 1; i <= 5; i++) {
      test1.add(i);
    }
    HashSet<Integer> test2 = new HashSet<Integer>();
    for(Integer j : test1) {
      test2.add(j);
    }
    test2.remove(5);
    for(Integer i : test1) {
      System.out.println(i);
    }
    for(Integer i : test2) {
      System.out.println(i);
    }
    //int x = b.getPoints(b.getCurrentPlayer());*/
    //System.out.println(b.status);


  /*  Flower flower = new Flower(new Position(2,5), new Position(2,4), new Position(3,4));
    Flower flower1 = new Flower(new Position(2,4), new Position(3,4), new Position(3,3));
    Flower flower2 = new Flower(new Position(3,3), new Position(3,4), new Position(4,3));
    Flower flower3 = new Flower(new Position(3,3), new Position(4,3), new Position(4,2));

    Flower flower5 = new Flower(new Position(1,2), new Position(2,2), new Position(1,3));
    Flower flower6 = new Flower(new Position(2,2), new Position(3,2), new Position(2,3));
    Flower flower7 = new Flower(new Position(2,2), new Position(1,3), new Position(2,3));
    Flower flower8 = new Flower(new Position(1,3), new Position(2,3), new Position(1,4));

    //Flower flower4 = new Flower(new Position(3,2), new Position(2,3), new Position(3,3));


    b.redflowerset.add(flower);
    b.redflowerset.add(flower1);
    b.redflowerset.add(flower2);
    b.redflowerset.add(flower3);
    b.redflowerset.add(flower5);
    b.redflowerset.add(flower6);
    b.redflowerset.add(flower7);
    b.redflowerset.add(flower8);



    //b.redflowerset.add(flower4);

    for(Field f : b.fieldset) {
      System.out.println(f);
    }
    System.out.println("initial end");
    int mark = 1; //1 = grau, andere ints sind andere farben fuer die felder
    int amount = 0; // fuer schritt 7
    //-vv Alle flowers kriegen eine graue Faerbung vv---------------------------
    for(Flower flowery : b.redflowerset) {
      for(Field field : b.fieldset) {
        if(field.equals(flowery)) {
          if(field.getMark() != -2) {//-2 ->wenn ditch da ist
            field.setMark(mark);
          }
          else {
            b.cleanUpMarks();
          }
        }
      }
    }//^^ --------------------------------------------------------------------^^
    for(Field f : b.fieldset) {
      System.out.println(f);
      System.out.println("fVertical = " + f.getVertical());
    }
    System.out.println("after mark end");
    //-vv Alle felder mit einer grauen Faerbung faerben sich und ihre grauen Nachbarn rekursiv
    for(Field f : b.fieldset) {
      //System.out.println("this is called by  col. : " + f + " " + f.getVertical());
      if(f.getMark() == 1) {
        System.out.println("this is called by add col. : " + f + " " + f.getVertical());
        b.additionalColoring(f, ++mark);
      }//Schritte 3-6
    }//^^---------------------------------------------------------------------^^
    /*for(Field f : b.fieldset) {
      System.out.println(f);
    }*/
    /*System.out.println("after mark check");
    int samount;
    HashSet<Field> saveableguys = new HashSet<Field>();
    HashSet<Field> gardenset = new HashSet<Field>();

    //vv schritte 7-10 vv
    for(int i = 2; i <= mark; i++) {//schritt 7
      samount = 0;
      for(Field f : b.fieldset) {
        if(f.getMark() == i) {
          saveableguys.add(f);  //saveableguys ist ein set, in dem felder vorgemerkt...
          samount++;             //...werden, damit ihre beet/garten menge an feldern rueckgespeichert werden kann
          if(samount > 4) { //hier schritt 9
            b.cleanUpMarks();
          }
        }
      }//ENDE FOREACH

      for(Field f : saveableguys) {//hier werden saveableguys' mengen zurueckgespeichert
        System.out.println(f);
        f.setClusteramount(samount);
        if(samount == 4) {          //gleichzeitig werden sie in ein set in dem alle gartenfelder sind,...
          gardenset.add(f);        //...gespeichert, sobald die menge 4 ist
        }
      }//ENDE FOREACH
      saveableguys.clear();// hier wird das set wieder geleert, damit nicht ...
                          //... die falschen amount-werte gepseichert werden
    }//ENDE FOR
  /*  System.out.println("gardenset");
    for(Field f : gardenset) {

      System.out.println(f);
    }*/

      /*  for(Field f : gardenset) {    //hier wird dann über alle felder die in einem...
          b.illegalColoring(f);         //...Garten sind, iteriert, damit diese die Felder um sich herum...
        }                             //nach der Gartenabstandsregel als ungültig markieren
      /*  System.out.println("gardenset after ill. color.");
        for(Field f : gardenset) {
          System.out.println(f);
        }
        System.out.println("fieldset after illegal coloring");*/
        /*for(Field f : b.fieldset) {
          for(Flower flowerx: b.redflowerset) {
            if(f.equals(flowerx) && (f.getMark() == -1)) { //hier wird das ganz ueberprueft:...
              b.cleanUpMarks();                             //wenn ein feld gefunden wird, was eine blume auf...
            }                                             //als markierung hat, wird false zurueckgegeben
          }
        }
        for(Field f : b.fieldset) {
          System.out.println(f);
        }
        b.cleanUpMarks();


    //checkin checkDitchPositions
  /*  Ditch di = Ditch.parseDitch("{(1,2),(2,1)}");
    Ditch du = Ditch.parseDitch("{(1,1),(2,2)}");

    System.out.println(b.checkDitchPositions(di));
    System.out.println(b.checkDitchPositions(du));*/

    /*Move m = new Move(MoveType.Surrender);
    b.make(m);
    System.out.println(b.status);*/

  /*  Field [] fieldsettest = new Field[size * size];
    Field [] fieldsettest2 = new Field[size * size];

    b.fieldset.toArray(fieldsettest);
    fieldsettest2 = fieldsettest.clone();
    fieldsettest[0].setColor(PlayerColor.Red);

    for(Field f : b.fieldset) {
      System.out.println(f.toString());
    }
    for(Field f : fieldsettest2) {
      System.out.println(f.toString());
    }*/

  }//END MAIN
  //============================================================================


}//END CLASS
