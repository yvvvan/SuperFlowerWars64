package flowerwarspp.player;

import flowerwarspp.preset.*;
import superflowerwars64.*;
import java.util.*;
import java.rmi.*;



/**
 * eine Klasse myPlayer um Spieler Object zu erzeugen
 * Interaktive Spieler Klasse
 */
public class Computerspieler implements Player{
// --Attribute------------------------------------------------------------------
    /*jeder Spieler hat einen eignen Brett*/
    private SWRBoard board;

    /*jeder Spieler hat eigne Farbe, Red or Blue*/
    private PlayerColor mycolor;
    //jede Spieler hat ein checknumber um die Reinfolge zu checken
    public int check=0;
    /**
     * ein Konstruktor
     */
    public Computerspieler (){
      super();
    }
// ------------------------------------------------------------------------------

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
      if(check==1){
        MyViewer myviewer=new MyViewer(this.board,this.mycolor,this.board.getStatus());//ich brauche ein Viewer von my selbst Board
        ArrayList<Move> possiblemove=new ArrayList<Move>(myviewer.getPossibleMoves());// mit Hilfe von myVierwer kann ich die possible moves bekommen
        Move surrender=new Move(MoveType.Surrender);
        possiblemove.remove(surrender);
        int listsize=possiblemove.size();
        Random random=new Random();
        int nextmove=random.nextInt(listsize);//bekomme ich ein random index von possiblemoves
        Move myMove = possiblemove.get(nextmove);//wechsele ich random index zu move
        board.make(myMove);
        check=2;
        return myMove;
      }else{
        throw new Exception("Es ist nicht in richtig Reihenfolge!");
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
          throw new Exception("Eigener Board-Status ist nicht mit Spiel-Board-Status identisch!");
        }
        check=3;
      }else{
        throw new Exception("Es ist nicht in richtig Reihenfolge!");
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
      if(status == Status.Ok){
        board.make(opponentMove);
        Status mystatus=board.getStatus();
        if(status!=mystatus){
          throw new Exception("Eigener Board-Status ist nicht mit Spiel-Board-Status identisch!");
        }
        check=1;
      }else{
        throw new Exception("Es ist nicht in richtig Reihenfolge!");
      }

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
         check=1;
      }else{
        throw new Exception("Es ist nicht in richtige Reihenfolge!");
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
