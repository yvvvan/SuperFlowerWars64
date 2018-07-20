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

  /**
   * Konstruktor, der die Groesse des Feldes (in Dreieckskanten) uebergeben bekommt
   * @param size die Groesse von Brett
   */
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
  /**
   * Methode, die den aktuellen Spieler zurueckgibt
   * @return der aktuelle Spieler(Farbe)
   */
  public PlayerColor getCurrentPlayer() {
    return current;
  }//END GETCURRENTPLAYER
  //============================================================================
  /**
   * Methode, die den aktuellen Status zurueckgibt
   * @return der aktuelle Status
   */
  public Status getStatus() {
    return status;
  }//END GETSTATUS
  //============================================================================
  /**
   * Methode, die die aktuellen Spielerfarbe veraendert
   * @param color die Spielerfarber
   */
  public void setCurrentPlayer(PlayerColor color) {
    current = color;
  }//END SETCURRENTPLAYER
  //============================================================================
  /**
   * Konstruktor fuer den Viewer auf dem Brett
   * @return Viewer Object
   */
  @Override
  public MyViewer viewer() {

    return new MyViewer(this, current, status);

  }//END VIEWER
  //============================================================================
  /**
   * Methode, die die Groesse des Bretts zurueckgibt
   * @return die Groesse
   */
  public int getSize () {
    return size;
  }//END GETSIZE
  //============================================================================
  /**
   * Hilfsmethode fuer die Initialisierung des Spielbretts mit Spielfeldern.
   * Diese Methode konstruiert Felder die aufrecht stehen.
   * @param  i    Zeile
   * @param  j    Spalte
   * @param  coll Collection von Felder
   * @return      Felder
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
  /**
   * Hilfsmethode fuer die Initialisierung des Spielbretts mit Spielfeldern.
   * Diese Methode konstruiert Felder die umgekehrt stehen.
   * @param  i    Zeile
   * @param  j    Spalte
   * @param  coll Collection von Felder
   * @return      Felder
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
  /**
   * Methode, die die Anzahl der Nachbarn einer Blume oder eines Feldes zurueckgibt
   * @author Ding Zhou
   * @param  flower gesuchte Blume
   * @param  color  Farbe der Blume
   * @return        Anzahl der Nachbarn
   */
  public int getNeighborAmount (Flower flower,PlayerColor color) {
    HashSet<Flower> myset=null;
    if(color==PlayerColor.Red){
      myset=redflowerset; }
    else{
      myset=blueflowerset;
    }
    int number=0;
    for(Flower f:myset){
      if(isNeighbours (f,flower)){
        number++;
      }
    }
    return number;
  }//ENDE GETNEIGHBORAMOUNT
  //============================================================================
  /**
   * Hilfsmethode fuer getNeighborAmount
   * @author Ding Zhou
   * @param  flower1 gesuchte Blume1
   * @param  flower2 gesuchte Blume2
   * @return         ob die 2 Blumen Nachbarn sind
   */
  public boolean isNeighbours (Flower flower1,Flower flower2){
    Position pos1_1=flower1.getFirst();
    Position pos1_2=flower1.getSecond();
    Position pos1_3=flower1.getThird();
    ArrayList<Position> position=new ArrayList<Position>();
    position.add(pos1_1);
    position.add(pos1_2);
    position.add(pos1_3);
    Position pos2_1=flower2.getFirst();
    Position pos2_2=flower2.getSecond();
    Position pos2_3=flower2.getThird();
    Position []pos={pos2_1,pos2_2,pos2_3};
    int number=0;
    for(int i=0;i<3;i++){
      if(position.contains(pos[i])){
        number++;
      }
    }
    if(number==2){
      return true;
    }
    else{
      return false;
    }
  }//ENDE ISNEIGHBORS
  //============================================================================
  /**
   * Methode, die die Verarbeitung von Zuegen uebernimmt und unter Umstaenden
   * an weitere Methoden weitergibt.
   * @param  move                  ein Zug
   * @throws IllegalStateException Falls der Zug illegal ist
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
    }

    switch (type) {
      case Flower:
          if(flowerMove(move, playerflowers)) {
            nextPlayer();
            // if(getPossibleMovesFl().size()!=0){
            status = Status.Ok;
            // if(turns>size*size/2)
            checkthis();
          }
          // else status =Status.Draw;
          // }
          else {
            status = Status.Illegal;
          }
          break;
        //END CASE FLOWER-------------------------------------------------------------------------

      case Ditch:
        if(ditchMove(move, playerditches, playerflowers)) {
          nextPlayer();
          status = Status.Ok;
          // if(turns>size*size/2)
          checkthis();
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
            if(m.getType() == MoveType.Flower) {  //da ein legaler Zug mind. 2 Blumen...
              status = Status.Illegal;            //haben muss,reicht es, beim move-...
              return;                             //type "end", einfach zu schauen...
            }                                     //ob noch ein leg. Blumenzug ...
          }                                       //moeglich ist.

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
          // System.out.println(getPoints(PlayerColor.Red)+"y2f"+getPoints(PlayerColor.Blue));
          // System.out.println(status);
          // System.out.println(fieldset);
          // System.out.println();
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
  /**
   * geprüft ob für Aktueller Spieler noch FLOWERMOVE Gibt
   * weil getPossibleMoves sehr kommpliziert ist und sehr lang dauert
   * es wird zuerst alle nicht besonders zu beobachtetenen Felder gesucht
   * die sind nicht besitzte Blumen, nicht Blumen um Garten,
   * nicht Blumen um 2er/3er Beet
   * @author Yufan Dong
   */
  private void checkthis(){
    HashSet<Field> B2B3Gset = new HashSet<Field>();
      for(Field field : fieldset)
        if(field.getColor()==current)
         if(field.getClusteramount() == 2 || field.getClusteramount() == 4 || field.getClusteramount() == 3)
            B2B3Gset.add(field);

    HashSet<Field> check = new HashSet<Field>();
    check.addAll(fieldset);
    //gar nicht erlaubt
    check.removeAll(redflowerset);
    check.removeAll(blueflowerset);

    //muss gucken 2erBeet 3erBeet + gar nicht erlaubt Garten
    check.removeAll(aroundBeet(B2B3Gset));
    //der Rest sind alle nicht besonders beobachteten Felder
    //Falls es kein Zug(2Blumen) gibt, suchen wir gPM (gPM ist sehr offiziell, ganz und langsam)
    if(check.size()<2) {
    Collection<Move> gP = getPossibleMoves();
      if(gP.contains(new Move(MoveType.End))&&gP.size()==2) {
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
      }
    }
  }
  //============================================================================
  /**
  * Methode, die die Menge aller gueltigen Zuege zurueckliefert
  * @author Yufan Dong
  * @return Menge der moegliche Zuege
  */
  public Collection<Move> getPossibleMovesFl() {

    HashSet<Move> possibleM = new HashSet<Move>();

    HashSet<Ditch> ditchset = new HashSet<Ditch>();
    ditchset.addAll(redditchset);
    ditchset.addAll(blueditchset);

    HashSet<Flower> flowerset = new HashSet<Flower>();
    flowerset = (current == PlayerColor.Red)?redflowerset:blueflowerset;

    HashSet<Flower> allflowerset = new HashSet<Flower>();
    allflowerset.addAll(redflowerset);
    allflowerset.addAll(blueflowerset);

    HashSet<Field> gardenset = new HashSet<Field>();
    // for(Flower flower : flowerset)
    for(Field field : fieldset)
    // if(field.equals(flower))
      if(field.getColor()==current)
        if(field.getClusteramount() == 4)
          gardenset.add(field);
          // System.out.println("gt"+gardenset);

        HashSet<Field> beet3set = new HashSet<Field>();
        // for(Flower flower : flowerset)
        for(Field field : fieldset)
        // if (field.equals(flower))
          if(field.getColor()==current)
            if(field.getClusteramount() == 3)
              beet3set.add(field);
          // System.out.println("bts"+beet3set);

        HashSet<Field> beetNachbarnset = new HashSet<Field>(); //link recht oben|unter
        for(Field flower : beet3set){
          beetNachbarnset.add(flower.getLeft());
          beetNachbarnset.add(flower.getRight());
          beetNachbarnset.add(flower.getVertical());
        }
        beetNachbarnset.remove(null);
        beetNachbarnset.removeAll(beet3set);
        // System.out.println(aroundBeet(beet3set).containsAll(beetNachbarnset));

      //FLOWER----------------------------------------------------------------

        HashSet<Flower> possibleF = new HashSet<Flower>();
        possibleF.addAll(fieldset);

        //alle besizte Field (red+blue)
        possibleF.removeAll(redflowerset);
        possibleF.removeAll(blueflowerset);

        //alle felder von garten
        possibleF.removeAll(aroundBeet(gardenset));

        //alle Nachbar von ditch (red+blue)
        for(Ditch d : ditchset){
          possibleF.removeAll(findFieldsOfDitch(d));
        }

        possibleF.remove(null);

        HashSet<Move> myM = new HashSet<Move>();

        for(Flower f1 : possibleF){
          for(Flower f2 : possibleF){
            if (!f1.equals(f2)){
              Move n = new Move(f1,f2);
              if(myM.add(n)){
                if (!isBeetNachbar(f1,f2)){
                  Move m = new Move(f1,f2);
                  possibleM.add(m);
                }
              }
            }
          }
        }
        possibleM.remove(null);
        return possibleM;
  }
  //============================================================================
  /**
   * Methode, die die Menge aller gueltigen Zuege zurueckliefert
   * @author Yufan Dong
   * @return Menge der moegliche Zuege
   */
  public Collection<Move> getPossibleMoves() {

       HashSet<Move> possibleM = new HashSet<Move>();

       HashSet<Ditch> ditchset = new HashSet<Ditch>();
        ditchset.addAll(redditchset);
        ditchset.addAll(blueditchset);


       HashSet<Flower> flowerset = new HashSet<Flower>();
       flowerset = (current == PlayerColor.Red)?redflowerset:blueflowerset;

       HashSet<Flower> allflowerset = new HashSet<Flower>();
       allflowerset.addAll(redflowerset);
       allflowerset.addAll(blueflowerset);

       HashSet<Field> gardenset = new HashSet<Field>();
       // for(Flower flower : flowerset)
         for(Field field : fieldset)
           // if(field.equals(flower))
           if(field.getColor()==current)
             if(field.getClusteramount() == 4)
               gardenset.add(field);
        // System.out.println("gt"+gardenset);

       HashSet<Field> beet3set = new HashSet<Field>();
       // for(Flower flower : flowerset)
         for(Field field : fieldset)
           // if (field.equals(flower))
           if(field.getColor()==current)
            if(field.getClusteramount() == 3)
               beet3set.add(field);
      // System.out.println("bts"+beet3set);

       HashSet<Field> beetNachbarnset = new HashSet<Field>(); //link recht oben|unter
       for(Field flower : beet3set){
           beetNachbarnset.add(flower.getLeft());
           beetNachbarnset.add(flower.getRight());
           beetNachbarnset.add(flower.getVertical());
       }
       beetNachbarnset.remove(null);
       beetNachbarnset.removeAll(beet3set);
       // System.out.println(aroundBeet(beet3set).containsAll(beetNachbarnset));
      //

  //FLOWER----------------------------------------------------------------

       HashSet<Flower> possibleF = new HashSet<Flower>();
       possibleF.addAll(fieldset);


       //alle besizte Field (red+blue)
       possibleF.removeAll(redflowerset);
       possibleF.removeAll(blueflowerset);

       //alle felder von garten
       possibleF.removeAll(aroundBeet(gardenset));

       //alle Nachbar von ditch (red+blue)
       for(Ditch d : ditchset){
         possibleF.removeAll(findFieldsOfDitch(d));
       }

       possibleF.remove(null);

       HashSet<Move> myM = new HashSet<Move>();

       for(Flower f1 : possibleF){
         for(Flower f2 : possibleF){
           if (!f1.equals(f2)){
             Move n = new Move(f1,f2);
             if(myM.add(n)){
               if (!isBeetNachbar(f1,f2)){
                 Move m = new Move(f1,f2);
                 possibleM.add(m);
               }
             }
           }
         }
       }
  //End --------------------------------------------------------------------
      possibleM.remove(null);
      if(possibleM.size()==0) possibleM.add(new Move(MoveType.End));

  //DITCH -------------------------------------------------------------------
       HashSet<Ditch> possibleD = new HashSet<Ditch>();

       // Punkte von Ditch muessen von einger Blumen
       HashSet<Position> points = new HashSet<Position>();
       for(Flower flower : flowerset){
         points.add(flower.getFirst());
         points.add(flower.getSecond());
         points.add(flower.getThird());
       }

       //schon besitzt
       HashSet<Position> unPossibleP = new HashSet<Position>();
       for(Ditch d : ditchset){
         unPossibleP.add(d.getFirst());
         unPossibleP.add(d.getSecond());
       }

       points.removeAll(unPossibleP);

       for(Position p1 : points)
         for(Position p2 : points)
          if(!p1.equals(p2)){
            int a = p1.getColumn();
            int b = p1.getRow();
            int c = p2.getColumn();
            int d = p2.getRow();

            int diff1 = Math.abs(a - c);
            int diff2 = Math.abs(b - d);
       // length = 1
            if((diff1 == 0 && diff2 == 1)||(diff1 == 1 && diff2 == 0)||((a-c==d-b)&&diff1==1)){
              Ditch one = new Ditch(p1,p2);
      // Punkte von Ditch koennen nicht von einer Blume
              if (isNEmpty(one)) possibleD.add(one);
            }
          }

    for(Ditch d : possibleD){
        Move m = new Move(d);
        possibleM.add(m);
    }

    //Surrender ------------------------------------------------------------
    possibleM.add(new Move(MoveType.Surrender));

    possibleM.remove(null);

    return possibleM;
  }//END GETPOSSIBLEMOVES
