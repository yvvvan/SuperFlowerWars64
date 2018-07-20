package app.gameboard;
import flowerwarspp.preset.*;
import app.superflowerwars64.*;
import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;
import java.awt.image.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Float;
import java.awt.geom.Point2D;
import java.lang.*;

public class GUI implements Requestable, Output {

    private GameboardGUI gameboard;
    private MyViewer myViewer;
    //private static final long serialVersionUID = 1L;


    public GUI(int size /*MyViewer myViewer*/) {
        //this.myViewer = myViewer;
        JFrame f = new JFrame("SuperFlowerWars64");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(1280, 1080);
        gameboard = new GameboardGUI(f, size, this);
        //gameboard = new GameboardGUI(f,myViewer.getSize(), this);
        JPanel scores = new Scores();
        gameboard.add(scores);
        gameboard.add(new Buttons(gameboard));
        gameboard.setBackground(new Color(255,140,0));
        scores.setBackground(new Color(255,140,0));
        f.add(gameboard);
        f.setVisible(true);
    }

    @Override
    public synchronized Move request() {
       Collection<Move> posMoves = myViewer.getPossibleMoves();
       do {
           try{
             wait();
           } catch (Exception e) {
             e.printStackTrace();
           }
       } while (!posMoves.contains(gameboard.getNewMove()));
      return gameboard.getNewMove();
    }

    @Override
    public void display(Move lastMove) {
        gameboard.handleLastMove(lastMove);
    }

}
