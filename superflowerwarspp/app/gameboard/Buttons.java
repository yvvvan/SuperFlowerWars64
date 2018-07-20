package app.gameboard;
import flowerwarspp.preset.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.*;

/**
    * Die Klasse Buttons ist für das Erzeugen zweier Buttons und für die Bearbeitung
    * der Ereignisse dieser Buttons
    * @author Viktoriya Pak
*/

public class Buttons extends JPanel {

    private GameboardGUI gGUI;
    /**
        * Ein Zug vom Typ {@link MoveType#Surrender}
    */
    private Move giveUpMove;
    /**
        * Ein Zug vom Typ {@link MoveType#End}
    */
    private Move endGameMove;

    /**
        * Konstruktor der Klasse erzeugt zweu Buttons und enthält
        * {@link ActionListener} für jeden Button. Wenn ein Button
        * angecklickt wird, werdern die entsprechende Züge erzeugt.
    */
    public Buttons(GameboardGUI gGUI) {
        this.gGUI = gGUI;
        setBackground(new Color(255,140,0));
        setLayout(new BorderLayout());
        JButton giveUpButton = new JButton("Give up");
        try {
            Image img = ImageIO.read(new File("aufgeben.png"));
            giveUpButton.setIcon(new ImageIcon(img));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        giveUpButton.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
               System.out.println("Give up");
               MoveType s = MoveType.Surrender;
               giveUpMove = new Move(s);
               gGUI.handleLastMove(giveUpMove);
           }
        });
		giveUpButton.setToolTipText("You can give up if ....");
        JButton end = new JButton("End game");
        end.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("End Game");
                MoveType s = MoveType.End;
                endGameMove = new Move(s);
                gGUI.handleLastMove(endGameMove);

			}
		});
		end.setToolTipText("You can end game every time you want. The game will be then stopped and points will be computed");
        add(giveUpButton, BorderLayout.NORTH);
        add(Box.createRigidArea(new Dimension(5,5)), BorderLayout.CENTER);
        add(end, BorderLayout.SOUTH);
    }

    /**
        * Getter-Methode für {@link #giveUpMove}
    */

    public Move getGiveUpMove() {
        return giveUpMove;
    }

    /**
        * Getter-Methode für {@link #endGameMove}
    */

    public Move getEndGameMove() {
        return endGameMove;
    }
}
