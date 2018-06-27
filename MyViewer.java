package superflowerwars64;

import flowerwarspp.preset.*;
import java.util.*;
import superflowerwars64.SWRBoard;

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
           return turnColor;
       }

       public int getSize() {
           return gameboard.getSize();
       }

       public Status getStatus() {
           return state;
       }

       public int getNeighborAmount(Flower flower) {
         return gameboard.getNeighborAmount(flower);
       }

       public Collection<Flower> getFlowers(final PlayerColor color) {
           LinkedList<Flower> flowers = new LinkedList<>();
           return flowers;
       }

       public Collection<Ditch> getDitches(final PlayerColor color) {
            LinkedList<Ditch> ditches = new LinkedList<>();
            return ditches;
       }

       public Collection<Move> getPossibleMoves() {
            return gameboard.getPossibleMoves();
       }

       public int getPoints(final PlayerColor color) {
            return gameboard.getPoints(color);
       }

}
