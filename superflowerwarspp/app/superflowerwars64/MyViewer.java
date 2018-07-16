package app.superflowerwars64;
import flowerwarspp.preset.*;
import java.util.*;
import app.superflowerwars64.SWRBoard;

public class MyViewer implements Viewer {

       private SWRBoard gameboard;
       private PlayerColor turnColor;
       private Status state;

       public MyViewer(SWRBoard gameboard,
                        PlayerColor turnColor,
                        Status state) {
           this.gameboard = gameboard;
           this.turnColor = turnColor;
           this.state = state;
       }

       public PlayerColor getTurn() {
           return gameboard.getCurrentPlayer();
       }

       public int getSize() {
           return gameboard.getSize();
       }

       public Status getStatus() {
           return gameboard.getStatus();
       }

       public int getNeighborAmount(Flower flower) {
         return gameboard.getNeighborAmount(flower);
       }

       public Collection<Flower> getFlowers(final PlayerColor color) {
           return gameboard.getFlowers(color);
       }

       public Collection<Ditch> getDitches(final PlayerColor color) {
            return gameboard.getDitches(color);
       }

       public Collection<Move> getPossibleMoves() {
            return gameboard.getPossibleMoves();
       }

       public int getPoints(final PlayerColor color) {
            return gameboard.getPoints(color);
       }

}