//============================================================================
  private boolean isNEmpty(Ditch d){
    HashSet<Flower> allflowerset = new HashSet<Flower>();
    allflowerset.addAll(redflowerset);
    allflowerset.addAll(blueflowerset);
    for(Flower flower : allflowerset){
      HashSet<Position> p =new HashSet<Position>();
      p.add(flower.getFirst());p.add(flower.getSecond());p.add(flower.getThird());
      if(p.contains(d.getFirst())&&p.contains(d.getSecond())) return false;
    }
    return true;
  }
//============================================================================
/**
 * Hilfesmethode, wird gepruefte, ob f1 und f2 miteinandren beeinflussen koennen
 * @author Yufan Dong
 * @param  f1              Blume1
 * @param  f2              Blume2
 * @return                 ob f1 <-> f2 beeinflussen koennen
 */
  private boolean isBeetNachbar( Flower f1, Flower f2){

    int ck = 0;
    for(Field f : fieldset){
      if(f.equals(f1)){
        ck++;
        f.setColor(current);
        beet.clear();
        checked.clear();
        updateBs(f);
        //falls f1 gemacht wird, beet ist das Beet von f1
        //falls Beet mehr als 4 Blumen hat, oder um Beet schon besitzt ist
        if(beet.size()>4 || (beet.size()==4&&isAround(beet))){
          f.setColor(null);
          return true;
       //else wird problemlos hinzufugt und pruefen ob f2 auch problemlos ist.
        }
      }
    }
    if(ck!=1) System.out.println("FFFFFFFFFFFFFFFFFF1"); //check ob in einer Reihefolge ist
   for(Field f : fieldset){
      if(f.equals(f2)){
        ck++;
        f.setColor(current);
        beet.clear();
        checked.clear();
        updateBs(f);
        if(beet.size()>4 || (beet.size()==4&&isAround(beet))){
          f.setColor(null);
          for(Field ff : fieldset)
           if(ff.equals(f1))
             ff.setColor(null);
          return true;
        }
      }
    }
    if(ck!=2) System.out.println("FFFFFFFFFFFFFFFFFF2");
    for(Field f : fieldset){
     if(f.equals(f1)||f.equals(f2)){
       f.setColor(null);
       ck--;
     }
   }
   if(ck!=0) System.out.println("FFFFFFFFFFFFFFFFFF0");
    return false;
  }//END ISBEETNACHBAR
