package flowerwarspp.spieler;


import flowerwarspp.preset.*;
import flowerwarspp.spielbrett.*;
import java.rmi.RemoteException;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.net.*;

public class Spieler extends UnicastRemoteObject implements Player{
	public Spielbrett spielbrett;
	public PlayerColor playcolor;
	public Status status;
	private Player player;


	public Spieler(Player player) throws RemoteException{
		this.player =player;
	}


	
	public void init(int boardSize, PlayerColor color) throws Exception, RemoteException{
		spielbrett	=	new Spielbrett(boardSize);
		playcolor	=	color;


	}


	public Move request() throws Exception, RemoteException{
		return player.request();
	}


	public void confirm(Status status) throws Exception, RemoteException{
		player.confirm(status);
	}


	public void update(Move opponentMove, Status status) throws Exception, RemoteException{
		player.update(opponentMove,status);
	}

	
	



}
