package flowerwarspp.player;
import flowerwarspp.preset.*;
import java.rmi.*;
import superflowerwars64.*;
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
      if(check==1){
        MyViewer myviewer=new MyViewer(this.board,this.mycolor,this.board.getStatus());//bekomme ich ein Viewer von diese board
        ArrayList<Move> possiblemove=new ArrayList<Move>(myviewer.getPossibleMoves());//mit Hilfe von diese Viewer kann ich die possiblemoves bekommen
        int listsize=possiblemove.size();// bekomme ich die size von possiblemoves
        ArrayList<Integer> score=new ArrayList();// ein Hilfecollectoin umzu speichern score von jede possible moves
        for (int i=0;i<listsize;i++){
          Move move=possiblemove.get(i);
          Flower flower1=move.getFirstFlower();
          Flower flower2=move.getSecondFlower();
          int n1=board.getNeighborAmount(flower1);
          int n2=board.getNeighborAmount(flower2);
          int thisscore=(n1 + 1) * (n2 + 1);
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
        throw new Exception("Es ist nicht in richtig Reinfolge!");
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
        throw new Exception("Es ist nicht in richtig Reinfolge!");
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
      if(check==3){
        if(status == Status.Ok){
          board.make(opponentMove);
          Status mystatus=board.getStatus();
          if(mystatus!=status){
            throw new Exception("Es ist nicht in richtig Reihenfolge!");
          }
        }
        check=1;
      }else{
        throw new Exception("Es ist nicht in richtig Reinfolge!");
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