//============================================================================
/**
 * Hilfesmethode, wird geprueft, ob es um ein Beet besitzt ist
 * @author Yufan Dong
 * @param  Beet das gesuchte Beet
 * @return      ob das besitzt ist
 */
  private boolean isAround(HashSet<Field> Beet){
    HashSet<Field> aroundYou = aroundBeet(Beet);
    // System.out.println(aroundYou);
    for(Field f : aroundYou){
      if (f.getColor() == current) return true;
    }
    return false;
  }//END ISAROUND
//============================================================================
/**
 * Hilfesmethode, wird gesuchte, alle Felder, die um ein Beet stehen
 * @author Yufan Dong
 * @param  Beet das gesuchte Beet
 * @return      eine Menge von Felder, die um das Beet stehen
 */
  private HashSet<Field> aroundBeet(HashSet<Field> Beet){
    HashSet<Field> beetAroundset = new HashSet<Field>();
    for(Field flower : Beet){
      Position[] p =new Position[]{flower.getFirst(),flower.getSecond(),flower.getThird()};
      for(int i = 0; i<3 ; i++){
        int a = p[i].getColumn();
        int b = p[i].getRow();
        // System.out.println(a+" "+b);
        if(a-1>0){
          ckadField(beetAroundset , p[i], new Position(a-1,b),   new Position(a-1,b+1) );
          ckadField(beetAroundset , p[i], new Position(a,b+1),   new Position(a-1,b+1) );}
          ckadField(beetAroundset , p[i], new Position(a,b+1),   new Position(a+1,b) );
        if(b-1>0){
          ckadField(beetAroundset , p[i], new Position(a+1,b-1), new Position(a+1,b) );
          ckadField(beetAroundset , p[i], new Position(a+1,b-1), new Position(a,b-1) );
        if(a-1>0)
          ckadField(beetAroundset , p[i], new Position(a-1,b),   new Position(a,b-1) );
        }
      }
    }
    beetAroundset.removeAll(Beet);
    return beetAroundset;
  }//END AROUNDBEET
