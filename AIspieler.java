package app.player;
import flowerwarspp.preset.*;
import java.rmi.*;
import app.superflowerwars64.*;
import java.util.*;



/**
 * eine Klasse myPlayer um Spieler Object zu erzeugen
 * Interaktive Spieler Klasse
 */
public class AIspieler implements Player {
// --Attribute------------------------------------------------------------------
    /*jeder Spieler hat einen eignen Brett*/
    private SWRBoard board;
    /*jeder Spieler hat eigne Farbe, Red or Blue*/
    private PlayerColor mycolor;
    //jede Spieler hat ein checknumber um Reihenfolge zu checken
    public int check=0;
// ------------------------------------------------------------------------------
    /**
     * ein Konstruktor
     */
    public AIspieler (){
        super();
    }
// --request -> confirm -> update ----------------------------------------------
    /**
     * [Fordert vom Spieler einen Zug an.
     * Für den Rückgabewert werden nur Objekt von Klassen dem Package flowerwarspp.preset verwendet.
     * D.h. es wird ein Move-Objekt rückgeliefert, dass selbst nur Referenzen auf Objekte
     * der Klassen Flower oder Ditch enthält, die ihrerseits nur Referenzen
     * auf Objekte der Klasse Position enthalten.]
     * @return [Move]
     */
    public Move request() throws Exception, RemoteException{
      // System.out.println("hererer");
      if(check==1){
        //bekomme ich ein Viewer von diese board
        MyViewer myviewer=new MyViewer(this.board,this.mycolor,this.board.getStatus());
        //mit Hilfe von diese Viewer kann ich die possiblemoves bekommen
        ArrayList<Move> possiblemove=new ArrayList<Move>(myviewer.getPossibleMovesFl());/*改正gPM返回所有可能Move，如果是Ditch等则getFF报错*/
        // System.out.println(possiblemove);
        int listsize=possiblemove.size();// bekomme ich die size von possiblemoves
        if(listsize>0){/*改正 最终情况为 无Flower，End，Surr，可能有Ditch，gPMFl只返回花，所以最终可能为空*/
        ArrayList<Integer> score=new ArrayList();// ein Hilfecollectoin umzu speichern score von jede possible moves
        for (int i=0;i<listsize;i++){
          Move move=possiblemove.get(i);
          Flower flower1=move.getFirstFlower();
          Flower flower2=move.getSecondFlower();
          int n1=board.getNeighborAmount(flower1,mycolor);
          int n2=board.getNeighborAmount(flower2,mycolor);
          int thisscore=(n1 + 1) * (n2 + 1);
          if(isNeighbours(flower1,flower2)){
            thisscore=thisscore*2;
          }
          score.add(thisscore);//iteriere ich die possible moves .und rechne ich die score von jede possible moves aus
        }
        int themax=Collections.max(score);//das ist die grosst elelment in der score Collection
        int indexmax=0;// hier ist der index von grosst elelment in Collection
        for (int i=0;i<listsize;i++){
          if(score.get(i)==themax){
            indexmax=i;
            break;
          }else{
            i++;
          }
        }//iterire ich die Collection um die groost elelmt aus zufinden
        Move myMove=possiblemove.get(indexmax);//my move ist die grosst elelment
        board.make(myMove);
        check=2;
        return myMove;
      }else{
        board.make(new Move(MoveType.End));
        check=2;
        return new Move(MoveType.End);
      }
      }else{
        throw new Exception("Es ist nicht in richtig Reinfolge!5");
      }
    }
    /**
     * [isNeighbours ist ein HelgfeMethode . wenn beide Flower sind zusammen ,die score werden verdoppelt]
     * @param  flower1 [flower1 von move]
     * @param  flower2 [flower2 von move]
     * @return         [ob die 2 flower zusammen sind]
     */

    public boolean isNeighbours (Flower flower1,Flower flower2){
      Position pos1_1=flower1.getFirst();
      Position pos1_2=flower1.getSecond();
      Position pos1_3=flower1.getThird();
      ArrayList<Position> position=new ArrayList();
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
    }
    /**
     * [Übergibt dem Spieler im Parameter status Informationen
     * über den letzten mit request vom Spieler gelieferten Zug.]
     * @param status [checken die Richtigkeit von Status]
     */
    public void confirm(Status status) throws Exception, RemoteException{
      if(check==2){
        if(status != board.getStatus()){
          throw new Exception("Eigener Board-Status ist nicht mit Spiel-Board-Status identisch!4");
        }
        check=3;
      }else{
        throw new Exception("Es ist nicht in richtig Reinfolge!3");
      }
    }
    /*eingabe : opponent Move + status des letzen Zuges
    /**
     * [Liefert dem Spieler im Parameter opponentMove den letzten Zug des Gegners
     * und im Parameter status Informationen über diesen Zug.]
     * @param opponentMove [opponent move]
     * @param status       [Status des letzten Zuges]
     */
    public void update(Move opponentMove, Status status) throws Exception, RemoteException{
      if(check != 3) throw new Exception("Update: Es ist nicht in richtig Reinfolge! "); /*改正 二号蓝色玩家最先开始同步，此时刚init check为1*/
        // System.out.println("eee");
        if(status != Status.Illegal){ /*改正 Ok,Draw,Win都应该同步update*/
          board.make(opponentMove);
          Status mystatus=board.getStatus();
          if(mystatus!=status)
            throw new Exception("Update: Status Fehler MainBoard & thisBoard ");
          check=1;
        }else throw new Exception("Update: Illegal Status MainBoard");

    }
    /**
     * Initialisiert den Spieler, sodass mit diesem Spieler-Objekt ein neues Spiel
     * mit einem Spielbrett der Größe size und der durch den Para- meter color bestimmten Farbe, begonnen werden kann.
     *Die Spielerfarbe ist einer der beiden Werte der Enumeration flowerwarspp.preset.
     *PlayerColor und kann die Werte Red und Blue annehmen.
     * @param boardSize [Die Größe von Spielbrett]
     * @param color     [Die Farbe von diese Spieler Object ]
     */
    public void init(int boardSize, PlayerColor color) throws Exception, RemoteException{
      if(check==0){
         board = new SWRBoard(boardSize);
         mycolor = color;
         check=(color==PlayerColor.Red)?1:3;/*改正 二号蓝色玩家最先开始同步，此时刚init check为1*/
         // System.out.println(color+""+check);
      }else{
        throw new Exception("Es ist nicht in richtig Reinfolge!");
      }
    }

// --getter & setter------------------------------------------------------------


	/**
	* Returns value of board
	* @return board
	*/
	public SWRBoard getBoard() {
		return board;
	}

	/**
	* Sets new value of board
	* @param myBoard [eines neues Board
	*/
	public void setBoard(SWRBoard board) {
		this.board = board;
	}

	/**
	* Returns value of mycolor
	* @return  PlayerColor von diese Object Spieler
	*/
	public PlayerColor getColor() {
		return mycolor;
	}

	/**
	* Sets new value of color
	* @param PlayerColor
	*
	*/
	public void setColor(PlayerColor color) {
		this.mycolor = color;
	}

}
