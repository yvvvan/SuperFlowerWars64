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
 * eine Klasse NetzwerkPlayer um Spieler Object zu erzeugen
 */
public class Netzwerkspieler1 extends UnicastRemoteObject implements Player{
      //jede Netzwerk hat ein Reference von player;
      private Player player;

      /**
       * Ein Konstruktor von NetzwerkSpieler
       * @param  player          [Eine Reference von player]
       * @throws RemoteException [description]
       */
      public Netzwerkspieler1(Player player) throws RemoteException{
        this.player=player;
      }
      /**
       * funktioniert gleich wie andere Spieler Klasse
       * Fordert vom Spieler einen Zug an.
       * Für den Rückgabewert werden nur Objekt von Klassen dem Package flowerwarspp.preset verwendet.
       * D.h. es wird ein Move-Objekt rückgeliefert, dass selbst nur Referenzen auf Objekte
       * der Klassen Flower oder Ditch enthält, die ihrerseits nur Referenzen
       * auf Objekte der Klasse Position enthalten.]
       * @return [eine return Move]
       * @throws Exception       [description]
       * @throws RemoteException [description]
       */
      public Move request() throws Exception, RemoteException{
        return player.request();
      }

      /**
       * [Übergibt dem Spieler im Parameter status Informationen
       * über den letzten mit request vom Spieler gelieferten Zug.]
       * @param  status          [checken die Richtigkeit von Status]
       * @throws Exception       [description]
       * @throws RemoteException [description]
       */
      public void confirm(Status status) throws Exception, RemoteException{
        player.confirm(status);
      }
      /**
       * [Liefert dem Spieler im Parameter opponentMove den letzten Zug des Gegners
       * und im Parameter status Informationen über diesen Zug.]
       * @param  opponentMove    [Move von gegenner]
       * @param  status          [Status von gegener]
       * @throws Exception       [description]
       * @throws RemoteException [description]
       */
      public void update(Move opponentMove, Status status) throws Exception, RemoteException{
        this.player.update(opponentMove,status);

      }
      public void init(int boardSize, PlayerColor color) throws Exception, RemoteException{
        this.player.init(boardSize,color);
      }


}