//============================================================================
 /**HilfsVariable , das Beet von einem Feld*/
 HashSet<Field> beet = new HashSet<Field>(size*size);
 /**HilfsVariable , von Methode updateBs genutzt wird*/
 HashSet<Field> checked = new HashSet<Field>(size*size);
//============================================================================
/**
 * Variable beet und Variable checked werden geaendert
 * beet is Beet around f
 * @author Yufan Dong
 * @param f Field f wird updated
 */
  private void updateBs(Field f){
    // falls beet mehr als 4 Blumen hat, einfach Illegal(in andere Methode)
    // nicht mehr weiter rechnen
    if(beet.size()< 5){ //dirket false
      if(f.getColor() != null) {
        beet.add(f);
        beet.add(f.getLeft());
        beet.add(f.getRight());
        beet.add(f.getVertical());
        beet.remove(null);
    //remove -> nicht gleiche Farbe
        HashSet<Field> remove = new HashSet<Field>();
        remove.clear();
        for(Field flower : beet){
          if (flower.getColor()!=f.getColor()){
            remove.add(flower);
          }
        }

        //beet -> nachbarn von f wird hier addiert
        beet.removeAll(remove);
          // System.out.println("B"+beet);

     //checked -> schon gepruefte(nachbar)/addierte Felder
        checked.add(f);
          // System.out.println("C"+checked);
    //help -> neu addierte Felder(nachbar); wird geprueft ob weitere nachbarn gibt
        HashSet<Field>help = new HashSet<Field>();
        help.addAll(beet);
        help.removeAll(checked);
          // System.out.println("H"+help);
        if(help.size()!=0){
          for(Field flower : help){
            updateBs(flower);
          }
        }
      }
    }
  }// END UPDATEBS
