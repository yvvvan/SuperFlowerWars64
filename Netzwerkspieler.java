package flowerwarspp.player;
import flowerwarspp.preset.Status;
import flowerwarspp.myBoard;
import flowerwarspp.eingabe;
import flowerwarspp.preset.*;
import flowerwarspp.preset.Requestable;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
/**
 * eine Klasse myPlayer um Spieler Object zu erzeugen
 * Interaktive Spieler Klasse
 */
public class Netzwerkspieler extends UnicastRemoteObject implements Player{
      /**
       * Ein Konstruktor
       * @param  eingabe         [Ein Reference von Requestable]
       * @throws RemoteException [wenn es Problem gibt ,throw RemoteException]
       */
      public Netzwerkspieler(Requestable eingabe) throws RemoteException{
        this.myeingabe=eingabe;
      }

      private static final long serialVersionUID=1L;
      //Jede UnicastRemoteObject muss ein serialVersionUID hat um datei zu transportieren
  // --Attribute------------------------------------------------------------------
      /*jeder Spieler hat einen eignen Brett*/
      private myBoard board;

      /*jeder Spieler hat eigne Farbe, Red or Blue*/
      private PlayerColor mycolor;
      //jede Spiele hat ein Reference von Requestable
      private Requestable myeingabe;
      //jede Spieler hat ein checknumber um Reihenfolge zu checken
      public int check=0;
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
          Move myMove=null;
          myMove=this.myeingabe.request();
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
        if (check==3){
          if(status == Status.Ok){
            board.make(opponentMove);
            Status mystatus=board.getStatus();
            if(mystatus!=status){
              throw new Exception("Eigener Board-Status ist nicht mit Spiel-Board-Status identisch!");
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
           board = new myBoard(boardSize);
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
  	public myBoard getBoard() {
  		return board;
  	}

  	/**
  	* Sets new value of board
  	* @param myBoard [eines neues Board
  	*/
  	public void setBoard(myBoard board) {
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