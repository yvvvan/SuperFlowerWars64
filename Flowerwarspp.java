package flowerwarspp;

import flowerwarspp.player.*;
import flowerwarspp.preset.*;
import flowerwarspp.boarddisplay.*;
import java.rmi.registry.*;
import java.rmi.*;
import java.net.*;

public class Flowerwarspp{
  private myBoard board;
  private Computerspieler spieler1;
  private Spieler spieler2;

  public Flowerwarspp(){
    board=new myBoard(6);
    spieler1=new Computerspieler();
    spieler2=new Spieler();
  }
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------
//Hier ,die methode offer und find für RMI sind richtig .die anderes weis ich nict
  public void offer(Player p,String host, String name){
    try{
      Naming.rebind("rmi://" + host + "/" + name, p);
      System.out.println("Player (" + name +") ready");
    }catch(MalformedURLException ex){
      ex.printStackTrace();
    }catch(RemoteException ex){
      ex.printStackTrace();
    }
  }

  public Player find(String host,String name){
    Player p=null;
    try{
      p=(Player)Naming.lookup("rmi://" + host + "/" +name);
      System.out.println("Player (" + name + ") found");
    }catch(Exception ex){
      ex.printStackTrace();
    }
    return p;
  }

//------------------------------------------------------------------------------
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------
//Hier ist die operation methode. ich den das ist auch richtig fuer spieler 
  public void operate(){
    Move move=null;
    Status status =Status.Ok;
    try{
      while (status != Status.Illegal){
        move=spieler1.request();
        board.make(move);
        status=board.getStatus();
        spieler1.confirm(status);
        spieler2.update(move,status);
        move=spieler2.request();
        board.make(move);
        status=board.getStatus();
        spieler2.confirm(status);
        spieler1.update(move,status);
      }
    }catch(Exception e){
        System.out.println("Widersprüche zwischen dem Status des eigenen Spielbretts und dem Status des Spielbretts des Hauptprogramms");
        e.printStackTrace();
    }
  }

}
