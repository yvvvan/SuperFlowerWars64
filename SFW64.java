package app.superflowerwars64;

import app.player.*;
import app.gameboard.*;
import flowerwarspp.preset.*;
import java.net.*;
import java.rmi.*;


public class SFW64 {

  private SWRBoard board;
  private Player player1;
  private Player player2;
  private Requestable eingabe;

  private ArgumentParser ArgsParser;

	private GameboardGUI gui;
  private MyViewer viewer;
  private int SIZE;

        // private Requestable myeingabe1;
        // private Requestable myeingabe2;
  	    // private Output output1;
  	    // private Output output2;
  	    //
        // private Eingabe eingabe1;
        // private Eingabe eingabe2;

  public static void main (String[] args) throws ArgumentParserException{
    SFW64 flowerwar=new SFW64();
    flowerwar.init(args);
    flowerwar.operate();

  }

  public void operate(){
    Move move = null;
    Status status = Status.Ok;
    viewer = board.viewer();
    // gui = new GameboardGUI();
    // gui.setMyViewer(viewer);
    // gui.display(SIZE);
    // Flower f1 = new Flower(new Position(2,5), new Position(2,4), new Position(3,4));
    // Flower f2 = new Flower(new Position(2,4), new Position(3,4), new Position(3,3));
    // move = new Move(f1,f2);
    // System.out.println(move);

    try{
      while (status == Status.Ok){
        // System.out.println(board.getPossibleMoves().size());
        move = player1.request();
        board.make(move);
        status=board.getStatus();
        player1.confirm(status);
        player2.update(move,status);
        System.out.println("1"+move+status);


        if(status != Status.Ok) break;
        // System.out.println(board.getPossibleMoves().size());
        move=player2.request();
        board.make(move);
        status=board.getStatus();
        player2.confirm(status);
        player1.update(move,status);
        System.out.println("2"+move+status);


      }

    }catch(Exception e){
        System.out.println("Widersprüche zwischen dem Status des eigenen Spielbretts"+
                                        " und dem Status des Spielbretts des Hauptprogramms");
        e.printStackTrace();
    }
    System.out.println(status);
    System.out.println("Red:  "+board.getPoints(PlayerColor.Red));
    System.out.println("Blue: "+board.getPoints(PlayerColor.Blue));

  }

  public void init(String[] args) throws ArgumentParserException{ // args: spieler1 spierl2 groesse
    ArgsParser = new ArgumentParser(args);
    SIZE = ArgsParser.getSize();
    board = new SWRBoard (SIZE);
    setPlayer(eingabe,ArgsParser);
  }

  public void setPlayer(Requestable eingabe, ArgumentParser Args)throws ArgumentParserException {
    Player[] p = new Player[2];
    for(int i = 0; i < 2; i++){
      PlayerType type = (i == 0)?Args.getRed():Args.getBlue();
      switch(type){
        case HUMAN :
          p[i] = new Spieler(eingabe);
        break;
        case RANDOM_AI :
          p[i]= new Computerspieler();
        break;
        // case SIMPLE_AI: //RANDOM_AI_2
        //   p[i] = new Computerspieler();
        // break;
        case ADVANCED_AI_1 :
          p[i] = new AIspieler();
        break;
        // case ADVANCED_AI_2 :
        //   p[i] = new AIspieler();
        // break;
        case REMOTE :
          //// noch ueberlegen
        break;
        default :
          System.out.println("Unsupported player type: "  + type);
        break;
      }
      try{
        if(i==0)
          p[i].init(Args.getSize(),PlayerColor.Red);
        else
          p[i].init(Args.getSize(),PlayerColor.Blue);
      } catch (Exception e){
          System.out.println("Exception");
          e.printStackTrace();
      }
    }
    player1 = p[0];
    player2 = p[1];
  }

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

}//END SFW64



// public void initPlayer(SP,NET){
//   switch(SP){
//     case 1: Spieler         player = new Spieler(eingabe1);                     break;
//     case 2: Computerspieler player = new Computerpieler(eingabe1);              break;
//     case 3: AIspieler       player = new AIspieler(eingabe1);                   break;
//     case 4: Netzwerkspieler1 player= new Netzwerkspieler1(initPlayer(NET));    break;
//     default:System.out.println("Falsche eingabe1");  System.exit(0);   break;
//   }
// }
//


        // final int COLOR = Integer.parseInt(args[0]);
        //
        // final int SIZE = Integer.parseInt(args[1]);
        //
        // final int isLocal = Integer.parseInt(args[2]); //0->Online 1->Local
        //
        // final int isOffer;
        //
        // final int SP1;
        // final int SP2;
        //
        // if (isLocal == 0) {
        //   isOffer = Integer.parseInt(args[3]);
        //   if(isOffer == 0){ //0->Find 1->offer
        //     player1 = find("localhost","flowerwar");
        //     SP1 = Integer.parseInt(args[4]);
        //     switch (SP1){
        //       case 1: player2 = new Spieler(eingabe1);             break;
        //       case 2: player2 = new Computerspieler();      break;
        //       case 3: player2 = new AIspieler();           break;
        //     }
        //   }
        //   else {//isOffer == 1
        //     SP1 = Integer.parseInt(args[4]);
        //     switch (SP1){
        //       case 1: player1 = new Spieler(eingabe2);             break;
        //       case 2: player1 = new Computerspieler();      break;
        //       case 3: player1 = new AIspieler();           break;
        //     }
        //     try{Player p = new Netzwerkspieler1(player1);offer(p,"localhost","flowerwar");}
        //     catch(RemoteException e){
        //         System.out.println("RemoteException");
        //         e.printStackTrace();
        //     }
        //
        //   }
        // }
        // else  {
        //   SP1 = Integer.parseInt(args[3]);
        //   switch (SP1){
        //     case 1: player1 = new Spieler(eingabe1);             break;
        //     case 2: player1 = new Computerspieler();      break;
        //     case 3: player1 = new AIspieler();           break;
        //   }
        //   SP2 = Integer.parseInt(args[4]);
        //   switch (SP2){
        //     case 1: player2 = new Spieler(eingabe2);             break;
        //     case 2: player2 = new Computerspieler();      break;
        //     case 3: player2 = new AIspieler();           break;
        //   }
        // }
        // getColored(COLOR,SIZE,player1,player2);
        //
        // board = new SWRBoard(SIZE);
        //
        // if(isLocal==1){
        //   switch (SP1){
        //     case 1:
        //   }
        // }



    // public void getColored(int a, int size, Player p1, Player p2){
    //   try{  switch (a){
    //     case 0:
    //       p1.init(size, PlayerColor.Red);
    //       p2.init(size, PlayerColor.Blue);
    //       break;
    //     case 1:
    //       p2.init(size, PlayerColor.Red);
    //       p1.init(size, PlayerColor.Blue);
    //       break;
    //         }
    //   }catch(Exception e){
    //       System.out.println("Exception");
    //       e.printStackTrace();
    //   }
    // }
