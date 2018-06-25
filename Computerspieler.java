package flowerwarspp.player;
import flowerwarspp.preset.Status;
import flowerwarspp.preset.Move;
import flowerwarspp.preset.*;
import flowerwarspp.myBoard;
import flowerwarspp.preset.MoveType;
import java.util.*;
import java.rmi.*;
import java.rmi.RemoteException;
/**
 * eine Klasse myPlayer um Spieler Object zu erzeugen
 * Interaktive Spieler Klasse
 */
public class Computerspieler implements Player,Runnable{ //S.137
// --Attribute------------------------------------------------------------------
    /*jeder Spieler hat einen eignen Brett*/
    private myBoard board;

    /*jeder Spieler hat einen Partner-Spieler*/
    private myPlayer partner;

    /*jeder Spieler hat eigne Farbe, Red or Blue*/
    private PlayerColor color;

    /*jeder Zug wird gespeichert*/
    private Move myMove;

// ------------------------------------------------------------------------------


// -----------------------------------------------------------------------------
    /**
     * Um Thread zu realisieren brauchen wir ein synchronized run Methode
     * sodass jedezeit kann nur ein Thread funktionieren.
     */
    synchronized public void run () {

      while(board.getStatus() == Status.Ok || board.getStatus() == Status.Illegal){
      switch(color){
        case Red:
          request();
          while(board.getStatus() != Status.Ok){
            System.out.println("Illegal Move");
            request();
          }
          partner.sendNotify();
          confirm(board.getStatus());
          partner.sendNotify();
          update(partner.getMyMove(),partner.getBoard().getStatus());
          partner.sendNotify();
        break;
        case Blue:
          update(partner.getMyMove(),partner.getBoard().getStatus());
          partner.sendNotify();
          request();
          while(board.getStatus() != Status.Ok){
            System.out.println("Illegal Move");
            request();
          }
          partner.sendNotify();
          confirm(board.getStatus());
          partner.sendNotify();
        break;
      }
      }
    }

      synchronized public void sendNotify () {
        notify ();
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
      try {
      //  myMove = eingabe.request(); ############
        int size =board.getSize();
        int size2=size+1;
        Random random=new Random();
        int flowerorditch=random.nextInt(2);// wenn flowerorditch 0 ist, ist Move Ditch .Amsonsten ist 2 flower
        int colum=random.nextInt(size2)+1;//
        int row=random.nextInt(size2)+1;
        Position position1=new Position(colum,row);// ich brauche ein anfangPosition am Anfang ich erklaere das am untern
        Posotion position2=new Position();
        Posotion position3=new Position();
        int colum2=random.nextInt(size2)+1;//wenn 2 Flower ich habe, brauche ich 2 AnfangPosition
        int row2=random.nextInt(size2)+1;
        Posotion position4=new Position(colum2,row2);
        Posotion position5=new Position();
        Posotion position6=new Position();
        if(flowerorditch==0){
          int direction=random.nextInt(3);//Ditch hat shon ein anfangPosition,aber braucht Ditch noch ein end Position .wir haben 3 Richtung
          if(direction==0){ // end Position am links
            position2=new Position(colum-1,row);
          }
          else if(direction==1){//end Position am links oben
            position2=new Position(colum-1,row+1);
          }
          else if(direction==2){//end Position am recht oben
            position2=new Position(colum,row+1);
          }
          Ditch ditch=new Ditch(position1,position2);
          this.myMove = new Move(ditch);
          board.make(myMove);
          return this.myMove;
        }else{
          int direction1=random.nextInt(2);//Flower1 hat shon ein anfangPosition,aber braucht Ditch noch ein end Position .wir haben 2Richtung
          if(direction==0){ // end Position am oben
            position2=new Position(colum-1,row+1);
            position3=new Position(colum,row+1);
          }
          else if(direction==1){//end Position am utern
            position2=new Position(colum,row-1);
            position3=new Position(colum+1,row-1);
          }
          Flower flower1=new Flower(position1,position2,position3);


          int direction2=random.nextInt(2);//Flower2 hat shon ein anfangPosition,aber braucht Ditch noch ein end Position .wir haben 2Richtung
          if(direction==0){ // end Position am oben
            position5=new Position(colum-1,row+1);
            position6=new Position(colum,row+1);
          }
          else if(direction==1){//end Position am utern
            position5=new Position(colum,row-1);
            position6=new Position(colum+1,row-1);
          }
          Flower flower2=new Flower(position4,position5,position6);
          this.myMove=Move(flower1,flower2);
          board.make(myMove);
          return this.myMove;
        }

      }catch (RemoteException e){
        throw new RemoteException("bei der Netzwerkkommunikation ist etwas schief gelaufen",e);
      }
    }

    /**
     * [Übergibt dem Spieler im Parameter status Informationen
     * über den letzten mit request vom Spieler gelieferten Zug.]
     * @param status [checken die Richtigkeit von Status]
     */
    public void confirm(Status status)throws Exception, RemoteException{
      try{
        try{
          if(status == board.getStatus()){
            switch(status){
              case Ok:
                board.make(myMove);
                break;
              case Illegal:
                board.setStatus(Status.Illegal);
               break;
            }
          }
        }catch (Exception e){
          throw new Exception("Spielbretter ist nicht uebereinstimmen oder nicht die Funktion",e);
        }
      }catch (RemoteException e){
        throw new RemoteException("bei der Netzwerkkommunikation ist etwas schief gelaufen",e);
      }

    }

    /*eingabe : opponent Move + status des letzen Zuges*/
    /**
     * [Liefert dem Spieler im Parameter opponentMove den letzten Zug des Gegners
     * und im Parameter status Informationen über diesen Zug.]
     * @param opponentMove [opponent move]
     * @param status       [Status des letzten Zuges]
     */
    public void update(Move opponentMove, Status status) throws Exception, RemoteException{
      try{
      if(status == partner.board.getStatus()){
        switch(status){
          case Ok:
            partner.board.make(opponentMove);
            break;
          case Illegal:
            partner.board.setStatus(Status.Illegal);
            break;
        }
      }
    }catch (RemoteException e){
      throw new RemoteException("bei der Netzwerkkommunikation ist etwas schief gelaufen",e);
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
      try{
        try{
         this.board = new myBoard(boardSize);
          this.color = color;
          this.parter=new myPlayer();
        }catch(Exception e){
          throw new Exception("Es ist nicht möglich eine Spieler zu initialisieren",e);
        }
      }catch(RemoteException e){
        throw new RemoteException("bei der Netzwerkkommunikation ist etwas schief gelaufen ",e);
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
	* Returns value of partner
	* @return Partner Spieler von diese Object
	*/
	public myPlayer getPartner() {
		return partner;
	}

	/**
	* Sets new value of partner
	* @param myPlayer partner Spieler
	*/
	public void setPartner(myPlayer partner) {
		this.partner = partner;
	}

	/**
	* Returns value of color
	* @return  PlayerColor von diese Object Spieler
	*/
	public PlayerColor getColor() {
		return color;
	}

	/**
	* Sets new value of color
	* @param PlayerColor
	*
	*/
	public void setColor(PlayerColor color) {
		this.color = color;
	}

	/**
	* Returns value of myMove
	* @return  den letzten Move
	*/
	public Move getMyMove() {
		return myMove;
	}

	/**
	* Sets new value of myMove
	* @param Move
	*/
	public void setMyMove(Move myMove) {
		this.myMove = myMove;
	}
}
