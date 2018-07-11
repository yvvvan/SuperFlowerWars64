package app.gameboard;
import flowerwarspp.preset.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.*;

public class Buttons extends JPanel {
    private Move giveUpMove;
    private Move endGameMove;
    public Buttons() {
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
           }
        });
		giveUpButton.setToolTipText("You can give up if ....");
        JButton end = new JButton("End game");
        end.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("End Game");
                MoveType s = MoveType.End;
                endGameMove = new Move(s);

			}
		});
		end.setToolTipText("You can end game every time you want. The game will be then stopped and points will be computed");
        add(giveUpButton, BorderLayout.NORTH);
        add(Box.createRigidArea(new Dimension(5,5)), BorderLayout.CENTER);
        add(end, BorderLayout.SOUTH);

    }

    public Move getGiveUpMove() {
        return giveUpMove;
    }

    public Move getEndGameMove() {
        return endGameMove;
    }
}
