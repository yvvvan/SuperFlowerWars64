package app.gameboard;
import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;
import java.awt.image.*;
import java.awt.event.*;

// Для обновления очков добавить их значения в качестве параметров конструктора ??

public class Scores extends JPanel {

    private int scoreRed = 14;
    private int scoreBlue = 1;

    public void setScoreRed(int sc) {
        scoreRed = sc;
    }
    public void setScoreBlue (int sc) {
        scoreBlue = sc;
    }

    public Scores() {

        try {
            BufferedImage redPic = ImageIO.read(new File("red.png"));
            BufferedImage bluePic = ImageIO.read(new File("blue.png"));
            JLabel redPicLabel = new JLabel(new ImageIcon(redPic));
            JLabel bluePicLabel = new JLabel(new ImageIcon(bluePic));
            //DrawScores sc = new DrawScores();
            Graphics gr = redPic.getGraphics();
            gr.setColor(Color.WHITE);
            gr.setFont(new Font("Arial Black", Font.BOLD, 60));
            if (scoreRed > 9)
                gr.drawString(String.valueOf(scoreRed), 15, 85);
            else
                gr.drawString(String.valueOf(scoreRed), 40, 85);
            gr = bluePic.getGraphics();
            gr.setColor(Color.WHITE);
            gr.setFont(new Font("Arial Black", Font.BOLD, 60));
            if (scoreBlue > 9)
                gr.drawString(String.valueOf(scoreBlue), 15, 85);
            else
                gr.drawString(String.valueOf(scoreBlue), 40, 85);
            add(redPicLabel);
            add(bluePicLabel);
            //add(Box.createRigidArea(new Dimension(5,5)));
            //add(new Buttons(), BorderLayout.EAST);

        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