//======================================================================
/**
 * Feld mit Position(p1,p2,p3) wird geprueft und addiert(falls gueltig)
 * @author Yufan Dong
 * @param list der Liste wird hinzufugt
 * @param p1   Position1
 * @param p2   Position2
 * @param p3   Position3
 */
  private void ckadField(Collection<Field> list ,Position p1,Position p2,Position p3){
    if((p1!=p2)&&(p2!=p3)&&(p3!=p1)){
      Position[] p = new Position[]{p1,p2,p3};
      boolean onBoard = true;
      for(int i = 0; i<3; i++){
        int a = p[i].getColumn();
        int b = p[i].getRow();
        if(a<0 || b<0 ||(a+b)>size+2){
          onBoard = false;
        }
      }
      if(onBoard){
        Flower fl = new Flower(p1,p2,p3);
        for(Field f:fieldset)
          if(f.equals(fl))
            list.add(f);
      }
    }
  }//END CKADFIELD
//============================================================================
  /**
   * Methode, die die Menge aller Blumen eines Spielers als Hashset zurueckgibt
   * @param  color Spieler(Farbe)
   * @return       Menge aller Blumen eines Spielers
   */
  public Collection<Flower> getFlowers(final PlayerColor color) {
    if(color == PlayerColor.Red) {
      return redflowerset;
    }
    else {
      return blueflowerset;
    }
  }//END GETFLOWERS
  //============================================================================
  /**
   * Methode, die die Menge aller Graeben eines Spielers als Hashset zurueckgibt
   * @param  color Spieler(Farbe)
   * @return       Menge aller Graeben eines Spielers
   */
  public Collection<Ditch> getDitches(final PlayerColor color) {
    if(color == PlayerColor.Red) {
      return redditchset;
    }
    else {
      return blueditchset;
    }
  }//END GETDITCHES
  //============================================================================
  /**
   * Hilfsmethode fuer make, die Zuege abhandelt, die Blumen setzen
   * @param  move            ein Zug(f1,f2)
   * @param  playerflowerset das Set von Blumen
   * @return                 Ob ein Zug gueltig ist.
   */
  private boolean flowerMove (Move move, HashSet<Flower> playerflowerset) {

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
        if(isFlowerMoveLegal(playerflowerset));/*dieser aufruf von isFlowerMoveLegal ist nur hier, um die korrekten
         clusteramounts in die felder zu speichern, da diese benoetigt werden*/
        return false;
      }
    //return true;
  }//END FLOWERMOVE
  //============================================================================
  /**
   * Hilfsmethode fuer flowerMove, die die korrekte Position ueberprueft.
   * @param  flower gepruefte Blume
   * @return        Ob die Blume die korrekte Position hat.
   */
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
  /**
   * Hilfsmethode fuer checkFlowerPositions, die ueberprueft, ob eine Position
   * auch wirklich auf dem Brett liegt
   * @param  pos  eine Position
   * @param  size die Groesse des Bretts
   * @return      ob die Position auf Brett ist
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
  /**
   * Hilfsmethode fuer flowerMove. Ueberprueft teilw. ueber andere Methoden
   * die Gueltigkeit eines Zuges, der Blumen beinhaltet.
   * @param  playerflowerset das Set von Blumen
   * @return                 Ob das Set gueltig ist
   */
  private boolean isFlowerMoveLegal(HashSet<Flower> playerflowerset) {

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

    for(Field f : gardenset) {    //hier wird dann ueber alle felder die in einem...
      illegalColoring(f);         //...Garten sind, iteriert, damit diese die Felder um sich herum...
    }                             //nach der Gartenabstandsregel als ungueltig markieren
    for(Field f : fieldset) {
      for(Flower flower: playerflowerset) {
        if(f.equals(flower) && (f.getMark() == -1)) { //hier wird das Ganze ueberprueft:...
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
   * es setzt, wieder zu entfernen, da diese immer nur für einen bestimmten Zug
   * korrekt sind.
   */
  private void cleanUpMarks () {

    for(Field f : fieldset) {
      if(f.getMark() != -2) {//alle Markierungen ausser die Ditch-markierung, da diese global fuer rot & blau gilt
        f.setMark(0);
      }
      if(f.getCheck() == true) {
        f.setCheck(false);
      }
    }

  }//END CLEANUPMARKS
  //============================================================================
  /**
   * Hilfsmethode fuer isFlowerMoveLegal, die dafuer sorgt, dass Beete/Gaerten sich selbst
   * und ihre Nachbarn in den zusaetzlichen Farben, basierend auf den Regeln
   * unter "Hilfestellungen zur Implementierung", markieren.
   * @param field Feld
   * @param mark  makieren
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
  /**
   * Hilfsmethode fuer isFlowerMoveLegal, die Nachbarfelder von Gaerten markiert,
   * sodass diese nicht bebaut werden duerfen.
   * @param field Feld
   */
  private void illegalColoring(Field field) {

    int mark = field.getMark();

    //vv wenn ein rechtes feld existiert und nicht dieselbe farbe hat wie es selbst... vv
    if(field.getRight() != null) {
      if(field.getRight().getMark() != mark) {
        /*^^ in separaten klauseln, damit wenns in einer ist,
        nicht rumgeheult wird, wenn right = null ist*/
        field.getRight().setMark(-1);
      }
      if(field.getRight().getVertical() != null) {
        if(field.getRight().getVertical().getMark() != mark) {
          field.getRight().getVertical().setMark(-1);
        }//...dann bekommt es die extra markierung -1, die aussagt, dass es nicht...
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
  /**
   * Methode, die die Verarbeitung von Zuegen uebernimmt,
   * die einen Graben setzen
   * @param  move            ein Zug(ditch)
   * @param  playerditchset  das Set von Ditch
   * @param  playerflowerset das Set von Flower
   * @return                 ob der Zug gueltig ist
   */
  private boolean ditchMove (Move move, HashSet<Ditch> playerditchset, HashSet<Flower> playerflowerset) {

    Ditch ditch = move.getDitch();

    int flowerconnectioncamount = 0; //um herauszufinden, ob auch wirklich 2 blumen verbunden sind, und nicht leere felder an einem Ende sind
    Position pos1 = ditch.getFirst();
    Position pos2 = ditch.getSecond();

    if(checkDitchPositions(ditch)) {//Legalitaet gecheckt!
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
        if(isNeighborEmpty(ditch)) {//FELDER WERDEN GLEICHZEITIG UNFRUCHTBAR GEMACHT!
          playerditchset.add(ditch);
          return true;
        }
      }
    }
    return false;

  }//END DITCHMOVE
  //============================================================================
  /**
   * Hilfsmethode fuer ditchMove. Ueberprueft, ob eine Blume eine Position
   * mit dem Graben gemeinsam hat, und somit mit ihm verbunden ist.
   * @param  ditch  Ditch
   * @param  flower Flower
   * @return        Ob Graben und Blume zusammenliegen.
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
  /**
   * Hilfsmethode fuer ditchMove, die die Gueltigkeit der Position eines ueber-
   * gebenen Graben ueberprueft.
   * @param  ditch DITCH
   * @return       ob Ditch eine gueltige Position hat
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
  /**
   * Hilfsmethode fuer ditchMove. Ueberprueft, ob die angrenzenden Felder
   * unbebaut sind. Und markiert die Felder als unfruchtbar
   * @param  ditch Ditch
   * @return       Ob es Nachbarn(Blumen) gibt
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
  /**
   * Hilfsmethode fuer isNeighborEmpty. Gibt die an einen Graben angrenzenden
   * Felder zurueck.
   * @param  ditch Ditch
   * @return       Die an einen Graben angrenzenden Felder
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
  /**
   * Methode, die die Punktzahl des uebergebenen Spielers zurueckgibt
   * @param  color Spieler(Farbe)
   * @return       Punkte
   */
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
    int mark = 1;
    //vv Alle flowers kriegen eine graue Faerbung vv
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
    for(Field f : fieldset) {
      if(f.getMark() == 1) {
        additionalColoring(f, ++mark);
      }
    }//bisher alle flowercluster in versch. farben makiert

    for(Field f : gardenset) {
      points += p(countingN(f, playerditchset, color));
    }
    cleanUpMarks();
    return points;
  }//END GETPOINTS
  //============================================================================
  /**
   * Hilfsmethode fuer getPoints. Berechnet das n für die p-Funktion.
   * Alle zusammenhängenden Gaerten einer Farbe werden abgeklappert bis es keine
   * mehr gibt, dann wird n zurueckgegeben.
   * @param  field          Das Feld auf das countingN aufgerufen wird.
   * @param  playerditchset Set von Graeben des Spielers.
   * @param  color          Farbe, dessen n berechnet werden soll.
   * @return                n
   */
  private int countingN (Field field, HashSet<Ditch> playerditchset, PlayerColor color) {
    //NUR MIT GARDENSET SACHEN AUFRUFEN!
    int anzahl = 0;

    if(field.getClusteramount() == 0) {
      return anzahl;
    }/*in der mit X gekennzeichneten Foreach-Schleife werden alle felder aufgerufen, die mit einem Ditch
    verbunden sind. D.h. auch leere Felder. Damit keine Schleifen und/ oder dopplungen
    entstehen, wird hier dann erstmal grundsätzlich 0 zurückgegeben.
    Es darf aber nicht ... != 4 sein, da sonst beete sofort returnen und nicht
    weiter Gräben folgen usw.*/

    if(!field.getCheck()) {//damit keine dopplungen entstehen
      HashSet<Ditch> tobecheckedset = new HashSet<Ditch>();   //set an zu checkenden Gräben
      HashSet<Ditch> checkedoffset = new HashSet<Ditch>();    //um Fehlern bei .remove vorzubeugen ist hier stattdessen ein extra Set
      HashSet<Field> tobecheckedfields = new HashSet<Field>();//set das die Felder auf der anderen Seite eines Graben enthält.

      checkYourGarden(field, tobecheckedset, playerditchset);

      for(Ditch ditch : tobecheckedset) {
        if(!checkedoffset.contains(ditch)) {
          findFields(ditch, tobecheckedfields, color);
          checkedoffset.add(ditch);
        }
      }
      for(Field f : tobecheckedfields) { // X
        anzahl += countingN(f, playerditchset, color);
      }// X

      if(field.getClusteramount() == 4) {
        anzahl++;
      }
    }

    return anzahl;
  }//END COUNTINGN
  //============================================================================
  /**
   * Hilfsmethode fuer countingN. Alle Felder in demselben Garten wie das auf-
   * rufende Feld 'sammeln' alle Graeben ein und checken sich selbst ab, sodass
   * sie nicht noch einmal aufgerufen werden koennen.
   * @param field          Feld auf dem checkYourGarden aufgerufen wird.
   * @param tobecheckedset Set an zu checkenden Graeben
   * @param playerditchset Set von Graeben des Spielers.
   */
  private void checkYourGarden (Field field, HashSet<Ditch> tobecheckedset, HashSet<Ditch> playerditchset) {

    field.setCheck(true);
    if(field.getRight() != null) {
      if(field.getRight().getMark() == field.getMark()) {//< hierfuer brauch ich die Faerbung aus getPoints!
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
  /**
   * Hilfsmethode fuer getPoints. Berechnet die Punkte eines Garten
   * auf Basis der in den Regeln festgelegten Weise
   * @param  n Anzahl der verbundenen Garten
   * @return   Punkte
   */
  private int p (int n) {
    if(n > 1) {
      return (p(n-1) + n);
    }
    else if(n == 1) {
      return 1;
    }
    else {
      return 0;
    }
  }//END P
  //============================================================================
  /**
   * Hilfsmethode fuer countingN. addiert alle Felder an dem
   * ueber gebenen Graben ins tobecheckedfields-set, wenn diese noch
   * nicht checkYourGarden aufriefen.
   * @param ditch             gegebener Graben
   * @param tobecheckedfields Set das die Felder auf der anderen Seite eines Graben enthält.
   */
  private void findFields (Ditch ditch, HashSet<Field> tobecheckedfields, PlayerColor color) {

    /*Hier kommen alle Felder rein, die auf der anderen Seite liegen.
      Denn Felder , die auf der anderen Seite liegen, muessen keine Nachbarn sein.
      Es koennen genauso gut Blumenbeete sein, die ueber Eck zsmliegen.
      Wenn also der countingN aufruf auf NUR EIN Feld ginge, wuerden nicht
      alle verbindungen ueberprueft. Trotzdem verhindert countingN mehrfache
      Aufrufe auf denselben Ditch!*/

      for(Field f : fieldset) {
        if(f.getCheck() == false) {
          if(isDitchConnectedToFlower(ditch, f) && (f.getColor() == color)) {
            tobecheckedfields.add(f);
          }
        }
      }

  }//END FINDFIELDS
  //============================================================================
}//END CLASS
