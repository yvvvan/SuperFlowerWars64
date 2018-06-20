package flowerwarspp;
import flowerwarspp.preset.Status;
import flowerwarspp.preset.*;
import java.util.*;
import java.rmi.*;


/**
 *
 */
public class myPlayer implements Player,Runnable { //S.137
// --Attribute------------------------------------------------------------------
    /*jeder Spieler hat einen eignen Brett*/
    private myBoard board;

    /*jeder Spieler hat einen Partner-Spieler*/
    private myPlayer partner = null;

    /*jeder Spieler hat eigne Farbe, Red or Blue*/
    private PlayerColor color;

    /*jeder Zug wird gespeichert*/
    private Move myMove;
// -----------------------------------------------------------------------------


// -----------------------------------------------------------------------------
    /**
     * [run description]
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

    /**
     * [sendNotify description]
     */
    synchronized public void sendNotify () {
      notify ();
    }
// -----------------------------------------------------------------------------

// --request -> confirm -> update ----------------------------------------------
    /**
     * [request description]
     * @return [description]
     */
    public Move request() /*throws Exception, RemoteException*/{
      //  myMove = eingabe.request(); ############
        board.make(myMove);
        return myMove;
    }

    /**
     * [confirm description]
     * @param status [description]
     */
    public void confirm(Status status) /*throws Exception, RemoteException*/{
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
    }

    /**
     * [update description]
     * @param opponentMove [description]
     * @param status       [description]
     */
    public void update(Move opponentMove, Status status) /*throws Exception, RemoteException*/{
      if(status == board.getStatus()){
        switch(status){
          case Ok:
            board.make(opponentMove);
            break;
          case Illegal:
            board.setStatus(Status.Illegal);
            break;
        }
      }
    }

    /**
     * [init description]
     * @param boardSize [description]
     * @param color     [description]
     */
    public void init(int boardSize, PlayerColor color) /*throws Exception, RemoteException*/{
      this.board = new myBoard(boardSize);
      this.color = color;
    }
// -----------------------------------------------------------------------------

// --getter & setter------------------------------------------------------------


	/**
	* Returns value of board
	* @return
	*/
	public myBoard getBoard() {
		return board;
	}

	/**
	* Sets new value of board
	* @param
	*/
	public void setBoard(myBoard board) {
		this.board = board;
	}

	/**
	* Returns value of partner
	* @return
	*/
	public myPlayer getPartner() {
		return partner;
	}

	/**
	* Sets new value of partner
	* @param
	*/
	public void setPartner(myPlayer partner) {
		this.partner = partner;
	}

	/**
	* Returns value of color
	* @return
	*/
	public PlayerColor getColor() {
		return color;
	}

	/**
	* Sets new value of color
	* @param
	*/
	public void setColor(PlayerColor color) {
		this.color = color;
	}

	/**
	* Returns value of myMove
	* @return
	*/
	public Move getMyMove() {
		return myMove;
	}

	/**
	* Sets new value of myMove
	* @param
	*/
	public void setMyMove(Move myMove) {
		this.myMove = myMove;
	}
}
